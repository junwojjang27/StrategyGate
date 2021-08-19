<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/common-taglibs.jsp"%>
<script type="text/javascript">
$(function(){
	
	$("#popRoleList").jqGrid({
		url			:	"${context_path}/superMng/superMng/superMng/menuDeployMng/popRoleList_json.do",
		postData	:	getFormData("popRoleForm"),
		width		:	"450",
		height		:	"300",  
		colModel	:	[
						{name:"authGubunNm",	index:"authGubunNm",	width:100,	align:"left",	label:"<spring:message code="word.authGubun" />"},
						{name:"authGubun",		index:"authGubun",	hidden:true},
						{name:"checkYn",   		index:"checkYn",	hidden:true}
						],
		rowNum		: ${jqgrid_rownum_max},
		multiselect	: true,
		autowidth: true,
		loadComplete : function(){
			var ids = $("#popRoleList").jqGrid("getDataIDs");
			var rowObj;
			$(ids).each(function(i,e){
				rowObj = $("#popRoleList").jqGrid("getRowData",e);
				if(rowObj.checkYn == 'Y'){
					//$("input[name='jqg_popItemList_"+e+"']").prop("checked",true);
					//$("input[name='jqg_popItemList_"+e+"']").attr("checked","checked");
					$("#popRoleList").jqGrid("setSelection",e,true);
				}
			})
		}
	});
	
	/*popup인 경우 gridContainer  popGridResize() 호출 해야 함.*/
	popGridResize("popRoleList");
});

function closePop(){
	$.fancybox.close();
}

function savePop(){
	var selectRoleIds = "";
	var selectRoleNms = "";
	var rowObj;
	var ids = $("#popRoleList").jqGrid("getGridParam","selarrrow");
	
	$(ids).each(function(i,e){
		rowObj = $("#popRoleList").jqGrid("getRowData",e);
		if(ids.length-1 === i){
			selectRoleIds += rowObj.authGubun;
			selectRoleNms += rowObj.authGubunNm;
		}else{
			selectRoleIds += rowObj.authGubun+"\,";
			selectRoleNms += rowObj.authGubunNm+"\,";
		}
	});
	
	var rowId = "${param.targetRowId}";
	var rowData = $("#list").jqGrid("getRowData" ,rowId);
	//var $grid = $("#list");
	rowData["authGubunNms"] = selectRoleNms;
	rowData["authGubuns"] = selectRoleIds;
	//$("#list").jqGrid("setCell",rowId,"authGubunNms",selectRoleNms,"dirty-cell");
	//$("#list").jqGrid("setCell",rowId,"authGubuns",selectRoleIds,"dirty-cell");
	$("#list").jqGrid("setRowData", rowId, rowData,'edited');
	
	//saveSelectedItemDo();
	$.fancybox.close();
}

</script>
<div class="popup" style="width:600px;height:500px;">
<form:form commandName="searchVO" id="popRoleForm" name="popRoleForm" method="post">
	<form:hidden path="pgmId" />
	<form:hidden path="findYear" />
	
	<p class="title"><spring:message code="word.authGubun"/></p>
	<div class="btn-dw">
	</div>
	<div class="gridContainer">
		<table id="popRoleList"></table>
	</div>	
	<div class="tbl-bottom">
		<div class="tbl-btn">
			<a href="#" class="save" onclick="savePop();return false;"><spring:message code="button.save"/></a>
			<a href="#" class="new" onclick="closePop();return false;"><spring:message code="button.close"/></a>
		</div>
	</div>
</form:form>
</div>
