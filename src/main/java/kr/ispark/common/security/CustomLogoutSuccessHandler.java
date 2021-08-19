package kr.ispark.common.security;

import java.io.IOException;
import java.sql.SQLException;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;
import org.springframework.stereotype.Component;

import egovframework.com.sym.log.clg.service.EgovLoginLogService;
import egovframework.com.sym.log.clg.service.LoginLog;
import kr.ispark.common.security.service.UserVO;

@Component
public class CustomLogoutSuccessHandler extends SimpleUrlLogoutSuccessHandler {
	public final Logger log = LoggerFactory.getLogger(this.getClass());
	
	@Resource(name="EgovLoginLogService")
	private EgovLoginLogService loginLogService;
	
	/* (non-Javadoc)
	 * @see org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler#onLogoutSuccess(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, org.springframework.security.core.Authentication)
	 */
	@Override
	public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
			throws IOException, ServletException {
		LoginLog loginLog = new LoginLog();
		if(authentication != null) {
			UserVO userVO = (UserVO)authentication.getPrincipal();
			loginLog.setLoginId(userVO.getUserId());
		}
		
		loginLog.setLoginIp(request.getRemoteAddr());
		loginLog.setLoginMthd("O"); // 로그인:I, 로그아웃:O
		loginLog.setErrOccrrAt("N");
		loginLog.setErrorCode("");
		
		try {
			loginLogService.logInsertLoginLog(loginLog);
		} catch (SQLException sqe) {
			sqe.getCause();
		} catch (Exception e) {
			e.getCause();
		}
		
		setDefaultTargetUrl("/");
		
		super.onLogoutSuccess(request, response, authentication);
	}
}
