<!-- 
*************************************************************************
* CLASS 명	: PastYearCopyList
* 작 업 자	: 박정현
* 작 업 일	: 2018-06-29
* 기	능	: 전년데이터일괄적용 List
* ---------------------------- 변 경 이 력 --------------------------------
* 번호	작 업 자		작	업	일			변 경 내 용				비고
* ----	---------	----------------	---------------------	-----------
*	1	박정현		2018-06-29				최 초 작 업
**************************************************************************
-->

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/common-taglibs.jsp"%>
<script type="text/javascript">
$(function(){
	$("#list").jqGrid({
		url			:	"${context_path}/system/system/batch/pastYearCopy/pastYearCopyList_json.do",
		postData	:	getFormData("form"),
		width		:	"${jqgrid_width}",
		height		:	"${jqgrid_height}",
		colModel	:	[
						{name:"systemGubun",	index:"systemGubun",	width:100,	align:"left",	label:"<spring:message code="word.division" />",
							formatter:function(cellvalue,options,rowobject){
								var str = "";
								if(cellvalue == 'system'){
									str = "<spring:message code="word.system" />";
								}else if(cellvalue == 'bsc'){
									str = "<spring:message code="word.deptSc" />";
								}else if(cellvalue == 'gov'){
									str = "<spring:message code="word.govManageEval" />";
								}else{
									str = cellvalue;
								}
								return str;
							}
						},
						{name:'tableNm',		index:'tableNm',		width:150,	align:'left',	label:"<spring:message code="word.item" />", formatter:linkCodeList},
						{name:'pastCnt',		index:'pastCnt',		width:100,	align:'center',	label:"<spring:message code="word.lastYearDataCnt" />" },
						{name:'newCnt',			index:'newCnt',			width:100,	align:'center',	label:"<spring:message code="word.currentDataCnt" />" },
						{name:'tableId',		index:'tableId',		jsonmap:'tableNm',	hidden:true}
						],
		rowNum		: ${jqgrid_rownum_max},
		sortname	: "sortOrder",
		sortorder	: "asc",
		cellEdit	: true,
		multiselect	: true,
		loadComplete : function() {
			//셀병합
			$("#list").rowspan("list",1,1);
		}
	});
	
});

// 목록 조회
function searchList() {
	loadPage("${context_path}/system/system/batch/pastYearCopy/pastYearCopyList.do", "form");
}

// 항목명
function linkCodeList(cellvalue, options, rowObject) {
	var displayLabel = '';
	if( cellvalue == 'COM_CODE' ){//공통코드
		displayLabel = "<spring:message code="word.commCode" />";
	}else if(cellvalue == 'COM_SIGNAL_STATUS'){//신호등
		displayLabel = "<spring:message code="word.signalLamp" />";
	}else if(cellvalue == 'BSC_EVAL_METHOD'){//평가등급
		displayLabel = "<spring:message code="word.evalGrade" />";
	}else if(cellvalue == 'BSC_SYSTEM_ITEM'){//시스템연계항목
		displayLabel = "<spring:message code="word.systemLinkItem" />";
	}else if(cellvalue == 'BSC_PERSPECTIVE'){//관점
		displayLabel = "<spring:message code="word.perspective" />";
	}else if(cellvalue == 'BSC_STRATEGY'){//전략목표
		displayLabel = "<spring:message code="word.strategy" />";
	}else if(cellvalue == 'BSC_SC_DEPT'){//성과조직
		displayLabel = "<spring:message code="word.scDeptNm3" />";
	}else if(cellvalue == 'BSC_METRIC_GRP'){//지표POOL
		displayLabel = "<spring:message code="word.metricPool" />";
	}else if(cellvalue == 'BSC_METRIC'){//지표
		displayLabel = "<spring:message code="word.metric" />";
	}else if(cellvalue == 'GOV_EVAL_CAT_GRP'){//경영평가범주
		displayLabel = "<spring:message code="word.manageEvalCate" />";
	}else if(cellvalue == 'GOV_EVAL_CAT'){//경영평가부문
		displayLabel = "<spring:message code="word.manageEvalSector" />";
	}else if(cellvalue == 'GOV_METRIC'){//경영평가지표
		displayLabel = "<spring:message code="word.manageEvalMetric" />";
	}
	return displayLabel;
}

//적용
function applyData() {
	var f = document.form;
	var tableIds = "";
	
		var selIds = $("#list").jqGrid("getGridParam","selarrrow");

		if(0 == selIds.length){
			$.showMsgBox("<spring:message code="errors.noSelectedData"/>");
			return false;
		}else{
			$.showConfirmBox("<spring:message code="system.system.batch.pastYearCopy" arguments="${searchVO.findYear-1}, ${searchVO.findYear}"/>", function() {
				$(selIds).each(function(idx,el){
					var rowObj = $("#list").jqGrid("getRowData",el);
					tableIds += rowObj.tableId+"|";
				});
				
				$("#tableIds").val(tableIds);
				
				sendAjax({
					"url" : "${context_path}/system/system/batch/pastYearCopy/applyPastYearCopy.do",
					"data" : getFormData("form"),
					"doneCallbackFunc" : "checkResult"
				});
			})
		}
}

function checkResult(data) {
	$.showMsgBox(data.msg);
	if(data.result == AJAX_SUCCESS) {
		searchList();
	}
}
</script>

<form:form commandName="searchVO" id="form" name="form" method="post">
	<form:hidden path="tableIds"/>
	
	<div class="sch-bx">
		<ul>
			<li>
				<label for="findYear"><spring:message code="word.year"/></label>
				<form:select path="findYear" class="select wx100" items="${codeUtil:getCodeList('017')}" itemLabel="codeNm" itemValue="codeId">
				</form:select>
			</li>
		</ul>
		<a href="#" class="btn-sch" onclick="searchList();return false;"><spring:message code="button.search"/></a>
	</div>
	<div class="btn-dw">
	</div>
	<div class="gridContainer">
		<table id="list"></table>
	</div>
	<div class="tbl-bottom mt0">
		<div class="tbl-btn">
			<a href="#" class="save" onclick="applyData();return false;"><spring:message code="button.apply"/></a>
		</div>
	</div>
</form:form>

