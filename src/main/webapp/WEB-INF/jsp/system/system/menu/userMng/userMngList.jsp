<!-- 
*************************************************************************
* CLASS 명	: UserMngList
* 작 업 자	: 현걸욱
* 작 업 일	: 2018-1-21
* 기	능	: 사용자관리 List
* ---------------------------- 변 경 이 력 --------------------------------
* 번호	작 업 자		작	업	일			변 경 내 용				비고
* ----	---------	----------------	---------------------	-----------
*	1	현걸욱		2018-1-21				최 초 작 업
**************************************************************************
-->
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/common-taglibs.jsp"%>
<script type="text/javascript">
$(function(){
	$("#list").jqGrid({
		url			:	"${context_path}/system/system/menu/userMng/userMngList_json.do",
		postData	:	getFormData("form"),
		width		:	"${jqgrid_width}",
		height		:	"${jqgrid_height}",
		colModel	:	[
						{name:"userId",			index:"userId",		width:100,		hidden:true},
						{name:"userNm",			index:"userNm",		width:100,		align:"left",	label:"<spring:message code="word.userNm" />"},
						{name:"deptNm",			index:"deptNm",		width:100,		align:"left",	label:"<spring:message code="word.deptNm" />"},
						{name:"posNm",			index:"posNm",		width:100,		align:"left",	label:"<spring:message code="word.posNm" />"},
						{name:"jikgubNm",		index:"jikgubNm",	width:100,		align:"left",	label:"<spring:message code="word.jikgubNm" />"},
						{name:"authGubun",		index:"authGubun",	width:100,		hidden:true},
						{name:"authGubunNm",	index:"authGubunNm",width:100,		align:"center",	label:"<spring:message code="word.authNm" />"},
						{name:"loginBt",	index:"loginBt",	width:100,	align:"center",	label:"<spring:message code="word.login"/>",
							formatter:function(cellvalue, options, rowObject) {
								return "<input type='button' class='pButton3_grid'  value='<spring:message code="button.login"/>' onclick='goLogin(\"" + rowObject.userId + "\");return false;' />";
							}, unformat:inputUnformatter
						}
						],
		rowNum		: ${jqgrid_rownum_max},
		pager		: "pager",
		cmTemplate  : {sortable: false},
		loadComplete : function() {
			gridRowSpan("list", "userId", "userNm");
			gridRowSpan("list", "userId", "deptNm");
			gridRowSpan("list", "userId", "posNm");
			gridRowSpan("list", "userId", "jikgubNm");
			gridRowSpan("list", "userId", "loginBt");
			gridRowSpan("list", "userId", "loginId");
		}
	});
	
	$("#findSearchDeptNm, #findSearchUserNm").keyup(function(e) {
		if(e.keyCode == 13) searchList();
	});
});

// 목록 조회
function searchList() {
 	reloadGrid("list", "form");
}

// 엑셀 다운로드
function excelDownload() {
	var f = document.form;
	f.action = "${context_path}/system/system/menu/userMng/excelDownload.do";
	f.submit();
}

//로그인
function goLogin(userId) {
	$("#userId").prop("disabled", false);
	
	var f = document.form;
	f.userId.value = userId;
	f.action = "${context_path}/loginProcess.do";
	f.submit();
}

</script>

<form:form commandName="searchVO" id="form" name="form" method="post">
    <form:hidden path="userId"/>
    <form:hidden path="findYear"/>
    <input type="hidden" name="adminLoginKey" value="${sessionScope.ADMIN_LOGIN_KEY}"/>

	<div class="sch-bx">
		<ul>
			<li>
				<label for="findSearchDeptNm"><spring:message code="word.deptNm"/></label>
				<form:input path="findSearchDeptNm"/>
			</li>
			<li>
				<label for="findSearchUserNm"><spring:message code="word.empNm"/></label>
				<form:input path="findSearchUserNm"/>
			</li>
		</ul>
		<a href="#" class="btn-sch" onclick="searchList();return false;"><spring:message code="button.search"/></a>
	</div>
	<div class="btn-dw">
		<%--
		<a href="#" class="excel" onclick="excelDownload();return false;"><span><spring:message code="button.excelDownload"/></span></a>
		--%>
	</div>
	<div class="gridContainer">
		<table id="list"></table>
		<div id="pager"></div>
	</div>
	<div class="tbl-bottom tbl-bottom2">
		<div class="tbl-btn">
			<%--
			<a href="#" class="save" onclick="saveSortOrder();return false;"><spring:message code="button.saveAll"/></a>
			<a href="#" class="new" onclick="addData();return false;"><spring:message code="button.add"/></a>
			<a href="#" class="delete" onclick="deleteData();return false;"><spring:message code="button.delete"/></a>
			--%>
		</div>
	</div>
</form:form>

