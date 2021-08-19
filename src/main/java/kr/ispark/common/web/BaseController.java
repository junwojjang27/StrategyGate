/*************************************************************************
* CLASS 명	: BaseController
* 작 업 자	: kimyh
* 작 업 일	: 2017. 12. 11.
* 기	능	: 모든 Controller의 공통적으로 사용하는 메소드와 변수를 모아놓은 Controller
* 			모든 Controller는 BaseController를 extends해서 생성해야 함
* ---------------------------- 변 경 이 력 --------------------------------
* 번호	작 업 자		작	업	일			변 경 내 용				비고
* ----	---------	----------------	---------------------	-----------
*	1	kimyh		2017. 12. 11.		최 초 작 업
**************************************************************************/
package kr.ispark.common.web;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.NoSuchMessageException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.WebUtils;
import org.springmodules.validation.commons.DefaultBeanValidator;

import egovframework.com.cmm.EgovMessageSource;
import kr.ispark.common.CommonVO;

public class BaseController {
	public final Logger log = LoggerFactory.getLogger(this.getClass());

	@Resource(name="egovMessageSource")
	public EgovMessageSource egovMessageSource;

	@Autowired
	public DefaultBeanValidator beanValidator;

	// ajax 프로세스 처리 성공시 (common.js에도 선언 필요)
	public final String AJAX_SUCCESS = "SUCCESS";

	// ajax 처리 성공 메시지 코드
	public final String AJAX_SUCCESS_MSG_CODE = "success.request.msg";

	// ajax 프로세스 처리 실패시 (common.js에도 선언 필요)
	public final String AJAX_FAIL = "FAIL";

	// ajax 처리 실패 메시지 코드
	public final String AJAX_FAIL_MSG_CODE = "fail.request.msg";

	// IE9용 파일 업로드 콜백 페이지
	public final String FILE_UPLOAD_CALLBACK_URL = "/common/fileUploadCallback.do";

	// 상세 조회용 json 데이터 생성
	public ModelAndView makeJsonData(CommonVO dataVO) {
		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("dataVO", dataVO);
		return new ModelAndView("jsonView", resultMap);
	}

	// list json 데이터 생성
	public ModelAndView makeJsonListData(List<? extends CommonVO> list) {
		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("list", list);
		return new ModelAndView("jsonView", resultMap);
	}

	// jqgrid용 json 데이터 생성 (데이터 전체 조회 또는 그리드 내에서 페이징 처리할 때 사용)
	public ModelAndView makeGridJsonData(List<? extends CommonVO> list) {
		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("rows", list);
		resultMap.put("page", 1);
		resultMap.put("total", 1);
		resultMap.put("records", list.size());

		return new ModelAndView("jsonView", resultMap);
	}

	// jqgrid용 json 데이터 생성 (페이징 쿼리 적용시 사용)
	public ModelAndView makeGridJsonData(List<?> list, int listCnt, CommonVO searchVO) {
		int page = searchVO.getPage();
		int rows = searchVO.getRows();
		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("rows", list);
		resultMap.put("page", page);
		resultMap.put("total", listCnt/rows + (listCnt%rows==0?0:1));
		resultMap.put("records", listCnt);

		return new ModelAndView("jsonView", resultMap);
	}

	// ajax용 성공 응답 생성(json)
	public ModelAndView makeSuccessJsonData() {
		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("result", AJAX_SUCCESS);
		resultMap.put("msg", egovMessageSource.getMessage(AJAX_SUCCESS_MSG_CODE));
		return makeSuccessJsonData(new HashMap<String, Object>());
	}

	// ajax용 성공 응답 생성(json) - 에러 msg 추가
	public ModelAndView makeSuccessJsonData(String msg) {
		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("msg", msg);
		return makeSuccessJsonData(resultMap);
	}

	// ajax용 성공 응답 생성(json)
	public ModelAndView makeSuccessJsonData(HashMap<String, Object> resultMap) {
		resultMap.put("result", AJAX_SUCCESS);
		if(resultMap.get("msg") == null) {
			resultMap.put("msg", egovMessageSource.getMessage(AJAX_SUCCESS_MSG_CODE));
		}
		return new ModelAndView("jsonView", resultMap);
	}

	// ajax용 성공 응답 생성(json) 듀얼화면 저장용(설문조사)
	public ModelAndView makeSuccessJsonDataDual(String key) {
		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("key", key);
		resultMap.put("result", AJAX_SUCCESS);
		if(resultMap.get("msg") == null) {
			resultMap.put("msg", egovMessageSource.getMessage(AJAX_SUCCESS_MSG_CODE));
		}
		return new ModelAndView("jsonView", resultMap);
	}
	
	// ajax용 실패 응답 생성(json)
	public ModelAndView makeFailJsonData() {
		return makeFailJsonData(new HashMap<String, Object>());
	}

