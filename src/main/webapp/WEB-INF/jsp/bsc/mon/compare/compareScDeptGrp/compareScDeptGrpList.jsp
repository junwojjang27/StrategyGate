<!-- 
*************************************************************************
* CLASS 명	: CompareScDeptGrpList
* 작 업 자	: kimyh
* 작 업 일	: 2018-04-27
* 기	능	: 평가군별 비교 List
* ---------------------------- 변 경 이 력 --------------------------------
* 번호	작 업 자		작	업	일			변 경 내 용				비고
* ----	---------	----------------	---------------------	-----------
*	1	kimyh		2018-04-27				최 초 작 업
**************************************************************************
-->
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/common-taglibs.jsp"%>
<validator:javascript formName="compareScDeptGrpVO" staticJavascript="false" xhtml="true" cdata="false"/>
<script type="text/javascript">
var barChart1, barChart2, lineChart;
$(function(){
	$("#list").jqGrid({
		url			:	"${context_path}/bsc/mon/compare/compareScDeptGrp/compareScDeptGrpList_json.do",
		postData	:	getFormData("form"),
		width		:	"${jqgrid_width}",
		height		:	"${jqgrid_height2}",
		colModel	:	[
							{name:"scDeptId",	index:"scDeptId",	hidden:true},
							{name:"rnk",		index:"rnk",		width:30,	align:"center",	sortable:false,	label:"<spring:message code="word.rank"/>"},
							{name:"scDeptNm",	index:"scDeptNm",	width:100,	align:"left",	sortable:false,	label:"<spring:message code="word.scDeptNm"/>",
								formatter:function(cellvalue, options, rowObject) {
									return "<a href='#' onclick='showDetail(\"" + removeNull(options.rowId) + "\");return false;'>" + escapeHTML(removeNull(cellvalue)) + "</a>";
								},
								unformat:linkUnformatter
							},
							{name:"conversionScore",		index:"conversionScore",		width:50,	align:"right",	label:"<spring:message code="word.conversionScore"/>",
								formatter:"number",	formatoptions:{thousandsSeperator:",", decimalPlaces:DECIMAL_SCALE, defaultValue:0, removePointZeros:REMOVE_POINT_ZEROS}},
							
							{name:"mesWeight",		index:"mesWeight",		width:50,	align:"center",	label:"<spring:message code="word.weight"/>",
								formatter:"number",	formatoptions:{thousandsSeperator:",", decimalPlaces:DECIMAL_SCALE, defaultValue:"&#160;", removePointZeros:REMOVE_POINT_ZEROS}},
							{name:"mesWeightScore",	index:"mesWeightScore",	width:50,	align:"right",	label:"<spring:message code="word.weightScore"/>",
								formatter:"number",	formatoptions:{thousandsSeperator:",", decimalPlaces:DECIMAL_SCALE, defaultValue:"&#160;", removePointZeros:REMOVE_POINT_ZEROS}},
							{name:"mesScoreRate",	index:"mesScoreRate",	width:50,	align:"right",	label:"<spring:message code="word.achievementRate"/>",
								formatter:"number",	formatoptions:{thousandsSeperator:",", decimalPlaces:DECIMAL_SCALE, defaultValue:"&#160;", removePointZeros:REMOVE_POINT_ZEROS}},
							
							{name:"nonMesWeight",		index:"nonMesWeight",		width:50,	align:"center",	label:"<spring:message code="word.weight"/>",
								formatter:"number",	formatoptions:{thousandsSeperator:",", decimalPlaces:DECIMAL_SCALE, defaultValue:"&#160;", removePointZeros:REMOVE_POINT_ZEROS}},
							{name:"nonMesWeightScore",	index:"nonMesWeightScore",	width:50,	align:"right",	label:"<spring:message code="word.weightScore"/>",
								formatter:"number",	formatoptions:{thousandsSeperator:",", decimalPlaces:DECIMAL_SCALE, defaultValue:"&#160;", removePointZeros:REMOVE_POINT_ZEROS}},
							{name:"nonMesScoreRate",	index:"nonMesScoreRate",	width:50,	align:"right",	label:"<spring:message code="word.achievementRate"/>",
								formatter:"number",	formatoptions:{thousandsSeperator:",", decimalPlaces:DECIMAL_SCALE, defaultValue:"&#160;", removePointZeros:REMOVE_POINT_ZEROS}},
							
							{name:"p1Weight",		index:"p1Weight",		width:50,	align:"center",	label:"<spring:message code="word.weight"/>",
								formatter:"number",	formatoptions:{thousandsSeperator:",", decimalPlaces:DECIMAL_SCALE, defaultValue:"&#160;", removePointZeros:REMOVE_POINT_ZEROS}},
							{name:"p1WeightScore",	index:"p1WeightScore",	width:50,	align:"right",	label:"<spring:message code="word.weightScore"/>",
								formatter:"number",	formatoptions:{thousandsSeperator:",", decimalPlaces:DECIMAL_SCALE, defaultValue:"&#160;", removePointZeros:REMOVE_POINT_ZEROS}},
							{name:"p1ScoreRate",	index:"p1ScoreRate",	width:50,	align:"right",	label:"<spring:message code="word.achievementRate"/>",
								formatter:"number",	formatoptions:{thousandsSeperator:",", decimalPlaces:DECIMAL_SCALE, defaultValue:"&#160;", removePointZeros:REMOVE_POINT_ZEROS}},
							
							{name:"p2Weight",		index:"p2Weight",		width:50,	align:"center",	label:"<spring:message code="word.weight"/>",
								formatter:"number",	formatoptions:{thousandsSeperator:",", decimalPlaces:DECIMAL_SCALE, defaultValue:"&#160;", removePointZeros:REMOVE_POINT_ZEROS}},
							{name:"p2WeightScore",	index:"p2WeightScore",	width:50,	align:"right",	label:"<spring:message code="word.weightScore"/>",
								formatter:"number",	formatoptions:{thousandsSeperator:",", decimalPlaces:DECIMAL_SCALE, defaultValue:"&#160;", removePointZeros:REMOVE_POINT_ZEROS}},
							{name:"p2ScoreRate",	index:"p2ScoreRate",	width:50,	align:"right",	label:"<spring:message code="word.achievementRate"/>",
								formatter:"number",	formatoptions:{thousandsSeperator:",", decimalPlaces:DECIMAL_SCALE, defaultValue:"&#160;", removePointZeros:REMOVE_POINT_ZEROS}}
						],
		rowNum		: ${jqgrid_rownum_max},
		loadComplete : function() {
			var findScDeptId = $("#findScDeptId").val();
			var dataCnt = 0;
			if($(this).jqGrid("getGridParam", "records") == 0) {
				$("#detailDiv").hide();
			} else {
				if(isEmpty(findScDeptId)) {
					showDetail(1);
				} else {
					$($(this).jqGrid("getRowData")).each(function(i, e) {
						if(findScDeptId == e.scDeptId) {
							showDetail(i+1);
							dataCnt++;
							return false;
						}
					});

					if(dataCnt == 0) {
						showDetail(1);
					}
				}
			}
		}
	});
	
	$("#list").jqGrid("setGroupHeaders", {
		useColSpanStyle : true,
		groupHeaders	: [
			{startColumnName : "mesWeight", numberOfColumns : 3, titleText : "<spring:message code="word.meas"/>"},
			{startColumnName : "nonMesWeight", numberOfColumns : 3, titleText : "<spring:message code="word.nonMeas"/>"},
			{startColumnName : "p1Weight", numberOfColumns : 3, titleText : "<spring:message code="word.common"/>"},
			{startColumnName : "p2Weight", numberOfColumns : 3, titleText : "<spring:message code="word.unique"/>"}
		]
	});
	
	var barChart1Options = chartJsCustom.noValueChartOptions.bar;
	barChart1Options.scales.yAxes = [{ticks:{maxTicksLimit : 5}}];
	barChart1 = new Chart($("#canvas1")[0].getContext("2d"), {
		type: "bar",
		data: {
				labels: ["<spring:message code="word.analysis8"/>", "<spring:message code="word.analysis4"/>", "<spring:message code="word.analysis6"/>"],
				datasets: [{
					backgroundColor: chartJsCustom.getBGColors(3),
					borderColor: chartJsCustom.getColors(3),
					borderWidth: 1,
					label: "<spring:message code="word.score"/>",		
					data: []
				}]
			},
		options: barChart1Options
	});
	
	barChart2 = new Chart($("#canvas2")[0].getContext("2d"), {
		type: "bar",
		data: {
				labels: ["<spring:message code="word.analysis1"/>", "<spring:message code="word.analysis7"/>", "<spring:message code="word.analysis3"/>", "<spring:message code="word.analysis5"/>"],
				datasets: [{
					backgroundColor: chartJsCustom.getBGColors(4),
					borderColor: chartJsCustom.getColors(4),
					borderWidth: 1,
					label: "<spring:message code="word.score"/>",
					data: []
				}]
			},
		options: chartJsCustom.noValueChartOptions.bar
	});
	
	lineChart =  new Chart($("#canvas3")[0].getContext("2d"), {
		type: "line",
		data: {
				labels : [
					"<spring:message code="unit.month01"/>",	"<spring:message code="unit.month02"/>",	"<spring:message code="unit.month03"/>",
					"<spring:message code="unit.month04"/>",	"<spring:message code="unit.month05"/>",	"<spring:message code="unit.month06"/>",
					"<spring:message code="unit.month07"/>",	"<spring:message code="unit.month08"/>",	"<spring:message code="unit.month09"/>",
					"<spring:message code="unit.month010"/>",	"<spring:message code="unit.month011"/>",	"<spring:message code="unit.month012"/>"
				],
				datasets : [{
					label: "<spring:message code="word.analysis11"/>",
					data: [],
					borderColor: chartJsCustom.getBGColor(0),
					backgroundColor: chartJsCustom.getColor(0),
					fill: false,
					lineTension: 0,
					pointRadius:5,
					pointStyle:"star"
				},{
					label: "<spring:message code="word.analysis12"/>",
					data: [],
					borderColor: chartJsCustom.getBGColor(1),
					backgroundColor: chartJsCustom.getColor(1),
					fill: false,
					lineTension: 0,
					pointRadius:5,
					pointStyle:"triangle"
				},{
					label: "<spring:message code="word.analysis3"/>",
					data: [],
					borderColor: chartJsCustom.getBGColor(2),
					backgroundColor: chartJsCustom.getColor(2),
					fill: false,
					lineTension: 0,
					pointRadius:5,
					pointStyle:"rect"
				}]
			},
		options: chartJsCustom.noValueChartOptions.line
	});
	
	// 연도 변경시 평가군 새로 조회
	$("#findYear").on("change", function() {
		getEvalGrpList();
	});
	
	$("#detailTitle1Btn").click(function(){
		if($(this).hasClass("expand_close_btn")){
			$(this).removeClass("expand_close_btn").addClass("expand_btn");
			$("#detailTitle1Div").slideUp();
		}else{
			$(this).removeClass("expand_btn").addClass("expand_close_btn");
			$("#detailTitle1Div").slideDown();
		}
	});
	
	$("#detailTitle2Btn").click(function(){
		if($(this).hasClass("expand_close_btn")){
			$(this).removeClass("expand_close_btn").addClass("expand_btn");
			$("#detailTitle2Div").slideUp();
		}else{
			$(this).removeClass("expand_btn").addClass("expand_close_btn");
			$("#detailTitle2Div").slideDown();
		}
	});
	
	$("#detailTitle3Btn").click(function(){
		if($(this).hasClass("expand_close_btn")){
			$(this).removeClass("expand_close_btn").addClass("expand_btn");
			$("#detailTitle3Div").slideUp();
		}else{
			$(this).removeClass("expand_btn").addClass("expand_close_btn");
			$("#detailTitle3Div").slideDown();
		}
	});
});

