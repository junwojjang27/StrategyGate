<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/common-taglibs.jsp"%>
<validator:javascript formName="boardVO" staticJavascript="false" xhtml="true" cdata="false"/>
<script type="text/javascript">
var upload1;
$(function() {
	setMaxLength("form");
	<c:if test="${boardSetting.useAtchFileYn eq 'Y'}">
		upload1 = new SGFileUploader({"targetId" : "divFile1", "inputName" : "upFile1", "maxFileCnt" : ${boardSetting.maxUploadCnt}, "allowFileExts": "<spring:eval expression="@globals.getProperty('fileUpload.allowFileExts')"></spring:eval>", "maxTotalSize" : ${boardSetting.maxUploadSize}});
		<c:import url="/common/fileList.do" charEncoding="utf-8">
			<c:param name="moduleName" value="upload1"/>
			<c:param name="param_atchFileId" value="${dataVO.atchFileId}"/>
		</c:import>
	</c:if>
	showBytes("ctnts", "contentBytes");
});

<c:if test="${boardSetting.writable}">
	<c:if test="${dataVO.seq == -1 or dataVO.editable}">
	// 저장
	function doSave() {
		if(!validateBoardVO(document.form)) {
			return;
		}
		
		$.showConfirmBox("<spring:message code="common.save.msg"/>", function() {
			sendMultipartForm({
				"url" : "${context_path}/system/system/board/saveBoard.do",
				"formId" : "form",
				"fileModules" : upload1,
				"doneCallbackFunc" : "checkResult",
				"failCallbackFunc" : "checkResult"
			});
		});
	}
	
	// 저장 callback
	function checkResult(data) {
		$.showMsgBox(data.msg);
		if(data.result == AJAX_SUCCESS) {
			var f = document.form;
			f.page.value = 1;
			goList();
		}
	}
	</c:if>
</c:if>

function goList() {
	var f = document.form;
	loadPage("${context_path}/system/system/board/boardList.do", "form");
}

function goBack() {
	var f = document.form;
	<c:if test="${dataVO.upSeq != -1}">
		f.seq.value = f.upSeq.value;
	</c:if>
	loadPage("${context_path}/system/system/board/boardDetail.do", "form");
}
</script>

<form:form commandName="dataVO" id="form" name="form" method="post" enctype="multipart/form-data">
	<form:hidden path="boardId"/>
	<form:hidden path="seq"/>
	<form:hidden path="upSeq"/>
	<form:hidden path="page" value="${searchVO.page}"/>
	<form:hidden path="rows" value="${searchVO.rows}"/>
	<div class="tbl-type02 mt20">
		<table summary="">
			<colgroup>
				<col width="140"/>
				<col width=""/>
			</colgroup>
			<tbody>
				<tr>
					<th scope="row"><label for="title"><spring:message code="word.title"/></label></th>
					<td><form:input path="title" maxlength="300" class="t-box03"/></td>
				</tr>
				<tr>
					<th scope="row"><label for="ctnts"><spring:message code="word.content"/></label></th>
					<td>
						<p><form:textarea path="contents" id="ctnts" maxlength="4000" cssClass="wp100 ml0"/></p>
						<p class="byte"><label id="contentBytes">0</label><label> / 4000byte</label></p>
					</td>
				</tr>
				<c:if test="${boardSetting.useAtchFileYn eq 'Y'}">
					<tr>
						<th scope="row"><spring:message code="word.atchFile"/></th>
						<td>
							<div id="divFile1"></div>
						</td>
					</tr>
				</c:if>
			</tbody>
		</table>
	</div>
	<div class="tbl-bottom">
		<div class="tbl-wbtn">
			<c:choose>
				<c:when test="${dataVO.seq != -1 or dataVO.upSeq != -1}">
					<a href="#" class="prev" onclick="goBack();return false;"><span><spring:message code="button.before"/></span></a>
				</c:when>
				<c:otherwise>
					<a href="#" class="prev" onclick="goList();return false;"><span><spring:message code="button.before"/></span></a>
				</c:otherwise>
			</c:choose>
		</div>
		<div class="tbl-btn">
			<c:if test="${boardSetting.writable}">
				<c:if test="${dataVO.seq == -1 or dataVO.editable}">
					<a href="#" class="save" onclick="doSave();return false;"><spring:message code="button.save"/></a>
				</c:if>
			</c:if>
		</div>
	</div>
</form:form>
