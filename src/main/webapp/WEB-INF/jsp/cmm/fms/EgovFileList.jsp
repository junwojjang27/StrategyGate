<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="ui" uri="http://egovframework.gov/ctl/ui"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib uri="/WEB-INF/tlds/secure.tld" prefix="secure" %>
<%

/**
 * @Class Name : EgovFileList.jsp
 * @Description : 파일 목록화면
 * @Modification Information
 * @
 * @  수정일   	수정자		수정내용
 * @ ----------	------		---------------------------
 * @ 2009.03.26	이삼섭		최초 생성
 * @ 2011.07.20	옥찬우		<Input> Tag id속성 추가( Line : 68 )
 * @ 2014.04.23	윤태성		암호화 추가 secure:encrypt
 *
 *  @author 공통서비스 개발팀 이삼섭
 *  @since 2009.03.26
 *  @version 1.0
 *  @see
 *
 */
%>
<c:if test="${not empty fileList}">
	<c:forEach var="fileVO" items="${fileList}" varStatus="status">
		<c:choose>
			<c:when test="${updateFlag=='Y'}">
				<a href="javascript:fn_egov_downFile('<c:out value="${secure:encrypt(fileVO.atchFileId)}"/>','<c:out value="${fileVO.fileSn}"/>')" style="line-height:30px">
				<c:out value="${fileVO.orignlFileNm}"/>&nbsp;[<c:out value="${fileVO.fileSize}"/>&nbsp;byte]
				</a>
				<input type="checkbox" name="chkAttachFiles${param.fileGroupSuffix}" id="chkAttachFile_<c:out value="${fileVO.fileSn}" />" value="<c:out value="${fileVO.fileSn}" />" /><label for="chkAttachFile_<c:out value="${fileVO.fileSn}" />"><span></span></label> 첨부파일 삭제<br/>
			</c:when>
			<c:otherwise>
				<p><a href="javascript:fn_egov_downFile('<c:out value="${secure:encrypt(fileVO.atchFileId)}"/>','<c:out value="${fileVO.fileSn}"/>')">
					<span><c:out value="${fileVO.orignlFileNm}"/>&nbsp;[<c:out value="${fileVO.fileSize}"/>&nbsp;byte]</span>
				</a></p>
			</c:otherwise>
		</c:choose>
	</c:forEach>
</c:if>
