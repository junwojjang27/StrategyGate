<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/common-taglibs.jsp"%>
<script type="text/javascript">
// 사용자 검색 팝업
function selectUser() {
	openFancybox("${context_path}/example/common/popUserList.do");
}

function setUser(userId, userNm) {
	$.showMsgBox("[" + userId + "] " + userNm);
	$.fancybox.close();
}
</script>
<div>
	<div class="tbl-bottom">
		<div class="tbl-btn">
			<a href="#" class="add" onclick="selectUser();return false;">사용자 검색</a>
		</div>
	</div>
	
	<div class="devGuide">
		<h3>기능 관련 jqGrid 설정</h3>
		<ol>
			<li>
				<dl>
					<dt>
						팝업 호출 방법1
					</dt>
					<dd>
						<span class="devGuideSource">openFancybox("&#36{context_path}/example/common/popUserList.do");</span>
						<br/>
						해당 URL을 호출함
					</dd>
				</dl>
			</li>
			<li>
				<dl>
					<dt>
						팝업 호출 방법2
					</dt>
					<dd>
						<span class="devGuideSource">openFancybox({"url" : "&#36{context_path}/example/common/popUserList.do", "data" : {"key":"value", ... });</span>
						<br/>
						url : 호출할 url
						<br/>
						data : 전송할 data
						<br/>
						※ fancybox로 불러온 소스는 호출한 창(부모창)의 소스에 추가되므로 부모창의 js 변수, 함수 등을 그대로 사용할 수 있음. 
					</dd>
				</dl>
			</li>
			<li></li>
		</ol>
	</div>
</div>