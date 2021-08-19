<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/common-taglibs.jsp"%>
<script type="text/javascript" >
var upload1;
$(function() {
	upload1 = new SGFileUploader({"targetId" : "divFile1", "inputName" : "fileNm", "maxFileCnt" : 1, "allowFileExts": "<spring:eval expression="@globals.getProperty('fileUpload.allowImgFileExts')"></spring:eval>", "maxTotalSize" : 1048576});
});

/******************************************
* 저장
******************************************/
function savePopDo() {
	if(upload1.fileCount == 0) {
		$.showMsgBox("<c:set var="messageArg"><spring:message code="word.file"/></c:set><spring:message code="errors.select" arguments="${messageArg}"/>");
		return;
	}

	sendMultipartForm({
		"url" : "${context_path}/bsc/base/strategy/strategyMapMng/popUploadBGProcess.do",
		"formId" : "formPop",
		"fileModules" : [upload1],
		"doneCallbackFunc" : "uploadDone"
	});
}

function uploadDone(data) {
	$.showMsgBox(data.msg, null, null);
	if(data.result == AJAX_SUCCESS) {
		reloadSvgChart();
		$.fancybox.close();
	}
}
</script>

<form:form commandName="searchVO" id="formPop" name="formPop" method="post" action="" enctype="multipart/form-data">
	<form:hidden path="findYear"/>
	<form:hidden path="findScDeptId"/>
	<div class="tbl-type02">
		<table summary="">
			<colgroup>
				<col width="120"/>
				<col width=""/>
			</colgroup>
			<tbody>
				<tr>
					<th scope="row"><label><spring:message code="word.year"/></label></th>
					<td><c:set var="messageArg">${searchVO.findYear}</c:set><spring:message code="unit.yearCommon" arguments="${messageArg}"/></td>
				</tr>
				<tr>
					<th scope="row"><spring:message code="word.atchFile"/></th>
					<td>
						<label class="red">※ <spring:message code="errors.imgFileOnly"/></label>
						<div id="divFile1"></div>
					</td>
				</tr>
			</tbody>
		</table>
	</div>
	<div class="tbl-bottom">
		<div class="tbl-btn">
			<a href="#" class="save" onclick="savePopDo();return false;"><spring:message code="button.upload"/></a>
		</div>
	</div>
</form:form>