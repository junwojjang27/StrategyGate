<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/common-taglibs.jsp"%>
<script type="text/javascript" >
var upload1;
$(function() {
	upload1 = new SGFileUploader({"targetId" : "divFile1", "inputName" : "fileNm", "maxFileCnt" : 1, "allowFileExts": ["xls", "xlsx"], "maxTotalSize" : 5242880});
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
		"url" : "${context_path}/example/excel/excelUploadProcess.do",
		"formId" : "formPop",
		"fileModules" : [upload1],
		"doneCallbackFunc" : "uploadDone"
	});
}

function uploadDone(data) {
	$.showMsgBox(data.msg, null, null);
	$.fancybox.close();
	reloadGrid("list");
}
</script>

<form:form commandName="searchVO" id="formPop" name="formPop" method="post" action="" enctype="multipart/form-data">
	<form:hidden path="findYear"/>
	<div class="tbl-type02">
		<table summary="">
			<caption>글 작성</caption>
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
					<th scope="row">첨부파일</th>
					<td>
						<label class="red">※ 1개, xls, xlsx만 허용, 5메가 제한</label>
						<div id="divFile1"></div>
					</td>
				</tr>
			</tbody>
		</table>
	</div>
	<div class="tbl-bottom">
		<div class="tbl-btn">
			<a href="#" class="save" onclick="savePopDo();return false;">엑셀 업로드</a>
		</div>
	</div>
</form:form>