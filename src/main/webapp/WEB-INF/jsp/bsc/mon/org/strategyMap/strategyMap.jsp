<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/common-taglibs.jsp"%>
<link href="${theme_path}/svgChart.css" type="text/css" rel="stylesheet"/>
<script type="text/javascript" src="${js_path}/d3.min.js"></script>
<script type="text/javascript" src="${js_path}/svgChart/svgChart.js"></script>
<script type="text/javascript">
$(function() {
	$("#list").jqGrid({
		url			:	"${context_path}/bsc/mon/org/strategyMap/selectList.do",
		datatype	:	"local",
		width		:	"${jqgrid_width}",
		height		:	"${jqgrid_height_diagBottom}",
		colModel	:	[
							{name:"metricId",	index:"metricId",	hidden:true},
							{name:"sortOrder",	index:"sortOrder",	hidden:true},
							{name:"metricNm",	index:"metricNm",	width:150,	align:"left",	title:true,	label:"<spring:message code="word.metric"/>",
								formatter:function(cellvalue, options, rowObject) {
									return "<a href='#' onclick='showChart(\"" + removeNull(options.rowId) + "\");clickGridRow(this);return false;'>" + escapeHTML(removeNull(cellvalue)) + "</a>";
								}, unformat:linkUnformatter
							},
							{name:"evalCycle",	index:"evalCycle",	width:50,	align:"center",	title:true,	label:"<spring:message code="word.evalCycle"/>"},
							{name:"weight",		index:"weight",		width:50,	align:"center",	title:true,	label:"<spring:message code="word.weight"/>"},
							{name:"unit",		index:"unit",		width:50,	align:"center",	title:true,	label:"<spring:message code="word.unit"/>"},
							{name:"actual",		index:"actual",		width:100,	align:"right",	title:true,	label:"<spring:message code="word.actual"/>",
								formatter:"number",	formatoptions:{thousandsSeperator:",", decimalPlaces:DECIMAL_SCALE, defaultValue:0, removePointZeros:REMOVE_POINT_ZEROS}},
							{name:"finalScore",	index:"finalScore",	width:70,	align:"right",	title:true,	label:"<spring:message code="word.mark"/>",
								formatter:"number",	formatoptions:{thousandsSeperator:",", decimalPlaces:DECIMAL_SCALE, defaultValue:0, removePointZeros:REMOVE_POINT_ZEROS}},
							{name:"color",		index:"color",		width:30,	align:"center",	label:"<spring:message code="word.status"/>",
								formatter:function(cellvalue, options, rowObject) {
									if(cellvalue == null) {
										cellvalue = "";
									}
									return "<div class='signalCircle' style='background-color:" + cellvalue + "'></div>";
								}
							}
						],
		rowNum		: ${jqgrid_rownum_max},
		sortname	: "sortOrder",
		sortorder	: "asc",
		loadComplete : function() {
			gridResize("list");
			if($(this).jqGrid("getGridParam", "records") == 0) {
				$("#detailTitle2").text("-");
				return;
			}
			showChart(1);
		}
	});
	
	barChart = new Chart($("#canvas")[0].getContext("2d"), {
		type: "bar",
		data: {
				labels: [
					"<spring:message code="unit.month01"/>",	"<spring:message code="unit.month02"/>",	"<spring:message code="unit.month03"/>",	"<spring:message code="unit.month04"/>",
					"<spring:message code="unit.month05"/>",	"<spring:message code="unit.month06"/>",	"<spring:message code="unit.month07"/>",	"<spring:message code="unit.month08"/>",
					"<spring:message code="unit.month09"/>",	"<spring:message code="unit.month010"/>",	"<spring:message code="unit.month011"/>",	"<spring:message code="unit.month012"/>"
					],
				datasets: [{
					type: "bar",
					label : "<spring:message code="word.target"/>",
					yAxisID : "yAxis1",
					backgroundColor: chartJsCustom.getBGColor(2),
					borderColor: chartJsCustom.getColor(2),
					borderWidth: 1,
					data: []
				},{
					type: "bar",
					label : "<spring:message code="word.actual"/>",
					yAxisID : "yAxis1",
					backgroundColor: chartJsCustom.getBGColor(1),
					borderColor: chartJsCustom.getColor(1),
					borderWidth: 1,
					data: []
				},{
					type: "line",
					fill: false,
					label : "<spring:message code="word.mark"/>",
					yAxisID : "yAxis2",
					backgroundColor: chartJsCustom.getColor(0),
					borderColor: chartJsCustom.getColor(0),
					borderWidth: 2,
					lineTension: 0,
					data: []
				}]
			},
		options: {
					tooltips: {
						callbacks: {
							label : function(tooltipItem, data) {
								return data.datasets[tooltipItem.datasetIndex].label + " : " + addComma(tooltipItem.yLabel);
							}
						}
					},
					responsive: true,
					maintainAspectRatio : false,
					layout : {
						padding : {
							top : 20
						}
					},
					legend: {
						display : true,
						position : "bottom"
					},
					scales : {
						xAxes : [{
							gridLines : {
								display : false
							}
						}],
						yAxes : [{
									ticks:{
											maxTicksLimit : 5,
											callback: function(label, index, labels) {
												return addComma(label);
											}
										},
									id : "yAxis1",
									position : "left"
								},
								{
									ticks:{
											min : 0,
											max : 100,
											maxTicksLimit : 5,
											callback: function(label, index, labels) {
												return addComma(label);
											}
										},
									id : "yAxis2",
									position : "right",
									gridLines: {
													drawOnChartArea: false
												}
								}]
					}
				}
	});
	
	reloadSvgChart();
});

