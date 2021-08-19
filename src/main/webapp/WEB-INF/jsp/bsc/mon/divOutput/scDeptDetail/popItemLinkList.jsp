<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/common-taglibs.jsp"%>
<script type="text/javascript">
$(function(){
	
	$("#popItemLinkList").jqGrid({
		url			:	"${context_path}/bsc/mon/divOutput/scDeptDetail/popItemLinkList_json.do",
		postData	:	getFormData("popItemLinkForm"),
		width		:	"820",
		height		:	"300",  
		colModel	:	[
						{name:"deptId",			index:"deptId",			hidden:true},
						{name:"deptNm",			index:"deptNm",			width:350,	align:"left",	label:"<spring:message code="word.org" />"},
						{name:"mon",			index:"mon",			width:100,	align:"center",	label:"<spring:message code="word.month" />"},
						{name:"actual",			index:"actual",			width:185,	align:"right",	label:"<spring:message code="word.actual" />"},
						{name:"adjustActual",	index:"adjustActual",	width:185,	align:"right",	label:"<spring:message code="word.adjustActual" />"}
						],
		rowNum		: ${jqgrid_rownum_max},
		multiselect	: false,
		autowidth: true
	});
	
	//$("#hisForm").hide();
	
});

function closePop(){
	$.fancybox.close();
}

</script>
<div class="popup">
<form:form commandName="searchVO" id="popItemLinkForm" name="popItemLinkForm" method="post">
	<form:hidden path="findYear"/>
	<form:hidden path="findMon"/>
	<form:hidden path="metricId"/>
	<form:hidden path="calTypeCol"/>
	<p class="title"><spring:message code="word.inquireLinkData"/></p>
	<div class="sch-bx">
		<ul>
			<li>
				<label><spring:message code="word.year"/> : <spring:message code="unit.yearCommon" arguments="${searchVO.findYear}"/><spring:message code="unit.monthCommon" arguments="${searchVO.findMon}"/></label>
			</li>
			<li>
				<label><spring:message code="word.linkItem"/> : <c:out value="${calTypeColNm}" /></label>
			</li>
		</ul>
	</div>
	<div class="btn-dw">
	</div>
	<table id="popItemLinkList"></table>
	<div class="tbl-bottom">
		<div class="tbl-btn">
			<a href="#" class="save" onclick="closePop();return false;"><spring:message code="button.close"/></a>
		</div>
	</div>
</form:form>
</div>
