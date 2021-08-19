package kr.ispark.common.security;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.StandardPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import kr.ispark.common.security.service.UserVO;
import kr.ispark.common.security.service.impl.LoginServiceImpl;
import kr.ispark.common.util.CommonUtil;
import kr.ispark.common.util.PropertyUtil;
import kr.ispark.common.util.SessionUtil;

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {
	public final Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired
	LoginServiceImpl loginService;
	
	@Autowired
	StandardPasswordEncoder passwordEncoder;

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {

		log.debug("\n\n#### CustomAuthenticationProvider.authenticate ###");

		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes()).getRequest();
		HttpServletResponse response = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getResponse();
		
		String userId = CommonUtil.nullToBlank(request.getParameter("userId"));
		String password = CommonUtil.nullToBlank(request.getParameter("password"));

		// 슈퍼관리자가 사용자 관리에서 계정 전환을 하는 경우를 체크하기 위한 변수들
		String superLoginKey = CommonUtil.removeNull(request.getParameter("superLoginKey"));

		// 일반관리자가 사용자 관리에서 계정 전환을 하는 경우를 체크하기 위한 변수들
		String adminLoginKey = CommonUtil.removeNull(request.getParameter("adminLoginKey"));
		if(SessionUtil.hasRole("01")
			&& CommonUtil.isNotEmpty(adminLoginKey)
			&& adminLoginKey.equals(CommonUtil.removeNull((String)SessionUtil.getAttribute("ADMIN_LOGIN_KEY")))) {
		}

		UserVO userVO = new UserVO();
		userVO.setUserId(userId);
		userVO.setIp(request.getRemoteAddr());

		try {
			
			UserVO user = loginService.selectUser(userVO);
			
			/*
			 * 슈퍼관리자가 사용자 관리 메뉴에서 계정 전환을 하는 경우에 대한 판단
			 * 1. 현재 로그인한 사용자가 SUPER 권한을 갖고 있음
			 * 3. superLoginKey 파라미터가 존재
			 * 4. superLoginKey 파라미터의 값과 세션의 SUPER_LOGIN_KEY 값이 같음 (SUPER_LOGIN_KEY 값은 사용자관리 목록 화면-UserAdminMngController.userAdminMngList-에서 생성함)
			 * 5. 1~4의 조건을 충족하고 전환하려는 계정이 존재하면 로그인 처리
			 */
			if(SessionUtil.hasRole(PropertyUtil.getProperty("Super.AuthCodeId"))
				&& CommonUtil.isNotEmpty(superLoginKey)
				&& superLoginKey.equals(CommonUtil.removeNull((String)SessionUtil.getAttribute("SUPER_LOGIN_KEY")))) {
				if(user == null) {
					return null;
				} else	{
					// 관리자가 계정 전환을 한 경우 비밀번호 변경 알림을 표시하지 않음
					user.setAlertPwChangeYn("N");
				}
			/*
			 * 일반관리자가 사용자 관리 메뉴에서 계정 전환을 하는 경우에 대한 판단
			 * 1. 현재 로그인한 사용자가 관리자(01) 권한을 갖고 있음
			 * 2. adminLoginKey 파라미터가 존재
			 * 4. adminLoginKey 파라미터의 값과 세션의 ADMIN_LOGIN_KEY 값이 같음 (ADMIN_LOGIN_KEY 값은 사용자관리 목록 화면-CompUserMngController.compUserMngList-에서 생성함)
			 * 5. 1~4의 조건을 충족하고 전환하려는 계정이 존재하면 로그인 처리
			 */
			} else if(SessionUtil.hasRole(PropertyUtil.getProperty("Admin.AuthCodeId"))
					&& CommonUtil.isNotEmpty(adminLoginKey)
					&& adminLoginKey.equals(CommonUtil.removeNull((String)SessionUtil.getAttribute("ADMIN_LOGIN_KEY")))) {
				if(user == null) {
					return null;
				} else	{
					// 관리자가 계정 전환을 한 경우 비밀번호 변경 알림을 표시하지 않음
					user.setAlertPwChangeYn("N");
				}
			/*
			 * 일반 로그인
			 * 1. 계정이 존재하고 비밀번호가 맞으면 로그인 처리
			 */
			} else {
				/*if(user == null || !passwordEncoder.matches(password, user.getPasswd())) {
					return null;
				}*/
			}
			
			// 권한 목록
			List<String> adminGubunList = loginService.selectAdminGubunList(userVO);
			user.setAdminGubunList(adminGubunList);
			ArrayList<GrantedAuthority> grantedAuthList = new ArrayList<GrantedAuthority>();
			for(String admingubun : adminGubunList) {
				grantedAuthList.add(new SimpleGrantedAuthority(admingubun));
			}

			return new UsernamePasswordAuthenticationToken(user, user.getUserId(), grantedAuthList);
		} catch (SQLException sqe) {
			log.error("error : "+sqe.getCause());
			return null;
		} catch (Exception e) {
			log.error("error : "+e.getCause());
			return null;
		}
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return true;
	}

}
