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
	$("#list").jqGrid({
		url			:	"${context_path}/bsc/mon/divOutput/scDeptDetail/scDeptDetailList_json.do",
		postData	:	getFormData("form"),
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
			
			if($("#list").jqGrid("getGridParam","records") > 0){
				
				$("#actChartArea").show();
				$("#actArea").show();
				$("#initiativeArea").show();
				
				var rowObj = $("#list").jqGrid("getRowData",1);
				$("#form #metricId").val(rowObj.metricId);
				$("#form #metricNm").val(rowObj.metricNm);
				$("#form #typeId").val(rowObj.typeId);
				
				showDetail(rowObj.metricId,rowObj.metricNm,rowObj.typeId,1);
				
			}else{
				$("#actChartArea").hide();
				$("#actArea").hide();
				$("#initiativeArea").hide();
			}
		}
	});
	
	var barChart1Options = chartJsCustom.noValueChartOptions.bar;
	barChart1Options.scales = {yAxes: [{
							          id: 'barY',
							          type: 'linear',
							          position: 'left',
							          display: false,
							          ticks: {maxTicksLimit : 5}
							        }, {
							          id: 'lineY',
							          type: 'linear',
							          position: 'right',
							          display: false,
							          ticks: {max: 120,min: 0}
							        }]
							      }
	barChart = new Chart($("#canvas1")[0].getContext("2d"), {
		type: "bar",
		data: {
				labels: ["<spring:message code="unit.month01"/>", "<spring:message code="unit.month02"/>", "<spring:message code="unit.month03"/>", "<spring:message code="unit.month04"/>", 
						 "<spring:message code="unit.month05"/>", "<spring:message code="unit.month06"/>", "<spring:message code="unit.month07"/>", "<spring:message code="unit.month08"/>",
						 "<spring:message code="unit.month09"/>", "<spring:message code="unit.month10"/>", "<spring:message code="unit.month11"/>", "<spring:message code="unit.month12"/>"
					    ],
				datasets: [
				{
					type: 'line',					
					yAxisID : 'lineY',
					label: "<spring:message code="word.score"/>",
					data: [],
					borderColor: "rgb(54, 162, 235)",
					backgroundColor:  "rgb(54, 162, 235)",
					lineTension: 0,
					pointRadius:5,
					fill: false,
					pointStyle:"rect"
				},
				{
					type: 'bar',		
					yAxisID : 'barY',
					label: "<spring:message code="word.target"/>",
					backgroundColor: "rgb(75, 192, 192)",
					borderColor: "rgb(75, 192, 192)",
					borderWidth: 1,
					data: []
				},
				{
					type: 'bar',
					yAxisID : 'barY',
					label: "<spring:message code="word.actual"/>",
					backgroundColor: "rgb(255, 99, 132)",
					borderColor: "rgb(255, 99, 132)",
					borderWidth: 1,
					data: []
				}]
			},
		options: barChart1Options
	});
	
	$("#monthlyActualStatusBtn").click(function(){
		if($(this).hasClass("expand_close_btn")){
			$(this).removeClass("expand_close_btn").addClass("expand_btn");
			$("#monthlyActualStatusDiv").slideUp();
		}else{
			$(this).removeClass("expand_btn").addClass("expand_close_btn");
			$("#monthlyActualStatusDiv").slideDown();
		}
	});
	
	$("#monthlyActualDetailBtn").click(function(){
		if($(this).hasClass("expand_close_btn")){
			$(this).removeClass("expand_close_btn").addClass("expand_btn");
			$("#monthlyActualDetailDiv").slideUp();
		}else{
			$(this).removeClass("expand_btn").addClass("expand_close_btn");
			$("#monthlyActualDetailDiv").slideDown();
		}
	});
	
	$("#actionPlanActualBtn").click(function(){
		if($(this).hasClass("expand_close_btn")){
			$(this).removeClass("expand_close_btn").addClass("expand_btn");
			$("#actionPlanActualDiv").slideUp();
		}else{
			$(this).removeClass("expand_btn").addClass("expand_close_btn");
			$("#actionPlanActualDiv").slideDown();
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
		postData	:	getFormData("form"),
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
			var rowCnt = jQuery("#actList").jqGrid('getGridParam', 'records');
			if(0<rowCnt){
				var ot = $("#actList .jqgrow").find("td[aria-describedby='actList_mon"+$("#findMon").val()+"']").offset().left;
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
		"data" : getFormData("form")
	});
}

function popMetricLink(calTypeCol){
	$("#calTypeCol").val(calTypeCol);
	$("#form #hisMetricId").val($("#form #metricId").val());
	openFancybox({
		"url" : "${context_path}/bsc/mon/divOutput/scDeptDetail/popMetricLinkList.do",
		"data" : getFormData("form")
	});
}

//실행계획 목록 조회
function getInitiativeList() {
	sendAjax({
		"url" : "${context_path}/bsc/mon/divOutput/scDeptDetail/initiativeList_json.do",
		"data" : getFormData("form"),
		"doneCallbackFunc" : "setInitiativeList"
	});
}

// 실행계획 목록 표시
function setInitiativeList(data) {
	$("#initiativeList").find("tr:not(#initiativeTemplate)").remove();
	var $tr;
	if(isEmpty(data.rows) || data.rows.length == 0) {
		$("<tr><td class='txt-c' colspan='" + $("#initiativeTemplate").children("td").length + "'><label><spring:message code="errors.noContent"/></label></td></tr>").appendTo("#initiativeList");
	} else {
		for(var i in data.rows) {
			$tr = $("#initiativeTemplate").clone().removeAttr("id").removeClass("hide").appendTo("#initiativeList");
			
			if(data.rows[i].upInitiativeId != ""){
				
				$tr.find(".initiativeNm label").addClass("link").text("└"+escapeHTML(data.rows[i].initiativeNm));
				$tr.find(".initiativeNm a").removeAttr("onclick").attr("onclick", "popInitiativeActDetail('" + data.rows[i].initiativeId + "');return false;");
				$tr.find(".progressRate").text(data.rows[i].progressRate);
				for(var j=parseInt(data.rows[i].startMon, 10), jLen=parseInt(data.rows[i].endMon, 10); j<=jLen; j++) {
					$tr.find(".initiativeMon" + j).addClass("checkAct");
				}
			}else{
				
				$tr.find(".initiativeNm label").addClass("link").text(escapeHTML(data.rows[i].initiativeNm));
				$tr.find(".initiativeNm a").removeAttr("onclick").attr("onclick", "popInitiativeDetail('" + data.rows[i].initiativeId + "');return false;");
				$tr.find(".progressRate").append("<progress id='progressBar'></progress>");
				$tr.find("#progressBar").attr({"max":100,"value":data.rows[i].progressRateSum});
				for(var j=parseInt(data.rows[i].startMon, 10), jLen=parseInt(data.rows[i].endMon, 10); j<=jLen; j++) {
					$tr.find(".initiativeMon" + j).addClass("check");
				}
			}
		}
	}
}

/******************************************
 * chart 당월/누적 Ajax
 ******************************************/
function getActualChart(){
	sendAjax({
		"url" : "${context_path}/bsc/mon/divOutput/scDeptDetail/actualDetailChart_json.do",
		"data" : getFormData("form"),
		"doneCallbackFunc" : "setActualChart"
	});
	
}

function setActualChart(data){
	if(data != undefined, data != ''){
		var target = data.target;
		var actual = data.actual;
		var score = data.score;
		
		barChart.data.datasets[1].data = [
			target.mon01,	target.mon02,	target.mon03,	target.mon04,
			target.mon05,	target.mon06,	target.mon07,	target.mon08,
			target.mon09,	target.mon10,	target.mon11,	target.mon12
		];
		
		barChart.data.datasets[2].data = [
			actual.mon01,	actual.mon02,	actual.mon03,	actual.mon04,
			actual.mon05,	actual.mon06,	actual.mon07,	actual.mon08,
			actual.mon09,	actual.mon10,	actual.mon11,	actual.mon12
		];
		
		barChart.data.datasets[0].data = [
			score.mon01,	score.mon02,	score.mon03,	score.mon04,
			score.mon05,	score.mon06,	score.mon07,	score.mon08,
			score.mon09,	score.mon10,	score.mon11,	score.mon12
		];
		
		barChart.update();
	}
}

//실행실적 조회, 수정
function popInitiativeActDetail(initiativeId) {
	document.form.initiativeId.value = initiativeId;
	openFancybox({
		"url" : "${context_path}/bsc/mon/divOutput/scDeptDetail/popInitiativeActDetail.do",
		"data" : getFormData("form")
	});
}  

//실행계획 조회, 수정
function popInitiativeDetail(initiativeId) {
	document.form.initiativeId.value = initiativeId;
	openFancybox({
		"url" : "${context_path}/bsc/mon/divOutput/scDeptDetail/popInitiativeDetail.do",
		"data" : getFormData("form")
	});
}

function showDetail(metricId,metricNm,typeId,rowId){
	
	clickGridRowByRowId("list",rowId);
	
	$("#form #metricId").val(metricId);
	$("#form #typeId").val(typeId);
	
	if(typeId == '02'){
		$("#actChartArea").hide();
		$("#actArea").hide();
		$("#initiativeArea").show();
		//reloadGrid("actList", "form");
		//getActual();
		getInitiativeList();
		
		$("#titleInitiative").empty();
		$("#titleInitiative").text(metricNm+" <spring:message code="word.actionPlanActual"/>");
		
	}else{
		$("#actArea").show();
		$("#actChartArea").show();
		$("#initiativeArea").show();
	 	//reloadGrid("actList", "form");
	 	getActualChart();
	 	getActual();
	 	getInitiativeList();
	 	
	 	$("#titleActual").empty();
		$("#titleInitiative").empty();
		$("#titleActualChart").text(metricNm+" <spring:message code="word.monthlyActualStatus" />");
		$("#titleActual").text(metricNm+" <spring:message code="word.monthlyActualDetail" />");
		$("#titleInitiative").text(metricNm+" <spring:message code="word.actionPlanActual"/>");
	 	
	}
}

// 목록 조회
function searchList() {
	reloadGrid("list", "form");
}
</script>

<form:form commandName="searchVO" id="form" name="form" method="post">
 	<form:hidden path="year"/>
	<form:hidden path="mon"/>
	<form:hidden path="analCycle"/>
	<form:hidden path="calTypeCol"/>
	<form:hidden path="metricId"/>
	<form:hidden path="hisMetricId"/>
	<form:hidden path="typeId"/>
	<form:hidden path="initiativeId"/>
	<form:hidden path="findScDeptId"/>

	<div class="sch-bx">
		<ul>
			<li>
				<label for="findYear"><spring:message code="word.baseYearMon"/></label>
				<form:select path="findYear" class="select wx100" items="${codeUtil:getCodeList('017')}" itemLabel="codeNm" itemValue="codeId">
				</form:select>
				<form:select path="findMon" class="select wx80" items="${codeUtil:getCodeList('024')}" itemLabel="codeNm" itemValue="codeId">
				</form:select>
			</li>
			<li>
				<div class="sch-oga" id="findScDeptList"></div>
			</li>
			<li>
				<label for="findAnalCycle"><spring:message code="word.analysisCycle"/></label>
				<form:radiobuttons path="findAnalCycle" items="${codeUtil:getCodeList('138')}" itemLabel="codeNm" itemValue="codeId" />
			</li>
		</ul>
		<a href="#" class="btn-sch" onclick="searchList();return false;"><spring:message code="button.search"/></a>
	</div>
	<div class="btn-dw">
	</div>
	<div class="gridContainer">
		<table id="list"></table>
		<div id="pager"></div>
	</div>
	
	<div id="actChartArea">
		<div class="ptitle3">
			<div class="txt" id="titleActualChart"><c:out value="${searchVO.metricNm}" /> <spring:message code="word.monthlyActualStatus" /></div>
			<div id="monthlyActualStatusBtn" class="expand_close_btn"></div>
		</div>
		
		<div id="monthlyActualStatusDiv">
			<div id="actDetailChart" class="m20 hx200">
				<div id="lineChart" class="hx200 chart100">
					<canvas id="canvas1"></canvas>
				</div>
			</div>
		</div>
	</div>
	
	<div id="actArea">
		<div class="ptitle3">
			<div class="txt" id="titleActual"><c:out value="${searchVO.metricNm}" /> <spring:message code="word.monthlyActualDetail" /></div>
			<div id="monthlyActualDetailBtn" class="expand_close_btn"></div>
		</div>
		<div class="gridContainer" id="monthlyActualDetailDiv">
			<table id="actList"></table>
			<div id="pager"></div>
		</div>
	</div>
	
	<div id="initiativeArea">
		<div class="ptitle3">
			<div class="txt" id="titleInitiative"><c:out value="${searchVO.metricNm}" /> <spring:message code="word.actionPlanActual" /></div>
			<div id="actionPlanActualBtn" class="expand_close_btn"></div>
		</div>
		<div class="tbl-type01" id="actionPlanActualDiv">
			<table>
				<colgroup>
					<col width="20%"/>
					<col width="10%"/>
					<col width="5%"/>
					<col width="5%"/>
					<col width="5%"/>
					<col width="5%"/>
					<col width="5%"/>
					<col width="5%"/>
					<col width="5%"/>
					<col width="5%"/>
					<col width="5%"/>
					<col width="5%"/>
					<col width="5%"/>
					<col width="5%"/>
				</colgroup>
				<thead>
					<tr>
						<th scope="col"><spring:message code="word.initiative"/></th>
						<th scope="col"><spring:message code="word.progressRate"/></th>
						<th scope="col"><spring:message code="unit.month1"/></th>
						<th scope="col"><spring:message code="unit.month2"/></th>
						<th scope="col"><spring:message code="unit.month3"/></th>
						<th scope="col"><spring:message code="unit.month4"/></th>
						<th scope="col"><spring:message code="unit.month5"/></th>
						<th scope="col"><spring:message code="unit.month6"/></th>
						<th scope="col"><spring:message code="unit.month7"/></th>
						<th scope="col"><spring:message code="unit.month8"/></th>
						<th scope="col"><spring:message code="unit.month9"/></th>
						<th scope="col"><spring:message code="unit.month10"/></th>
						<th scope="col"><spring:message code="unit.month11"/></th>
						<th scope="col"><spring:message code="unit.month12"/></th>
					</tr>
				</thead>
				<tbody id="initiativeList">
					<tr id="initiativeTemplate" class="hide">
						<td class="initiativeNm" style="text-align:left;"><a href="#" onclick="return false;"><label></label></a><span id="addAct"></span></td>
						<td class="progressRate"></td>
						<td class="initiativeMon1"></td>
						<td class="initiativeMon2"></td>
						<td class="initiativeMon3"></td>
						<td class="initiativeMon4"></td>
						<td class="initiativeMon5"></td>
						<td class="initiativeMon6"></td>
						<td class="initiativeMon7"></td>
						<td class="initiativeMon8"></td>
						<td class="initiativeMon9"></td>
						<td class="initiativeMon10"></td>
						<td class="initiativeMon11"></td>
						<td class="initiativeMon12"></td>
					</tr>
				</tbody>
			</table>
		</div>
	</div>
	
</form:form>

