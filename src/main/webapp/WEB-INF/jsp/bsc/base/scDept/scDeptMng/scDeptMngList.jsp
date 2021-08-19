<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/common-taglibs.jsp"%>
<validator:javascript formName="scDeptVO" staticJavascript="false" xhtml="true" cdata="false"/>
<script type="text/javascript">
$(function(){
	$("#list").jqGrid({
		url			:	"${context_path}/bsc/base/scDept/scDeptMng/scDeptMngList_json.do",
		postData	:	getFormData("form"),
		width		:	"${jqgrid_width}",
		height		:	"${jqgrid_height5}",
		colModel	:	[
							{name:"year",			index:"year",			hidden:true},
							{name:"scDeptGrpId",	index:"scDeptGrpId",	hidden:true},
							{name:"scDeptId",		index:"scDeptId",	hidden:true},
							{name:"metricCnt",		index:"metricCnt",	hidden:true},
							{name:"scDeptNm",		index:"scDeptNm",	width:200,		align:"left",	label:"<spring:message code="word.scDeptNm"/>",
								formatter:function(cellvalue, options, rowObject) {
									return "<a href='#' onclick='showDetail(\"" + removeNull(rowObject.scDeptId) + "\");return false;'>" + escapeHTML(removeNull(cellvalue)) + "</a>";
								},
								unformat:linkUnformatter
							},
							{name:"scDeptGrpId",	index:"scDeptGrpId",	hidden:true},
							{name:"scDeptGrpNm",	index:"scDeptGrpNm",	width:150,	align:"left",	label:"<spring:message code="word.evalGrp"/>"},
							{name:"managerUserId",	index:"managerUserId",	hidden:true},
							{name:"managerUserNm",	index:"managerUserNm",	width:100,		align:"left",	label:"<spring:message code="word.manager"/>"},
							{name:"bscUserId",		index:"bscUserId",	hidden:true},
							{name:"bscUserNm",		index:"bscUserNm",	width:100,		align:"left",	label:"<spring:message code="word.meticInCharge"/>"},
							{name:"sortOrder",		index:"sortOrder",		width:50,		align:"center",	label:"<spring:message code="word.sortOrder"/>",
								editable:true, formatter:"text", editrules:{required:true,number:true}, editoptions:{maxlength:3}
							}
						],
		rowNum		: ${jqgrid_rownum_max},
		pager		: "pager",
		sortname	: "sortOrder",
		sortorder	: "asc",
		multiselect	: true,
		cellEdit	: true,
		cellsubmit  :'clientArray',
		loadComplete : function() {
			hideGridCheckbox("list", "metricCnt", 0, false);
		}
	});
	
	$("#newForm").hide();
	$("#sortOrder").numericOnly();

	// 연도 변경시 조직평가군 새로 불러옴
	$("#findYear").on("change", function() {
		getScDeptGrpList();
	});
	
	/***** 사용여부 미사용시 삭제 버튼 숨김 *****/
	<c:choose>
		<c:when test="${searchVO.findUseYn == 'N'}">
			$(".delete").hide();
		</c:when>
		<c:otherwise>
			$(".delete").show();
		</c:otherwise>
	</c:choose>
	$("#findUseYn").on("change", function() {
		if($(this).val() == "N"){
			$(".delete").hide();
		}else{
			$(".delete").show();
		}
	});
	/***** 사용여부 미사용시 삭제 버튼 숨김 end *****/
	
	getScDeptGrpList();
});

// 목록 조회
function searchList() {
	$("#newForm").hide();
 	reloadGrid("list", "form");
}

// 엑셀 다운로드
function excelDownload() {
	var f = document.form;
	f.action = "${context_path}/bsc/base/scDept/scDeptMng/excelDownload.do";
	f.submit();
}

