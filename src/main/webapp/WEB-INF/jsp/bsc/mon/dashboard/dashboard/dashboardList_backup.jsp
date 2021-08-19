<!-- 
*************************************************************************
* CLASS 명	: DashboardList
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

<style type="text/css">

.drag {
  width: 48%;
  height: 300px;
  display: inline-block;
  border:1px;
  border-color: black;	
  position:relative;
  margin:10px 10px 10px 10px;
}

.dragBig {
  width: 99%;
  height: 300px;
  display: inline-block;
  border:1px;
  border-color: black;	
  position:relative;
  margin:10px 10px 10px 10px;
}

#big {
  width: 100%;
  height: 100%;
  background: #CCCCFF;
}

#dboxE{
  width: 100%;
  height: 100%;
  display: inline-block;
}

</style>

<script type="text/javascript">
/*
var index = 5;
var pos;


var divPos = [];
$(".drag").each(function(i,e){
	console.log(e.id+"/"+$(e).position().left+"/"+$(e).position().top+"/"+$(e).width()+"/"+$(e).height());
	//[id:{top:,left:,width:,height:,cVertical:,cHorizl:}]
	
	var divPosDetails = {}; //var divPosDetails = new Object();
	
	divPosDetails.top = $(e).position().top;
	divPosDetails.left = $(e).position().left;
	divPosDetails.width = $(e).width();
	divPosDetails.height = $(e).height();
	divPosDetails.cv = $(e).position().top;
	divPosDetails.ch = $(e).position().left;
	
	divPos[e.id] = divPosDetails;
	
	//$(this).css({"top":$(e).position().top,"left":$(e).position().left});
	
});

console.log("divPos",divPos);

for(var a in divPos){
	console.log("a",a);
}

var dropPos;
*/