	// ajax용 실패 응답 생성(json) - 에러 msg 추가
	public ModelAndView makeFailJsonData(String msg) {
		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("msg", msg);
		return makeFailJsonData(resultMap);
	}

	// ajax용 실패 응답 생성(json)
	public ModelAndView makeFailJsonData(HashMap<String, Object> resultMap) {
		resultMap.put("result", AJAX_FAIL);
		if(resultMap.get("msg") == null) {
			resultMap.put("msg", egovMessageSource.getMessage(AJAX_FAIL_MSG_CODE));
		}
		return new ModelAndView("jsonView", resultMap);
	}

	/**
	 * 결과값이 0 또는 -1이면 실패, 아니면 성공 json 응답 생성
	 * @param	int resultCnt
	 * @return	ModelAndView
	 */
	public ModelAndView makeJsonDataByResultCnt(int resultCnt) {
		if(resultCnt == 0 || resultCnt == -1) {
			return makeFailJsonData();
		}
		return makeSuccessJsonData();
	}

	/*
	 * 성공/실패 응답 처리
	 * - ajax 호출인 경우 : json으로 응답
	 * - form submit인 경우(IE9) : 응답 페이지로 포워딩
	 */
	/**
	 * 성공/실패 응답 처리
	 * - ajax 호출인 경우 : json으로 응답
	 * - form submit인 경우(IE9) : 응답 페이지로 포워딩
	 * @param	boolean isSuccess	: true : 성공, false : 실패
	 * @param	MultipartHttpServletRequest multiRequest
	 * @param	HttpServletResponse response
	 * @param	CommonVO dataVO
	 * @param	String msg	: 메시지
	 * @throws Exception
	 */
	public void resultHandling (boolean isSuccess, MultipartHttpServletRequest multiRequest, HttpServletResponse response, CommonVO dataVO) throws Exception {
		resultHandling(isSuccess, multiRequest, response, dataVO, null);
	}
	public void resultHandling (
			boolean isSuccess, MultipartHttpServletRequest multiRequest, HttpServletResponse response,
			CommonVO dataVO, String msg) throws Exception {
		// ajax 호출 여부 체크.
		boolean isAjaxCall = "XMLHttpRequest".equals(multiRequest.getHeader("X-Requested-With"));

		String result = isSuccess ? AJAX_SUCCESS : AJAX_FAIL;
		String msgCode = isSuccess ? AJAX_SUCCESS_MSG_CODE : AJAX_FAIL_MSG_CODE;
		String callbackFunc = isSuccess ? dataVO.getDoneCallbackFunc() : dataVO.getFailCallbackFunc();
		if(msg == null) {
			msg = egovMessageSource.getMessage(msgCode);
		}

		if(isAjaxCall) {
			String data = StringUtils.join(new String[] {
					" { ",
					" \"result\" : \"", result ,"\", ",
					" \"msg\" : \"", msg , "\" ",
					" } "
			});
			response.setCharacterEncoding("UTF-8");
			PrintWriter out = response.getWriter();
			out.print(data);
			out.flush();
			out.close();
		} else {
			multiRequest.setAttribute("result", result);
			multiRequest.setAttribute("msg", msg);
			multiRequest.setAttribute("callbackFunc", callbackFunc);
			multiRequest.getRequestDispatcher(FILE_UPLOAD_CALLBACK_URL).forward(multiRequest, response);
		}
	}

	// grid, list용 validation
	public void validateList(List<? extends CommonVO> gridDataList, BindingResult bindingResult) {
		for(Object vo : gridDataList) {
			beanValidator.validate(vo, bindingResult);
			if(bindingResult.hasErrors()) {
				break;
			}
		}
	}

	// list validation의 에러메시지 확인
	public String getListErrorMsg(BindingResult bindingResult) {
		String msg = null;
		FieldError fieldError = bindingResult.getFieldError();
		if(fieldError != null) {
			try {
				msg = egovMessageSource.getMessage(fieldError);
			} catch(NoSuchMessageException e) {
				msg = egovMessageSource.getMessage(AJAX_FAIL_MSG_CODE);
			}
		}

		if(msg == null) {
			msg = egovMessageSource.getMessage(AJAX_FAIL_MSG_CODE);
		}
		return msg;
	}

	// 사용자 쿠키에서 값을 가져옴
	public String getCookie(HttpServletRequest request, String cookieName) {
		return WebUtils.getCookie(request, cookieName) == null ? null : WebUtils.getCookie(request, cookieName).getValue();
	}

	/**
	 * jqgrid용 List to Map (페이징이 없는 경우)
	 * @param	List<CommonVO> dataList
	 * @return	HashMap<String, Object>
	 */
	public HashMap<String, Object> makeResultMapForGrid(List<?> dataList) {
		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		int listCnt = dataList.size();
		resultMap.put("rows", dataList);
		resultMap.put("page", 1);
		resultMap.put("total", 1);
		resultMap.put("records", listCnt);
		return resultMap;
	}
}
