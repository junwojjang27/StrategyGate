<!--
*************************************************************************
* CLASS 명	: SurvQuesGrpList
* 작 업 자	: 최학룡
* 작 업 일	: 2018-10-11
* 기	능	: 설문질문그룹 List
* ---------------------------- 변 경 이 력 --------------------------------
* 번호	작 업 자		작	업	일			변 경 내 용				비고
* ----	---------	----------------	---------------------	-----------
*	1	최학룡		2018-10-11				최 초 작 업
**************************************************************************
-->
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/common-taglibs.jsp"%>
<validator:javascript formName="survQuesGrpVO" staticJavascript="false" xhtml="true" cdata="false"/>
<script type="text/javascript">
$(function(){
	$("#list").jqGrid({
		url			:	"${context_path}/system/system/survey/survQuesGrp/survQuesGrpList_json.do",
		postData	:	getFormData("form"),
		width		:	"${jqgrid_width}",
		height		:	"${jqgrid_height2}",
		colModel	:	[
						{name:"isNew",		index:"isNew",	hidden:true},
						{name:"quesGrpId",		index:"quesGrpId",		hidden:true},
						{name:"quesGrpNm",	index:"quesGrpNm",	width:300,	align:"left",	label:"<spring:message code="word.survQuesGrp"/>",
							editable:true, edittype:"text", editrules:{required:true}, editoptions:{maxlength:300}
						},
						{name:"mapQuesCnt",	index:"mapQuesCnt",	width:100,	align:"center",	label:"<spring:message code="word.mapQuesCnt"/>"},
						{name:"sortOrder",		index:"sortOrder",		width:100,	align:"center",	label:"<spring:message code="word.sortOrder"/>",
							editable:true, edittype:"text", editrules:{required:true, integer:true}, editoptions:{maxlength:5}
						}
						],
		rowNum		: ${jqgrid_rownum_max},
		sortname	: "sortOrder",
		sortorder	: "asc",
		cellEdit	: true,
		multiselect	: true,
		loadComplete : function() {
		}
	});

	$("#listSub").jqGrid({
		url			:	"${context_path}/system/system/survey/survQuesGrp/survQuesGrpForMapList_json.do",
		postData	:	getFormData("form"),
		width		:	"${jqgrid_width}",
		height		:	"${jqgrid_dual}",
		colModel	:	[
						{name:"quesGrpId",	index:"quesGrpId",		width:250,	align:"left",	label:"<spring:message code="word.survQuesGrp"/>",
							editable:true, edittype:"select", formatter:"select", editrules:{required:true},
							editoptions:{value:{
								<c:forEach var="item" items="${quesGrpList}" varStatus="status">"${item.quesGrpId}":"${item.quesGrpNm}"<c:if test="${not status.last}">,</c:if></c:forEach>
								<c:if test="${empty quesGrpList}">"":""</c:if>
							}}
						},
						{name:"quesSeq",		index:"quesSeq",	width:100,	align:"center",	label:"<spring:message code="word.questionNum"/>"},
						{name:"quesId",			index:"quesId",		hidden:true},
						{name:"quesNm",		index:"quesNm",	width:500,	align:"left",	label:"<spring:message code="word.question"/>"},
						],
		rowNum		: ${jqgrid_rownum_max},
		sortname	: "quesSeq",
		sortorder	: "asc",
		cellEdit	: true,
		loadComplete : function() {
			$("#surveyId").val($('#findSurveyId').val());
			selectCloseYn(); <%-- 마감정보 set --%>
		}
	});

	$(".tbl-btn").find("a").not(".view").bind('click', function(event){
		if($("#closeYn").val() == "Y") {
			event.preventDefault();
			return false;
		}else{
			eval($(this).attr("id")+"()");
		}
	});
});

function selectCloseYn() {
	sendAjax({
		"url" : "${context_path}/system/system/survey/survReg/selectDetail.do",
		"data" : getFormData("form"),
		"doneCallbackFunc" : "setCloseYn"
	});
}

function setCloseYn(data) {
	$("#closeYn").val(data.dataVO.closeYn);
	if(data.dataVO.closeYn == "Y") {
		$(".tbl-btn").find("a:not(.view)").addClass("disabled").css("cursor","default");
		$(".tbl-btn").find("a#addData").removeClass("add");
		$(".tbl-btn").find("a#saveData").removeClass("save");
		$(".tbl-btn").find("a#deleteData").removeClass("delete");
		$(".tbl-btn").find("a#saveDataSub").removeClass("save");
		$(".page-noti").removeClass("hide");
	}else{
		$(".tbl-btn").find("a:not(.view)").removeClass("disabled").css("cursor","pointer");
		$(".tbl-btn").find("a#addData").addClass("add");
		$(".tbl-btn").find("a#saveData").addClass("save");
		$(".tbl-btn").find("a#deleteData").addClass("delete");
		$(".tbl-btn").find("a#saveDataSub").addClass("save");
		$(".page-noti").addClass("hide");
	}
}

