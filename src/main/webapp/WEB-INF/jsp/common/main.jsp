<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/common-taglibs.jsp"%>
<sec:authorize access="isAnonymous()">
	<script type="text/javascript">
		$.showMsgBox("<spring:message code="fail.common.login2"/>", function() {
			doLogout();
		});
	</script>
</sec:authorize>
<sec:authorize access="isAuthenticated()">
	<script type="text/javascript">
		var intervalFunc = setInterval(function() {
			if($("#scDeptList .scDeptRoot").length == 0) return;
			
			clearInterval(intervalFunc);
			if(isNotEmpty(getPgmId())) {
				loadPageByPgmId(getPgmId());
			} else {
				/*
				<c:choose>
					<c:when test="${alertPwChangeYn eq 'Y'}">
						loadPageByPgmId("${defaultPgmId}", "<spring:message code="info.pwChange.msg" arguments="${sessionScope.loginVO.pwChangeCycle}"/>");
					</c:when>
					<c:otherwise>
						loadPageByPgmId("${defaultPgmId}");
					</c:otherwise>	
				</c:choose>
				*/
				
				<c:choose>
				<c:when test="${alertPwChangeYn eq 'Y'}">
					loadPageByPgmId("MAIN", "<spring:message code="info.pwChange.msg" arguments="${sessionScope.loginVO.pwChangeCycle}"/>");
				</c:when>
				<c:otherwise>
					loadPageByPgmId("MAIN");
				</c:otherwise>	
			</c:choose>
			}
		}, 100);
	</script>
</sec:authorize>
