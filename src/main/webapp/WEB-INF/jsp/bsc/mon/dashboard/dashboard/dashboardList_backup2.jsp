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
  border:2px solid  #484848;
  margin:10px 10px 10px 10px;
}

.dragBig {
  width: 98%;
  height: 300px;
  display: inline-block;
  border:1px;
  border:2px solid  #484848;
  margin:10px 10px 10px 10px;
}

</style>

<script type="text/javascript">

$(".dragGroup").empty();

<c:forEach items="${itemUserList}" var="itemUser" varStatus="status">
	$(".dragGroup").append("<div id='${itemUser.itemId}' class='drag'></div>");
</c:forEach>

<c:forEach items="${itemUserList}" var="itemUser" varStatus="status">
	getItemData("${itemUser.url}","${itemUser.itemId}");
</c:forEach>

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
		items : ".drag",
		//dropOnEmpty: true,
		//grid: [ 20, 20 ],
		//scroll: false,
		//placeholder: "sortable-placeholder",
		sort: function(event, ui) {
            
			//console.log("event",event);
			
			
			var $target = $(event.target);
            if (!/html|body/i.test($target.offsetParent()[0].tagName)) {
                var top = event.pageY - $target.offsetParent().offset().top - (ui.helper.outerHeight(true) / 2);
                ui.helper.css({'top' : top + 'px'});
            }
            
        },
        start: function(event, ui) {
            var start_pos = ui.item.index();
            ui.item.data('start_pos', start_pos);
        },
        end : function(event,ui){
        	console.log("event end",event);
        	var $list = $(".drag");
        	$($list).each(function(i,e){
        		console.log("e.id",e.id);	
        	});
        	
        },
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
        	
        	var $list = $(".drag");
        	$($list).each(function(i,e){
        		console.log("e.id",e.id);
        	});
        }
        
	});
	
	
	/*지표실적상세 graph*/
	/*
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
	*/
	
});

function getItemData(url,itemId){
	$.ajax({
		url : "${context_path}"+url,
		data : $("#form").serialize(),
		method : "POST",
		cache : false,
		dataType : "html"
	}).done(function(data) {
		$("#"+itemId).append(data);
	});
}

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
		
	</div>
</form:form>

