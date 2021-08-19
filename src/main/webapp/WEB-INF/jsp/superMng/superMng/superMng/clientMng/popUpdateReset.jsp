<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/common-taglibs.jsp"%>
<script type="text/javascript">
function doReset() {
	if($("#confirmCompId").val() != $("#newCompId").val()) {
		$.showMsgBox("<spring:message code="superMng.superMng.clientMng.clientMngList.error"/>", null, "confirmCompId");
		return false;
	}
	
	$.showConfirmBox("<spring:message code="superMng.superMng.clientMng.clientMngList.confirm3"/>", function() {
		sendAjax({
			"url" : "${context_path}/superMng/superMng/superMng/clientMng/updateReset.do",
			"data" : getFormData("form"),
			"doneCallbackFunc" : "checkResult"
		});
		$.fancybox.close();
	});
}
</script>
<div class="popup wx600">
	<p class="title"><spring:message code="word.reset"/></p>
	<form:form commandName="searchVO" id="popForm" name="popForm" method="post" cssClass="mb20">
		<div class="tbl-type02 mt20">
			<table summary="">
				<colgroup>
					<col width="140"/>
					<col/>
				</colgroup>
				<tbody>
					<tr>
						<th scope="row"><label><spring:message code="word.compNm"/></label></th>
						<td>
							<c:out value="${searchVO.compNm}"/>
						</td>
					</tr>
					<tr>
						<th scope="row"><label><spring:message code="word.compId"/></label></th>
						<td class="pd10">
							<input type="text" id="confirmCompId" class="mb10"/>
							<br/>
							<span class="red"><spring:message code="superMng.superMng.clientMng.clientMngList.info" htmlEscape="false"/></span>
						</td>
					</tr>
				</tbody>
			</table>
		</div>
	
		<div class="tbl-bottom">
			<div class="tbl-btn">
				<a href="#" class="save" onclick="doReset();return false;"><spring:message code="button.reset"/></a>
			</div>
		</div>
	</form:form>
</div>