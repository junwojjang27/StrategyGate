<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/common-taglibs.jsp"%>
<?xml version="1.0" encoding="utf-8"?>
<chart>
	<etc>
		<c:set var="boxing" value=""/>
		<c:choose>
			<c:when test="${autoYn ne 'Y'}">
				<c:forEach var="list1" items="${list1}">
					<item text="<c:out value="${list1.scDeptNm}" escapeXml="true"/>" code="<c:out value="${list1.scDeptId}" escapeXml="true"/>" signal="<c:out value="${list1.status}" escapeXml="true"/>" kind ="<c:out value="${list1.deptKind}" escapeXml="true"/>" x="<c:out value="${list1.subDeptXPos}" escapeXml="true"/>" y="<c:out value="${list1.subDeptYPos}" escapeXml="true"/>" score="<c:out value="${list1.score}" escapeXml="true"/>">
					<link><![CDATA[javascript:goGridSearch('<c:out value="${list1.scDeptId}" escapeXml="true"/>', '<c:out value="${list1.scDeptNm}" escapeXml="true"/>')]]></link>
			
						<c:forEach var="list2" items="${list2}">
							<c:if test="${list1.scDeptId eq list2.upScDeptId}">
							<item text="<c:out value="${list2.scDeptNm}" escapeXml="true"/>" code="<c:out value="${list2.scDeptId}" escapeXml="true"/>" signal="<c:out value="${list2.status}" escapeXml="true"/>" kind ="<c:out value="${list2.deptKind}" escapeXml="true"/>" x="<c:out value="${list2.subDeptXPos}" escapeXml="true"/>" y="<c:out value="${list2.subDeptYPos}" escapeXml="true"/>" score="<c:out value="${list2.score}" escapeXml="true"/>">
							<link><![CDATA[javascript:goGridSearch('<c:out value="${list2.scDeptId}" escapeXml="true"/>', '<c:out value="${list2.scDeptNm}" escapeXml="true"/>')]]></link>
			
								<c:forEach var="list3" items="${list3}">
									<c:if test="${list2.scDeptId eq list3.upScDeptId}">
									<item text="<c:out value="${list3.scDeptNm}" escapeXml="true"/>" code="<c:out value="${list3.scDeptId}" escapeXml="true"/>" signal="<c:out value="${list3.status}" escapeXml="true"/>" kind ="<c:out value="${list3.deptKind}" escapeXml="true"/>" x="<c:out value="${list3.subDeptXPos}" escapeXml="true"/>" y="<c:out value="${list3.subDeptYPos}" escapeXml="true"/>" score="<c:out value="${list3.score}" escapeXml="true"/>" boxing="2">
									<link><![CDATA[javascript:goGridSearch('<c:out value="${list3.scDeptId}" escapeXml="true"/>', '<c:out value="${list3.scDeptNm}" escapeXml="true"/>')]]></link>
			
										<c:forEach var="list4" items="${list4}">
										<c:if test="${list3.scDeptId eq list4.upScDeptId}">
											<item text="<c:out value="${list4.scDeptNm}" escapeXml="true"/>" code="<c:out value="${list4.scDeptId}" escapeXml="true"/>" signal="<c:out value="${list4.status}" escapeXml="true"/>" kind ="05" x="<c:out value="${list4.subDeptXPos}" escapeXml="true"/>" y="<c:out value="${list4.subDeptYPos}" escapeXml="true"/>" score="<c:out value="${list4.score}" escapeXml="true"/>" boxing="0">
											<link><![CDATA[javascript:goGridSearch('<c:out value="${list4.scDeptId}" escapeXml="true"/>', '<c:out value="${list4.scDeptNm}" escapeXml="true"/>')]]></link>
											</item>
										</c:if>
										</c:forEach>
			
									</item>
									</c:if>
								</c:forEach>
							</item>
							</c:if>
						</c:forEach>
					</item>
				</c:forEach>
			</c:when>
			<c:otherwise>
				<c:forEach var="list1" items="${list1}">
					<item text="<c:out value="${list1.scDeptNm}" escapeXml="true"/>" code="<c:out value="${list1.scDeptId}" escapeXml="true"/>" signal="<c:out value="${list1.status}" escapeXml="true"/>" kind ="<c:out value="${list1.deptKind}" escapeXml="true"/>" x="<c:out value="${list1.subDeptXPos}" escapeXml="true"/>" y="<c:out value="${list1.subDeptYPos}" escapeXml="true"/>" score="<c:out value="${list1.score}" escapeXml="true"/>">
					<link><![CDATA[javascript:goGridSearch('<c:out value="${list1.scDeptId}" escapeXml="true"/>', '<c:out value="${list1.scDeptNm}" escapeXml="true"/>')]]></link>
			
						<c:forEach var="list2" items="${list2}">
							<c:if test="${list1.scDeptId eq list2.upScDeptId}">
							<item text="<c:out value="${list2.scDeptNm}" escapeXml="true"/>" code="<c:out value="${list2.scDeptId}" escapeXml="true"/>" signal="<c:out value="${list2.status}" escapeXml="true"/>" kind ="<c:out value="${list2.deptKind}" escapeXml="true"/>" x="<c:out value="${list2.subDeptXPos}" escapeXml="true"/>" y="<c:out value="${list2.subDeptYPos}" escapeXml="true"/>" score="<c:out value="${list2.score}" escapeXml="true"/>">
							<link><![CDATA[javascript:goGridSearch('<c:out value="${list2.scDeptId}" escapeXml="true"/>', '<c:out value="${list2.scDeptNm}" escapeXml="true"/>')]]></link>
			
								<c:forEach var="list3" items="${list3}">
									<c:if test="${list2.scDeptId eq list3.upScDeptId}">
									<item text="<c:out value="${list3.scDeptNm}" escapeXml="true"/>" code="<c:out value="${list3.scDeptId}" escapeXml="true"/>" signal="<c:out value="${list3.status}" escapeXml="true"/>" kind ="<c:out value="${list3.deptKind}" escapeXml="true"/>" x="<c:out value="${list3.subDeptXPos}" escapeXml="true"/>" y="<c:out value="${list3.subDeptYPos}" escapeXml="true"/>" score="<c:out value="${list3.score}" escapeXml="true"/>" boxing="2">
									<link><![CDATA[javascript:goGridSearch('<c:out value="${list3.scDeptId}" escapeXml="true"/>', '<c:out value="${list3.scDeptNm}" escapeXml="true"/>')]]></link>
			
										<c:forEach var="list4" items="${list4}">
										<c:if test="${list3.scDeptId eq list4.upScDeptId}">
											<item text="<c:out value="${list4.scDeptNm}" escapeXml="true"/>" code="<c:out value="${list4.scDeptId}" escapeXml="true"/>" signal="<c:out value="${list4.status}" escapeXml="true"/>" kind ="05" x="<c:out value="${list4.subDeptXPos}" escapeXml="true"/>" y="<c:out value="${list4.subDeptYPos}" escapeXml="true"/>" score="<c:out value="${list4.score}" escapeXml="true"/>" boxing="0">
											<link><![CDATA[javascript:goGridSearch('<c:out value="${list4.scDeptId}" escapeXml="true"/>', '<c:out value="${list4.scDeptNm}" escapeXml="true"/>')]]></link>
											</item>
										</c:if>
										</c:forEach>
			
									</item>
									</c:if>
								</c:forEach>
							</item>
							</c:if>
						</c:forEach>
					</item>
				</c:forEach>
			</c:otherwise>
		</c:choose>
	</etc>

	<!--
	view : 범례 보기 true=1, false=0;
	text : 범례 타이틀
	textColor : 범례 글자 색상
	textAlign : 범주 타이틀 위치 정렬
	x : 범주의 위치 x (기준 우측 상단 위치에서 조정)
	y : 범주의 위치 y (기준 우측 상단 위치에서 조정)
	backGround : 백그라운드 이미지 이름
	lineView : 라인 보기 true : 1, false = 0;
	-->
	<category_setting view="0" text="<spring:message code="word.legend" htmlEscape="false"/>" textColor="0x333333" textAlign="center" x="0" y="80" backGround="svgOrgBG_${param.findYear}.jpg" width="1188" height="400" lineView = "0"/>
	<kind>
		<item kind="etc" onload="yes"/>
	</kind>
</chart>



