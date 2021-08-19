<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/common-taglibs.jsp"%>
<validator:javascript formName="exampleBoardVO" staticJavascript="false" xhtml="true" cdata="false"/>
<script type="text/javascript">
var upload1, upload2;
$(function() {
	setMaxLength("form");
	upload1 = new SGFileUploader({"targetId" : "divFile1", "inputName" : "upFile1", "maxFileCnt" : 5, "allowFileExts": "<spring:eval expression="@globals.getProperty('fileUpload.allowFileExts')"></spring:eval>", "maxTotalSize" : 5242880});
	<c:import url="/common/fileList.do" charEncoding="utf-8">
		<c:param name="moduleName" value="upload1"/>// 첨부파일 객체를 넣어줌
		<c:param name="param_atchFileId" value="${dataVO.atchFileId}"/>//첨부파일 아이디 삽입
	</c:import> // targetId, maxfilecnt, spring:eval/ 확장자 설정
	
	upload2 = new SGFileUploader({"targetId" : "divFile2", "inputName" : "upFile2", "maxFileCnt" : 1});
	<c:import url="/common/fileList.do" charEncoding="utf-8">
		<c:param name="moduleName" value="upload2"/>
		<c:param name="param_atchFileId" value="${dataVO.atchFileId2}"/>
	</c:import>
	
	showBytes("content", "contentBytes");
});

// 저장
function doSave() {
	if(!validateExampleBoardVO(document.form)) {
		return;
	}
	
	sendMultipartForm({ //첨부파일이있는화면에서 서버로 요청하는 화면
		"url" : "${context_path}/example/board/saveBoard.do",
		"formId" : "form",
		"fileModules" : [upload1, upload2],
		"doneCallbackFunc" : "checkResult",
		"failCallbackFunc" : "checkResult"
	});
}

// 저장 callback
function checkResult(data) {
	$.showMsgBox(data.msg);
	if(data.result == AJAX_SUCCESS) {
		goBack();
	} else if(data.result == AJAX_FAIL) {
	}
}

// 취소
function goBack() {
	<c:choose>
		<c:when test="${empty dataVO.id}">
			loadPage("${context_path}/example/board/boardList.do", "form");
		</c:when>
		<c:otherwise>
			loadPage("${context_path}/example/board/boardDetail.do", "form");
		</c:otherwise>
	</c:choose>
}
</script>

