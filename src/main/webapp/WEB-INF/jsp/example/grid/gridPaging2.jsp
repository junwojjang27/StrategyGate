<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/common-taglibs.jsp"%>
<script type="text/javascript">
$(function(){
	/******************************************
	* 데이터 그리드 출력
	******************************************/
	$("#list").jqGrid({
		url			:	"${context_path}/example/grid/gridListPaging_json.do",
		postData	:	getFormData("formFind"),
		width		:	"${jqgrid_width}",
		height		:	"${jqgrid_height}",
		colModel	:	[
							{name:"year",			index:"year",			hidden:true,	label:"<spring:message code="word.year2"/>"},
							{name:"perspectiveId",	index:"perspectiveId",	width:100,		align:"center",	label:"<spring:message code="word.perspectiveCode"/>"},
							{name:"perspectiveNm",	index:"perspectiveNm",	width:200,		align:"left",	title:true,	label:"<spring:message code="word.perspective"/>"},
							{name:"etc",			index:"etc",			width:200,		align:"left",	title:true,	label:"<spring:message code="word.etc"/>"},
							{name:"sortOrder",		index:"sortOrder",		width:100,		align:"center",	title:true,	label:"<spring:message code="word.sortOrder"/>"},
							{name:"useYn",			index:"useYn",			width:100,		align:"center",	label:"<spring:message code="word.useYn"/>"},
							{name:"createDt",		index:"createDt",		width:100,		align:"center",	title:true,	label:"<spring:message code="word.insertDT"/>"},
							{name:"amt",			index:"amt",			width:100,		align:"right",	title:true,	label:"<spring:message code="word.budget"/>",
								formatter:"number",	formatoptions:{thousandsSeperator:",", decimalPlaces:DECIMAL_SCALE, defaultValue:0, removePointZeros:REMOVE_POINT_ZEROS}
							}
						],
		rowNum		: ${jqgrid_rownum},
		pager		: "pager",
		sortname	: "sortOrder",
		sortorder	: "asc",
		onSortCol	: function(sidx, column, sortOrder) {
					$("#formFind input[name=sidx]").val(sidx);
					$("#formFind input[name=sord]").val(sortOrder);
					searchList(1);
					return "stop";
		},
		onPaging	: function (pgButton) {
					setGridPaging($(this).attr("id"), pgButton, "searchList");
		},
		loadonce	: false
	});
});

// 조회
function searchList(pageNum) {
	if(isEmpty(pageNum)) {
		pageNum = parseInt($("#page").val(), 10);
	}
 	reloadGrid("list", "formFind", pageNum);
}
</script>

<form:form commandName="searchVO" id="formFind" name="formFind" method="post">
	<form:input path="page" class="formHiddenData"/>
	<form:input path="rows" class="formHiddenData"/>
	<form:input path="sidx" class="formHiddenData"/>
	<form:input path="sord" class="formHiddenData"/>
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
		<a href="#" class="btn-sch" onclick="searchList();return false;">검색</a>
	</div>
	<div class="btn-dw">
	</div>
	<div class="gridContainer">
		<table id="list"></table>
		<div id="pager"></div>
	</div>
</form:form>
<div class="devGuide">
	<h3>기능 관련 jqGrid 설정</h3>
	<ol>
		<li>
			<dl>
				<dt>
					DB 쿼리
				</dt>
				<dd>
					<b>※ 쿼리에서 페이징과 정렬을 적용해야 함.</b>
				</dd>
			</dl>
		</li>
		<li>
			<dl>
				<dt>
					페이징, 정렬값 선언
				</dt>
				<dd>
					<span class="devGuideSource">
						&lt;form:input path="page" class="formHiddenData"/&gt;
						<br/>
						&lt;form:input path="rows" class="formHiddenData"/&gt;
						<br/>
						&lt;form:input path="sidx" class="formHiddenData"/&gt;
						<br/>
						&lt;form:input path="sord" class="formHiddenData"/&gt;
					</span>
				</dd>
			</dl>
		</li>
		<li>
			<dl>
				<dt>
					pager
				</dt>
				<dd>
					<span class="devGuideSource">pager		: "pager"</span>
					<br/>
					<b>※ pager를 적용할 &lt;div id="pager">를 선언해야 함.</b>
				</dd>
			</dl>
		</li>
		<li>
			<dl>
				<dt>
					onSortCol, onPaging, loadonce
					<br/>선언 필요
				</dt>
				<dd>
					<span class="devGuideSource">
						onSortCol	: function(sidx, column, sortOrder) {</span>	// 정렬시 호출됨
						<br/><span class="devGuideSource">&nbsp;&nbsp;&nbsp;&nbsp;$("#formFind input[name=sidx]").val(sidx);</span>	// 정렬할 컬럼 name
						<br/><span class="devGuideSource">&nbsp;&nbsp;&nbsp;&nbsp;$("#formFind input[name=sord]").val(sortOrder);</span>	// 정렬 순서 (asc, desc)
						<br/><span class="devGuideSource">&nbsp;&nbsp;&nbsp;&nbsp;searchList(1);</span>	// 조회 함수 호출
						<br/><span class="devGuideSource">&nbsp;&nbsp;&nbsp;&nbsp;return "stop";</span>
						<br/><span class="devGuideSource">},</span>
						<br/><span class="devGuideSource">onPaging	: function (pgButton) {</span>
						<br/><span class="devGuideSource">&nbsp;&nbsp;&nbsp;&nbsp;setGridPaging($(this).attr("id"), pgButton, "searchList");</span>	// 이동할 페이지 설정 후 searchList 함수 호출
						<br/><span class="devGuideSource">},</span>
						<br/><span class="devGuideSource">loadonce	: false</span>
				</dd>
			</dl>
		</li>
		<li>
			<dl>
				<dt>
					조회, 페이징 실행
				</dt>
				<dd>
					searchList 함수에서 처리
				</dd>
			</dl>
		</li>
		<li></li>
	</ol>
</div>
