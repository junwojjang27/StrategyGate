<!--
*************************************************************************
* CLASS 명	: ScDeptDetailList
* 작 업 자	: 현걸욱
* 작 업 일	: 2018-04-25
* 기	능	: 조직성과상세 List
* ---------------------------- 변 경 이 력 --------------------------------
* 번호	작 업 자		작	업	일			변 경 내 용				비고
* ----	---------	----------------	---------------------	-----------
*	1	현걸욱		2018-04-25				최 초 작 업
**************************************************************************
-->
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/common-taglibs.jsp"%>
<script type="text/javascript">
$(function(){
	$("#scDeptNm").text(opener.getScDeptNm($("#findScDeptId").val()));

	$("#yearMon").text($("#findYear").val()+"<spring:message code='unit.year' /> "+$("#findMon").val()+"<spring:message code='unit.month' />");
	$("#listPop").jqGrid({
		url			:	"${context_path}/bsc/mon/divOutput/scDeptDetail/scDeptDetailList_json.do",
		postData	:	getFormData("formPop"),
		width		:	"${jqgrid_width}",
		height		:	"${jqgrid_height5}",
		colModel	:	[
						{name:"year",		index:"year",		hidden:true},
						{name:"mon",		index:"mon",		hidden:true},
						{name:"analCycle",	index:"analCycle",	hidden:true},
						{name:"perspectiveNm",	index:"perspectiveNm",	width:100,	align:"center",	label:"<spring:message code='word.perspective' />"},
						{name:"strategyNm",		index:"strategyNm",	width:100,	align:"left",	label:"<spring:message code='word.strategy' />"},
						{name:"metricId",	index:"metricId",	hidden:true},
						{name:"metricNm",	index:"metricNm",	width:250,	align:"left",	label:"<spring:message code='word.metric' />",
							formatter:function(cellvalue,options,rowObject){
								return "<a href='#' onclick='showDetail(\"" + removeNull(rowObject.metricId) + "\",\"" + removeNull(rowObject.metricNm) + "\",\"" + removeNull(rowObject.typeId) + "\",\"" + options.rowId + "\");return false;'>" + escapeHTML(removeNull(cellvalue)) + "</a>";
							},
							unformat:linkUnformatter
						},
						{name:"propertyNm",		index:"propertyNm",	width:60,	align:"center",	label:"<spring:message code='word.attr' />"},
						{name:"typeId",			index:"typeId",	hidden:true},
						{name:"typeNm",			index:"typeNm",	width:60,	align:"center",	label:"<spring:message code='word.type' />"},
						{name:"unitNm",			index:"unitNm",	width:60,	align:"center",	label:"<spring:message code='word.unit' />"},
						{name:"evalCycleNm",	index:"evalCycleNm",	width:60,	align:"center",	label:"<spring:message code='word.cycle' />"},
						{name:"weight",			index:"weight",	width:60,	align:"center",	label:"<spring:message code='word.weight' />"},
						{name:"target",			index:"target",	width:130,	align:"right",	label:"<spring:message code='word.target' />",
							formatter:'number', formatoptions: { defaultValue: "&#160;", decimalSeparator: ".", thousandsSeparator: ",", decimalPlaces:DECIMAL_SCALE, removePointZeros:REMOVE_POINT_ZEROS}
						},
						{name:"actual",			index:"actual",	width:130,	align:"right",	label:"<spring:message code='word.actual' />",
							formatter:'number', formatoptions: { defaultValue: "&#160;", decimalSeparator: ".", thousandsSeparator: ",", decimalPlaces:DECIMAL_SCALE, removePointZeros:REMOVE_POINT_ZEROS}
						},
						{name:"finalScore",		index:"finalScore",	width:70,	align:"right",	label:"<spring:message code='word.score' />",
							formatter:'number', formatoptions: { defaultValue: "&#160;", decimalSeparator: ".", thousandsSeparator: ",", decimalPlaces:DECIMAL_SCALE, removePointZeros:REMOVE_POINT_ZEROS}
						},
						{name:"weightScore",	index:"weightScore",	width:70,	align:"right",	label:"<spring:message code='word.weightScore' />",
							formatter:'number', formatoptions: { defaultValue: "&#160;", decimalSeparator: ".", thousandsSeparator: ",", decimalPlaces:DECIMAL_SCALE, removePointZeros:REMOVE_POINT_ZEROS}
						},
						{name:"color",			index:"color",	width:40,	align:"center",	label:"<spring:message code='word.status' />", formatter:getStatusColor}
						],
		rowNum		: ${jqgrid_rownum_max},
		multiselect	: false,
		loadComplete : function(){

			if($("#listPop").jqGrid("getGridParam","records") > 0){

				$("#actArea").show();
				$("#initiativeArea").show();

				var rowObj = $("#listPop").jqGrid("getRowData",1);
				$("#formPop #metricId").val(rowObj.metricId);
				$("#formPop #metricNm").val(rowObj.metricNm);
				$("#formPop #typeId").val(rowObj.typeId);

				showDetail(rowObj.metricId,rowObj.metricNm,rowObj.typeId,1);

			}else{
				$("#actArea").hide();
				$("#initiativeArea").hide();
			}
		}
	});
});

