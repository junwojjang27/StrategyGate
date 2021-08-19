package kr.ispark.common.web;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import kr.ispark.common.exception.SecureException;

@Controller
public class ErrorController extends BaseController {
	public final Logger log = LoggerFactory.getLogger(this.getClass());

	@RequestMapping(value="/error/404.do")
	public void error404(HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
		log.info("404 Error");
		response.setStatus(HttpServletResponse.SC_NOT_FOUND);
		request.setAttribute("msg", egovMessageSource.getMessage("errors.http404"));
		handle(request, response, model);
	}

	@RequestMapping(value="/error/500.do")
	public void error500(HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
		log.info("500 Error");
		response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		request.setAttribute("msg", egovMessageSource.getMessage("errors.errorOccurred"));
		handle(request, response, model);
	}

	@RequestMapping(value="/error/throwable.do")
	public void errorThrowable(HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
		log.info("Throwable Error");
		response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		if(request.getAttribute("javax.servlet.error.exception") instanceof SecureException) {
			request.setAttribute("msg", egovMessageSource.getMessage("errors.secureException"));
			request.setAttribute("errorType", "secure");
		} else {
			request.setAttribute("msg", egovMessageSource.getMessage("errors.errorOccurred"));
			request.setAttribute("errorType", "error");
		}
		handle(request, response, model);
	}

	@RequestMapping(value="/error/accessDenied.do")
	public void errorAccessDenied(HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
		log.info("AccessDenied Error");
		response.setStatus(HttpServletResponse.SC_FORBIDDEN);
		request.setAttribute("msg", egovMessageSource.getMessage("errors.secureException"));
		request.setAttribute("errorType", "secure");
		handle(request, response, model);
	}

	public void handle(HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
		String msg = (String)request.getAttribute("msg");
		response.setCharacterEncoding("UTF-8");

		// ajax 요청일 경우
		if ("XMLHttpRequest".equals(request.getHeader("X-Requested-With"))){
			String data = StringUtils.join(new String[] {
					" { ",
					" \"result\" : \""+ AJAX_FAIL +"\", ",
					" \"msg\" : \"", msg , "\" ",
					" } "
			});
			PrintWriter out = response.getWriter();
			out.print(data);
			out.flush();
			out.close();
		} else {
			response.sendRedirect(request.getContextPath() + "/login.do?" + request.getAttribute("errorType"));
		}
	}
}
