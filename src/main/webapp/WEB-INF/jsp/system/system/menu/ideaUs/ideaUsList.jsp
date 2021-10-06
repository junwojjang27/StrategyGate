<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/common-taglibs.jsp"%>
<validator:javascript formName="ideaUsVO" staticJavascript="false" xhtml="true" cdata="false"/>
<script type="text/javascript">
	$(function(){
		$("#list").jqGrid({
			url			:	"${context_path}/system/system/menu/ideaUs/ideaUsList_json.do",
			postData	:	getFormData("form"),
			width		:	"${jqgrid_width}",
			height		:	"(count%10)*30",    //jqGrid크기를 제안 수에 맞게 변경
			colModel	:	[
				{name:"ideaCd",		index:"ideaCd",	hidden:true},
				{name:"year",		index:"year",	hidden:true},
				{name:"findUseYn",		index:"findUseYn",	hidden:true},
				{name:"category",	index:"category",	width:30,	align:"center",	label:"<spring:message code="word.category"/>"},
				{name:"title",	index:"title",	width:200,	align:"left",	label:"<spring:message code="word.title"/>",
					formatter:function(cellvalue, options, rowObject) {
						return "<a href='#' onclick='showDetail(\"" + removeNull(rowObject.ideaCd) + "\");return false;'>" + escapeHTML(removeNull(cellvalue)) + "</a>";
					},//formatter : 색깔이나 액션을 주는 (데이터 가공) / 제목에 링크, 돋보기 표시 등
					unformat:linkUnformatter
				},
				{name:"userId",		index:"userId",	hidden:true},
				{name:"userNm",	index:"userNm",	width:30,	align:"center",	label:"<spring:message code="word.insertUser"/>"},
				{name:"deptNm",	index:"deptNm",	width:30,	align:"center",	label:"<spring:message code="word.deptNm"/>"},

				{name:"state",		index:"state", width:30,	align:"center",	label:"진행상태"},
				{name:"createDt",	index:"creatDt",	width:40,	align:"center",	label:"<spring:message code="word.insertDT"/>"},
			],
			rowNum		: 10,
			pager		: "pager",
			sortname	: "createDt",
			sortorder	: "asc",
			multiselect	: true,
			loadComplete : function() {
                var count = $('#list').getGridParam('records'); //jqGrid크기 조절하기 위함. (서버에서 받은 실제 레코드 수)

				var cnt = 0;

				<sec:authorize access="hasRole('01')"> //관리자 권한 체크
				cnt = 1;
				</sec:authorize>

				if(cnt != 1){ //관리자 아닐 경우 체크박스 자기꺼만 보이게
					var loginUserId = "${sessionScope.loginVO.userId}"; //로그인 정보를 가져온다.
					hideGridCheckbox("list", "userId", loginUserId, false);
				}

				//byte check
				showBytes("content", "contentBytes");
				setMaxLength("form");
			}


		});
		$("#findUseYn").on("change", function() {//사용여부에 따라 삭제 버튼 보임여부
			if($(this).val() == "N"){
				$(".delete").hide();
			}else{
				$(".delete").show();
			}
		});

		$("#newForm").hide();

		$("#searchKeyword").keyup(function(e) { //enter눌렀을 때 searchList()로 이동
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

		ischange = true;

		if (cnt != 1) {		//관리자 아닐 경우
			var userId = "${sessionScope.loginVO.userId}";
			var num = $("#list").getGridParam("reccount");		//현재 화면에 표시되고있는 레코드 숫자
			var rowData;
			for (var n = 1; n <= num; n++) { 	//1 ~ (화면에 표시되고있는 레코드 숫자) 까지 반복하여 ideaCd를 비교해서 해당 제안을 찾음.
				rowData = $("#list").jqGrid("getRowData", n); //아이디에 해당하는 행의 데이터를 가져온다.
				if(rowData.ideaCd == $("#ideaCd").val()) {
					if (rowData.userId == userId)
						$(".save").show();
					else
						$(".save").hide();
				}
			}
		}
		else {
			var num = $("#list").getGridParam("reccount");		//현재 화면에 표시되고있는 레코드 숫자
			var rowData;
			for (var n = 1; n <= num; n++) {	//1 ~ (화면에 표시되고있는 레코드 숫자) 까지 반복하여 ideaCd를 비교해서 해당 제안을 찾음.
				rowData = $("#list").jqGrid("getRowData", n);
				if(rowData.ideaCd == $("#ideaCd").val()) {
					if (rowData.state == '대기') {
						$(".save").show();
					}
					else {
						$(".save").hide();
						ischange = false;
					}
				}
			}
		}

		sendAjax({
			"url" : "${context_path}/system/system/menu/ideaUs/selectDetail.do",
			"data" : getFormData("form"),
			"doneCallbackFunc" : "setDetail"
		});
	}
	var isChange;
	var upload1;
	// 상세 조회 값 세팅
	function setDetail(data) {


		$("#newForm").show();
		var dataVO = data.dataVO;

		voToForm(dataVO, "form", ["year","category","title", "userNm", "deptNm", "content"]);	//VO의 값을 form에 세팅
		$("#userNm").text(dataVO.userNm);
		$("#deptNm").text(dataVO.deptNm);
		$("#useYn").val($("#findUseYn").val());
		// $("#title").val(dataVO.title);

		if(!ischange) {
			document.getElementById('category').disabled = true;
			document.getElementById('useYn').disabled = true;
			document.getElementById('title1').readOnly = true;
			document.getElementById('content').readOnly = true;
		}
		else {
			document.getElementById('category').disabled = false;
			document.getElementById('useYn').disabled = false;
			document.getElementById('title1').readOnly = false;
			document.getElementById('content').readOnly = false;
		}

		var modifyYn = "Y";

		//첨부파일 ajax
		$.ajax({
			url : "${context_path}/system/system/menu/ideaUs/ideaUsDetail.do",
			data : {
				"modifyYn" : modifyYn,
				"atchFileId" : dataVO.atchFileKey,
				"_csrf" : getCsrf("form")
			},
			method : "POST",
			cache : false,
			dataType : "html"
		}).done(function(html) {
			$("#spanAttachFile").html(html);
		}).fail(function(jqXHR, textStatus) {
			try{
				var json = JSON.parse(jqXHR.responseText);
				if(!isEmpty(json.msg)) {
					$.showMsgBox(json.msg);
				} else {
					$.showMsgBox(getMessage("errors.processing"));
				}
			}catch(e) {
				$.showMsgBox(getMessage("errors.processing"));
			}
		});
		//byte check
		// showBytes("content", "contentBytes");
		// setMaxLength("form");
	}
	// 등록
	function addData() {

		document.getElementById('category').disabled = false;
		document.getElementById('useYn').disabled = false;
		document.getElementById('title1').readOnly = false;
		document.getElementById('content').readOnly = false;

		$("#newForm").show();
		resetForm("form", ["ideaCd", "category","title","content","atchFileId","useYn", "userNm", "deptNm"]);
		$("#year").val($("#findYear").val());
		$("#title").focus();
		var userNm = "${sessionScope.loginVO.userNm}";
		var deptNm = "${sessionScope.loginVO.deptNm}";
		$("#userNm").text(userNm);
		$("#deptNm").text(deptNm);
		var modifyYn = "Y";
		//첨부파일 ajax
		$.ajax({
			url : "${context_path}/system/system/menu/ideaUs/ideaUsDetail.do",
			data : {
				"modifyYn" : modifyYn,
				"_csrf" : getCsrf("form")
			},
			method : "POST",
			cache : false,
			dataType : "html"
		}).done(function(html) {
			$("#spanAttachFile").empty();
			$("#spanAttachFile").html(html);
		}).fail(function(jqXHR, textStatus) {
			try{
				var json = JSON.parse(jqXHR.responseText);
				if(!isEmpty(json.msg)) {
					$.showMsgBox(json.msg);
				} else {
					$.showMsgBox(getMessage("errors.processing"));
				}
			}catch(e) {
				$.showMsgBox(getMessage("errors.processing"));
			}
		});
		//byte
		// showBytes("content", "contentBytes");
		// setMaxLength("form");
	}
	// 저장
	function saveData() {
		var f = document.form;
		if(!validateIdeaUsVO(f)) {  //유효성
			return;
		}

		alert($("#ideaCd").val());

		isUse = false;
		var num = $("#list").getGridParam("reccount");
		var rowData;
		for (var n = 1; n <= num; n++) {
			rowData = $("#list").jqGrid("getRowData", n);
			if(rowData.ideaCd == $("#ideaCd").val()) {
				if (rowData.state != "대기")
					isUse = true;
			}
		}

		if(isUse){
			$.showMsgBox("검토가 완료된 제안은 수정할 수 없습니다.",null);
			return false;
		}

		sendMultipartForm({
			"url" : "${context_path}/system/system/menu/ideaUs/saveIdeaUs.do",
			"formId" : "form",
			"fileModules" : [upload1],
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
			$.showMsgBox("검토가 완료된 제안은 삭제할 수 없습니다.",null); //메세지 수정 必
			return false;
		}


		if(deleteDataToForm("list", "ideaCd", "form")) {
			$.showConfirmBox("<spring:message code="common.delete.msg"/>", "doDeleteData");
		}
	}
	// 삭제 처리
	function doDeleteData() {
		sendAjax({
			"url" : "${context_path}/system/system/menu/ideaUs/deleteIdeaUs.do",
			"data" : getFormData("form"),
			"doneCallbackFunc" : "checkResult"
		});
	}

	var isUse;
	var isUser;

</script>

<form:form commandName="searchVO" id="form" name="form" method="post">
	<form:hidden path="ideaCd"/>	<!--이렇게 3개가 있어야 ideaCd, year, createDt를 사용할 수 있다고 추정... (없을 시 오류)-->
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
					<option value=""><spring:message code="word.all" /></option>
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
	<div class="btn-dw"></div>
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
			<li>개인,부서별,동아리별 프로젝트 단위의 아이디어를 제안하는 페이지 입니다.</li>
			<li>공모전에 참가하실 팀은 게시판에서 관련 폼을 다운받아 제출하시기 바랍니다.</li>
            <li>개인으로 제안하실 분은 카테고리를 개인으로 설정하여 제출하시기 바랍니다.</li>
		</ul>
	</div>

	<div id="newForm">
		<div class="ptitle" id="titleIdeaUsNm">혁신제안</div>
		<div class="tbl-type02">
			<table summary="">
				<!--<caption></caption>-->
				<colgroup>
					<col width="15%"/>
					<col width="35%"/>
					<col width="15%"/>
					<col width="35%"/>
				</colgroup>
				<tbody>
				<tr>
					<th scope="row"><label for="userNm"><spring:message code="word.insertUser"/></label></th>
					<td><span id="userNm"/></td>
					<th scope="row"><label for="deptNm"><spring:message code="word.dept"/></label></th>
					<td><span id="deptNm"/></td>
				</tr>
				<tr>
					<th scope="row"><label for="category"><spring:message code="word.category"/></label><span class="red">(*)</span></th>
					<td>
						<form:select path="category" class="select wx100" items="${codeUtil:getCodeList('385')}" itemLabel="codeNm" itemValue="codeId">
						</form:select>
					</td>
					<th scope="row"><label for="useYn"><spring:message code="word.useYn"/></label><span class="red">(*)</span></th>
					<td>
						<form:select path="useYn" class="select wx80" items="${codeUtil:getCodeList('011')}" itemLabel="codeNm" itemValue="codeId">
						</form:select>
					</td>
				</tr>
				<tr>
					<th scope="row"><label for="title"><spring:message code="word.title"/></label><span class="red">(*)</span></th>
					<td colspan="3"><form:input path="title" class="t-box01" maxlength="300" id="title1"/></td>
				</tr>
				<tr>
					<th scope="row"><label for="content"><spring:message code="word.content"/></label><span class="red">(*)</span></th>
					<td colspan="3">
						<p><form:textarea path="content" maxlength="4000"/></p>
						<p class="byte"><label id="contentBytes">0</label><label> / 4000byte</label></p>
					</td>
				</tr>
				<tr>
					<th scope="row"><label><spring:message code="word.atchFile"/></label></th>
					<td colspan="3">
						<span id="spanAttachFile"></span>
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