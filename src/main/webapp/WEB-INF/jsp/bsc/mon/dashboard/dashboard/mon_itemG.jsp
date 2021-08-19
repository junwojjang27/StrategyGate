
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/common-taglibs.jsp"%>

<script type="text/javascript">
	$(function(){
		var barChart_g_Options = chartJsCustom.noValueChartOptions.bar;
		barChart_g_Options.scales = {yAxes: [{
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
		barChart_g = new Chart($("#canvas_g")[0].getContext("2d"), {
			type: "bar",
			data: {
				labels: ["<spring:message code="word.analysis1"/>", "<spring:message code="word.analysis7"/>", "<spring:message code="word.analysis3"/>", "<spring:message code="word.analysis5"/>"],
					datasets: [{
						label: "<spring:message code="word.score"/>",
						backgroundColor: chartJsCustom.getBGColor(2),
						borderColor: chartJsCustom.getColor(2),
						borderWidth: 1,
						data: [
							<c:out value="${commonUtil:getNumberFormat1(scDeptGrpDetail.deptScore)}" />,
							<c:out value="${commonUtil:getNumberFormat1(scDeptGrpDetail.minScore)}" />,
							<c:out value="${commonUtil:getNumberFormat1(scDeptGrpDetail.avgScore)}" />,
							<c:out value="${commonUtil:getNumberFormat1(scDeptGrpDetail.maxScore)}" />
						]
					}]
				},
			options: barChart_g_Options
		});
	});
</script>
	<div style="width:100%;height:220px;">
		<div class="divTitleH-Graph"><spring:message code="word.evalGrpCompareScDept" /></div>
		<div class="wp95 hx200" style="margin-left:15px;">
			<canvas id="canvas_g"></canvas>
		</div>
	</div>

