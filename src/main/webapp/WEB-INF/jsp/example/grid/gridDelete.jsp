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
		colModel	:	[
							{name:"year",			index:"year",			hidden:true,	label:"<spring:message code="word.year2"/>"},
							{name:"perspectiveId",	index:"perspectiveId",	width:100,		align:"center",	label:"<spring:message code="word.perspectiveCode"/>"},
							{name:"perspectiveNm",	index:"perspectiveNm",	width:200,		align:"left",	title:true,	label:"<spring:message code="word.perspective"/>",
								editable:true,		editrules:{custom:true, custom_func:jqGridChkBytes},	editoptions:{maxlength:150}
							},
							{name:"etc",			index:"etc",			width:200,		align:"left",	title:true,	label:"<spring:message code="word.etc"/>",
								formatter:textareaFormatter, unformat:linkUnformatter,
								editable:true,		edittype:"textarea",	editrules:{custom:true, custom_func:jqGridChkBytes},	editoptions:{maxlength:1000}},
							{name:"userId",			index:"userId",			width:100,		align:"center",	label:"<spring:message code="word.inChargeId"/>",
								editrules:{required:true}
							},
							{name:"userNm",			index:"userNm",			width:100,		align:"center",	label:"<spring:message code="word.inCharge"/>", formatter:selectUserFormatter, unformat:linkUnformatter},
							{name:"sortOrder",		index:"sortOrder",		width:100,		align:"center",	title:true,	label:"<spring:message code="word.sortOrder"/>", sorttype:"number",
								editable:true,		editrules:{integer:true}, editoptions:{maxlength:3}
							},
							{name:"useYn",			index:"useYn",			width:100,		align:"center", editable:true,	formatter:"select",	edittype:"select",
								editoptions:{value:{
									<c:forEach var="item" items="${codeUtil:getCodeList('011')}" varStatus="status">"${item.codeId}":"${item.codeNm}"<c:if test="${not status.last}">,</c:if></c:forEach>
								}},	label:"<spring:message code="word.useYn"/>"
							},
							{name:"createDt",		index:"createDt",		width:100,		align:"center",	editable:true,	title:true,	label:"<spring:message code="word.insertDT"/>",
								editoptions:jqGridDatepickerEditoptions
							},
							{name:"amt",			index:"amt",			width:100,		align:"right",	title:true,	label:"<spring:message code="word.budget"/>",
								formatter:"number",	formatoptions:{thousandsSeperator:",", decimalPlaces:DECIMAL_SCALE, defaultValue:0, removePointZeros:REMOVE_POINT_ZEROS},
								editable:true,	editrules:{number:true}
							}
						],
		rowNum		: ${jqgrid_rownum},
		pager		: "pager",
		sortname	: "sortOrder",
		sortorder	: "asc",
		loadonce	: true,
		multiselect	: true,
		cellEdit	: true,
		loadComplete	: function() {
			hideGridCheckbox("list", "useYn", "N", true);
		}
	});
});

// 조회
function searchList(pageNum) {
	if(isEmpty(pageNum)) {
		pageNum = parseInt($("#page").val(), 10);
	}
 	reloadGrid("list2", "formFind2", pageNum);
}

// 사용자 검색 formatter
function selectUserFormatter(cellvalue, options, rowObject) {
	return removeNull(cellvalue) + " <a href='#' onclick='popSearchUserForGrid(\"list\", \"" + options.rowId + "\", \"userId\", \"userNm\");return false;'><img src='${img_path}/icon_search.gif'/></a>";
}

// 추가
function addData() {
	$("#list").jqGrid("addRowData", ("newRow" + $.jgrid.guid++), {"year" : $("#formFind [name=findYear]").val(), "useYn":"Y"}, "last");
	$("#list").jqGrid("scrollToBottom");
}

