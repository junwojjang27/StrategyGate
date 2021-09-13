<!-- 
*************************************************************************
* CLASS 명	: IdeaSingleList
* 작 업 자	: 하성준
* 작 업 일	: 2021-09-07
* 기	능	: 간단제안 List
* ---------------------------- 변 경 이 력 --------------------------------
* 번호	작 업 자		작	업	일			변 경 내 용				비고
* ----	---------	----------------	---------------------	-----------
*	1	하성준		2021-09-07				최 초 작 업
**************************************************************************
-->

<!--
필독 : 다국어 지원기능에 의해서 화면에 나타나는 한글명칭은 message properties파일을 이용하여 수정할 것. 
 -->

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/common-taglibs.jsp"%>
<validator:javascript formName="ideaSingleVO" staticJavascript="false" xhtml="true" cdata="false"/>
<script type="text/javascript">
$(function(){
	$("#list").jqGrid({
		url			:	"${context_path}/system/system/menu/ideaSingle/ideaSingleList_json.do",
		postData	:	getFormData("form"),
		width		:	"${jqgrid_width}",
		height		:	"${jqgrid_height}",
		colModel	:	[
						/*{name:"ideaCd",	index:"ideaCd",	width:100,	align:"center",	label:"제안코드",
							formatter:function(cellvalue, options, rowObject) {
								return "<a href='#' onclick='showDetail(\"" + removeNull(rowObject.ideaCd) + "\");return false;'>" + escapeHTML(removeNull(cellvalue)) + "</a>";
							},
							unformat:linkUnformatter
						},
						{name:"year",	index:"year",	width:100,	align:"center",	label:"기준년도",
							formatter:function(cellvalue, options, rowObject) {
								return "<a href='#' onclick='showDetail(\"" + removeNull(rowObject.year) + "\");return false;'>" + escapeHTML(removeNull(cellvalue)) + "</a>";
							},
							unformat:linkUnformatter
						},*/
						{name:"userId",	index:"userId",	width:100,	align:"center",	label:"아이디", hidden:true},
						{name:"category",	index:"category",	width:20,	align:"center",	label:"<spring:message code="word.category"/>"},
						{name:"title",	index:"title",	width:100,	align:"center",	label:"<spring:message code="word.title"/>",
							formatter:function(cellvalue, options, rowObject) {
								return "<a href='#' onclick='showDetail(\"" + removeNull(rowObject.ideaCd) + "\");return false;'>" + escapeHTML(removeNull(cellvalue)) + "</a>";
							},
							unformat:linkUnformatter
						},
						//{name:"content",	index:"content",	width:100,	align:"center",	label:"내용"},
						//{name:"state",	index:"state",	width:100,	align:"center",	label:"상태(접수/승인/반려)"},
						/*{name:"createDt",	index:"createDt",	width:100,	align:"center",	label:"생성일자",
							formatter:function(cellvalue, options, rowObject) {
								return "<a href='#' onclick='showDetail(\"" + removeNull(rowObject.createDt) + "\");return false;'>" + escapeHTML(removeNull(cellvalue)) + "</a>";
							},
							unformat:linkUnformatter
						},
						{name:"updateDt",	index:"updateDt",	width:100,	align:"center",	label:"수정일자"},
						{name:"deleteDt",	index:"deleteDt",	width:100,	align:"center",	label:"삭제일자"},
						{name:"startDt",	index:"startDt",	width:100,	align:"center",	label:"평가시작일자"},
						{name:"endDt",	index:"endDt",	width:100,	align:"center",	label:"평가종료일자"},
						{name:"atchFileId",	index:"atchFileId",	width:100,	align:"center",	label:"첨부파일ID"},
						{name:"ideaGbnCd",	index:"ideaGbnCd",	width:100,	align:"center",	label:"평가구분코드"},
						{name:"degree",	index:"degree",	width:100,	align:"center",	label:"차수"},
						{name:"evalState",	index:"evalState",	width:100,	align:"center",	label:"평가상태(대기/진행/종료)"}	*/
						{name:"userNm",	index:"userNm",	width:50,	align:"center",	label:"<spring:message code="word.insertUser"/>"},
						{name:"DeptNm",	index:"DeptNm",	width:50,	align:"center",	label:"<spring:message code="word.deptNm"/>"},
						{name:"CreateDt",	index:"CreatDt",	width:50,	align:"center",	label:"<spring:message code="word.insertDT"/>"}

						],
		//rowNum		: ${jqgrid_rownum_max},
		pager		: "pager",
        rowNum      : 10,
		sortname	: "sortOrder",
		sortorder	: "asc",
		cellEdit	: true,
		multiselect	: true,

		loadComplete : function() {

			//alert($("#findCategory").text());
			//alert($("#findCategory").val());
		}
	});
	
	$("#newForm").hide();
});

