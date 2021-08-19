<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib uri="/WEB-INF/tlds/codeUtil.tld" prefix="codeUtil" %>
<chart palette="1"
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
    <c:forEach items="${codeUtil:getCodeList('024')}" var="codeItem">
    	<category label="${codeItem.codeNm}"></category>
    </c:forEach>
    </categories>
    <dataset seriesName="<spring:message code="word.target" htmlEscape="false"/>" color="2488E6" showValues="">
        <set value="${target.mon01}" />
        <set value="${target.mon02}" />
        <set value="${target.mon03}" />
        <set value="${target.mon04}" />
        <set value="${target.mon05}" />
        <set value="${target.mon06}" />
        <set value="${target.mon07}" />
        <set value="${target.mon08}" />
        <set value="${target.mon09}" />
        <set value="${target.mon10}" />
        <set value="${target.mon11}" />
        <set value="${target.mon12}" />
    </dataset>
    <dataset seriesName="<spring:message code="word.actual" htmlEscape="false"/>" color="84D612" showValues="" >
        <set value="${actual.mon01}" />
        <set value="${actual.mon02}" />
        <set value="${actual.mon03}" />
        <set value="${actual.mon04}" />
        <set value="${actual.mon05}" />
        <set value="${actual.mon06}" />
        <set value="${actual.mon07}" />
        <set value="${actual.mon08}" />
        <set value="${actual.mon09}" />
        <set value="${actual.mon10}" />
        <set value="${actual.mon11}" />
        <set value="${actual.mon12}" />
    </dataset>
    <dataset seriesName="<spring:message code="word.score" htmlEscape="false"/>" color="FE0081" showValues="0" parentYAxis="S" anchorBorderThickness="3" anchorBgColor="FE0081" >
        <set value="${score.mon01}" />
        <set value="${score.mon02}" />
        <set value="${score.mon03}" />
        <set value="${score.mon04}" />
        <set value="${score.mon05}" />
        <set value="${score.mon06}" />
        <set value="${score.mon07}" />
        <set value="${score.mon08}" />
        <set value="${score.mon09}" />
        <set value="${score.mon10}" />
        <set value="${score.mon11}" />
        <set value="${score.mon12}" />
	</dataset>
</chart>