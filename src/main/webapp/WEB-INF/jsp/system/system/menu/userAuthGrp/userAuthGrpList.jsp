<!-- 
*************************************************************************
* CLASS 명	: UserAuthGrpList
* 작 업 자	: 현걸욱
* 작 업 일	: 2018-1-19
* 기	능	: 사용자권한그룹설정 List
* ---------------------------- 변 경 이 력 --------------------------------
* 번호	작 업 자		작	업	일			변 경 내 용				비고
* ----	---------	----------------	---------------------	-----------
*	1	현걸욱		2018-1-19				최 초 작 업
**************************************************************************
-->
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/common-taglibs.jsp"%>
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
	
	$("#userList").on("dblclick","select",function(){
		addRow();
	});
	
	$("#userSelectedList").on("dblclick","select",function(){
		delRow();
	});
	
	$("#findAuthGubun").on("change",function(){
		if($(this).val()!="01" && $(this).val()!="90"){
			$(".tbl-btn a").show();
		}else{
			$(".tbl-btn a").show();
		}
	}).change();
	
});

// 조직도 조회
function reloadDeptList() {
	
	sendAjax({
		"url" : "${context_path}/system/system/menu/userAuthGrp/deptList_json.do",
		"data" : getFormData("form"),
		"doneCallbackFunc"	: "makeDeptList"
	});
}

// 성과조직 tree 생성
function makeDeptList(data) {
	
	var $list = $("#deptList1").empty();
	var $dept;
	$("#deptTitle1").text("<spring:message code="word.scDeptNm"/>");
	
	$(data.list).each(function(i, e) {
		$dept = $("<li id='dept1_" + e.deptId + "'>");
		
		if(isEmpty(e.upDeptId)) {
			if(isEmpty($("#baseDeptId").val())) {
				$("#baseDeptId").val(e.deptId);
			}
			$dept.addClass("deptRoot1").appendTo($list);
		} else {
			$dept.appendTo($list.find("#upDept1_" + e.upDeptId));
		}
		$dept.append("<a href='#' onclick='return false;'><span onclick='getUserList(\"" + e.deptId + "\",\"N\", event);return false'>" + e.deptNm + "</span></a>");
		if(e.isLeaf == "N") {
			$dept.append("<ul id='upDept1_" + e.deptId + "'>");
			$dept.children("a").addClass("active");
		} else {
			$dept.addClass("childn");
		}
	});
	
	if(isNotEmpty($("#baseDeptId").val())) {
		getUserList($("#baseDeptId").val(),"Y", null);
	}
}

// 매핑정보 조회
function getUserList(deptId, initYn, e) {
	if(e != null) {
		e.stopPropagation();
	}
	$("#initYn").val(initYn);
	$("#baseDeptId").val(deptId);
	
	sendAjax({
		"url" : "${context_path}/system/system/menu/userAuthGrp/userList_json.do",
		"data" : getFormData("form"),
		"doneCallbackFunc"	: "makeUserList"
	});
}

// 매핑정보 체크
function makeUserList(json) {
	
	if(json != null){
		makeSearchUserList(json.userList);
		if($("#initYn").val() == 'Y'){
			makeSelectedUserList(json.selectedUserList);	
		}
	}
}

function makeSearchUserList(data){
	$("#userList").empty();
	var $select = $("<select></select>").attr({id:"userData",name:"userData",multiple:"multiple",style:"width:100%;height:500px;"});
	if(data.length > 0){
		for(var i=0; i<data.length ; i++){
			$("<option></option>").val(data[i].userId).text(data[i].userNm+"["+data[i].deptNm+"]").data("deptId",data[i].deptId).appendTo($select);
		}
	}
	$("#userList").append($select);
}

function makeSelectedUserList(data){
	$("#userSelectedList").empty();
	var $select = $("<select></select>").attr({id:"userSelectedData",name:"userSelectedData",multiple:"multiple",style:"width:100%;height:500px;"});
	if(data.length > 0){
		for(var i=0; i<data.length ; i++){
			$("<option></option>").val(data[i].userId).text(data[i].userNm+"["+data[i].deptNm+"]").data("deptId",data[i].deptId).appendTo($select);
		}
	}
	$("#userSelectedList").append($select);
}