$(function(){
	
	/*
	$(".drag").resizable({
		ghost:true,
		//containment: "parent",
		//alsoResize:".dropCanvas",
		resize:function(e,ui){
			
		},
		stop:function(e,ui){
			
			//console.log($(e.target).closest(".dropCanvas"));
			//console.log(ui.size.width+":"+ui.size.height);
			
			//var $parent = $(e.target).closest(".dropCanvas");
			
			//console.log("$parent",$parent);
			
			//$parent.width(ui.size.width);
			//$parent.height(ui.size.height);
			//console.log($(e.target).closest(".dropCanvas:first").width()+":"+$(e.target).closest(".dropCanvas:first").height());
		}
	});
	*/
	
	$(".dragGroup").sortable({
		disableSelection : true,
		//dropOnEmpty: true,
		grid: [ 20, 20 ],
		scroll: false,
		//placeholder: "sortable-placeholder",
		sort: function(event, ui) {
            
			//console.log("event",event);
			
			var $target = $(event.target);
            if (!/html|body/i.test($target.offsetParent()[0].tagName)) {
                var top = event.pageY - $target.offsetParent().offset().top - (ui.helper.outerHeight(true) / 2);
                ui.helper.css({'top' : top + 'px'});
            }
            
        },
        /*
        strat : function(event,ui){
        	console.log("event",event);
        },
        end : function(event,ui){
        	console.log("event end",event);
        	var $list = $(".drag");
        	$($list).each(function(i,e){
        		console.log("e.id",e.id);	
        	});
        	
        },
        toArray: function(options){
        	console.log("options",options);	
        },
        */
        change : function( event, ui ){
        	//console.log("ui",ui);
        	//console.log("ui",ui.helper[0].offsetTop);
        	//var height = ui.item[0].offsetTop-80;
        },
        update : function( event, ui ){
        	//console.log("event update",event);
        	console.log("ui",ui);
        	console.log("ui",ui.item[0].offsetTop + ";" + event.pageY);
        	console.log("11",$(ui.item[0]).find(".chart"));
        	
        	var height = ui.item[0].offsetTop+80;
        	/*
        	if($(ui.item[0]).find(".chart").length == 0 || $(event.target).find(".chart") == undefined){
        		
        		 $(ui.item[0]).offset({'top' : height + 'px'});
        	}
        	*/
        	
        	/*
        	if($(event.target).find(".chart").length > 0){
    			$(this).offset({"top":ui.item[0].offsetTop-80});
    		}
        	*/
        	
        	var $list = $(".drag,.dragBig");
        	$($list).each(function(i,e){
        		console.log("e.id",e.id);
        	});
        	/*데이터 저장*/
        }
        
	});
	
	
	/*지표실적상세 graph*/
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
	barChart1 = new Chart($("#canvas1")[0].getContext("2d"), {
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
					label: "<spring:message code="word.analysis3"/>",
					data: [80,70,90,50,60,40,80,70,90,50,60,40],
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
					backgroundColor: "rgb(75, 192, 192)",
					borderColor: "rgb(75, 192, 192)",
					borderWidth: 1,
					data: [100,100,100,100,100,100,100,100,100,100,100,100]
				},
				{
					type: 'bar',
					yAxisID : 'barY',
					backgroundColor: "rgb(255, 99, 132)",
					borderColor: "rgb(255, 99, 132)",
					borderWidth: 1,
					data: [55,60,85,99,78,77,55,60,85,99,78,77]
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
					type: 'bar',		
					backgroundColor: "rgb(75, 192, 192)",
					borderColor: "rgb(75, 192, 192)",
					borderWidth: 1,
					data: [40,50,10]
				},
				{
					type: 'bar',
					backgroundColor: "rgb(255, 99, 132)",
					borderColor: "rgb(255, 99, 132)",
					borderWidth: 1,
					data: [42,49,5]
				}]
			},
		options: barChart1Options
	});
	
	barChart3 = new Chart($("#canvas3")[0].getContext("2d"), {
		type: "bar",
		data: {
				labels: ["<spring:message code="word.meas"/>", "<spring:message code="word.nonMeas"/>", "<spring:message code="word.all"/>"
					    ],
				datasets: [
				{
					type: 'bar',		
					backgroundColor: "rgb(75, 192, 192)",
					borderColor: "rgb(75, 192, 192)",
					borderWidth: 1,
					data: [80,10,10]
				},
				{
					type: 'bar',
					backgroundColor: "rgb(255, 99, 132)",
					borderColor: "rgb(255, 99, 132)",
					borderWidth: 1,
					data: [75,5,9]
				}]
			},
		options: barChart1Options
	});
	
});

</script>