// 삭제
function deleteData() {
	if(deleteDataToForm("list", "perspectiveId", "formFind")) {
		$.showConfirmBox("<spring:message code="common.delete.msg"/>", "doDeleteData");
	}
}

// 삭제 처리
function doDeleteData() {
	sendAjax({
		"url" : "${context_path}/example/grid/gridDeleteProcess.do",
		"data" : getFormData("formFind"),
		"doneCallbackFunc" : "reloadGrid",
		"doneCallbackArgs" : ["list", "formFind", $("#list").jqGrid("getGridParam", "page")]
	});
}

// 저장
function saveData() {
	if(!gridToForm("list", "formFind", true)) return false;

	sendAjax({
		"url" : "${context_path}/example/grid/gridSaveProcess.do",
		"data" : getFormData("formFind"),
		"doneCallbackFunc" : "reloadGrid",
		"doneCallbackArgs" : ["list", "formFind", $("#list").jqGrid("getGridParam", "page")]
	});
}
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
	<div class="tbl-bottom tbl-bottom2">
		<div class="tbl-btn">
			<a href="#" class="delete" onclick="deleteData();return false;">삭제</a>
		</div>
	</div>
</form:form>
<div class="devGuide">
	<h3>기능 관련 jqGrid 설정</h3>
	<ol>
		<li>
			<dl>
				<dt>
					조건부 체크박스 표시
				</dt>
				<dd>
					<span class="devGuideSource">multiselect	: true,
					<br/>loadComplete	: function() {
					<br/>&nbsp;&nbsp;&nbsp;&nbsp;hideGridCheckbox("list", "useYn", "N", true);</span>	// grid ID, 비교할 열의 name, 해당 열의 비교할 값, true:일치하면 체크박스 숨김, false:일치하지 않으면 숨김
					<br/><span class="devGuideSource">}</span>
				</dd>
			</dl>
		</li>
	</ol>
	<h3>삭제 관련 js 설정</h3>
	<ol>
		<li>
			<dl>
				<dt>
					체크한 값들을 form에 추가
				</dt>
				<dd>
					<span class="devGuideSource">
						function deleteData() {
						<br/>&nbsp;&nbsp;&nbsp;&nbsp;if(deleteDataToForm("list", "perspectiveId", "formFind")) {</span>	// id가 list인 grid에서 체크한 row의 PK에 해당하는 열의 값들을 formFind에 hidden으로 생성해줌
						<span class="devGuideSource"><br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;$.showConfirmBox("<spring:message code="common.delete.msg"/>", "doDeleteData");</span>	// confirmBox에서 '예'를 선택하면 doDeleteData 함수를 호출
						<span class="devGuideSource"><br/>&nbsp;&nbsp;&nbsp;&nbsp;}
						<br/>}
					</span>
				</dd>
			</dl>
		</li>
		<li>
			<dl>
				<dt>
					삭제 처리 & callback
				</dt>
				<dd>
					<span class="devGuideSource">function doDeleteData() {</span>
					<span class="devGuideSource"><br/><span class="tab"></span>sendAjax({"url" : "&#36{context_path}/example/grid/gridDeleteProcess.do",</span>	// 삭제처리할 url
					<span class="devGuideSource"><br/><span class="tab"></span><span class="tab"></span>"data" : getFormData("formFind"),</span>	// formFind의 form 값을 ajax 데이터로 생성
					<span class="devGuideSource"><br/><span class="tab"></span><span class="tab"></span>"doneCallbackFunc" : "reloadGrid",</span>	// ajax 호출 성공시 reloadGrid 함수 호출
					<span class="devGuideSource"><br/><span class="tab"></span><span class="tab"></span>"doneCallbackArgs" : ["list", "formFind", $("#list").jqGrid("getGridParam", "page")]);</span>	// []안의 값들을 인자값으로 전달
					<span class="devGuideSource"><br/>}</span>
				</dd>
			</dl>
		</li>
		<li></li>
	</ol>
</div>
