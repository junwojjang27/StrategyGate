<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/common-taglibs.jsp"%>
<link href="${theme_path}/scDeptChart.css" type="text/css" rel="stylesheet"/>
<script type="text/javascript">
var chart;
$(function() {
	if($.inArray($("#findScDeptId").val(),	gMonitoringScDeptIdArr) == -1) {
		$("#findScDeptId").val(gMonitoringRootScDeptId);
	}
	
	$("#list").jqGrid({
		url			:	"${context_path}/bsc/mon/org/scDeptDiag/selectList.do",
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
	
	loadScDeptChart();
});

function loadScDeptChart() {
	sendAjax({
		"url" : "${context_path}/bsc/mon/org/scDeptDiag/selectScDeptData.do",
		"data" : getFormData("form"),
		"doneCallbackFunc" : function(data) {
			drawScDeptChart(data.scDeptList, data.signalList);
		}
	});
}

function drawScDeptChart(scDeptList, signalList) {
	var signalMap = {};
	signalMap[""] = "#E1E1E1";	// 기본 색상
	if(isNotEmpty(signalList)) {
		for(var i in signalList) {
			signalMap[signalList[i].codeNm.toUpperCase()] = signalList[i].color;
		}
	}
	
	$(".scDeptChart").empty();
	var scDeptId, scDeptNm, upScDeptId, levelId, statusNm;
	var $ul, $li, $div, htmlStr;
	var findScDeptId = $("#findScDeptId").val();
	var findCnt = 0;
	var minLevelId = -1;
	for(var i in scDeptList) {
		levelId = parseInt(scDeptList[i].levelId, 10);
		if(minLevelId == -1 || levelId < minLevelId) minLevelId = levelId;
	}
	
	for(var i in scDeptList) {
		scDeptId = escapeHTML(removeNull(scDeptList[i].scDeptId));
		scDeptNm = escapeHTML(removeNull(scDeptList[i].scDeptNm));
		upScDeptId = escapeHTML(removeNull(scDeptList[i].upScDeptId));
		levelId = parseInt(scDeptList[i].levelId, 10) - minLevelId + 1;
		
		statusNm = escapeHTML(removeNull(scDeptList[i].statusNm)).toUpperCase();
		
		if(findScDeptId == scDeptId) {
			goGridSearch(scDeptId, scDeptNm);
			findCnt++;
		}
		
		switch(levelId) {
			case 1:
				$ul = $("<ul class='scDeptChartLvl1'>").appendTo(".scDeptChart");
				$li = $("<li class='scDeptChart_" + scDeptId + "'>").appendTo($ul);
				htmlStr = "<div class='scDeptChartLvl1Layout'>	";
				htmlStr += "	<div>	";
				htmlStr += "		<label class='scDeptChartLvl1Lbl' title='" + scDeptNm + "'>" + scDeptNm + "</label>	";
				htmlStr += "		<span class='scDeptChartVLine1'></span>	";
				htmlStr += "	</div>	";
				htmlStr += "</div>	";
				$li.html(htmlStr);
				
				$li.find(".scDeptChartLvl1Lbl").css("background", "linear-gradient(#FFFFFF, " + signalMap[statusNm] + ")")
					.on("click", {"scDeptId":scDeptId, "scDeptNm":scDeptNm}, function(e) {
						goGridSearch(e.data.scDeptId, e.data.scDeptNm);
					});
				break;
			case 2:
				$ul = $("ul.scDeptChartLvl2");
				if($ul.length == 0) {
					$ul = $("<ul class='scDeptChartLvl2'>").appendTo(".scDeptChartLvl1Layout");
				}
				$li = $("<li class='scDeptChart_" + scDeptId + "'>").appendTo($ul);
				$div = $("<div>").appendTo($li);
				$div.append("<span class='scDeptChartHLine'>");
				$div.append("<span class='scDeptChartVLine2'>");
				$div.append("<label class='scDeptChartLvl2Lbl' title='" + scDeptNm + "'>" + scDeptNm + "</label>");
				
				$li.find(".scDeptChartLvl2Lbl").css("background", "linear-gradient(#FFFFFF, " + signalMap[statusNm] + ")")
					.on("click", {"scDeptId":scDeptId, "scDeptNm":scDeptNm}, function(e) {
						goGridSearch(e.data.scDeptId, e.data.scDeptNm);
					});
				break;
			default:
				if(levelId > 4) levelId = 4;
			
				$ul = $("li.scDeptChart_" + upScDeptId + " > ul");
				if($ul.length == 0) {
					$ul = $("<ul class='scDeptChartLvl" + levelId + "'>").appendTo("li.scDeptChart_" + upScDeptId);
				}
				
				$li = $("<li class='scDeptChart_" + scDeptId + "'>").appendTo($ul);
				$div = $("<div>").appendTo($li);
				$div.append("<div class='scDeptChartDot'>");
				$div.append("<label class='scDeptChartLvl" + levelId + "Lbl' title='" + scDeptNm + "'>" + scDeptNm + "</label>");
				
				$div.find(".scDeptChartDot").css("background", "linear-gradient(#FFFFFF, " + signalMap[statusNm] + ")");
				
				$div.find("label.scDeptChartLvl" + levelId + "Lbl")
					.on("click", {"scDeptId":scDeptId, "scDeptNm":scDeptNm}, function(e) {
						goGridSearch(e.data.scDeptId, e.data.scDeptNm);
					});
				
				break;
		}
	}
	
	correctPos();
	
	if(findCnt == 0 && scDeptList.length > 0) {
		goGridSearch(escapeHTML(removeNull(scDeptList[0].scDeptId)), escapeHTML(removeNull(scDeptList[0].scDeptNm)));
	}
}

// 조직도 위치, 간격 보정
function correctPos() {
	var lvl2Cnt = $(".scDeptChartLvl2 > li").length;
	$(".scDeptChartLvl2 > li").css("width", (Math.floor((100 / lvl2Cnt)*10000)/10000) + "%");
	$(".scDeptChartLvl2").css("max-width", "");
	$(".scDeptChartVLine1, .scDeptChartHLine").show();
	if(lvl2Cnt == 0) {
		$(".scDeptChartVLine1").hide();
	} else if(lvl2Cnt == 1) {
		$(".scDeptChartHLine").hide();
	} else if(lvl2Cnt < 6) {
		$(".scDeptChartLvl2").css("max-width", (lvl2Cnt * 300) + "px");
	}
}

function goGridSearch(scDeptId, scDeptNm) {
	$("#findScDeptId").val(scDeptId);
	$("#detailTitle1").text(scDeptNm);
	setEmptyChart();
	reloadGrid("list", "form");
	
	$("#detailDiv").show();
	gridResize("list");
}

function showChart(rowId) {
	$("#list tr#" + rowId).click();
	var rowData = $("#list").jqGrid("getRowData", rowId);
	
	$("#findMetricId").val(rowData.metricId);
	$("#detailTitle2").text(rowData.metricNm);
	
	sendAjax({
		"url" : "${context_path}/bsc/mon/org/scDeptDiag/selectChartData.do",
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
			<li class="ml20">
				<form:radiobuttons path="findAnalCycle" items="${codeUtil:getCodeList('138')}" itemLabel="codeNm" itemValue="codeId" />
			</li>
		</ul>
		<a href="#" class="btn-sch" onclick="loadScDeptChart();return false;"><spring:message code="button.search"/></a>
	</div>
	<div class="scDeptChart"></div>
	
	<div id="detailDiv" class="hx350 hide2">
		<div class="wp49 gridContainer fl hx350">
			<div class="ptitle" id="detailTitle1"></div>
			<table id="list"></table>
		</div>
		<div id="chart" class="wp49 fr hx350">
			<div class="ptitle_noLeftMag" id="detailTitle2"></div>
			<canvas id="canvas" class="wp95 hx270"></canvas>
		</div>
	</div>
</form:form>