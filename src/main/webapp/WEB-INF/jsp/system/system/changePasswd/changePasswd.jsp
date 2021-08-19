<!-- 
*************************************************************************
* CLASS 명	: ChangePasswd
* 작 업 자	: kimyh
* 작 업 일	: 2018-07-06
* 기	능	: 패스워드 변경
* ---------------------------- 변 경 이 력 --------------------------------
* 번호	작 업 자		작	업	일			변 경 내 용				비고
* ----	---------	----------------	---------------------	-----------
*	1	kimyh		2018-07-06				최 초 작 업
**************************************************************************
-->
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/common-taglibs.jsp"%>
<script type="text/javascript">
$(function() {

});

// 저장
function updatePassword() {
	var pwdPattern = /^.*(?=^.{8,20}$)(?=.*\d)(?=.*[a-zA-Z])(?=.*[\!@#$%\^&\*\(\)_\-`~,\.<>'";:\\\|+=\/\?\[\]\{\}]).*$/;
	var pw = $("#passwd").val();
	var pw2 = $("#passwd2").val();
	
	if(isEmpty(pw)) {
		<c:set var="messageArg"><spring:message code="word.password"/></c:set>
		$.showMsgBox("<spring:message code="common.required.msg" arguments="${messageArg}"/>", null, $("#passwd"));
		return false;
	}
	
	if(isEmpty(pw2)) {
		<c:set var="messageArg"><spring:message code="word.password3"/></c:set>
		$.showMsgBox("<spring:message code="common.required.msg" arguments="${messageArg}"/>", null, $("#passwd2"));
		return false;
	}
	
	if(!pwdPattern.test(pw)) {
		$.showMsgBox("<spring:message code="fail.common.login.password2"/>", null, $("#passwd"));
		return false; 
	}
	
	if(pw != pw2) {
		<c:set var="messageArg"><spring:message code="word.password"/></c:set>
		<c:set var="messageArg2"><spring:message code="word.password3"/></c:set>
		$.showMsgBox("<spring:message code="errors.passwordupdate" arguments="${messageArg},${messageArg2}"/>", null, $("#passwd"));
		return false;
	}
	
	$.showConfirmBox("<spring:message code="common.save.msg"/>", function() {
		sendAjax({
			"url" : "${context_path}/system/system/changePasswd/changePasswd/updatePassword.do",
			"data" : getFormData("form"),
			"doneCallbackFunc" : "checkResult"
		});
	});
}

function checkResult(data) {
	if(data.result == AJAX_SUCCESS) {
		$.showMsgBox(data.msg + "<br/><spring:message code="fail.common.login2"/>", "doLogout");
	} else {
		$.showMsgBox(data.msg);
	}
}
</script>

<form:form commandName="searchVO" id="form" name="form" method="post">
	<div class="btn-dw">
	</div>
	<div class="tbl-type02">
		<table>
			<colgroup>
				<col width="140"/>
				<col width=""/>
				<col width="140"/>
				<col width=""/>
			</colgroup>
			<tbody>
				<tr>
					<th scope="row"><label><spring:message code="word.id"/></label></th>
					<td>
						${dataVO.userId}
					</td>
					<th scope="row"><label><spring:message code="word.name"/></label></th>
					<td>
						${dataVO.userNm}
					</td>
				</tr>
				<tr>
					<th scope="row"><label for="passwd"><spring:message code="word.password"/></label><span class="red">(*)</span></th>
					<td>
						<form:password path="passwd" class="t-box01 wx200" value=""/>
					</td>
					<th scope="row"><label for="passwd2"><spring:message code="word.password3"/></label><span class="red">(*)</span></th>
					<td>
						<input type="password" id="passwd2" class="t-box01 wx200" value=""/>
					</td>
				</tr>
			</tbody>
		</table>
	</div>
	<div id="btns2" class="tbl-bottom mt0">
		<div class="tbl-btn">
			<a href="#" class="save" onclick="updatePassword();return false;"><spring:message code="button.save"/></a>
		</div>
	</div>
	<div class="page-noti" id="pageNoti">
		<ul>
			<li><spring:message code="errors.validation.input18"/></li>
		</ul>
	</div>
</form:form>