function getStatusColor(cellValue, options, rowObject){
	return "<div class='signalTable' style='background-color:"+rowObject.color+";' >&nbsp;</div>";
}

function getActual(){
	var $grid = $("#actList");
	delete $grid;
	$("#actList").GridUnload("#actList");

	$("#actList").jqGrid({
		url			:	"${context_path}/bsc/mon/divOutput/scDeptDetail/actualMngActList_json.do",
		postData	:	getFormData("formPop"),
		width		:	"${jqgrid_width}",
		height		:	"${jqgrid_height5}",
		colModel	:	[
						{name:"analCycle",		index:"analCycle",		frozen:true, hidden:true},
						{name:"analCycleNm",	index:"analCycleNm",	frozen:true, width:60,	align:"center",	label:"<spring:message code="word.cycle"/>"},
						{name:"calTypeCol",		index:"calTypeCol",		frozen:true, hidden:true},
						{name:"calTypeColNm",	index:"calTypeColNm",	frozen:true, width:150,	align:"left",	label:"<spring:message code="word.calItem"/>",
							formatter:function(cellvalue,options,rowobject){
								var str = "";
								if(cellvalue == 'actual'){
									str = "<spring:message code="word.actual" />";
								}else if(cellvalue == 'target'){
									str = "<spring:message code="word.target" />";
								}else if(cellvalue == 'score'){
									str = "<spring:message code="word.score" />";
								}else{
									str = cellvalue;
								}
								return str;
							}
						},
						{name:"unit",			index:"unit",			frozen:true, hidden:true},
						{name:"unitNm",			index:"unitNm",			frozen:true, width:70,	align:"center",	label:"<spring:message code="word.unit"/>"},
						{name:"linkGbn",		index:"linkGbn",		frozen:true, width:80,	align:"center",	label:"<spring:message code="word.link"/>",
							formatter:function(c,o,obj){
								var str = "";
								if(c == 'item' && obj.analCycle == 'M'){
									str = "<input type='button' class='pButton_grid' onclick='popItemLink(\""+obj.calTypeCol+"\")' value='항목연계'/>";
								}else if(c == 'metric' && obj.analCycle == 'M'){
									str = "<input type='button' class='pButton_grid' onclick='popMetricLink(\""+obj.calTypeCol+"\")' value='지표연계'/>";
								}else{
									str = "-";
								}
								return str;
							}
						},
						{name:"monYn",			index:"monYn",			frozen:true, hidden:true},
						{name:"inputYn",		index:"inputYn",		frozen:true, hidden:true},
						{name:"mon01",			index:"mon01",			width:150,	align:"right",	label:"<spring:message code="unit.month1"/>",
							formatter:'number', formatoptions: { defaultValue: "&#160;", decimalSeparator: ".", thousandsSeparator: ",", decimalPlaces:DECIMAL_SCALE, removePointZeros:REMOVE_POINT_ZEROS}
						},
						{name:"mon02",			index:"mon02",			width:150,	align:"right",	label:"<spring:message code="unit.month2"/>",
							formatter:'number', formatoptions: { defaultValue: "&#160;", decimalSeparator: ".", thousandsSeparator: ",", decimalPlaces:DECIMAL_SCALE, removePointZeros:REMOVE_POINT_ZEROS}
						},
						{name:"mon03",			index:"mon03",			width:150,	align:"right",	label:"<spring:message code="unit.month3"/>",
							formatter:'number', formatoptions: { defaultValue: "&#160;", decimalSeparator: ".", thousandsSeparator: ",", decimalPlaces:DECIMAL_SCALE, removePointZeros:REMOVE_POINT_ZEROS}
						},
						{name:"mon04",			index:"mon04",			width:150,	align:"right",	label:"<spring:message code="unit.month4"/>",
							formatter:'number', formatoptions: { defaultValue: "&#160;", decimalSeparator: ".", thousandsSeparator: ",", decimalPlaces:DECIMAL_SCALE, removePointZeros:REMOVE_POINT_ZEROS}
						},
						{name:"mon05",			index:"mon05",			width:150,	align:"right",	label:"<spring:message code="unit.month5"/>",
							formatter:'number', formatoptions: { defaultValue: "&#160;", decimalSeparator: ".", thousandsSeparator: ",", decimalPlaces:DECIMAL_SCALE, removePointZeros:REMOVE_POINT_ZEROS}
						},
						{name:"mon06",			index:"mon06",			width:150,	align:"right",	label:"<spring:message code="unit.month6"/>",
							formatter:'number', formatoptions: { defaultValue: "&#160;", decimalSeparator: ".", thousandsSeparator: ",", decimalPlaces:DECIMAL_SCALE, removePointZeros:REMOVE_POINT_ZEROS}
						},
						{name:"mon07",			index:"mon07",			width:150,	align:"right",	label:"<spring:message code="unit.month7"/>",
							formatter:'number', formatoptions: { defaultValue: "&#160;", decimalSeparator: ".", thousandsSeparator: ",", decimalPlaces:DECIMAL_SCALE, removePointZeros:REMOVE_POINT_ZEROS}
						},
						{name:"mon08",			index:"mon08",			width:150,	align:"right",	label:"<spring:message code="unit.month8"/>",
							formatter:'number', formatoptions: { defaultValue: "&#160;", decimalSeparator: ".", thousandsSeparator: ",", decimalPlaces:DECIMAL_SCALE, removePointZeros:REMOVE_POINT_ZEROS}
						},
						{name:"mon09",			index:"mon09",			width:150,	align:"right",	label:"<spring:message code="unit.month9"/>",
							formatter:'number', formatoptions: { defaultValue: "&#160;", decimalSeparator: ".", thousandsSeparator: ",", decimalPlaces:DECIMAL_SCALE, removePointZeros:REMOVE_POINT_ZEROS}
						},
						{name:"mon10",			index:"mon10",			width:150,	align:"right",	label:"<spring:message code="unit.month10"/>",
							formatter:'number', formatoptions: { defaultValue: "&#160;", decimalSeparator: ".", thousandsSeparator: ",", decimalPlaces:DECIMAL_SCALE, removePointZeros:REMOVE_POINT_ZEROS}
						},
						{name:"mon11",			index:"mon11",			width:150,	align:"right",	label:"<spring:message code="unit.month11"/>",
							formatter:'number', formatoptions: { defaultValue: "&#160;", decimalSeparator: ".", thousandsSeparator: ",", decimalPlaces:DECIMAL_SCALE, removePointZeros:REMOVE_POINT_ZEROS}
						},
						{name:"mon12",			index:"mon12",			width:150,	align:"right",	label:"<spring:message code="unit.month12"/>",
							formatter:'number', formatoptions: { defaultValue: "&#160;", decimalSeparator: ".", thousandsSeparator: ",", decimalPlaces:DECIMAL_SCALE, removePointZeros:REMOVE_POINT_ZEROS}
						}
						],
		rowNum		: ${jqgrid_rownum_max},
		cmTemplate :{sortable:false},
		multiselect	: false,
		shrinkToFit : false,
		loadonce    : false,
		loadComplete:function(){

			//셀병합
			$("#actList_frozen").rowspan("actList_frozen",1,0);

		},
		gridComplete: function() {
			var rowCnt = $("#actList").jqGrid('getGridParam', 'records');
			if(0<rowCnt){
				var ot = $("#actList .jqgrow").find("td[aria-describedby='actList_mon"+$("#formPop #findMon").val()+"']").offset().left;
				var os = $("#actList .jqgrow").find("td[aria-describedby='actList_mon01']").offset().left;
				$("#actList").closest(".ui-jqgrid-bdiv").animate({scrollLeft:ot-os},500);
			}
		}

	});
	$("#actList").jqGrid("setFrozenColumns");
}

