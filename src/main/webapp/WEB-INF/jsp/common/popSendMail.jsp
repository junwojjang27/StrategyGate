<!-- 
*************************************************************************
* CLASS 명	: popSendMail
* 작 업 자	: kimyh
* 작 업 일	: 2018-09-06
* 기	능	: 메일발송 popup
* ---------------------------- 변 경 이 력 --------------------------------
* 번호	작 업 자		작	업	일			변 경 내 용				비고
* ----	---------	----------------	---------------------	-----------
*	1	kimyh		2018-09-06				최 초 작 업
**************************************************************************
-->
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
	
	if($("#popForm [name=userIdList]").length == 0) {
		<c:set var="messageArg"><spring:message code="word.receiveUser"/></c:set>
		$.showMsgBox("<spring:message code="common.required.msg" arguments="${messageArg}"/>");
		return false;
	}
	
	if(isEmpty($("#popForm [name=contents]").val())) {
		<c:set var="messageArg"><spring:message code="word.content"/></c:set>
		$.showMsgBox("<spring:message code="common.required.msg" arguments="${messageArg}"/>", null, $("#popForm [name=contents]")[0]);
		return false;
	}
	
	$.showConfirmBox("<spring:message code="common.sendMail.msg"/>", function() {
		sendAjax({
			"url" : "${context_path}${searchVO.sendMailProcessUrl}",
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
<form:form commandName="searchVO" id="popForm" name="popForm" method="post">
	<c:forEach items="${searchVO.userIdList}" var="userId" varStatus="status">
		<input type="hidden" name="userIdList" value="${userId}"/>
	</c:forEach>
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
					<th scope="row"><spring:message code="word.receiveUser"/></th>
					<td>
						<c:forEach items="${searchVO.userNmList}" var="userNm" varStatus="status">
							${userNm}<c:if test="${not status.last}">, </c:if>
						</c:forEach>
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