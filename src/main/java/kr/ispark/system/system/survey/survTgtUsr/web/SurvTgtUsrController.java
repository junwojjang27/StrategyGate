/*************************************************************************
* CLASS 명	: SurvTgtUsrController
* 작 업 자	: 최학룡
* 작 업 일	: 2018-10-12
* 기	능	: 설문대상자 Controller
* ---------------------------- 변 경 이 력 --------------------------------
* 번호	작 업 자		작	업	일			변 경 내 용				비고
* ----	---------	----------------	---------------------	-----------
*	1	최학룡		2018-10-12
**************************************************************************/
package kr.ispark.system.system.survey.survTgtUsr.web;

import java.util.List;

import kr.ispark.common.security.service.UserVO;
import kr.ispark.common.system.service.impl.CommonServiceImpl;
/*공통 컨트롤러*/
import kr.ispark.common.web.BaseController;
import kr.ispark.system.system.survey.survReg.service.SurvRegVO;
import kr.ispark.system.system.survey.survReg.service.impl.SurvRegServiceImpl;
import kr.ispark.system.system.survey.survTgtUsr.service.SurvTgtUsrVO;
import kr.ispark.system.system.survey.survTgtUsr.service.impl.SurvTgtUsrServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import egovframework.com.cmm.util.EgovUserDetailsHelper;

@Controller
public class SurvTgtUsrController extends BaseController {
	@Autowired
	private SurvTgtUsrServiceImpl survTgtUsrService;

	@Autowired
	private SurvRegServiceImpl survRegService;

	@Autowired
	private CommonServiceImpl commonService;

	/**
	 * 설문대상자 목록 화면
	 * @param	SurvTgtUsrVO searchVO
	 * @param	Model model
	 * @return	String
	 * @throws	Exception
	 */
	@RequestMapping("/system/system/survey/survTgtUsr/survTgtUsrList.do")
	public String survTgtUsrList(@ModelAttribute("searchVO") SurvTgtUsrVO searchVO, Model model) throws Exception {
		SurvRegVO vo = new SurvRegVO();
		vo.setFindUseYn("Y");
		List<SurvRegVO> surveyList = survRegService.selectList(vo);
		model.addAttribute("surveyList", surveyList);
		return "/system/system/survey/survTgtUsr/survTgtUsrList." + searchVO.getLayout();
	}

	/**
	 * 설문대상자 그리드 조회(json)
	 * @param	SurvTgtUsrVO searchVO
	 * @return	ModelAndView
	 * @throws	Exception
	 */
	@RequestMapping("/system/system/survey/survTgtUsr/survTgtUsrList_json.do")
	public ModelAndView survTgtUsrList_json(@ModelAttribute("searchVO") SurvTgtUsrVO searchVO) throws Exception {
		List<SurvTgtUsrVO> dataList = survTgtUsrService.selectList(searchVO);
		return makeGridJsonData(dataList, dataList.size(), searchVO);
	}

	/**
	 * 설문대상자 삭제
	 * @param	SurvTgtUsrVO dataVO
	 * @param	Model model
	 * @return	ModelAndView
	 * @throws	Exception
	 */
	@RequestMapping("/system/system/survey/survTgtUsr/deleteSurvTgtUsr.do")
	public ModelAndView deleteSurvTgtUsr(@ModelAttribute("dataVO") SurvTgtUsrVO dataVO, Model model) throws Exception {
		return makeJsonDataByResultCnt(survTgtUsrService.deleteSurvTgtUsr(dataVO));
	}

	/**
	 * 설문대상자 저장
	 * @param	SurvTgtUsrVO dataVO
	 * @param	Model model
	 * @param	BindingResult bindingResult
	 * @return	ModelAndView
	 * @throws	Exception
	 */
	@RequestMapping("/system/system/survey/survTgtUsr/saveSurvTgtUsr.do")
	public ModelAndView saveSurvTgtUsr(@ModelAttribute("dataVO") SurvTgtUsrVO dataVO, Model model, BindingResult bindingResult) throws Exception {
		//spring validation check
		validateList(dataVO.getGridDataList(),bindingResult);
		if(bindingResult.hasErrors()){
			return makeFailJsonData(getListErrorMsg(bindingResult));
		}

		return makeJsonDataByResultCnt(survTgtUsrService.saveData(dataVO));
	}

	/**
	 * 전직원추가
	 */
	@RequestMapping("/system/system/survey/survTgtUsr/saveAllUser.do")
	public ModelAndView saveAllUser(@ModelAttribute("dataVO") SurvTgtUsrVO dataVO, Model model, BindingResult bindingResult) throws Exception {
		return makeJsonDataByResultCnt(survTgtUsrService.saveAllUser(dataVO));
	}

	/**
	 * 사용자 조회 팝업
	 */
	@RequestMapping("/system/system/survey/survTgtUsr/popSearchUserForSurveyList.do")
	public String popSearchUserList(@ModelAttribute("searchVO") UserVO searchVO, Model model) throws Exception {
		Boolean isAuthenticated = EgovUserDetailsHelper.isAuthenticated();
		if(isAuthenticated) {
			model.addAttribute("deptList", commonService.selectDeptList(searchVO));
			return "/system/system/survey/survTgtUsr/popSearchUserForSurveyList." + searchVO.getLayout();
		} else {
			return "forward:/error/accessDenied.do";
		}
	}
}

