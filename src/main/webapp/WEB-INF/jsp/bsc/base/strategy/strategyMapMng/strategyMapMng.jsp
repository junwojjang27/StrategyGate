<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/common-taglibs.jsp"%>
<link href="${theme_path}/svgChart.css" type="text/css" rel="stylesheet"/>
<script type="text/javascript" src="${js_path}/d3.min.js"></script>
<script type="text/javascript" src="${js_path}/svgChart/svgChart.js"></script>
<script type="text/javascript">
$(function() {
	$("#toolbardrag").draggable();
	
	setMaxLength("form");
	reloadSvgChart();
});

var svgChart;
function reloadSvgChart() {
	sendAjax({
		"url" : "${context_path}/bsc/base/strategy/strategyMapMng/getData.do",
		"data" : getFormData("form"),
		"doneCallbackFunc" : "setInfo"
	});

	var findYear = $("#findYear").val();
	var findScDeptId = $("#findScDeptId").val();
	
	$("#chart").empty();
	svgChart = new SvgChart({
		"width" : 1200,
		"height" : 500,
		"targetId" : "chart",
		"imgPath" : "${context_path}/images/backgrounds/",
		"url" : "${context_path}/bsc/base/strategy/strategyMapMng/strategyMapMng_xml.do?suffix=jsp%26findYear=" + findYear + "%26metric_gbn=BSC%26findScDeptId=" + findScDeptId + "%26admin_bool=_StartegyAdmin_%26gubun=0%26img_url=${root_img_path}%26context_url=${context_path}",
		"trafficUrl" : "${context_path}/bsc/mon/org/strategyMap/trafficSignal_xml.do?findYear=" + findYear + "&gubun=0",
		"saveUrl" : "${context_path}/bsc/base/strategy/strategyMapMng/saveStrategyMap.do",
		"mode" : "EDIT",
		"showMetric" : false
	});
}

function setInfo(data) {
	$("#vision").val(data.info.vision);
	$("#mission").val(data.info.mission);
}

function addHLine() {svgChart.addHLine();}
function removeHLine() {svgChart.removeHLine();}
function resetArrows() {svgChart.resetArrows();}
function hideArrows() {svgChart.hideArrows();}
function addArrow() {svgChart.addArrow();}
function removeArrow() {svgChart.removeArrow();}
function changeBox(type) {svgChart.changeBox(type);}

// 새로고침
function reloadChart() {
	svgChart.init();
}

// 저장
function saveSvgChart() {
	var data = {
		"findYear"	: $("#findYear").val(),
		"findScDeptId" : $("#findScDeptId").val(),
		"vision"	:	$("#vision").val(),
		"mission"	:	$("#mission").val(),
		"_csrf" : getCsrf()
	}
	svgChart.saveChart(data);
}

// 배경로딩
function popUploadBG() {
	openFancybox({
		"url" : "${context_path}/bsc/base/strategy/strategyMapMng/popUploadBG.do",
		"data" : {
					"findYear" : $("#findYear").val(),
					"findScDeptId" : $("#findScDeptId").val()
				}
	});
}

// 배경삭제
function deleteBG() {
	$.showConfirmBox("<spring:message code="common.deleteBg.msg"/>", "deleteBGProcess");
}

function deleteBGProcess() {
	sendAjax({
		"url" : "${context_path}/bsc/base/strategy/strategyMapMng/deleteBGProcess.do",
		"data" : getFormData("form"),
		"doneCallbackFunc" : "deleteBGCallback"
	});
}

function deleteBGCallback(data) {
	$.showMsgBox(data.msg, null, null);
	if(data.result == AJAX_SUCCESS) {
		window.location.reload();
	}
}

