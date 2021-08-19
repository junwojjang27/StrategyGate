<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/common-taglibs.jsp"%>
<validator:javascript formName="scDeptVO" staticJavascript="false" xhtml="true" cdata="false"/>
<script type="text/javascript">
$(function(){
	reloadDeptList();
	
	$("#deptList1").on("click", "a", function(e) {
		if($(this).hasClass("active")) {
			$(this).removeClass("active");
		} else {
			$(this).addClass("active");
		}
	});
	$("#deptList2").on("click", "input, label", function(e) {
		e.stopPropagation();
	});
	$("#deptList2").on("click", "p", function(e) {
		e.stopPropagation();
		if($(this).hasClass("active")) {
			$(this).removeClass("active");
		} else {
			$(this).addClass("active");
		}
	});
});

// 조직도 조회
function reloadDeptList() {
	sendAjax({
		"url" : "${context_path}/bsc/base/scDept/scDeptMapping/scDeptList_json.do",
		"data" : getFormData("form"),
		"doneCallbackFunc"	: "makeDeptTree"
	});
}

// 성과조직 tree 생성
function makeDeptTree(data) {
	var findMappingBase = $("#findMappingBase").val();
	var $list = $("#deptList1").empty();
	var $dept;
	
	if(findMappingBase == "1") {
		$("#deptTitle1").text("<spring:message code="word.scDeptNm"/>");
		$("#deptTitle2").text("<spring:message code="word.personnelOrg"/>");
	} else {
		$("#deptTitle1").text("<spring:message code="word.personnelOrg"/>");
		$("#deptTitle2").text("<spring:message code="word.scDeptNm"/>");
	}
	
	$(findMappingBase=="1" ? data.scDeptList : data.deptList).each(function(i, e) {
		$dept = $("<li id='dept1_" + e.deptId + "'>");
		if(e.mappingCnt == 0) {
			$dept.addClass("red");
		}
		if(isEmpty(e.upDeptId)) {
			if(isEmpty($("#baseDeptId").val())) {
				$("#baseDeptId").val(e.deptId);
			}
			$dept.addClass("deptRoot1").data("deptId", e.deptId).appendTo($list);
		} else {
			$dept.appendTo($list.find("#upDept1_" + e.upDeptId));
		}
		$dept.append("<a href='#' onclick='return false;'><span onclick='getMappingList(\"" + e.deptId + "\", event);return false'>" + e.deptNm + "</span></a>");
		if(e.isLeaf == "N") {
			$dept.append("<ul id='upDept1_" + e.deptId + "'>");
			$dept.addClass("hasChild");
			$dept.children("a").addClass("active");
		}
	});
	
	// 인사조직 tree
	$list = $("#deptList2").empty();
	$(findMappingBase=="1" ? data.deptList : data.scDeptList).each(function(i, e) {
		$dept = $("<li id='dept2_" + e.deptId + "'>");
		if(e.mappingCnt == 0) {
			$dept.addClass("red");
		}
		if(isEmpty(e.upDeptId)) {
			$dept.addClass("deptMappingRoot").data("deptId", e.deptId).appendTo($list);
		} else {
			$dept.appendTo($list.find("#upDept2_" + e.upDeptId));
		}
		$dept.append("<p><input type='checkbox' name='deptIds' id='chkDept_" + e.deptId + "' value='" + e.deptId + "' disabled='disabled'/><label for='chkDept_" + e.deptId + "'>" + e.deptNm + "</label></p>");
		if(e.isLeaf == "N") {
			$dept.append("<ul id='upDept2_" + e.deptId + "'>");
			$dept.addClass("hasChild");
			$dept.children("p").addClass("active");
		}
	});
	
	if(isNotEmpty($("#baseDeptId").val())) {
		getMappingList($("#baseDeptId").val(), null);
	}
}

// 매핑정보 조회
function getMappingList(deptId, e) {
	if(e != null) {
		e.stopPropagation();
	}
	$("#baseDeptId").val(deptId);
	$("#deptList1 .active2").removeClass("active2");
	$("#dept1_" + deptId + " > a").addClass("active2");
	
	sendAjax({
		"url" : "${context_path}/bsc/base/scDept/scDeptMapping/scDeptMappingList_json.do",
		"data" : getFormData("form"),
		"doneCallbackFunc"	: "chkMappingList"
	});
}

