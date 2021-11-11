<%--
  Created by IntelliJ IDEA.
  User: junwo
  Date: 2021-11-10
  Time: 오후 2:39
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/jsp/common/tiles/common-params.jsp" %>
<%@ include file="/WEB-INF/jsp/common/common-taglibs.jsp" %>
<!doctype html>
<html lang="ko">
<head>
    <script type="text/javascript">
        $(function() {
            setMaxLength("form");
        });

        var modifyYn = "Y";
        //첨부파일 ajax
        $.ajax({
            url: "${context_path}/system/system/menu/cha/popAtchFile2.do",
            data: {
                "modifyYn": modifyYn,
                "_csrf": getCsrf("form")
            },
            method: "POST",
            cache: false,
            dataType: "html"
        }).done(function (html) {
            $("#spanAttachFile").empty();
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

        // 저장
        function saveData() {
            var f = document.form;

            $.showConfirmBox("<spring:message code="common.save.msg"/>", function() {
                sendMultipartForm({
                    "url": "${context_path}/system/system/menu/cha/saveCha22.do",
                    "formId": "form",
                    "fileModules": [upload1],
                    "doneCallbackFunc" : "checkResult"
                });
            });
        }

        function checkResult(data) {
            $.showMsgBox(data.msg);
            if(data.result == AJAX_SUCCESS) {
                $.fancybox.close();
            }
        }
    </script>
</head>
<body>
<form:form commandName="searchVO" id="form" name="form" method="post">
    <p>------------------------------------------------------------------------------------------------------------------------</p>
    <span id="spanAttachFile"></span>
    <div class="tbl-btn">
        <a href="#" class="save" onclick="saveData();return false;"><spring:message code="button.save"/></a>
    </div>
</form:form>

</body>
</html>