<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/common-taglibs.jsp"%>
<script type="text/javascript">
$(function(){
	
	$("#popItemCMetricList").jqGrid({
		url			:	"${context_path}/bsc/mon/dashboard/dashboard/popItemCMetricList_json.do",
		postData	:	getFormData("popItemCMetricForm"),
		width		:	"450",
		height		:	"300",  
		colModel	:	[
						{name:"metricId",		index:"metricId",		hidden:true},
						{name:"metricNm",		index:"metricNm",		width:350,	align:"left",	label:"<spring:message code="word.metric" />"},
						{name:"sortOrder",		index:"sortOrder",		width:100,	align:"center",	label:"<spring:message code="word.sortOrder" />",
							editable:true, edittype:"text", editrules:{required:true, custom:true, custom_func:jqGridChkNumDotBytes}, editoptions:{maxDot:5,maxlength:22}
						},
						{name:"checkYn",   	index:"checkYn",	hidden:true}
						],
		rowNum		: ${jqgrid_rownum_max},
		cellEdit	: true,
		multiselect	: true,
		autowidth: true,
		onSelectRow:function(rowId, status, e){
			if(status === true){
				$("#popItemCMetricList").find("#" + rowId).find("[aria-describedby=popItemCMetricList_sortOrder]").removeClass("not-editable-cell");
			}else{
				$("#popItemCMetricList").find("#" + rowId).find("[aria-describedby=popItemCMetricList_sortOrder]").addClass("not-editable-cell");
				$("#popItemCMetricList").jqGrid("setCell", rowId, "sortOrder", null);
			}
		},
		loadComplete : function(){
			var ids = $("#popItemCMetricList").jqGrid("getDataIDs");
			var rowObj;
			$("#popItemCMetricList").find("input[type='text']").prop("disabled", true);
			$(ids).each(function(i,e){
				rowObj = $("#popItemCMetricList").jqGrid("getRowData",e);
				if(rowObj.checkYn == 'Y'){
					$("#popItemCMetricList").jqGrid("setSelection",e,true);
					$("#popItemCMetricList").find("#" + e).find("[aria-describedby=popItemCMetricList_sortOrder]").removeClass("not-editable-cell");
				} else {
					$("#popItemCMetricList").find("#" + e).find("[aria-describedby=popItemCMetricList_sortOrder]").addClass("not-editable-cell");
				}
			});
		}
	});
	
	/*popup인 경우 gridContainerdp width를 정의하고 gridResize() 호출 해야 함.*/
	gridResize("popItemCMetricList");
	
});

function closeItemCPop(){
	$.fancybox.close();
}

function saveItemCPop(){
	var selectedMetricIds = "";
	var selectedSortOrders = "";
	var rowObj;
	var isOk = true;
	var cnt = 0;
	var ids = $("#popItemCMetricList").jqGrid("getGridParam","selarrrow");
	
	$(ids).each(function(i,e){
		rowObj = $("#popItemCMetricList").jqGrid("getRowData",e);
		
		if(rowObj.sortOrder === null || rowObj.sortOrder === undefined || rowObj.sortOrder === ""){
			isOk = false;
		}
		selectedMetricIds += rowObj.metricId+"\|";
		selectedSortOrders += rowObj.sortOrder+"\|";
		
		cnt++;
	});
	$("#form #selectedMetricIds").val(selectedMetricIds);
	$("#form #selectedSortOrders").val(selectedSortOrders);
	
	if(!isOk){
		alert("정렬순서를 모두 등록 해야 합니다.");
		return false;
	}
	if(cnt > 4){
		alert("반드시 4개의 지표를 선택해야 합니다.");
		return false;
	}
	saveSelectedMetricDo();
	$.fancybox.close();
}

</script>
<div class="popup" style="width:600px;height:500px;">
<form:form commandName="searchVO" id="popItemCMetricForm" name="popItemCMetricForm" method="post">
	<form:hidden path="findYear"/>
	<form:hidden path="findMon"/>
	<p class="title"><spring:message code="word.viewUserItem"/></p>
	<div class="btn-dw">
	</div>
	<div class="gridContainer" style="width:600px;">
		<table id="popItemCMetricList"></table>
	</div>	
	<div class="tbl-bottom">
		<div class="tbl-btn">
			<a href="#" class="save" onclick="saveItemCPop();return false;"><spring:message code="button.save"/></a>
			<a href="#" class="new" onclick="closeItemCPop();return false;"><spring:message code="button.close"/></a>
		</div>
	</div>
</form:form>
</div>
