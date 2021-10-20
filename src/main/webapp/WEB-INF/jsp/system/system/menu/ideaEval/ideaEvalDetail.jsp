<%--
  Created by IntelliJ IDEA.
  User: junwo
  Date: 2021-10-13
  Time: 오전 10:27
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/common-taglibs.jsp"%>
<script type="text/javascript">
    $(function(){
        $("#list").jqGrid({
            url			:	"${context_path}/system/system/menu/ideaEval/ideaEvalItemList_json.do",
            postData	:	getFormData("form"),
            width		:	"${jqgrid_width}",
            height		:	"(num%10)*30",		//jqGrid크기를 제안 수에 맞게 변경
            colModel	:	[
                {name:"evalItemTitle",	    index:"evalItemTitle",	    width:20,	align:"center",	label:"평가항목"},
                {name:"evalItemContent",	index:"evalItemContent",	width:100,	align:"left",	label:"평가항목설명"},
                {name:"weightId",		    index:"weightId",		    width:20,	align:"center",	label:"가중치(%)"},
                {name:"evalGrade",		    index:"evalGrade",		    width:20,	align:"center",	label:"평가등급", editable: true,  edittype: "select",
                    formatter: 'select', editrules: {required: true, custom: true, custom_func: test}, editoptions: {value: getEvalGradeSelect()}},
                {name:"score",		        index:"score",		        width:20,	align:"center",	label:"평가점수"},
                {name:"year",		    index:"year",		    width:20,	align:"center",	label:"년도" ,hidden: true},
                {name:"evalItemCd",		    index:"evalItemCd",		    width:20,	align:"center",	label:"평가항목코드" ,hidden: true},
                {name:"evalDegreeId",		    index:"evalDegreeId",		    width:20,	align:"center",	label:"평가차수" ,hidden: true},
                {name:"ideaCd",		    index:"ideaCd",		    width:20,	align:"center",	label:"제안코드"  ,hidden: true},
                {name:"ideaGbnCd",		    index:"ideaGbnCd",		    width:20,	align:"center",	label:"제안구분코드"  ,hidden: true},
                {name:"saveYn",		    index:"saveYn",		    width:20,	align:"center",	label:"저장여부"  ,hidden: true},
                {name:"evalSum",		    index:"evalSum",		    width:20,	align:"center",	label:"총점"  ,hidden: true},
                {name:"evalCd",		    index:"evalCd",		    width:20,	align:"center",	label:"평가코드"  },
            ],
            cellEdit: true,
            cellsubmit: 'clientArray',
            loadComplete : function() {
                var num = $("#list").getGridParam("reccount");		//현재 화면에 표시되고있는 레코드 숫자
                var rowData;
                for (var n = 1; n <= num; n++) {	//1 ~ (화면에 표시되고있는 레코드 숫자) 까지 반복하여 ideaCd를 비교해서 해당 제안을 찾음.
                    rowData = $("#list").jqGrid("getRowData", n);
                    if(isNotEmpty(rowData.evalGrade)) {
                        if(rowData.evalGrade == "001") rowData.score = 100;
                        else if(rowData.evalGrade == "002") rowData.score = 80;
                        else if(rowData.evalGrade == "003") rowData.score = 60;
                        else if(rowData.evalGrade == "004") rowData.score = 40;
                        else rowData.score = 20;
                        jQuery("#list").setCell(n, "score", rowData.score);
                        rowData.saveYn = "Y";
                        jQuery("#list").setCell(n, "saveYn", rowData.saveYn);
                    }
                    else {
                        rowData.saveYn = "N";
                        jQuery("#list").setCell(n, "saveYn", rowData.saveYn);
                    }
                    if(isNotEmpty(rowData.evalCd)) {
                        $("#evalCd").val(rowData.evalCd);
                    }
                }

                var count = $("#list").getGridParam("reccount");		//현재 화면에 표시되고있는 레코드 숫자
                rowData = $("#list").jqGrid("getRowData", count);
                if(isNotEmpty(rowData.evalSum)) {
                    $(".edit").hide();
                    $(".save").hide();
                    $.showMsgBox("평가가 완료된 제안입니다.");
                }
                else {
                    $(".edit").show();
                    $(".save").show();
                }

                /**********첨부파일 시작**********/
                var modifyYn = "Y";
                //첨부파일 ajax
                $.ajax({
                    url: "${context_path}/system/system/menu/ideaEval/ideaEvalAtchFileForm.do",
                    data: {
                        "modifyYn": modifyYn,
                        "atchFileId": "${dataVO.atchFileKey}",
                        "_csrf": getCsrf("form")
                    },
                    method: "POST",
                    cache: false,
                    dataType: "html"
                }).done(function (html) {
                    $("#spanAttachFile").html(html);
                }).fail(function (jqXHR, textStatus) {
                    try {
                        var json = JSON.parse(jqXHR.responseText);
                        if (!isEmpty(json.msg)) {
                            $.showMsgBox(json.msg);
                        } else {
                            $.showMsgBox(getMessage("errors.processing"));
                        }
                    } catch (e) {
                        $.showMsgBox(getMessage("errors.processing"));
                    }
                });
                /**********첨부파일 끝**********/
            }
        });


    });


    function test(rowId) {
        var num = $("#list").getGridParam("selrow");
        var rowData = $("#list").jqGrid("getRowData", num);
        if(rowId == "001") rowData.score = 100;
        else if(rowId == "002") rowData.score = 80;
        else if(rowId == "003") rowData.score = 60;
        else if(rowId == "004") rowData.score = 40;
        else rowData.score = 20;

        jQuery("#list").setCell(num, "score", rowData.score)

        rowData.ideaCd = "${dataVO.ideaCd}";
        jQuery("#list").setCell(num, "ideaCd", rowData.ideaCd)

        rowData.ideaGbnCd = "${dataVO.ideaGbnCd}";
        jQuery("#list").setCell(num, "ideaGbnCd", rowData.ideaGbnCd)

        return [true, ""];
    }

    //평가단 선택 여부
    function getEvalGradeSelect() {
        var selectStr = "";
        <c:forEach var="evalGradeList" items="${codeUtil:getCodeList('388')}" varStatus="status">
        selectStr += "<c:out value="${evalGradeList.codeId}"/>" + ":" + "<c:out value="${evalGradeList.codeNm}"/>";
        <c:if test="${not status.last}">
        selectStr += ";";
        </c:if>
        </c:forEach>

        return selectStr;
    }

    // 목록으로
    function goList() {
        loadPage("${context_path}/system/system/menu/ideaEval/ideaEvalList.do", "form");
    }

    //점수확인
    function checkScore() {
        var zero = "0";
        var sum = parseInt(zero);
        var zero = "0";
        var sumWeightId = parseInt(zero);
        var num = $("#list").getGridParam("reccount");		//현재 화면에 표시되고있는 레코드 숫자
        var rowData;
        for (var n = 1; n <= num; n++) {	//1 ~ (화면에 표시되고있는 레코드 숫자) 까지 반복하여 ideaCd를 비교해서 해당 제안을 찾음.
            rowData = $("#list").jqGrid("getRowData", n);
            temp = rowData.weightId;
            sumWeightId = sumWeightId + parseInt(temp);
        }
        for (var n = 1; n <= num; n++) {	//1 ~ (화면에 표시되고있는 레코드 숫자) 까지 반복하여 ideaCd를 비교해서 해당 제안을 찾음.
            rowData = $("#list").jqGrid("getRowData", n);
            temp = rowData.score;
            if(isNotEmpty(temp)) {
                temp = parseInt(temp) / sumWeightId * parseInt(rowData.weightId);
                sum = sum + temp;
            }
        }
        $.showMsgBox("현재 평가점수는 " + Math.round(sum) + "점 입니다.", null);
    }

    //저장
    function saveData() {
        if (!gridToFormChanged("list", "form", true)) return false;

        $.showConfirmBox("<spring:message code="common.save.msg"/>", "doSaveData");
    }

    function doSaveData() {
        sendAjax({
            "url": "${context_path}/system/system/menu/ideaEval/saveIdeaEval.do",
            "data": getFormData("form"),
            "doneCallbackFunc": "checkResult"
        });
    }

    //제출
    function submitData() {


        $.showConfirmBox("제출하시겠습니까?", "doSubmitData");
    }

    function doSubmitData() {
        if (gridToFormChanged("list", "form", true)) {
            sendAjax({
                "url": "${context_path}/system/system/menu/ideaEval/saveIdeaEval.do",
                "data": getFormData("form")
            });
        }

        var f = document.form;

        var zero = "0";
        var sum = parseInt(zero);
        var sumWeightId = parseInt(zero);
        var num = $("#list").getGridParam("reccount");		//현재 화면에 표시되고있는 레코드 숫자
        var rowData;
        for (var n = 1; n <= num; n++) {	//1 ~ (화면에 표시되고있는 레코드 숫자) 까지 반복하여 ideaCd를 비교해서 해당 제안을 찾음.
            rowData = $("#list").jqGrid("getRowData", n);
            temp = rowData.weightId;
            sumWeightId = sumWeightId + parseInt(temp);
        }
        for (var n = 1; n <= num; n++) {	//1 ~ (화면에 표시되고있는 레코드 숫자) 까지 반복하여 ideaCd를 비교해서 해당 제안을 찾음.
            rowData = $("#list").jqGrid("getRowData", n);
            temp = rowData.score;
            if(isNotEmpty(temp)) {
                temp = parseInt(temp) / sumWeightId * parseInt(rowData.weightId);
                sum = sum + temp;
            }
            else {
                $.showMsgBox("모든 항목의 평가등급을 매겨주세요.", null);
                return false;
            }
        }

        $("#evalSum").val(Math.round(sum));
        sendAjax({
            "url": "${context_path}/system/system/menu/ideaEval/submitIdeaEval.do",
            "data": getFormData("form"),
            "doneCallbackFunc": "checkResult"
        });
    }

    //저장 callback
    function checkResult(data) {
        $(window).scrollTop(0);	//윈도우 스크롤 맨 위로 이동시키기
        $.showMsgBox(data.msg);
        if (data.result == AJAX_SUCCESS) {
            searchList();
        }
    }

    // 목록 조회
    function searchList() {
        reloadGrid("list", "form");
    }