// 평가군 목록 조회
function getEvalGrpList() {
	sendAjax({
		"url" : "${context_path}/common/codeList_json.do",
		"data" : {
			"codeGrpId" : "003",
			"findYear"	: $("#findYear").val(),
			"_csrf"		: getCsrf()
		},
		"doneCallbackFunc" : function(data) {
			$("#findEvalGrpId option").remove();
			if(isNotEmpty(data.list)) {
				var list = data.list;
				for(var i in list) {
					$("<option value='" + list[i].codeId + "'>" + list[i].codeNm + "</option>").appendTo($("#findEvalGrpId"));
				}
			}
		}
	});
}

// 상세 조회
function showDetail(rowId) {
	clickGridRowByRowId("list",rowId);
	
	var rowData = $("#list").jqGrid("getRowData", rowId);
	$("#detailTitle1").text("<spring:message code="bsc.mon.compare.compareScDeptGrp.title1"/> : " + rowData.scDeptNm);
	
	var f = document.form;
	f.findScDeptId.value = rowData.scDeptId;
	sendAjax({
		"url" : "${context_path}/bsc/mon/compare/compareScDeptGrp/compareScDeptGrpDetail.do",
		"data" : getFormData("form"),
		"doneCallbackFunc" : "setDetail"
	});
}

