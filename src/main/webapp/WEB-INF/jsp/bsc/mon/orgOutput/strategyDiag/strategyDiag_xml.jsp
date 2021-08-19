<?xml version="1.0" encoding="utf-8"?>
<%@ page contentType="text/xml; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<root>
<!-- 2014년7월7일 한봉준과장님께서 소스를 단순하게 변경해주심 -->

	<attribute pdfVisible="true" pdfUrl="${context_path}/bsc/module/commModule/MatrixPdfDownload" applicationBar="true" position="horizontal">
		<node attributes="점수/false" name="전략연계표" />
	</attribute>
	<c:set value="0" var="node_cnt" />
	<c:set value="${fn:length(list)}" var="listSize" />
	<c:if test="${listSize != 0}">
		<c:forEach var="list" items="${list}">
			<c:if test="${list.nodeRemoveCnt > 0}">
				<c:forEach var="i" begin="1" end="${list.nodeRemoveCnt}" step="1">
					<c:set value="${node_cnt-1}" var="node_cnt" />
					</node>
				</c:forEach>
			</c:if>
			<c:set value="${list.scDeptId}|${list.strategyId}" var="url" />
			<c:set value="${node_cnt+1}" var="node_cnt" />
			<node label="${list.strategyNm}" color="${sinalMap[list.status]}" fontColor="0x000000" title="${list.scDeptNm}" attributes="점수/${list.score}" url="${url}">
		</c:forEach>
	</c:if>
	<c:if test="${node_cnt > 0}">
		<c:forEach var="i" begin="1" end="${node_cnt}" step="1">
			</node>
		</c:forEach>
	</c:if>

</root>