package kr.ispark.common.interceptor;

import java.util.Iterator;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import egovframework.com.cmm.EgovMessageSource;
import kr.ispark.common.exception.SecureException;

/**
 * 레퍼러 체크
 *
 */


public class RefererInterceptor extends HandlerInterceptorAdapter {

	protected final Logger log = LoggerFactory.getLogger(this.getClass());

	@Resource(name="egovMessageSource")
    EgovMessageSource egovMessageSource;

	private PathMatcher pathMatcher;

	private Set<String> permittedAntPathURL;

	public void setPermittedAntPathURL(Set<String> permittedAntPathURL) {
		this.permittedAntPathURL = permittedAntPathURL;
	}

	/**
	 * 제외된 URL이 아닌 URL에 대해서 refer체크를 한다.
	 * refer
	 */
	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {

		String requestURI = request.getRequestURI(); //요청 URI
		boolean isPermittedAntPathURL = false;

		pathMatcher = new AntPathMatcher();
		
		for(Iterator<String> it = this.permittedAntPathURL.iterator(); it.hasNext();){
			String urlPattern = request.getContextPath() + (String) it.next();
			if(pathMatcher.matchStart(urlPattern, requestURI)){// antpath를 사용하여 경로를 확인
				isPermittedAntPathURL = true;
			}
		}

		if(!isPermittedAntPathURL){
			// 현재도메인과 레퍼러의 도메인을 비교함
			String serverName = request.getServerName();
			String headerReferer = request.getHeader("referer");
			if(headerReferer != null && (headerReferer.startsWith("http://"+serverName) || headerReferer.startsWith("https://"+serverName))){
				return true;
			}
			throw new SecureException(egovMessageSource.getMessage("errors.security.referer.error"));
		}else{
			return true;
		}

	}

}