function popItemLink(calTypeCol){
	$("#calTypeCol").val(calTypeCol);
	openFancybox({
		"url" : "${context_path}/bsc/mon/divOutput/scDeptDetail/popItemLinkList.do",
		"data" : getFormData("formPop")
	});
}

function popMetricLink(calTypeCol){
	$("#calTypeCol").val(calTypeCol);
	$("#formPop #hisMetricId").val($("#formPop #metricId").val());
	openFancybox({
		"url" : "${context_path}/bsc/mon/divOutput/scDeptDetail/popMetricLinkList.do",
		"data" : getFormData("formPop")
	});
}

function showDetail(metricId,metricNm,typeId,rowId){
	clickGridRowByRowId("listPop",rowId);

	$("#formPop #metricId").val(metricId);
	$("#formPop #typeId").val(typeId);

	if(typeId == '02'){
		$("#actArea").hide();
		$("#initiativeArea").show();

		$("#titleInitiative").empty();
		$("#titleInitiative").text(metricNm+" <spring:message code="word.actionPlanActual"/>");

	}else{
		$("#actArea").show();
		$("#initiativeArea").show();
	 	getActual();

	 	$("#titleActual").empty();
		$("#titleInitiative").empty();
		$("#titleActual").text(metricNm+" <spring:message code="word.monthlyActualDetail" />");
		$("#titleInitiative").text(metricNm+" <spring:message code="word.actionPlanActual"/>");

	}
}

