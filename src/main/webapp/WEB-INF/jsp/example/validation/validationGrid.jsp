<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/common-taglibs.jsp"%>
<validator:javascript formName="exampleVO" staticJavascript="false" xhtml="true" cdata="false"/>
<script type="text/javascript">
$(function(){
	setMaxLength("form");
	$("#form [name=sortOrder]").numericOnly();
	
	/******************************************
	* 데이터 그리드 출력
	******************************************/
	$("#list").jqGrid({
		url			:	"${context_path}/example/grid/gridList_json.do",
		postData	:	getFormData("form"),
		width		:	"${jqgrid_width}",
		height		:	"200px",
		colModel	:	[
							{name:"year",			index:"year",			hidden:true,	label:"<spring:message code="word.year2"/>"},
							{name:"perspectiveId",	index:"perspectiveId",	width:100,		align:"center",	label:"<spring:message code="word.perspectiveCode"/>"},
							{name:"perspectiveNm",	index:"perspectiveNm",	width:200,		align:"left",	title:true,	label:"<spring:message code="word.perspective"/>",
								editable:true,		editrules:{custom:true, custom_func:jqGridChkBytes, required:true},	editoptions:{maxlength:150}
							},
							{name:"etc",			index:"etc",			width:200,		align:"left",	title:true,	label:"<spring:message code="word.etc"/>",
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
		rowNum		: ${jqgrid_rownum_max},
		pager		: "pager",
		sortname	: "sortOrder",
		sortorder	: "asc",
		loadonce	: true,
		cellEdit	: true
	});
});

// 사용자 검색 formatter
function selectUserFormatter(cellvalue, options, rowObject) {
	return removeNull(cellvalue) + " <a href='#' onclick='selectUser(\"" + options.rowId + "\");return false;'><img src='${img_path}/icon_search.gif'/></a>";
}

// 사용자 검색 formatter
function selectUserFormatter(cellvalue, options, rowObject) {
	return removeNull(cellvalue) + " <a href='#' onclick='popSearchUserForGrid(\"list\", \"" + options.rowId + "\", \"userId\", \"userNm\");return false;'><img src='${img_path}/icon_search.gif'/></a>";
}

// 추가
function addData() {
	$("#list").jqGrid("addRowData", ("newRow" + $.jgrid.guid++), {"year" : $("#form [name=findYear]").val(), "useYn":"Y"}, "last");
	$("#list").jqGrid("scrollToBottom");
}

// 저장
function saveData() {
	if(!validateExampleVO(document.form)) {
		return;
	}

	if(!gridToForm("list", "form", true)) return false;

	sendAjax({
		"url" : "${context_path}/example/validation/validationGridSave.do",
		"data" : getFormData("form"),
		"doneCallbackFunc" : "checkResult"
	});
}

function checkResult(data) {
	$.showMsgBox(data.msg);
	reloadGrid("list", "form");
}
</script>

<form:form commandName="searchVO" id="form" name="form" method="post">
	<form:hidden path="findYear" value="2016"/>
	<div class="sch-bx2">
	</div>
	<div class="tbl-type02">
		<table summary="">
			<caption>전략목표 등록/수정</caption>
			<tbody>
				<tr>
					<th scope="row"><label for="perspectiveNm">관점명<span class="red">(*)</span></label></th>
					<td><form:input path="perspectiveNm" maxlength="30" class="t-box03"/> (필수, 30bytes)</td>
				</tr>
				<tr>
					<th scope="row"><label for="userId">담당자ID<span class="red">(*)</span></label></th>
					<td><form:input path="userId" class="t-box03"/> (필수)</td>
				</tr>
				<tr>
					<th scope="row"><label for="userNm">담당자명</label></th>
					<td><form:input path="userNm" class="t-box03"/></td>
				</tr>
				<tr>
					<th scope="row"><label for="sortOrder">정렬순서</label></th>
					<td><form:input path="sortOrder" maxlength="3" class="t-box03"/> (숫자, 3자리)</td>
				</tr>
			</tbody>
		</table>
	</div>
	
	<div class="gridContainer mt30">
		<table id="list"></table>
		<div id="pager"></div>
	</div>
	<div class="tbl-bottom tbl-bottom2">
		<div class="tbl-btn">
			<a href="#" class="new" onclick="addData();return false;">추가</a>
			<a href="#" class="save" onclick="saveData();return false;">저장</a>
		</div>
	</div>
</form:form>
<div class="devGuide">
	<h3>Client-side 유효성 체크 - 폼 & jqGrid</h3>
	<ol>
		<li>
			<dl>
				<dt>
					VO별 Validator 선언
				</dt>
				<dd>
					/src/main/resources/validator 경로 아래에 VO별로 ~~~Validator.xml을 선언해야 함
				</dd>
			</dl>
		</li>
		<li>
			<dl>
				<dt>
					form 유효성 체크
				</dt>
				<dd>
					<span class="devGuideSource">&lt;validator:javascript formName="exampleVO" staticJavascript="false" xhtml="true" cdata="false"/></span>	// jsp 상단에 form에 맞는 VO 선언하면 
				<br/><span class="devGuideSource">validateExampleVO(document.form)</span>	// validate + VO이름에 해당하는 js 함수가 자동생성됨. 이 함수에 form을 전달하면 validation이 적용되어 true(문제 없음), false(유효성 오류 있음)가 리턴됨
				</dd>
			</dl>
		</li>
		<li>
			<dl>
				<dt>
					grid 유효성 체크
				</dt>
				<dd>
					<span class="devGuideSource">gridToForm("list", "form", true)</span>	// grid id, form id, 유효성 체크 실행 여부
					※ grid의 colModel 설정에서 각 열에 맞는 editrules, editoptions 설정 필요
				</dd>
			</dl>
		</li>
		<li>
			<dl>
				<dt>
					저장 처리 & callback
				</dt>
				<dd>
					<span class="devGuideSource">function saveData() {</span>
					<span class="devGuideSource"><br/><span class="tab"></span>if(!gridToForm("list", "form", true)) return false;</span>	// id가 list인 grid의 전체 값들을 form에 넣고 유효성을 체크함. 유효성 체크를 통과하면 true, 아니면 false를 리턴
					<span class="devGuideSource"><br/><span class="tab"></span>sendAjax({"url" : "&#36{context_path}/example/grid/gridSaveProcess.do",</span>	// 저장 처리할 url
					<span class="devGuideSource"><br/><span class="tab"></span><span class="tab"></span>"data" : getFormData("form"),</span>	// form의 form 값을 ajax 데이터로 생성
					<span class="devGuideSource"><br/><span class="tab"></span><span class="tab"></span>"doneCallbackFunc" : "checkResult"});</span>	// ajax 호출 성공시 checkResult 함수 호출
					<span class="devGuideSource"><br/>}</span>
				</dd>
			</dl>
		</li>
		<li></li>
	</ol>
	<h3>Server-side 유효성 체크 - 폼 & jqGrid</h3>
	<ol>
		<li>
			<dl>
				<dt>
					form 체크
				</dt>
				<dd>
					<span class="devGuideSource">beanValidator.validate(dataVO, bindingResult);</span>
				</dd>
			</dl>
		</li>
		<li>
			<dl>
				<dt>
					grid 체크
				</dt>
				<dd>
					<span class="devGuideSource">validateList(dataVO.getGridDataList(), bindingResult);</span>
				</dd>
			</dl>
		</li>
		<li></li>
	</ol>
	<br/>
</div>
