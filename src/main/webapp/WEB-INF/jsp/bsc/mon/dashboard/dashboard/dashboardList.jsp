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

<script type="text/javascript">

<c:choose>
	<c:when test="${fn:length(itemUserList) > 0}">
		$(".dragGroup").empty();
	
		<c:forEach items="${itemUserList}" var="itemUser" varStatus="status">
			$(".dragGroup").append("<div id='d${itemUser.itemId}' class='dropCanvas ${itemUser.classNm}'><div id='${itemUser.itemId}' class='drag'></div></div>");
		</c:forEach>
	
		<c:forEach items="${itemUserList}" var="itemUser" varStatus="status">
			getItemData("${itemUser.url}","${itemUser.itemId}");
		</c:forEach>
	</c:when>
	<c:otherwise>
		popItemList();	
	</c:otherwise>
</c:choose>

$(function(){
	formToFindValue("form");
	
	$(".dragGroup").sortable({
		disableSelection : true,
		//dropOnEmpty: true,
		//grid: [ 20, 20 ],
		scroll: false,
		//placeholder: "sortable-placeholder",
		sort: function(event, ui) {
			//console.log("event.pageY",event.pageY);
			//console.log("event.screenY",event.screenY);
			var page = event.pageY;
			var spage = event.screenY;
			
			//event["pageY"] = page-400;
			//event["screenY"] = page - 400;
			
			//console.log("sort event",event);
			//console.log("sort ui",ui);
			
			var $target = $(event.target);
            if (!/html|body/i.test($target.offsetParent()[0].tagName)) {
                var top = event.pageY - $target.offsetParent().offset().top - (ui.helper.outerHeight(true) / 2);
                ui.helper.css({'top' : top + 'px'});
            }
        },
        start : function(event,ui){
        	//console.log("strat event",event);
        },
        activate : function(event,ui){
        	//console.log("activate event",event);
			//console.log("activate ui",ui);
        },
        over : function( event, ui ){
        	//console.log("over event",event);
			//console.log("over ui",ui);
        },
        receive : function( event, ui ){
        	//console.log("receive over event",event);
			//console.log("receive over ui",ui);
        },
        deactivate : function( event, ui ){
        	//console.log("deactivate over event",event);
			//console.log("deactivate over ui",ui);
        },
        /*
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
        	//console.log("change ui",ui);
        	//console.log("change ui",ui.helper[0].offsetTop);
        	//var height = ui.item[0].offsetTop-80;
        },
        update : function( event, ui ){
        	//console.log("event update",event);
        	//console.log("ui",ui);
        	//console.log("ui",ui.item[0].offsetTop + ";" + event.pageY);
        	//console.log("11",$(ui.item[0]).find(".chart"));
        	
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
        	saveItemOrder();
        }
        
	});
	/*
	$(".drag,.dragBig").draggable({
		stack : ".drag", //드레그하는 대상의 z-index 높게 설정
		cursor : "pointer", //마우스 커서
		revert: false, //원위치
		refreshPositions: true, //????
		start : function(e,ui){
		},
		drag : function(e,ui){
            return true;
        },
		stop : function(){ // 드래그 종료시 실행
	    	$(".drag").css({"top":0,"left":0});
		}	
  	});


	$(".dropCanvas").droppable({
		drop:function(e,ui){
		
			//이동대상box(drag)
			var $dragBox = $(ui.draggable.context);
			//변경대상 box
			var $replaceBox = $(this).find("div:first");
			//이동대상 canvas
			var $dragCan = $("#d"+ui.draggable[0].id);
			//변경대상 canvas
			var $replaceCan = $(this);
				
			if($dragBox.attr("id") != undefined && $replaceBox.attr("id") != undefined &&
					$dragCan.attr("id") != undefined && $replaceCan.attr("id") != undefined){
				
				var replaceWidth = $replaceBox.width();
				var replaceHeight = $replaceBox.height();
				var dragWidth = $dragBox.width();
				var dragHeight = $dragBox.height();
				
				var parentWidth = $(".dragGroup").width();
				var dragWidthPercent = ((dragWidth/parentWidth)*100).toFixed(5)+"%";
				var replaceWidthPercent = ((replaceWidth/parentWidth)*100).toFixed(5)+"%";
				
				$replaceBox.height(replaceHeight);
				$replaceCan.width(dragWidthPercent);
				$replaceCan.height(dragHeight);
				$dragBox.height(dragHeight);
				$dragCan.width(replaceWidthPercent);
				$dragCan.height(replaceHeight);
				
				$replaceBox.appendTo($dragCan);
				$dragBox.appendTo($replaceCan);
				
				$replaceCan.attr("id","d"+$dragBox.attr("id"));
				$dragCan.attr("id","d"+$replaceBox.attr("id"));
				
				if($dragBox.attr("id") != $replaceBox.attr("id")){
					saveItemOrder();
				}
				
			}else{
				//윈위지로 가세요~~~~
				$(".drag").draggable( "option", "revert", true );
			}
		}
	});
	*/
	
});

function searchList(){
	loadPage("${context_path}/bsc/mon/dashboard/dashboard/dashboardList.do", "form");
}

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

function saveItemOrder(){
	
	var itemIds = "";
	$(".drag").each(function(i,e){
		itemIds += e.id+"\|";
	});
	
	$("#itemIds").val(itemIds);
	
	sendAjax({
		"url" : "${context_path}/bsc/mon/dashboard/dashboard/saveDashboard.do",
		"data" : getFormData("form")
		//"doneCallbackFunc" : "saveComplete"
	});
	
}

function saveComplete(){
	$.showMsgBox("<spring:message code="success.common.update"/>",null);
}

function popItemList(){
	openFancybox({
		"url" : "${context_path}/bsc/mon/dashboard/dashboard/popItemList.do",
		"data" : getFormData("form")
	});
}

function saveItemDo(){
	sendAjax({
		"url" : "${context_path}/bsc/mon/dashboard/dashboard/saveDashboard.do",
		"data" : getFormData("form"),
		"doneCallbackFunc" : "saveComplete"
	});
}

function saveSelectedItemDo(){
	sendAjax({
		"url" : "${context_path}/bsc/mon/dashboard/dashboard/saveDashboard.do",
		"data" : getFormData("form"),
		"doneCallbackFunc" : "saveCompleteReload"
	});
}

function saveCompleteReload(){
	$.showMsgBox("<spring:message code="success.common.update"/>",null);
	loadPage("${context_path}/bsc/mon/dashboard/dashboard/dashboardList.do", "form");
}

</script>

<form:form commandName="searchVO" id="form" name="form" method="post">
	<form:hidden path="itemIds" />
	<form:hidden path="selectedItemIds"/>
	<form:hidden path="findScDeptId" />
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
			<li>
				<input type="button" class="ui-btn" onclick="popItemList()" value="">
			</li>
		</ul>
		<a href="#" class="btn-sch" onclick="searchList();return false;"><spring:message code="button.search"/></a>
	</div>
	<div id="big" class="dragGroup ovf">
		
	</div>
</form:form>