// 목록 조회
function searchList() {
 	reloadGrid("list", "form");
 	reloadSelBox();
 	reloadGrid("listSub", "form");
}

function reloadSelBox() {
	sendAjax({
		"url" : "${context_path}/system/system/survey/survQuesGrp/selectQuesGrpList.do",
		"data" : getFormData("form"),
		"doneCallbackFunc" : function(data) {
			var colModel = $("#listSub").jqGrid("getGridParam", "colModel");
			var opt = {};
			$(data.list).each(function(i, e) {
				opt[e.quesGrpId] = e.quesGrpNm;
			});
			colModel[0].editoptions.value = opt;
		}
	});
}

// 상세 조회 값 세팅
function setDetail(data) {
	var dataVO = data.dataVO;

	voToForm(dataVO, "form", ["surveyId","quesGrpId","quesGrpNm","sortOrder","createDt"]);
	$("#quesGrpId").focus();
}

// 등록
function addData() {
	var rowData = {isNew:"Y", sortOrder:''};

	$("#list").jqGrid("addRowData", ("newRow"+$.jgrid.guid++), rowData, "last");
	$('#list tr:last').focus();
}

// 저장
function saveData() {
	var ids = $("#list").jqGrid("getDataIDs");
	if(ids.length == 0) {
		$.showMsgBox("<spring:message code="errors.noDataToSave"/>");
		return false;
	}

	if(!gridToFormChanged("list", "form", true)) return false;

	$.showConfirmBox("<spring:message code="common.save.msg"/>", function() {
		sendAjax({
			"url" : "${context_path}/system/system/survey/survQuesGrp/saveSurvQuesGrp.do",
			"data" : getFormData("form"),
			"doneCallbackFunc" : "checkResult"
		});
	});
}

// 매핑 저장
function saveDataSub() {
	var ids = $("#listSub").jqGrid("getDataIDs");
	if(ids.length == 0) {
		$.showMsgBox("<spring:message code="errors.noDataToSave"/>");
		return false;
	}
	if(!gridToFormChanged("listSub", "form", true)) return false;

	$.showConfirmBox("<spring:message code="common.save.msg"/>", function() {
		sendAjax({
			"url" : "${context_path}/system/system/survey/survQuesGrp/saveSurvQuesGrpForMap.do",
			"data" : getFormData("form"),
			"doneCallbackFunc" : "checkResult"
		});
	});
}

// 삭제
function deleteData() {
	if(deleteGridToForm("list", "quesGrpId", "form")) {
		$.showConfirmBox("<spring:message code="common.delete.msg"/>", "doDeleteData");
	}
}

// 삭제 처리
function doDeleteData() {
	sendAjax({
		"url" : "${context_path}/system/system/survey/survQuesGrp/deleteSurvQuesGrp.do",
		"data" : getFormData("form"),
		"doneCallbackFunc" : "checkResult"
	});
}

<%-- 미리보기 --%>
function previewData() {
	openFancybox({
		"url" : "${context_path}/system/system/survey/survQuesGrp/popSurvActionList.do",
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

<style>
	select[name="quesGrpId"]{
		width: 99% !important;
	}
</style>

<form:form commandName="searchVO" id="form" name="form" method="post">
 	<form:hidden path="surveyId"/>
	<form:hidden path="quesGrpId"/>
	<form:hidden path="closeYn"/><%-- 마감체크용 --%>

	<div class="sch-bx">
		<ul>
			<li>
				<label for="findSurveyId"><spring:message code="word.survNm"/></label>
				<form:select path="findSurveyId" class="select wx400" >
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
			<a href="#" class="add" id="addData" onclick="return false;"><spring:message code="button.add"/></a>
			<a href="#" class="save" id="saveData" onclick="return false;"><spring:message code="button.save"/></a>
			<a href="#" class="delete" id="deleteData" onclick="return false;"><spring:message code="button.delete"/></a>
		</div>
	</div>
	<div class="page-noti">
		<ul>
			<li><spring:message code="system.system.survey.closeY"/></li>
		</ul>
	</div>

	<div id="newForm">
		<div class="ptitle" id="titleSurvQuesGrpNm"><spring:message code="word.survQuesGrpForMap"/></div>
		<div class="gridContainer">
			<table id="listSub"></table>
		</div>
		<div class="tbl-bottom">
			<div class="tbl-wbtn">
			</div>
			<div class="tbl-btn">
				<a href="#" class="view" onclick="previewData();return false;"><spring:message code="button.preview"/></a>
				<a href="#" class="save" id="saveDataSub" onclick="return false;"><spring:message code="button.save"/></a>
			</div>
		</div>
	</div>
</form:form>

