<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/common-taglibs.jsp"%>
<script type="text/javascript">
$(function(){
	
	$("#popMenuDeployList").jqGrid({
		url			:	"${context_path}/superMng/superMng/superMng/menuDeployMng/popMenuDeployList_json.do",
		postData	:	getFormData("popMenuDeployForm"),
		width		:	"450",
		height		:	"300",  
		colModel	:	[
						{name:"compId",		index:"compId",		hidden:true},
						{name:"compNm",		index:"compNm",		width:250,	align:"left",	label:"<spring:message code="word.clientNm" />"},
						{name:"deployYn",	index:"deployYn",		width:100,	align:"center",	label:"<spring:message code="word.menuDeployYn" />"}
						],
		rowNum		: ${jqgrid_rownum_max},
		cellEdit	: true,
		multiselect	: true,
		autowidth: true,
		loadComplete : function(){
			/*			
			if("${searchVO.deployTargetYn}" === "Y"){
				var ids = $("#popMenuDeployList").jqGrid("getDataIDs");
				var rowObj;
				$(ids).each(function(i,e){
					rowObj = $("#popMenuDeployList").jqGrid("getRowData",e);
					if(rowObj.deployYn == 'Y'){
						$("#popMenuDeployList").find(":checkbox:contains('_"+e+"')").hide();
						$("#popMenuDeployList").find(":checkbox[id*='_"+e+"']").hide();
					} 
				});	
			}else{
				$("#popMenuDeployList").find(":checkbox").hide();
			}
			
			*/
			
		}
	});
	
	/*popup인 경우 gridContainerdp width를 정의하고 gridResize() 호출 해야 함.*/
	gridResize("popMenuDeployList");
	
});

function closeDeployPop(){
	$.fancybox.close();
}

function saveDeployPop(){
	var compIds = "";
	var rowObj;
	var isOk = true;
	var cnt = 0;
	var ids = $("#popMenuDeployList").jqGrid("getGridParam","selarrrow");
	
	$(ids).each(function(i,e){
		rowObj = $("#popMenuDeployList").jqGrid("getRowData",e);
		
		compIds += rowObj.compId+"\|";
		
		cnt++;
	});
	$("#form #tempCompIds").val(compIds);
	
	saveDeployDataDo();
	$.fancybox.close();
}

</script>
<div class="popup" style="width:600px;height:500px;">
<form:form commandName="searchVO" id="popMenuDeployForm" name="popMenuDeployForm" method="post">
	<form:hidden path="pgmId"/>
	<form:hidden path="deployTargetYn"/>
	<p class="title"><spring:message code="word.compMenuDeploy"/></p>
	<div class="btn-dw">
	</div>
	<div class="gridContainer" style="width:600px;">
		<table id="popMenuDeployList"></table>
	</div>	
	<div class="tbl-bottom">
		<div class="tbl-btn">
			<a href="#" class="save" onclick="saveDeployPop();return false;"><spring:message code="button.deploy"/></a>
			<a href="#" class="new" onclick="closeDeployPop();return false;"><spring:message code="button.close"/></a>
		</div>
	</div>
</form:form>
</div>
