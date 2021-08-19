
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/common-taglibs.jsp"%>
	<div style="width:100%;height:220px;">
		<div class="divTitleT-Graph"><spring:message code="word.metricResultStatusTop5" /></div>
		<div class="tbl-typedash">
			<table>
				<colgroup>
					<col width="30%">
					<col width="10%">
					<col width="15%">
					<col width="15%">
					<col width="10%">
					<col width="10%">
				</colgroup>
				<tbody>
					</tbody><tbody>
					<tr>
						<th><spring:message code="word.metric" /></th>
						<th><spring:message code="word.weight" /></th>
						<th><spring:message code="word.target" /></th>
						<th><spring:message code="word.actual" /></th>
						<th><spring:message code="word.score" /></th>
						<th><spring:message code="word.status" /></th>
					</tr>
					<c:forEach items="${metricTopList}" var="mList" varStatus="status">
						<tr>
							<td class="name"><c:out value="${mList.metricNm}" /></td>
							<td><c:out value="${commonUtil:getNumberFormat2(mList.weight,0)}" /></td>
							<td class="td-type02"><c:out value="${commonUtil:getNumberFormat1(mList.target)}" /></td>
							<td class="td-type02"><c:out value="${commonUtil:getNumberFormat1(mList.actual)}" /></td>
							<td><c:out value="${commonUtil:getNumberFormat1(mList.finalScore)}" /></td>
							<td><div class="gridSignal" style="background-color:<c:out value="${mList.color}" />" >&nbsp;</div></td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</div>
	</div>
	

