<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/common-taglibs.jsp"%>
<validator:javascript formName="ideaSingleVO" staticJavascript="false" xhtml="true" cdata="false"/>
<script type="text/javascript">
	$(function(){
		$("#list").jqGrid({
			url			:	"${context_path}/system/system/menu/ideaSingle/ideaSingleList_json.do",
			postData	:	getFormData("form"),
			width		:	"${jqgrid_width}",
			height		:	"${jqgrid_height}",
			colModel	:	[
				{name:"year",	index:"year",	width:30, hidden:true},
				{name:"ideaCd",	index:"ideaCd",	width:30, hidden:true},
				{name:"state",		index:"state",	hidden:true},
				{name:"category",	index:"category",	width:30,	align:"center",	label:"<spring:message code="word.category"/>"},
				{name:"title",	index:"title",	width:200,	align:"center",	label:"<spring:message code="word.title"/>",
					formatter:function(cellvalue, options, rowObject) {//(넘어온값, )
						return "<a href='#' onclick='showDetail(\"" + removeNull(rowObject.ideaCd)+ "\");return false;'>" + escapeHTML(removeNull(cellvalue)) + "</a>";
					},//formatter : 색깔이나 액션을 주는 (데이터 가공) / 제목에 링크, 돋보기 표시 등
					unformat:linkUnformatter
				},
				{name:"userNm",	index:"userNm",	width:50,	align:"center",	label:"<spring:message code="word.insertUser"/>"},
				{name:"deptNm",	index:"deptNm",	width:50,	align:"center",	label:"<spring:message code="word.deptNm"/>"},
				{name:"createDt",	index:"creatDt",	width:50,	align:"center",	label:"<spring:message code="word.insertDT"/>"},
			],
			pager		: "pager",
			rowNum		: 10,
			sortname	: "sortOrder",
			sortorder	: "asc",
			cellEdit	: true,
			multiselect	: true,
			loadComplete : function() {
			}
		});

		$("#newForm").hide();
	});
	// 목록 조회
	function searchList() {
		$("#newForm").hide();
		reloadGrid("list", "form");
	}
	// 상세 조회
	function showDetail(ideaCd, year) {
		var f = document.form;
		f.ideaCd.value = ideaCd;
		f.year.value = year;

		sendAjax({
			"url" : "${context_path}/system/system/menu/ideaSingle/selectDetail.do",
			"data" : getFormData("form"),
			"doneCallbackFunc" : "setDetail"
		});
	}
	// 상세 조회 값 세팅
	function setDetail(data) {
		$("#newForm").show();
		var dataVO = data.dataVO;

		voToForm(dataVO, "form", ["title","year","category"]);
		$("#content").val(dataVO.content);
		$("#content").focus();
	}
	// 등록
	function addData() {
		$("#newForm").show();
		resetForm("form", ["category","title","userNm","DeptNm","createDt","content"]);
		$("#year").val($("#findYear").val());
		$("#title").focus();
		showBytes("content", "contentBytes");
	}
	// 저장
	function saveData() {
		var f = document.form;
		if(!validateIdeaSingleVO(f)) {
			return;
		}
		if($("#ideaCd").val() != null) //제안을 수정하는 경우
			checkState();//진행상태 체크

		if(isUse){ //검토가 완료된 제안을 수정하는 경우 수정불가
			$.showMsgBox("검토가 완료된 제안은 수정할 수 없습니다.",null); //메세지 수정 必
			return false;
		}
		sendAjax({
			"url" : "${context_path}/system/system/menu/ideaSingle/saveIdeaSingle.do",
			"data" : getFormData("form"),
			"doneCallbackFunc" : "searchList"
			//"doneCallbackFunc" : "checkResult",
			//"failCallbackFunc" : "checkResult"
		});
	}
	//저장 callback
	function checkResult(data) {
		$(window).scrollTop(0);
		$.showMsgBox(data.msg);
		if(data.result == AJAX_SUCCESS) {
			searchList();
		}
	}
	// 삭제
	function deleteData() {

		var ids = $("#list").jqGrid("getGridParam", "selarrrow"); //선택된 jqgrid의 행을 배열로 가져온다.
		var rowData;
		var isUse = false;
		$(ids).each(function(i,v){ //배열의 처음부터 마지막까지 반복한다.
			rowData = $("#list").jqGrid("getRowData",v);
			if(rowData.state == '001'){// 검토 진행상태를 확인한다. 001:대기 002:승인 003:반려
				isUse = true;
			}
		});

		if(isUse){
			$.showMsgBox("검토가 완료된 제안은 삭제할 수 없습니다.",null); //메세지 수정 必
			return false;
		}

		if(deleteDataToForm("list", "ideaCd", "form")) {
			$.showConfirmBox("<spring:message code="common.delete.msg"/>", "doDeleteData");
		}
	}
	// 삭제 처리
	function doDeleteData() {
		/*var delList = [];
		$("#form").find("[name=keys]").each(function(i, e) {
			delList.push($(this).val());
		});*/
		sendAjax({
			"url" : "${context_path}/system/system/menu/ideaSingle/deleteIdeaSingle.do",
			"data" : getFormData("form"),
			"doneCallbackFunc" : "searchList"
		});
	}

	//평가상태
	var isUse;
	function checkState() { //현재 진행상태를 확인하는 함수

		var num = $("#list").getGridParam("reccount");
		var rowData;
		isUse = false;
		for (var n = 1; n <= num; n++) { //jqgrid의 아이디를 1부터 화면에 표시된 개수까지 비교한다.
			rowData = $("#list").jqGrid("getRowData", n); //아이디에 해당하는 행의 데이터를 가져온다.
			if(rowData.ideaCd == $("#ideaCd").val()){ //세부사항을 조회한 제안과 아이디가 동일한 경우
				if (rowData.state == '001') { // 검토 진행상태를 확인한다. 001:대기 002:승인 003:반려
					//제안이 검토가 완료된 상태인 경우 전역변수에 true를 저장한 후 반복을 종료한다.
					isUse = true;
					return;
				}
			}
		}

	}
