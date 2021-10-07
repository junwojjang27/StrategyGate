<!--
*************************************************************************
* CLASS 명 : IdeaReviewList
* 작 업 자 : 하성준
* 작 업 일 : 2021-10-05
* 기 능 : IDEA+검토 List
* ---------------------------- 변 경 이 력 --------------------------------
* 번호 작 업 자 작 업 일 변 경 내 용 비고
* ---- --------- ---------------- --------------------- -----------
* 1 하성준 2021-10-05 최 초 작 업
**************************************************************************
-->

<!--
필독 : 다국어 지원기능에 의해서 화면에 나타나는 한글명칭은 message properties파일을 이용하여 수정할 것.
-->

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/jsp/common/common-taglibs.jsp" %>
<validator:javascript formName="ideaReviewVO" staticJavascript="false" xhtml="true" cdata="false"/>
<script type="text/javascript">
    $(function () {
        $("#list").jqGrid({
            url: "${context_path}/system/system/menu/ideaReview/ideaReviewList_json.do",
            postData: getFormData("form"),
            width: "${jqgrid_width}",
            height: "(count%10)*30",	//jqGrid크기를 제안 수에 맞게 변경
            colModel: [
                {name: "ideaGbnCd",       index: "ideaGbnCd",       width: 20,  align: "center", label: "<spring:message code="word.ideaDivision"/>"},
                {name: "userNm",          index: "userNm",          width: 20,  align: "center", label: "<spring:message code="word.insertUser"/>"},
                {name: "category",        index: "category",        width: 20,  align: "center", label: "<spring:message code="word.category"/>"},
                {name: "title",           index: "title",           width: 100, align: "left",   label: "<spring:message code="word.title"/>",
                    formatter: function (cellvalue, options, rowObject) {
                        return "<a href='#' onclick='showDetail(\"" + removeNull(rowObject.ideaCd) + "\");return false;'>" + escapeHTML(removeNull(cellvalue)) + "</a>";
                    },//formatter : 색깔이나 액션을 주는 (데이터 가공) / 제목에 링크, 돋보기 표시 등
                    unformat: linkUnformatter
                },
                {name: "state",           index: "state",           width: 20,  align: "center", label: "<spring:message code="word.progressStatus"/>"},
                {name: "degree",          index: "degree",          width: 20,  align: "center", label: "<spring:message code="word.evalDegree"/>"},
                {name: "evalState",       index: "evalState",       width: 20,  align: "center", label: "<spring:message code="word.evalStatus"/>"},
                {name: "startDt",         index: "startDt",         width: 20,  align: "center", label: "<spring:message code="word.evalStartDt"/>"},
                {name: "endDt",           index: "endDt",           width: 20,  align: "center", label: "<spring:message code="word.evalEndDt"/>"},
                {name: "createDt",        index: "createDt",        width: 20,  align: "center", label: "<spring:message code="word.createDT"/>"},
                {name: "deptNm",          index: "deptNm",          width: 20,  align: "center", label: "부서명",      hidden: true},
                {name: "ideaCd",          index: "ideaCd",          width: 100, align: "center", label: "제안코드",     hidden: true},
                {name: "year",            index: "year",            width: 100, align: "center", label: "기준년도",     hidden: true},
                {name: "userId",          index: "userId",          width: 100, align: "center", label: "아이디",      hidden: true},
                {name: "content",         index: "content",         width: 100, align: "center", label: "내용",        hidden: true},
                {name: "updateDt",        index: "updateDt",        width: 100, align: "center", label: "수정일자",     hidden: true},
                {name: "deleteDt",        index: "deleteDt",        width: 100, align: "center", label: "삭제일자",     hidden: true},
                {name: "atchFileId",      index: "atchFileId",      width: 100, align: "center", label: "첨부파일ID",   hidden: true},
                /*{name: "ideaExecutionCd", index: "ideaExecutionCd", width: 100, align: "center", label: "실행안코드",   hidden: true},
                {name: "result",          index: "result",          width: 100, align: "center", label: "폐기/실행",    hidden: true},
                {name: "excutionDt",      index: "excutionDt",      width: 100, align: "center", label: "실행시작일자",  hidden: true},*/
            ],
            pager: "pager",
            rowNum: 10,
            sortname: "sortOrder",
            sortorder: "asc",
            loadComplete: function () {
                var count = $('#list').getGridParam('records');  //jqGrid크기 조절하기 위함. (서버에서 받은 실제 레코드 수)

                var $grid = $("#list");
                var ids = $grid.jqGrid("getDataIDs");
                var rowObj;
                $(ids).each(function (idx, rowId) {
                    rowObj = $grid.jqGrid("getRowData", rowId);
                    if (rowObj.state == "대기") {                 //대기 상태인 리스트는 배경을 노란색으로.
                        $grid.find("#" + rowId).css("backgroundColor", "#FFBB00");
                    }
                });

                //byte check
                showBytes("content", "contentBytes");
                setMaxLength("form");
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
    function showDetail(ideaCd) {
        var f = document.form;
        f.ideaCd.value = ideaCd;

        sendAjax({
            "url": "${context_path}/system/system/menu/ideaReview/selectDetail.do",
            "data": getFormData("form"),
            "doneCallbackFunc": "setDetail"
        });
    }

    // 상세 조회 값 세팅
    function setDetail(data) {
        $("#newForm").show();
        var dataVO = data.dataVO;

        voToForm(dataVO, "form", ["state","startDt", "endDt"]);

        $("#ideaGbnCd").text(dataVO.ideaGbnCd);
        $("#category").text(dataVO.category);
        $("#userNm").text(dataVO.userNm);
        $("#deptNm").text(dataVO.deptNm);
        $("#degree").text(dataVO.degree);
        $("#evalState").text(dataVO.evalState);
        $("#title1").text(dataVO.title);
        $("#content").text(dataVO.content);

        /**********첨부파일 시작**********/
        var modifyYn = "Y";
        //첨부파일 ajax
        $.ajax({
            url: "${context_path}/system/system/menu/ideaReview/ideaReviewDetail.do",
            data: {
                "modifyYn": modifyYn,
                "atchFileId": dataVO.atchFileKey,
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

    // 저장
    function saveData() {
        var f = document.form;

        if ($("#state").val() == '001') {
            if ($("#startDt").val() != "" || $("#endDt").val() != "") {
                $.showMsgBox("승인되지 않은 제안은 평가 기간을 입력할 수 없습니다.");
                return false;
            }
        }
        if ($("#state").val() == '002') {
            if ($("#startDt").val() == "" || $("#endDt").val() == "") {
                $.showMsgBox("평가기간을 입력해 주세요.");
                return false;
            }
        }
        if ($("#state").val() == '003') {
            if ($("#startDt").val() != "" || $("#endDt").val() != "") {
                $.showMsgBox("반려시킬 제안은 평가 기간을 입력할 수 없습니다.");
                return false;
            }
        }

        var startTemp = $("#startDt").val().replace(/\./g, ''); //날짜 비교를 위함.
        var endTemp = $("#endDt").val().replace(/\./g, '');

        if ($("#state").val() == '002' && startTemp > endTemp) { //시작날짜가 종료날짜보다 뒤일 경우
            $.showMsgBox("<spring:message code="errors.wrongPeriod"/>");
            return false;
        }

        var now = new Date(); //현재 시간 구하기

        year = now.getFullYear();
        month = now.getMonth() + 1;
        date = now.getDate();

        today = year * 10000 + month * 100 + date;

        if ($("#state").val() == '002' && today > startTemp) {
            $.showMsgBox("평가 시작일이 현재 날짜보다 빠를 수 없습니다.");
            return false;
        }

        $.showConfirmBox("<spring:message code="common.save.msg"/>", function () {
            sendAjax({
                "url": "${context_path}/system/system/menu/ideaReview/saveIdeaReview.do",
                "data": getFormData("form"),
                "doneCallbackFunc": "checkResult"
            });
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

    function popDatePicker(obj) {   //날짜 선택
        $('#' + obj).datepicker().datepicker("show");
    }
</script>

<form:form commandName="searchVO" id="form" name="form" method="post">
    <form:hidden path="ideaCd"/>
    <form:hidden path="year"/>

    <div class="sch-bx">
        <ul>
            <li>
                <label for="findYear"><spring:message code="word.year"/></label>
                <form:select path="findYear" class="select wx80" items="${codeUtil:getCodeList('017')}" itemLabel="codeNm" itemValue="codeId">
                </form:select>
            </li>
            <li>
                <label for="findState"><spring:message code="word.progressStatus"/></label>
                <form:select path="findState" class="select wx80">
                    <option value=""><spring:message code="word.all"/></option>
                    <form:options items="${codeUtil:getCodeList('390')}" itemLabel="codeNm" itemValue="codeId"/>
                </form:select>
            </li>
            <li>
                <label for="findDegree"><spring:message code="word.evalDegree"/></label>
                <form:select path="findDegree" class="select wx80">
                    <option value=""><spring:message code="word.all"/></option>
                    <form:options items="${codeUtil:getCodeList('387')}" itemLabel="codeNm" itemValue="codeId"/>
                </form:select>
            </li>
            <li>
                <label for="findEvalState"><spring:message code="word.evalStatus"/></label>
                <form:select path="findEvalState" class="select wx80">
                    <option value=""><spring:message code="word.all"/></option>
                    <form:options items="${codeUtil:getCodeList('391')}" itemLabel="codeNm" itemValue="codeId"/>
                </form:select>
            </li>
            <li>
                <label for="colName"><spring:message code="word.search"/></label>
                <form:select path="colName" class="select wx100">
                    <form:option value="title"><spring:message code="word.title"/></form:option>
                    <form:option value="userNm"><spring:message code="word.insertUser"/></form:option>
                </form:select>
                <span class="searchBar"><form:input path="searchKeyword" class="t-box01 wx200" maxlength="20"/> </span>
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
    <div class="tbl-bottom tbl-bottom2">
        <div class="tbl-btn">
        </div>
    </div>
    <div class="page-noti">
        <ul>
            <li>평가 대기인 것만 노란색으로 표시됩니다.</li>
            <li>노란색 표시를 보고 평가기간을 입력해주시기 바랍니다.</li>
        </ul>
    </div>

    <div id="newForm">
        <div class="ptitle" id="titleIdeaReviewNm"></div>
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
                    <th scope="row"><label for="ideaGbnCd"><spring:message code="word.ideaDivision"/></label></th>
                    <td><span id="ideaGbnCd"/></td>
                    <th scope="row"><label for="category"><spring:message code="word.category"/></label></th>
                    <td><span id="category"/></td>
                </tr>
                <tr>
                    <th scope="row"><label for="userNm"><spring:message code="word.insertUser"/></label></th>
                    <td><span id="userNm"/></td>
                    <th scope="row"><label for="deptNm"><spring:message code="word.dept"/></label></th>
                    <td><span id="deptNm"/></td>
                </tr>
                <tr>
                    <th scope="row"><label for="degree"><spring:message code="word.evalDegree"/></label></th>
                    <td><span id="degree"/></td>
                    <th scope="row"><label for="state"><spring:message code="word.progressStatus"/></label></th>
                    <td>
                        <form:select path="state" class="select wx100" items="${codeUtil:getCodeList('390')}" itemLabel="codeNm" itemValue="codeId">
                        </form:select>
                    </td>
                </tr>
                <tr>
                    <th scope="row"><label for="evalState"><spring:message code="word.evalStatus"/></label></th>
                    <td><span id="evalState"/></td>
                    <th scope="row"><label for="startDt"><spring:message code="word.evalPeriod"/></label></th>
                    <td>
                        <form:input path="startDt" class="wx165 datepicker" readonly="true" maxlength="10"/>
                        <a href="#" class="btn-search btn-absolute" style="margin-left:-40px;" onclick="popDatePicker('startDt');return false;"><spring:message code="word.search"/></a>
                        <a href="#" class="close ml0" onclick="resetInput(['startDt']);return false;"><spring:message code="word.delete"/></a>
                        ~
                        <form:input path="endDt" class="wx165 datepicker" readonly="true" maxlength="10"/>
                        <a href="#" class="btn-search btn-absolute" style="margin-left:-40px;" onclick="popDatePicker('endDt');return false;"><spring:message code="word.search"/></a>
                        <a href="#" class="close ml0" onclick="resetInput(['endDt']);return false;"><spring:message code="word.delete"/></a>
                    </td>
                </tr>
                <tr>
                    <th scope="row"><label for="title1"><spring:message code="word.title"/></label></th>
                    <td colspan="3"><span id="title1"/></td>
                </tr>
                <tr>
                    <th scope="row"><label for="content"><spring:message code="word.content"/></label></th>
                    <td colspan="3"><span id="content"/></td>
                </tr>
                <tr>
                    <th scope="row"><label><spring:message code="word.atchFile"/></label></th>
                    <td colspan="3"><span id="spanAttachFile"></span></td>
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