<div>
	<div class="ptitle">
		게시판 예제 (파일 첨부) - 
		<c:choose>
			<c:when test="${empty dataVO.id}">등록</c:when>
			<c:otherwise>수정</c:otherwise>
		</c:choose>
	</div>
	<form:form commandName="dataVO" id="form" name="form" method="post" enctype="multipart/form-data">
		<form:hidden path="id"/>
		<div class="tbl-type02">
			<table summary="">
				<caption>글 작성</caption>
				<colgroup>
					<col width="120"/>
					<col width=""/>
				</colgroup>
				<tbody>
					<tr>
						<th scope="row"><label for="subject">제목</label></th>
						<td><form:input path="subject" maxlength="30" class="t-box03"/></td>
					</tr>
					<tr>
						<th scope="row"><label for="content">비고</label></th>
						<td>
							<p><form:textarea path="content" maxlength="100"/></p>
							<p class="byte"><label id="contentBytes">0</label><label> / 100byte</label></p>
						</td>
					</tr>
					<tr>
						<th scope="row">첨부파일</th>
						<td>
							<label class="red">※ 5개, <spring:eval expression="@globals.getProperty('fileUpload.allowFileExts')"></spring:eval>만 허용, 5메가 제한</label>
							<div id="divFile1"></div>
						</td>
					</tr>
					<tr>
						<th scope="row">첨부파일2</th>
						<td>
							<div id="divFile2"></div>
						</td>
					</tr>
				</tbody>
			</table>
		</div>
		<div class="tbl-bottom">
			<div class="tbl-wbtn">
				<a href="#" class="prev" onclick="goBack();return false;"><span>이전</span></a>
			</div>
			<div class="tbl-btn">
				<a href="#" class="save" onclick="doSave();return false;">저장</a>
			</div>
		</div>
	</form:form>
	<div class="devGuide">
		<h3>파일모듈 설정</h3>
		<ol>
			<li>
				<dl>
					<dt>
						파일모듈을 변수 선언
					</dt>
					<dd>
						<span class="devGuideSource">var upload1, upload2;</span>	// 파일 모듈 개수만큼
					</dd>
				</dl>
			</li>
			<li>
				<dl>
					<dt>
						파일모듈 추가
					</dt>
					<dd>
						<span class="devGuideSource">$(function() {</span>
						<span class="devGuideSource"><br/><span class="tab"></span>upload1 = new SGFileUploader({</span>
						<span class="devGuideSource"><br/><span class="tab"></span><span class="tab"></span><span class="tab"></span>"targetId" : "divFile1",</span>	// 파일모듈을 위치시킬 dom id
						<span class="devGuideSource"><br/><span class="tab"></span><span class="tab"></span><span class="tab"></span>"inputName" : "upFile1",</span>	// 파라미터명
						<span class="devGuideSource"><br/><span class="tab"></span><span class="tab"></span><span class="tab"></span>"maxFileCnt" : 5,</span>			// 최대 허용 업로드 수
						<span class="devGuideSource"><br/><span class="tab"></span><span class="tab"></span><span class="tab"></span>"allowFileExts": "<spring:eval expression="@globals.getProperty('fileUpload.allowImgFileExts')"></spring:eval>",</span>	// 업로드 허용 확장자명
						<span class="devGuideSource"><br/><span class="tab"></span><span class="tab"></span><span class="tab"></span>"maxTotalSize" : 5242880</span>	// 최대 허용 업로드 용량 (전체 파일을 합친 용량)
						<span class="devGuideSource"><br/><span class="tab"></span><span class="tab"></span>});</span>
						<span class="devGuideSource"><br/>});</span>
					</dd>
				</dl>
			</li>
			<li>
				<dl>
					<dt>
						수정시 첨부파일 목록 표시
					</dt>
					<dd>
						<span class="devGuideSource">$(function() {</span>
						<span class="devGuideSource"><br/><span class="tab"></span>upload1 = new SGFileUploader({ ... });</span>
						<span class="devGuideSource"><br/><span class="tab"></span>&lt;c:import url="/common/fileList.do" charEncoding="utf-8"></span>
						<span class="devGuideSource red"><br/><span class="tab"></span><span class="tab"></span>&lt;c:param name="moduleName" value="upload1"/></span>	// 목록을 표시할 파일모듈 변수명
						<span class="devGuideSource"><br/><span class="tab"></span><span class="tab"></span>&lt;c:param name="param_atchFileId" value="&#36{dataVO.atchFileId}"/></span>
						<span class="devGuideSource"><br/><span class="tab"></span><span class="tab"></span>&lt;c:param name="canDelete" value="N"/></span>	// 삭제 체크박스 표시여부 (생략할 경우 'Y')
						<span class="devGuideSource"><br/><span class="tab"></span>&lt;/c:import></span>
						<span class="devGuideSource"><br/>});</span>
					</dd>
				</dl>
			</li>
			<li>
				<dl>
					<dt>
						첨부파일 수 체크
					</dt>
					<dd>
						<span class="devGuideSource">sendMultipartForm(...)</span> 함수를 사용해서 저장해야함.
						<br/>※ doSave() {} 함수 참고
					</dd>
				</dl>
			</li>
			<li>
				<dl>
					<dt>
						※ 유의사항
					</dt>
					<dd>
						1. IE9에서는 한 번에 여러 파일을 선택하거나 드래그 앤 드롭으로 파일 업로드를 할 수 없음.<br/>
						<span class="red">2. 서버쪽에서도 파일 용량, 파일 수, 허용 확장자를 체크해야함.</span>
						<span class="devGuideSource"><br/>CustomEgovFileMngUtil 클래스의 validation 메소드를 사용할 것.</span>
					</dd>
				</dl>
			</li>
			<li></li>
		</ol>
	</div>
</div>