/*************************************************************************
* CLASS 명	: UserAuthGrpController
* 작 업 자	: 현걸욱
* 작 업 일	: 2018-1-19
* 기	능	: 사용자권한그룹설정 Controller
* ---------------------------- 변 경 이 력 --------------------------------
* 번호	작 업 자		작	업	일			변 경 내 용				비고
* ----	---------	----------------	---------------------	-----------
*	1	현걸욱		2018-1-19
**************************************************************************/
package kr.ispark.system.system.menu.userAuthGrp.web;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import egovframework.let.utl.fcc.service.EgovStringUtil;
import kr.ispark.system.system.menu.userAuthGrp.service.impl.UserAuthGrpServiceImpl;
import kr.ispark.system.system.menu.userAuthGrp.service.UserAuthGrpVO;
/*공통 컨트롤러*/
import kr.ispark.common.web.BaseController;

@Controller
public class UserAuthGrpController extends BaseController {
	@Autowired
	private UserAuthGrpServiceImpl userAuthGrpService;
	
	/**
	 * 사용자권한그룹설정 목록 화면
	 * @param	UserAuthGrpVO searchVO
	 * @param	Model model
	 * @return	String
	 * @throws	Exception
	 */
	@RequestMapping(value="/system/system/menu/userAuthGrp/userAuthGrpList.do")
	public String userAuthGrpList(@ModelAttribute("searchVO") UserAuthGrpVO searchVO, Model model) throws Exception {
		return "/system/system/menu/userAuthGrp/userAuthGrpList." + searchVO.getLayout();
	}
	
	/**
	 * 사용자권한그룹설정 그리드 조회(json)
	 * @param searchVO
	 * @return	ModelAndView
	 * @throws Exception
	 */
	@RequestMapping(value="/system/system/menu/userAuthGrp/userAuthGrpList_json.do")
	public ModelAndView userAuthGrpList_json(@ModelAttribute("searchVO") UserAuthGrpVO searchVO)throws Exception {
		
		List<UserAuthGrpVO> dataList = userAuthGrpService.selectList(searchVO);
		return makeGridJsonData(dataList, dataList.size(), searchVO);
	}
	
	/**
	 * 사용자권한그룹설정 그리드 조회(json)
	 * @param searchVO
	 * @return	ModelAndView
	 * @throws Exception
	 */
	@RequestMapping(value="/system/system/menu/userAuthGrp/deptList_json.do")
	public ModelAndView deptList_json(@ModelAttribute("searchVO") UserAuthGrpVO searchVO)throws Exception {
		
		List<UserAuthGrpVO> dataList = userAuthGrpService.selectDeptList(searchVO);
		return makeJsonListData(dataList);
	}
	
	/**
	 * 사용자권한그룹설정 그리드 조회(json)
	 * @param searchVO
	 * @return	ModelAndView
	 * @throws Exception
	 */
	@RequestMapping(value="/system/system/menu/userAuthGrp/userList_json.do")
	public ModelAndView userList_json(@ModelAttribute("searchVO") UserAuthGrpVO searchVO)throws Exception {
		
		List<UserAuthGrpVO> userList = userAuthGrpService.selectUserList(searchVO);
		List<UserAuthGrpVO> selectedUserList = userAuthGrpService.selectSelectedUserList(searchVO);
		HashMap<String,Object> resultMap = new HashMap<String,Object>();
		resultMap.put("userList", userList);
		resultMap.put("selectedUserList", selectedUserList);
		
		return new ModelAndView("jsonView",resultMap);
	}
	
