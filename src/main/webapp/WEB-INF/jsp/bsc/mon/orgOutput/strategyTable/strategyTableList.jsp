<!--
*************************************************************************
* CLASS 명	: StrategyTableList
* 작 업 자	: 현걸욱
* 작 업 일	: 2018-05-03
* 기	능	: 전략연계표 List
* ---------------------------- 변 경 이 력 --------------------------------
* 번호	작 업 자		작	업	일			변 경 내 용				비고
* ----	---------	----------------	---------------------	-----------
*	1	현걸욱		2018-05-03				최 초 작 업
**************************************************************************
-->
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/common-taglibs.jsp"%>
<validator:javascript formName="strategyTableVO" staticJavascript="false" xhtml="true" cdata="false"/>
<script type="text/javascript">
$(function(){
	$("#list").jqGrid({
		url			:	"${context_path}/bsc/mon/orgOutput/strategyTable/strategyTableList_json.do",
		postData	:	getFormData("form"),
		width		:	"${jqgrid_width}",
		height		:	"${jqgrid_height}",
		colModel	:	[
						{name:"year",	index:"year",	hidden:true},
						{name:"scDeptId",	index:"scDeptId",	hidden:true},
						{name:"scDeptNm",	index:"scDeptNm",	width:150,	align:"left",	label:"<spring:message code="word.scDeptNm" />"},
						{name:"strategyId",	index:"strategyId",	hidden:true},
						{name:"strategyNm",	index:"strategyNm",	width:150,	align:"left",	label:"<spring:message code="word.strategy" />"},
						{name:"color",		index:"color",		width:60,	align:"center",	label:"<spring:message code="word.status" />",
							formatter:function(cellvalue, options, rowObject) {
								if(cellvalue == null) {
									cellvalue = "";
								}
								return "<div class='signalCircle' style='background-color:" + cellvalue + "'></div>";
							}
						},
						{name:"firScDeptId",	index:"firScDeptId",	hidden:true},
						{name:"firScDeptNm",	index:"firScDeptNm",	width:150,	align:"left",	label:"<spring:message code="word.scDeptNm" />"},
						{name:"firStrategyId",	index:"firStrategyId",	hidden:true},
						{name:"firStrategyNm",	index:"firStrategyNm",	width:150,	align:"left",	label:"<spring:message code="word.strategy" />",
							formatter:function(cellvalue, options, rowObject) {
									return "<a href='#' onclick='popDetails(\""+rowObject.firScDeptId+"\");return false;'>"+cellvalue+"</a>";
							}
						},
						{name:"firColor",		index:"firColor",		width:60,	align:"center",	label:"<spring:message code="word.status" />",
							formatter:function(cellvalue, options, rowObject) {
								if(cellvalue == null) {
									cellvalue = "";
								}
								return "<div class='signalCircle' style='background-color:" + cellvalue + "'></div>";
							}
						},
						{name:"secScDeptId",	index:"secScDeptId",	hidden:true},
						{name:"secScDeptNm",	index:"secScDeptNm",	width:150,	align:"left",	label:"<spring:message code="word.scDeptNm" />"},
						{name:"secStrategyId",	index:"secStrategyId",	hidden:true},
						{name:"secStrategyNm",	index:"secStrategyNm",	width:150,	align:"left",	label:"<spring:message code="word.strategy" />"},
						{name:"secColor",		index:"secColor",		width:60,	align:"center",	label:"<spring:message code="word.status" />",
							formatter:function(cellvalue, options, rowObject) {
								if(cellvalue == null) {
									cellvalue = "";
								}
								return "<div class='signalCircle' style='background-color:" + cellvalue + "'></div>";
							}
						},
						{name:"thiScDeptId",	index:"thiScDeptId",	hidden:true},
						{name:"thiScDeptNm",	index:"thiScDeptNm",	width:150,	align:"left",	label:"<spring:message code="word.scDeptNm" />"},
						{name:"thiStrategyId",	index:"thiStrategyId",	hidden:true},
						{name:"thiStrategyNm",	index:"thiStrategyNm",	width:150,	align:"left",	label:"<spring:message code="word.strategy" />"},
						{name:"thiColor",		index:"thiColor",		width:60,	align:"center",	label:"<spring:message code="word.status" />",
							formatter:function(cellvalue, options, rowObject) {
								if(cellvalue == null) {
									cellvalue = "";
								}
								return "<div class='signalCircle' style='background-color:" + cellvalue + "'></div>";
							}
						}
						],
		rowNum		: ${jqgrid_rownum_max},
		multiselect	: false,
		loadComplete:function(){
			$("#list").rowspan("list",2,1);
			$("#list").rowspan("list",4,1);
			$("#list").rowspan("list",5,1);

			$("#list").rowspan("list",7,6);
			$("#list").rowspan("list",9,6);
			$("#list").rowspan("list",10,6);

			$("#list").rowspan("list",12,11);
			$("#list").rowspan("list",14,11);
			$("#list").rowspan("list",15,11);
		}
	});

	$("#findYear").change(function(){
		getStrategyList();
	});
});

