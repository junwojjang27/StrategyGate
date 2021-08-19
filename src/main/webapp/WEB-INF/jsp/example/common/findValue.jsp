<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/common-taglibs.jsp"%>
<script type="text/javascript">
$(function() {
	$("[name=findAnalCycle][value='" + getFindValue("findAnalCycle") + "']").attr("checked", "checked");
});

function doSearch() {
	loadPage("${root}/example/common/findValue.do", "f");
}

function doSearch2() {
	loadPage("${root}/example/common/findValue.do");
}
</script>
	
<form:form commandName="searchVO" id="f" name="f" method="post">
	<div class="sch-bx">
		<ul>
			<li>
				<label for="findYear"><spring:message code="word.year"/></label>
				<form:select path="findYear" class="select wx100" items="${codeUtil:getCodeList('017')}" itemLabel="codeNm" itemValue="codeId">
				</form:select>
			</li>
			<li>
				<label for="findMon"><spring:message code="word.mon"/></label>
				<form:select path="findMon" class="select" items="${codeUtil:getCodeList('024')}" itemLabel="codeNm" itemValue="codeId">
				</form:select>
			</li>
			<li>
				<label for="findAnalCycle"><spring:message code="word.cycle"/></label>
				<form:radiobuttons path="findAnalCycle" items="${codeUtil:getCodeList('138')}" itemLabel="codeNm"  itemValue="codeId" />
			</li>
			
		</ul>
		<a href="#" class="btn-sch" onclick="reloadGrid('list');return false;">검색</a>
	</div>
	<div class="btn-dw">
		<a href="#" onclick="formToFindValue('f');return false;"><span>전체 검색조건 저장</span></a>
		<a href="#" onclick="setFindValue('findYear', document.f.findYear.value);return false;"><span>기준년도만 저장</span></a>
		&nbsp;&nbsp;
		<a href="#" onclick="doSearch();return false;"><span>페이지 이동 & 전체 검색조건 저장</span></a>
		<a href="#" onclick="doSearch2();return false;"><span>페이지 이동 & 검색조건 저장 안함</span></a>
		
	</div>
</form:form>
	
<div class="devGuide">
	<h3>기능 관련 설명</h3>
	<ol>
		<li>
			<dl>
				<dt>
					검색조건 저장소
				</dt>
				<dd>
					검색조건은 common.js의 gFindValues와 쿠키에 저장된다.<br/>
					- gFindValues : 화면 이동시 검색어를 저장하기 위한 전역변수
					- 쿠키 : 새로고침, 새 탭으로 열기에서도 검색어를 유지하기 위해서 사용 
				</dd>
			</dl>
		</li>
		<li>
			<dl>
				<dt>
					전체 검색조건 저장 (수동)
				</dt>
				<dd>
					<span class="devGuideSource">formToFindValue("formId")</span><br/>
					formToFindValue(formId) 함수를 호출하면 id가 formId인 form에서 name이 find로 시작하는 객체들의 값들을 전역변수와 쿠키에 저장한다.
				</dd>
			</dl>
		</li>
		<li>
			<dl>
				<dt>
					개별 검색조건 저장 (수동)
				</dt>
				<dd>
					<span class="devGuideSource">setFindValue("변수명", "값")</span><br/>
					setFindValue(key, value) 함수를 전역변수와 쿠키에서 해당 변수의 값을 저장한다.
				</dd>
			</dl>
		</li>
		<li>
			<dl>
				<dt>
					전체 검색조건 저장 (자동)
				</dt>
				<dd>
					<span class="devGuideSource">loadPage(url, formId)</span> // 페이지 이동 함수
					<br/><span class="devGuideSource">getFormData(formId)</span>	// form 값을 serialize하는 함수
					<br/><span class="devGuideSource">reloadGrid(gridId, formId)</span>	// 그리드 새로고침 함수
					<br/>위의 함수들 사용시 formId에 해당하는 form의 검색조건들이 자동 저장된다.
				</dd>
			</dl>
		</li>
		<li>
			<dl>
				<dt>
					페이지 이동 & 검색조건 저장 안함
				</dt>
				<dd>
					<span class="devGuideSource">loadPage(url)</span><br/>
					loadPage 함수 호출시 url만 전달하면 검색조건을 저장하지 않고 해당 url로 이동한다.
				</dd>
			</dl>
		</li>
		<li></li>
	</ol>
</div>
