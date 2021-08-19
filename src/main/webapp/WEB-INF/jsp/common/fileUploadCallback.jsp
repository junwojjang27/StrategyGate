<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/common-taglibs.jsp"%>
<sec:authorize access="isAuthenticated()">
<script type="text/javascript">
	parent.showLoading(false);

	<c:if test="${not empty callbackFunc}">
		var json = {"msg" : "${msg}", "result" : "${result}"};
		parent.window["${callbackFunc}"].apply(window, [json]);
	</c:if>
	<c:if test="${not empty dataVO.alwaysCallbackFunc}">
		parent.window["${dataVO.alwaysCallbackFunc}"].apply(window, []);
	</c:if>
</script>
</sec:authorize>