// 상세 조회
function showDetail(scDeptId) {
	var f = document.form;
	f.scDeptId.value = scDeptId;
	sendAjax({
		"url" : "${context_path}/bsc/base/scDept/scDeptMng/selectDetail.do",
		"data" : getFormData("form"),
		"doneCallbackFunc" : "setDetail"
	});
}

// 상세 조회 값 세팅
function setDetail(data) {
	$("#newForm").show();
	var dataVO = data.dataVO;
	$("#titleScDeptNm").text("<spring:message code="word.scDeptNm"/> : " + dataVO.scDeptNm);
	voToForm(dataVO, "form", ["year", "scDeptNm", "scDeptFullNm", "upScDeptId", "upScDeptNm", "scDeptGrpId", "bscUserId", "bscUserNm", "managerUserId", "managerUserNm", "deptKind", "useYn", "sortOrder", "levelId"]);
	$("#scDeptFullNm").val(dataVO.scDeptFullNm);	// 부서명 구분자인 >가 &lt;로 escape 되기 때문에 별도로 세팅
	if(dataVO.levelId == "1") {
		$(".spanUpScDept").hide();
		$("#upScDeptId").val("");
	} else {
		$(".spanUpScDept").show();
	}
	$("#resetAllYn1").prop("checked", true);
	
	$("#scDeptNm").focus();
}

// 정렬순서저장
function saveSortOrder() {
	if(!gridToForm("list", "form", true)) return false;
	
	sendAjax({
		"url" : "${context_path}/bsc/base/scDept/scDeptMng/saveSortOrder.do",
		"data" : getFormData("form"),
		"doneCallbackFunc" : "checkResult",
		"doneCallbackArgs" : [false, null]
	});
}

function checkResult(doReloadScDeptList, findScDeptId, data) {
	if(data.result == AJAX_SUCCESS) {
		$(window).scrollTop(0);
		$.showMsgBox(data.msg);
		if(isNotEmpty(findScDeptId)) {
			setScDept(findScDeptId);
		}
		if(doReloadScDeptList) {
			reloadScDeptList();
		}
		searchList();
	} else {
		$.showMsgBox(data.msg);
	}
}

// 등록
function addData() {
	
	$("#newForm").show();
	$(".spanUpScDept").show();
	$("#titleScDeptNm").text("<spring:message code="word.scDeptNm"/>");
	resetForm("form", ["year", "scDeptId", "scDeptNm", "scDeptFullNm", "upScDeptId", "upScDeptNm", "scDeptGrpId", "bscUserId", "bscUserNm", "managerUserId", "managerUserNm", "deptKind", "useYn", "sortOrder"]);
	$("#year").val($("#findYear").val());
	$("#scDeptNm").focus();
}

// 저장
function saveData() {
	var f = document.form;
	if(!validateScDeptVO(f)) {
		return;
	}
	
	if(f.scDeptId.value == f.upScDeptId.value) {
		$.showMsgBox("<spring:message code="bsc.base.scDeptMng.scDeptMngForm.error"/>");
		return false;
	}
	
	if(f.upScDeptId.value == "" && f.levelId.value > 1) {
		// 최상위 조직이 아닌 경우 상위조직값이 필수 항목 
		if(document.form.scDeptId.value != getScDeptRootId()) {
			<c:set var="messageArg"><spring:message code="word.upOrg"/></c:set>
			$.showMsgBox("<spring:message code="common.required.msg" arguments="${messageArg}"/>");
			return false;
		}
	}
	
	// 전체 조직명 설정
	f.scDeptFullNm.value = (isNotEmpty(f.upScDeptId.value) ? (getScDeptFullNm(f.upScDeptId.value) + ">") : "") + f.scDeptNm.value
	
	$.showConfirmBox("<spring:message code="common.save.msg"/>", function() {
		//저장시 상위부서가 조회부서가 되도록 수정 
		$("#findScDeptId").val(f.upScDeptId.value);
		
		sendAjax({
			"url" : "${context_path}/bsc/base/scDept/scDeptMng/saveScDeptMng.do",//문제가 없으면 저장하라고 서버에 요청
			"data" : getFormData("form"),
			"doneCallbackFunc" : "checkResult",
			"doneCallbackArgs" : [true, null]
		});
	});
}

