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
							{name:"userNm",			index:"userNm",			width:100,		align:"center",	label:"<spring:message code="word.inCharge"/>",
								formatter:selectUserFormatter, unformat:linkUnformatter},
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
</form:form>
<div class="devGuide">
	<h3>기능 관련 jqGrid 설정</h3>
	<ol>
		<li>
			<dl>
				<dt>
					수정 가능 여부 설정
				</dt>
				<dd>
					<span class="devGuideSource">colModel > editable:true</span>
					<br/>
					colModel의 editable 속성이 true면 수정 가능, false거나 생략하면 수정 불가 
				</dd>
			</dl>
		</li>
		<li>
			<dl>
				<dt>
					textarea용 formatter
				</dt>
				<dd>
					<span class="devGuideSource">colModel > formatter:textareaFormatter</span>
					<br/>
					행 높이 제한 + 스크롤바 생성 
				</dd>
			</dl>
		</li>
		<li>
			<dl>
				<dt>
					byte 길이 체크
				</dt>
				<dd>
					<span class="devGuideSource">colModel > editrules:{custom:true, custom_func:jqGridChkBytes},	editoptions:{maxlength:150}</span>
					<br/>
					colModel의 editrules는 위와 같이 설정, editoptions의 maxlength에 입력 가능한 byte 설정
					<br/>
					※ editrules 없이 editoptions의 maxlength만 설정하면 byte 길이 체크를 하지 않고 글자수 체크만 적용됨.
				</dd>
			</dl>
		</li>
		<li>
			<dl>
				<dt>
					필수 입력값 설정
				</dt>
				<dd>
					<span class="devGuideSource">colModel > editrules:{required:true}</span>
				</dd>
			</dl>
		</li>
		<li>
			<dl>
				<dt>
					팝업 호출 & 선택값 입력
				</dt>
				<dd>
					<span class="devGuideSource">colModel > formatter:selectUserFormatter</span>
					<br/>
					selectUserFormatter를 참고해서 해당 기능을 구현해야 함. 
				</dd>
			</dl>
		</li>
		<li>
			<dl>
				<dt>
					select box 표시
				</dt>
				<dd>
					<span class="devGuideSource">colModel > edittype:"select",
					<br/>formatter:"select",
					<br/>editoptions:{value:{
					<br/>&nbsp;&nbsp;&nbsp;&nbsp;&lt;c:forEach var="item" items="&#36{codeUtil:getCodeList('011')}" varStatus="status">"&#36{item.codeId}":"&#36{item.codeNm}"&lt;c:if test="&#36{not status.last}">,&lt;/c:if>&lt;/c:forEach>
					<br/>}}</span>
				</dd>
			</dl>
		</li>
		<li>
			<dl>
				<dt>
					날짜 입력 설정
				</dt>
				<dd>
					<span class="devGuideSource">colModel > editoptions:jqGridDatepickerEditoptions</span>
				</dd>
			</dl>
		</li>
		<li>
			<dl>
				<dt>
					금액 Formatter
				</dt>
				<dd>
					<span class="devGuideSource">colModel > formatter:"number",	formatoptions:{thousandsSeperator:",", decimalPlaces:DECIMAL_SCALE, defaultValue:0, removePointZeros:REMOVE_POINT_ZEROS}</span>
					<br/>
					세 자리 마다 콤마 표시, 소수점 세자리까지 표시&반올림, 소수점 끝이 0이면 생략 처리
				</dd>
			</dl>
		</li>
		<li>
			<dl>
				<dt>
					Cell Edit 기능 사용
				</dt>
				<dd>
					<span class="devGuideSource">cellEdit	: true</span>
				</dd>
			</dl>
		</li>
		<li>
			<dl>
				<dt>
					조건부 체크박스 표시
				</dt>
				<dd>
					<span class="devGuideSource">loadComplete	: function() {
					<br/><span class="tab"></span>hideGridCheckbox("list", "useYn", "N", true);</span>	// grid ID, 비교할 열의 name, 해당 열의 비교할 값, true:일치하면 체크박스 숨김, false:일치하지 않으면 숨김
					<br/><span class="devGuideSource">}</span>
				</dd>
			</dl>
		</li>
		<li></li>
	</ol>
</div>
