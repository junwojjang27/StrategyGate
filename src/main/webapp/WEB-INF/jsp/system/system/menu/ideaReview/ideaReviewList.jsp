<!-- 
*************************************************************************
* CLASS 명	: IdeaReviewList
* 작 업 자	: 하성준
* 작 업 일	: 2021-10-05
* 기	능	: IDEA+검토 List
* ---------------------------- 변 경 이 력 --------------------------------
* 번호	작 업 자		작	업	일			변 경 내 용				비고
* ----	---------	----------------	---------------------	-----------
*	1	하성준		2021-10-05				최 초 작 업
**************************************************************************
-->

<!--
필독 : 다국어 지원기능에 의해서 화면에 나타나는 한글명칭은 message properties파일을 이용하여 수정할 것. 
 -->

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/common-taglibs.jsp"%>
<validator:javascript formName="ideaReviewVO" staticJavascript="false" xhtml="true" cdata="false"/>
<script type="text/javascript">
$(function(){
	$("#list").jqGrid({
		url			:	"${context_path}/system/system/menu/ideaReview/ideaReviewList_json.do",
		postData	:	getFormData("form"),
		width		:	"${jqgrid_width}",
		height		:	"${jqgrid_height}",
		colModel	:	[
						{name:"ideaExecutionCd",	index:"ideaExecutionCd",	width:100,	align:"center",	label:"실행안코드",
							formatter:function(cellvalue, options, rowObject) {
								return "<a href='#' onclick='showDetail(\"" + removeNull(rowObject.ideaExecutionCd) + "\");return false;'>" + escapeHTML(removeNull(cellvalue)) + "</a>";
							},
							unformat:linkUnformatter
						},
						{name:"ideaCd",	index:"ideaCd",	width:100,	align:"center",	label:"제안번호"},
						{name:"result",	index:"result",	width:100,	align:"center",	label:"폐기,실행"},
						{name:"excutionDt",	index:"excutionDt",	width:100,	align:"center",	label:"실행시작일자"},
						{name:"createDt",	index:"createDt",	width:100,	align:"center",	label:"생성일자"},
						{name:"updateDt",	index:"updateDt",	width:100,	align:"center",	label:"수정일자"},
						{name:"deleteDt",	index:"deleteDt",	width:100,	align:"center",	label:"삭제일자"},
						{name:"year",	index:"year",	width:100,	align:"center",	label:"기준년도"
							formatter:function(cellvalue, options, rowObject) {
								return "<a href='#' onclick='showDetail(\"" + removeNull(rowObject.year) + "\");return false;'>" + escapeHTML(removeNull(cellvalue)) + "</a>";
							},
							unformat:linkUnformatter
						}

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
	f.action = "${context_path}/system/system/menu/ideaReview/excelDownload.do";
	f.submit();
}

// 상세 조회
function showDetail(parameter) {
	var f = document.form;
	f.ideaExecutionCd.value = ideaExecutionCd;
	f.year.value = year;

	
	sendAjax({
		"url" : "${context_path}/system/system/menu/ideaReview/selectDetail.do",
		"data" : getFormData("form"),
		"doneCallbackFunc" : "setDetail"
	});
}

// 상세 조회 값 세팅
function setDetail(data) {
	$("#newForm").show();
	var dataVO = data.dataVO;
	
	$("#titleIdeaReviewNm").text("IDEA+검토 : " + dataVO.ideaCd);
	
	voToForm(dataVO, "form", ["ideaExecutionCd","ideaCd","result","excutionDt","createDt","updateDt","deleteDt",]);
	$("#ideaCd").focus();
}

// 정렬순서저장
function saveSortOrder() {
	if(!gridToForm("list", "form", true)) return false;
	
	sendAjax({
		"url" : "${context_path}/system/system/menu/ideaReview/saveSortOrder.do",
		"data" : getFormData("form"),
		"doneCallbackFunc" : "searchList"
	});
}

// 등록
function addData() {
	$("#newForm").show();
	
	$("#titleIdeaReviewNm").text("IDEA+검토");
	
	resetForm("form", ["ideaExecutionCd","ideaCd","result","excutionDt","createDt","updateDt","deleteDt",]);
	$("#year").val($("#findYear").val());
	$("#ideaCd").focus();
}

// 저장
function saveData() {
	var f = document.form;
	if(!validatePerspectiveVO(f)) {
		return;
	}
	
	sendAjax({
		"url" : "${context_path}/system/system/menu/ideaReview/saveIdeaReview.do",
		"data" : getFormData("form"),
		"doneCallbackFunc" : "searchList"
	});
}

// 삭제
function deleteData() {
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
		"url" : "${context_path}/system/system/menu/ideaReview/deleteIdeaReview.do",
		"data" : getFormData("form"),
		"doneCallbackFunc" : "searchList"
	});
}
</script>

<form:form commandName="searchVO" id="form" name="form" method="post">
 	<form:hidden path="ideaExecutionCd"/>
	<form:hidden path="year"/>

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

	<div id="newForm">
		<div class="ptitle" id="titleIdeaReviewNm"></div>
		<div class="tbl-type02">
			<table summary="">
				<caption></caption>
				<colgroup>
					<col width="20%"/>
					<col width="80%"/>
				</colgroup>
				<tbody>
					<tr> 
						<th scope="row"><label for="ideaExecutionCd">실행안코드</label><span class="red">(*)</span></th> 
						<td ><form:input path="ideaExecutionCd" class="t-box01"/></td> 
					</tr> 
					<tr> 
						<th scope="row"><label for="ideaCd">제안번호</label></th> 
						<td ><form:input path="ideaCd" class="t-box01"/></td> 
					</tr> 
					<tr> 
						<th scope="row"><label for="result">폐기,실행</label></th> 
						<td ><form:input path="result" class="t-box01"/></td> 
					</tr> 
					<tr> 
						<th scope="row"><label for="excutionDt">실행시작일자</label></th> 
						<td ><form:input path="excutionDt" class="t-box01"/></td> 
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

