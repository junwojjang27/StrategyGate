<!-- 
*************************************************************************
* CLASS 명	: CompMenuMngList
* 작 업 자	: joey
* 작 업 일	: 2018-1-7
* 기	능	: 메뉴관리 List
* ---------------------------- 변 경 이 력 --------------------------------
* 번호	작 업 자		작	업	일			변 경 내 용				비고
* ----	---------	----------------	---------------------	-----------
*	1	joey		2018-1-7				최 초 작 업
**************************************************************************
-->
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/common-taglibs.jsp"%>
<validator:javascript formName="menuMngVO" staticJavascript="false" xhtml="true" cdata="false"/>
<script type="text/javascript">
$(function(){
	$("#list").jqGrid({
		url			:	"${context_path}/system/system/comp/compMenuMng/compMenuMngList_json.do",
		postData	:	getFormData("form"),
		width		:	"${jqgrid_width}",
		height		:	"500",
		colModel	:	[
						{name:"pgmId",		index:"pgmId",		width:100,	hidden:true},
						{name:"mainPgmNm",	index:"mainPgmNm",  width:200, 	align:"left",	label:"<spring:message code="word.menu"/>"},
						/*
						{name:"pgmNm",	    index:"pgmNm",		width:150, 	align:"left",	label:"<spring:message code="word.menu"/>",
							editable:true, edittype:"text", editrules:{custom:true, custom_func:jqGridChkBytes},	editoptions:{maxlength:150}
						},
						*/
						{name:"pgmNm",	index:"pgmNm",	width:250,	align:"left",	label:"<spring:message code="word.menu"/>",
							//editable:true, edittype:"text", editrules:{required:true, custom:true, custom_func:jqGridChkBytes}, editoptions:{maxlength:500}
							editable:true, edittype:"custom", 
							editrules:{required:true},
							editoptions:{custom_element:getMultiInput,
								         custom_value:getMultiValue	
							}
						},
						{name:"pgmNmLang",	index:"pgmNmLang", hidden:true},
						{name:"upPgmId",	index:"upPgmId",    align:"center",	hidden:true, label:"<spring:message code="word.upperMenuCd"/>"},
						{name:"upPgmNm",	index:"upPgmNm",	width:150,	align:"center",	label:"<spring:message code="word.upperMenuNm"/>", hidden:true},
						{name:"pgmLevelId",	index:"pgmLevelId",	width:60, hidden:true},
						/*
						{name:"url",		index:"url",		width:250,	align:"left",	label:"<spring:message code="word.menuUrl"/>"},	
						{name:"param",		index:"param",		width:80,	align:"left",	label:"<spring:message code="word.menuParam"/>",
							editable:true, edittype:"text", editrules:{custom:true, custom_func:jqGridChkBytes},	editoptions:{maxlength:150}
						},	
						*/
						{name:"guideCommentLink",index:"guideCommentLink",width:80,	align:"center", label:"<spring:message code="word.help"/>",
							formatter:function(cellvalue, options, rowObject) {
								if(rowObject.pgmLevelId < 4) return "";
								if(rowObject.guideCnt == 0) {
									return "<input type='button' class='pButton3_grid'  value='<spring:message code="button.add"/>' onclick='popGuideComment(\"" + options.rowId + "\");clickGridRow(this);return false;' />";
								}else if(rowObject.guideCnt == 1) {
									return "<input type='button' class='pButton3_grid'  value='<spring:message code="button.modify"/>' onclick='popGuideComment(\"" + options.rowId + "\");clickGridRow(this);return false;' />";
								}else{
									return "";
								}
							}, unformat:inputUnformatter
						},
						{name:"urlPattern",	index:"urlPattern", hidden:true},	
						
						{name:"authGubunNms",	index:"authGubunNms", width:120, align:"left", label:"<spring:message code="word.auth"/>",
							formatter:function(cellvalue, options, rowObject){
								if(rowObject.pgmLevelId < 4) return "";
								return "<div style='width:90%;text-overflow:ellipsis;white-space:nowrap;overflow:hidden;float:left;'>"+cellvalue+"</div>" + "<a href='#' onclick='popRoleInfo(\"" + options.rowId + "\");return false;'><img src='${img_path}/icon_search.gif'/></a>";
							},
							unformat:function(cellValue, options, cell){return cellValue;}
						},
						/*
						{name:"authGubunNms",	index:"authGubunNms", width:120, label:"<spring:message code="word.auth"/>",
							editable:true, 
							edittype:'custom',
							editoptions:{custom_element:multiElement, 
								         custom_value:multiVal, 
								         value:getAdminSelect()
					        }
						},
						*/
						{name:"authGubuns",	index:"authGubuns", hidden:true},
						{name:"sortOrder",	index:"sortOrder",	width:80,	align:"center",	label:"<spring:message code="word.sortOrder"/>",
							editable:true, edittype:"text", editrules:{required:true,integer:true}, editoptions:{}
						},
						{name:"useYn",			index:"useYn",				width:80,	align:"center",		label:"<spring:message code="word.useYn"/>",
							editable:true, edittype:"select", formatter:'select', editrules:{required:true}, editoptions:{value:getUseYnSelect()}
						},
						{name:"hasLeaf",	index:"hasLeaf",	hidden:true},
						{name:"expanded",	index:"expanded",	hidden:true},
						{name:"levelId",	index:"levelId",	hidden:true},
						{name:"guideCnt",	index:"guideCnt",	hidden:true}
						],
		rowNum		: ${jqgrid_rownum_max},
		loadonce 	: true,
		autowidth   : true,
		treeGrid	: true,		     //treegrid 사용여부
		treeGridModel: 'adjacency',  //grid model
		ExpandColumn: 'mainPgmNm',    //펼쳐질 name
		ExpandColClick: true,        //ExpandColumn click 시 펼쳐지게 할것인지.
		tree_root_level: 0,          //최상위 레벨
        jsonReader: {                
           id:"pgmId", // 기준                        //데이터 읽엇을때 부모키와 연관된 key
           repeatitems: false        
        },

		treeReader: {
             level_field:"levelId",   
             parent_id_field:"upPgmId",
             leaf_field:"hasLeaf",
             expanded_field:false
        },
        
        cellEdit	: true,
		cellsubmit  :'clientArray',
		loadComplete : function() {
			
			var $grid = $("#list");
			var ids = $grid.jqGrid("getDataIDs");
			var rowObj;
			$(ids).each(function(idx,rowId) {
				rowObj = $grid.jqGrid("getRowData",rowId);
				if(rowObj.useYn == "N") {
					$grid.find("#" + rowId).css("backgroundColor","#FFBB00");
				}
			});
			
			$("table#list").find("tr td span").eq(0).click();
		}
	});
	
	$("body").on("blur", ".customInput input", function(e) {
		$(this).unbind("blur");
	}).on("click", "input[id$=_pgmNm]", function(e) {
		e.stopPropagation();
	}).on("click", function(e) {
		// 코드명 수정 중 코드명 외의 다른 부분을 클릭했을 경우 해당 코드명 저장 처리
		var attr = removeNull($(e.target).attr("aria-describedby"));
		if(attr != "list_pgmNm") {
			if($("#list").find("input[id$=_pgmNm]").length > 0) {
				$("#list").find("input[id$=_pgmNm]").closest("tr").click();
			}
		}
	});
	
});