function setDetail(data) {
	var analysis = data.scoreAnalysis;
	
	var deptScore 		= removePointZeros(parseFloat(analysis.deptScore).toFixed(DECIMAL_SCALE));
	var stdScore 		= removePointZeros(parseFloat(analysis.stdScore).toFixed(DECIMAL_SCALE));
	var avgScore 		= removePointZeros(parseFloat(analysis.avgScore).toFixed(DECIMAL_SCALE));
	var deptAvgScore 	= removePointZeros(parseFloat(analysis.deptAvgScore).toFixed(DECIMAL_SCALE));
	var maxScore 		= removePointZeros(parseFloat(analysis.maxScore).toFixed(DECIMAL_SCALE));
	var deptMaxScore 	= removePointZeros(parseFloat(analysis.deptMaxScore).toFixed(DECIMAL_SCALE));
	var minScore		= removePointZeros(parseFloat(analysis.minScore).toFixed(DECIMAL_SCALE));
	var deptMinScore 	= removePointZeros(parseFloat(analysis.deptMinScore).toFixed(DECIMAL_SCALE));
	
	$("#detailTable1, #detailTable3").find(".analysis1 label").text(deptScore);
	$("#detailTable1, #detailTable3").find(".analysis2 label").text(stdScore);
	$("#detailTable1, #detailTable3").find(".analysis3 label").text(avgScore);
	$("#detailTable1, #detailTable3").find(".analysis4 label").text(deptAvgScore);
	$("#detailTable1, #detailTable3").find(".analysis5 label").text(maxScore);
	$("#detailTable1, #detailTable3").find(".analysis6 label").text(deptMaxScore);
	$("#detailTable1, #detailTable3").find(".analysis7 label").text(minScore);
	$("#detailTable1, #detailTable3").find(".analysis8 label").text(deptMinScore);

	barChart1.data.datasets[0].data = [deptMinScore, deptAvgScore, deptMaxScore];
	barChart1.update();                                            
	
	barChart2.data.datasets[0].data = [deptScore, minScore, avgScore, maxScore];
	barChart2.update();
	
	$(data.monthlyTrend).each(function(i, e) {
		lineChart.data.datasets[i].data = [
			removePointZeros(parseFloat(e.mon01).toFixed(DECIMAL_SCALE)),	
			removePointZeros(parseFloat(e.mon02).toFixed(DECIMAL_SCALE)),	
			removePointZeros(parseFloat(e.mon03).toFixed(DECIMAL_SCALE)),
			removePointZeros(parseFloat(e.mon04).toFixed(DECIMAL_SCALE)),
			removePointZeros(parseFloat(e.mon05).toFixed(DECIMAL_SCALE)),
			removePointZeros(parseFloat(e.mon06).toFixed(DECIMAL_SCALE)),
			removePointZeros(parseFloat(e.mon07).toFixed(DECIMAL_SCALE)),
			removePointZeros(parseFloat(e.mon08).toFixed(DECIMAL_SCALE)),
			removePointZeros(parseFloat(e.mon09).toFixed(DECIMAL_SCALE)),
			removePointZeros(parseFloat(e.mon10).toFixed(DECIMAL_SCALE)),
			removePointZeros(parseFloat(e.mon11).toFixed(DECIMAL_SCALE)),
			removePointZeros(parseFloat(e.mon12).toFixed(DECIMAL_SCALE))
		];
	});
	lineChart.update();
	
	var performance, htmlStr, type;
	$("#detailTable3 tbody tr:not(#totalTr)").remove();
	$(data.performanceList).each(function(i, e) {
		performance = e;
		
		switch(performance.type) {
			case "TYPE1":
				type = "<spring:message code="word.meas"/>";
				break;
			case "TYPE2":
				type = "<spring:message code="word.nonMeas"/>";
				break;
			case "PROPERTY1":
				type = "<spring:message code="word.common"/>";
				break;
			case "PROPERTY2":
				type = "<spring:message code="word.unique"/>";
				break;
			default:
				type = "";
				break;
		}
		
		deptScore = removePointZeros(parseFloat(performance.deptScore).toFixed(DECIMAL_SCALE));       
		stdScore = removePointZeros(parseFloat(performance.stdScore).toFixed(DECIMAL_SCALE));         
		avgScore = removePointZeros(parseFloat(performance.avgScore).toFixed(DECIMAL_SCALE));         
		deptAvgScore = removePointZeros(parseFloat(performance.deptAvgScore).toFixed(DECIMAL_SCALE)); 
		maxScore = removePointZeros(parseFloat(performance.maxScore).toFixed(DECIMAL_SCALE));         
		deptMaxScore = removePointZeros(parseFloat(performance.deptMaxScore).toFixed(DECIMAL_SCALE)); 
		minScore = removePointZeros(parseFloat(performance.minScore).toFixed(DECIMAL_SCALE));         
		deptMinScore = removePointZeros(parseFloat(performance.deptMinScore).toFixed(DECIMAL_SCALE)); 
		
		htmlStr = "";
		htmlStr += "<td>" + type + "</td>";
		htmlStr += "<td class='txt-r'><label>" + deptScore    + "</label></td>";
		htmlStr += "<td class='txt-r'><label>" + stdScore     + "</label></td>";
		htmlStr += "<td class='txt-r'><label>" + avgScore     + "</label></td>";
		htmlStr += "<td class='txt-r'><label>" + deptAvgScore + "</label></td>";
		htmlStr += "<td class='txt-r'><label>" + maxScore     + "</label></td>";
		htmlStr += "<td class='txt-r'><label>" + deptMaxScore + "</label></td>";
		htmlStr += "<td class='txt-r'><label>" + minScore     + "</label></td>";
		htmlStr += "<td class='txt-r'><label>" + deptMinScore + "</label></td>";
		
		$("<tr>").html(htmlStr).insertBefore("#totalTr");
	});
	
	$("#detailDiv").show();
}