</script>

<form:form commandName="searchVO" id="form" name="form" method="post">
    <form:hidden path="ideaCd" value="${dataVO.ideaCd}"/>
    <form:hidden path="year" value="${dataVO.year}"/>
    <form:hidden path="degree" value="${dataVO.degree}"/>
    <form:hidden path="ideaGbnCd" value="${dataVO.ideaGbnCd}"/>
    <form:hidden path="evalSum"/>
    <form:hidden path="loginUserId" value="${sessionScope.loginVO.userId}"/>
    <form:hidden path="evalCd" value="${dataVO.evalCd}"/>
    <form:hidden path="atchFileKey" value="${dataVO.atchFileKey}"/>


    <div id="newForm">
        <div class="ptitle" id="titleIdeaEvalNm"></div>
        <div class="tbl-type02">
            <table summary="">
                <caption></caption>
                <colgroup>
                    <col width="15%"/>
                    <col width="35%"/>
                    <col width="15%"/>
                    <col width="35%"/>
                </colgroup>
                <tbody>
                <tr>
                    <th scope="row"><label for="userNm"><spring:message code="word.insertUser"/></label></th>
                    <td><c:out value="${dataVO.userNm}" escapeXml="true"/></td>
                </tr>
                <tr>
                    <th scope="row"><label for="category">카테고리</label></th>
                    <td><c:out value="${dataVO.category}" escapeXml="true"/></td>
                    <th scope="row"><label for="createDt">생성일자</label></th>
                    <td><c:out value="${dataVO.createDt}" escapeXml="true"/></td>
                </tr>
                <tr>
                    <th scope="row"><label for="title">제목</label></th>
                    <td colspan="3"><c:out value="${dataVO.title}" escapeXml="true"/></td>
                </tr>
                <tr>
                    <th scope="row"><label for="content">내용</label></th>
                    <td colspan="3"><c:out value="${dataVO.content}" escapeXml="true"/></td>
                </tr>
                <tr>
                    <th scope="row"><label><spring:message code="word.atchFile"/></label></th>
                    <td colspan="3"><span id="spanAttachFile"></span></td>
                </tr>
                </tbody>
            </table>
        </div>
        <div class="btn-dw"></div>
        <div class="gridContainer">
            <table id="list"></table>
            <div id="pager"></div>
        </div>

        <div class="tbl-bottom">
            <div class="tbl-wbtn">
                <a href="#" class="prev" onclick="goList();return false;"><span><spring:message code="button.before"/></span></a>
            </div>
            <div class="tbl-btn"></div>
        </div>
        <div class="tbl-bottom tbl-bottom2">
            <div class="tbl-btn">
                <a href="#" class="new" onclick="checkScore();return false;">평가점수확인</a>
                <a href="#" class="edit" onclick="saveData();return false;">임시저장</a>
                <a href="#" class="save" onclick="submitData();return false;">평가제출</a>
            </div>
        </div>
    </div>
</form:form>