function popRoleInfo(rowId){
	var rowObj = $("#list").jqGrid("getRowData",rowId);
	var data = {
			"findYear":$("#form #findYear").val(),
			"pgmId":rowObj.pgmId,
			"targetRowId":rowId,
			"_csrf":getCsrf("form")
	}
	openFancybox({
		"url" : "${context_path}/system/system/comp/compMenuMng/popRoleList.do",
		"data" : data
	});
}

function getMultiInput(value,options){
	var gridIds = options.id.split("_");
	var dataIds = $("#list").jqGrid("getDataIDs");
	
	var id = dataIds[Number(gridIds[0])-1];
	var valuesText = $("#list #"+id).find("td[aria-describedby='list_pgmNmLang']").text();
	
	var values = {};
	if(valuesText != ''){
		values = valuesText.split("$%^");
	}
	
	var inputtext = "";
	var langValues = {};
	var valueText;
	
	inputtext+="<div class=\"customInput\">";
	<c:forEach var="langList" items="${langList}" varStatus="status">
		valueText = "";
		if(values.length != undefined){
			for(var i=0 ; i<values.length ; i++){
				langValues = values[i].split("#$%");
				
				if(langValues[0] == "${langList.lang}"){ 
					valueText=langValues[1];
					break;
				}
			}
		}
		
	
		inputtext+="<span class='langNm'>${langList.langNm}</span><input id=\"${langList.lang}_"+options.id+"\" type=\"text\" value=\""+valueText+"\" style=\"width:100%;height:30px;\"/>";
		<c:if test="${not status.last}">
			inputtext += "</br>";
		</c:if>
	</c:forEach>
	inputtext+="</div>";
	return inputtext;
}

