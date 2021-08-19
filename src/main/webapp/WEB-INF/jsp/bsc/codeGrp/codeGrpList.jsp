<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/common-taglibs.jsp"%>
<script type="text/javascript">
	$(function(){
		/******************************************
		 * 데이터 그리드 출력
		 ******************************************/
		$("#list").jqGrid({
			url          :"${context_path}/bsc/codeGrp/codeGrpList_json.do",
			mtype        :"POST",
			datatype     :"json",
			postData	:	getFormData("formFind"),
			jsonReader   : {
    						page   : "page",
    						total  : "total",
    						root   : "rows",
    						records: function(obj){return obj.length;},
    						repeatitems: false,
    						id     : "id"
    				       },
			height       : "400",
			width        : "100%",
			colNames     :['<spring:message code="word.codeGroupCd"/>', '<spring:message code="word.codeGroup"/>','<spring:message code="word.codeGroupNm"/>','<spring:message code="word.yearlyManageYn"/>' ,'<spring:message code="word.codeCnt"/>'],
			colModel     :[
							{name:'codeGrpId'   ,index:'codeGrpId'   ,width:160   ,align:'center' },
							{name:'codeGrpNm'   ,index:'codeGrpNm'   ,width:250   ,align:'left' },
							{name:'codeNm'      ,index:'codeNm'      ,width:230   ,align:'center' },
							{name:'yearYn'      ,index:'yearYn'      ,width:230   ,align:'center' },
							{name:'cnt'         ,index:'cnt'         ,width:227   ,sorttype:'int',align:'center' }
							],
			rowNum       : "${jqGrid_rownum}",
			autowidth    : true,
			viewrecords  : true,
			loadonce     : true,
			multiselect  : true,
			cellEdit     : true,
			sortable     : false,
			loadComplete : function() {}
		});
	});

</script>

<div>
	<form:form commandName="searchVO" id="formFind" name="formFind" method="post" action="${root}/bsc/codeGrp/codeGrpList.do">
		<div class="gridContainer">
			<table id="list"></table>
			<div id="pager"></div>
		</div>
	</form:form>
</div>
