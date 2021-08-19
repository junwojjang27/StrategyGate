<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/common-taglibs.jsp"%>
<script type="text/javascript">
$(function() {
});

// 수정 화면으로
function goUpdate() {
	loadPage("${context_path}/example/board/boardUpdateForm.do", "form");
}

// 목록으로
function goList() {
	loadPage("${context_path}/example/board/boardList.do", "form");
}
</script>

<div class="ptitle">
	게시판 예제 (파일 첨부) - 조회
</div>
<form:form commandName="dataVO" id="form" name="form" method="post">
	<form:hidden path="id"/>
	<div class="tbl-type02">
		<table summary="">
			<caption>글 조회</caption>
			<colgroup>
				<col width="120"/>
				<col width=""/>
			</colgroup>
			<tbody>
				<tr>
					<th scope="row"><label for="subject">제목</label></th>
					<td><c:out value="${dataVO.subject}" escapeXml="true"/></td>
				</tr>
				<tr>
					<th scope="row"><label for="content">비고</label></th>
					<td>
						<c:set var="newLine" value="<%=\"\n\"%>"/>
						<c:set var="content"><c:out value="${fn:replace(dataVO.content, newLine, '<br/>')}" escapeXml="true"></c:out></c:set>
						<c:out value="${fn:replace(content, '&lt;br/&gt;', '<br/>')}" escapeXml="false"></c:out>
					</td>
				</tr>
				<tr>
					<th scope="row">첨부파일</th>
					<td>
						<c:import url="/common/fileList.do" charEncoding="utf-8">
							<c:param name="downloadOnly" value="Y"/>
							<c:param name="param_atchFileId" value="${dataVO.atchFileId}"/>
						</c:import>
					</td>
				</tr>
				<tr>
					<th scope="row">첨부파일2</th>
					<td>
						<c:import url="/common/fileList.do" charEncoding="utf-8">
							<c:param name="downloadOnly" value="Y"/>
							<c:param name="param_atchFileId" value="${dataVO.atchFileId2}"/>
						</c:import>
					</td>
				</tr>
			</tbody>
		</table>
	</div>
	<div class="tbl-bottom">
		<div class="tbl-wbtn">
			<a href="#" class="prev" onclick="goList();return false;"><span>이전</span></a>
		</div>
		<div class="tbl-btn">
			<a href="#" class="add" onclick="goUpdate();return false;">수정</a>
		</div>
	</div>
</form:form>
<div class="devGuide">
	<h3>파일모듈 설정</h3>
	<ol>
		<li>
			<dl>
				<dt>
					첨부파일 목록 표시
				</dt>
				<dd>
					<span class="devGuideSource">&lt;td></span>
					<span class="devGuideSource"><br/><span class="tab"></span>&lt;c:import url="/common/fileList.do" charEncoding="utf-8"></span>
					<span class="devGuideSource red"><br/><span class="tab"></span><span class="tab"></span>&lt;c:param name="downloadOnly" value="Y"/></span> // 조회 전용
					<span class="devGuideSource"><br/><span class="tab"></span><span class="tab"></span>&lt;c:param name="param_atchFileId" value="&#36{dataVO.atchFileId}"/></span>
					<span class="devGuideSource"><br/><span class="tab"></span>&lt;/c:import></span>
					<span class="devGuideSource"><br/>&lt;/td></span>
				</dd>
			</dl>
		</li>
		<li></li>
	</ol>
</div>
