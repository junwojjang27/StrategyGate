<%--
  Created by IntelliJ IDEA.
  User: junwo
  Date: 2021-10-13
  Time: 오전 10:27
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/jsp/common/common-taglibs.jsp" %>
<script type="text/javascript">
    $(function() {
    });
</script>

<form:form commandName="searchVO" id="form" name="form" method="post">
    <form:hidden path="ideaCd"/>
    <form:hidden path="year"/>
    <form:hidden path="createDt"/>

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
                    <th scope="row"><label for="ideaCd">제안코드</label><span class="red">(*)</span></th>
                    <td><form:input path="ideaCd" class="t-box01"/></td>
                </tr>
                <tr>
                    <th scope="row"><label for="userId">아이디</label></th>
                    <td><form:input path="userId" class="t-box01"/></td>
                </tr>
                <tr>
                    <th scope="row"><label for="category">카테고리</label></th>
                    <td><form:input path="category" class="t-box01"/></td>
                </tr>
                <tr>
                    <th scope="row"><label for="title">제목</label></th>
                    <td><form:input path="title" class="t-box01"/></td>
                </tr>
                <tr>
                    <th scope="row"><label for="content">내용</label></th>
                    <td><form:input path="content" class="t-box01"/></td>
                </tr>
                <tr>
                    <th scope="row"><label for="state">상태(접수/승인/반려)</label></th>
                    <td><form:input path="state" class="t-box01"/></td>
                </tr>
                <tr>
                    <th scope="row"><label for="createDt">생성일자</label><span class="red">(*)</span></th>
                    <td><form:input path="createDt" class="t-box01"/></td>
                </tr>
                <tr>
                    <th scope="row"><label for="updateDt">수정일자</label></th>
                    <td><form:input path="updateDt" class="t-box01"/></td>
                </tr>
                <tr>
                    <th scope="row"><label for="deleteDt">삭제일자</label></th>
                    <td><form:input path="deleteDt" class="t-box01"/></td>
                </tr>
                <tr>
                    <th scope="row"><label for="startDt">평가시작일자</label></th>
                    <td><form:input path="startDt" class="t-box01"/></td>
                </tr>
                <tr>
                    <th scope="row"><label for="endDt">평가종료일자</label></th>
                    <td><form:input path="endDt" class="t-box01"/></td>
                </tr>
                <tr>
                    <th scope="row"><label for="atchFileId">첨부파일ID</label></th>
                    <td><form:input path="atchFileId" class="t-box01"/></td>
                </tr>
                <tr>
                    <th scope="row"><label for="ideaGbnCd">평가구분코드</label></th>
                    <td><form:input path="ideaGbnCd" class="t-box01"/></td>
                </tr>
                <tr>
                    <th scope="row"><label for="degree">차수</label></th>
                    <td><form:input path="degree" class="t-box01"/></td>
                </tr>
                <tr>
                    <th scope="row"><label for="evalState">평가상태(대기/진행/종료)</label></th>
                    <td><form:input path="evalState" class="t-box01"/></td>
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