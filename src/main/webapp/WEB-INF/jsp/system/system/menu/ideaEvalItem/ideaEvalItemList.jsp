<!-- 
*************************************************************************
* CLASS 명	: IdeaEvalItemList
* 작 업 자	: 하성준
* 작 업 일	: 2021-09-27
* 기	능	: 평가항목관리 List
* ---------------------------- 변 경 이 력 --------------------------------
* 번호	작 업 자		작	업	일			변 경 내 용				비고
* ----	---------	----------------	---------------------	-----------
*	1	하성준		2021-09-27				최 초 작 업
**************************************************************************
-->

<!--
필독 : 다국어 지원기능에 의해서 화면에 나타나는 한글명칭은 message properties파일을 이용하여 수정할 것. 
 -->

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/common-taglibs.jsp"%>
<validator:javascript formName="ideaEvalItemVO" staticJavascript="false" xhtml="true" cdata="false"/>
<script type="text/javascript">
$(function(){
	$("#list").jqGrid({
		url			:	"${context_path}/system/system/menu/ideaEvalItem/ideaEvalItemList_json.do",
		postData	:	getFormData("form"),
		width		:	"${jqgrid_width}",
		height		:	"400",
		colModel	:	[
						{name:"evalItemCd",	index:"evalItemCd",	width:100,	align:"center",	label:"항목코드", hidden:true},
						{name:"year",	index:"year",	width:100,	align:"center",	label:"기준년도", hidden:true},
						{name:"evalItemTitle",	index:"evalItemTitle",	width:20,	align:"center",	label:"항목이름",
							editable:true, edittype:"text", editrules:{required:true, custom:true, custom_func:jqGridChkBytes},	editoptions:{maxlength:20}
						},
						{name:"evalItemContent",	align:"left",	index:"evalItemContent",	width:100,	label:"항목내용",
							editable:true, edittype:"text", editrules:{required:true, custom:true, custom_func:jqGridChkBytes},	editoptions:{maxlength:100}
						},
						{name:"evalDegreeId",	index:"evalDegreeId",	width:20,	align:"center",	label:"평가차수",
							editable:true, edittype:"select", formatter:'select', editrules:{required:true}, editoptions:{value:getEvalDegreeIdSelect()}
						},
						{name:"weightId",	index:"weightId",	width:20,	align:"center",	label:"가중치",
							editable:true, edittype:"text", editrules:{required:true, custom:true, custom_func:validNum},	editoptions:{maxlength:22}
						},
						{name:"particalTypeId",	index:"particalTypeId",	width:20,	align:"center",	label:"평가자구분",
							editable:true, edittype:"select", formatter:'select', editrules:{required:true}, editoptions:{value:getParticalTypeSelect()}
						},
						{name:"createDt",	index:"createDt",	width:30,	align:"center",	label:"등록날짜"},
						{name:"updateDt",	index:"updateDt",	width:100,	align:"center",	label:"수정일자", hidden:true},
						{name:"deleteDt",	index:"deleteDt",	width:100,	align:"center",	label:"삭제일자", hidden:true}

						],
		rowNum		: ${jqgrid_rownum_max},
		multiselect	: true,
		cellEdit	: true,
		cellsubmit  :'clientArray',
		sortname	: "createDt",
		sortorder	: "asc",
		loadComplete : function() {
		}
	});

	/***** 사용여부 미사용시 삭제 버튼 숨김 *****/
	<c:choose>
	<c:when test="${searchVO.findUseYn == 'N'}">
	$(".delete").hide();
	$(".new").hide();
	$(".save").hide();
	</c:when>
	<c:otherwise>
	$(".delete").show();
	$(".new").show();
	$(".save").show();
	</c:otherwise>
	</c:choose>
	$("#findUseYn").on("change", function() {
		if($(this).val() == "N"){
			$(".delete").hide();
			$(".new").hide();
			$(".save").hide();
		}else{
			$(".delete").show();
			$(".new").show();
			$(".save").show();
		}
	});
	/***** 사용여부 미사용시 삭제 버튼 숨김 end *****/
});

//숫자만 입력가능
function validNum(val, nm, valref){

	if($.isNumeric(val)){
		return [true, ""];
	}else{

		return [false, "숫자만 입력 가능 합니다."];
	}

}

