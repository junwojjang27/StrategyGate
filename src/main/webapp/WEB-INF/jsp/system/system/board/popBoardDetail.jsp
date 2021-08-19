<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/common-taglibs.jsp"%>
<script type="text/javascript">
$(function() {
	<c:if test="${boardSetting.useCommentYn eq 'Y'}">
		popLoadCommentList();
		showBytes("popCtnts", "popContentBytes");
		setMaxLength("popBoardform");
	</c:if>
});

<c:if test="${boardSetting.writable}">
	<c:if test="${dataVO.editable}">
	// 수정 화면으로
	function popGoUpdate() {
		
		openFancybox({
			"url" : "${context_path}/comPop/system/system/board/boardUpdateForm.do",
			"data" : getFormData("popBoardform")
		});
		
		//loadPage("${context_path}/comPop/system/system/board/boardUpdateForm.do", "popBoardform");
	}
	</c:if>
	
	<c:if test="${dataVO.deletable}">
	// 삭제
	function popDeleteData() {
		$.showConfirmBox("<spring:message code="common.delete.msg"/>", function() {
			sendAjax({
				"url" : "${context_path}/comPop/system/system/board/deleteBoard.do",
				"data" : getFormData("popBoardform"),
				"doneCallbackFunc" : "popCheckResult"
			});
		});
	}
	</c:if>
</c:if>

<c:if test="${boardSetting.writable and boardSetting.replyWritable and boardSetting.useReplyYn eq 'Y'}">
// 답글 폼으로 이동
function popGoReply() {
	var f = document.popBoardform;
	openFancybox({
		"url" : "${context_path}/comPop/system/system/board/boardReplyForm.do",
		"data" : getFormData("popBoardform")
	});
	//loadPage("${context_path}/comPop/system/system/board/boardReplyForm.do", "popBoardform");
}
</c:if>

// 목록으로
function popGoList() {
	openFancybox({
		"url" : "${context_path}/comPop/system/system/board/boardList.do",
		"data" : getFormData("popBoardform")
	});
	//loadPage("${context_path}/comPop/system/system/board/boardList.do", "popBoardform");
}

function popCheckResult(data) {
	$.showMsgBox(data.msg);
	if(data.result == AJAX_SUCCESS) {
		popGoList();
	}
}

<c:if test="${boardSetting.useCommentYn eq 'Y'}">
function popLoadCommentList() {
	$("#popCtnts").val("");
	$("#popContentBytes").text(0);
	sendAjax({
		"url" : "${context_path}/comPop/system/system/board/boardCommentList_json.do",
		"data" : getFormData("popBoardform"),
		"doneCallbackFunc" : "popShowCommentList"
	});
}

function popShowCommentList(data) {
	var listCnt = data.list.length;
	$("#popCommentsCnt").text("(" + listCnt + ")");
	
	$("#popTrCmtTemplate").nextAll().remove();
	
	if(listCnt == 0) {
		$("#popTrNoData").show();
	} else {
		$("#popTrNoData").hide();
		var cmt, $tr;
		for(var i in data.list) {
			cmt = data.list[i];
			$tr = $("#popTrCmtTemplate").clone().removeAttr("id").removeClass("hide");
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

function popSaveComment() {
	sendAjax({
		"url" : "${context_path}/comPop/system/system/board/insertComment.do",
		"data" : getFormData("popBoardform"),
		"doneCallbackFunc" : "popCheckCommentResult"
	});
}

function deleteComment(commentSeq) {
	$("#commentSeq").val(commentSeq);
	$.showConfirmBox("<spring:message code="common.delete.msg"/>", function() {
		sendAjax({
			"url" : "${context_path}/comPop/system/system/board/deleteComment.do",
			"data" : getFormData("popBoardform"),
			"doneCallbackFunc" : "popCheckCommentResult"
		});
	});
}

function popCheckCommentResult(data) {
	if(data.result == AJAX_SUCCESS) {
		popLoadCommentList();
	} else {
		$.showMsgBox(data.msg);
	}
}
</c:if>
</script>
<div class="popup wx800">
	<p class="title">QnA</p>
	<form:form commandName="searchVO" id="popBoardform" name="popBoardform" method="post">
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
						<td colspan="3">
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
				<a href="#" class="prev" onclick="popGoList();return false;"><span><spring:message code="button.before"/></span></a>
			</div>
			<div class="tbl-btn">
				<c:if test="${boardSetting.writable}">
					<c:if test="${boardSetting.useReplyYn eq 'Y' and boardSetting.replyWritable}">
						<a href="#" class="add" onclick="popGoReply();return false;"><spring:message code="button.reply2"/></a>
					</c:if>
					<c:if test="${dataVO.editable}">
						<a href="#" class="add" onclick="popGoUpdate();return false;"><spring:message code="button.update"/></a>
					</c:if>
					<c:if test="${dataVO.deletable and dataVO.replyCnt == 0}">
						<a href="#" class="delete" onclick="popDeleteData();return false;"><spring:message code="button.delete"/></a>
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
			<div class="ptitle"><spring:message code="word.comment"/> <label id="popCommentsCnt">(0)</label></div>
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
						<tr id="popTrNoData" class="hide2">
							<td colspan="3" class="txt-c"><spring:message code="errors.noResult"/></td>
						</tr>
						<tr id="popTrCmtTemplate" class="hide">
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
							<th scope="row"><label for="popCtnts"><spring:message code="word.comment"/></label></th>
							<td>
								<p><form:textarea path="contents" id="popCtnts" maxlength="500" cssClass="wp100 hx60 ml0"/></p>
								<p class="byte"><label id="popContentBytes">0</label><label> / 500byte</label></p>
							</td>
						</tr>
					</tbody>
				</table>
			</div>
			<div class="tbl-bottom">
				<div class="tbl-btn">
					<a href="#" class="save" onclick="popSaveComment();return false;"><spring:message code="button.save"/></a>
				</div>
			</div>
		</c:if>
	</form:form>
</div>	
