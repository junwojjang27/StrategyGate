<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/common-taglibs.jsp"%>
<div style="width:600px;height:300px;overflow:auto;">
	<ul>
		<c:forEach items="${userList}" var="user">
			<li><a href="#" onclick="setUser('${user.userId}', '${user.userNm}', '${param.rowId}');return false;">${user.userNm}</a></li>
		</c:forEach>
	</ul>
</div>