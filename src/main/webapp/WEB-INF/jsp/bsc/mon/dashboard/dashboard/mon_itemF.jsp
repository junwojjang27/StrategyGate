
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/common-taglibs.jsp"%>
<script type="text/javascript">
	// 좌측 메뉴 펼침시 화면 밀림 방지를 위한 처리
	function resizeCallback() {
		$("#itemF canvas").width("99%");
	}
	
	$(function(){
		var barChart_f_ptions = chartJsCustom.noValueChartOptions.bar;
		barChart_f_ptions.scales = {yAxes: [{
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
								        }],
								    xAxes: [{
										    	gridLines : {
													display: false
												}
									        }]    
								      }
		barChart_f = new Chart($("#canvas_f")[0].getContext("2d"), {
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
						borderColor: chartJsCustom.getBGColor(1),
						backgroundColor: chartJsCustom.getBGColor(1),
						lineTension: 0,
						pointRadius:5,
						fill: false,
						pointStyle:"rect"
					},
					{
						type: 'bar',		
						yAxisID : 'barY',
						label: "<spring:message code="word.target"/>",
						backgroundColor: chartJsCustom.getBGColor(2),
						borderColor: chartJsCustom.getBGColor(2),
						borderWidth: 1,
						data: []
					},
					{
						type: 'bar',
						yAxisID : 'barY',
						label: "<spring:message code="word.actual"/>",
						backgroundColor: chartJsCustom.getBGColor(3),
						borderColor: chartJsCustom.getBGColor(3),
						borderWidth: 1,
						data: []
					}]
				},
			options: barChart_f_ptions
		});
		
		getActualChart();
		
		$("#findMetricId").change(function(e){
			getActualChart();
		});
	});	
	
	function getActualChart(){
		sendAjax({
			"url" : "${context_path}/bsc/mon/dashboard/dashboard/actualDetailChart_json.do",
			"data" : "findYear="+$("#findYear").val()+"&findMon="+$("#findMon").val()+"&findAnalCycle="+$(':radio[name="findAnalCycle"]:checked').val()+"&findMetricId="
			         +$("#findMetricId").val()+"&_csrf="+getCsrf("form")+"&findScDeptId="+$("#findScDeptId").val(),
			"doneCallbackFunc" : "setActualChart"
		});
		
	}

	function setActualChart(data){
		if(data != undefined, data != ''){
			var detail = data.detail;
			var target = data.target;
			var actual = data.actual;
			var score = data.score;
			
			barChart_f.data.datasets[1].data = [
				target.mon01,	target.mon02,	target.mon03,	target.mon04,
				target.mon05,	target.mon06,	target.mon07,	target.mon08,
				target.mon09,	target.mon10,	target.mon11,	target.mon12
			];
			
			barChart_f.data.datasets[2].data = [
				actual.mon01,	actual.mon02,	actual.mon03,	actual.mon04,
				actual.mon05,	actual.mon06,	actual.mon07,	actual.mon08,
				actual.mon09,	actual.mon10,	actual.mon11,	actual.mon12
			];
			
			barChart_f.data.datasets[0].data = [
				score.mon01,	score.mon02,	score.mon03,	score.mon04,
				score.mon05,	score.mon06,	score.mon07,	score.mon08,
				score.mon09,	score.mon10,	score.mon11,	score.mon12
			];
			
			barChart_f.update();
			
			$("#itemF").find(".Bg").each(function(i,e){
				$(this).empty();
				if(i==0){
					$(this).html("<div class=\"Title\"><spring:message code='word.target'/></div><div class=\"Num\">"+removeNull(getNumberFormat(detail.target,"2"))+"</div>");
				}else if(i==1){
					var classNm = "";
					var imageNm = "";
					if(detail.direction === 'up'){
						$(this).html("<div class=\"Title\"><spring:message code='word.actual'/></div><div class=\"NumGreen\"><img src=\"${img_path}/ico_up.png\" style=\"margin:8px 4px 0 0;\" />"+removeNull(getNumberFormat(detail.actual,"2"))+"</div>");
					}else if(detail.direction === 'down'){
						$(this).html("<div class=\"Title\"><spring:message code='word.actual'/></div><div class=\"NumRed\"><img src=\"${img_path}/ico_down_red.png\" style=\"margin:8px 4px 0 0;\" />"+removeNull(getNumberFormat(detail.actual,"2"))+"</div>");
					}else{
						$(this).html("<div class=\"Title\"><spring:message code='word.actual'/></div><div class=\"Num\">"+removeNull(getNumberFormat(detail.actual,"2"))+"</div>");
					}	
				}else if(i==2){
					$(this).html("<div class=\"Title\"><spring:message code='word.score'/></div><div class=\"Num\">"+removeNull(getNumberFormat(detail.finalScore,"2"))+"%</div>");
				}
			});
		}
	}
</script>
	<div style="width:100%;height:220px;">
		<div class="divTitleUD-GraphDivided01"><spring:message code="word.metricResultStatusDetail" /></div>
		<div class="divSectionTitleDivided02">
			<select class="select wx200" id="findMetricId" name="findMetricId">
				<c:forEach items="${metricList}" var="mList" varStatus="status">
				    <c:set var="selected" value=""/>
				    <c:if test="${mList.metricId eq searchVO.findMetricId}" >
				    	<c:set var="selected" value="selected"/>
				    </c:if>
					<option value="${mList.metricId}" ${selected}><c:out value="${mList.metricNm}" /></option>
				</c:forEach>
			</select>
		</div>
		<div class="divPerforIndicatorD">
			<div class="Bg">
			</div>
			<div class="Bg">
				<%--
				<div class="Title">실적</div>
					<div class="NumRed">
						<img src="./images/common/ico_down_red.png" style="margin:8px 4px 0 0;" />
						<c:out value="${detail.actual}" />
					</div>
					--%>
			</div>
			<div class="Bg">
			</div>
		</div>
		<div style="float:left;width:100%;height:20px;">&nbsp;&nbsp;</div>
		<div class="chart wp97 hx200">
			<canvas id="canvas_f" style="margin-left:15px;"></canvas>
		</div>
	</div>