// 삭제
function deleteData() {
	if(deleteDataToForm("list", "scDeptId", "form")) {
		$.showConfirmBox("<spring:message code="common.delete.msg"/>", "doDeleteData");
	}
}

// 삭제 처리
function doDeleteData() {
	var delList = [];
	$("#form").find("[name=keys]").each(function(i, e) {
		delList.push($(this).val());
	});
	
	if($.inArray(getScDeptRootId(), delList) != -1) {
		$.showMsgBox("<spring:message code="bsc.base.scDeptMng.scDeptMngForm.error2"/>");
		return false;
	}
	
	sendAjax({
		"url" : "${context_path}/bsc/base/scDept/scDeptMng/deleteScDeptMng.do",
		"data" : getFormData("form"),
		"doneCallbackFunc" : "checkResult",
		"doneCallbackArgs" : [true, ($.inArray($("#findScDeptId").val(), delList) != -1 ? getScDeptRootId() : null)]
	});
}

// 조직평가군 코드 조회
function getScDeptGrpList() {
	sendAjax({
		"url" : "${context_path}/common/codeList_json.do",
		"data" : {
			"codeGrpId" : "003",
			"findYear"	: $("#findYear").val(),
			"_csrf"		: getCsrf()
		},
		"doneCallbackFunc" : "setScDeptGrpId"
	});
}

// 조직평가군 세팅
function setScDeptGrpId(data) {
	$("#scDeptGrpId").empty();
	if(isNotEmpty(data.list)) {
		var list = data.list;
		for(var i in list) {
			$("<option value='" + list[i].codeId + "'>" + list[i].codeNm + "</option>").appendTo($("#scDeptGrpId"));
		}
	}
}
</script>

