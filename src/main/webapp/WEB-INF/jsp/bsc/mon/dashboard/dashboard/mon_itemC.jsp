<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/common-taglibs.jsp"%>
<script type="text/javascript">
$(function(){
	
});

function popMetricList(){
	openFancybox({
		"url" : "${context_path}/bsc/mon/dashboard/dashboard/popItemCMetricList.do",
		"data" : getFormData("itemCForm")
	});
}

function saveSelectedMetricDo(){
	sendAjax({
		"url" : "${context_path}/bsc/mon/dashboard/dashboard/saveItemCData.do",
		"data" : getFormData("itemCForm"),
		"doneCallbackFunc" : "getMainMetricData"
	});
}

function getMainMetricData(){
	sendAjax({
		"url" : "${context_path}/bsc/mon/dashboard/dashboard/mon_itemC_json.do",
		"data" : getFormData("itemCForm"),
		"doneCallbackFunc" : "setMainMetricData"
	});
}

function setMainMetricData(data){
	var data = data.list;
	$("#itemCDiv").empty();

	$(data).each(function(i,e){
		var $body = $("<div class=\"Bg\"></div>");
		var $bodySub;
		$("<div class=\"TitleLeft\"></div>").text(e.metricNm).appendTo($body);
		$("<div class=\"TitleRight\"></div>").text("(단위 : "+e.unitNm+")").appendTo($body);
		if(e.direction == 'up'){
			$bodySub = $("<div class=\"NumGreen\"></div>");
			$("<div style='width:100%;height:100%;text-overflow:ellipsis;white-space:nowrap;overflow:hidden;float:left;'></div>").html("<img src=\"${img_path}/ico_up.png\" style=\"margin: 0 6px 0 0;\"/>"+getNumberFormat(e.actual)).appendTo($bodySub);
			$bodySub.appendTo($body);
		}else if(e.direction == 'down'){
			$bodySub = $("<div class=\"NumRed\"></div>");
			$("<div style='width:100%;height:100%;text-overflow:ellipsis;white-space:nowrap;overflow:hidden;float:left;'></div>").html("<img src=\"${img_path}/ico_down_red.png\" style=\"margin: 0 6px 0 0;\"/>"+getNumberFormat(e.actual)).appendTo($bodySub);
			$bodySub.appendTo($body);
		}else{
			$bodySub = $("<div class=\"Num\"></div>");
			$("<div style='width:100%;height:100%;text-overflow:ellipsis;white-space:nowrap;overflow:hidden;float:left;'></div>").html(getNumberFormat(e.actual)).appendTo($bodySub);
			$bodySub.appendTo($body);
		}
		var $lastElem = $("<div class=\"StandardBg\"></div>");
		$("<div class=\"Line01\"></div>").text("<spring:message code="word.target" />").appendTo($lastElem);
		$("<div class=\"Line02\"></div>").text(getNumberFormat(e.target)).appendTo($lastElem);
		$($lastElem).appendTo($body);
		
		$("#itemCDiv").append($body);
	});
}

</script>
    <form:form commandName="searchVO" id="itemCForm" name="itemCForm" method="post">
        <form:hidden path="findYear" />
        <form:hidden path="findMon" />
        <form:hidden path="findAnalCycle" />
    	<form:hidden path="selectedMetricIds" />
    	<form:hidden path="selectedSortOrders" />
    </form:form>
	
	<div style="width:100%;height:160px;">
		<input type="button" class="ui-item-btn" onclick="popMetricList()" value="">
		<div class="divTitleT-Graph"><spring:message code="word.majorMetricSc" /></div>
		<div class="wp100 hx70">
			<div id="itemCDiv" class="divKeyIndicatorPerfor">
				<c:forEach items="${mainMetricList}" var="mList" varStatus="status">
				
					<div class="Bg">
						<div class="TitleLeft"><c:out value="${mList.metricNm}" /></div>
						<div class="TitleRight">(<spring:message code="word.unit" /> : <c:out value="${mList.unitNm}" />)</div>
						<c:choose>
							<c:when test="${mList.direction eq 'up' }" >
								<div class="NumGreen">
									<div style='width:100%;height:100%;text-overflow:ellipsis;white-space:nowrap;overflow:hidden;float:left;'>
										<img src="${img_path}/ico_up.png" style="margin: 0 6px 0 0;"/>
										<c:out value="${commonUtil:getNumberFormat1(mList.actual)}" />
									</div>
								</div>	
							</c:when>
							<c:when test="${mList.direction eq 'down' }" >
								<div class="NumRed">
									<div style='width:100%;height:100%;text-overflow:ellipsis;white-space:nowrap;overflow:hidden;float:left;'>
										<img src="${img_path}/ico_down_red.png" style="margin: 0 6px 0 0;"/>
										<c:out value="${commonUtil:getNumberFormat1(mList.actual)}" />
									</div>
								</div>
							</c:when>
							<c:otherwise>
								<div class="Num">
									<div style='width:100%;height:100%;text-overflow:ellipsis;white-space:nowrap;overflow:hidden;float:left;'>
										<c:out value="${commonUtil:getNumberFormat1(mList.actual)}" />
									</div>
								</div>
							</c:otherwise>
						</c:choose>
						<div class="StandardBg">
							<div class="Line01"><spring:message code="word.target" /></div>
							<div class="Line02"><c:out value="${commonUtil:getNumberFormat1(mList.target)}" /></div>
						</div>
					</div>
				
				</c:forEach>
			</div>
		</div>	
	</div>


