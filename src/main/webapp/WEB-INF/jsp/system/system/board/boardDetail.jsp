<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/common-taglibs.jsp"%>
<script type="text/javascript">
$(function() {
	<c:if test="${boardSetting.useCommentYn eq 'Y'}">
		loadCommentList();
		showBytes("ctnts", "contentBytes");
		setMaxLength("form");
	</c:if>
});

<c:if test="${boardSetting.writable}">
	<c:if test="${dataVO.editable}">
	// 수정 화면으로
	function goUpdate() {
		loadPage("${context_path}/system/system/board/boardUpdateForm.do", "form");
	}
	</c:if>
	
	<c:if test="${dataVO.deletable}">
	// 삭제
	function deleteData() {
		$.showConfirmBox("<spring:message code="common.delete.msg"/>", function() {
			sendAjax({
				"url" : "${context_path}/system/system/board/deleteBoard.do",
				"data" : getFormData("form"),
				"doneCallbackFunc" : "checkResult"
			});
		});
	}
	</c:if>
</c:if>

<c:if test="${boardSetting.writable and boardSetting.replyWritable and boardSetting.useReplyYn eq 'Y'}">
// 답글 폼으로 이동
function goReply() {
	var f = document.form;
	loadPage("${context_path}/system/system/board/boardReplyForm.do", "form");
}
</c:if>

// 목록으로
function goList() {
	loadPage("${context_path}/system/system/board/boardList.do", "form");
}

function checkResult(data) {
	$.showMsgBox(data.msg);
	if(data.result == AJAX_SUCCESS) {
		goList();
	}
}

<c:if test="${boardSetting.useCommentYn eq 'Y'}">
function loadCommentList() {
	$("#ctnts").val("");
	$("#contentBytes").text(0);
	sendAjax({
		"url" : "${context_path}/system/system/board/boardCommentList_json.do",
		"data" : getFormData("form"),
		"doneCallbackFunc" : "showCommentList"
	});
}

function showCommentList(data) {
	var listCnt = data.list.length;
	$("#commentsCnt").text("(" + listCnt + ")");
	
	$("#trCmtTemplate").nextAll().remove();
	
	if(listCnt == 0) {
		$("#trNoData").show();
	} else {
		$("#trNoData").hide();
		var cmt, $tr;
		for(var i in data.list) {
			cmt = data.list[i];
			$tr = $("#trCmtTemplate").clone().removeAttr("id").removeClass("hide");
			$tr.find(".cmtUserNm").text(cmt.userNm);
			if(cmt.insertUserId == "${sessionScope.userId}") {
				$tr.find(".cmtUserNm").addClass("fs-bold");
			}
			$tr.find(".cmtDeptNm").text(isNotEmpty(cmt.deptNm) ? "(" + cmt.deptNm + ")" : "");
			$tr.find(".cmtCreateDt").text(cmt.createDt);
			$tr.find(".cmtContents").html(escapeHTML(removeNull(cmt.contents)).replace(/[\r|\n]+/g, "<br/>"));
			
			<sec:authorize access="hasRole('01')">
				$tr.find("a.delete").on("click", {"cmtSeq" : cmt.commentSeq}, function(e) {
					deleteComment(e.data.cmtSeq);
					e.preventDefault();
				});
			</sec:authorize>
			<sec:authorize access="not hasRole('01')">
				if(cmt.insertUserId == "${sessionScope.userId}") {
					$tr.find("a.delete").on("click", {"cmtSeq" : cmt.commentSeq}, function(e) {
						deleteComment(e.data.cmtSeq);
						e.preventDefault();
					});
				} else {
					$tr.find("a.delete").remove();
				}
			</sec:authorize>
			
			$("#commentTable tbody").append($tr);
		}
	}
}

function saveComment() {
	sendAjax({
		"url" : "${context_path}/system/system/board/insertComment.do",
		"data" : getFormData("form"),
		"doneCallbackFunc" : "checkCommentResult"
	});
}

function deleteComment(commentSeq) {
	$("#commentSeq").val(commentSeq);
	$.showConfirmBox("<spring:message code="common.delete.msg"/>", function() {
		sendAjax({
			"url" : "${context_path}/system/system/board/deleteComment.do",
			"data" : getFormData("form"),
			"doneCallbackFunc" : "checkCommentResult"
		});
	});
}

