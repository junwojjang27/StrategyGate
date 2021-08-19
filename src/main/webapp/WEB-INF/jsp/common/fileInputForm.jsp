<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/common-taglibs.jsp"%>
<%@ taglib prefix="secure" uri="/WEB-INF/tlds/secure.tld"%>
<script type="text/javascript">
var upload1;
upload1 = new SGFileUploader({"targetId" : "atchFile1", "inputName" : "upFile1", "maxFileCnt" : 5, "maxTotalSize" : "5MB"});
</script>
<div id="atchFile1"></div>