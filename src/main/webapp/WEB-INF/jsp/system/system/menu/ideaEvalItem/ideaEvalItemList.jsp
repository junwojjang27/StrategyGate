<!-- 
*************************************************************************
* CLASS 명	: IdeaEvalItemList
* 작 업 자	: 하성준
* 작 업 일	: 2021-09-27
* 기	능	: 평가항목관리 List
* ---------------------------- 변 경 이 력 --------------------------------
* 번호	작 업 자		작	업	일			변 경 내 용				비고
* ----	---------	----------------	---------------------	-----------
*	1	하성준		2021-09-27				최 초 작 업
**************************************************************************
-->

<!--
필독 : 다국어 지원기능에 의해서 화면에 나타나는 한글명칭은 message properties파일을 이용하여 수정할 것. 
 -->

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/common-taglibs.jsp"%>
<validator:javascript formName="ideaEvalItemVO" staticJavascript="false" xhtml="true" cdata="false"/>
<script type="text/javascript">
$(function(){
	$("#list").jqGrid({
		url			:	"${context_path}/system/system/menu/ideaEvalItem/ideaEvalItemList_json.do",
		postData	:	getFormData("form"),
		width		:	"${jqgrid_width}",
		height		:	"${jqgrid_height}",
		colModel	:	[
						{name:"evalItemCd",	index:"evalItemCd",	width:100,	align:"center",	label:"항목코드", hidden:true},
						{name:"year",	index:"year",	width:100,	align:"center",	label:"기준년도", hidden:true},
						{name:"evalItemTitle",	index:"evalItemTitle",	width:20,	align:"center",	label:"항목이름"},
						{name:"evalItemContent",	align:"left",	index:"evalItemContent",	width:100,	align:"center",	label:"항목내용"},
						{name:"evalDegreeId",	index:"evalDegreeId",	width:20,	align:"center",	label:"평가차수"},
						{name:"weightId",	index:"weightId",	width:20,	align:"center",	label:"가중치"},
						{name:"particalTypeId",	index:"particalTypeId",	width:20,	align:"center",	label:"평가자구분"},
						{name:"createDt",	index:"createDt",	width:30,	align:"center",	label:"등록날짜"},
						{name:"updateDt",	index:"updateDt",	width:100,	align:"center",	label:"수정일자", hidden:true},
						{name:"deleteDt",	index:"deleteDt",	width:100,	align:"center",	label:"삭제일자", hidden:true}

						],
		rowNum		: ${jqgrid_rownum_max},
		pager		: "pager",
		sortname	: "sortOrder",
		sortorder	: "asc",
		cellEdit	: true,
		multiselect	: true,
		loadComplete : function() {
		}
	});
	
	$("#newForm").hide();
});

// 목록 조회
function searchList() {
	$("#newForm").hide();
 	reloadGrid("list", "form");
}

// 엑셀 다운로드
function excelDownload() {
	var f = document.form;
	f.action = "${context_path}/system/system/menu/ideaEvalItem/excelDownload.do";
	f.submit();
}

// 상세 조회
function showDetail(parameter) {
	var f = document.form;
	f.evalItemCd.value = evalItemCd;
	f.year.value = year;
	f.evalDegreeId.value = evalDegreeId;

	
	sendAjax({
		"url" : "${context_path}/system/system/menu/ideaEvalItem/selectDetail.do",
		"data" : getFormData("form"),
		"doneCallbackFunc" : "setDetail"
	});
}

// 상세 조회 값 세팅
function setDetail(data) {
	$("#newForm").show();
	var dataVO = data.dataVO;
	
	$("#titleIdeaEvalItemNm").text("평가항목관리 : " + dataVO.evalDegreeId);
	
	voToForm(dataVO, "form", ["evalItemCd","evalDegreeId","evalItemTitle","particalTypeId","weightId","createDt","updateDt","deleteDt","evalItemContent"]);
	$("#evalDegreeId").focus();
}

// 정렬순서저장
function saveSortOrder() {
	if(!gridToForm("list", "form", true)) return false;
	
	sendAjax({
		"url" : "${context_path}/system/system/menu/ideaEvalItem/saveSortOrder.do",
		"data" : getFormData("form"),
		"doneCallbackFunc" : "searchList"
	});
}

// 등록
function addData() {
	$("#newForm").show();
	
	$("#titleIdeaEvalItemNm").text("평가항목관리");
	
	resetForm("form", ["evalItemCd","evalDegreeId","evalItemTitle","particalTypeId","weightId","createDt","updateDt","deleteDt","evalItemContent"]);
	$("#year").val($("#findYear").val());
	$("#evalDegreeId").focus();
}

// 저장
function saveData() {
	var f = document.form;
	if(!validatePerspectiveVO(f)) {
		return;
	}
	
	sendAjax({
		"url" : "${context_path}/system/system/menu/ideaEvalItem/saveIdeaEvalItem.do",
		"data" : getFormData("form"),
		"doneCallbackFunc" : "searchList"
	});
}

// 삭제
function deleteData() {
	if(deleteDataToForm("list", "evalDegreeId", "form")) {
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
		"url" : "${context_path}/system/system/menu/ideaEvalItem/deleteIdeaEvalItem.do",
		"data" : getFormData("form"),
		"doneCallbackFunc" : "searchList"
	});
}
</script>

<form:form commandName="searchVO" id="form" name="form" method="post">
 	<form:hidden path="evalItemCd"/>
	<form:hidden path="year"/>
	<form:hidden path="evalDegreeId"/>
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
		</ul>
		<a href="#" class="btn-sch" onclick="searchList();return false;"><spring:message code="button.search"/></a>
	</div>
	<div class="btn-dw">
		<a href="#" class="excel" onclick="excelDownload();return false;"><span><spring:message code="button.excelDownload"/></span></a>
	</div>
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
			<li></li>
			<li></li>
		</ul>
	</div>
<!--
	<div id="newForm">
		<div class="ptitle" id="titleIdeaEvalItemNm"></div>
		<div class="tbl-type02">
			<table summary="">
				<caption></caption>
				<colgroup>
					<col width="20%"/>
					<col width="80%"/>
				</colgroup>
				<tbody>
					<tr> 
						<th scope="row"><label for="evalItemCd">항목코드</label><span class="red">(*)</span></th> 
						<td ><form:input path="evalItemCd" class="t-box01"/></td> 
					</tr> 
					<tr> 
						<th scope="row"><label for="evalDegreeId">평가차수(1,2,3)</label><span class="red">(*)</span></th> 
						<td ><form:input path="evalDegreeId" class="t-box01"/></td> 
					</tr> 
					<tr> 
						<th scope="row"><label for="evalItemTitle">항목이름</label></th> 
						<td ><form:input path="evalItemTitle" class="t-box01"/></td> 
					</tr> 
					<tr> 
						<th scope="row"><label for="particalTypeId">직원구분(전직원/직원)</label></th> 
						<td ><form:input path="particalTypeId" class="t-box01"/></td> 
					</tr> 
					<tr> 
						<th scope="row"><label for="weightId">가중치</label></th> 
						<td ><form:input path="weightId" class="t-box01"/></td> 
					</tr> 
					<tr> 
						<th scope="row"><label for="createDt">생성일자</label></th> 
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
						<th scope="row"><label for="evalItemContent">항목 내용</label></th> 
						<td ><form:input path="evalItemContent" class="t-box01"/></td> 
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
-->
</form:form>