var svgChart;
function reloadSvgChart() {
	$("#detailDiv").hide();
	
	var findYear = $("#findYear").val();
	var findMon = $("#findMon").val();
	var findAnalCycle = $("[name=findAnalCycle]:checked").val();
	var findScDeptId = $("#findScDeptId").val();
	
	$("#strategyMap").empty();
	svgChart = new SvgChart({
		"width"	: 1250,
		"height": 500,
		"targetId" : "strategyMap",
		"imgPath" : "${context_path}/images/backgrounds/",
		"url" : "${context_path}/bsc/mon/org/strategyMap/strategyMap_xml.do?suffix=jsp%26findYear=" + findYear + "%26findMon=" + findMon + "%26findAnalCycle=" + findAnalCycle + "%26metric_gbn=BSC%26findScDeptId=" + findScDeptId + "%26admin_bool=_StartegyAdmin_%26gubun=0%26img_url=${root_img_path}%26context_url=${context_path}",
		"trafficUrl" : "${context_path}/bsc/mon/org/strategyMap/trafficSignal_xml.do?findYear=" + findYear + "&gubun=0",
		"showMetric" : false
	});
	
	$("#detailTitle1").text(getScDeptNm(findScDeptId));
	setEmptyChart();
	reloadGrid("list", "form");
	
	gridResize("list");
	$("#detailDiv").show();
}

function goChartSearch(metricId, metricNm) {
	$($("#list").jqGrid("getDataIDs")).each(function(i, e) {
		if(metricId == $("#list").jqGrid("getRowData", e).metricId) {
			$("#list tr#" + e).click();
			showChart(e);
			return false;
		}
	});
}

function showChart(rowId) {
	$("#list tr#" + rowId).click();
	var rowData = $("#list").jqGrid("getRowData", rowId);
	
	$("#findMetricId").val(rowData.metricId);
	$("#detailTitle2").text(rowData.metricNm);
	
	sendAjax({
		"url" : "${context_path}/bsc/mon/org/strategyMap/selectChartData.do",
		"data" : getFormData("form"),
		"doneCallbackFunc" : "setChartData"
	});
}

var targetArr, actualArr, scoreArr;
function setChartData(data) {
	if(isNotEmpty(data.list)) {
		targetArr = [];
		actualArr = [];
		scoreArr = [];
		$(data.list).each(function(i, e) {
			targetArr.push(e.target);
			actualArr.push(e.actual);
			scoreArr.push(e.finalScore);
		});
		
		barChart.data.datasets[0].data = targetArr;
		barChart.data.datasets[1].data = actualArr;
		barChart.data.datasets[2].data = scoreArr;
		barChart.update();
	} else {
		setEmptyChart();
	}
}

function setEmptyChart() {
	barChart.data.datasets[0].data = [];
	barChart.data.datasets[1].data = [];
	barChart.data.datasets[2].data = [];
	barChart.update();
}
</script>
<form:form commandName="searchVO" id="form" name="form" method="post">
	<form:hidden path="findScDeptId"/>
	<input type="hidden" name="findMetricId" id="findMetricId"/>
	<div class="sch-bx">
		<ul>
			<li>
				<label for="findYear"><spring:message code="word.year"/></label>
				<form:select path="findYear" class="select wx100" items="${codeUtil:getCodeList('017')}" itemLabel="codeNm" itemValue="codeId">
				</form:select>
				<form:select path="findMon" class="select wx80" items="${codeUtil:getCodeList('024')}" itemLabel="codeNm" itemValue="codeId">
				</form:select>
			</li>
			<li>
				<div class="sch-oga" id="findScDeptList"></div>
			</li>
			<li class="ml20">
				<form:radiobuttons path="findAnalCycle" items="${codeUtil:getCodeList('138')}" itemLabel="codeNm" itemValue="codeId" />
			</li>
		</ul>
		<a href="#" class="btn-sch" onclick="reloadSvgChart();return false;"><spring:message code="button.search"/></a>
	</div>
	<div class="table-bx2">
		<div id="strategyMap"></div>
	</div>
	
	<div id="detailDiv" class="hx350 hide2">
		<div class="wp49 gridContainer fl hx300">
			<div class="ptitle" id="detailTitle1"></div>
			<table id="list"></table>
		</div>
		<div id="chart" class="wp49 fr hx350">
			<div class="ptitle_noLeftMag" id="detailTitle2"></div>
			<canvas id="canvas" class="wp95 hx270"></canvas>
		</div>
	</div>
</form:form>