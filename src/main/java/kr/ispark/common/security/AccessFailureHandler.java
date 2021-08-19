package kr.ispark.common.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

public class AccessFailureHandler implements AccessDeniedHandler {
	private static final Logger logger = LoggerFactory.getLogger(AccessFailureHandler.class);
	
	private String errorPage;
	
	public AccessFailureHandler() {
	}
	
	public AccessFailureHandler(String errorPage) {
		this.errorPage = errorPage;
	}
	
	public String getErrorPage() {
		return errorPage;
	}
	
	public void setErrorPage(String errorPage) {
		this.errorPage = errorPage;
	}

	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException exception)
			throws IOException, ServletException {
		/*
		String message = exception.getMessage();
		response.setStatus(HttpServletResponse.SC_FORBIDDEN);
		response.setCharacterEncoding("UTF-8");
		
		// ajax 요청일 경우
		if ("XMLHttpRequest".equals(request.getHeader("X-Requested-With"))){
			String data = StringUtils.join(new String[] {
					" { ",
					" \"result\" : \"FAIL\", ",
					" \"msg\" : \"", message , "\" ",
					" } "
			});
			PrintWriter out = response.getWriter();
			out.print(data);
			out.flush();
			out.close();
		} else {
			request.setAttribute("message", exception.getMessage());
			request.getRequestDispatcher(errorPage).forward(request, response);
		}
		*/
		request.setAttribute("exception", exception);
		request.getRequestDispatcher(errorPage).forward(request, response);
	}
}
