package kr.ispark.common.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import kr.ispark.common.security.service.UserVO;
import kr.ispark.common.security.service.impl.LoginServiceImpl;

public class CustomSecurityMetadataSource implements FilterInvocationSecurityMetadataSource {

	private Logger log = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	LoginServiceImpl loginService;
	
	private List<RequestMatcher> permittedUrlList;
	
	public CustomSecurityMetadataSource(List<String> permittedUrl) {
		permittedUrlList = new ArrayList<RequestMatcher>();
		if(permittedUrl != null) {
			for(String url : permittedUrl) {
				permittedUrlList.add(new AntPathRequestMatcher(url));
			}
		}
	}
	
	@Override
	public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException {
		log.debug("\n\n#### CustomSecurityMetadataSource.getAttributes ###");
		log.debug("\n" + ((FilterInvocation)object).getRequest().getServletPath());
		
		HttpServletRequest request = ((FilterInvocation)object).getRequest();
		Collection<ConfigAttribute> result = null;
		boolean doCheck = true;
		
		// 권한 체크 예외 URL 여부 확인
		for(RequestMatcher permittedPattern : permittedUrlList) {
			if(permittedPattern.matches(request)) {
				doCheck = false;
				break;
			}
		}
		
		// 권한 체크가 필요 없으면 통과 (return null인 경우 해당 페이지 접근 가능)
		if(!doCheck) {
			return null;
		}
		
		// 로그인 사용자의 URL별 권한 목록을 가져와서 비교
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		UserVO userVO = null;
		if(authentication != null) {
			if(authentication.getPrincipal() instanceof UserVO) {
				userVO = (UserVO)authentication.getPrincipal();
				
				if(userVO != null && !userVO.getRequestMap().isEmpty()) {
					for(Map.Entry<RequestMatcher, Collection<ConfigAttribute>> entry : userVO.getRequestMap().entrySet()){
						if(entry.getKey().matches(request)){
							result = entry.getValue();
							break;
						}
					}
				}
			}
		}
		
		// 요청한 URL에 해당하는 권한 정보가 없다면 권한 없음 처리
		if(result == null) {
			List<ConfigAttribute> configList = new LinkedList<ConfigAttribute>();
			configList.add(new SecurityConfig("ACCESS_DENIED"));
			result = configList;
		}
		return result;
	}

	@Override
	public Collection<ConfigAttribute> getAllConfigAttributes() {
		return null;
	}

	@Override
	public boolean supports(Class<?> clazz) {
		return FilterInvocation.class.isAssignableFrom(clazz);
	}
}
