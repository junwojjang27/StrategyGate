<!-- 
*************************************************************************
* CLASS 명	: ScDeptSummaryList
* 작 업 자	: 현걸욱
* 작 업 일	: 2018-04-24
* 기	능	: 조직성과요약 List
* ---------------------------- 변 경 이 력 --------------------------------
* 번호	작 업 자		작	업	일			변 경 내 용				비고
* ----	---------	----------------	---------------------	-----------
*	1	현걸욱		2018-04-24				최 초 작 업
**************************************************************************
-->
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/common-taglibs.jsp"%>

<script type="text/javascript">
$(function(){
	var barChart1Options = chartJsCustom.noValueChartOptions.bar;
	//barChart1Options.scales.yAxes = [ticks:{maxTicksLimit : 5}];
	barChart1 = new Chart($("#canvas1")[0].getContext("2d"), {
		type: "bar",
		data: {
				labels: ["<spring:message code="word.meas"/>", "<spring:message code="word.nonMeas"/>", "<spring:message code="word.all"/>"
					    ],
				datasets: [
				{
					label: "<spring:message code="word.weight"/>",
					type: 'bar',		
					backgroundColor: "rgb(75, 192, 192)",
					borderColor: "rgb(75, 192, 192)",
					borderWidth: 1,
					data: []
				},
				{
					label: "<spring:message code="word.weightScore"/>",
					type: 'bar',
					backgroundColor: "rgb(255, 99, 132)",
					borderColor: "rgb(255, 99, 132)",
					borderWidth: 1,
					data: []
				}]
			},
		options: barChart1Options
	});
	
	barChart2 = new Chart($("#canvas2")[0].getContext("2d"), {
		type: "bar",
		data: {
				labels: ["<spring:message code="word.common"/>", "<spring:message code="word.unique"/>", "<spring:message code="word.all"/>"
					    ],
				datasets: [
				{
					label: "<spring:message code="word.weight"/>",
					type: 'bar',		
					backgroundColor: "rgb(75, 192, 192)",
					borderColor: "rgb(75, 192, 192)",
					borderWidth: 1,
					data: []
				},
				{
					label: "<spring:message code="word.weightScore"/>",
					type: 'bar',
					backgroundColor: "rgb(255, 99, 132)",
					borderColor: "rgb(255, 99, 132)",
					borderWidth: 1,
					data: []
				}]
			},
		options: barChart1Options
	});
	
	getTypeChart();
	getGbnChart();
	
	$("#persMetricStatusBtn").click(function(){
		if($(this).hasClass("expand_close_btn")){
			$(this).removeClass("expand_close_btn").addClass("expand_btn");
			$("#persMetricStatusDiv").slideUp();
		}else{
			$(this).removeClass("expand_btn").addClass("expand_close_btn");
			$("#persMetricStatusDiv").slideDown();
		}
	});
	
	$("#outputInEvalGrpBtn").click(function(){
		if($(this).hasClass("expand_close_btn")){
			$(this).removeClass("expand_close_btn").addClass("expand_btn");
			$("#outputInEvalGrpDiv").slideUp();
		}else{
			$(this).removeClass("expand_btn").addClass("expand_close_btn");
			$("#outputInEvalGrpDiv").slideDown();
		}
	});
	
	$("#persMetricVisibleBtn").click(function(){
		if($(this).hasClass("closeInfo_btn")){
			$(this).removeClass("closeInfo_btn").addClass("moreinfo_btn");
			$("div.operatorMetricClass").fadeOut();
		}else{
			$(this).removeClass("moreinfo_btn").addClass("closeInfo_btn");
			$("div.operatorMetricClass").fadeIn();
		}
	});
});

/******************************************
 * chart 당월/누적 Ajax
 ******************************************/
function getTypeChart(){
	sendAjax({
		"url" : "${context_path}/bsc/mon/divOutput/scDeptSummary/typeOutputChart_json.do",
		"data" : getFormData("form"),
		"doneCallbackFunc" : "setTypeChart"
	});
	
}

