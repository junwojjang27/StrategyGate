<!--
*************************************************************************
* CLASS 명	: SurvProgStatList
* 작 업 자	: 최학룡
* 작 업 일	: 2018-10-18
* 기	능	: 설문진행현황 List
* ---------------------------- 변 경 이 력 --------------------------------
* 번호	작 업 자		작	업	일			변 경 내 용				비고
* ----	---------	----------------	---------------------	-----------
*	1	최학룡		2018-10-18				최 초 작 업
**************************************************************************
-->
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/common-taglibs.jsp"%>
<script type="text/javascript">
$(function(){
	$("#list").jqGrid({
		url			:	"${context_path}/system/system/survey/survProgStat/survProgStatList_json.do",
		postData	:	getFormData("form"),
		width		:	"${jqgrid_width}",
		height		:	"${jqgrid_single}",
		colModel	:	[
						{name:"surveyId",			index:"surveyId",			hidden:true},
						{name:"surveyNm",			index:"surveyNm",			width:400,	align:"left",	label:"<spring:message code="word.survNm"/>"},
						{name:"surveyTypeNm",	index:"surveyTypeNm",	width:100,	align:"center",	label:"<spring:message code="word.survType"/>"},
						{name:"surveyDate",		index:"surveyDate",			width:200,	align:"center",	label:"<spring:message code="word.survPeriod"/>"},
						{name:"targetCnt",			index:"targetCnt",			width:100,	align:"center",	label:"<spring:message code="word.survTgtCnt"/>"},
						{name:"answerCnt",			index:"answerCnt",			width:100,	align:"center",	label:"<spring:message code="word.survAnsCnt"/>"},
						{name:"answerRate",		index:"answerRate",			width:100,	align:"center",	label:"<spring:message code="word.survAnsRate"/>"},
						{name:"closeYn",			index:"closeYn",				hidden:true},
						{name:"closeNm",			index:"closeNm",			width:100,	align:"center",	label:"<spring:message code="word.close"/>",
							formatter:function(cellvalue, options, rowObject) {
								return "<a href='#' onclick='finishData(\"" + removeNull(rowObject.surveyId) + "\",\"" + removeNull(rowObject.closeYn) + "\",\"" + removeNull(rowObject.surveyYear) + "\");return false;'>" + escapeHTML(removeNull(cellvalue)) + "</a>";
							},
							unformat:linkUnformatter
						},
						{name:"surveyYear",		index:"surveyYear",				hidden:true}
						],
		rowNum		: ${jqgrid_rownum_max},
		cellEdit	: true,
		multiselect	: true,
		loadComplete : function() {
		}
	});
});

// 목록 조회
function searchList() {
 	reloadGrid("list", "form");
}

<%-- 마감/마감취소 --%>
function finishData(surveyId, closeYn, surveyYear) {
	var f = document.form;
	f.surveyId.value = surveyId;
	f.closeYn.value = closeYn;
	f.surveyYear.value = surveyYear;

	sendAjax({
		"url" : "${context_path}/system/system/survey/survProgStat/saveSurvProgStat.do",
		"data" : getFormData("form"),
		"doneCallbackFunc" : "checkResult"
	});
}

<%-- 미응답자메일발송 --%>
function survPopSendMail() {
	if(deleteDataToForm("list", "surveyId", "form")) {
		var $grid = $("#list");
		var ids = $grid.jqGrid("getGridParam", "selarrrow");
		var rowObj = "";
		var chkYn = "";
		var chkCloseYn = 0, chkCnt = 0;
		$(ids).each(function(i,el){
			rowObj = $grid.jqGrid("getRowData",el);
			if(rowObj.closeYn == "Y") {
				chkCloseYn++;
			}
			chkYn = $grid.find("input.cbox#jqg_list_"+el).is(':checked');
			if(chkYn) {
				chkCnt++;
				$("#keySurveyId").val(rowObj.surveyId);
			}
		});
		if(chkCloseYn > 0) {
			$.showMsgBox("<spring:message code="system.system.survey.survProcStat.error1"/>");
			return false;
		}
		if(chkCnt > 1) {
			$.showMsgBox("<spring:message code="system.system.survey.survProcStat.error2"/>");
			return false;
		}

		doSendMail();
	}
}

<%-- 발송 처리 --%>
function doSendMail() {
	openFancybox({
		"url" : "${context_path}/system/system/survey/survProgStat/survPopSendMail.do",
		"data" : getFormData("form")
	});
}

<%-- 요청처리 callback --%>
function checkResult(data) {
	$.showMsgBox(data.msg);
	if(data.result == AJAX_SUCCESS) {
		searchList();
	}
}
</script>

<form:form commandName="searchVO" id="form" name="form" method="post">
 	<form:hidden path="surveyId"/>
 	<form:hidden path="closeYn"/>
 	<form:hidden path="surveyYear"/>
 	<form:hidden path="keySurveyId"/>

	<div class="sch-bx">
		<ul>
			<li>
				<label for="findSurveyId"><spring:message code="word.survNm"/></label>
				<form:select path="findSurveyId" class="select wx400" >
					<option value=""><spring:message code="word.all"/></option>
					<form:options items="${surveyList}"  itemLabel="surveyNm" itemValue="surveyId" />
				</form:select>
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
	<div class="tbl-bottom">
		<div class="tbl-btn">
			<a href="#" class="save" onclick="survPopSendMail();return false;"><spring:message code="button.noResponseSendMail"/></a>
		</div>
	</div>
</form:form>

