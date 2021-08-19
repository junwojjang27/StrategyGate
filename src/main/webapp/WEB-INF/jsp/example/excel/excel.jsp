<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/common-taglibs.jsp"%>
<script type="text/javascript">
$(function(){
	/******************************************
	* 데이터 그리드 출력
	******************************************/
	$("#list").jqGrid({
		url			:	"${context_path}/example/excel/excelList_json.do",
		postData	:	getFormData("form"),
		width		:	"${jqgrid_width}",
		height		:	"${jqgrid_height}",
		rowNum		:	"${jqgrid_rownum_max}",
		colModel	:	[
							{name:"year",			index:"year",		hidden:true},
							{name:"scDeptId",		index:"scDeptId",	width:100,		align:"center",	label:"<spring:message code="word.orgCode"/>",	key:true},
							{name:"scDeptNm",		index:"scDeptNm",	width:200,		align:"left",	label:"<spring:message code="word.orgNm"/>"},
							{name:"scDeptGrpId",	index:"scDeptGrpId",	width:150,	align:"left",	label:"<spring:message code="word.evalGrp"/>",
								editable:true,	formatter:"select",	edittype:"select",
								editoptions:{value:{
									<c:forEach var="item" items="${codeUtil:getCodeListByYear('003', '2017')}" varStatus="status">"${item.codeId}":"${item.codeNm}"<c:if test="${not status.last}">,</c:if></c:forEach>
								}}
							},
							{name:"bscUserId",		index:"bscUserId",	hidden:true},
							{name:"bscUserNm",		index:"bscUserNm",	width:100,		align:"left",	label:"<spring:message code="word.manager2"/>"},
							{name:"managerUserId",	index:"managerUserId",	hidden:true},
							{name:"managerUserNm",	index:"managerUserNm",	width:100,		align:"left",	label:"<spring:message code="word.meticInCharge"/>"},
							{name:"sortOrder",		index:"sortOrder",		width:50,		align:"right",	label:"<spring:message code="word.sortOrder"/>", sorttype:"number",
								editable:true,		editrules:{integer:true}, editoptions:{maxlength:3}
							},
							{name:"useYn",			index:"useYn",			width:50,		align:"center",	label:"<spring:message code="word.useYn"/>",
								editable:true,	formatter:"select",	edittype:"select",
								editoptions:{value:{
									<c:forEach var="item" items="${codeUtil:getCodeList('011')}" varStatus="status">"${item.codeId}":"${item.codeNm}"<c:if test="${not status.last}">,</c:if></c:forEach>
								}}
							},
							{name:"content",		index:"content",		width:200,		align:"left",	label:"<spring:message code="word.etc"/>",
								editable:true,		edittype:"textarea",	editrules:{custom:true, custom_func:jqGridChkBytes},	editoptions:{maxlength:4000}
							}
						],
		sortname	: "sortOrder",
		sortorder	: "asc",
		//cellEdit	: true,
		treeGridModel	: "adjacency",
		ExpandColumn	: "scDeptNm",
		ExpandColClick	: true,
		tree_root_level	: 1,
		treeGrid	: true,
		treeReader	: {
			level_field		:	"level",
			parent_id_field	:	"upScDeptId",
			leaf_field		:	"isLeaf",
			expanded_field	:	"expandYn"
		}
	});
});

function excelFormDownload() {
	var f = document.form;
	f.action = "${context_path}/example/excel/excelFormDownload.do";
	f.submit();
}

function excelUpload() {
	openFancybox({"url" : "${context_path}/example/excel/popExcelUpload.do", "data" : {"findYear" : $("#findYear").val()}});
}
</script>

<form:form commandName="searchVO" id="form" name="form" method="post">
	<div class="sch-bx">
		<ul>
			<li>
				<label for="findYear"><spring:message code="word.year"/></label>
				<form:select path="findYear" class="select wx100" items="${codeUtil:getCodeList('017')}" itemLabel="codeNm" itemValue="codeId">
				</form:select>
			</li>
			<li>
				<label for="findUseYn"><spring:message code="word.useYn"/></label>
				<form:select path="findUseYn" class="select" items="${codeUtil:getCodeList('011')}" itemLabel="codeNm" itemValue="codeId">
				</form:select>
			</li>
		</ul>
		<a href="#" class="btn-sch" onclick="reloadGrid('list');return false;">검색</a>
	</div>
	<div class="btn-dw">
		<a href="#" class="excel" onclick="excelFormDownload();return false;"><span>양식 다운로드</span></a>
		<a href="#" class="excel" onclick="excelUpload();return false;"><span>엑셀 업로드</span></a>
	</div>
	<div class="gridContainer">
		<table id="list"></table>
	</div>
</form:form>

<div class="devGuide">
	<h3>엑셀 업로드 관련 설정</h3>
	<ol>
		<li>
			<dl>
				<dt>
					업로드 팝업 호출
				</dt>
				<dd>
					<span class="devGuideSource">openFancybox({"url" : "&#36{context_path}/example/excel/popExcelUpload.do", "data" : {"findYear" : $("#findYear").val()}});</span>
					<br/>
					팝업 URL과 전송할 데이터를 { }에 선언해서 호출
				</dd>
			</dl>
		</li>
		<li>
			<dl>
				<dt>
					파일 모듈 설정
				</dt>
				<dd>
					<span class="devGuideSource">var upload1;</span>
					<span class="devGuideSource"><br/>$(function() {</span>
					<span class="devGuideSource"><br/><span class="tab"></span>upload1 = new SGFileUploader({</span>
					<span class="devGuideSource"><br/><span class="tab"></span><span class="tab"></span>"targetId" : "divFile1",</span>	// 파일모듈을 생성할 dom id
					<span class="devGuideSource"><br/><span class="tab"></span><span class="tab"></span>"inputName" : "fileNm",</span>	// 파일모듈의 name
					<span class="devGuideSource"><br/><span class="tab"></span><span class="tab"></span>"maxFileCnt" : 1,</span>	// 최대 첨부 파일 수
					<span class="devGuideSource"><br/><span class="tab"></span><span class="tab"></span>"allowFileExts": ["xls", "xlsx"],</span>	// 허용하는 확장자들
					<span class="devGuideSource"><br/><span class="tab"></span><span class="tab"></span>"maxTotalSize" : 5242880</span>	// 최대 용량
					<span class="devGuideSource"><br/><span class="tab"></span>});</span>
					<span class="devGuideSource"><br/>});</span>
				</dd>
			</dl>
		</li>
		<li>
			<dl>
				<dt>
					파일 업로드
				</dt>
				<dd>
					<span class="devGuideSource">sendMultipartForm({</span>
					<span class="devGuideSource"><br/><span class="tab"></span>"url" : "&#36{context_path}/example/excel/excelUploadProcess.do",</span>	// 파일 업로드 URL
					<span class="devGuideSource"><br/><span class="tab"></span>"formId" : "formPop",</span>	// 전송할 form id
					<span class="devGuideSource"><br/><span class="tab"></span>"fileModules" : [upload1],</span>	// 전송할 파일모듈
					<span class="devGuideSource"><br/><span class="tab"></span>"doneCallbackFunc" : "uploadDone"</span>	// 성공시 호출할 callback 함수 이름
					<span class="devGuideSource"><br/>});</span>
				</dd>
			</dl>
		</li>
		<li></li>
	</ol>
</div>
