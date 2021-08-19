<!-- 
*************************************************************************
* CLASS 명	: YearBatchList
* 작 업 자	: 현걸욱
* 작 업 일	: 2018-04-23
* 기	능	: 년배치 List
* ---------------------------- 변 경 이 력 --------------------------------
* 번호	작 업 자		작	업	일			변 경 내 용				비고
* ----	---------	----------------	---------------------	-----------
*	1	현걸욱		2018-04-23				최 초 작 업
**************************************************************************
-->
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/common-taglibs.jsp"%>
<validator:javascript formName="yearBatchVO" staticJavascript="false" xhtml="true" cdata="false"/>
<script type="text/javascript">
$(function(){
	$("#list").jqGrid({
		url			:	"${context_path}/system/system/batch/yearBatch/yearBatchList_json.do",
		postData	:	getFormData("form"),
		width		:	"${jqgrid_width}",
		height		:	"${jqgrid_height}",
		colModel	:	[
						{name:"procId",		index:"procId",		width:100,	align:"center",	label:"<spring:message code="word.workCode"/>"},
						{name:"procNm",		index:"procNm",		width:100,	align:"center",	label:"<spring:message code="word.work"/>"},
						{name:"execDate",	index:"execDate",	width:100,	align:"center",	label:"<spring:message code="word.finalActionDt"/>"},
						{name:"execYn",		index:"execYn",		width:100,	align:"center",	label:"<spring:message code="word.status"/>", formatter:linkStatus},
						{name:"exec",		index:"exec",		width:100,	align:"center",	label:"<spring:message code="word.reAction"/>", formatter:linkExec, title:false}
						],
		rowNum		: ${jqgrid_rownum_max},
		multiselect	: false
	});
});

// 목록 조회
function searchList() {
 	reloadGrid("list", "form");
}

function checkResult(data) {
	$.showMsgBox(data.msg);
	if(data.result == AJAX_SUCCESS) {
		searchList();
	}
}

// 엑셀 다운로드
function excelDownload() {
	var f = document.form;
	f.action = "${context_path}/system/system/batch/yearBatch/excelDownload.do";
	f.submit();
}

/******************************************
 * jqGrid 상태
 ******************************************/
function linkStatus(cellvalue, options, rowObject) {
	var status="";
	if(cellvalue!=""){
		status= cellvalue==""?"<spring:message code="word.normal"/>":"<spring:message code="word.noNormal"/>";
	}
	return cellvalue;
}

/******************************************
 * jqGrid 집계 링크
 ******************************************/
function linkExec(cellvalue, options, rowObject) {
	var ret = $("#list").jqGrid('getRowData',options.rowId);
	return "<input type=\'button\' class='pButton3_grid' value=\'<spring:message code="button.totalProcess"/>\' onclick=\"goExecute("+options.rowId+")\")\">";
}

/******************************************
 * 집계
 ******************************************/
function goExecute(rowId) {
	var form = $("#form")[0];
	var ret = $("#list").jqGrid('getRowData',rowId);

	$(form.procId).val(ret.procId);
	
	sendAjax({
		"url" : "${context_path}/system/system/batch/yearBatch/execYearBatch.do",
		"data" : getFormData("form"),
		"doneCallbackFunc" : "checkResult"
	});
	
	
}

// 저장
function execData() {
	sendAjax({
		"url" : "${context_path}/system/system/batch/yearBatch/execYearBatch.do",
		"data" : getFormData("form"),
		"doneCallbackFunc" : "checkResult"
	});
}
</script>

<form:form commandName="searchVO" id="form" name="form" method="post">
 	<form:hidden path="procId"/>

	<div class="sch-bx">
		<ul>
			<li>
				<label for="findYear"><spring:message code="word.baseYearMon"/></label>
					<form:select path="findYear"  class="select wx80" items="${codeUtil:getCodeList('017')}"  itemLabel="codeNm"  itemValue="codeId">
					</form:select>
					<form:select path="findMon"  class="select wx80" items="${codeUtil:getCodeList('024')}"  itemLabel="codeNm"  itemValue="codeId">
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
</form:form>

