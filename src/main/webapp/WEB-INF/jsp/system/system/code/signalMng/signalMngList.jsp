<!-- 
*************************************************************************
* CLASS 명	: SignalMngList
* 작 업 자	: 현걸욱
* 작 업 일	: 2018-2-6
* 기	능	: 신호등관리 List
* ---------------------------- 변 경 이 력 --------------------------------
* 번호	작 업 자		작	업	일			변 경 내 용				비고
* ----	---------	----------------	---------------------	-----------
*	1	현걸욱		2018-2-6				최 초 작 업
**************************************************************************
-->
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/common-taglibs.jsp"%>
<validator:javascript formName="signalMngVO" staticJavascript="false" xhtml="true" cdata="false"/>
<link href="${css_path}/color_picker_farbtastic.css" type="text/css" rel="stylesheet" />
<script type="text/javascript" charset="utf-8" src="${js_path}/color_picker_farbtastic.js"></script>
<script type="text/javascript">
$(function(){
	$("#list").jqGrid({
		url			:	"${context_path}/system/system/code/signalMng/signalMngList_json.do",
		postData	:	getFormData("form"),
		width		:	"${jqgrid_width}",
		height		:	"400px",
		colModel	:	[
						{name:"year",			index:"year",	hidden:true},
						{name:"statusCodeId",	index:"statusCodeId",	width:70,	align:"center",	label:"<spring:message code="word.signalLampStatusCode"/>"},
						{name:"statusId",		index:"statusId",	hidden:true},
						{name:"statusNm",		index:"statusNm",	width:80,	align:"center",	label:"<spring:message code="word.signalLamp"/>"},
						{name:"color",			index:"color",	width:100,	align:"center",	label:"<spring:message code="word.color"/>",
							editable:true, edittype:"custom",
							editoptions:{
								  		 dataInit:function(e){
													var indicatorId = $(e).find("input").attr("id");
													var pickerid = $(e).find("div").eq(1).attr("id");
													$.farbtastic("#"+pickerid).linkTo("#"+indicatorId);
													
												  },
										  custom_element:colorPickerSelect,		  
										  custom_value:colorPickerValue	  
							},
							cellattr:function(rowId,tv,rowObject,colModel,rowData){
								if (colModel.name == 'color' ) {return 'style="background-color:'+rowData.color+';"';}
							}
						},
						{name:"fromValue",	index:"fromValue",	width:80,	align:"center",	label:"<spring:message code="word.startSection"/>(<spring:message code="word.more"/>)",
							editable:true, edittype:"text", editrules:{required:true,number:true},	editoptions:{maxlength:10}			
						},
						{name:"toValue",	index:"toValue",	width:80,	align:"center",	label:"<spring:message code="word.endSection"/>(<spring:message code="word.below"/>)",
							formater:"number", formatoptions: { defaultValue: "&#160;", decimalSeparator: ".", thousandsSeparator: ",", decimalPlaces:DECIMAL_SCALE, removePointZeros:REMOVE_POINT_ZEROS}, 
							editable:true, edittype:"text", editrules:{required:true,number:true},	editoptions:{maxlength:10}			
						},
						{name:"colorVal",	index:"colorVal", hidden:true}
						],
		rowNum		: ${jqgrid_rownum_max},
		//pager		: "pager",
		//sortname	: "sortOrder",
		//sortorder	: "asc",
		cellEdit	: true,
		cellsubmit  :'clientArray',
		multiselect	: false
	});
	
	
	$("body").on("click", function(e) {
		// 코드명 수정 중 코드명 외의 다른 부분을 클릭했을 경우 해당 코드명 저장 처리
		var attr = removeNull($(e.target).attr("aria-describedby"));
		
		if(attr != "list_color") {
			if($("#list").find("div[id$=_color]").length > 0) {
				$("#list").find("div[id$=_color]").closest("tr").find("td").eq(0).click();
			}
		}
	}).on("click","div[id^='picker_']",function(e){
		e.stopPropagation();
	});
	
	
});


function colorPickerSelect(value,options){
	
	var id = options.id;
	var dataId = id.split("_")[0];
	var rowId = $("#list").jqGrid("getDataIDs")[Number(dataId)-1];	
	
	var val = value;
	
	var $p = $("<p></p>");
	var $div = $("<div class=\"inputColor\"></div>");
	
	var $input = $("<input type=\"text\" id=\"indicator_"+rowId+"\" />");
	$input.attr("style","width:100%;background-color:"+val+";");
	$input.appendTo($div);
	
	var $pickerdiv = $("<div id=\"picker_"+rowId+"\" style=\"height:100%;position:absolute;background-color:white;\"></div>");
	$pickerdiv.appendTo($div);
	
	$div.appendTo($p);
	
	var str = $p.html();
	
	return str;
}



function colorPickerValue(elem, operation, value){
	
	var dataId = $(elem).attr("id").split("_")[0];
	var rowId = $("#list").jqGrid("getDataIDs")[Number(dataId)-1];	
		
	var colorVal = $(elem).find("input:first").css("background-color");
	var hexColorVal = rgb2hex(colorVal);
	
	$("#list").jqGrid('setCell', rowId, 'colorVal',hexColorVal);	
	$("#list").jqGrid('setCell', rowId, 'color','',{"background-color":hexColorVal});
	
	return hexColorVal;
}

/******************************************
 * RGB(255, 255, 255)코드를 HEX 코드로 변환시킴
 ******************************************/
function rgb2hex(rgb){
	rgb = rgb.match(/^rgb\((\d+),\s*(\d+),\s*(\d+)\)$/);
	return "#" +
	  ("0" + parseInt(rgb[1],10).toString(16)).slice(-2) +
	  ("0" + parseInt(rgb[2],10).toString(16)).slice(-2) +
	  ("0" + parseInt(rgb[3],10).toString(16)).slice(-2);
}

// 목록 조회
function searchList() {
 	reloadGrid("list", "form");
}

// 저장
function saveData() {
	if(gridToFormChanged("list", "form", true)){
		sendAjax({
			"url" : "${context_path}/system/system/code/signalMng/saveSignalMng.do",
			"data" : getFormData("form"),
			"doneCallbackFunc" : "checkResult"
		});
	}else{
		return false;
	}	
}

function checkResult(data) {
	$.showMsgBox(data.msg);
	if(data.result == AJAX_SUCCESS) {
		searchList();
	}
}

</script>

<form:form commandName="searchVO" id="form" name="form" method="post">
 	<form:hidden path="year"/>
	<form:hidden path="statusId"/>

	<div class="sch-bx">
		<ul>
			<li>
				<label for="findYear"><spring:message code="word.year"/></label>
				<form:select path="findYear" class="select wx100" items="${codeUtil:getCodeList('017')}" itemLabel="codeNm" itemValue="codeId">
				</form:select>
			</li>
		</ul>
		<a href="#" class="btn-sch" onclick="searchList();return false;"><spring:message code="button.search"/></a>
	</div>
	<div class="btn-dw">
		<%--
		<a href="#" class="excel" onclick="excelDownload();return false;"><span><spring:message code="button.excelDownload"/></span></a>
		--%>
	</div>
	<div class="gridContainer">
		<table id="list"></table>
		<div id="pager"></div>
	</div>
	<div class="tbl-bottom">
		<div class="tbl-btn">
			<a href="#" class="new" onclick="saveData();return false;"><spring:message code="button.saveAll"/></a>
		</div>
	</div>
</form:form>