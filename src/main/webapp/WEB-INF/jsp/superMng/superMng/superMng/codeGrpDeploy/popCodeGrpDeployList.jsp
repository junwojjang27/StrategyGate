<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/common-taglibs.jsp"%>
<script type="text/javascript">
$(function(){
	
	$("#popDeployList").jqGrid({
		url			:	"${context_path}/superMng/superMng/superMng/codeGrpDeploy/popCodeGrpDeployList_json.do",
		postData	:	getFormData("popDeployForm"),
		width		:	"450",
		height		:	"300",  
		colModel	:	[
						{name:"compId",		index:"compId",		hidden:true},
						{name:"compNm",		index:"compNm",		width:250,	align:"left",	label:"<spring:message code="word.clientNm" />"},
						{name:"deployYn",	index:"deployYn",		width:100,	align:"center",	label:"<spring:message code="word.codeGrpDeployYn" />"}
						],
		rowNum		: ${jqgrid_rownum_max},
		cellEdit	: true,
		multiselect	: true,
		autowidth: true,
		loadComplete : function(){
			
			if("${searchVO.deployTargetYn}" === "Y"){
				
				//이미 배포 되어있어도 공통코드가 수정될 수 있으니 배포 되었어도 다시 적용 가능하도록 주석처리
				/*
				var ids = $("#popDeployList").jqGrid("getDataIDs");
				var rowObj;
				$(ids).each(function(i,e){
					rowObj = $("#popDeployList").jqGrid("getRowData",e);
					if(rowObj.deployYn == 'Y'){
						$("#popDeployList").find(":checkbox:contains('_"+e+"')").hide();
						$("#popDeployList").find(":checkbox[id*='_"+e+"']").hide();
					} 
				});	
				*/
			}else{
				$("#popDeployList").find(":checkbox").hide();
			}
			
		}
	});
	
	/*popup인 경우 gridContainerdp width를 정의하고 gridResize() 호출 해야 함.*/
	gridResize("popDeployList");
	
});

function closeDeployPop(){
	$.fancybox.close();
}

function saveDeployPop(){
	var compIds = "";
	var rowObj;
	var isOk = true;
	var cnt = 0;
	var ids = $("#popDeployList").jqGrid("getGridParam","selarrrow");
	
	$(ids).each(function(i,e){
		rowObj = $("#popDeployList").jqGrid("getRowData",e);
		
		compIds += rowObj.compId+"\|";
		
		cnt++;
	});
	$("#form #compIds").val(compIds);
	
	saveDeployDataDo();
	$.fancybox.close();
}

</script>
<div class="popup" style="width:600px;height:500px;">
<form:form commandName="searchVO" id="popDeployForm" name="popDeployForm" method="post">
	<form:hidden path="codeGrpId"/>
	<form:hidden path="deployTargetYn"/>
	<p class="title"><spring:message code="word.compCodeGrpDeploy"/></p>
	<div class="btn-dw">
	</div>
	<div class="gridContainer" style="width:600px;">
		<table id="popDeployList"></table>
	</div>	
	<div class="tbl-bottom">
		<div class="tbl-btn">
			<a href="#" class="save" onclick="saveDeployPop();return false;"><spring:message code="button.deploy"/></a>
			<a href="#" class="new" onclick="closeDeployPop();return false;"><spring:message code="button.close"/></a>
		</div>
	</div>
</form:form>
</div>
