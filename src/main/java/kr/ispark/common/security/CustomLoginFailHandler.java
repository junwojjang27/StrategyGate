package kr.ispark.common.security;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.LocaleUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import egovframework.com.sym.log.clg.service.EgovLoginLogService;
import egovframework.com.sym.log.clg.service.LoginLog;
import egovframework.rte.psl.dataaccess.util.EgovMap;
import kr.ispark.common.security.service.UserVO;
import kr.ispark.common.security.service.impl.LoginServiceImpl;
import kr.ispark.common.system.service.LangVO;
import kr.ispark.common.system.service.MenuVO;
import kr.ispark.common.system.service.impl.MenuServiceImpl;
import kr.ispark.common.util.CommonUtil;
import kr.ispark.common.util.ContextHolder;
import kr.ispark.common.util.DataSourceType;
import kr.ispark.common.util.PropertyUtil;
import kr.ispark.common.util.SessionUtil;

public class CustomLoginFailHandler implements AuthenticationFailureHandler {
	public final Logger log = LoggerFactory.getLogger(this.getClass());
	
	/*
	private String errorSSOPage;
	*/
	private String errorLOGINPage;
	
	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException exception) throws IOException, ServletException {
			// TODO Auto-generated method stub
		
			//String loginGbn = request.getParameter("loginGbn");
			
			request.setAttribute("exception", exception);
			
			/*
			if("SSO".equals(loginGbn)){
				response.sendRedirect(request.getContextPath()+errorSSOPage);
				//request.getRequestDispatcher(errorSSOPage).forward(request, response);
			}else if("LOGIN".equals(loginGbn)){
				response.sendRedirect(request.getContextPath()+errorLOGINPage);
				//request.getRequestDispatcher(errorLOGINPage).forward(request, response);
			}
			*/
			
			response.sendRedirect(request.getContextPath()+errorLOGINPage);
	}
	/*
	public void setErrorSSOPage(String errorPage){
			this.errorSSOPage = errorPage;
	}
	*/
	
	public void setErrorLOGINPage(String errorPage){
		this.errorLOGINPage = errorPage;
}
	
}
