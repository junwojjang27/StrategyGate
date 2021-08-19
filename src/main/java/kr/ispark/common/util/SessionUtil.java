package kr.ispark.common.util;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import egovframework.com.cmm.util.EgovUserDetailsHelper;
import kr.ispark.common.security.service.UserVO;

/**
 * session Util
 * - Spring에서 제공하는 RequestContextHolder 를 이용하여
 * request 객체를 service까지 전달하지 않고 사용할 수 있게 해줌
 * 
 */
public class SessionUtil {

	/**
	 * attribute 값을 가져 오기 위한 method
	 * 
	 * @param String  attribute key name 
	 * @return Object attribute obj
	 */
	public static Object getAttribute(String name) {
		if(RequestContextHolder.getRequestAttributes() == null) {
			return null;
		}
		try {
			return (Object)RequestContextHolder.getRequestAttributes().getAttribute(name, RequestAttributes.SCOPE_SESSION);
		} catch(NullPointerException npe) {
			return null;
		} catch(Exception e) {
			return null;
		}
	}

	/**
	 * attribute 설정 method
	 * 
	 * @param String  attribute key name 
	 * @param Object  attribute obj
	 * @return void
	 */
	public static void setAttribute(String name, Object object) throws Exception {
		RequestContextHolder.getRequestAttributes().setAttribute(name, object, RequestAttributes.SCOPE_SESSION);
	}

	/**
	 * 설정한 attribute 삭제 
	 * 
	 * @param String  attribute key name 
	 * @return void
	 */
	public static void removeAttribute(String name) throws Exception {
		RequestContextHolder.getRequestAttributes().removeAttribute(name, RequestAttributes.SCOPE_SESSION);
	}

	/**
	 * 로그인 유저VO
	 * @return	UserVO
	 * @throws Exception
	 */
	public static UserVO getUserVO() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if(authentication.getPrincipal() instanceof UserVO) {
			return (UserVO)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		} else {
			return null;
		}
	}
	
	/**
	 * session id 
	 * 
	 * @param void
	 * @return String SessionId 값
	 */
	public static String getSessionId() throws Exception  {
		return RequestContextHolder.getRequestAttributes().getSessionId();
	}
	
	/*
	 * userId 리턴
	 * 
	 * @param 
	 * @return String userId 값
	 */
	public static String getUserId() {
		String userId = null;
		try {
			userId = (String)getAttribute("userId");
		} catch(NullPointerException npe) {
			npe.getCause();
		} catch(Exception e) {
			e.getCause();
		}
		return userId;
	}
	
	/*
	 * 권한 보유 여부
	 * @param	String auth 권한코드
	 * @return	boolean
	 */
	public static boolean hasRole(String auth) {
		return hasAuth(auth);
	}
	public static boolean hasAuth(String auth) {
		boolean result = false;
		try {
			List<String> authList = EgovUserDetailsHelper.getAuthorities();
			if(authList == null) return false;
			result = authList.contains(auth);
		} catch(NullPointerException npe) {
			npe.getCause();
		} catch(Exception e) {
			e.getCause();
		}
		return result;
	}
	
	public static void logoutUser() {
    	try {
    		removeAttribute("userId");
    		removeAttribute(SessionLocaleResolver.LOCALE_SESSION_ATTRIBUTE_NAME);
    		removeAttribute("lang");
			removeAttribute("loginVO");
	    	removeAttribute("menuList");
	    	removeAttribute("menuMap");
	    	removeAttribute("langList");
	    	
	    	removeAttribute("SUPER_LOGIN_KEY");
	    	removeAttribute("ADMIN_LOGIN_KEY");
	    	SecurityContextHolder.getContext().setAuthentication(null);
    	} catch(NullPointerException npe) {
			npe.getCause();	
		} catch (Exception e) {
			e.getCause();
		}
	}
}
