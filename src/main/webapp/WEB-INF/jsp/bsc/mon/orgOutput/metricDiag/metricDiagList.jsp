<!-- 
*************************************************************************
* CLASS 명	: MetricDiagList
* 작 업 자	: 현걸욱
* 작 업 일	: 2018-05-03
* 기	능	: 지표연계도 List
* ---------------------------- 변 경 이 력 --------------------------------
* 번호	작 업 자		작	업	일			변 경 내 용				비고
* ----	---------	----------------	---------------------	-----------
*	1	현걸욱		2018-05-03				최 초 작 업
**************************************************************************
-->
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/common-taglibs.jsp"%>
<script type="text/javascript" src="${js_path}/d3.min.js"></script>
<script type="text/javascript" src="${js_path}/svgChart/svgTree.js"></script>

<script type="text/javascript">

$(function(){
	
	$('#findMetricId').select2({
		   allowClear: false,	
		   width:"300px",
		   language: "${searchVO.lang}",
		   selectOnClose : true
	});
	
	$("#findYear").on("change",function(){
		getSelect2Data();
	});
	
});

function getSelect2Data(){
	sendAjax({
		"url" : "${context_path}/bsc/mon/orgOutput/metricDiag/metricList_json.do",
		"data" : getFormData("form"),
		"doneCallbackFunc" : "setSelect2Data"
	});
}

function setSelect2Data(data){
	var datas = data.list;
	$("select[name='findMetricId']").empty();
	
	if(datas !=  null && datas !=  undefined && datas.length > 0){
		
		$(datas).each(function(i,e){
			$("#findMetricId").append("<option value='"+e.metricId+"'>"+e.metricNm+"</option>");
			
		});
	}	
}

// 목록 조회
function searchList() {
	reloadChart();
}

function reloadChart(){
	
	$(".svgTreeLayout").remove();
	
	new SvgTree({
		"url":"${context_path}/bsc/mon/orgOutput/metricDiag/metricDiagList_xml.do?year="+$("#findYear").val()+"%26findYear="+$("#findYear").val()+"%26findMon="+$("#findMon").val()+"%26findAnalCycle="+$("[name='findAnalCycle']:checked").val()+"%26findMetricId="+$("#findMetricId").val(),
		"targetId":"splitterBox"
	});
	
}

</script>

<form:form commandName="searchVO" id="form" name="form" method="post">
 	<form:hidden path="year"/>
	<form:hidden path="mon"/>
	<form:hidden path="analCycle"/>
	<form:hidden path="scDeptId"/>
	<form:hidden path="metricGrpId"/>

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
				<label for="findMetricId"><spring:message code="word.metric"/></label>
				<form:select path="findMetricId" class="select wx200" items="${metricList}" itemLabel="metricNm" itemValue="metricId">
				</form:select>
			</li>
			<li>
				<label for="findAnalCycle"><spring:message code="word.analysisCycle"/></label>
				<form:radiobuttons path="findAnalCycle" items="${codeUtil:getCodeList('138')}" itemLabel="codeNm" itemValue="codeId" />
			</li>
		</ul>
		<a href="#" class="btn-sch" onclick="searchList();return false;"><spring:message code="button.search"/></a>
	</div>
	<div class="btn-dw">
	</div>
	<div class="table-bx2">
		<div id="splitterBox">
			<div style="padding: 0px; border: 1px dashed rgb(213, 213, 213); border-image: none;">
				<script>
					new SvgTree({
						"url":"${context_path}/bsc/mon/orgOutput/metricDiag/metricDiagList_xml.do?year=${searchVO.findYear}%26findYear=${searchVO.findYear}%26findMon=${searchVO.findMon}%26findAnalCycle=${searchVO.findAnalCycle}%26findMetricId=${searchVO.findMetricId}",
						"targetId":"splitterBox"
					});
				</script>
			</div>
		</div>		
	</div>
</form:form>