function getMultiValue(elem, operation, value){
	
	var $elems = $(elem).find("input");
	var result = "";
	var bindResult = {};
	var bindResultStr = "";
	if(0<$elems.length){
		for(var i=0 ; i<$elems.length ; i++){
			if("${searchVO.lang}" == $elems.eq(i).attr("id").split('_')[0]){
				result = $elems.eq(i).val();
			}
			bindResult[$elems.eq(i).attr("id").split('_')[0]] = $elems.eq(i).val();
			bindResultStr += $elems.eq(i).attr("id").split('_')[0]+"#$%"+$elems.eq(i).val()+"$%^";
		}
	}
	
	var gridId = id = $elems.eq(0).attr("id").split('_')[1];
	var dataIds = $("#list").jqGrid("getDataIDs");
	var id = dataIds[Number(gridId)-1];
	
	$("#list #"+id).find("td[aria-describedby='list_pgmNmLang']").text(bindResultStr);
	$("#list #"+id).find("td[aria-describedby='list_pgmNmLang']").attr("title",bindResultStr);
	
	var $tr = $(elem).parents("tr:first");
	$tr.addClass("edited");
	$("#list #"+id).find("td[aria-describedby='list_pgmNmLang']").addClass("dirty-cell");
	
	
	return result;
}

function addRow(pgmId,pgmLevelId,pgmNm,rowId){
	
	var newId="newRow"+$.jgrid.guid++;
	
	if(pgmId != ''){
		var rowData = {upPgmId:pgmId,
				       upPgmNm:pgmNm,
				       pgmLevelId:Number(pgmLevelId)+1,
				       mainPgmNm:pgmNm+" add Menu as child.",
		               useYn:'Y',
		               adminGubuns:'',
		               authGubunNms:'',
		               hasLeaf:true}
			
		$("#list").jqGrid("addRowData", (newId), rowData, "after", rowId);
		$("#"+newId).find("td > a").eq(0).attr("onclick","delRow(\""+newId+"\")").text("-");
	}
}

function delRow(rowId){
	$("#list").jqGrid("delRowData", rowId);
}

function getAdminSelect(){
	var selectStr="";
	<c:forEach var="adminList" items="${codeUtil:getCodeList('018')}" varStatus="status">
		selectStr += "<c:out value="${adminList.codeId}"/>"+":"+"<c:out value="${adminList.codeNm}"/>";
		<c:if test="${not status.last}">
			selectStr += ";";
		</c:if>
	</c:forEach>
	
	return selectStr;
}

function getUseYnSelect(){
	
	var selectStr="";
	<c:forEach var="useYnList" items="${codeUtil:getCodeList('011')}" varStatus="status">
		selectStr += "<c:out value="${useYnList.codeId}"/>"+":"+"<c:out value="${useYnList.codeNm}"/>";
		<c:if test="${not status.last}">
			selectStr += ";";
		</c:if>
	</c:forEach>
	
	return selectStr;
}

//그리드 조회
function searchList(pageNum) {
	if(isEmpty(pageNum)) {
		pageNum = parseInt($("#page").val(), 10);
	}
 	reloadGrid("list", "form", pageNum);
} 

//저장
function saveData() {
	if(!gridToFormChanged("list", "form", true)) return false;
	
	sendAjax({
		"url" : "${context_path}/system/system/comp/compMenuMng/saveCompMenuMng.do",
		"data" : getFormData("form"),
		"doneCallbackFunc" : "checkResult"
	});
}

function checkResult(data) {
	$.showMsgBox(data.msg);
	if(data.result == AJAX_SUCCESS) {
		searchList();
	}
}

//상세 조회
function popGuideComment(rowId) {
	var rowData = $("#list").jqGrid("getRowData", rowId);
	var f = document.form;
	f.pgmId.value = rowData.pgmId;
	
	openFancybox({
		"url" : "${context_path}/system/system/comp/compMenuMng/popGuideComment.do",
		"data" : getFormData("form")
	});
	
}

</script>

<form:form commandName="searchVO" id="form" name="form" method="post">
    <form:hidden path="pgmId"/>
<%-- 	<div class="sch-bx">
		<ul>
			<li>
				<label for="findUseYn"><spring:message code="word.useYn"/></label>
				<form:select path="findUseYn" class="select" items="${codeUtil:getCodeList('011')}" itemLabel="codeNm" itemValue="codeId">
				</form:select>
			</li>
		</ul>
		<a href="#" class="btn-sch" onclick="searchList();return false;"><spring:message code="button.search"/></a>
	</div> --%>
	<div class="btn-dw">
	    <%--
		<a href="#" class="excel" onclick="excelDownload();return false;"><span><spring:message code="button.excelDownload"/></span></a>
		--%>
	</div>
	<div class="gridContainer">
		<table id="list"></table>
		<div id="pager"></div>
	</div>
	<div class="tbl-bottom tbl-bottom2 mt0">
		<div class="tbl-btn">
			<a href="#" class="save" onclick="saveData();return false;"><spring:message code="button.save"/></a>
		</div>
	</div>
</form:form>

