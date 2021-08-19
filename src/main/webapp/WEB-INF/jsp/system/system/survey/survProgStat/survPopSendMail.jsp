<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/common-taglibs.jsp"%>
<script type="text/javascript">
$(function() {
	setTimeout(function() {
		$("#popForm input[name=title]").focus();
	}, 100);
});

// 메일발송
function sendMail() {
	if(isEmpty($("#popForm input[name=title]").val())) {
		<c:set var="messageArg"><spring:message code="word.title"/></c:set>
		$.showMsgBox("<spring:message code="common.required.msg" arguments="${messageArg}"/>", null, $("#popForm input[name=title]")[0]);
		return false;
	}
	
	if(isEmpty($("#popForm [name=contents]").val())) {
		<c:set var="messageArg"><spring:message code="word.content"/></c:set>
		$.showMsgBox("<spring:message code="common.required.msg" arguments="${messageArg}"/>", null, $("#popForm [name=contents]")[0]);
		return false;
	}
	
	$.showConfirmBox("<spring:message code="common.sendMail.msg"/>", function() {
		sendAjax({
			"url" : "${context_path}/system/system/survey/survProgStat/selectSendMail.do",
			"data" : getFormData("popForm"),
			"doneCallbackFunc" : "popCheckResult"
		});
	})
}

function popCheckResult(data) {
	$.showMsgBox(data.msg);
	if(data.result == AJAX_SUCCESS) {
		$.fancybox.close();
	}
}
</script>

<div class="popup wx800">
<p class="title"><spring:message code="word.mailSend"/></p>
<form:form commandName="dataVO" id="popForm" name="popForm" method="post">
	<form:hidden path="keySurveyId" value="${keySurveyId}"/>
	<div class="sch-bx">
	</div>
	<div class="tbl-type02 mt10">
		<table summary="">
			<colgroup>
				<col width="120"/>
				<col width=""/>
			</colgroup>
			<tbody>
				<tr>
					<th scope="row"><spring:message code="word.title"/></th>
					<td>
						<input type="text" name="title" class="wp100"/>
					</td>
				</tr>
				<tr>
					<th scope="row"><spring:message code="word.content"/></th>
					<td>
						<textarea name="contents"/>
					</td>
				</tr>
			</tbody>
		</table>
	</div>
	<div class="tbl-bottom mt0 mb10">
		<div class="tbl-btn">
			<a href="#" class="save" onclick="sendMail();return false;"><spring:message code="button.mailSend"/></a>
		</div>
	</div>
</form:form>
</div>