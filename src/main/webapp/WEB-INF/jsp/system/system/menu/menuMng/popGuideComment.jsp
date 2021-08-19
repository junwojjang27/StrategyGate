<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/common-taglibs.jsp"%>
<script type="text/javascript">
$(function() {
	setMaxLength("popGuideForm");
	showBytes("guideComment", "guideCommentBytes");
});

//상세 조회
function saveGuideComment() {

	$.showConfirmBox("<spring:message code="common.save.msg"/>", function() {
		sendAjax({
		"url" : "${context_path}/system/system/menu/menuMng/saveGuideComment.do",
		"data" : getFormData("popGuideForm"),
		"doneCallbackFunc" : "checkResult"
		});
	});
}

function checkResult(data) {
	$.showMsgBox(data.msg);
	if(data.result == AJAX_SUCCESS) {
		$.fancybox.close();
		searchList();
	}
}
</script>
<div class="popup wx550 hx800">
	<p class="title"><spring:message code="word.help"/></p>
	<form:form commandName="dataVO" id="popGuideForm" name="popGuideForm" method="post">
	 	<form:hidden path="year"/>
	 	<form:hidden path="pgmId"/>

		<div class="sch-bx">
			<ul>
				<li>
					<label for="pgmNm"><spring:message code="word.menuNm"/></label>
					<span><b><c:out value="${dataVO.pgmNm}"></c:out></b></span>
				</li>
			</ul>
		</div>
		<div class="btn-dw">
		</div>
		<div class="tbl-type02">
			<table summary="" >
				<caption></caption>
				<colgroup>
					<col width="100%"/>
				</colgroup>
				<tbody>
					<tr>
						<th scope="row"><label for="guideComment"><spring:message code="word.help"/></label></th>
					</tr>
					<tr>
						<td>
							<p><form:textarea path="guideComment" maxlength="4000" cssClass="wp100 hx450 ml0"/></p>
							<p class="byte"><label id="guideCommentBytes">0</label><label> / 4000byte</label></p>
						</td>
					</tr>
				</tbody>
			</table>
		</div>
		<div class="tbl-bottom">
			<div class="tbl-btn">
				<a href="#" class="new" onclick="saveGuideComment();return false;"><spring:message code="button.save"/></a>
				<a href="#" class="new" onclick="$.fancybox.close();return false;"><spring:message code="button.close"/></a>
			</div>
		</div>
	</form:form>
</div>	
