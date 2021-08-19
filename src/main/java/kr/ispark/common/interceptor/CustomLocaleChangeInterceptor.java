package kr.ispark.common.interceptor;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.springframework.web.servlet.support.RequestContextUtils;

import kr.ispark.common.security.service.UserVO;
import kr.ispark.common.system.service.MenuVO;
import kr.ispark.common.system.service.impl.MenuServiceImpl;
import kr.ispark.common.util.SessionUtil;
import kr.ispark.system.system.comp.compUserMng.service.impl.CompUserMngServiceImpl;

public class CustomLocaleChangeInterceptor extends HandlerInterceptorAdapter {

	@Autowired
	MenuServiceImpl menuService;

	@Autowired
	CompUserMngServiceImpl compUserMngService;

	/**
	 * Default name of the locale specification parameter: "locale".
	 */
	public static final String DEFAULT_PARAM_NAME = "locale";

	private String paramName = DEFAULT_PARAM_NAME;


	/**
	 * Set the name of the parameter that contains a locale specification
	 * in a locale change request. Default is "locale".
	 */
	public void setParamName(String paramName) {
		this.paramName = paramName;
	}

	/**
	 * Return the name of the parameter that contains a locale specification
	 * in a locale change request.
	 */
	public String getParamName() {
		return this.paramName;
	}


	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {

		String newLocale = request.getParameter(this.paramName);

		if (newLocale != null) {
			LocaleResolver localeResolver = RequestContextUtils.getLocaleResolver(request);
			if (localeResolver == null) {
				throw new IllegalStateException("No LocaleResolver found: not in a DispatcherServlet request?");
			}
			localeResolver.setLocale(request, response, StringUtils.parseLocaleString(newLocale));

			UserVO userVO = SessionUtil.getUserVO();
			if(userVO != null) {
				userVO.setLang(newLocale);
				compUserMngService.updateUserLang(userVO);
			}

			// 메뉴 조회
			List<MenuVO> menuList = menuService.selectList(userVO);
			SessionUtil.setAttribute("menuList", menuList);
			SessionUtil.setAttribute("lang", newLocale);
		}
		// Proceed in any case.
		return true;
	}
}
