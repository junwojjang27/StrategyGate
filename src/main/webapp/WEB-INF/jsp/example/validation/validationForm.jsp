<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/common-taglibs.jsp"%>
<validator:javascript formName="exampleVO" staticJavascript="false" xhtml="true" cdata="false"/>
<script type="text/javascript">
function doSave() {
	if(!validateExampleVO(document.form)) {
		return;
	}
	
	sendAjax({
		"url" : "${context_path}/example/validation/validationSave.do",
		"data" : getFormData("form"),
		"doneCallbackFunc" : "checkResult"
	});
}

function doSave2() {
	sendAjax({
		"url" : "${context_path}/example/validation/validationSave.do",
		"data" : getFormData("form2"),
		"doneCallbackFunc" : "checkResult"
	});
}

function checkResult(data) {
	$.showMsgBox(data.msg);
}

$(function() {
	setMaxLength("form");
	$("#form [name=sortOrder]").numericOnly();
});
</script>
<form:form commandName="searchVO" id="form" name="form" method="post">
	<div class="ptitle">Client-side Validation</div>
	<div class="tbl-type02">
		<table summary="">
			<caption>Client-side Validation</caption>
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
	<div class="tbl-bottom">
		<div class="tbl-btn">
			<a href="#" class="save" onclick="doSave();return false;">저장</a>
		</div>
	</div>
</form:form>

<form:form commandName="searchVO" id="form2" name="form2" method="post">
	<div class="ptitle">Server-side Validation</div>
	<div class="tbl-type02">
		<table summary="">
			<caption>Server-side Validation</caption>
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
	<div class="tbl-bottom">
		<div class="tbl-btn">
			<a href="#" class="save" onclick="doSave2();return false;">저장</a>
		</div>
	</div>
</form:form>
<div class="page-noti">
	<ul>
		<li>여기서는 설명을 위해 client-side와 server-side를 각각 따로 적용했지만, 실제 서비스에서는 양쪽 모두에 적용해야 함.</li>
	</ul>
</div>

<div class="devGuide">
	<h3>Client-side Validation</h3>
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
					~~~Validator.xml에 의한 체크
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
					byte 최대 길이 제한
				</dt>
				<dd>
					<span class="devGuideSource">$(function() {</span>
					<br/><span class="devGuideSource"><span class="tab"></span>setMaxLength("form");</span>	// id가 form인 form의 input들 중에 maxlength가 선언된 input의 maxlength를 byte길이로 제한해줌
					<br/><span class="devGuideSource">}</span>
				</dd>
			</dl>
		</li>
		<li>
			<dl>
				<dt>
					숫자 입력만 받음
				</dt>
				<dd>
					<span class="devGuideSource">$(function() {</span>
					<br/><span class="devGuideSource"><span class="tab"></span>$("#id").numericOnly();</span>	// id가 id인 객체를 찾아서 숫자만 입력하도록 처리
					<br/><span class="devGuideSource">}</span>
				</dd>
			</dl>
		</li>
		<li></li>
	</ol>
	<h3>Server-side Validation</h3>
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
					서버쪽에서의 체크
				</dt>
				<dd>
					<span class="devGuideSource">public ModelAndView validationSave(@ModelAttribute("dataVO") ExampleVO dataVO, Model model, BindingResult bindingResult) throws Exception {</span>
					<br/><span class="devGuideSource"><span class="tab"></span>beanValidator.validate(dataVO, bindingResult);</span>	// 해당 VO에 맞는 validator가 적용됨
				    <br/><span class="devGuideSource"><span class="tab"></span>if(bindingResult.hasErrors()){</span>	// validation을 통과하지 못한 경우
					<br/><span class="devGuideSource"><span class="tab"></span><span class="tab"></span>return makeFailJsonData(getListErrorMsg(bindingResult));</span>	// 해당 오류 메시지를 msg 변수에 담아서 client로 전달
				    <br/><span class="devGuideSource"><span class="tab"></span>}</span>
				    <br/><span class="devGuideSource"><span class="tab"></span>return makeSuccessJsonData();</span>
					<br/><span class="devGuideSource">}</span>
					<br/>※ ~~~Validator.xml 파일을 작성하면 client & server 양쪽에서 사용가능
				</dd>
			</dl>
			<li></li>
		</li>
	</ol>
</div>