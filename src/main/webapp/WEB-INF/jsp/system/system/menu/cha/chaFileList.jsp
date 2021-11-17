<%--
  Created by IntelliJ IDEA.
  User: junwo
  Date: 2021-11-17
  Time: 오후 1:28
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/common-taglibs.jsp"%>
<%@ taglib prefix="secure" uri="/WEB-INF/tlds/secure.tld"%>
<c:if test="${not empty fileList}">
<c:forEach var="fileVO" items="${fileList}" varStatus="status">
<a href="#" onclick="fileDownload('<c:out value="${secure:encrypt(fileVO.atchFileId)}"/>', '<c:out value="${fileVO.fileSn}"/>', '${param.isPublic}');return false;"><label><c:out value="${fileVO.orignlFileNm}"/> (<fmt:formatNumber value="${fileVO.fileSize}" pattern="#,###"/> bytes)</label></a>
</c:forEach>
</c:if>