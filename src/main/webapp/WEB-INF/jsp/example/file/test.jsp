<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/common-taglibs.jsp"%>
<script type="text/javascript">
function doSave() {
	sendMultipartForm({
		"url" : "${context_path}/example/file/saveFiles.do",
		"formId" : "f",
		"fileModules" : [upload1, upload2],
		"doneCallbackFunc" : "doneCallback"
	});
	return false;
}

function doneCallback(json) {
	$.showMsgBox(json.msg);
}

var upload1, upload2;
$(function() {
	upload1 = new SGFileUploader({"targetId" : "divFile1", "inputName" : "upFile1", "maxFileCnt" : 3, "allowFileExts": "<spring:eval expression="@globals.getProperty('fileUpload.allowImgFileExts')"></spring:eval>", "maxTotalSize" : 524288000});
	<c:import url="/common/fileList.do" charEncoding="utf-8">
		<c:param name="moduleName" value="upload1"/>
		<c:param name="canDelete" value="Y"/>
		<c:param name="param_atchFileId" value="FILE_000000000000071"/>
	</c:import>
	
	upload2 = new SGFileUploader({"targetId" : "divFile2", "inputName" : "upFile2", "maxFileCnt" : 5, "maxTotalSize" : 524288000});
});
</script>

<form:form commandName="searchVO" id="f" name="f" method="post" enctype="multipart/form-data">
	<div class="ptitle">다중 파일 업로드</div>
	<div class="tbl-type02">
		<table summary="">
			<caption>다중 파일 업로드</caption>
			<tbody>
				<tr>
					<th scope="row"><label>파일첨부1</label></th>
					<td>
						<label class="red">※ 3개, "<spring:eval expression="@globals.getProperty('fileUpload.allowFileExts')"></spring:eval>"만 허용, 5메가 제한</label>
						<div id="divFile1"></div>
					</td>
				</tr>
				<tr>
					<th scope="row"><label>파일첨부2</label></th>
					<td>
						<label class="red">※ 5개, 10메가 제한</label>
						<div id="divFile2"></div>
					</td>
				</tr>
			</tbody>
		</table>
	</div>
	<div class="tbl-bottom">
		<div class="tbl-btn">
			<a href="#" class="save" onclick="doSave();return false;">저장</a>
		</div>
	</div>
</form:form>

<div class="ptitle">다운로드 전용</div>
<div class="tbl-type02">
	<table summary="">
		<caption>다운로드 전용</caption>
		<tbody>
			<tr>
				<th scope="row"><label>파일첨부</label></th>
				<td>
					<div id="divFile3"></div>
					<c:import url="/common/fileList.do" charEncoding="utf-8">
						<c:param name="downloadOnly" value="Y"/>
						<c:param name="param_atchFileId" value="FILE_000000000000072"/>
					</c:import>
				</td>
			</tr>
		</tbody>
	</table>
</div>
