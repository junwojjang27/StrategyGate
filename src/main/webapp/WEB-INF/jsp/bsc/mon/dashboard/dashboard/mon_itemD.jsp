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
 var barChart_d;
 
	$(function(){
		
		barChart_d = new Chart(document.getElementById("canvas_d"), {
		    type: 'horizontalBar',
		    data: {
		      labels: [
		    	  <c:forEach items="${evalGrpList}" var="eList" varStatus="status">
					"${eList.scDeptNm}" <c:if test="${not status.last}">,</c:if>
				  </c:forEach>
		      ],
		      datasets: [
		        {
		          label: "<spring:message code="word.score"/>",		        
		          backgroundColor: chartJsCustom.getBGColor(1),
				  borderColor: chartJsCustom.getColor(1),
				  borderWidth: 1,
		          data: [
		        	  <c:forEach items="${evalGrpList}" var="eList" varStatus="status">
						"${eList.score}" <c:if test="${not status.last}">,</c:if>
					  </c:forEach>
		          ]
		        }
		      ]
		    },
		    options: {
				responsive: true,
				maintainAspectRatio : false,
				legend: {
					display: true,
					position: "bottom"
				},
				scales : {
					xAxes : [{
						display:false,
						gridLines : {
							display: false
						}
					}],
					yAxes : [{
						gridLines : {
							display: false
						}
					}]
				},
				animation : {
					duration : 500,
				}
		    }	
		});
		
		$("#findScDeptGrpId").change(function(e){
			getEvalGrpChart();
		});
		
	});
	
	function getEvalGrpChart(){
		sendAjax({
			"url" : "${context_path}/bsc/mon/dashboard/dashboard/evalGrpScoreList_json.do",
			"data" : "findYear="+$("#findYear").val()+"&findMon="+$("#findMon").val()+"&findAnalCycle="+$(':radio[name="findAnalCycle"]:checked').val()+"&findScDeptGrpId="
			         +$("#findScDeptGrpId").val()+"&_csrf="+getCsrf("form")+"&findScDeptId="+$("#findScDeptId").val(),
			"doneCallbackFunc" : "setEvalGrpChart"
		});
		
	}

	function setEvalGrpChart(data){
		if(data != undefined && data != ''){
			var data = data.list;
			var scoreArray = [];
			var nameArray = [];
			$(data).each(function(i,e){
				scoreArray[i] = e.score;
				nameArray[i] = e.scDeptNm;
			});
			barChart_d.data.labels = nameArray;
			barChart_d.data.datasets[0].data = scoreArray;
		}	
		barChart_d.update();
	}
	
</script>
	<div style="width:100%;height:220px;">
		<div class="wp95 hx220">
			<div class="divTitleV-Graph"><spring:message code="word.evalGrpDeptStatus" />
				<select class="wx150" id="findScDeptGrpId" name="findScDeptGrpId" style="float:right;position:relative;margin-right:40px;">
					<c:forEach items="${scDeptGrpList}" var="grpList" varStatus="status">
						<c:set var="selected" value=""/>
						<c:if test="${grpList.codeId eq searchVO.findScDeptGrpId}" >
							<c:set var="selected" value=""/>
						</c:if>
						<option value="${grpList.codeId}" ${selected}><c:out value="${grpList.codeNm}" /></option>
					</c:forEach>
				</select>
			</div>
			<div class="wp100 hx200">
				<canvas id="canvas_d" style="margin-left:15px;"></canvas>
			</div>
		</div>
	</div>