function setTypeChart(data){
	if(data != undefined, data != ''){
		var datas = data.list;
		
		$(datas).each(function(i,e){
			barChart1.data.datasets[0].data[i] = removePointZeros(parseFloat(e.weight).toFixed(DECIMAL_SCALE));
		});
		
		$(datas).each(function(i,e){
			barChart1.data.datasets[1].data[i] = removePointZeros(parseFloat(e.weightScore).toFixed(DECIMAL_SCALE));
		});
		
		barChart1.update();
	}
}

/******************************************
 * chart 당월/누적 Ajax
 ******************************************/
function getGbnChart(){
	sendAjax({
		"url" : "${context_path}/bsc/mon/divOutput/scDeptSummary/gbnOutputChart_json.do",
		"data" : getFormData("form"),
		"doneCallbackFunc" : "setGbnChart"
	});
	
}

function setGbnChart(data){
	if(data != undefined, data != ''){
		var datas = data.list;
		
		$(datas).each(function(i,e){
			barChart2.data.datasets[0].data[i] = removePointZeros(parseFloat(e.weight).toFixed(DECIMAL_SCALE));
		});
		
		$(datas).each(function(i,e){
			barChart2.data.datasets[1].data[i] = removePointZeros(parseFloat(e.weightScore).toFixed(DECIMAL_SCALE));
		});
		
		barChart2.update();
	}
}

// 목록 조회
function searchList() {
 	//reload("list", "form");
	loadPage("${context_path}/bsc/mon/divOutput/scDeptSummary/scDeptSummaryList.do", "form");
}

// 엑셀 다운로드
function excelDownload() {
	var f = document.form;
	f.action = "${context_path}/bsc/mon/divOutput/scDeptSummary/excelDownload.do";
	f.submit();
}

</script>

