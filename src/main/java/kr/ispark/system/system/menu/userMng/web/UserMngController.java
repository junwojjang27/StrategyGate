/*************************************************************************
* CLASS 명	: UserMngController
* 작 업 자	: 현걸욱
* 작 업 일	: 2018-1-21
* 기	능	: 사용자관리 Controller
* ---------------------------- 변 경 이 력 --------------------------------
* 번호	작 업 자		작	업	일			변 경 내 용				비고
* ----	---------	----------------	---------------------	-----------
*	1	현걸욱		2018-1-21
**************************************************************************/
package kr.ispark.system.system.menu.userMng.web;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.StandardPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import egovframework.let.utl.fcc.service.EgovStringUtil;
import kr.ispark.system.system.menu.userMng.service.impl.UserMngServiceImpl;
import kr.ispark.system.system.menu.userMng.service.UserMngVO;
import kr.ispark.common.util.SessionUtil;
/*공통 컨트롤러*/
import kr.ispark.common.web.BaseController;

@Controller
public class UserMngController extends BaseController {
	@Autowired
	private UserMngServiceImpl userMngService;
	
	@Autowired
	StandardPasswordEncoder passwordEncoder;
	/**
	 * 사용자관리 목록 화면
	 * @param	UserMngVO searchVO
	 * @param	Model model
	 * @return	String
	 * @throws	Exception
	 */
	@RequestMapping(value="/system/system/menu/userMng/userMngList.do")
	public String userMngList(@ModelAttribute("searchVO") UserMngVO searchVO, Model model) throws Exception {
		
		//findYear값을 현재 년도로 변경
		Calendar cal = new GregorianCalendar(); //new GregorianCalendar()생성자가 호출되는 시점에 날짜 관련된 모든 값을 cal에 넣어줌.
		int year = 	cal.get(Calendar.YEAR);
		String strYear = String.valueOf(year);
		searchVO.setFindYear(strYear);
		
		/*
		 * 관리자의 로그인 전환 구분을 위한 key
		 * CustomAuthenticationProvider에서 로그인 처리를 할 때 이 key가 있으면 관리자의 로그인 전환을 처리
		 */
		SessionUtil.setAttribute("ADMIN_LOGIN_KEY", passwordEncoder.encode(String.valueOf(System.currentTimeMillis())));
		
		return "/system/system/menu/userMng/userMngList." + searchVO.getLayout();
	}
	
	/**
	 * 사용자관리 그리드 조회(json)
	 * @param searchVO
	 * @return	ModelAndView
	 * @throws Exception
	 */
	@RequestMapping(value="/system/system/menu/userMng/userMngList_json.do")
	public ModelAndView userMngList_json(@ModelAttribute("searchVO") UserMngVO searchVO)throws Exception {
		
		List<UserMngVO> dataList = userMngService.selectList(searchVO);
		return makeGridJsonData(dataList, dataList.size(), searchVO);
	}
	
	//excelDownload
	/**
	 * 엑셀양식다운로드
	 */
	@RequestMapping("/system/system/menu/userMng/excelDownload.do")
	public String excelDownload(@ModelAttribute("searchVO") UserMngVO searchVO,
			Model model)throws Exception {
		
		/*
		 * 현재페이지 화면에 맞게 반드시 수정할 것.
		 */
		
		List<UserMngVO> dataList = userMngService.selectList(searchVO);
		
		// 타이틀
		model.addAttribute("title", egovMessageSource.getMessage("word.userMngManage"));
		
		// 검색조건
		model.addAttribute("year", egovMessageSource.getMessage("word.year"));
		model.addAttribute("findYear", searchVO.getFindYear());
		
		// header
		model.addAttribute("userMngNm", egovMessageSource.getMessage("word.userMngNm"));	// 사용자관리
		model.addAttribute("sortOrder", egovMessageSource.getMessage("word.sortOrder"));			// 정렬순서
		
		// 조직 데이터
		model.addAttribute("dataList", dataList);
		
		// 시트명
		model.addAttribute("sheetName", egovMessageSource.getMessage("word.userMngNm"));
		
		model.addAttribute("destJxlsFileName", egovMessageSource.getMessage("word.userMngNm") + "_" + EgovStringUtil.getTimeStamp()+".xlsx");
		model.addAttribute("templateJxlsFileName", "userMngList.xlsx");

		return "excelDownloadView";
	}
}