function checkCommentResult(data) {
	if(data.result == AJAX_SUCCESS) {
		loadCommentList();
	} else {
		$.showMsgBox(data.msg);
	}
}
</c:if>
</script>
<form:form commandName="searchVO" id="form" name="form" method="post">
	<form:hidden path="boardId"/>
	<form:hidden path="seq"/>
	<form:hidden path="commentSeq"/>
	<form:hidden path="page"/>
	<form:hidden path="rows"/>


	<div class="tbl-type02 mt20">
		<table summary="">
			<colgroup>
				<col width="140"/>
				<col width=""/>
				<col width="140"/>
				<col width=""/>
			</colgroup>
			<tbody>
				<tr>
					<th scope="row"><label for="title"><spring:message code="word.title"/></label></th>
					<td><c:out value="${dataVO.title}" escapeXml="true"/></td>
					<th scope="row"><label><spring:message code="word.insertUser"/></label></th>
					<td><c:out value="${dataVO.userNm}" escapeXml="true"/></td>
				</tr>
				<tr>
					<th scope="row"><label for="contents"><spring:message code="word.content"/></label></th>
					<td colspan="3" class="pd10">
						<c:set var="newLine" value="<%=\"\n\"%>"/>
						<c:set var="contents"><c:out value="${fn:replace(dataVO.contents, newLine, '<br/>')}" escapeXml="true"></c:out></c:set>
						<c:out value="${fn:replace(contents, '&lt;br/&gt;', '<br/>')}" escapeXml="false"></c:out>
					</td>
				</tr>
				<c:if test="${boardSetting.useAtchFileYn eq 'Y'}">
					<tr>
						<th scope="row"><spring:message code="word.atchFile"/></th>
						<td colspan="3">
							<c:import url="/common/fileList.do" charEncoding="utf-8">
								<c:param name="downloadOnly" value="Y"/>
								<c:param name="param_atchFileId" value="${dataVO.atchFileId}"/>
							</c:import>
						</td>
					</tr>
				</c:if>
			</tbody>
		</table>
	</div>
	<div class="tbl-bottom">
		<div class="tbl-wbtn">
			<a href="#" class="prev" onclick="goList();return false;"><span><spring:message code="button.before"/></span></a>
		</div>
		<div class="tbl-btn">
			<c:if test="${boardSetting.writable}">
				<c:if test="${boardSetting.useReplyYn eq 'Y' and boardSetting.replyWritable}">
					<a href="#" class="add" onclick="goReply();return false;"><spring:message code="button.reply2"/></a>
				</c:if>
				<c:if test="${dataVO.editable}">
					<a href="#" class="add" onclick="goUpdate();return false;"><spring:message code="button.update"/></a>
				</c:if>
				<c:if test="${dataVO.deletable and dataVO.replyCnt == 0}">
					<a href="#" class="delete" onclick="deleteData();return false;"><spring:message code="button.delete"/></a>
				</c:if>
			</c:if>
		</div>
	</div>
	<c:if test="${dataVO.deletable and dataVO.replyCnt > 0}">
		<div class="page-noti">
			<ul>
				<li><spring:message code="system.system.board.cantDelete"/></li>
			</ul>
		</div>
	</c:if>
	
	<c:if test="${boardSetting.useCommentYn eq 'Y'}">
		<div class="ptitle"><spring:message code="word.comment"/> <label id="commentsCnt">(0)</label></div>
		<div class="tbl-type02">
			<table id="commentTable">
				<colgroup>
					<col width="20%"/>
					<col width="70%"/>
					<col width="10%"/>
				</colgroup>
				<thead>
					<tr>
						<th scope="col" class="pl0 txt-c"><spring:message code="word.writeUser"/></th>
						<th scope="col" class="pl0 txt-c"><spring:message code="word.content"/></th>
						<th scope="col" class="pl0 txt-c"><spring:message code="word.delete"/></th>
					</tr>
				</thead>
				<tbody>
					<tr id="trNoData" class="hide2">
						<td colspan="3" class="txt-c"><spring:message code="errors.noResult"/></td>
					</tr>
					<tr id="trCmtTemplate" class="hide">
						<td class="txt-c">
							<div class="mb5">
								<label class="cmtUserNm"></label> <label class="cmtDeptNm"></label>
							</div>
							<label class="cmtCreateDt"></label><br/>
						</td>
						<td>
							<p class="cmtContents"></p>
						</td>
						<td class="tbl-btn txt-c pd5">
							<a href="#" class="delete pd5"><spring:message code="button.delete"/></a>
						</td>
					</tr>
				</tbody>
			</table>
		</div>
		<div class="tbl-type02 mt10">
			<table>
				<colgroup>
					<col width="140"/>
					<col width=""/>
				</colgroup>
				<tbody>
					<tr>
						<th scope="row"><label for="ctnts"><spring:message code="word.comment"/></label></th>
						<td>
							<p><form:textarea path="contents" id="ctnts" maxlength="500" cssClass="wp100 hx60 ml0"/></p>
							<p class="byte"><label id="contentBytes">0</label><label> / 500byte</label></p>
						</td>
					</tr>
				</tbody>
			</table>
		</div>
		<div class="tbl-bottom">
			<div class="tbl-btn">
				<a href="#" class="save" onclick="saveComment();return false;"><spring:message code="button.save"/></a>
			</div>
		</div>
	</c:if>
</form:form>