<form:form commandName="searchVO" id="form" name="form" method="post">
	<form:hidden path="findScDeptId"/>
	<form:hidden path="scDeptId"/>
	<form:hidden path="scDeptFullNm"/>
	<form:hidden path="year"/>
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
		<a href="#" class="btn-sch" onclick="searchList();return false;"><spring:message code="button.search"/></a>
	</div>
	<div class="btn-dw">
		<a href="#" class="excel" onclick="excelDownload();return false;"><span><spring:message code="button.excelDownload"/></span></a>
	</div>
	<div class="gridContainer">
		<table id="list"></table>
		<div id="pager"></div>
	</div>
	<div class="tbl-bottom tbl-bottom2">
		<div class="tbl-btn">
			<a href="#" class="save" onclick="saveSortOrder();return false;"><spring:message code="button.saveAll"/></a>
			<a href="#" class="new" onclick="addData();return false;"><spring:message code="button.add"/></a>
			<a href="#" class="delete" onclick="deleteData();return false;"><spring:message code="button.delete"/></a>
		</div>
	</div>
	<div class="page-noti">
		<ul>
			<li><spring:message code="bsc.base.scDeptMng.scDeptMngList.info"/></li>
			<li><spring:message code="bsc.base.scDeptMng.scDeptMngList.info2"/></li>
		</ul>
	</div>

	<div id="newForm">
		<div class="ptitle" id="titleScDeptNm"></div>
		<div class="tbl-type02">
			<table summary="">
				<caption></caption>
				<colgroup>
					<col width="140"/>
					<col width=""/>
					<col width="140"/>
					<col width=""/>
				</colgroup>
				<tbody>
					<tr>
						<th scope="row"><label for="scDeptNm"><spring:message code="word.scDeptNm"/></label><span class="red">(*)</span></th>
						<td><form:input path="scDeptNm" class="t-box01"/></td>
						<th scope="row"><label for="upScDeptNm"><spring:message code="word.upOrg"/></label><span class="red spanUpScDept">(*)</span></th>
						<td>
							<form:hidden path="levelId"/>
							<form:hidden path="upScDeptId"/>
							<span class="spanUpScDept">
								<a href="#" class="btn-search" onclick="popScDeptList('upScDeptId', 'upScDeptNm', 'scDeptFullNm');return false;"><spring:message code="button.search"/></a><form:input path="upScDeptNm" class="t-box02" readonly="true"/><a href="#" class="close" onclick="resetInput(['upScDeptId', 'upScDeptNm']);return false;"><spring:message code="button.delete"/></a>
							</span>
						</td>
					</tr>
					<tr>
						<th scope="row"><label for="scDeptGrpId"><spring:message code="word.evalGrp"/></label><span class="red">(*)</span></th>
						<td>
							<form:select path="scDeptGrpId" class="select"></form:select>
						</td>
						<th scope="row"><label for="deptKind"><spring:message code="word.deptDiagramShape"/></label></th>
						<td>
							<form:select path="deptKind" class="select" items="${codeUtil:getCodeList('025')}" itemLabel="codeNm" itemValue="codeId">
							</form:select>
						</td>
					</tr>
					<tr>
						<th scope="row"><label for="managerUserNm"><spring:message code="word.manager"/></label></th>
						<td>
							<form:hidden path="managerUserId"/>
							<a href="#" class="btn-search" onclick="popSearchUser('managerUserId', 'managerUserNm');return false;"><spring:message code="button.search"/></a><form:input path="managerUserNm" class="t-box02" readonly="true"/><a href="#" class="close" onclick="resetInput(['managerUserId', 'managerUserNm']);return false;"><spring:message code="button.delete"/></a>
						</td>
						<th scope="row"><label for="bscUserNm"><spring:message code="word.meticInCharge"/></label></th>
						<td>
							<form:hidden path="bscUserId"/>
							<a href="#" class="btn-search" onclick="popSearchUser('bscUserId', 'bscUserNm');return false;"><spring:message code="button.search"/></a><form:input path="bscUserNm" class="t-box02" readonly="true"/><a href="#" class="close" onclick="resetInput(['bscUserId', 'bscUserNm']);return false;"><spring:message code="button.delete"/></a>
						</td>
					</tr>
					<tr>
						<th scope="row"><label for="resetAllYn1"><spring:message code="word.actUserReset"/></label></th>
						<td colspan="3" class="hx50">
							<c:forEach items="${codeUtil:getCodeList('011')}" var="item" varStatus="status">
								<c:set var="cls" value=""/>
								<c:if test="${status.index eq 0}"><c:set var="cls" value="ml0"/></c:if>
								<form:radiobutton path="resetAllYn" id="resetAllYn${status.index}" value="${item.codeId}"/><label class="${cls}" for="resetAllYn${status.index}"><span></span>${item.codeNm}</label>
							</c:forEach>
							<label class="red mt10 block"><spring:message code="bsc.base.scDeptMng.scDeptMngForm.info3"/></label>
						</td>
					</tr>
					<tr>
						<th scope="row"><label for="useYn"><spring:message code="word.useYn"/></label></th>
						<td>
							<form:select path="useYn" class="select" items="${codeUtil:getCodeList('011')}" itemLabel="codeNm" itemValue="codeId">
							</form:select>
						</td>
						<th scope="row"><label for="sortOrder"><spring:message code="word.sortOrder"/></label><span class="red">(*)</span></th>
						<td><form:input path="sortOrder" class="t-box01"/></td>
					</tr>
				</tbody>
			</table>
		</div>
		<div class="tbl-bottom">
			<div class="tbl-wbtn">
			</div>
			<div class="tbl-btn">
				<a href="#" class="save" onclick="saveData();return false;"><spring:message code="button.save"/></a>
			</div>
		</div>
	</div>
</form:form>
