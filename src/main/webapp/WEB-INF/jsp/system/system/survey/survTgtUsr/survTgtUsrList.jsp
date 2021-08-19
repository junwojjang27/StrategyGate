<!--
*************************************************************************
* CLASS 명	: SurvTgtUsrList
* 작 업 자	: 최학룡
* 작 업 일	: 2018-10-12
* 기	능	: 설문대상자 List
* ---------------------------- 변 경 이 력 --------------------------------
* 번호	작 업 자		작	업	일			변 경 내 용				비고
* ----	---------	----------------	---------------------	-----------
*	1	최학룡		2018-10-12				최 초 작 업
**************************************************************************
-->
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/common-taglibs.jsp"%>
<validator:javascript formName="survTgtUsrVO" staticJavascript="false" xhtml="true" cdata="false"/>
<script type="text/javascript">
$(function(){
	$("#list").jqGrid({
		url			:	"${context_path}/system/system/survey/survTgtUsr/survTgtUsrList_json.do",
		postData	:	getFormData("form"),
		width		:	"${jqgrid_width}",
		height		:	"${jqgrid_height}",
		colModel	:	[
						{name:"surveyUserId",		index:"surveyUserId",	hidden:true},
						{name:"surveyUserNm",	index:"surveyUserNm",	width:150,	align:"center",	label:"<spring:message code="word.survTgtUsr"/>"},
						{name:"deptNm",			index:"deptNm",		width:400,	align:"left",		label:"<spring:message code="word.belongDept"/>"},
						{name:"posNm",				index:"posNm",			width:100,	align:"center",	label:"<spring:message code="word.pos"/>"},
						{name:"jikgubNm",			index:"jikgubNm",		width:100,	align:"center",	label:"<spring:message code="word.jikgub"/>"}

						],
		rowNum		: ${jqgrid_rownum_max},
		cellEdit	: true,
		multiselect	: true,
		loadComplete : function() {
			$("#surveyId").val($('#findSurveyId').val());
			selectCloseYn(); <%-- 마감정보 set --%>
		}
	});

	$(".tbl-btn").find("a").bind('click', function(event){
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
		$(".tbl-btn").find("a").addClass("disabled").css("cursor","default");
		$(".tbl-btn").find("a#addAllUser").removeClass("add");
		$(".tbl-btn").find("a#addData").removeClass("add");
		$(".tbl-btn").find("a#deleteData").removeClass("delete");
		$(".page-noti").removeClass("hide");
	}else{
		$(".tbl-btn").find("a").removeClass("disabled").css("cursor","pointer");
		$(".tbl-btn").find("a#addAllUser").addClass("add");
		$(".tbl-btn").find("a#addData").addClass("add");
		$(".tbl-btn").find("a#deleteData").addClass("delete");
		$(".page-noti").addClass("hide");
	}
}

// 목록 조회
function searchList() {
 	reloadGrid("list", "form");
}

// 전직원추가
function addAllUser() {
	if($("#closeYn").val() == "Y") {
		$.showMsgBox("<spring:message code="system.system.survey.closeY"/>");
		return false;
	}

	var now = new Date();
	$("#findYear").val(now.getFullYear());

	$.showConfirmBox("<spring:message code="system.system.survey.survTgtUsr.confirm"/>", function() {
		sendAjax({
			"url" : "${context_path}/system/system/survey/survTgtUsr/saveAllUser.do",
			"data" : getFormData("form"),
			"doneCallbackFunc" : "checkResult"
		});
	});
}

// 추가
function addData() {
	if($("#closeYn").val() == "Y") {
		$.showMsgBox("<spring:message code="system.system.survey.closeY"/>");
		return false;
	}

	var $grid = $("#list");
	var ids = $grid.getGridParam("reccount");

	var now = new Date();
	$("#findYear").val(now.getFullYear());

	var data = {
			"gridCall" : "Y",
			"findYear" : $("#findYear").val(),
			"targetGridId" : "list",
			"targetRowId" : ids,
			"targetUserId" : 'surveyUserId',
			"targetUserNm" : 'surveyUserNm',
			"targetDeptNm" : 'deptNm',
			"targetPosNm" : 'posNm',
			"targetJikgubNm" : 'jikgubNm'
		};

	openFancybox({
		"url" : "${context_path}/system/system/survey/survTgtUsr/popSearchUserForSurveyList.do",
		"data" : data
	});


}

<%-- 그리드 내 사용자 등록처리 --%>
function addDataProc() {
	if(!gridToForm("list", "form", true)) return false;

	sendAjax({
		"url" : "${context_path}/system/system/survey/survTgtUsr/saveSurvTgtUsr.do",
		"data" : getFormData("form"),
		"doneCallbackFunc" : "checkResult"
	});
}

// 삭제
function deleteData() {
	if($("#closeYn").val() == "Y") {
		$.showMsgBox("<spring:message code="system.system.survey.closeY"/>");
		return false;
	}

	if(deleteDataToForm("list", "surveyUserId", "form")) {
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
		"url" : "${context_path}/system/system/survey/survTgtUsr/deleteSurvTgtUsr.do",
		"data" : getFormData("form"),
		"doneCallbackFunc" : "searchList"
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
	<form:hidden path="surveyUserId"/>
	<form:hidden path="findYear"/>
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
			<a href="#" class="add" id="addAllUser" onclick="return false;"><spring:message code="button.allUserAdd"/></a>
			<a href="#" class="add" id="addData" onclick="return false;"><spring:message code="button.add"/></a>
			<a href="#" class="delete" id="deleteData" onclick="return false;"><spring:message code="button.delete"/></a>
		</div>
	</div>
	<div class="page-noti">
		<ul>
			<li><spring:message code="system.system.survey.closeY"/></li>
		</ul>
	</div>
</form:form>

