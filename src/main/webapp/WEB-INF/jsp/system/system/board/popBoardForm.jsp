<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/common-taglibs.jsp"%>
<validator:javascript formName="boardVO" staticJavascript="false" xhtml="true" cdata="false"/>
<script type="text/javascript">
var popUpload1;
$(function() {
	setMaxLength("popBoardform");
	<c:if test="${boardSetting.useAtchFileYn eq 'Y'}">
		popUpload1 = new SGFileUploader({"targetId" : "popDivFile1", "inputName" : "upFile1", "maxFileCnt" : ${boardSetting.maxUploadCnt}, "allowFileExts": "<spring:eval expression="@globals.getProperty('fileUpload.allowFileExts')"></spring:eval>", "maxTotalSize" : ${boardSetting.maxUploadSize}});
		<c:import url="/common/fileList.do" charEncoding="utf-8">
			<c:param name="moduleName" value="popUpload1"/>
			<c:param name="param_atchFileId" value="${dataVO.atchFileId}"/>
		</c:import>
	</c:if>
	showBytes("popCtnts", "popContentBytes");
});

<c:if test="${boardSetting.writable}">
	<c:if test="${dataVO.seq == -1 or dataVO.editable}">
	// 저장
	function popDoSave() {
		if(!validateBoardVO(document.popBoardform)) {
			return;
		}
		
		$.showConfirmBox("<spring:message code="common.save.msg"/>", function() {
			sendMultipartForm({
				"url" : "${context_path}/comPop/system/system/board/saveBoard.do",
				"formId" : "popBoardform",
				"fileModules" : popUpload1,
				"doneCallbackFunc" : "popCheckResult",
				"failCallbackFunc" : "popCheckResult"
			});
		});
	}
	
	// 저장 callback
	function popCheckResult(data) {
		$.showMsgBox(data.msg);
		if(data.result == AJAX_SUCCESS) {
			var f = document.popBoardform;
			f.page.value = 1;
			popGoList();
		}
	}
	</c:if>
</c:if>

function popGoList() {
	var f = document.popBoardform;
	openFancybox({
		"url" : "${context_path}/comPop/system/system/board/boardList.do",
		"data" : getFormData("popBoardform")
	});
	//loadPage("${context_path}/comPop/system/system/board/boardList.do", "popBoardform");
}

function popGoBack() {
	var f = document.popBoardform;
	<c:if test="${dataVO.upSeq != -1}">
		f.seq.value = f.upSeq.value;
	</c:if>
	
	openFancybox({
		"url" : "${context_path}/comPop/system/system/board/boardDetail.do",
		"data" : getFormData("popBoardform")
	});
	
	//loadPage("${context_path}/comPop/system/system/board/boardDetail.do", "popBoardform");
}
</script>
<div class="popup wx800">
	<p class="title">QnA</p>
	<form:form commandName="dataVO" id="popBoardform" name="popBoardform" method="post" enctype="multipart/form-data">
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
						<th scope="row"><label for="popCtnts"><spring:message code="word.content"/></label></th>
						<td>
							<p><form:textarea path="contents" id="popCtnts" maxlength="4000" cssClass="wp100 ml0"/></p>
							<p class="byte"><label id="popContentBytes">0</label><label> / 4000byte</label></p>
						</td>
					</tr>
					<c:if test="${boardSetting.useAtchFileYn eq 'Y'}">
						<tr>
							<th scope="row"><spring:message code="word.atchFile"/></th>
							<td>
								<div id="popDivFile1"></div>
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
						<a href="#" class="prev" onclick="popGoBack();return false;"><span><spring:message code="button.before"/></span></a>
					</c:when>
					<c:otherwise>
						<a href="#" class="prev" onclick="popGoList();return false;"><span><spring:message code="button.before"/></span></a>
					</c:otherwise>
				</c:choose>
			</div>
			<div class="tbl-btn">
				<c:if test="${boardSetting.writable}">
					<c:if test="${dataVO.seq == -1 or dataVO.editable}">
						<a href="#" class="save" onclick="popDoSave();return false;"><spring:message code="button.save"/></a>
					</c:if>
				</c:if>
			</div>
		</div>
	</form:form>
</div>	
