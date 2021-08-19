<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/common-taglibs.jsp"%>
<script type="text/javascript">
$(function(){
	$("#popList").jqGrid({
		url			:	"${context_path}/superMng/superMng/superMng/clientMng/clientHistoryList_json.do",
		postData	:	getFormData("form"),
		width		:	"${jqgrid_width}",
		height		:	"${jqgrid_height}",  
		colModel	:	[
							{name:"contractDt",		index:"contractDt",		width:100,	align:"center",	label:"<spring:message code="word.contractDt"/>"},
							{name:"serviceDt",		index:"serviceDt",		width:150,	align:"center",	label:"<spring:message code="word.serviceDt"/>"},
							{name:"serviceType",	index:"serviceType",	width:100,	align:"center",	label:"<spring:message code="word.serviceType"/>"}
						],
		rowNum		: ${jqgrid_rownum_max},
		sortname	: "contractDt",
		sortorder	: "asc",
		loadComplete : function(){
			gridResize("popList");
		}
	});
	
});
</script>
<div class="popup">
	<p class="title"><spring:message code="word.viewHistory"/></p>
	<div class="sch-bx">
		<ul>
			<li>
				<label><spring:message code="word.compId"/> : ${searchVO.newCompId}</label>
			</li>
			<li>
				<label><spring:message code="word.compNm"/> : ${searchVO.compNm}</label>
			</li>
		</ul>
	</div>
	<div class="btn-dw">
	</div>
	<div class="gridContainer">
		<table id="popList"></table>
	</div>
	
	<div class="tbl-bottom">
		<div class="tbl-btn">
			<a href="#" class="close" onclick="$.fancybox.close();return false;"><spring:message code="button.close"/></a>
		</div>
	</div>
</div>