</script>

<form:form commandName="searchVO" id="form" name="form" method="post">
	<form:hidden path="ideaCd"/>
	<form:hidden path="year"/>
	<form:hidden path="createDt"/>

	<div class="sch-bx">
		<ul>
			<li>
				<label for="findYear"><spring:message code="word.year"/></label>
				<form:select path="findYear" class="select wx100" items="${codeUtil:getCodeList('017')}" itemLabel="codeNm" itemValue="codeId">
				</form:select>
			</li>
			<li>
				<label for="findUseYn"><spring:message code="word.useYn"/></label>
				<form:select path="findUseYn" class="select wx80" items="${codeUtil:getCodeList('011')}" itemLabel="codeNm" itemValue="codeId">
				</form:select>
			</li>
			<li>
				<label for="findCategory"><spring:message code="word.category"/></label>
				<form:select path="findCategory" class="select wx100" items="${codeUtil:getCodeList('385')}" itemLabel="codeNm" itemValue="codeId">
				</form:select>
			</li>
			<li>
				<label for="colName"><spring:message code="word.search" /></label>
				<form:select path="colName" class="select wx100">
					<form:option value="title"><spring:message code="word.title"/></form:option>
					<form:option value="userNm"><spring:message code="word.insertUser"/></form:option>
				</form:select>
				<span class="searchBar"><form:input path="searchKeyword" class="t-box01 wx200" maxlength="20"/> </span>
			</li>
		</ul>
		<a href="#" class="btn-sch" onclick="searchList();return false;"><spring:message code="button.search"/></a>
	</div>
	<div class="btn-dw"></div>
	<div class="gridContainer">
		<table id="list"></table>
		<div id="pager"></div>
	</div>
	<div class="tbl-bottom tbl-bottom2">
		<div class="tbl-btn">
			<a href="#" class="new" onclick="addData();return false;"><spring:message code="button.add"/></a>
			<a href="#" class="delete" onclick="deleteData();return false;"><spring:message code="button.delete"/></a>
		</div>
	</div>
	<div class="page-noti">
		<ul>
			<li></li>
			<li></li>
		</ul>
	</div>

	<div id="newForm">
		<div class="ptitle" id="titleIdeaSingleNm"></div>
		<div class="tbl-type02">
			<table summary="">
				<caption></caption>
				<colgroup>
					<col width="20%"/>
					<col width="80%"/>
				</colgroup>
				<tbody>
				<tr>
					<th scope="row"><label for="category"><spring:message code="word.category"/></label><span class="red">(*)</span></th>
					<td >
						<form:select path="category" class="select wx100" items="${codeUtil:getCodeList('385')}" itemLabel="codeNm" itemValue="codeId">
						</form:select>
					</td>
				</tr>
				<tr>
					<th scope="row"><label for="title"><spring:message code="word.title"/></label><span class="red">(*)</span></th>
					<td ><form:input path="title" class="t-box01"/></td>
				</tr>
				<tr>
					<th scope="row"><label for="content"><spring:message code="word.content" /></label><span class="red">(*)</span></th>
					<td>
						<p><form:textarea path="content" maxlength="4000"/></p>
						<p class="byte"><label id="contentBytes">0</label><label> / 4000byte</label></p>
					</td>
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