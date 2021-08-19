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
		rowNum		:	"${jqgrid_rownum_max}",
		colModel	:	[
							{name:"year",			index:"year",			hidden:true,	label:"<spring:message code="word.year2"/>"},
							{name:"perspectiveId",	index:"perspectiveId",	width:100,		align:"center",	label:"<spring:message code="word.perspectiveCode"/>",
								formatter:showDetailLinkFormatter},
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
		sortname	: "sortOrder",
		sortorder	: "asc"
	});
});

function showDetail(val) {
	$.showMsgBox(val);
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
					grid url에 파라미터 전송
				</dt>
				<dd>
					<span class="devGuideSource">postData : getFormData("formFind")</span>
					<br/>
					id가 formFind인 form의 input값들을 serialize()해서 url 호출시 post로 보냄
				</dd>
			</dl>
		</li>
		<li>
			<dl>
				<dt>
					데이터 전체 조회시
				</dt>
				<dd>
					<span class="devGuideSource">rowNum		:	"&#36{jqgrid_rownum_max}",</span>
					<br/>
					rowNum을 &#36{jqgrid_rownum_max}로 설정할 것
				</dd>
			</dl>
		</li>
		<li>
			<dl>
				<dt>
					'관점코드' 값 클릭시 함수 호출
				</dt>
				<dd>
					<span class="devGuideSource">colModel > formatter : showDetailLinkFormatter</span>
					<br/>
					'showDetail' 함수를 호출하게 되고 해당 열의 값을 매개변수로 전달함 (위 grid의 '관점코드')
					<br/>
					※ 각 페이지별로 <b>showDetail</b> 함수를 작성해야함.
					<br/>
					※ 저장 등 해당 열의 값을 사용해야할 경우 <b>unformat:linkUnformatter</b> 도 같이 선언해줘야 함
				</dd>
			</dl>
		</li>
		<li>
			<dl>
				<dt>
					'기타'열은 클릭해도 정렬이 되지 않음
				</dt>
				<dd>
					<span class="devGuideSource">colModel > sortable:false</span> 인 열은 클릭해도 정렬하지 않음
				</dd>
			</dl>
		</li>
		<li>
			<dl>
				<dt>
					'예산금액'의 금액 Formatter
				</dt>
				<dd>
					<span class="devGuideSource">colModel > formatter:"number",	formatoptions:{thousandsSeperator:",", decimalPlaces:DECIMAL_SCALE, defaultValue:0, removePointZeros:REMOVE_POINT_ZEROS}</span>
					<br/>
					세 자리 마다 콤마 표시, 소수점 세자리까지 표시&반올림, 소수점 끝이 0이면 생략 처리
				</dd>
			</dl>
		</li>
		<li></li>
	</ol>
	<br/>
</div>
