<!-- 
*************************************************************************
* CLASS 명	: MenuDeployMngList
* 작 업 자	: 현걸욱
* 작 업 일	: 2018-07-06
* 기	능	: 메뉴배포관리 List
* ---------------------------- 변 경 이 력 --------------------------------
* 번호	작 업 자		작	업	일			변 경 내 용				비고
* ----	---------	----------------	---------------------	-----------
*	1	현걸욱		2018-07-06				최 초 작 업
**************************************************************************
-->

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/common-taglibs.jsp"%>
<validator:javascript formName="menuMngVO" staticJavascript="false" xhtml="true" cdata="false"/>
<script type="text/javascript">
$(function(){
	
	$("#list").jqGrid({
		url			:	"${context_path}/superMng/superMng/superMng/menuDeployMng/menuDeployMngList_json.do",
		postData	:	getFormData("form"),
		width		:	"${jqgrid_width}",
		height		:	"500",
		colModel	:	[
						{name:"pgmId",		index:"pgmId",		width:100,	hidden:true},
						{name:"mainPgmNm",	index:"mainPgmNm",  width:200, 	align:"left",	label:"<spring:message code="word.menu"/>"},
						/*
						{name:"pgmNm",	    index:"pgmNm",		width:150, 	align:"left",	label:"<spring:message code="word.menu"/>"},
						*/
						{name:"upPgmId",	index:"upPgmId",    hidden:true},
						{name:"upPgmNm",	index:"upPgmNm",	hidden:true},
						{name:"pgmLevelId",	index:"pgmLevelId",	hidden:true},
						{name:"urlPattern",	index:"urlPattern", hidden:true},
						{name:"pgmGbnId",	index:"pgmGbnId",		width:80,	align:"center",	label:"<spring:message code="word.division"/>",
							editable:true, edittype:"select", formatter:"select", editroles:{required:true}, 
							editoptions:{value:getDeployGbnSelect()}
						},
						{name:"deployTargetYn",		index:"deployTargetYn",		width:80,	align:"center",	label:"<spring:message code="word.deployObjectYn"/>",
							editable:true, edittype:"select", formatter:"select", editroles:{required:true}, 
							editoptions:{value:getDeployObjectYnSelect()}
						},
						{name:"deployCnt",		index:"deployCnt",		width:80,	align:"center",	label:"<spring:message code="word.deployObjectCnt"/>",
							formatter:function(cellvalue, options, rowObject) {
								return "<a href='#' onclick='popMenuDeployPage(\"" + removeNull(rowObject.pgmId) + "\",\"" + removeNull(rowObject.deployTargetYn) + "\");return false;'>" + escapeHTML(removeNull(rowObject.deployCnt))+"/"+escapeHTML(removeNull(rowObject.compCnt)) + "</a>";
							},
							unformat:linkUnformatter
						},
						{name:"compCnt",	index:"compCnt",	hidden:true},
						{name:"hasLeaf",	index:"hasLeaf",	hidden:true},
						{name:"expanded",	index:"expanded",	hidden:true},
						{name:"levelId",	index:"levelId",	hidden:true}
						],
		rowNum		: ${jqgrid_rownum_max},
		loadonce 	: false,
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
             expanded_field:true
        },
        
        cellEdit	: true,
		cellsubmit  :'clientArray',
		beforeSaveCell : function(rowid, cellname, value, iRow, iCol){
			
			if(cellname == "deployTargetYn"){
				
				var $g =  $("#list");
				var recodeChilds;
				var recodeParent;
				var arrays = [];
				var tarrays = [];
				arrays.push(rowid);
				
				if(value === "Y"){
					
					recodeParent = $g.jqGrid("getNodeParent", $g.jqGrid("getLocalRow",rowid));
					if(recodeParent.deployTargetYn === "N"){
						$.showMsgBox("<spring:message code="errors.upMenuNotUse"/>");
						value="N";
					}
				}else{
					while(arrays.length > 0){
						for(var i=0 ; i<arrays.length ; i++){
							recodeChilds = $g.jqGrid("getNodeChildren", $g.jqGrid("getLocalRow",arrays[i]));
							$(recodeChilds).each(function(i,e){
								$g.setCell(e._id_,"deployTargetYn",value);
								$g.setRowData(e._id_,false,'edited');
								if(e.hasLeaf != 'true'){
									tarrays.push(e._id_);
								}
							});
						}
						arrays = [];
						arrays = tarrays;
						tarrays = [];
					}
				}
			}
			
			return value;
		},
		loadComplete : function() {
			var $grid = $("#list");
			var ids = $grid.jqGrid("getDataIDs");
			var rowObj;
			$(ids).each(function(idx,rowId) {
				rowObj = $grid.jqGrid("getRowData",rowId);
				if(rowObj.deployTargetYn == "N") {
					$grid.find("#" + rowId).css("backgroundColor","#FFDDDD");
				}
				
			});
		},
		gridComplete: function(){
			$("table#list").find("tr td span").eq(0).click();
		}
	});
});


