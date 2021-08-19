/*************************************************************************
* CLASS 명	: SurvQuesController
* 작 업 자	: 최학룡
* 작 업 일	: 2018-10-10
* 기	능	: 설문질문등록 Controller
* ---------------------------- 변 경 이 력 --------------------------------
* 번호	작 업 자		작	업	일			변 경 내 용				비고
* ----	---------	----------------	---------------------	-----------
*	1	최학룡		2018-10-10
**************************************************************************/
package kr.ispark.system.system.survey.survQues.web;

import java.util.List;


import kr.ispark.common.util.CommonUtil;
/*공통 컨트롤러*/
import kr.ispark.common.web.BaseController;
import kr.ispark.system.system.survey.survQues.service.SurvQuesVO;
import kr.ispark.system.system.survey.survQues.service.impl.SurvQuesServiceImpl;
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
public class SurvQuesController extends BaseController {
	@Autowired
	private SurvQuesServiceImpl survQuesService;

	@Autowired
	private SurvRegServiceImpl survRegService;

	/**
	 * 설문질문등록 목록 화면
	 * @param	SurvQuesVO searchVO
	 * @param	Model model
	 * @return	String
	 * @throws	Exception
	 */
	@RequestMapping("/system/system/survey/survQues/survQuesList.do")
	public String survQuesList(@ModelAttribute("searchVO") SurvQuesVO searchVO, Model model) throws Exception {
		SurvRegVO vo = new SurvRegVO();
		vo.setFindUseYn("Y");
		List<SurvRegVO> surveyList = survRegService.selectList(vo);
		model.addAttribute("surveyList", surveyList);
		return "/system/system/survey/survQues/survQuesList." + searchVO.getLayout();
	}

	/**
	 * 설문질문등록 그리드 조회(json)
	 * @param	SurvQuesVO searchVO
	 * @return	ModelAndView
	 * @throws	Exception
	 */
	@RequestMapping("/system/system/survey/survQues/survQuesList_json.do")
	public ModelAndView survQuesList_json(@ModelAttribute("searchVO") SurvQuesVO searchVO) throws Exception {
		List<SurvQuesVO> dataList = survQuesService.selectList(searchVO);
		return makeGridJsonData(dataList, dataList.size(), searchVO);
	}

	/**
	 * 설문질문등록 조회
	 * @param	SurvQuesVO searchVO
	 * @return	ModelAndView
	 * @throws	Exception
	 */
	@RequestMapping("/system/system/survey/survQues/selectDetail.do")
	public ModelAndView selectDetail(@ModelAttribute("searchVO") SurvQuesVO searchVO) throws Exception {
		return makeJsonData(survQuesService.selectDetail(searchVO));
	}

	/**
	 * 설문질문답변 목록조회
	 */
	@RequestMapping("/system/system/survey/survQues/selectItemList.do")
	public ModelAndView selectItemList(@ModelAttribute("searchVO") SurvQuesVO searchVO) throws Exception {
		return makeJsonListData(survQuesService.selectItemList(searchVO));
	}

	/**
	 * 연계질문 조회(json)
	 */
	@RequestMapping("/system/system/survey/survQues/selectLinkQuesList_json.do")
	public ModelAndView selectLinkQuesList_json(@ModelAttribute("searchVO") SurvQuesVO searchVO) throws Exception {
		searchVO.setSelectQuesLinkYn("Y");
		List<SurvQuesVO> dataList = survQuesService.selectList(searchVO);
		return makeJsonListData(dataList);
	}

	/**
	 * 설문질문등록 삭제
	 * @param	SurvQuesVO dataVO
	 * @param	Model model
	 * @return	ModelAndView
	 * @throws	Exception
	 */
	@RequestMapping("/system/system/survey/survQues/deleteSurvQues.do")
	public ModelAndView deleteSurvQues(@ModelAttribute("dataVO") SurvQuesVO dataVO, Model model) throws Exception {
		return makeJsonDataByResultCnt(survQuesService.deleteSurvQues(dataVO));
	}

	/**
	 * 설문질문등록 저장
	 * @param	SurvQuesVO dataVO
	 * @param	Model model
	 * @param	BindingResult bindingResult
	 * @return	ModelAndView
	 * @throws	Exception
	 */
	@RequestMapping("/system/system/survey/survQues/saveSurvQues.do")
	public ModelAndView saveSurvQues(@ModelAttribute("dataVO") SurvQuesVO dataVO, Model model, BindingResult bindingResult) throws Exception {
		beanValidator.validate(dataVO, bindingResult);
		if(bindingResult.hasErrors()){
			return makeFailJsonData(getListErrorMsg(bindingResult));
		}
		String rtnVal = survQuesService.saveData(dataVO);
		if(CommonUtil.isEmpty(rtnVal)) {
			return makeFailJsonData();
		}
		return makeSuccessJsonDataDual(rtnVal);
	}
}

