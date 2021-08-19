<!-- 
*************************************************************************
* CLASS 명	: CompUserMngList
* 작 업 자	: 박정현
* 작 업 일	: 2018-07-02
* 기	능	: 사용자관리 List
* ---------------------------- 변 경 이 력 --------------------------------
* 번호	작 업 자		작	업	일			변 경 내 용				비고
* ----	---------	----------------	---------------------	-----------
*	1	박정현		2018-07-02				최 초 작 업
**************************************************************************
-->

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/common-taglibs.jsp"%>
<validator:javascript formName="compUserMngVO" staticJavascript="false" xhtml="true" cdata="false"/>
<script type="text/javascript">
$(function(){
	$("#list").jqGrid({
		url			:	"${context_path}/system/system/comp/compUserMng/compUserMngList_json.do",
		postData	:	getFormData("form"),
		width		:	"${jqgrid_width}",
		height		:	"${jqgrid_height2}",
		colModel	:	[
						{name:"userId",	index:"userId",	width:80,	align:"left",	label:"<spring:message code="word.empNum"/>"},
						{name:"userNm",		index:"userNm",		width:100,	align:"left",	label:"<spring:message code="word.name2"/>",
							formatter:function(cellvalue, options, rowObject) {
								return "<a href='#' onclick='showDetail(\"" + removeNull(rowObject.userId) + "\");return false;'>" + escapeHTML(removeNull(cellvalue)) + "</a>";
							},
							unformat:linkUnformatter},
						{name:"posNm",		index:"posNm",		width:100,	align:"center",	label:"<spring:message code="word.pos"/>"},
						{name:"jikgubNm",	index:"jikgubNm",	width:100,	align:"center",	label:"<spring:message code="word.jikgub"/>"},
						{name:"jobNm",		index:"jobNm",		width:100,	align:"center",	label:"<spring:message code="word.job"/>"},
						{name:"deptNm",		index:"deptNm",		width:100,	align:"left",	label:"<spring:message code="word.belongDept"/>"},
						{name:"email",		index:"email",		width:100,	align:"left",	label:"<spring:message code="word.email"/>"},
						{name:"beingYn",	index:"beingYn",	width:80,	align:"center",	label:"<spring:message code="word.beingYn"/>",
							formatter:function(cellvalue,options,rowobject){
								var str = "";
								<c:forEach var="beingYnList" items="${codeUtil:getCodeList('368')}" varStatus="status">
									if(cellvalue == "${beingYnList.codeId}"){
										str = "${beingYnList.codeNm}"
									}
								</c:forEach>
								return str;
							}
						},
						{name:"resetPw",	index:"resetPw",	width:100,	align:"center",	label:"<spring:message code="word.resetPasswd"/>",
							formatter:function(cellvalue, options, rowObject) {
								return "<input type='button' class='pButton3_grid'  value='<spring:message code="button.reset"/>' onclick='goReset(\"" + rowObject.userId + "\");return false;' />";
							}, unformat:inputUnformatter
						},
						{name:"loginBt",	index:"loginBt",	width:80,	align:"center",	label:"<spring:message code="word.login"/>",
							formatter:function(cellvalue, options, rowObject) {
								return "<input type='button' class='pButton3_grid'  value='<spring:message code="button.login"/>' onclick='goLogin(\"" + rowObject.userId + "\");return false;' />";
							}, unformat:inputUnformatter
						}
						],
		rowNum		: ${jqgrid_rownum_max},
		pager		: "pager",
		sortname	: "userNm",
		sortorder	: "asc",
		multiselect	: true,
		loadComplete : function() {
			hideGridCheckbox("list", "userId", "${sessionScope.compId}_admin", true);
		}
	});
	$("#newForm").hide();
	setMaxLength("form");
	
	$("#findUserNm").keyup(function(e) {
		if(e.keyCode == 13) searchList();
	});
});

// 목록 조회
function searchList() {
	$("#newForm").hide();
	reloadGrid("list", "form");
}

// 상세 조회
function showDetail(parameter) {
	var f = document.form;
	$("#userId").prop("disabled", false);
	f.userId.value = parameter;

	sendAjax({
		"url" : "${context_path}/system/system/comp/compUserMng/selectDetail.do",
		"data" : getFormData("form"),
		"doneCallbackFunc" : "setDetail"
	});
}

