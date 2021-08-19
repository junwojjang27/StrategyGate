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
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.WebAttributes;
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

public class CustomLoginSuccessHandler implements AuthenticationSuccessHandler {
	public final Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private LoginServiceImpl loginService;

	@Autowired
	private MenuServiceImpl menuService;

	@Resource(name="EgovLoginLogService")
	private EgovLoginLogService loginLogService;

	private String defaultUrl;
	private String noServiceUrl;
	private String noConnectionUrl;

	private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
			throws IOException, ServletException {
		clearAuthenticationAttributes(request);

		log.debug("\n#### CustomLoginSuccessHandler.onAuthenticationSuccess ###");
		log.debug("login success");

		UserVO userVO = (UserVO)authentication.getPrincipal();

		try {

			Locale locale = LocaleUtils.toLocale(userVO.getLang());
			SessionUtil.setAttribute("userId", CommonUtil.nullToBlank(userVO.getUserId()));
			SessionUtil.setAttribute(SessionLocaleResolver.LOCALE_SESSION_ATTRIBUTE_NAME, locale);
			LocaleContextHolder.setLocale(locale);

			SessionUtil.setAttribute("lang", locale.getLanguage());

			// egov의 유저 인증 호환용
			userVO.setIp(request.getRemoteAddr());
			SessionUtil.setAttribute("loginVO", userVO);

			// 메뉴 조회
			List<MenuVO> menuList = menuService.selectList(userVO);
			SessionUtil.setAttribute("menuList", menuList);

			HashMap<String, String> menuMap = new HashMap<String, String>();
			for(MenuVO menuVO : menuList) {
				if(!CommonUtil.isEmpty(menuVO.getUrl())) {
					menuMap.put(menuVO.getPgmId(), menuVO.getUrl());
				}
			}
			SessionUtil.setAttribute("menuMap", menuMap);

			// 언어목록 조회
			List<LangVO> langList = loginService.selectLangList(userVO);
			SessionUtil.setAttribute("langList", langList);

			// 사용자의 연도별 성과조직 목록
			List<EgovMap> userScDeptList = loginService.selectUserScDeptList(userVO);
			EgovMap userScDeptMap = new EgovMap();
			for(EgovMap scDept : userScDeptList) {
				userScDeptMap.put(scDept.get("year"), scDept.get("scDeptId"));
			}
			SessionUtil.setAttribute("userScDeptMap", userScDeptMap);

			// 접근권한 map 생성
			List<ConfigAttribute> configList = new LinkedList<ConfigAttribute>();
			if(userVO.getAdminGubunList() != null) {
				for(String adminGubun : userVO.getAdminGubunList()) {
					configList.add(new SecurityConfig(adminGubun));
				}

				LinkedHashMap<RequestMatcher, Collection<ConfigAttribute>> requestMap = new LinkedHashMap<RequestMatcher, Collection<ConfigAttribute>>();;
				for(MenuVO menuVO : menuList) {
					//if(menuVO.getUrlPattern() == null) continue;
					if(CommonUtil.isEmpty(menuVO.getUrlPattern())) continue;
					requestMap.put(new AntPathRequestMatcher(menuVO.getUrlPattern()), configList);
				}
				userVO.setRequestMap(requestMap);
			}

			// 검색조건 쿠키 초기화
			Cookie[] cookies = request.getCookies();
			for(Cookie cookie : cookies) {
				if(!cookie.getName().replaceAll("\r", "").replaceAll("\n", "").startsWith("find")) continue;

				cookie.setValue("");
				cookie.setMaxAge(0);
				cookie.setPath("");
				response.addCookie(cookie);
			}

			// 로그인 로그
			LoginLog loginLog = new LoginLog();
			loginLog.setLoginId(userVO.getUserId());
			loginLog.setLoginIp(userVO.getIp());
			loginLog.setLoginMthd("I");	// 로그인:I, 로그아웃:O
			loginLog.setErrOccrrAt("N");
			loginLog.setErrorCode("");
			loginLogService.logInsertLoginLog(loginLog);

			log.debug("#######################\n");
		} catch (IOException ie) {
			log.error("error : "+ie.getCause());
		} catch (Exception e) {
			log.error("error : "+e.getCause());
		}

		redirectStrategy.sendRedirect(request, response, defaultUrl);
	}

	private void clearAuthenticationAttributes(HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		if(session == null) {
			return;
		}
		session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
	}

	public String getDefaultUrl() {
		return defaultUrl;
	}

	public void setDefaultUrl(String defaultUrl) {
		this.defaultUrl = defaultUrl;
	}

	public String getNoServiceUrl() {
		return noServiceUrl;
	}

	public void setNoServiceUrl(String noServiceUrl) {
		this.noServiceUrl = noServiceUrl;
	}
	
	public String getNoConnectionUrl() {
		return noConnectionUrl;
	}

	public void setNoConnectionUrl(String noConnectionUrl) {
		this.noConnectionUrl = noConnectionUrl;
	}
}
