/*************************************************************************
* CLASS 명	: SurvRegController
* 작 업 자	: 최학룡
* 작 업 일	: 2018-10-05
* 기	능	: 설문등록 Controller
* ---------------------------- 변 경 이 력 --------------------------------
* 번호	작 업 자		작	업	일			변 경 내 용				비고
* ----	---------	----------------	---------------------	-----------
*	1	최학룡		2018-10-05
**************************************************************************/
package kr.ispark.system.system.survey.survReg.web;

import java.util.List;

import kr.ispark.common.util.CommonUtil;
/*공통 컨트롤러*/
import kr.ispark.common.web.BaseController;
import kr.ispark.system.system.survey.survReg.service.SurvRegVO;
import kr.ispark.system.system.survey.survReg.service.impl.SurvRegServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class SurvRegController extends BaseController {
	@Autowired
	private SurvRegServiceImpl survRegService;

	/**
	 * 설문등록 목록 화면
	 * @param	SurvRegVO searchVO
	 * @param	Model model
	 * @return	String
	 * @throws	Exception
	 */
	@RequestMapping("/system/system/survey/survReg/survRegList.do")
	public String survRegList(@ModelAttribute("searchVO") SurvRegVO searchVO, Model model) throws Exception {
		return "/system/system/survey/survReg/survRegList." + searchVO.getLayout();
	}

	/**
	 * 설문등록 그리드 조회(json)
	 * @param	SurvRegVO searchVO
	 * @return	ModelAndView
	 * @throws	Exception
	 */
	@RequestMapping("/system/system/survey/survReg/survRegList_json.do")
	public ModelAndView survRegList_json(@ModelAttribute("searchVO") SurvRegVO searchVO) throws Exception {
		List<SurvRegVO> dataList = survRegService.selectList(searchVO);
		return makeGridJsonData(dataList, dataList.size(), searchVO);
	}

	/**
	 * 설문등록 조회
	 * @param	SurvRegVO searchVO
	 * @return	ModelAndView
	 * @throws	Exception
	 */
	@RequestMapping("/system/system/survey/survReg/selectDetail.do")
	public ModelAndView selectDetail(@ModelAttribute("searchVO") SurvRegVO searchVO) throws Exception {
		return makeJsonData(survRegService.selectDetail(searchVO));
	}

	/**
	 * 설문등록 설문복사
	 * @param	SurvRegVO dataVO
	 * @param	Model model
	 * @param	BindingResult bindingResult
	 * @return	ModelAndView
	 * @throws	Exception
	 */
	@RequestMapping("/system/system/survey/survReg/copySurvReg.do")
	public ModelAndView copySurvReg(@ModelAttribute("dataVO") SurvRegVO dataVO, Model model, BindingResult bindingResult) throws Exception {
		return makeJsonDataByResultCnt(survRegService.insertSurveyCopy(dataVO));
	}

	/**
	 * 설문등록 삭제
	 * @param	SurvRegVO dataVO
	 * @param	Model model
	 * @return	ModelAndView
	 * @throws	Exception
	 */
	@RequestMapping("/system/system/survey/survReg/deleteSurvReg.do")
	public ModelAndView deleteSurvReg(@ModelAttribute("dataVO") SurvRegVO dataVO, Model model) throws Exception {
		return makeJsonDataByResultCnt(survRegService.deleteSurvReg(dataVO));
	}

	/**
	 * 설문등록 저장
	 * @param	SurvRegVO dataVO
	 * @param	Model model
	 * @param	BindingResult bindingResult
	 * @return	ModelAndView
	 * @throws	Exception
	 */
	@RequestMapping("/system/system/survey/survReg/saveSurvReg.do")
	public ModelAndView saveSurvReg(@ModelAttribute("dataVO") SurvRegVO dataVO, Model model, BindingResult bindingResult) throws Exception {
		beanValidator.validate(dataVO, bindingResult);
		if(bindingResult.hasErrors()){
			return makeFailJsonData(getListErrorMsg(bindingResult));
		}
		String rtnVal = survRegService.saveData(dataVO);
		if(CommonUtil.isEmpty(rtnVal)) {
			return makeFailJsonData();
		}
		return makeSuccessJsonDataDual(rtnVal);
	}
}

