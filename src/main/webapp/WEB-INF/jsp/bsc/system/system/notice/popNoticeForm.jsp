<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/common-taglibs.jsp"%>
<style type="text/css">
html {overflow:hidden;}
</style>
<script type="text/javascript">

$(function(){

});

// 저장
function popClose() {
	self.close();
}

function popNoticeClose(id) {
	$.cookie("popNoticeClose" + id, "Y", {path:contextPath});
	popClose();
}
</script>

<form:form commandName="dataVO" id="popForm" name="popForm" method="post" cssClass="mb20">
<div class="ptitle"><spring:message code="word.notice"/></div>
	<div id="baseForm" class="tbl-type02">
		<table summary="">
			<colgroup>
				<col width="30%"/>
				<col width="70%"/>
			</colgroup>
			<tbody>
				<tr>
					<th scope="row"><label for="subject"><spring:message code="word.title"/></label></th>
					<td colspan="3">
						<c:out value="${dataVO.subject}"/>
					</td>
				</tr>
				<tr>
					<th scope="row"><label for="content"><spring:message code="word.content"/></label></th>
					<td colspan="3" class="pd10">
						<c:set var="newLine" value="<%=\"\n\"%>"/>
						<c:set var="content"><c:out value="${fn:replace(dataVO.content, newLine, '<br/>')}" escapeXml="false"></c:out></c:set>
						<span><c:out value="${content}" escapeXml="false"></c:out></span>
					</td>
				</tr>
				<tr>
					<th scope="row"><label><spring:message code="word.atchFile"/></label></th>
					<td class="pd10">
						<c:choose>
							<c:when test="${isPublic eq 'Y'}">
								<c:import url="/common/publicFileList.do" charEncoding="utf-8">
									<c:param name="isPublic" value="Y"/>
									<c:param name="downloadOnly" value="Y"/>
									<c:param name="param_atchFileId" value="${dataVO.atchFileKey}"/>
								</c:import>
							</c:when>
							<c:otherwise>
								<c:import url="/common/fileList.do" charEncoding="utf-8">
									<c:param name="downloadOnly" value="Y"/>
									<c:param name="param_atchFileId" value="${dataVO.atchFileKey}"/>
								</c:import>
							</c:otherwise>
						</c:choose>
					</td>
				</tr>
			</tbody>
		</table>
	</div>

	<div class="tbl-bottom">
		<c:if test="${not empty showCloseOption and showCloseOption eq 'Y'}">
		<div class="tbl-wbtn popCloseOption">
			<input type="checkbox" id="cbPopNoticeClose" onclick="popNoticeClose('${dataVO.id}')"/><label for="cbPopNoticeClose"><span></span> <spring:message code="button.dontShowAgain"/></label>
		</div>
		</c:if>
		<div class="tbl-btn">
			<a href="#" class="close" onclick="popClose();return false;"><spring:message code="button.close"/></a>
		</div>
	</div>
</form:form>
