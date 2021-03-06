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

		// ?????????????????? ????????? ???????????? ?????? ????????? ?????? ????????? ???????????? ?????? ?????????
		String superLoginKey = CommonUtil.removeNull(request.getParameter("superLoginKey"));

		// ?????????????????? ????????? ???????????? ?????? ????????? ?????? ????????? ???????????? ?????? ?????????
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
			 * ?????????????????? ????????? ?????? ???????????? ?????? ????????? ?????? ????????? ?????? ??????
			 * 1. ?????? ???????????? ???????????? SUPER ????????? ?????? ??????
			 * 3. superLoginKey ??????????????? ??????
			 * 4. superLoginKey ??????????????? ?????? ????????? SUPER_LOGIN_KEY ?????? ?????? (SUPER_LOGIN_KEY ?????? ??????????????? ?????? ??????-UserAdminMngController.userAdminMngList-?????? ?????????)
			 * 5. 1~4??? ????????? ???????????? ??????????????? ????????? ???????????? ????????? ??????
			 */
			if(SessionUtil.hasRole(PropertyUtil.getProperty("Super.AuthCodeId"))
				&& CommonUtil.isNotEmpty(superLoginKey)
				&& superLoginKey.equals(CommonUtil.removeNull((String)SessionUtil.getAttribute("SUPER_LOGIN_KEY")))) {
				if(user == null) {
					return null;
				} else	{
					// ???????????? ?????? ????????? ??? ?????? ???????????? ?????? ????????? ???????????? ??????
					user.setAlertPwChangeYn("N");
				}
			/*
			 * ?????????????????? ????????? ?????? ???????????? ?????? ????????? ?????? ????????? ?????? ??????
			 * 1. ?????? ???????????? ???????????? ?????????(01) ????????? ?????? ??????
			 * 2. adminLoginKey ??????????????? ??????
			 * 4. adminLoginKey ??????????????? ?????? ????????? ADMIN_LOGIN_KEY ?????? ?????? (ADMIN_LOGIN_KEY ?????? ??????????????? ?????? ??????-CompUserMngController.compUserMngList-?????? ?????????)
			 * 5. 1~4??? ????????? ???????????? ??????????????? ????????? ???????????? ????????? ??????
			 */
			} else if(SessionUtil.hasRole(PropertyUtil.getProperty("Admin.AuthCodeId"))
					&& CommonUtil.isNotEmpty(adminLoginKey)
					&& adminLoginKey.equals(CommonUtil.removeNull((String)SessionUtil.getAttribute("ADMIN_LOGIN_KEY")))) {
				if(user == null) {
					return null;
				} else	{
					// ???????????? ?????? ????????? ??? ?????? ???????????? ?????? ????????? ???????????? ??????
					user.setAlertPwChangeYn("N");
				}
			/*
			 * ?????? ?????????
			 * 1. ????????? ???????????? ??????????????? ????????? ????????? ??????
			 */
			} else {
				/*if(user == null || !passwordEncoder.matches(password, user.getPasswd())) {
					return null;
				}*/
			}
			
			// ?????? ??????
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