// 목록 조회
function searchList() {
	$("#newForm").hide();
 	reloadGrid("list", "form");
}
/*
// 엑셀 다운로드
function excelDownload() {
	var f = document.form;
	f.action = "${context_path}/system/system/menu/ideaSingle/excelDownload.do";
	f.submit();
}
*/
// 상세 조회
//function showDetail(parameter) {
function showDetail(ideaCd, year) {
	var f = document.form;
	f.ideaCd.value = ideaCd;
	f.year.value = year;
	//f.createDt.value = createDt;

	
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
	//var userId = "${sessionScope.loginVO.userId}";

	//document.getElementById("userNm").innerHTML = dataVO.userNm;
	//document.getElementById("deptNm").innerHTML = dataVO.deptNm;
	
	//$("#titleIdeaSingleNm").text("간단제안 : " + dataVO.userId);
	voToForm(dataVO, "form", ["title","ideaCd","userId","category"]);
	$("#content").val(dataVO.content);
	$("#content").focus();
}

// 정렬순서저장
function saveSortOrder() {
	if(!gridToForm("list", "form", true)) return false;
	
	sendAjax({
		"url" : "${context_path}/system/system/menu/ideaSingle/saveSortOrder.do",
		"data" : getFormData("form"),
		"doneCallbackFunc" : "searchList"
	});
}

// 등록
function addData() {
	$("#newForm").show();
	
	//$("#titleIdeaSingleNm").text("간단제안");
	
	//resetForm("form", ["ideaCd","userId","category","title","content","state","createDt","updateDt","deleteDt","startDt","endDt","atchFileId","ideaGbnCd","degree","evalState"]);
	resetForm("form", ["category","title", "userNm", "DeptNm", "createDt", "content"]);

	$("#year").val($("#findYear").val());
	$("#title").focus();

	//byte check
	showBytes("content", "contentBytes");
}

