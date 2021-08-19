package egovframework.com.sec.ram.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import egovframework.com.cmm.service.EgovUserDetailsService;
/* 20161125
 * isAuthenticated() 메소드를 이전에는 EgovUserDetailsHelper.isAuthenticated() 사용했기 때문에 문제 발생시 참조가 필요할 것 같아 남겨둠.
 * 아래의 두 EgovUserDetailsHelper 중 어느 것을 사용해야할지 확인 필요.
 * 기존에는 egovframework.rte.fdl.security.userdetails.util.EgovUserDetailsHelper; 를 사용했는데
 * 전체 프로젝트 중 다른 쪽에서는 com.cmm.util.EgovUserDetailsHelper를 사용하는데 여기서만 rte.fdl.security.userdetails.util.EgovUserDetailsHelper;를 사용했던 것이 이상함.   
import egovframework.com.cmm.util.EgovUserDetailsHelper;
import egovframework.rte.fdl.security.userdetails.util.EgovUserDetailsHelper;
*/
import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;
import egovframework.rte.fdl.security.userdetails.EgovUserDetails;
import egovframework.rte.fdl.string.EgovObjectUtil;



public class EgovUserDetailsSecurityServiceImpl extends EgovAbstractServiceImpl implements EgovUserDetailsService {
	/**
	 * 인증된 사용자객체를 VO형식으로 가져온다.
	 * @return Object - 사용자 ValueObject
	 */
	public Object getAuthenticatedUser() {
		// 기존의 경우 로그인되지 않은 경우 SecurityContext의 getAuthentication()의 리턴 값이 null이었으나,
		// 신규 버전의 경우는 null이 아닌 값으로 처리된다.

		if(isAuthenticated()) {

			SecurityContext context = SecurityContextHolder.getContext();
			Authentication authentication = context.getAuthentication();

			if (EgovObjectUtil.isNull(authentication)) {
				return null;
			}

			if (authentication.getPrincipal() instanceof EgovUserDetails) {
				EgovUserDetails details = (EgovUserDetails) authentication.getPrincipal();

				return details.getEgovUserVO();
			} else {
				return authentication.getPrincipal();
			}
		}

		return null;
		
	}

	
	/**
	 * 인증된 사용자의 권한 정보를 가져온다.
	 * 예) [ROLE_ADMIN, ROLE_USER, ROLE_A, ROLE_B, ROLE_RESTRICTED, IS_AUTHENTICATED_FULLY, IS_AUTHENTICATED_REMEMBERED, IS_AUTHENTICATED_ANONYMOUSLY]
	 * @return List - 사용자 권한정보 목록
	 */
	public List<String> getAuthorities() {
		List<String> listAuth = new ArrayList<String>();

		SecurityContext context = SecurityContextHolder.getContext();
		Authentication authentication = context.getAuthentication();

		if (EgovObjectUtil.isNull(authentication)) {
			// log.debug("## authentication object is null!!");
			return null;
		}

		Collection<GrantedAuthority> authorities = (Collection<GrantedAuthority>) authentication.getAuthorities();

		for (GrantedAuthority authority : authorities) {
			listAuth.add(authority.getAuthority());
		}

		/*
		for (int i = 0; i < authorities.length; i++) {
			listAuth.add(authorities[i].getAuthority());

			// log.debug("## EgovUserDetailsHelper.getAuthorities : Authority is " + authorities[i].getAuthority());
		}
		*/

		return listAuth;
	}
	
	/**
	 * 인증된 사용자 여부를 체크한다.
	 * @return Boolean - 인증된 사용자 여부(TRUE / FALSE)	
	 */

	public Boolean isAuthenticated() {
		SecurityContext context = SecurityContextHolder.getContext();
		Authentication authentication = context.getAuthentication();
		
		if (EgovObjectUtil.isNull(authentication)) {
			return Boolean.FALSE;
		}
		
		String username = authentication.getName();
		if (username.equals("anonymousUser") || username.equals("roleAnonymous")) {
			return Boolean.FALSE;
		}

		Object principal = authentication.getPrincipal();
		
		return Boolean.valueOf(!EgovObjectUtil.isNull(principal));
	}

}
