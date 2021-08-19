<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/common-taglibs.jsp"%>
<script type="text/javascript">
$(function(){
	/******************************************
	* 데이터 그리드 출력
	******************************************/
	$("#list").jqGrid({
		url			:	"${context_path}/example/grid/gridList_json.do",
		postData	:	getFormData("formFind"),
		width		:	"${jqgrid_width}",
		height		:	"${jqgrid_height}",
		rowNum		:	${jqgrid_rownum},
		colModel	:	[
							{name:"year",			index:"year",			hidden:true,	label:"<spring:message code="word.year2"/>"},
							{name:"perspectiveId",	index:"perspectiveId",	width:100,		align:"center",	label:"<spring:message code="word.perspectiveCode"/>"},
							{name:"perspectiveNm",	index:"perspectiveNm",	width:200,		align:"left",	label:"<spring:message code="word.perspective"/>"},
							{name:"etc",			index:"etc",			width:200,		align:"left",	label:"<spring:message code="word.etc"/>", sortable:false},
							{name:"userId",			index:"userId",			width:100,		align:"center",	label:"<spring:message code="word.inChargeId"/>"},
							{name:"userNm",			index:"userNm",			width:100,		align:"center",	label:"<spring:message code="word.inCharge"/>"},
							{name:"sortOrder",		index:"sortOrder",		width:100,		align:"center",	label:"<spring:message code="word.sortOrder"/>", sorttype:"number"},
							{name:"useYn",			index:"useYn",			width:100,		align:"center",	label:"<spring:message code="word.useYn"/>"},
							{name:"createDt",		index:"createDt",		width:100,		align:"center",	label:"<spring:message code="word.insertDT"/>"},
							{name:"amt",			index:"amt",			width:100,		align:"right",	label:"<spring:message code="word.budget"/>", sorttype:"number",
								formatter:"number",	formatoptions:{thousandsSeperator:",", decimalPlaces:DECIMAL_SCALE, defaultValue:0, removePointZeros:REMOVE_POINT_ZEROS}}
						],
		pager		: "pager",
		sortname	: "sortOrder",
		sortorder	: "asc"
	});
});

</script>

<form:form commandName="searchVO" id="formFind" name="formFind" method="post">
	<div class="sch-bx">
		<ul>
			<li>
				<label for="findYear"><spring:message code="word.year"/></label>
				<form:select path="findYear" class="select wx100" items="${codeUtil:getCodeList('017')}" itemLabel="codeNm" itemValue="codeId">
				</form:select>
			</li>
			<li>
				<div class="sch-oga" id="findScDeptList"></div>
			</li>
			<li>
				<label for="findUseYn"><spring:message code="word.useYn"/></label>
				<form:select path="findUseYn" class="select" items="${codeUtil:getCodeList('011')}" itemLabel="codeNm" itemValue="codeId">
				</form:select>
			</li>
		</ul>
		<a href="#" class="btn-sch" onclick="reloadGrid('list');return false;">검색</a>
	</div>
	<div class="btn-dw">
	</div>
	<div class="gridContainer">
		<table id="list"></table>
		<div id="pager"></div>
	</div>
	<div class="cboth"></div>
</form:form>
<div class="devGuide">
	<h3>기능 관련 jqGrid 설정</h3>
	<ol>
		<li>
			<dl>
				<dt>
					jqGrid 자체 페이징
				</dt>
				<dd>
					<span class="devGuideSource">rowNum		:	"&#36{jqgrid_rownum}"</span>
					<br/>
					<span class="devGuideSource">pager		: "pager"</span>
					<br/>
					DB에서 데이터를 전체 조회하고 jqGrid의 rowNum을 &#36{jqgrid_rownum}로 설정하면 정한 row수만큼 나눠서 표시됨.
					<br/>
					<b>※ pager를 적용할 &lt;div id="pager">를 선언해야 함.</b>
				</dd>
			</dl>
		</li>
		<li></li>
	</ol>
</div>
