<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@ include file="/WEB-INF/jsp/common/common-taglibs.jsp"%>
<%@ include file="/WEB-INF/jsp/common/tiles/common-params.jsp"%>
<!doctype html>
<html lang="ko">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
<meta http-equiv="X-UA-Compatible" content="IE=edge"/>
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>bizGATE BSC+</title>
<link rel="stylesheet" type="text/css" href="${css_path}/smoothness/jquery-ui.min.css"/>
<link rel="stylesheet" type="text/css" href="${css_path}/ui.jqgrid.css"/>
<link rel="stylesheet" type="text/css" href="${css_path}/jquery.fancybox-1.3.4.css"/>
<link rel="stylesheet" type="text/css" href="${css_path}/theme/${theme}/common.css"/>
<%-- select검색 추가 --%>
<link rel="stylesheet" type="text/css" href="${css_path}/select2.min.css"/>
<script type="text/javascript" src="${js_path}/jquery-1.11.0.min.js"></script>
<script type="text/javascript" src="${js_path}/jquery-ui.min.js"></script>
<script type="text/javascript" src="${js_path}/jquery.i18n.properties.min.js"></script>
<script type="text/javascript" src="${js_path}/i18n/grid.locale-${pageContext.response.locale}.js"></script>
<script type="text/javascript" src="${js_path}/jquery.jqGrid.min.js"></script>
<script type="text/javascript" src="${js_path}/jquery.cookie.js"></script>
<script type="text/javascript" src="${js_path}/common.js"></script>
<script type="text/javascript" src="${js_path}/jquery.fancybox-1.3.4.pack.js"></script>
<script type="text/javascript" src="${js_path}/Chart.bundle.min.js"></script>
<%-- select검색 추가 --%>
<script type="text/javascript" src="${js_path}/select2.min.js"></script>

<script type="text/javascript">
<validator:javascript dynamicJavascript="false" staticJavascript="true"/>

var currentLocale = "${pageContext.response.locale}";
var contextPath = "${root}";

$.i18n.properties({
	name:"Messages", 
	path:"${context_path}/properties/",
	language:"${pageContext.response.locale}",
	mode:"both",
	async:false,
	cache:true,
	callback:function() {
		i18nLoaded = true;
	}
});
</script>
</head>
<body class="popBody">
<div>
	<!-- 콘텐츠 시작 -->
	<div class="contents">
		<tiles:insertAttribute name="content"/>
	</div>
	<!-- 콘텐츠 끝 -->
	<div id="sgDialog" title="<spring:message code="word.confirm"/>"><p></p></div> <!-- 다이어로그 -->
	<div class="loadingDiv">
		<div class="loadingImgDiv">
			<img src="${img_path}/loading.gif"/>
		</div>
		<div class="loadingBGDiv"></div>
	</div>
	<form:form action="${root}/logout.do" name="logoutForm" method="POST"></form:form>
	<iframe name="hiddenFrame" id="hiddenFrame" frameborder="0"></iframe>
	<form:form id="layoutPopForm"></form:form>
</div>
</body>
</html>