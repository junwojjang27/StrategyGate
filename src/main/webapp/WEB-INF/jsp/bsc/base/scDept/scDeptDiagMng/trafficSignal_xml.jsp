<%@ page language="java" contentType="text/xml; charset=UTF-8" pageEncoding="UTF-8"%><%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%><%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%><?xml version="1.0" encoding="utf-8"?>
<chart>
	<etc>
		<item>
			<c:forEach var= "list" items="${signalList}">
				<signal code="${list.codeId}" text="${list.codeNm}" color="${list.color}"/>
			</c:forEach>
			<signal code="" text="<spring:message code="word.nonEval" htmlEscape="false"/>" color="0xE1E1E1"/>
		</item>
		<shape>
			<item kind="06" type="05" fontsize="11" fontcolor="0x000000" width="100" height="15" fontfamily="<spring:message code="font.dotum" htmlEscape="false"/>" fontbold="false"/>
			<item kind="05" type="05" fontsize="11" fontcolor="0x000000" width="100" height="15" fontfamily="<spring:message code="font.dotum" htmlEscape="false"/>" fontbold="false"/>
			<item kind="07" type="07" fontsize="13" fontcolor="0x000000" width="150" height="35" fontfamily="<spring:message code="font.dotum" htmlEscape="false"/>" fontbold="true"/>
			<item kind="03" type="03" fontsize="11" fontcolor="0x000000" width="130" height="28" fontfamily="<spring:message code="font.dotum" htmlEscape="false"/>" fontbold="true"/>
			<item kind="19" type="19" fontsize="11" fontcolor="0x000000" width="130" height="25" fontfamily="<spring:message code="font.dotum" htmlEscape="false"/>" fontbold="false"/>
		</shape>
	</etc>
</chart>