<form:form commandName="searchVO" id="form" name="form" method="post">
 	<form:hidden path="year"/>
	<form:hidden path="mon"/>
	<form:hidden path="analCycle"/>
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
	
	
	<%-- 
	<div class="btn-dw">
		<a href="#" class="excel" onclick="excelDownload();return false;"><span><spring:message code="button.excelDownload"/></span></a>
	</div>
	
	<div class="gridContainer">
		<table id="list"></table>
	</div>
	
	<div class="tbl-bottom tbl-bottom2">
		<div class="tbl-btn">
			<a href="#" class="save" onclick="saveSortOrder();return false;"><spring:message code="button.saveAll"/></a>
			<a href="#" class="new" onclick="addData();return false;"><spring:message code="button.add"/></a>
			<a href="#" class="delete" onclick="deleteData();return false;"><spring:message code="button.delete"/></a>
		</div>
	</div>
	--%>
	<div class="divDualBox h390p">
		<div class="divFloatLeft">
			<div class="ptitleLeft"><spring:message code="word.metricTypeStatus" /></div>
			<div class="tbl-typeLeft">
				<table summary="">
					<caption></caption>
					<colgroup>
						<col width="20%"/>
						<col width="20%"/>
						<col width="20%"/>
						<col width="20%"/>
						<col width="20%"/>
					</colgroup>
					<tbody>
						<tr>
						    <th><spring:message code="word.division" /></th>
							<th><spring:message code="word.metricCnt" /></th>
							<th><spring:message code="word.weight" /></th>
							<th><spring:message code="word.weightScore" /></th>
							<th><spring:message code="word.conversionScore" /></th>
						</tr>
						<c:forEach items="${typeList}" var="typeList" varStatus="status">
							<%--
							<fmt:parseNumber value="${typeList.weightScore}" var="typeWeightScore"/>
							<fmt:parseNumber value="${typeList.conversionScore}" var="typeConversionScore"/>
							--%>
							<tr>
								<td>
								<c:choose>
									<c:when test="${typeList.typeId eq 'ALL'}" >
										<spring:message code="word.all" />
									</c:when>
									<c:otherwise>
										<c:out value="${typeList.typeNm}"/>
									</c:otherwise>
								</c:choose>
								</td>
								<td><c:out value="${typeList.metricCnt}"/></td>
								<td><c:out value="${typeList.weight}"/></td>
								<td>${commonUtil:getNumberFormat1(typeList.weightScore)}<%-- <fmt:formatNumber value="${typeWeightScore}" pattern="0.000"/> --%></td>
								<td>${commonUtil:getNumberFormat1(typeList.conversionScore)}<%-- <fmt:formatNumber value="${typeConversionScore}" pattern="0.000"/> --%></td>		
							</tr>
						</c:forEach>
					</tbody>
				</table>
			</div>
			<div id="typeChart" class="chartLeft h200p">
				<div id="actDetailChart" class="m20 hx200">
					<div id="lineChart" class="wp100 hx200">
						<canvas id="canvas1"></canvas>
					</div>
				</div>
			</div>
		</div>
		<div class="divFloatLeft">
			<div class="ptitleRight"><spring:message code="word.metricGbnStatus" /></div>
			<div class="tbl-typeRight">
				<table summary="">
					<caption></caption>
					<colgroup>
						<col width="20%"/>
						<col width="20%"/>
						<col width="20%"/>
						<col width="20%"/>
						<col width="20%"/>
					</colgroup>
					<tbody>
						<tr>
						    <th><spring:message code="word.division" /></th>
							<th><spring:message code="word.metricCnt" /></th>
							<th><spring:message code="word.weight" /></th>
							<th><spring:message code="word.weightScore" /></th>
							<th><spring:message code="word.conversionScore" /></th>
						</tr>
						<c:forEach items="${gbnList}" var="gbnList" varStatus="status">
							<fmt:parseNumber value="${gbnList.weightScore}" var="gbnWeightScore"/>
							<fmt:parseNumber value="${gbnList.conversionScore}" var="gbnConversionScore"/>
							<tr>
								<td>
									<c:choose>
										<c:when test="${gbnList.propertyId eq 'ALL'}" >
											<spring:message code="word.all" />
										</c:when>
										<c:otherwise>
											<c:out value="${gbnList.propertyNm}"/>
										</c:otherwise>
									</c:choose>
								</td>
								<td><c:out value="${gbnList.metricCnt}"/></td>
								<td><c:out value="${gbnList.weight}"/></td>
								<td>${commonUtil:getNumberFormat1(gbnList.weightScore)}<%-- <fmt:formatNumber value="${gbnWeightScore}" pattern="0.000"/> --%></td>
								<td>${commonUtil:getNumberFormat1(gbnList.conversionScore)}<%-- <fmt:formatNumber value="${gbnConversionScore}" pattern="0.000"/> --%></td>	
							</tr>
						</c:forEach>
					</tbody>
				</table>
			</div>
			<div id="gbnChart" class="chartRight h200p">
				<div id="actDetailChart" class="m20 hx200">
					<div id="lineChart" class="wp100 hx200">
						<canvas id="canvas2"></canvas>
					</div>
				</div>
			</div>
		</div>
	</div>
	<div>
		<div class="ptitle3">
			<div class="txt"><spring:message code="word.persMetricStatus" /></div>
			<div id="persMetricVisibleBtn" class="closeInfo_btn"></div>
			<div id="persMetricStatusBtn" class="expand_close_btn"></div>
		</div>
		<div id="persMetricStatusDiv" class="tbl-type01">
			<table summary="">
				<caption></caption>
				<colgroup>
					<col width="12%"/>
					<c:forEach items="${perspectiveList}" var="perspective" varStatus="status">
						<col width="18%"/>
					</c:forEach>
				</colgroup>
				<tbody>
					<tr>
					    <th><spring:message code="word.division" /></th>
					    <c:forEach items="${perspectiveList}" var="perspective" varStatus="status">
							<th><c:out value="${perspective.perspectiveNm}" /></th>
						</c:forEach>
					</tr>
					<c:forEach items="${signalList}" var="signal" varStatus="status">
						<tr>
							<td><div class="signalTable" style='background-color:${signal.color};' onclick="return false;">&nbsp;</div>&nbsp;<c:out value="${signal.signalNm}" /></td>
							
							<c:forEach items="${perspectiveList}" var="perspective" varStatus="status">	
								<td class="td-type01">
								<c:forEach items="${strategyList}" var="strategy" varStatus="status">
									<c:if test="${signal.signalId eq strategy.status && perspective.perspectiveId eq strategy.perspectiveId}">
										<div style="margin:7px 0 0 0;">
											<c:out value="${strategy.strategyNm}" />
										</div>
										<div class="operatorMetricClass" style="margin:4px 0 5px 10px;">
											<ul>
												<c:forEach items="${metricList}" var="metric" varStatus="status">
													<c:if test="${strategy.strategyId eq metric.strategyId}">
														<li style="margin-top:2px;"><a class="signalTable" style='background-color:${metric.color}' onclick="return false;">&nbsp;</a>&nbsp;<c:out value="${metric.metricNm}" /></li>
													</c:if>
												</c:forEach>
											</ul>
										</div>	
									</c:if>
								</c:forEach>
								</td>
							</c:forEach>	
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</div>
	</div>
	<div style="width:100%;">
		<div class="ptitle3">
			<div class="txt"><spring:message code="word.outputInEvalGrp" /></div>
			<div id="outputInEvalGrpBtn" class="expand_close_btn"></div>
		</div>
		<div id="outputInEvalGrpDiv" class="tbl-type01">
			<table summary="">
				<caption></caption>
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
				<tbody>
					<tr>
					    <th><spring:message code="word.division" /></th>
						<th><spring:message code="word.select" /><spring:message code="word.orgScore" /></th>
						<th><spring:message code="word.stadConst" /></th>
						<th><spring:message code="word.inputTypeAvg" /></th>
						<th><spring:message code="word.avgScoreCompare" /></th>
						<th><spring:message code="word.maxScore" /></th>
						<th><spring:message code="word.maxScoreCompare" /></th>
						<th><spring:message code="word.minScore" /></th>
						<th><spring:message code="word.minScoreCompare" /></th>
					</tr>
					<c:forEach items="${compareList}" var="compare" varStatus="status">
						<fmt:parseNumber value="${compare.conversionScore}" var="cpConversionScore"/>
						<fmt:parseNumber value="${compare.stddevScore}" var="cpStddevScore"/>
						<fmt:parseNumber value="${compare.avgScore}" var="cpAvgScore"/>
						<fmt:parseNumber value="${compare.avgCompareScore}" var="cpAvgCompareScore"/>
						<fmt:parseNumber value="${compare.maxScore}" var="cpMaxScore"/>
						<fmt:parseNumber value="${compare.maxCompareScore}" var="cpMaxCompareScore"/>
						<fmt:parseNumber value="${compare.minScore}" var="cpMinScore"/>
						<fmt:parseNumber value="${compare.minCompareScore}" var="cpMinCompareScore"/>
						<tr>
							<td>
								<c:choose>
									<c:when test="${compare.typeId eq 'ALL'}" >
										<spring:message code="word.all" />
									</c:when>
									<c:otherwise>
										<c:out value="${compare.typeNm}"/>
									</c:otherwise>
								</c:choose>
							</td>
							<td class="td-type02">${commonUtil:getNumberFormat1(compare.conversionScore)}<%-- <fmt:formatNumber value="${cpConversionScore}" pattern="0.000"/> --%></td>
							<td class="td-type02">${commonUtil:getNumberFormat1(compare.stddevScore)}<%-- <fmt:formatNumber value="${cpStddevScore}" pattern="0.000"/> --%></td>
							<td class="td-type02">${commonUtil:getNumberFormat1(compare.avgScore)}<%-- <fmt:formatNumber value="${cpAvgScore}" pattern="0.000"/> --%></td>
							<td class="td-type02">${commonUtil:getNumberFormat1(compare.avgCompareScore)}<%-- <fmt:formatNumber value="${cpAvgCompareScore}" pattern="0.000"/> --%></td>
							<td class="td-type02">${commonUtil:getNumberFormat1(compare.maxScore)}<%-- <fmt:formatNumber value="${cpMaxScore}" pattern="0.000"/> --%></td>
							<td class="td-type02">${commonUtil:getNumberFormat1(compare.maxCompareScore)}<%-- <fmt:formatNumber value="${cpMaxCompareScore}" pattern="0.000"/> --%></td>
							<td class="td-type02">${commonUtil:getNumberFormat1(compare.minScore)}<%-- <fmt:formatNumber value="${cpMinScore}" pattern="0.000"/> --%></td>
							<td class="td-type02">${commonUtil:getNumberFormat1(compare.minCompareScore)}<%-- <fmt:formatNumber value="${cpMinCompareScore}" pattern="0.000"/> --%></td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</div>
	</div>
</form:form>

