package kr.ispark.common.security;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

public class CustomCsrfRequestMatcher implements RequestMatcher {
	private RequestMatcher apiMatcher = new AntPathRequestMatcher("/api/apiTest.do");

	@Override
	public boolean matches(HttpServletRequest request) {
		/*
		 * POST 요청이면서 url이 /api/apiTest.do가 아닌 경우 csrf 체크 (true면 체크, false면 체크 하지 않음)
		 */
		return (request.getMethod().equals("POST") && !apiMatcher.matches(request));
	}
}
