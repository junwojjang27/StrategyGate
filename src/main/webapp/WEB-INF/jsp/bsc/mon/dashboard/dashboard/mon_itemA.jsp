<!-- 
*************************************************************************
* CLASS 명	: mon_itemA
* 작 업 자	: 현걸욱
* 작 업 일	: 2018-05-08
* 기	능	: dashboard List
* ---------------------------- 변 경 이 력 --------------------------------
* 번호	작 업 자		작	업	일			변 경 내 용				비고
* ----	---------	----------------	---------------------	-----------
*	1	현걸욱		2018-05-08				최 초 작 업
**************************************************************************
-->
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/common-taglibs.jsp"%>
<script type="text/javascript">
	$(function(){
		
		var ctx = $("#canvas_a")[0].getContext("2d");
		var doughnutData = ["<c:out value='${deptScore.restScore}' />","<c:out value='${deptScore.score}' />"];
		
		barChart_a = new Chart(ctx, {
			type: "doughnut",
			data: {
					labels: ["<spring:message code='word.orgRemainScore'/>","<spring:message code='word.orgScore'/>"
						    ],
					datasets: [
					{
						backgroundColor: ["gray","rgb(255, 99, 132)"],
						data: ["<c:out value='${commonUtil:getNumberFormat1(deptScore.restScore)}' />","<c:out value='${commonUtil:getNumberFormat1(deptScore.score)}' />"]
					}]
				},
			options: {
			        legend: {
			            display: false,
			        },
			        responsive: true,
			        maintainAspectRatio : false,
			        cutoutPercentage: 70,
				},
			plugins: [{
	            id: 'my-plugin',
	            afterDraw: function (chart, option) {
	              chart.ctx.fillStyle = 'black';
	              chart.ctx.textBaseline = 'middle';
	              chart.ctx.textAlign = 'center';
	              chart.ctx.font = '20px Arial';
	              chart.ctx.fillText("<c:out value='${commonUtil:getNumberFormat1(deptScore.score)}' /><c:if test='${not empty deptScore.score}'>%</c:if>", chart.canvas.width/2, chart.canvas.height/2);
	            }
	         }]	
		});
	});
</script>

	<div style="width:100%;height:220;">
		<div class="divTitleC-Graph"><spring:message code="word.deptSc" /></div>
		<div style="margin-top:30px;height:150px;width:45%;background-color:#fff;float:left;">
			<canvas id="canvas_a"></canvas>
		</div>
		<div class="wp40 hp100" style="margin-top:20px;">
			<div class="divPerforNum">
				<c:set var="classNm" value=""/>
				
				<c:forEach items="${perspectiveList}" var="pList" varStatus="status">
					<c:choose>
						<c:when test="${status.index%2==0}">
							<c:set var="classNm" value="BgL"/>
						</c:when>
						<c:when test="${status.index%2==1}">
							<c:set var="classNm" value="BgR"/>
						</c:when>
					</c:choose>
					<div class="${classNm}">
						<div class="Title"><c:out value="${pList.perspectiveNm}" /></div>
						<div class="Num"><c:out value="${commonUtil:getNumberFormat1(pList.score)}" /></div>
					</div>
				</c:forEach>
				
			</div>
		</div>
	</div>
