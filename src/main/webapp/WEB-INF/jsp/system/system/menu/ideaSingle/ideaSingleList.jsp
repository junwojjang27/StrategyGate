<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/common-taglibs.jsp"%>
<validator:javascript formName="ideaSingleVO" staticJavascript="false" xhtml="true" cdata="false"/>
<script type="text/javascript">
	$(function(){
		$("#list").jqGrid({
			url			:	"${context_path}/system/system/menu/ideaSingle/ideaSingleList_json.do",
			postData	:	getFormData("form"),	//검색조건의 모든 요소가 검색이 된 후.(검색조건을 먼저 읽은 후에 검색)
			width		:	"${jqgrid_width}",
			height		:	"(count%10)*30",	//jqGrid크기를 제안 수에 맞게 변경
			colModel	:	[
				{name:"year",	index:"year",	width:30, hidden:true},
				{name:"ideaCd",	index:"ideaCd",	width:30, hidden:true},
				{name:"content",		index:"content",	hidden:true},
				{name:"category",	index:"category",	width:30,	align:"center",	label:"<spring:message code="word.category"/>"},
				{name:"title",	index:"title",	width:200,	align:"left",	label:"<spring:message code="word.title"/>",
					formatter:function(cellvalue, options, rowObject) {
						return "<a href='#' onclick='showDetail(\"" + removeNull(rowObject.ideaCd)+ "\");return false;'>" + escapeHTML(removeNull(cellvalue)) + "</a>";
						//escapeHTML뜻 : DB에 이거 없이 넣으면 digit가 나옴?....
					},//formatter : 색깔이나 액션을 주는 (데이터 가공) / 제목에 링크, 돋보기 표시 등
					unformat:linkUnformatter	//포메터로 포맷된 데이터가 저장이 될 떄 언포맷 된 상태에서 나가야 한다.. (순수 데이터만 데이터에 날라가기(포매터에서 가공된 데이터를 다시 본래의 데이터로 저장))
				},
				{name:"userId",		index:"userId",	hidden:true},
				{name:"userNm",	index:"userNm",	width:30,	align:"center",	label:"<spring:message code="word.insertUser"/>"},
				{name:"deptNm",	index:"deptNm",	width:30,	align:"center",	label:"<spring:message code="word.deptNm"/>"},
				{name:"state",		index:"state", width:30,	align:"center",	label:"진행상태"},
				{name:"createDt",	index:"creatDt",	width:50,	align:"center",	label:"<spring:message code="word.insertDT"/>"},
			],
			pager		: "pager",
			rowNum		: 10,
			sortname	: "createDt",
			sortorder	: "asc",
			multiselect	: true,
			loadComplete : function() {
				var count = $('#list').getGridParam('records'); //jqGrid크기 조절하기 위함. (서버에서 받은 실제 레코드 수)

				var cnt = 0;
				<sec:authorize access="hasRole('01')"> //관리자 여부
				cnt = 1;
				</sec:authorize>
				if(cnt != 1){	//관리자 아닐 경우 체크박스 자기꺼만 보이게
					var loginUserId = "${sessionScope.loginVO.userId}";
					hideGridCheckbox("list", "userId", loginUserId, false);
				}
			}
		});

		$("#findUseYn").on("change", function() {	//사용여부에 따라 삭제 버튼 보임여부
			if($(this).val() == "N"){
				$(".delete").hide();
			}else{
				$(".delete").show();
			}
		});

		$("#newForm").hide();

		$("#searchKeyword").keyup(function(e) {		//enter눌렀을 때 searchList()로 이동
			if(e.keyCode == 13) searchList();
		});
	});
	// 목록 조회
	function searchList() {
		$("#newForm").hide();
		reloadGrid("list", "form");
	}
	// 상세 조회
	function showDetail(ideaCd) {
		var f = document.form;
		f.ideaCd.value = ideaCd;

		var cnt = 0;

		<sec:authorize access="hasRole('01')">	//관리자 여부
		cnt = 1;
		</sec:authorize>

		if (cnt != 1) {		//관리자 아닐 경우
			var userId = "${sessionScope.loginVO.userId}";
			var num = $("#list").getGridParam("reccount");		//현재 화면에 표시되고있는 레코드 숫자
			var rowData;
			for (var n = 1; n <= num; n++) {	//1 ~ (화면에 표시되고있는 레코드 숫자) 까지 반복하여 ideaCd를 비교해서 해당 제안을 찾음.
				rowData = $("#list").jqGrid("getRowData", n);
				if(rowData.ideaCd == $("#ideaCd").val()) {
					if (rowData.userId == userId)
						$(".save").show();
					else
						$(".save").hide();
				}
			}
		}
		sendAjax({
			"url" : "${context_path}/system/system/menu/ideaSingle/selectDetail.do",
			"data" : getFormData("form"),
			"doneCallbackFunc" : "setDetail"
		});
	}
	// 상세 조회 값 세팅
	function setDetail(data) {
		$("#newForm").show();
		var dataVO = data.dataVO;
		voToForm(dataVO, "form", ["title","year","category"]);	//VO의 값을 form에 세팅
		$("#content").val(dataVO.content);
		$("#content").focus();
		showBytes("content", "contentBytes");
		setMaxLength("form");
	}

	var first;
	// 등록
	function addData() {

		$("#newForm").show();
		resetForm("form", ["category","title", "content"]);		//form에 있는 값 지우기
		$("#year").val($("#findYear").val());		//year은 화면 왼쪽 위 년도 로 세팅

		first = true;

		//byte
		showBytes("content", "contentBytes");
		setMaxLength("form");
	}
	// 저장
	function saveData() {
		var f = document.form;
		if(!validateIdeaSingleVO(f)) {	//유효성 체크
			return;
		}

		if (first != true) {
			isUse = false;
			var num = $("#list").getGridParam("reccount");
			var rowData;
			for (var n = 1; n <= num; n++) {
				rowData = $("#list").jqGrid("getRowData", n);
				if(rowData.ideaCd == $("#ideaCd").val()) {
					if (rowData.state != "대기")		//제안의 상태가 대기가 아닐 경우
						isUse = true;
				}
			}
		}

		first = false;

		if(isUse){
			$.showMsgBox("검토가 완료된 제안은 수정할 수 없습니다.",null);
			return false;
		}

		sendAjax({
			"url" : "${context_path}/system/system/menu/ideaSingle/saveIdeaSingle.do",
			"data" : getFormData("form"),
			"doneCallbackFunc" : "checkResult",
			"failCallbackFunc" : "checkResult"
		});
	}
	//저장 callback
	function checkResult(data) {
		$(window).scrollTop(0);	//윈도우 스크롤 맨 위로 이동시키기
		$.showMsgBox(data.msg);
		if(data.result == AJAX_SUCCESS) {
			searchList();
		}
	}
	// 삭제
	function deleteData() {
		isUse = false;

		var num = $("#list").jqGrid("getGridParam", "selarrrow");
		var rowData;
		isUse = false;
		$(num).each(function(i,n){
			rowData = $("#list").jqGrid("getRowData", n);
			if (rowData.state != '대기') {
				isUse = true;
				return;
			}
		});

		if(isUse){
			$.showMsgBox("검토가 완료된 제안은 삭제할 수 없습니다.",null);
			return false;
		}
		if(deleteDataToForm("list", "ideaCd", "form")) {	//삭제할 데이터를 form에 넣기
			$.showConfirmBox("<spring:message code="common.delete.msg"/>", "doDeleteData");
		}
	}
	// 삭제 처리
	function doDeleteData() {
		sendAjax({
			"url" : "${context_path}/system/system/menu/ideaSingle/deleteIdeaSingle.do",
			"data" : getFormData("form"),
			"doneCallbackFunc" : "searchList"
		});
	}

	var isUser;
	var isUse;
