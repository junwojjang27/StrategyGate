<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/common-taglibs.jsp"%>
<?xml version="1.0" encoding="UTF-8"?>
<c:set var="isLink" value="true"/>
<chart>
	<etc img="${bgImgNm}" showMetricYn="${showMetricYn}">
		<c:forEach var="list" items="${perspectiveList}">
			<perspective text="${list.strategyNm}" index="${list.strategyId}" x="${list.x1}" y="${list.y1}" kind="${list.kind}"/></c:forEach>
		
		<c:forEach var="list" items="${strategyList}" varStatus="">
			<item text="${list.strategyNm}" index="${list.strategyId}" x="${list.x1}" y="${list.y1}" kind="${list.kind}" signal="${list.status}">
			<c:forEach var="list2" items="${metricList}" varStatus="">
				<c:if test="${list2.strategyId == list.strategyId}" >
					<data metricId="${list2.metricId}" text="${list2.metricNm}" signal="${list2.status}" url="javascript:goChartSearch('${list2.metricId}','${list2.metricNm}');"/>
				</c:if>
			</c:forEach>
			</item>
		</c:forEach>
		<c:forEach var="list3" items="${arrowList}" varStatus="status">
			<c:choose>
				<c:when test="${list3.kind eq 'hLine'}">
					<hLine value="${list3.strategyId}" px1="${list3.x1}" py1="${list3.y1}" px2="${list3.x2}" py2="${list3.y2}"/>
				</c:when>
				<c:otherwise>
					<arrow value="${list3.strategyId}" px1="${list3.x1}" py1="${list3.y1}" px2="${list3.x2}" py2="${list3.y2}" cx1="${list3.x3}" cy1="${list3.y3}" cx2="${list3.x4}" cy2="${list3.y4}"/>
				</c:otherwise>
			</c:choose>
		</c:forEach>
	</etc>
</chart>