//평가단 선택 여부
function getParticalTypeSelect(){

	var selectStr="";
	<c:forEach var="particalTypeIdList" items="${codeUtil:getCodeList('386')}" varStatus="status">
	selectStr += "<c:out value="${particalTypeIdList.codeId}"/>"+":"+"<c:out value="${particalTypeIdList.codeNm}"/>";
	<c:if test="${not status.last}">
	selectStr += ";";
	</c:if>
	</c:forEach>

	return selectStr;
}

//평가차수 선택 여부
function getEvalDegreeIdSelect(){

	var selectStr="";
	<c:forEach var="evalDegreeIdList" items="${codeUtil:getCodeList('387')}" varStatus="status">
	selectStr += "<c:out value="${evalDegreeIdList.codeId}"/>"+":"+"<c:out value="${evalDegreeIdList.codeNm}"/>";
	<c:if test="${not status.last}">
	selectStr += ";";
	</c:if>
	</c:forEach>

	return selectStr;
}

//추가
function addRow(){
	//var rowId = "newRow"+$.jgrid.guid++;
    var rowId = $("#list").getGridParam("reccount"); // 페이징 처리 시 현 페이지의 Max RowId 값

	/*
	/*조회된 데이터 없을시 나타나는 문구 없앰.
	if($("#list").find(".noGridResult").length > 0){
		$("#list").find(".noGridResult").closest("tr").hide();
	}
	*/

	var rowData = {year:$("#findYear").val(),
		useYn:'Y'}

	$("#list").jqGrid("addRowData", rowId+1, rowData,'last');
	$('#list tr:last').focus();

	/*
	//수정한 것만 저장할 경우 추가시 아래구문이 추가되어야 함. (필수체크목록)
	$("#list").jqGrid("setRowData", rowId, rowData,'edited');
	 */
}

// 목록 조회
function searchList() {
	$("#newForm").hide();
 	reloadGrid("list", "form");
}

// 상세 조회
function showDetail(parameter) {
	var f = document.form;
	f.evalItemCd.value = evalItemCd;
	f.year.value = year;
	f.evalDegreeId.value = evalDegreeId;

	
	sendAjax({
		"url" : "${context_path}/system/system/menu/ideaEvalItem/selectDetail.do",
		"data" : getFormData("form"),
		"doneCallbackFunc" : "setDetail"
	});
}

// 상세 조회 값 세팅
function setDetail(data) {
	$("#newForm").show();
	var dataVO = data.dataVO;
	
	$("#titleIdeaEvalItemNm").text("평가항목관리 : " + dataVO.evalDegreeId);
	
	voToForm(dataVO, "form", ["evalItemCd","evalDegreeId","evalItemTitle","particalTypeId","weightId","createDt","updateDt","deleteDt","evalItemContent"]);
	$("#evalDegreeId").focus();
}

// 정렬순서저장
function saveSortOrder() {
	if(!gridToForm("list", "form", true)) return false;
	
	sendAjax({
		"url" : "${context_path}/system/system/menu/ideaEvalItem/saveSortOrder.do",
		"data" : getFormData("form"),
		"doneCallbackFunc" : "searchList"
	});
}
/*
// 등록
function addData() {
	$("#newForm").show();
	
	$("#titleIdeaEvalItemNm").text("평가항목관리");
	
	resetForm("form", ["evalItemCd","evalDegreeId","evalItemTitle","particalTypeId","weightId","createDt","updateDt","deleteDt","evalItemContent"]);
	$("#year").val($("#findYear").val());
	$("#evalDegreeId").focus();
}
*/
// 저장
function saveData() {
	/*var f = document.form;
	if(!validatePerspectiveVO(f)) {
		return;
	}*/
	if(!gridToFormChanged("list", "form", true)) return false;

	var zero = "0";
	var sum1 = parseInt(zero);
    var sum2 = parseInt(zero);
    var sum3 = parseInt(zero);
    var check = true;
    var temp1, temp2, temp3;


	var num = $("#list").getGridParam("reccount");		//현재 화면에 표시되고있는 레코드 숫자
	var rowData;
	for (var n = 1; n <= num; n++) {	//1 ~ (화면에 표시되고있는 레코드 숫자) 까지 반복하여 ideaCd를 비교해서 해당 제안을 찾음.
		rowData = $("#list").jqGrid("getRowData", n);
		if(rowData.evalDegreeId == "001") {
            temp1 = rowData.weightId;
            sum1 = sum1 + parseInt(temp1);
        }
		else if (rowData.evalDegreeId == "002") {
            temp2 = rowData.weightId;
            sum2 = sum2 + parseInt(temp2);
        }
		else if (rowData.evalDegreeId == "003"){
            temp3 = rowData.weightId;
            sum3 = sum3 + parseInt(temp3);
        }
	}

	if (sum1 != 100 || sum2 != 100 || sum3 != 100) {
		$.showMsgBox("차수별 가중치 총합이 100이 아닙니다.\n\n1차 평가 : " + sum1 + "\n2차 평가 : " + sum2 + "\n최종평가 : " + sum3, null);
		check = false;
	}

    if (!check) {
        return false;
    }

	sendAjax({
		"url" : "${context_path}/system/system/menu/ideaEvalItem/saveIdeaEvalItem.do",
		"data" : getFormData("form"),
		"doneCallbackFunc" : "checkResult"
	});
}

