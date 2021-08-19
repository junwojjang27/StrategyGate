/*************************************************************************
* CLASS 명	: UserAdminMngController
* 작 업 자	: 박정현
* 작 업 일	: 2018-07-04
* 기	능	: 사용자관리 Controller
* ---------------------------- 변 경 이 력 --------------------------------
* 번호	작 업 자		작	업	일			변 경 내 용				비고
* ----	---------	----------------	---------------------	-----------
*	1	박정현		2018-07-04
**************************************************************************/
package kr.ispark.superMng.superMng.superMng.userAdminMng.web;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.StandardPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import kr.ispark.common.system.service.DeptVO;
import kr.ispark.common.util.SessionUtil;
/*공통 컨트롤러*/
import kr.ispark.common.web.BaseController;
import kr.ispark.superMng.superMng.superMng.userAdminMng.service.UserAdminMngVO;
import kr.ispark.superMng.superMng.superMng.userAdminMng.service.impl.UserAdminMngServiceImpl;

@Controller
public class UserAdminMngController extends BaseController {
	@Autowired
	private UserAdminMngServiceImpl userAdminMngService;
	
	@Autowired
	StandardPasswordEncoder passwordEncoder;
	
	/**
	 * 사용자관리 목록 화면
	 * @param	UserAdminMngVO searchVO
	 * @param	Model model
	 * @return	String
	 * @throws	Exception
	 */
	@RequestMapping("/superMng/superMng/superMng/userAdminMng/userAdminMngList.do")
	public String userAdminMngList(@ModelAttribute("searchVO") UserAdminMngVO searchVO, Model model) throws Exception {

		//findYear값을 현재 년도로 변경
		Calendar cal = new GregorianCalendar(); //new GregorianCalendar()생성자가 호출되는 시점에 날짜 관련된 모든 값을 cal에 넣어줌.
		int year = 	cal.get(Calendar.YEAR);
		String strYear = String.valueOf(year);
		searchVO.setFindYear(strYear);
		
		List<UserAdminMngVO> compList = userAdminMngService.selectCompList(searchVO);
		model.addAttribute("compList", compList);
		if( compList != null && compList.size() > 0) {
			UserAdminMngVO compData = compList.get(0);
			searchVO.setFindCompId(compData.getCompId());
		}
		List<UserAdminMngVO> authList = userAdminMngService.selectAuthList(searchVO);
		model.addAttribute("authList", authList);

		/*
		 * 슈퍼관리자의 로그인 전환 구분을 위한 key
		 * CustomAuthenticationProvider에서 로그인 처리를 할 때 이 key가 있으면 슈퍼관리자의 로그인 전환을 처리
		 */
		SessionUtil.setAttribute("SUPER_LOGIN_KEY", passwordEncoder.encode(String.valueOf(System.currentTimeMillis())));
		
		return "/superMng/superMng/superMng/userAdminMng/userAdminMngList." + searchVO.getLayout();
	}
	
	/**
	 * 사용자관리 그리드 조회(json)
	 * @param	UserAdminMngVO searchVO
	 * @return	ModelAndView
	 * @throws	Exception
	 */
	@RequestMapping("/superMng/superMng/superMng/userAdminMng/userAdminMngList_json.do")
	public ModelAndView userAdminMngList_json(@ModelAttribute("searchVO") UserAdminMngVO searchVO) throws Exception {
		List<UserAdminMngVO> dataList = userAdminMngService.selectList(searchVO);
		return makeGridJsonData(dataList, dataList.size(), searchVO);
	}
	
	/**
	 * 조직 목록 조회
	 * @param searchVO
	 * @return	ModelAndView
	 * @throws Exception
	 */
	@RequestMapping(value="/superMng/superMng/superMng/userAdminMng/selectDeptList.do")
	public ModelAndView selectDeptList(@ModelAttribute("searchVO") UserAdminMngVO searchVO)throws Exception {
		List<DeptVO> deptList = userAdminMngService.selectDeptList(searchVO);
		
		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("deptList", deptList);
		return new ModelAndView("jsonView", resultMap);
	}
	
	/**
	 * 조직, 권한 목록 조회
	 * @param searchVO
	 * @return	ModelAndView
	 * @throws Exception
	 */
	@RequestMapping(value="/superMng/superMng/superMng/userAdminMng/selectDeptAuthList.do")
	public ModelAndView selectDeptAuthList(@ModelAttribute("searchVO") UserAdminMngVO searchVO)throws Exception {
		List<UserAdminMngVO> authList = userAdminMngService.selectAuthList(searchVO);
		List<DeptVO> deptList = userAdminMngService.selectDeptList(searchVO);
		
		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("authList", authList);
		resultMap.put("deptList", deptList);
		return new ModelAndView("jsonView", resultMap);
	}
}

