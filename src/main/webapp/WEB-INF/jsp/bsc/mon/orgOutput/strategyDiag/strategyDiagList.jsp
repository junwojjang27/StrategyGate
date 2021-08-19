<!--
*************************************************************************
* CLASS 명	: StrategyDiagList
* 작 업 자	: 현걸욱
* 작 업 일	: 2018-05-03
* 기	능	: 전략연계도 List
* ---------------------------- 변 경 이 력 --------------------------------
* 번호	작 업 자		작	업	일			변 경 내 용				비고
* ----	---------	----------------	---------------------	-----------
*	1	현걸욱		2018-05-03				최 초 작 업
**************************************************************************
-->
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/common-taglibs.jsp"%>
<link href="${theme_path}/svgChart.css" type="text/css" rel="stylesheet"/>
<script type="text/javascript" src="${js_path}/d3.min.js"></script>
<script type="text/javascript" src="${js_path}/svgChart/svgTree.js"></script>

<script type="text/javascript">

$(function(){

	$('#findStrategyId').select2({
		   allowClear: false,
		   width:"200px",
		   language: "${searchVO.lang}",
		   selectOnClose : true
	});

	$("#findYear").on("change",function(){
		getSelect2Data();
	});

});

function getSelect2Data(){
	sendAjax({
		"url" : "${context_path}/bsc/mon/orgOutput/strategyDiag/strategyList_json.do",
		"data" : getFormData("form"),
		"doneCallbackFunc" : "setSelect2Data"
	});
}

function setSelect2Data(data){
	var datas = data.list;
	$("select[name='findStrategyId']").empty();

	if(datas !=  null && datas !=  undefined && datas.length > 0){

		$(datas).each(function(i,e){
			$("#findStrategyId").append("<option value='"+e.strategyId+"'>"+e.strategyNm+"</option>");

		});
	}
}

function cascadingJS(val) {
	val = val.split("|")[0];
	var dualScreenLeft = window.screenLeft != undefined ? window.screenLeft : window.screenX;
    var dualScreenTop = window.screenTop != undefined ? window.screenTop : window.screenY;
	var width = window.innerWidth ? window.innerWidth : document.documentElement.clientWidth ? document.documentElement.clientWidth : screen.width;
    var height = window.innerHeight ? window.innerHeight : document.documentElement.clientHeight ? document.documentElement.clientHeight : screen.height;
	var left = ((width / 2) - 900) + dualScreenLeft;
    var top = ((height / 2) - 400) + dualScreenTop;
    var url = "${context_path}/bsc/mon/divOutput/scDeptDetail/popScDeptDetailList.do?findYear="+$("#findYear").val()+"&findMon="+$("#findMon").val()+"&findStrategyId="+$("#findStrategyId").val()+"&findScDeptId="+val+"&layout=popup";
    var title = "popScDeptDetailList"
	$("#findScDeptId").val(val);
    var newWindow = window.open(url, title, 'scrollbars=no, width=' + 1300 + ', height=' + 600 + ', top=' + top + ', left=' + left);
    if (window.focus) {
        newWindow.focus();
    }
}

// 목록 조회
function searchList() {
	reloadChart();
}

function reloadChart(){

	$(".svgTreeLayout").remove();

	new SvgTree({
		"url":"${context_path}/bsc/mon/orgOutput/strategyDiag/strategyDiagList_xml.do?year="+$("#findYear").val()+"%26findYear="+$("#findYear").val()+"%26findMon="+$("#findMon").val()+"%26findAnalCycle="+$("[name='findAnalCycle']:checked").val()+"%26findStrategyId="+$("#findStrategyId").val(),
		"targetId":"splitterBox"
	});

}

</script>

<form:form commandName="searchVO" id="form" name="form" method="post">
 	<form:hidden path="year"/>
	<form:hidden path="mon"/>
	<form:hidden path="analCycle"/>
	<form:hidden path="scDeptId"/>
	<form:hidden path="strategyId"/>
	<form:hidden path="findScDeptId"/>

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
				<label for="findStrategyId"><spring:message code="word.strategy"/></label>
				<form:select path="findStrategyId" class="select wx200" items="${strategyList}" itemLabel="strategyNm" itemValue="strategyId">
				</form:select>
			</li>
			<li>
				<label for="findAnalCycle"><spring:message code="word.analysisCycle"/></label>
				<form:radiobuttons path="findAnalCycle" items="${codeUtil:getCodeList('138')}" itemLabel="codeNm" itemValue="codeId" />
			</li>
		</ul>
		<a href="#" class="btn-sch" onclick="searchList();return false;"><spring:message code="button.search"/></a>
	</div>
	<div class="table-bx2">
		<div id="splitterBox">
			<div style="padding: 0px; border-image: none;">
				<script>
					new SvgTree({
						"url":"${context_path}/bsc/mon/orgOutput/strategyDiag/strategyDiagList_xml.do?year=${searchVO.findYear}%26findYear=${searchVO.findYear}%26findMon=${searchVO.findMon}%26findAnalCycle=${searchVO.findAnalCycle}%26findStrategyId=${searchVO.findStrategyId}",
						"targetId":"splitterBox",
						"width"	: "1200",
						"height": "600"
					});
				</script>
			</div>
		</div>
	</div>
</form:form>