function checkResult(data) {
	$(window).scrollTop(0);	//윈도우 스크롤 맨 위로 이동시키기
	$.showMsgBox(data.msg);
	if(data.result == AJAX_SUCCESS) {
		searchList();
	}
}

// 삭제
function deleteData() {

    var test = $("#list").getGridParam('selarrrow');

    for ( var n = 0; n < test.length; n++ ) {
        rowData = $("#list").jqGrid("getRowData", test[n]);
        if(rowData.weightId != 0) {
            alert(test[n]);
            $.showMsgBox("가중치가 0인 제안만 삭제할 수 있습니다.",null);
            return false;
        }
    }

	if(deleteDataToForm("list", "evalItemCd", "form")) {
		$.showConfirmBox("<spring:message code="common.delete.msg"/>", "doDeleteData");
	}


}

// 삭제 처리
function doDeleteData() {
	/*
	var delList = [];
	$("#form").find("[name=keys]").each(function(i, e) {
		delList.push($(this).val());
	});
	*/
	
	sendAjax({
		"url" : "${context_path}/system/system/menu/ideaEvalItem/deleteIdeaEvalItem.do",
		"data" : getFormData("form"),
		"doneCallbackFunc" : "checkResult"
	});
}
</script>

<form:form commandName="searchVO" id="form" name="form" method="post">
 	<%--<form:hidden path="evalItemCd"/>
	<form:hidden path="year"/>
	<form:hidden path="evalDegreeId"/>
	<form:hidden path="createDt"/>--%>
	<sec:authorize access="hasRole('01')">
		<input type="hidden" id="tableId" name="tableId" value="IDEA_EVAL_ITEM"/>
	</sec:authorize>

	<div class="sch-bx">
		<ul>
			<li>
				<label for="findYear"><spring:message code="word.year"/></label>
				<form:select path="findYear" class="select wx80" items="${codeUtil:getCodeList('017')}" itemLabel="codeNm" itemValue="codeId">
				</form:select>
			</li>
			<li>
				<label for="findEvalDegreeId">평가차수</label>
				<form:select path="findEvalDegreeId" class="select wx80" items="${codeUtil:getCodeList('387')}" itemLabel="codeNm" itemValue="codeId">
				</form:select>
			</li>
			<li>
				<label for="findUseYn"><spring:message code="word.useYn"/></label>
				<form:select path="findUseYn" class="select wx80" items="${codeUtil:getCodeList('011')}" itemLabel="codeNm" itemValue="codeId">
				</form:select>
			</li>

		</ul>
		<a href="#" class="btn-sch" onclick="searchList();return false;"><spring:message code="button.search"/></a>
	</div>
	<div class="gridContainer">
		<table id="list"></table>
		<div id="pager"></div>
	</div>
	<div class="tbl-bottom tbl-bottom2">
		<div class="tbl-btn">
			<a href="#" class="new" onclick="addRow();return false;"><spring:message code="button.add"/></a>
			<a href="#" class="save" onclick="saveData();return false;"><spring:message code="button.save"/></a>
			<a href="#" class="delete" onclick="deleteData();return false;"><spring:message code="button.delete"/></a>
		</div>
	</div>
	<div class="page-noti">
		<ul>
			<li>제안평가에 사용 중인 평가항목은 추가,수정,삭제 할 수 없습니다.</li>
			<li>가중치의 총합은 100%가 되어야 합니다.</li>
		</ul>
	</div>

</form:form>