function getStrategyList(){
	sendAjax({
		"url" : "${context_path}/bsc/mon/orgOutput/strategyTable/strategyList_json.do",
		"data" : getFormData("form"),
		"doneCallbackFunc" : "setStrategyList"
	});
}

function setStrategyList(data){
	var list = data.list;
	$("#findStrategyId").empty();
	$(list).each(function(i,e){
		$("#findStrategyId").append("<option value=\""+e.strategyId+"\">"+e.strategyNm+"</option>");
	});
}

// 목록 조회
function searchList() {
 	reloadGrid("list", "form");
}

// 엑셀 다운로드
function excelDownload() {
	var f = document.form;
	f.action = "${context_path}/bsc/mon/orgOutput/strategyTable/excelDownload.do";
	f.submit();
}

function popDetails(val) {
	val = val.split("|")[0];
	var w = 1800; var h = 800;
	var dualScreenLeft = window.screenLeft != undefined ? window.screenLeft : window.screenX;
    var dualScreenTop = window.screenTop != undefined ? window.screenTop : window.screenY;
	$("#findScDeptId").val(val);
	var width = window.innerWidth ? window.innerWidth : document.documentElement.clientWidth ? document.documentElement.clientWidth : screen.width;
    var height = window.innerHeight ? window.innerHeight : document.documentElement.clientHeight ? document.documentElement.clientHeight : screen.height;
	var left = ((width / 2) - (w / 2)) + dualScreenLeft;
    var top = ((height / 2) - (h / 2)) + dualScreenTop;
    var url = "${context_path}/bsc/mon/divOutput/scDeptDetail/popScDeptDetailList.do?findYear="+$("#findYear").val()+"&findMon="+$("#findMon").val()+"&findStrategyId="+$("#findStrategyId").val()+"&findScDeptId="+val+"&layout=popup";
    var title = "popScDeptDetailList"
    var newWindow = window.open(url, title, 'scrollbars=no, width=' + w + ', height=' + h + ', top=' + top + ', left=' + left);

    // Puts focus on the newWindow
    if (window.focus) {
        newWindow.focus();
    }
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
				<label for="findAnalCycle"><spring:message code="word.analysisCycle"/></label>
				<form:radiobuttons path="findAnalCycle" items="${codeUtil:getCodeList('138')}" itemLabel="codeNm" itemValue="codeId" />
			</li>
			<li>
				<label for="findStrategyId"><spring:message code="word.strategy"/></label>
				<form:select path="findStrategyId" class="select wx200" items="${strategyList}" itemLabel="strategyNm" itemValue="strategyId">
				</form:select>
			</li>
		</ul>
		<a href="#" class="btn-sch" onclick="searchList();return false;"><spring:message code="button.search"/></a>
	</div>
	<div class="btn-dw">
		<a href="#" class="excel" onclick="excelDownload();return false;"><span><spring:message code="button.excelDownload"/></span></a>
	</div>
	<div class="gridContainer">
		<table id="list"></table>
	</div>
	<%--
	<div class="tbl-bottom tbl-bottom2">
		<div class="tbl-btn">
			<a href="#" class="save" onclick="saveSortOrder();return false;"><spring:message code="button.saveAll"/></a>
			<a href="#" class="new" onclick="addData();return false;"><spring:message code="button.add"/></a>
			<a href="#" class="delete" onclick="deleteData();return false;"><spring:message code="button.delete"/></a>
		</div>
	</div>
	<div class="page-noti">
		<ul>
			<li></li>
			<li></li>
		</ul>
	</div>
	--%>

</form:form>

