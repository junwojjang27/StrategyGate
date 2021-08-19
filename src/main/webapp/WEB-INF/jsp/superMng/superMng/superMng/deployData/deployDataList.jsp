<!-- 
*************************************************************************
* CLASS 명	: DeployDataList
* 작 업 자	: 현걸욱
* 작 업 일	: 2018-07-09
* 기	능	: 고객사별 전년데이터 일괄적용 List
* ---------------------------- 변 경 이 력 --------------------------------
* 번호	작 업 자		작	업	일			변 경 내 용				비고
* ----	---------	----------------	---------------------	-----------
*	1	현걸욱		2018-07-09				최 초 작 업
**************************************************************************
-->

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/common-taglibs.jsp"%>
<validator:javascript formName="deployDataVO" staticJavascript="false" xhtml="true" cdata="false"/>
<script type="text/javascript">
<c:set var="pastYear">parseInt($("#findyear").val())-1</c:set>
<c:set var="newYear">$("#findyear").val()</c:set>
$(function(){
	$("#list").jqGrid({
		url			:	"${context_path}/superMng/superMng/superMng/deployData/deployDataList_json.do",
		postData	:	getFormData("form"),
		width		:	"${jqgrid_width}",
		height		:	"${jqgrid_height}",
		colModel	:	[
						{name:"compId",	index:"compId",	width:100,	align:"center",	label:"<spring:message code="word.compId" />"},
						{name:"compNm",	index:"compNm",	width:100,	align:"center",	label:"<spring:message code="word.compNm" />"},
						{name:"hasPastDataYn",	index:"hasPastDataYn",	width:100,	align:"center",	label:"<spring:message code="word.hasYearData" />"},
						{name:"hasNewDataYn",	index:"hasNewDataYn",	width:100,	align:"center",	label:"<spring:message code="word.hasYearData" />"}

						],
		rowNum		: ${jqgrid_rownum_max},
		pager		: "pager",
		multiselect : true,
		loadComplete : function() {
			var colModel = $("#list").jqGrid("getGridParam","colModel");
			var str1 = colModel[3].label;
			var str2 = colModel[4].label;
			
			$("#list").jqGrid('setLabel', "hasPastDataYn",str1.replace("{0}",parseInt($("#findYear").val())-1)); 
			$("#list").jqGrid('setLabel', "hasNewDataYn",str2.replace("{0}",$("#findYear").val()));
			
		}
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
	f.action = "${context_path}/superMng/superMng/superMng/deployData/excelDownload.do";
	f.submit();
}

function insertPastYearData(){
	var ids = $("#list").jqGrid("getGridParam","selarrrow");

	if(ids.length === 0){
		$.showMsgBox("<spring:message code="errors.noSelectedData"/>");
		return false;
	}
	
	$.showConfirmBox("<spring:message code="common.beforeCopy.msg"/>",insertPastYearDo);
}

function insertPastYearDo(){
	
	var compIds = "";
	var ids = $("#list").jqGrid("getGridParam","selarrrow");

	if(ids.length === 0){
		$.showMsgBox("<spring:message code="errors.noSelectedData"/>");
		return false;
	}
	
	var rowData;
	$(ids).each(function(i,e){
		rowData = $("#list").jqGrid("getRowData",e);
		compIds += rowData.compId+"\|";
	});
	
	$("#compIds").val(compIds);
	
	sendAjax({
		"url" : "${context_path}/superMng/superMng/superMng/deployData/saveDeployData.do",
		"data" : getFormData("form"),
		"doneCallbackFunc" : "checkResult"
	});
	
}

</script>

<form:form commandName="searchVO" id="form" name="form" method="post">
	<form:hidden path="compIds" />
	<div class="sch-bx">
		<ul>
			<li>
				<label for="findYear"><spring:message code="word.year"/></label>
				<form:select path="findYear" class="select wx100" items="${codeUtil:getCodeList('017')}" itemLabel="codeNm" itemValue="codeId">
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
			<a href="#" class="save" onclick="insertPastYearData();return false;"><spring:message code="button.applyAll"/></a>
		</div>
	</div>
</form:form>

