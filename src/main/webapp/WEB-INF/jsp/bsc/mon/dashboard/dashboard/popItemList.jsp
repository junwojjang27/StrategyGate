<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/common-taglibs.jsp"%>
<script type="text/javascript">
$(function(){
	
	$("#popItemList").jqGrid({
		url			:	"${context_path}/bsc/mon/dashboard/dashboard/popItemList_json.do",
		postData	:	getFormData("popItemForm"),
		width		:	"450",
		height		:	"300",  
		colModel	:	[
						{name:"itemId",		index:"itemId",		width:100,	align:"left",	label:"<spring:message code="word.item" />"},
						{name:"itemNm",		index:"itemNm",		width:350,	align:"left",	label:"<spring:message code="word.content" />"},
						{name:"checkYn",   	index:"checkYn",	hidden:true}
						],
		rowNum		: ${jqgrid_rownum_max},
		multiselect	: true,
		autowidth: true,
		loadComplete : function(){
			var ids = $("#popItemList").jqGrid("getDataIDs");
			var rowObj;
			$(ids).each(function(i,e){
				rowObj = $("#popItemList").jqGrid("getRowData",e);
				if(rowObj.checkYn == 'Y'){
					//$("input[name='jqg_popItemList_"+e+"']").prop("checked",true);
					//$("input[name='jqg_popItemList_"+e+"']").attr("checked","checked");
					$("#popItemList").jqGrid("setSelection",e,true);
				}
			})
		}
	});
	
});

function closePop(){
	$.fancybox.close();
}

function savePop(){
	var selectedItemIds = "";
	var rowObj;
	var ids = $("#popItemList").jqGrid("getGridParam","selarrrow");
	
	$(ids).each(function(i,e){
		rowObj = $("#popItemList").jqGrid("getRowData",e);
		selectedItemIds += rowObj.itemId+"\|";
	});
	$("#form #itemIds").val(selectedItemIds);
	saveSelectedItemDo();
	$.fancybox.close();
}

</script>
<div class="popup" style="width:600px;height:500px;">
<form:form commandName="searchVO" id="popItemForm" name="popItemForm" method="post">
	<form:hidden path="findYear"/>
	<form:hidden path="findMon"/>
	<p class="title"><spring:message code="word.viewUserItem"/></p>
	<div class="btn-dw">
	</div>
	<div class="gridContainer">
		<table id="popItemList"></table>
	</div>	
	<div class="tbl-bottom">
		<div class="tbl-btn">
			<a href="#" class="save" onclick="savePop();return false;"><spring:message code="button.save"/></a>
			<a href="#" class="new" onclick="closePop();return false;"><spring:message code="button.close"/></a>
		</div>
	</div>
</form:form>
</div>