// 상세 조회 값 세팅
function setDetail(data) {
	$("#newForm").show();
	$("#updateYn").val("Y");
	var dataVO = data.dataVO;
	
	$("#titleCompUserMngNm").text("<spring:message code="word.userMng"/> : " + dataVO.userNm);
	
	voToForm(dataVO, "form", ["userId","userNm","deptId","deptNm","jikgubId","jikgubNm","posId","posNm","email","beingYn","jobId","jobNm"]);

	$("#userNm").focus();
	$("#userId").prop("disabled", true);
}

// 엑셀 업로드
function popExcelUploadForm() {
	openFancybox({
		"url" : "${context_path}/system/system/comp/compUserMng/popExcelUploadForm.do",
		"data" : getFormData("form")
	});
}

// 등록
function addData() {
	$("#userId").prop("disabled", false);
	$("#newForm").show();
	$("#updateYn").val("N");
	
	$("#titleCompUserMngNm").text("<spring:message code="word.userMng"/>");
	
	resetForm("form", ["userId","userNm","deptId","deptNm","jikgubId","jikgubNm","posId","posNm","email","beingYn","jobId","jobNm"]);
	$("#year").val($("#findYear").val());
	$("#userId").focus();
}

// 저장
function saveData() {
	var f = document.form;
	if(!validateCompUserMngVO(f)) {
		return;
	}

	$("#userId").prop("disabled", false);
	$("#jikgubNm").val($("#jikgubId option:selected").text());
	$("#jobNm").val($("#jobId option:selected").text());
	$("#posNm").val($("#posId option:selected").text());

	sendAjax({
		"url" : "${context_path}/system/system/comp/compUserMng/saveCompUserMng.do",
		"data" : getFormData("form"),
		"doneCallbackFunc" : "checkResult"
	});
	
}

// 삭제
function deleteData() {
	if(deleteDataToForm("list", "userId", "form")) {
		$.showConfirmBox("<spring:message code="common.delete.msg"/>", "doDeleteData");
	}
}

// 삭제 처리
function doDeleteData() {
	var delList = [];
	$("#form").find("[name=keys]").each(function(i, e) {
		delList.push($(this).val());
	});
	
	sendAjax({
		"url" : "${context_path}/system/system/comp/compUserMng/deleteCompUserMng.do",
		"data" : getFormData("form"),
		"doneCallbackFunc" : "checkResult"
	});
}

// 패스워드 초기화
function goReset(userId) {
	$.showConfirmBox("<spring:message code="bsc.system.userMng.resetPasswd.confirm"/>", function() {
		$("#userId").prop("disabled", false).val(userId);

		sendAjax({
			"url" : "${context_path}/system/system/comp/compUserMng/passwordReset.do",
			"data" : getFormData("form"),
			"doneCallbackFunc" : function(data) {
				$.showMsgBox(data.msg);
			}
		});
	});			
}

function checkResult(data) {
	$.showMsgBox(data.msg);
	if(data.result == AJAX_SUCCESS) {
		searchList();
	}
}

// 로그인
function goLogin(userId) {
	$("#userId").prop("disabled", false);
	
	var f = document.form;
	f.userId.value = userId;
	f.action = "${context_path}/loginProcess.do";
	f.submit();
}


//엑셀 양식 다운로드
function popExcelDownload() {
	var f = document.form;
	f.action = "${context_path}/system/system/comp/compUserMng/excelFormDownload.do";
	f.submit();
}

</script>