<sec:authorize access="hasRole('01')">
// 전년도 데이터 복사
function copyData() {
	var findYear = parseInt($("#findYear").val(), 10);
	var lastYear = findYear - 1;
	
	$.showConfirmBox(getMessage("common.confirm.copyPastData.msg", lastYear, findYear, findYear), function() {
		sendAjax({
			"url" : "${context_path}/common/copyPastData.do",
			"data" : getFormData("form"),
			"doneCallbackFunc" : "reloadSvgChart"
		});
	});
}
</sec:authorize>
</script>
<form:form commandName="searchVO" id="form" name="form" method="post">
	<form:hidden path="findScDeptId" value=""/>
	<sec:authorize access="hasRole('01')">
		<input type="hidden" id="tableId" name="tableId" value="BSC_STRATEGY_MAP"/>
	</sec:authorize>
	<div class="sch-bx">
		<ul>
			<li>
				<label for="findYear"><spring:message code="word.year"/></label>
				<form:select path="findYear" class="select wx100" items="${codeUtil:getCodeList('017')}" itemLabel="codeNm" itemValue="codeId">
				</form:select>
			</li>
			<li>
				<div class="sch-oga" id="findScDeptList"></div>
			</li>
			<li>
				<div class="btn-dw">
					<a href="#" onclick="popUploadBG();return false;"><span><spring:message code="button.loadBg"/></span></a>
					<a href="#" onclick="deleteBG();return false;"><span><spring:message code="button.deleteBg"/></span></a>
				</div>
			</li>
		</ul>
		<a href="#" class="btn-sch" onclick="reloadSvgChart();return false;"><spring:message code="button.search"/></a>
	</div>
	<div class="btn-dw">
	</div>
	<div class="table-bx2">
		<div id="chart"></div>
	</div>
	<div class="tbl-bottom mt0" >
		<sec:authorize access="hasRole('01')">
			<div class="tbl-wbtn">
				<a href="#" class="delete" onclick="copyData();return false;"><span><spring:message code="button.copyPastData"/></span></a>
			</div>
		</sec:authorize>
		<div class="tbl-btn">
			<a href="#" class="new" onclick="reloadChart();return false;"><spring:message code="button.refresh"/></a>
			<a href="#" class="save" onclick="saveSvgChart();return false;"><spring:message code="button.save"/></a>
		</div>
	</div>
	<div id="toolbardrag" class="strategyToolBar-div">
		<div class="tbl-rbtn">
			<div class="title"><spring:message code="word.controllBar"/></div>
			<div class="subtitle1">I&nbsp;&nbsp;<spring:message code="button.hLine"/></div>
			<a href="#" class="btn" onclick="addHLine();return false;"><spring:message code="button.hLineAdd"/></a>
			<a href="#" class="btn" onclick="removeHLine();return false;"><spring:message code="button.hLineDelete"/></a>
			<div class="subtitle2">I&nbsp;&nbsp;<spring:message code="button.arrow"/></div>
			<a href="#" class="btn" onclick="resetArrows();return false;"><spring:message code="button.arrowReset"/></a>
			<a href="#" class="btn" onclick="hideArrows();return false;"><spring:message code="button.arrowHide"/></a>
			<a href="#" class="btn" onclick="addArrow();return false;"><spring:message code="button.arrowAdd"/></a>
			<a href="#" class="btn" onclick="removeArrow();return false;"><spring:message code="button.arrowDelete"/></a>
			<div class="subtitle2">I&nbsp;&nbsp;<spring:message code="word.strategy"/></div>
			<a href="#" class="btn" onclick="changeBox('Symbol1');return false;"><spring:message code="button.boxOval"/></a>
			<a href="#" class="btn" onclick="changeBox('Symbol2');return false;"><spring:message code="button.boxRound"/></a>
			<a href="#" class="btn" onclick="changeBox('Symbol3');return false;"><spring:message code="button.boxCircle"/></a>
			<a href="#" class="btn" onclick="changeBox('Symbol4');return false;"><spring:message code="button.boxTransparent"/></a>
		</div>
	</div>
</form:form>