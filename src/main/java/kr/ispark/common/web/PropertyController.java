/*************************************************************************
* CLASS 명      : PropertyController
* 작 업 자      : kimyh
* 작 업 일      : 2016년 9월 27일
* 기    능      : Property i18n Test
* ---------------------------- 변 경 이 력 --------------------------------
* 번호   작 업 자      작   업   일        변 경 내 용              비고
* ----  ---------  -----------------  -------------------------    --------
*   1    kimyh      2016년 9월 27일         최 초 작 업
**************************************************************************/
package kr.ispark.common.web;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class PropertyController {
	/**
	 * jquery.i18n용 message 파일 리턴
	 * @return /properties/Messages.properties
	 * @exception Exception
	 */
	@RequestMapping(value="/properties/Messages.properties")
	public void getMessages(HttpServletRequest request, HttpServletResponse response) throws Exception {
		getMessages(response, LocaleContextHolder.getLocale().getLanguage());
	}
	
	@RequestMapping(value="/properties/Messages_{language}.properties")
	public void getMessagesByLocale(HttpServletRequest request, HttpServletResponse response, @PathVariable String language) throws Exception {
		getMessages(response, language);
	}
	
	private void getMessages(HttpServletResponse response, String locale) throws Exception {
		response.setContentType("text/x-java-properties");
		OutputStream outputStream = response.getOutputStream();
		Resource resource = new ClassPathResource("/message/message-for-js_" + locale + ".properties");
		if(resource.exists()) {
			InputStream inputStream = resource.getInputStream();

			List<String> readLines = IOUtils.readLines(inputStream, "UTF-8");
			IOUtils.writeLines(readLines, null, outputStream, "UTF-8");

			IOUtils.closeQuietly(inputStream);
			IOUtils.closeQuietly(outputStream);
		}
	}
}