<form:form commandName="searchVO" id="form" name="form" method="post">
	<form:hidden path="findYear"/>
	<form:hidden path="updateYn"/>
	<input type="hidden" name="adminLoginKey" value="${sessionScope.ADMIN_LOGIN_KEY}"/>

	<div class="sch-bx">
		<ul>
			<li>
				<label for="findBeingYn"><spring:message code="word.beingYn"/></label>
				<form:select path="findBeingYn" class="select wx100">
					<option value=""><spring:message code="word.all"/></option>
					<form:options items="${codeUtil:getCodeList('368')}" itemValue="codeId" itemLabel="codeNm"></form:options>
				</form:select>
			</li>
			<li>
				<label for="findPosId"><spring:message code="word.pos"/></label>
				<form:select path="findPosId" class="select wx100">
					<option value=""><spring:message code="word.all"/></option>
					<form:options items="${codeUtil:getCodeList('344')}" itemValue="codeId" itemLabel="codeNm"></form:options>
				</form:select>
			</li>
			<li>
				<label for="findJikgubId"><spring:message code="word.jikgub"/></label>
				<form:select path="findJikgubId" class="select wx100">
					<option value=""><spring:message code="word.all"/></option>
					<form:options items="${codeUtil:getCodeList('345')}" itemValue="codeId" itemLabel="codeNm"></form:options>
				</form:select>
			</li>
			<li>
				<label for="findJobId"><spring:message code="word.job"/></label>
				<form:select path="findJobId" class="select wx100">
					<option value=""><spring:message code="word.all"/></option>
					<form:options items="${codeUtil:getCodeList('343')}" itemValue="codeId" itemLabel="codeNm"></form:options>
				</form:select>
			</li>
			<li>
				<label for="findUserNm"><spring:message code="word.name2"/></label>
				<form:input path="findUserNm" class="wx150"/>
			</li>
		</ul>
		<a href="#" class="btn-sch" onclick="searchList();return false;"><spring:message code="button.search"/></a>
	</div>
	<div class="btn-dw">
	</div>
	<div class="gridContainer">
		<table id="list"></table>
		<div id="pager"></div>
	</div>
	<div class="tbl-bottom tbl-bottom2 wx600">
		<div class="tbl-btn">
			<a href="#" class="dw" onclick="popExcelDownload();return false;"><spring:message code="button.excelFormDownload"/></a>
			<a href="#" class="new" onclick="popExcelUploadForm();return false;"><spring:message code="button.excelUpload"/></a>
			<a href="#" class="new" onclick="addData();return false;"><spring:message code="button.add"/></a>
			<a href="#" class="delete" onclick="deleteData();return false;"><spring:message code="button.delete"/></a>
		</div>
	</div>

	<div id="newForm">
		<div class="ptitle" id="titleCompUserMngNm"></div>
		<div class="tbl-type02">
			<table summary="">
				<caption></caption>
				<colgroup>
					<col width="10%"/>
					<col width="40%"/>
					<col width="10%"/>
					<col width="40%"/>
				</colgroup>
				<tbody>
					<tr>
						<th scope="row"><label for="userId"><spring:message code="word.empNum"/></label><span class="red">(*)</span></th>
						<td><form:input path="userId" class="t-box02"/></td>
						<th scope="row"><label for="userNm"><spring:message code="word.name2"/></label><span class="red">(*)</span></th>
						<td><form:input path="userNm" class="t-box02" maxlength="50"/></td>
					</tr>
					<tr>
						<th scope="row"><label for="posId"><spring:message code="word.pos"/></label><span class="red">(*)</span></th>
						<td>
							<form:hidden path="posNm"/>
							<form:select path="posId" class="t-box02">
								<form:options items="${codeUtil:getCodeList('344')}" itemValue="codeId" itemLabel="codeNm"></form:options>
							</form:select>
						</td>
						<th scope="row"><label for="jikgubId"><spring:message code="word.jikgub"/></label><span class="red">(*)</span></th>
						<td>
							<form:hidden path="jikgubNm"/>
							<form:select path="jikgubId" class="t-box02">
								<form:options items="${codeUtil:getCodeList('345')}" itemValue="codeId" itemLabel="codeNm"></form:options>
							</form:select>
						</td>
					</tr>
					<tr>
						<th scope="row"><label for="jobId"><spring:message code="word.job"/></label><span class="red">(*)</span></th>
						<td>
							<form:hidden path="jobNm"/>
							<form:select path="jobId" class="t-box02">
								<form:options items="${codeUtil:getCodeList('343')}" itemValue="codeId" itemLabel="codeNm"></form:options>
							</form:select>
						</td>
						<th scope="row"><label for="deptId"><spring:message code="word.belongDept"/></label><span class="red">(*)</span></th>
						<td>
							<form:hidden path="deptId"/>
							<span class="spanDept">
								<form:input path="deptNm" class="t-box02" readonly="true"/>
								<a href="#" class="btn-search" onclick="popDeptList('deptId', 'deptNm', 'deptFullNm');return false;"><spring:message code="button.search"/></a>
								<a href="#" class="close" onclick="resetInput(['deptId', 'deptNm']);return false;"><spring:message code="button.delete"/></a>
							</span>
						</td>
					</tr>
					<tr>
						<th scope="row"><label for="email"><spring:message code="word.email"/></label></th>
						<td><form:input path="email" class="t-box02" maxlength="100"/></td>
						<th scope="row"><label for="beingYn"><spring:message code="word.beingYn"/></label><span class="red">(*)</span></th>
						<td>
							<form:select path="beingYn" class="t-box02">
								<form:options items="${codeUtil:getCodeList('368')}" itemValue="codeId" itemLabel="codeNm"></form:options>
							</form:select>
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