</script>

<form:form commandName="searchVO" id="form" name="form" method="post">
	<form:hidden path="ideaCd"/>		<!--게시물이 바뀌었을 때(하나 작업하고 다른 제안 다시 클릭 한 겨우)-->
	<form:hidden path="year"/>
	<form:hidden path="createDt"/>

	<div class="sch-bx">
		<ul>
			<li>
				<label for="findYear"><spring:message code="word.year"/></label>
				<form:select path="findYear" class="select wx100" items="${codeUtil:getCodeList('017')}" itemLabel="codeNm" itemValue="codeId">
				</form:select>
			</li>
			<li>
				<label for="findUseYn"><spring:message code="word.useYn"/></label>
				<form:select path="findUseYn" class="select wx80" items="${codeUtil:getCodeList('011')}" itemLabel="codeNm" itemValue="codeId">
				</form:select>
			</li>
			<li>
				<label for="findCategory"><spring:message code="word.category"/></label>
				<form:select path="findCategory" class="select wx100">
					<option value=""><spring:message code="word.all" /></option>	<!--카테고리 라벨에서 전체 를 추가-->
					<form:options items="${codeUtil:getCodeList('385')}" itemLabel="codeNm" itemValue="codeId"/>
				</form:select>
			</li>
			<li>
				<label for="colName"><spring:message code="word.search" /></label>
				<form:select path="colName" class="select wx100">
					<form:option value="title"><spring:message code="word.title"/></form:option>
					<form:option value="userNm"><spring:message code="word.insertUser"/></form:option>
				</form:select>
				<span class="searchBar"><form:input path="searchKeyword" class="t-box01 wx200" maxlength="20"/> </span>
			</li>
		</ul>
		<a href="#" class="btn-sch" onclick="searchList();return false;"><spring:message code="button.search"/></a>
	</div>
	<!--<div class="btn-dw"></div>-->
	<div class="gridContainer">
		<table id="list"></table>
		<div id="pager"></div>
	</div>
	<div class="tbl-bottom tbl-bottom2">
		<div class="tbl-btn">
			<a href="#" class="new" onclick="addData();return false;"><spring:message code="button.add"/></a>
			<a href="#" class="delete" onclick="deleteData();return false;"><spring:message code="button.delete"/></a>
		</div>
	</div>
	<div class="page-noti">
		<ul>
			<li>간단 아이디어를 제안하는 페이지 입니다.</li>
			<li>직원복지, 환경미화 등 간단한 아이디어만 제안해주세요.</li>
			<li>제안 검토가 완료 된 제안은 수정할 수 없습니다.</li>
		</ul>
	</div>

	<div id="newForm">
		<div class="ptitle" id="titleIdeaSingleNm">간단제안</div>
		<div class="tbl-type02">
			<table summary="">
				<!--<caption></caption>-->
				<colgroup> 		<!--열 넓이비율 지정-->
					<col width="20%"/>
					<col width="80%"/>
				</colgroup>
				<tbody>
				<tr>
					<th scope="row"><label for="category"><spring:message code="word.category"/></label><span class="red">(*)</span></th>
					<td >
						<form:select path="category" class="select wx100" items="${codeUtil:getCodeList('385')}" itemLabel="codeNm" itemValue="codeId">
						</form:select>
					</td>
				</tr>
				<tr>
					<th scope="row"><label for="title"><spring:message code="word.title"/></label><span class="red">(*)</span></th>
					<td ><form:input path="title" class="t-box01" maxlength="300"/></td> <!--최대길이 지정-->
				</tr>
				<tr>
					<th scope="row"><label for="content"><spring:message code="word.content" /></label><span class="red">(*)</span></th>
					<td>
						<p><form:textarea path="content" maxlength="4000"/></p>
						<p class="byte"><label id="contentBytes">0</label><label> / 4000byte</label></p>
					</td>
				</tr>
				</tbody>
			</table>
		</div>
		<div class="tbl-bottom">
			<div class="tbl-btn">
				<a href="#" class="save" onclick="saveData();return false;"><spring:message code="button.save"/></a>
			</div>
		</div>
	</div>
</form:form>