	//excelDownload
	/**
	 * 엑셀양식다운로드
	 */
	@RequestMapping("/system/system/menu/userAuthGrp/excelDownload.do")
	public String excelDownload(@ModelAttribute("searchVO") UserAuthGrpVO searchVO,
			Model model)throws Exception {
		
		/*
		 * 현재페이지 화면에 맞게 반드시 수정할 것.
		 */
		
		List<UserAuthGrpVO> dataList = userAuthGrpService.selectList(searchVO);
		
		// 타이틀
		model.addAttribute("title", egovMessageSource.getMessage("word.userAuthGrpManage"));
		
		// 검색조건
		model.addAttribute("year", egovMessageSource.getMessage("word.year"));
		model.addAttribute("findYear", searchVO.getFindYear());
		
		// header
		model.addAttribute("userAuthGrpNm", egovMessageSource.getMessage("word.userAuthGrpNm"));	// 사용자권한그룹설정
		model.addAttribute("sortOrder", egovMessageSource.getMessage("word.sortOrder"));			// 정렬순서
		
		// 조직 데이터
		model.addAttribute("dataList", dataList);
		
		// 시트명
		model.addAttribute("sheetName", egovMessageSource.getMessage("word.userAuthGrpNm"));
		
		model.addAttribute("destJxlsFileName", egovMessageSource.getMessage("word.userAuthGrpNm") + "_" + EgovStringUtil.getTimeStamp()+".xlsx");
		model.addAttribute("templateJxlsFileName", "userAuthGrpList.xlsx");

		return "excelDownloadView";
	}

	/**
	 * 사용자권한그룹설정 조회
	 * @param searchVO
	 * @return	ModelAndView
	 * @throws Exception
	 */
	@RequestMapping(value="/system/system/menu/userAuthGrp/selectDetail.do")
	public ModelAndView selectDetail(@ModelAttribute("searchVO") UserAuthGrpVO searchVO)throws Exception {
		return makeJsonData(userAuthGrpService.selectDetail(searchVO));
	}
	
	/**
	 * 사용자권한그룹설정 정렬순서저장
	 * @param dataVO
	 * @param model
	 * @param bindingResult
	 * @return	ModelAndView
	 * @throws Exception
	 */
	@RequestMapping("/system/system/menu/userAuthGrp/saveSortOrder.do")
	public ModelAndView saveSortOrder(@ModelAttribute("dataVO") UserAuthGrpVO dataVO, Model model, BindingResult bindingResult) throws Exception {
		// list 유효성 체크
		validateList(dataVO.getGridDataList(), bindingResult);
		if(bindingResult.hasErrors()) {
			return makeFailJsonData(getListErrorMsg(bindingResult));
		}
		
		int resultCnt = userAuthGrpService.updateSortOrder(dataVO);
		if(resultCnt == 0) {
			return makeFailJsonData();
		}
		return makeSuccessJsonData();
	}
	
	/**
	 * 사용자권한그룹설정 삭제
	 * @param dataVO
	 * @param model
	 * @param bindingResult
	 * @return	ModelAndView
	 * @throws Exception
	 */
	@RequestMapping("/system/system/menu/userAuthGrp/deleteUserAuthGrp.do")
	public ModelAndView deleteUserAuthGrp(@ModelAttribute("dataVO") UserAuthGrpVO dataVO, Model model) throws Exception {
		int resultCnt = userAuthGrpService.deleteUserAuthGrp(dataVO);
		if(resultCnt == 0) {
			return makeFailJsonData();
		}
		return makeSuccessJsonData();
	}
	
	/**
	 * 사용자권한그룹설정 저장
	 * @param dataVO
	 * @param model
	 * @param bindingResult
	 * @return	ModelAndView
	 * @throws Exception
	 */
	@RequestMapping("/system/system/menu/userAuthGrp/saveUserAuthGrp.do")
	public ModelAndView saveUserAuthGrp(@ModelAttribute("dataVO") UserAuthGrpVO dataVO, Model model, BindingResult bindingResult) throws Exception {
		beanValidator.validate(dataVO, bindingResult);
		if(bindingResult.hasErrors()){
			return makeFailJsonData(getListErrorMsg(bindingResult));
		}

		int resultCnt = userAuthGrpService.saveData(dataVO);
		if(resultCnt == 0) {
			return makeFailJsonData();
		}
		return makeSuccessJsonData();
	}
}

