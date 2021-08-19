<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/common-taglibs.jsp"%>
<link href="${css_path}/theme/${theme}/svgChart.css" type="text/css" rel="stylesheet"/>
<script type="text/javascript" src="${js_path}/d3.min.js"></script>
<script type="text/javascript" src="${js_path}/svgChart/svgOrg.js"></script>
<script type="text/javascript">
$(function() {
	reloadSvgOrg();
});

var svgOrg;
function reloadSvgOrg() {
	var params = getFormData("form");
	
	$("#svgOrg").empty();
	svgOrg = new SvgOrg({
		"mode"	: "EDIT",
		"width"	: 1200,
		"imgPath" : "${context_path}/images/backgrounds/",
		"targetId" : "svgOrg",
		"url" : "${context_path}/bsc/base/scDept/scDeptDiagMng/scDeptDiagMng_xml.do?" + params,
		"signalUrl" : "${context_path}/bsc/base/scDept/scDeptDiagMng/trafficSignal_xml.do?" + params,
		"saveUrl" : "${context_path}/bsc/base/scDept/scDeptDiagMng/saveScDeptDiag.do",
		"hideDefaultBtn" : true
	});
}

// 저장
function saveSvgOrg() {
	var data = {
		"findYear" : $("#findYear").val(),
		"findScDeptId" : $("#findScDeptId").val(),
		"_csrf" : getCsrf()
	}
	svgOrg.saveChart(data);
}

// 배경로딩
function popUploadBG() {
	openFancybox({"url" : "${context_path}/bsc/base/scDept/scDeptDiagMng/popUploadBG.do", "data" : {"findYear" : $("#findYear").val()}});
}

// 배경삭제
function deleteBG() {
	$.showConfirmBox("<spring:message code="common.deleteBg.msg"/>", "deleteBGProcess");
}

function deleteBGProcess() {
	sendAjax({
		"url" : "${context_path}/bsc/base/scDept/scDeptDiagMng/deleteBGProcess.do",
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
</script>
<form:form commandName="searchVO" id="form" name="form" method="post">
	<c:set var="defaultRootScDeptId"><spring:eval expression="@globals.getProperty('default.rootScDeptId')"></spring:eval></c:set>
	<form:hidden path="findScDeptId" value="${defaultRootScDeptId}"/>
	<div class="sch-bx">
		<ul>
			<li>
				<label for="findYear"><spring:message code="word.year"/></label>
				<form:select path="findYear" class="select wx100" items="${codeUtil:getCodeList('017')}" itemLabel="codeNm" itemValue="codeId">
				</form:select>
			</li>
			<li>
				<div class="btn-dw">
					<a href="#" onclick="popUploadBG();return false;"><span><spring:message code="button.loadBg"/></span></a>
					<a href="#" onclick="deleteBG();return false;"><span><spring:message code="button.deleteBg"/></span></a>
				</div>
			</li>
		</ul>
		<a href="#" class="btn-sch" onclick="reloadSvgOrg();return false;"><spring:message code="button.search"/></a>
	</div>
	<div class="btn-dw">
	</div>
	<div class="table-bx2">
		<div id="svgOrg"></div>
	</div>
	<div class="tbl-bottom tbl-bottom2 mt0">
		<div class="tbl-btn">
			<a href="#" class="save" onclick="saveSvgOrg();return false;"><spring:message code="button.save"/></a>
		</div>
	</div>
</form:form>