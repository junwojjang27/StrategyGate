<!-- 
*************************************************************************
* CLASS 명	: popExcelUpload
* 작 업 자	: kimyh
* 작 업 일	: 2018-07-30
* 기	능	: 사용자관리 엑셀업로드
* ---------------------------- 변 경 이 력 --------------------------------
* 번호	작 업 자		작	업	일			변 경 내 용				비고
* ----	---------	----------------	---------------------	-----------
*	1	kimyh		2018-07-30				최 초 작 업
**************************************************************************
-->
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/common-taglibs.jsp"%>
<script type="text/javascript">
var upload1;
$(function() {
	upload1 = new SGFileUploader({"targetId" : "divFile1", "inputName" : "fileNm", "maxFileCnt" : 1, "allowFileExts": ["xls", "xlsx"], "maxTotalSize" : 5242880});
});

// 엑셀 업로드
function popExcelUpload() {
	if(upload1.fileCount == 0) {
		$.showMsgBox("<c:set var="messageArg"><spring:message code="word.file"/></c:set><spring:message code="errors.select" arguments="${messageArg}"/>");
		return;
	}

	sendMultipartForm({
		"url" : "${context_path}/system/system/comp/compUserMng/popExcelUpload.do",
		"formId" : "popForm",
		"fileModules" : [upload1],
		"doneCallbackFunc" : "popCheckResult"
	});
}

function popCheckResult(data) {
	$.showMsgBox(data.msg);
	if(data.result == AJAX_SUCCESS) {
		upload1.reset();
		searchList();
		$.fancybox.close();
	}
}
</script>

<div class="popup wx800">
<p class="title"><spring:message code="word.userMng"/></p>
<form:form commandName="searchVO" id="popForm" name="popForm" method="post">
	<div class="sch-bx">
		<ul>
			<li>
				<label for="findYear"><spring:message code="word.year"/> (<spring:message code="word.org"/>)</label>
				<form:select path="findYear" class="select wx100" items="${codeUtil:getCodeList('017')}" itemLabel="codeNm" itemValue="codeId">
				</form:select>
			</li>
		</ul>
	</div>
	<div id="popExcelUploadForm" class="tbl-type02 mt10">
		<table summary="">
			<colgroup>
				<col width="120"/>
				<col width=""/>
			</colgroup>
			<tbody>
				<tr>
					<th scope="row"><spring:message code="word.atchFile"/></th>
					<td>
						<div id="divFile1"></div>
					</td>
				</tr>
			</tbody>
		</table>
	</div>
	<div class="tbl-bottom mt0 mb10">
		<div class="tbl-btn">
			<a href="#" class="save" onclick="popExcelUpload();return false;"><spring:message code="button.excelUpload"/></a>
			<a href="#" class="delete" onclick="$.fancybox.close();return false;"><spring:message code="button.cancel"/></a>
		</div>
	</div>
</form:form>
</div>