function addRow(){
	
	var isExist = false;
	
	$("select[name='userData'] option:selected").each(function(i,v){
		if($("select[name='userSelectedData'] option[value='"+$(this).val()+"']").length > 0){
			isExist = true;
		}else{
			$("<option></option>").val($(this).val()).text($(this).text()).data("deptId",$(this).data("deptId")).appendTo("#userSelectedData");
			$(this).remove();
		}
	});
	
	if(isExist)$.showMsgBox('<spring:message code="common.alreadySelectedData.msg" />',null);
}

function delRow(){
	
	$("select[name='userSelectedData'] option:selected").each(function(i,v){
		$(this).remove();
		$("<option></option>").val($(this).val()).text($(this).text()).data("deptId",$(this).data("deptId")).appendTo("#userData");
	});
	
}

// 매핑정보 저장
function save() {
	if($("select[name='userSelectedData'] option").length == 0
			&& $("#findAuthGubun").val() == "<spring:eval expression="@globals.getProperty('Admin.AuthCodeId')"></spring:eval>") {
		$.showMsgBox("<spring:message code="system.system.menu.userAuthGrp.error1"/>");
		return false;
	}
	
	var $form = $("#form")[0];
	$("[name='deptSelectedData']").remove();

	$("select[name='userSelectedData'] option").prop("selected", true);
	$("select[name='userSelectedData'] option").each(function(){
		$("<input type=\"hidden\" name=\"deptSelectedData\" />").val($(this).data("deptId")).appendTo($form);
	});
	sendAjax({
		"url" : "${context_path}/system/system/menu/userAuthGrp/saveUserAuthGrp.do",
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

</script>

<form:form commandName="searchVO" id="form" name="form" method="post">
	<form:hidden path="baseDeptId"/>
	<form:hidden path="userSelectedIds"/>
	<input type="hidden" id="initYn"/>
	<div class="sch-bx">
		<ul>
			<li>
				<label for="findAuthGubun"><spring:message code="word.auth"/></label>
				<form:select path="findAuthGubun" class="select" items="${codeUtil:getCodeList('018')}" itemLabel="codeNm" itemValue="codeId">
				</form:select>
			</li>
		</ul>
		<a href="#" class="btn-sch" onclick="searchList();return false;"><spring:message code="button.search"/></a>
	</div>
	<div class="btn-dw">
		<%--
		<a href="#" class="excel" onclick="excelDownload();return false;"><span><spring:message code="button.excelDownload"/></span></a>
		--%>
	</div>
	<div class="table-bx-top">
		<div class="table-bx2">
			<div class="mapp">
				<p id="deptTitle1"><spring:message code="word.scDeptNm"/></p>
				<ul id="deptList1" style="height:500px;"></ul>
				<a href="#" class="aopen" onclick="openAll('deptList1');return false;"><span><spring:message code="button.allOpen"/></span></a>
				<a href="#" class="aclose" onclick="closeAll('deptList1');return false;"><span><spring:message code="button.allClose"/></span></a>
			</div>
		</div>
		<div class="table-bx3">
			<div class="moveLeft">
				<p id="userTitle"><spring:message code="word.emp"/>
				<ul id="userList">
				</ul>
			</div>
			<div class="moveCenter">
				<div><input type="button" class="goRight" id="goRight" title="<spring:message code="button.add"/>" onclick="addRow();return false;"></div>
				<br />
				<div><input type="button" class="goLeft" id="goLeft" title="<spring:message code="button.delete"/>" onclick="delRow();return false;"></div>
			</div>
			<div class="moveRight">
				<p id="userSelectedTitle"><spring:message code="word.selectedEmp"/></p>
				<ul id="userSelectedList">
				</ul>
			</div>
		</div>
	</div>	
	<div class="tbl-bottom tbl-bottom2 mt0">
		<div class="tbl-btn">
			<a href="#" class="save" onclick="save();return false;"><spring:message code="button.save"/></a>
		</div>
	</div>
</form:form>