// 매핑정보 체크
function chkMappingList(json) {
	var findMappingBase = $("#findMappingBase").val();
	$("#deptList2").find(":checkbox").prop("checked", false).prop("disabled", true);
	$("#deptList2 li.red > p").children(":checkbox").prop("disabled", false);
	
	$(json.list).each(function(i, e) {
		$("#chkDept_" + (findMappingBase=="1" ? e.deptId : e.scDeptId)).prop("checked", true).prop("disabled", false);
	});
}

// 매핑정보 저장
function saveMapping() {
	if(isEmpty($("#baseDeptId").val())) {
		$.showMsgBox(getMessage("errors.select", $("#deptTitle1").text()));
		return false;
	}
	
	// 인사조직은 성과조직을 1곳만 선택할 수 있음
	if($("#findMappingBase").val() == "2"
			&& $("[name=deptIds]:checked").length > 1) {
		$.showMsgBox("<spring:message code="bsc.base.scDeptMapping.scDeptMappingModify.error"/>");
		return false;
	}
	
	sendAjax({
		"url" : "${context_path}/bsc/base/scDept/scDeptMapping/saveMapping.do",
		"data" : getFormData("form"),
		"doneCallbackFunc"	: "checkResult"
	});
}

function checkResult(data) {
	$.showMsgBox(data.msg);
	if(data.result == AJAX_SUCCESS) {
		reloadDeptList();
	}
}

// 목록 조회
function searchList() {
	$("#baseDeptId").val("");
	reloadDeptList();
}

// 전체닫음
function closeAll(id) {
	$("#" + id).find(".active").removeClass("active");
}

// 전체펼침
function openAll(id) {
	$("#" + id).find(id=="deptList1"?"a":"p").addClass("active");
}

// 엑셀 다운로드
function excelDownload() {
	var f = document.form;
	f.action = "${context_path}/bsc/base/scDept/scDeptMapping/scDeptMappingListExcel.do";
	f.submit();
}
</script>

<form:form commandName="searchVO" id="form" name="form" method="post">
	<form:hidden path="baseDeptId"/>
	<div class="sch-bx">
		<ul>
			<li>
				<label for="findYear"><spring:message code="word.year"/></label>
				<form:select path="findYear" class="select wx100" items="${codeUtil:getCodeList('017')}" itemLabel="codeNm" itemValue="codeId">
				</form:select>
			</li>
			
			<li>
				<label for="findMappingBase"><spring:message code="word.mappingBase"/></label>
				<form:select path="findMappingBase" class="select" items="${codeUtil:getCodeList('133')}" itemLabel="codeNm" itemValue="codeId">
				</form:select>
			</li>
		</ul>
		<a href="#" class="btn-sch" onclick="searchList();return false;"><spring:message code="button.search"/></a>
	</div>
	<div class="btn-dw">
		<a href="#" class="excel" onclick="excelDownload();return false;"><span><spring:message code="button.excelDownload"/></span></a>
	</div>
	<div class="table-bx2">
		<div class="mapp">
			<p id="deptTitle1"><spring:message code="word.scDeptNm"/></p>
			<ul id="deptList1" class="hx500"></ul>
			<a href="#" class="aopen" onclick="openAll('deptList1');return false;"><span><spring:message code="button.allOpen"/></span></a>
			<a href="#" class="aclose" onclick="closeAll('deptList1');return false;"><span><spring:message code="button.allClose"/></span></a>
		</div>
		<div class="mapp">
			<p id="deptTitle2"><spring:message code="word.personnelOrg"/></p>
			<ul id="deptList2" class="hx500"></ul>
			<a href="#" class="aopen" onclick="openAll('deptList2');return false;"><span><spring:message code="button.allOpen"/></span></a>
			<a href="#" class="aclose" onclick="closeAll('deptList2');return false;"><span><spring:message code="button.allClose"/></span></a>
		</div>
	</div>
	<div class="tbl-bottom tbl-bottom2 mt0">
		<div class="tbl-btn">
			<a href="#" class="save" onclick="saveMapping();return false;"><spring:message code="button.save"/></a>
		</div>
	</div>
</form:form>
