<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/common-taglibs.jsp"%>
<validator:javascript formName="ideaUsVO" staticJavascript="false" xhtml="true" cdata="false"/>
<script type="text/javascript">
	$(function(){
		$("#list").jqGrid({
			url			:	"${context_path}/system/system/menu/ideaUs/ideaUsList_json.do",
			postData	:	getFormData("form"),
			width		:	"${jqgrid_width}",
			height		:	"300",
			colModel	:	[
				{name:"ideaCd",		index:"ideaCd",	hidden:true},
				{name:"year",		index:"year",	hidden:true},
				{name:"findUseYn",		index:"findUseYn",	hidden:true},
				{name:"category",	index:"category",	width:30,	align:"center",	label:"<spring:message code="word.category"/>"},
				{name:"title",	index:"title",	width:200,	align:"left",	label:"<spring:message code="word.title"/>",
					formatter:function(cellvalue, options, rowObject) {//(넘어온값, )
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
			//loadonce	: false,
			loadComplete : function() {
				var cnt = 0;

				<sec:authorize access="hasRole('01')"> //관리자 권한 체크
				cnt = 1;
				</sec:authorize>

				if(cnt != 1){
					var loginUserId = "${sessionScope.loginVO.userId}"; //로그인 정보를 가져온다.
					hideGridCheckbox("list", "userId", loginUserId, false);
				}
			}


		});
		/***** 사용여부 미사용시 삭제 버튼 숨김 *****/
		<c:choose>
		<c:when test="${searchVO.findUseYn == 'N'}">
		$(".delete").hide();
		</c:when>
		<c:otherwise>
		$(".delete").show();
		</c:otherwise>
		</c:choose>
		$("#findUseYn").on("change", function() {
			if($(this).val() == "N"){
				$(".delete").hide();
			}else{
				$(".delete").show();
			}
		});
		/***** 사용여부 미사용시 삭제 버튼 숨김 end *****/
		$("#newForm").hide();

		$("#searchKeyword").keyup(function(e) {
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

		var num = $("#list").getGridParam("reccount");
		var rowData;
		for (var n = 1; n <= num; n++) { //jqgrid의 아이디를 1부터 화면에 표시된 개수까지 비교한다.
			rowData = $("#list").jqGrid("getRowData", n); //아이디에 해당하는 행의 데이터를 가져온다.
			if(rowData.ideaCd == $("#ideaCd").val()) {
				if (rowData.state != "대기")
					$(".save").hide();
				else
					$(".save").show();
			}
		}

		sendAjax({
			"url" : "${context_path}/system/system/menu/ideaUs/selectDetail.do",
			"data" : getFormData("form"),
			"doneCallbackFunc" : "setDetail"
		});
	}
	var upload1;
	// 상세 조회 값 세팅
	function setDetail(data) {
		$("#newForm").show();
		var dataVO = data.dataVO;
		voToForm(dataVO, "form", ["year","category","title"]);
		$("#title").val(dataVO.title);
		$("#content").val(dataVO.content);
		$("#useYn").val($("#findUseYn").val());
		$("#userNm").empty();
		$("#userNm").text(dataVO.userNm);
		$("#deptNm").empty();
		$("#deptNm").text(dataVO.deptNm);
		$("#title").focus();


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
		showBytes("content", "contentBytes");
		setMaxLength("form");
	}
	// 등록
	function addData() {
		$("#newForm").show();
		resetForm("form", ["ideaCd", "category","title","content","atchFileId","useYn", "userNm", "deptNm"]);
		$("#year").val($("#findYear").val());
		$("#title").focus();
		var userNm = "${sessionScope.loginVO.userNm}";
		var deptNm = "${sessionScope.loginVO.deptNm}";
		$("#userNm").empty();
		$("#userNm").text(userNm);
		$("#deptNm").empty();
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
		showBytes("content", "contentBytes");
		setMaxLength("form");
	}
	// 저장
	function saveData() {
		var f = document.form;
		if(!validateIdeaUsVO(f)) {
			return;
		}

		var cnt = 0;

		isUser = false;

		if($("#ideaCd").val() != "") {
			cnt = 0;

			<sec:authorize access="hasRole('01')"> //관리자 권한 체크
				cnt = 1;
			</sec:authorize>


			if (cnt != 1) {
				var num = $("#list").getGridParam("reccount");
				var rowData;
				var userId = "${sessionScope.loginVO.userId}"; //로그인 정보를 가져온다.
				for (var n = 1; n <= num; n++) { //jqgrid의 아이디를 1부터 화면에 표시된 개수까지 비교한다.
					rowData = $("#list").jqGrid("getRowData", n); //아이디에 해당하는 행의 데이터를 가져온다.
					if(rowData.ideaCd == $("#ideaCd").val()) {
						if (rowData.userId != userId) {
							isUser = true;
						}
					}
				}
			}
		}

		if(isUser){
			$.showMsgBox("유저가다름.",null);
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
		$(window).scrollTop(0);
		$.showMsgBox(data.msg);
		if(data.result == AJAX_SUCCESS) {
			searchList();
		}
	}
	// 삭제
	function deleteData() {
		isUse = false;
		isUser = false;

		checkState();

		if(isUse){
			$.showMsgBox("검토가 완료된 제안은 삭제할 수 없습니다.",null); //메세지 수정 必
			return false;
		}
		if(isUser){
			$.showMsgBox("유저가다름.",null);
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
	//진행상태체크
	var isUse;
	var isUser;
	function checkState() { //현재 진행상태를 확인하는 함수
		var num = $("#list").jqGrid("getGridParam", "selarrrow");
		var rowData;
		var userId = "${sessionScope.loginVO.userId}";
		isUse = false;

		$(num).each(function(i,n){ //jqgrid의 아이디를 1부터 화면에 표시된 개수까지 비교한다.
			rowData = $("#list").jqGrid("getRowData", n); //아이디에 해당하는 행의 데이터를 가져온다.


			if(rowData.userId != userId){
				isUser = true;
				return;
			}

			if (rowData.state != '대기') { // 검토 진행상태를 확인한다. 001:대기 002:승인 003:반려
				//제안이 검토가 완료된 상태인 경우 전역변수에 true를 저장한 후 반복을 종료한다.
				isUse = true;
				return;
			}

		});
	}
</script>

<form:form commandName="searchVO" id="form" name="form" method="post">
	<form:hidden path="ideaCd"/>
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
			<li></li>
			<li></li>
		</ul>
	</div>

	<div id="newForm">
		<div class="ptitle" id="titleIdeaUsNm"></div>
		<div class="tbl-type02">
			<table summary="">
				<caption></caption>
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
					<td colspan="3"><form:input path="title" class="t-box01" maxlength="300"/></td>
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
			<div class="tbl-wbtn">
			</div>
			<div class="tbl-btn">
				<a href="#" class="save" onclick="saveData();return false;"><spring:message code="button.save"/></a>
			</div>
		</div>
	</div>
</form:form>