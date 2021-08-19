<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib uri="/WEB-INF/tlds/codeUtil.tld" prefix="codeUtil" %>
<chart  palette="1"
		caption=""
		showLabels="1"
		showvalues="0"
		numberPrefix=""
		PYAxisName=""
		SYAxisName=""
		sYAxisValuesDecimals="2"
		connectNullData="0"
		numDivLines="4"
		formatNumberScale="0"
		bgColor='FFFFFF,EBEFF1'
		canvasBorderColor='D8D9D4'
		canvasBorderThickness='1'
		labelDisplay='none'
		baseFontSize='11'
		showLegend='1'
		legendPosition='BOTTOM'
		legendBorderThickness='1'
		legendBorderAlpha='4'
		legendShadow='1'
		plotFillAngle='180'
		plotGradientColor='E0F0F0'
		showPlotBorder='0'
		plotSpacePercent='30'
>
    <categories>
    <c:forEach items="${gbnList}" var="gbn">
    	<category label="${gbn.propertyNm}"></category>
    </c:forEach>
    </categories>
    
    <dataset seriesName="<spring:message code="word.weight" htmlEscape="false"/>" color="green" showValues="">
		<c:forEach items="${gbnList}" var="gbn">
			<set value="${gbn.weight}" />
		</c:forEach>	
	</dataset>
	
	<dataset seriesName="<spring:message code="word.weightScore" htmlEscape="false"/>" color="yellow" showValues="">
		<c:forEach items="${gbnList}" var="gbn">
			<set value="${gbn.weightScore}" />
		</c:forEach>	
	</dataset>
</chart>