// 목록 조회
function searchListPop() {
	reloadGrid("listPop", "formPop");

}
</script>

<form:form commandName="searchVO" id="formPop" name="formPop" method="post">
 	<form:hidden path="findYear"/>
	<form:hidden path="findMon"/>
	<form:hidden path="analCycle"/>
	<form:hidden path="calTypeCol"/>
	<form:hidden path="metricId"/>
	<form:hidden path="hisMetricId"/>
	<form:hidden path="typeId"/>
	<form:hidden path="initiativeId"/>
	<form:hidden path="findScDeptId"/>
	<form:hidden path="findStrategyId"/>

	<div class="sch-bx">
		<ul>
			<li>
				<label for="findYear"><spring:message code="word.baseYearMon"/></label> : <span id="yearMon"></span>
			</li>
			<li>
				<label><spring:message code="word.scDeptNm"/></label> : <span id="scDeptNm"></span>
			</li>
			<li>
				<label for="findAnalCycle"><spring:message code="word.analysisCycle"/></label>
				<form:radiobuttons path="findAnalCycle" items="${codeUtil:getCodeList('138')}" itemLabel="codeNm" itemValue="codeId" />
			</li>
		</ul>
		<a href="#" class="btn-sch" onclick="searchListPop();return false;"><spring:message code="button.search"/></a>
	</div>
	<div class="btn-dw">
	</div>
	<div class="gridContainer">
		<table id="listPop"></table>
		<div id="pager"></div>
	</div>

	<div id="actArea">
		<div class="ptitle3 for_popup">
			<div class="txt" id="titleActual"><c:out value="${searchVO.metricNm}" /> <spring:message code="word.monthlyActualDetail" /></div>
		</div>
		<div class="gridContainer" id="monthlyActualDetailDiv">
			<table id="actList"></table>
			<div id="pager"></div>
		</div>
	</div>
</form:form>