<form:form commandName="searchVO" id="form" name="form" method="post">
	<div class="sch-bx">
		<ul>
			<li>
				<label for="findYear"><spring:message code="word.year"/></label>
				<form:select path="findYear" class="select wx100" items="${codeUtil:getCodeList('017')}" itemLabel="codeNm" itemValue="codeId">
				</form:select>
			</li>
			<li>
				<label for="findUseYn"><spring:message code="word.useYn"/></label>
				<form:select path="findUseYn" class="select wx80" items="${codeUtil:getCodeList('011')}" itemLabel="codeNm" itemValue="codeId">
				</form:select>
			</li>
		</ul>
		<a href="#" class="btn-sch" onclick="searchList();return false;"><spring:message code="button.search"/></a>
	</div>
	<div class="btn-dw">
	</div>
	
	<div id="big" class="dragGroup">
		<%--
		<div id="dboxA" class="dropCanvas">	
			<div class="drag" id="boxA" >
				<span>지표목록</span>
				<div style="height:250px;">
					<table id="metricList"></table>
				</div>
			</div>
		</div>	
		<div id="dboxB" class="dropCanvas">
			<div class="drag" id="boxB" >
			</div>
		</div>
		<div id="dboxC" class="dropCanvas">
			<div class="drag" id="boxC" >
			</div>
		</div>
		<div id="dboxD" class="dropCanvas">
			<div class="drag" id="boxD" >
			</div>
		</div>
		<div id="dboxE" class="dropCanvas">
			<div class="drag" id="boxE" >
			   <span>월별 실적 추이</span>
				<div id="actDetailChart" class="m20 hx200">
					<div id="lineChart" class="wp100 hx200">
						<canvas id="canvas1"></canvas>
					</div>
				</div>
			</div>
		</div>
		--%>
		<div class="drag" id="boxA" >
			<div class="ptitle">실행계획</div>
			<div class="tbl-type02" style="width:93%;height:230px;overflow:auto;">
				<table>
					<colgroup>
						<col width="20%"/>
						<col width="20%"/>
						<col width="20%"/>
						<col width="10%"/>
						<col width="10%"/>
						<col width="20%"/>
					</colgroup>
					<tbody>
						<tr>
							<th>지표</th>
							<th>목표</th>
							<th>실적</th>
							<th>달성율</th>
							<th>상태</th>
							<th>현황</th>
						<tr>
						<tr>
							<td>매출액</td>
							<td>2,000,000</td>
							<td>1,850,000</td>
							<td>87.5%</td>
							<td>01</td>
							<td>graph</td>
						<tr>
						<tr>
							<td>매출액</td>
							<td>2,000,000</td>
							<td>1,850,000</td>
							<td>87.5%</td>
							<td>01</td>
							<td>graph</td>
						<tr>
						<tr>
							<td>매출액</td>
							<td>2,000,000</td>
							<td>1,850,000</td>
							<td>87.5%</td>
							<td>01</td>
							<td>graph</td>
						<tr>
						<tr>
							<td>매출액</td>
							<td>2,000,000</td>
							<td>1,850,000</td>
							<td>87.5%</td>
							<td>01</td>
							<td>graph</td>
						<tr>
						<tr>
							<td>매출액</td>
							<td>2,000,000</td>
							<td>1,850,000</td>
							<td>87.5%</td>
							<td>01</td>
							<td>graph</td>
						<tr>
					</tbody>
				</table>
			</div>
		</div>
		<div class="drag" id="boxB" >
			<div class="tbl-type02" style="width:93%;height:230px;overflow:auto;">
				<table>
					<colgroup>
						<col width="20%"/>
						<col width="20%"/>
						<col width="20%"/>
						<col width="10%"/>
						<col width="10%"/>
						<col width="20%"/>
					</colgroup>
					<tbody>
						<tr>
							<th>지표</th>
							<th>목표</th>
							<th>실적</th>
							<th>달성율</th>
							<th>상태</th>
							<th>현황</th>
						<tr>
						<tr>
							<td>매출액</td>
							<td>2,000,000</td>
							<td>1,850,000</td>
							<td>87.5%</td>
							<td>01</td>
							<td>graph</td>
						<tr>
						<tr>
							<td>매출액</td>
							<td>2,000,000</td>
							<td>1,850,000</td>
							<td>87.5%</td>
							<td>01</td>
							<td>graph</td>
						<tr>
						<tr>
							<td>매출액</td>
							<td>2,000,000</td>
							<td>1,850,000</td>
							<td>87.5%</td>
							<td>01</td>
							<td>graph</td>
						<tr>
						<tr>
							<td>매출액</td>
							<td>2,000,000</td>
							<td>1,850,000</td>
							<td>87.5%</td>
							<td>01</td>
							<td>graph</td>
						<tr>
						<tr>
							<td>매출액</td>
							<td>2,000,000</td>
							<td>1,850,000</td>
							<td>87.5%</td>
							<td>01</td>
							<td>graph</td>
						<tr>	
					</tbody>
				</table>
			</div>
		</div>
		<div class="drag" id="boxC">
			<div style="width:93%;height:300px;">
				<div id="lineChart" class="chart wp100 hx200">
					<canvas id="canvas2"></canvas>
				</div>
			</div>
		</div>
		<div class="drag" id="boxD" >
			<div style="width:93%;height:300px;">
				<div class="chart wp100 hx200">
					<canvas id="canvas3"></canvas>
				</div>
			</div>
		</div>
		<div class="drag" id="boxE" >
			<div style="width:93%;height:300px;">
				<div class="chart wp98 hx200">
					<canvas id="canvas1"></canvas>
				</div>
			</div>	
		</div>
		
	</div>
</form:form>

