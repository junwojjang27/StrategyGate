<!-- 
*************************************************************************
* CLASS 명	: nonCamelPageNmList
* 작 업 자	: devNm
* 작 업 일	: devDate
* 기	능	: koPageNm List
* ---------------------------- 변 경 이 력 --------------------------------
* 번호	작 업 자		작	업	일			변 경 내 용				비고
* ----	---------	----------------	---------------------	-----------
*	1	devNm		devDate				최 초 작 업
**************************************************************************
-->

<!--
필독 : 다국어 지원기능에 의해서 화면에 나타나는 한글명칭은 message properties파일을 이용하여 수정할 것. 
 -->

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/common-taglibs.jsp"%>
<validator:javascript formName="camelPageNmVO" staticJavascript="false" xhtml="true" cdata="false"/>
<script type="text/javascript">
$(function(){
	$("#list").jqGrid({
		url			:	"${context_path}packageBarPath/camelPageNmList_json.do",
		postData	:	getFormData("form"),
		width		:	"${jqgrid_width}",
		height		:	"${jqgrid_height}",
		colModel	:	[
gridModel
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
	f.action = "${context_path}packageBarPath/excelDownload.do";
	f.submit();
}

// 상세 조회
function showDetail(parameter) {
	var f = document.form;
bindKeys
	
	sendAjax({
		"url" : "${context_path}packageBarPath/selectDetail.do",
		"data" : getFormData("form"),
		"doneCallbackFunc" : "setDetail"
	});
}

// 상세 조회 값 세팅
function setDetail(data) {
	$("#newForm").show();
	var dataVO = data.dataVO;
	
	$("#titlenonCamelPageNmNm").text("koPageNm : " + dataVO.lastKey);
	
	voToForm(dataVO, "form", [voToFormKey]);
	$("#lastKey").focus();
}

// 정렬순서저장
function saveSortOrder() {
	if(!gridToForm("list", "form", true)) return false;
	
	sendAjax({
		"url" : "${context_path}packageBarPath/saveSortOrder.do",
		"data" : getFormData("form"),
		"doneCallbackFunc" : "searchList"
	});
}

// 등록
function addData() {
	$("#newForm").show();
	
	$("#titlenonCamelPageNmNm").text("koPageNm");
	
	resetForm("form", [resetFormColumn]);
	$("#year").val($("#findYear").val());
	$("#lastKey").focus();
}

// 저장
function saveData() {
	var f = document.form;
	if(!validatePerspectiveVO(f)) {
		return;
	}
	
	sendAjax({
		"url" : "${context_path}packageBarPath/savenonCamelPageNm.do",
		"data" : getFormData("form"),
		"doneCallbackFunc" : "searchList"
	});
}

// 삭제
function deleteData() {
	if(deleteDataToForm("list", "lastKey", "form")) {
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
		"url" : "${context_path}packageBarPath/deletenonCamelPageNm.do",
		"data" : getFormData("form"),
		"doneCallbackFunc" : "searchList"
	});
}
</script>

<form:form commandName="searchVO" id="form" name="form" method="post">
 hiddenKey
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
		<div class="ptitle" id="titlenonCamelPageNmNm"></div>
		<div class="tbl-type02">
			<table summary="">
				<caption></caption>
				<colgroup>
					<col width="20%"/>
					<col width="80%"/>
				</colgroup>
				<tbody>
enrollmentHtml
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
