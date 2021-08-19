<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<c:set var="root" value="${pageContext.request.contextPath}" scope="request"/>
<c:set var="context_path" value="${root}" scope="request"/>
<c:set var="js_path" value="${root}/js" scope="request"/>
<c:set var="css_path" value="${root}/css" scope="request"/>

<c:set var="theme" value="${theme}" />
<c:if test="${empty theme}">
	<c:choose>
		<c:when test="${not empty cookie.theme.value}">
			<c:set var="theme" value="${cookie.theme.value}"/>
		</c:when>
		<c:otherwise>
			<c:set var="theme"><spring:eval expression="@globals.getProperty('default.theme')"></spring:eval></c:set>
		</c:otherwise>
	</c:choose>
</c:if>

<c:set var="img_path" value="${root}/images/theme/${theme}" scope="request"/>

<c:set var="jqgrid_width" value="100%" scope="request"/>
<c:set var="jqgrid_width_pop" value="500" scope="request"/>
<c:set var="jqgrid_height" value="400" scope="request"/>
<c:set var="jqgrid_height2" value="200" scope="request"/>
<c:set var="jqgrid_height3" value="300" scope="request"/>
<c:set var="jqgrid_height4" value="215" scope="request"/><%//조직성과도, 전략체계도%>
<c:set var="jqgrid_height5" value="250" scope="request"/>
<c:set var="jqgrid_height_popInnerGrid" value="365" scope="request"/>
<c:set var="jqgrid_height_diagBottom" value="228" scope="request"/>
<c:set var="jqgrid_rownum" value="20" scope="request"/>
<c:set var="jqgrid_rownum_max" value="99999" scope="request"/>

<%// 검색조건용 값 세팅. 쿠키에 해당 값이 존재하면 적용.%>
<jsp:useBean id="calVar" class="java.util.GregorianCalendar" scope="request"/>
<% calVar.add(java.util.GregorianCalendar.MONTH, -1); %>
<fmt:formatDate value="${calVar.time}" pattern="yyyy" var="findYear" scope="request"/>
<fmt:formatDate value="${calVar.time}" pattern="MM" var="findMon" scope="request"/>

<c:set var="findAnalCycle" value="Y"/>
<c:if test="${not empty cookie.findYear.value}">
	<c:set var="findYear" value="${cookie.findYear.value}"/>
</c:if>
<c:if test="${not empty cookie.findMon.value}">
	<c:set var="findMon" value="${cookie.findMon.value}"/>
</c:if>
<c:if test="${not empty cookie.findAnalCycle.value}">
	<c:set var="findAnalCycle" value="${cookie.findAnalCycle.value}"/>
</c:if>
<c:if test="${not empty cookie.findEvalDegreeId.value}">
	<c:set var="findEvalDegreeId" value="${cookie.findEvalDegreeId.value}"/>
</c:if>
<c:if test="${not empty cookie.findScDeptId.value}">
	<c:set var="findScDeptId" value="${cookie.findScDeptId.value}"/>
</c:if>
<c:if test="${empty cookie.findScDeptId.value and not empty sessionScope.userScDeptMap}">
	<c:set var="findScDeptId" value="${sessionScope.userScDeptMap[findYear]}"/>
</c:if>
<c:if test="${not empty cookie.findDeptId.value}">
	<c:set var="findDeptId" value="${cookie.findDeptId.value}"/>
</c:if>
<c:if test="${empty theme_path}">
	<c:set var="theme_path" value="${css_path}/theme/${theme}" scope="request"/>
</c:if>
