
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/common-taglibs.jsp"%>

<script type="text/javascript">
	$(function(){
		$(function(){
			
			var barChart_e_Options = chartJsCustom.noValueChartOptions.bar;
			barChart_e_Options.scales = {yAxes: [{
								          display: false,
								          gridLines : {
												display: false
											}
								        }],
								        xAxes: [{
								          gridLines : {
												display: false
											}
								        }]
								      }
			barChart_e = new Chart($("#canvas_e")[0].getContext("2d"), {
				type: "bar",
				data: {
						labels: [
									<c:forEach items="${govList}" var="gList" varStatus="status">
										"${gList.year}" <c:if test="${not status.last}">,</c:if>
									</c:forEach>
							    ],
						datasets: [
						{
							label: "<spring:message code="word.weight"/>",						
							type: 'bar',
							backgroundColor: chartJsCustom.getBGColor(3),
							borderColor: chartJsCustom.getBGColor(3),
							borderWidth: 1,
							data: [
								<c:forEach items="${govList}" var="gList" varStatus="status">
									"${gList.weight}" <c:if test="${not status.last}">,</c:if>
								</c:forEach>
							]
						},
						{
							label: "<spring:message code="word.score"/>",	
							type: 'bar',
							backgroundColor: chartJsCustom.getBGColor(5),
							borderColor: chartJsCustom.getBGColor(5),
							borderWidth: 1,
							data: [
								<c:forEach items="${govList}" var="gList" varStatus="status">
									"${gList.govScore}" <c:if test="${not status.last}">,</c:if>
								</c:forEach>
							]
						}]
					},
				options: barChart_e_Options
			});
			
		});
		
	});
	
	function getGovChart(){
		sendAjax({
			"url" : "${context_path}/bsc/mon/dashboard/dashboard/govScoreList_json.do",
			"data" : "findYear="+$("#findYear").val()+"&findMon="+$("#findMon").val()+"&findAnalCycle="+$(':radio[name="findAnalCycle"]:checked').val()+"&_csrf="+getCsrf("form"),
			"doneCallbackFunc" : "setGovChart"
		});
		
	}

</script>
	<div style="width:100%;height:220px;">
		<div class="divTitleH-Graph"><spring:message code="word.yearGovResultStatusPer3" /></div>
		<div class="wp95 hx200">
			<canvas id="canvas_e" style="margin-left:15px;"></canvas>
		</div>
	</div>

