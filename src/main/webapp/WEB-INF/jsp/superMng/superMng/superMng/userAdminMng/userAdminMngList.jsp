<!-- 
*************************************************************************
* CLASS 명	: UserAdminMngList
* 작 업 자	: 박정현
* 작 업 일	: 2018-07-04
* 기	능	: 사용자관리 List
* ---------------------------- 변 경 이 력 --------------------------------
* 번호	작 업 자		작	업	일			변 경 내 용				비고
* ----	---------	----------------	---------------------	-----------
*	1	박정현		2018-07-04				최 초 작 업
**************************************************************************
-->
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/common-taglibs.jsp"%>
<script type="text/javascript">
$(function(){
	getDeptList();
	
	$("#list").jqGrid({
		url			:	"${context_path}/superMng/superMng/superMng/userAdminMng/userAdminMngList_json.do",
		postData	:	getFormData("form"),
		width		:	"${jqgrid_width}",
		height		:	"${jqgrid_height}",
		colModel	:	[
							{name:"compId",		index:"compId",		hidden:true},
							{name:"jikgubId",	index:"jikgubId",	hidden:true},
							{name:"posId",		index:"posId",		hidden:true},
							{name:"deptId",		index:"deptId",		hidden:true},
							{name:"deptNm",		index:"deptNm",		width:150,	align:"left",	label:"<spring:message code="word.belongDept"/>"},
							{name:"userId",		index:"userId",		width:100,	align:"center",	label:"<spring:message code="word.empNum"/>"},
							{name:"userNm",		index:"userNm",		width:100,	align:"center",	label:"<spring:message code="word.name2"/>"},
							{name:"jikgubNm",	index:"jikgubNm",	width:80,	align:"center",	label:"<spring:message code="word.jikgub"/>"},
							{name:"posNm",		index:"posNm",		width:80,	align:"center",	label:"<spring:message code="word.pos"/>"},
							{name:"authGubunNm",index:"authGubunNm",width:120,	align:"center",	label:"<spring:message code="word.auth"/>"},
							{name:"loginBt",	index:"loginBt",	width:100,	align:"center",	label:"<spring:message code="word.login"/>",
								formatter:function(cellvalue, options, rowObject) {
									return "<input type='button' class='pButton3_grid'  value='<spring:message code="button.login"/>' onclick='login(\"" + rowObject.compId + "\", \"" + rowObject.userId + "\");return false;' />";
								}, unformat:inputUnformatter
							}
						],
		rowNum		: ${jqgrid_rownum},
		pager		: "pager",
		sortname	: "userId",
		sortorder	: "asc",
		cellEdit	: true,
		multiselect	: false,
		loadComplete : function() {
			$("#list").rowspan("list",4,5);
			$("#list").rowspan("list",6,5);
			$("#list").rowspan("list",7,5);
			$("#list").rowspan("list",8,5);
			$("#list").rowspan("list",10,5);
			
			$("#list").rowspan("list",5,5);
		}
	});
	
	$("#findUserNm").keyup(function(e) {
		if(e.keyCode == 13) searchList();
	});
});

// 목록 조회
function searchList() {
	reloadGrid("list", "form");
}

// 조직 조회
function getDeptList() {
	sendAjax({
		"url" : "${context_path}/superMng/superMng/superMng/userAdminMng/selectDeptList.do",
		"data" : getFormData("form"),
		"doneCallbackFunc" : "makeScDeptList"
	});
}

// 조직, 권한 조회
function getDeptAuthList() {
	$("#findAuthId option").remove();
	
	sendAjax({
		"url" : "${context_path}/superMng/superMng/superMng/userAdminMng/selectDeptAuthList.do",
		"data" : getFormData("form"),
		"doneCallbackFunc" : function(data) {
			if(isEmpty(data.authList)) {
				$("<option>").val("").text("<spring:message code="word.none"/>").appendTo("#findAuthId");
			} else {
				$("<option>").val("").text("<spring:message code="word.all"/>").appendTo("#findAuthId");
				for(var i in data.authList) {
					$("<option>").val(data.authList[i].authGubun).text(data.authList[i].authGubunNm).appendTo("#findAuthId");
				}
			}
			
			makeScDeptList(data);
		}
	});
}

// 로그인
function login(compId, userId) {
	var f = document.form;
	f.compId.value = compId;
	f.userId.value = userId;
	f.action = "${context_path}/loginProcess.do";
	f.submit();
}
</script>
<form:form commandName="searchVO" id="form" name="form" method="post">
	<form:hidden path="userId"/>
	<form:hidden path="findYear"/>
	<form:hidden path="findDeptId"/>
	<input type="hidden" name="compId"/>
	<input type="hidden" name="superLoginKey" value="${sessionScope.SUPER_LOGIN_KEY}"/>

	<div class="sch-bx">
		<ul>
			<li>
				<label for="findCompId"><spring:message code="word.compNm"/></label>
				<form:select path="findCompId" class="wx150" title="${messageArg}" onchange="getDeptAuthList();">
					<form:options items="${compList}" itemValue="compId" itemLabel="compNm"></form:options>
				</form:select>
			</li>
			<li>
				<label><spring:message code="word.belongDept"/></label>
			</li>
			<li>
				<div class="sch-oga forDeptList useFindAll" id="findDeptList"></div>
			</li>
			<li>
				<label for="findAuthId"><spring:message code="word.auth"/></label>
				<form:select path="findAuthId" class="select wx150">
					<option value=""><spring:message code="word.all"/></option>
					<form:options items="${authList}" itemValue="authGubun" itemLabel="authGubunNm"></form:options>
				</form:select>
			</li>
			<li>
				<label for="findUserNm"><spring:message code="word.name2"/></label>
				<form:input path="findUserNm" class="wx150"/>
			</li>
		</ul>
		<a href="#" class="btn-sch" onclick="searchList();return false;"><spring:message code="button.search"/></a>
	</div>
	<div class="btn-dw">
	</div>
	<div class="gridContainer">
		<table id="list"></table>
		<div id="pager"></div>
	</div>
</form:form>