// 목록 조회
function searchList() {
 	reloadGrid("list", "form");
}
</script>

<form:form commandName="searchVO" id="form" name="form" method="post">
	<form:hidden path="findScDeptId"/>
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
				<label for="findEvalGrpId"><spring:message code="word.evalGrp"/></label>
				<form:select path="findEvalGrpId" class="select">
					<form:options items="${evalGrpList}" itemLabel="codeNm" itemValue="codeId"/>
				</form:select>
			</li>
			<li class="ml20">
				<form:radiobuttons path="findAnalCycle" items="${codeUtil:getCodeList('138')}" itemLabel="codeNm" itemValue="codeId" />
			</li>
		</ul>
		<a href="#" class="btn-sch" onclick="searchList();return false;"><spring:message code="button.search"/></a>
	</div>
	<div class="btn-dw">
	</div>
	<div class="gridContainer">
		<table id="list"></table>
	</div>

	<div id="detailDiv" class="hide2">
		<div class="ptitle3">
			<div class="txt" id="detailTitle1"></div>
			<div id="detailTitle1Btn" class="expand_close_btn"></div>
		</div>
		<div  id="detailTitle1Div">
			<div class="tbl-type01">
				<table id="detailTable1">
					<colgroup>
						<col width="12.5%"/>
						<col width="12.5%"/>
						<col width="12.5%"/>
						<col width="12.5%"/>
						<col width="12.5%"/>
						<col width="12.5%"/>
						<col width="12.5%"/>
						<col width="12.5%"/>
					</colgroup>
					<thead>
						<tr>
							<th><spring:message code="word.analysis1"/></th>
							<th><spring:message code="word.analysis2"/></th>
							<th><spring:message code="word.analysis3"/></th>
							<th><spring:message code="word.analysis4"/></th>
							<th><spring:message code="word.analysis5"/></th>
							<th><spring:message code="word.analysis6"/></th>
							<th><spring:message code="word.analysis7"/></th>
							<th><spring:message code="word.analysis8"/></th>
						</tr>
					</thead>			
					<tbody>
						<tr>
							<td class="analysis1 txt-r"><label></label></td>
							<td class="analysis2 txt-r"><label></label></td>
							<td class="analysis3 txt-r"><label></label></td>
							<td class="analysis4 txt-r"><label></label></td>
							<td class="analysis5 txt-r"><label></label></td>
							<td class="analysis6 txt-r"><label></label></td>
							<td class="analysis7 txt-r"><label></label></td>
							<td class="analysis8 txt-r"><label></label></td>
						</tr>
					</tbody>
				</table>
			</div>
			<div class="m20 hx200">
				<div id="barChart1" class="wp50 hx200 fl">
					<canvas id="canvas1"></canvas>
				</div>
				<div id="barChart2" class="wp50 hx200 fr">		
					<canvas id="canvas2"></canvas>
				</div>
			</div>
		</div>
		
		<div class="ptitle3">
			<div class="txt" id="detailTitle2"><spring:message code="bsc.mon.compare.compareScDeptGrp.title2"/></div>
			<div id="detailTitle2Btn" class="expand_close_btn"></div>
		</div>
		
		<div id="detailTitle2Div">
			<div class="m20 hx200">
				<div id="lineChart" class="chart100 hx200">
					<canvas id="canvas3"></canvas>
				</div>
			</div>
		</div>

		<div class="ptitle3">
			<div class="txt" id="detailTitle3"><spring:message code="bsc.mon.compare.compareScDeptGrp.title3"/></div>
			<div id="detailTitle3Btn" class="expand_close_btn"></div>
		</div>
		<div class="tbl-type01" id="detailTitle3Div">
			<table id="detailTable3">
				<colgroup>
					<col width="12%"/>
					<col width="11%"/>
					<col width="11%"/>
					<col width="11%"/>
					<col width="11%"/>
					<col width="11%"/>
					<col width="11%"/>
					<col width="11%"/>
					<col width="11%"/>
				</colgroup>
				<thead>
					<tr>
						<th><spring:message code="word.metricDivision"/></th>
						<th><spring:message code="word.analysis1"/></th>
						<th><spring:message code="word.analysis2"/></th>
						<th><spring:message code="word.analysis3"/></th>
						<th><spring:message code="word.analysis4"/></th>
						<th><spring:message code="word.analysis5"/></th>
						<th><spring:message code="word.analysis6"/></th>
						<th><spring:message code="word.analysis7"/></th>
						<th><spring:message code="word.analysis8"/></th>
					</tr>
				</thead>			
				<tbody>
					<tr id="totalTr">
						<td><label><spring:message code="word.all"/></label></td>
						<td class="analysis1 txt-r"><label></label></td>
						<td class="analysis2 txt-r"><label></label></td>
						<td class="analysis3 txt-r"><label></label></td>
						<td class="analysis4 txt-r"><label></label></td>
						<td class="analysis5 txt-r"><label></label></td>
						<td class="analysis6 txt-r"><label></label></td>
						<td class="analysis7 txt-r"><label></label></td>
						<td class="analysis8 txt-r"><label></label></td>
					</tr>
				</tbody>
			</table>
		</div>
		
		<div class="tbl-bottom">
			<div class="tbl-btn">
			</div>
		</div>
	</div>
</form:form>