/*
function closeAll(){
	var data = $("#list").jqGrid('getGridParam','data'); 
	if (data[0]) { 
		for (i=0;i<data.length;i++) { 
			$("#list").jqGrid('collapseRow',data[i]); 
			$("#list").jqGrid('collapseNode',data[i]); 
		}
	}
}

function expandAll(){
	var data = $("#list").jqGrid('getGridParam','data'); 
	if (data[0]) { 
		for (i=0;i<data.length;i++) { 
			$("#list").jqGrid('expandRow',data[i]); 
			$("#list").jqGrid('expandNode',data[i]); 
		}
	}
}
*/


function getDeployGbnSelect(){
	var selectStr="";
	<c:forEach var="gbnList" items="${codeUtil:getCodeList('002')}" varStatus="status">
		selectStr += "<c:out value="${gbnList.codeId}"/>"+":"+"<c:out value="${gbnList.codeNm}"/>";
		<c:if test="${not status.last}">
			selectStr += ";";
		</c:if>
	</c:forEach>
	
	return selectStr;
}

function getDeployObjectYnSelect(){
	var selectStr="";
	<c:forEach var="deployList" items="${codeUtil:getCodeList('001')}" varStatus="status">
		selectStr += "<c:out value="${deployList.codeId}"/>"+":"+"<c:out value="${deployList.codeNm}"/>";
		<c:if test="${not status.last}">
			selectStr += ";";
		</c:if>
	</c:forEach>
	
	return selectStr;
}

function popMenuDeployPage(pgmId,deployTargetYn){
	
	document.form.pgmId.value=pgmId;
	document.form.deployTargetYn.value=deployTargetYn;
	
	openFancybox({
		"url" : "${context_path}/superMng/superMng/superMng/menuDeployMng/popMenuDeployList.do",
		"data" : getFormData("form")
	});
}

//저장
function saveData() {
	if(!gridToFormChanged("list", "form", true)) return false;
	
	sendAjax({
		"url" : "${context_path}/superMng/superMng/superMng/menuDeployMng/saveMenuDeployMng.do",
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

function searchList(){
	reloadGrid("list","form");
}

function moveToMenu(){
	
	loadPage("${context_path}/superMng/superMng/superMng/menuDeployMng/menuDeployList.do","form");
	
	/*
	sendAjax({
		"url" : "${context_path}/superMng/superMng/superMng/menuDeployMng/menuDeployList.do",
		"data" : getFormData("form")
	});
	*/
}

function saveDeployDataDo(){
	sendAjax({
		"url" : "${context_path}/superMng/superMng/superMng/menuDeployMng/updateDeploy.do",
		"data" : getFormData("form"),
		"doneCallbackFunc" : "checkResult"
	});
}

</script>

<form:form commandName="searchVO" id="form" name="form" method="post">
    <form:hidden path="pgmId"/>
    <form:hidden path="tempCompIds"/>
    <form:hidden path="deployTargetYn"/>
	<%--
	<div class="sch-bx">
		<ul>
			<li>
				<label for="findUseYn"><spring:message code="word.useYn"/></label>
				<form:select path="findUseYn" class="select" items="${codeUtil:getCodeList('011')}" itemLabel="codeNm" itemValue="codeId">
				</form:select>
			</li>
		</ul>
		<a href="#" class="btn-sch" onclick="searchList();return false;"><spring:message code="button.search"/></a>
	</div>
	--%>
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
			
			<%--
			<a href="#" class="delete" onclick="closeAll();return false;">다접어</a>
			<a href="#" class="delete" onclick="expandAll();return false;">다펼쳐</a>
			--%>
		
			<a href="#" class="delete" onclick="moveToMenu();return false;"><spring:message code="word.menuManage"/></a>
			<a href="#" class="save" onclick="saveData();return false;"><spring:message code="button.saveAll"/></a>
		</div>
	</div>
</form:form>



