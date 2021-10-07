<%--
  Created by IntelliJ IDEA.
  User: junwo
  Date: 2021-10-05
  Time: 오전 11:40
  To change this template use File | Settings | File Templates.
--%>

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/jsp/common/tiles/common-params.jsp" %>
<%@ include file="/WEB-INF/jsp/common/common-taglibs.jsp" %>
<!doctype html>
<html lang="ko">
<head>
    <script type="text/javascript">
        var upload1 = null;
        $(function () {
            <c:choose>
            <c:when test="${searchVO.modifyYn eq 'Y'}">
            upload1 = new SGFileUploader({
                "targetId": "atchFile1",
                "inputName": "upFile1",
                "maxFileCnt": 5,
                "maxTotalSize": "5MB"
            });
            <c:import url="/common/fileList.do" charEncoding="utf-8">
            <c:param name="moduleName" value="upload1"/>
            <c:param name="param_atchFileId" value="${searchVO.atchFileId}"/>
            </c:import>
            </c:when>
            </c:choose>
        });
    </script>
</head>
<body>
<c:choose>
    <c:when test="${searchVO.modifyYn eq 'N'}">
        <div id="atchFile1"></div>
    </c:when>
    <c:otherwise>
        <c:import url="/common/fileList.do" charEncoding="utf-8">
            <c:param name="downloadOnly" value="Y"/>
            <c:param name="param_atchFileId" value="${searchVO.atchFileId}"/>
        </c:import>
    </c:otherwise>
</c:choose>
</body>
</html>
