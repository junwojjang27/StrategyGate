<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/common-taglibs.jsp"%>
<%@ taglib prefix="secure" uri="/WEB-INF/tlds/secure.tld"%>
<c:if test="${not empty fileList}">
	<c:choose>
		<c:when test="${param.downloadOnly eq 'Y'}">
			<ul class="fileList">
				<c:forEach var="fileVO" items="${fileList}" varStatus="status">
					<li>
						<a href="#" onclick="fileDownload('<c:out value="${secure:encrypt(fileVO.atchFileId)}"/>', '<c:out value="${fileVO.fileSn}"/>', '${param.isPublic}');return false;"><label><c:out value="${fileVO.orignlFileNm}"/> (<fmt:formatNumber value="${fileVO.fileSize}" pattern="#,###"/> bytes)</label></a>
					</li>
				</c:forEach>
			</ul>
		</c:when>
		<c:otherwise>
			<c:forEach var="fileVO" items="${fileList}" varStatus="status">
				${param.moduleName}.showFile(
					"<c:out value="${secure:encrypt(fileVO.atchFileId)}"/>", "<c:out value="${fileVO.fileSn}"/>",
					"<c:out value="${fileVO.orignlFileNm}"/>", <c:out value="${fileVO.fileSize}"/>,
					<c:choose><c:when test="${param.canDelete eq 'N'}">false</c:when><c:otherwise>true</c:otherwise></c:choose>
				);
			</c:forEach>
		</c:otherwise>
	</c:choose>
</c:if>