// 저장
function saveData() {
	var f = document.form;
	if(!validateIdeaSingleVO(f)) {
		return;
	}
	
	sendAjax({
		"url" : "${context_path}/system/system/menu/ideaSingle/saveIdeaSingle.do",
		"data" : getFormData("form"),
		//"doneCallbackFunc" : "searchList",
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
	else{
        $("#content").val("jkl");
    }
}

// 삭제
function deleteData() {
	/*if(deleteDataToForm("list", "userId", "form")) {
		$.showConfirmBox("<spring:message code="common.delete.msg"/>", "doDeleteData");
	}*/
	var ids = $("#list").jqGrid("getGridParam", "selarrrow");
	var rowData;
	var isUse = false;
	$(ids).each(function(i,v){
		rowData = $("#list").jqGrid("getRowData",v);
		if(Number(rowData.metricCnt) > 0){
			isUse = true;
		}
	});
	if(isUse){
		$.showMsgBox("<spring:message code="bsc.system.calTypeMng.noDelete"/>",null);
		return false;
	}
	if(deleteDataToForm("list", "ideaCd", "form")) {
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
		"url" : "${context_path}/system/system/menu/ideaSingle/deleteIdeaSingle.do",
		"data" : getFormData("form"),
		"doneCallbackFunc" : "searchList"
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
				<label for="category"><spring:message code="word.category"/></label>
				<form:select path="findCategory" class="select wx100" items="${codeUtil:getCodeList('385')}" itemLabel="codeNm" itemValue="codeId">
				</form:select>
			</li>
			<li>
				<label for="findSearch"><spring:message code="word.search"/></label>
				<form:select path="findSearch" class="select wx100">
					<form:option value="subject"><spring:message code="word.title"/></form:option>
					<form:option value="content"><spring:message code="word.content"/></form:option>
				</form:select>
				<span class="searchBar"><form:input path="searchKeyword" class="t-box01 wx200" maxlength="20"/> </span>
			</li>
		</ul>
		<a href="#" class="btn-sch" onclick="searchList();return false;"><spring:message code="button.search"/></a>
	</div>

	<div class="btn-dw">
	</div>

	<%--
	<div class="btn-dw">
		<a href="#" class="excel" onclick="excelDownload();return false;"><span><spring:message code="button.excelDownload"/></span></a>
	</div>
	--%>

	<div class="gridContainer">
		<table id="list"></table>
		<div id="pager"></div>
	</div>
	<div class="tbl-bottom tbl-bottom2">
		<div class="tbl-btn">
			<a href="#" class="save" onclick="saveSortOrder();return false;"><spring:message code="button.saveAll"/></a>
			<a href="#" class="new" onclick="addData();return false;"><spring:message code="button.add"/></a>
			<a href="#" class="delete" onclick="deleteData();return false;"><spring:message code="button.delete"/></a>
		</div>
	</div>
	<div class="page-noti">
		<ul>
			<li>간단 아이디어를 제안하는 페이지 입니다.</li>
			<li>직원복지, 환경미화 등 간단한 아이디어만 제시해주세요.</li>
			<li>제안 검토가 완료 된 제안은 수정할 수 없습니다.</li>
		</ul>
	</div>

	<div id="newForm">
		<div class="ptitle" id="titleIdeaSingleNm"></div>
		<div class="tbl-type02">
			<table summary="">
				<caption></caption>
				<colgroup>
					<col width="20%"/>
					<col width="80%"/>
				</colgroup>
				<tbody>
					<%-- <tr>
						<th scope="row"><label for="ideaCd">제안코드</label><span class="red">(*)</span></th> 
						<td ><form:input path="ideaCd" class="t-box01"/></td> 
					</tr> 
					<tr> 
						<th scope="row"><label for="userId">아이디</label></th> 
						<td ><form:input path="userId" class="t-box01"/></td> 
					</tr> --%>



					<tr> 
						<!--<th scope="row"><label for="category">카테고리</label><span class="red">(*)</span></th>-->
                        <th scope="row"><label for="category"><spring:message code="word.category"/></label><span class="red">(*)</span></th>
						<td colspan="3">
							<form:select path="category" class="select" items="${codeUtil:getCodeList('385')}" itemLabel="codeNm" itemValue="codeId">
							</form:select>
						</td>
					</tr> 
					<tr>
                        <th scope="row"><label for="title"><spring:message code="word.title"/></label><span class="red">(*)</span></th>
						<td ><form:input path="title" class="t-box01"/></td> 
					</tr> 
					<tr>
                        <th scope="row"><label for="content"><spring:message code="word.content" /></label><span class="red">(*)</span></th>
						<td colspan="3">
							<p><form:textarea path="content" maxlength="4000"/></p>
							<p class="byte"><label id="contentBytes">0</label><label> / 4000byte</label></p>
						</td>
					</tr>

					<%-- <tr>
						<th scope="row"><label for="state">상태(접수/승인/반려)</label></th> 
						<td ><form:input path="state" class="t-box01"/></td> 
					</tr> 
					<tr> 
						<th scope="row"><label for="createDt">생성일자</label><span class="red">(*)</span></th> 
						<td ><form:input path="createDt" class="t-box01"/></td> 
					</tr> 
					<tr> 
						<th scope="row"><label for="updateDt">수정일자</label></th> 
						<td ><form:input path="updateDt" class="t-box01"/></td> 
					</tr> 
					<tr> 
						<th scope="row"><label for="deleteDt">삭제일자</label></th> 
						<td ><form:input path="deleteDt" class="t-box01"/></td> 
					</tr> 
					<tr> 
						<th scope="row"><label for="startDt">평가시작일자</label></th> 
						<td ><form:input path="startDt" class="t-box01"/></td> 
					</tr> 
					<tr> 
						<th scope="row"><label for="endDt">평가종료일자</label></th> 
						<td ><form:input path="endDt" class="t-box01"/></td> 
					</tr> 
					<tr> 
						<th scope="row"><label for="atchFileId">첨부파일ID</label></th> 
						<td ><form:input path="atchFileId" class="t-box01"/></td> 
					</tr> 
					<tr> 
						<th scope="row"><label for="ideaGbnCd">평가구분코드</label></th> 
						<td ><form:input path="ideaGbnCd" class="t-box01"/></td> 
					</tr> 
					<tr> 
						<th scope="row"><label for="degree">차수</label></th> 
						<td ><form:input path="degree" class="t-box01"/></td> 
					</tr> 
					<tr> 
						<th scope="row"><label for="evalState">평가상태(대기/진행/종료)</label></th> 
						<td ><form:input path="evalState" class="t-box01"/></td> 
					</tr> --%>

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

