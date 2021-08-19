/*************************************************************************
* CLASS 명	: SurvActionController
* 작 업 자	: 최학룡
* 작 업 일	: 2018-10-16
* 기	능	: 설문실시 Controller
* ---------------------------- 변 경 이 력 --------------------------------
* 번호	작 업 자		작	업	일			변 경 내 용				비고
* ----	---------	----------------	---------------------	-----------
*	1	최학룡		2018-10-16
**************************************************************************/
package kr.ispark.system.system.survey.survAction.web;

import java.util.HashMap;
import java.util.List;

import kr.ispark.common.security.service.UserVO;
import kr.ispark.common.util.CommonUtil;
import kr.ispark.common.util.SessionUtil;
/*공통 컨트롤러*/
import kr.ispark.common.web.BaseController;
import kr.ispark.system.system.survey.survAction.service.SurvActionVO;
import kr.ispark.system.system.survey.survAction.service.impl.SurvActionServiceImpl;
import kr.ispark.system.system.survey.survQues.service.SurvQuesVO;
import kr.ispark.system.system.survey.survQues.service.impl.SurvQuesServiceImpl;
import kr.ispark.system.system.survey.survQuesGrp.service.SurvQuesGrpVO;
import kr.ispark.system.system.survey.survQuesGrp.service.impl.SurvQuesGrpServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class SurvActionController extends BaseController {
	@Autowired
	private SurvActionServiceImpl survActionService;

	@Autowired
	private SurvQuesServiceImpl survQuesService;

	@Autowired
	private SurvQuesGrpServiceImpl survQuesGrpService;

	/**
	 * 설문실시 목록 화면
	 * @param	SurvActionVO searchVO
	 * @param	Model model
	 * @return	String
	 * @throws	Exception
	 */
	@RequestMapping("/system/system/survey/survAction/survActionList.do")
	public String survActionList(@ModelAttribute("searchVO") SurvActionVO searchVO, Model model) throws Exception {
		UserVO uservo = (UserVO)SessionUtil.getAttribute("loginVO");
		searchVO.setUserNm(uservo.getUserNm());
		return "/system/system/survey/survAction/survActionList." + searchVO.getLayout();
	}

	/**
	 * 설문실시 그리드 조회(json)
	 * @param	SurvActionVO searchVO
	 * @return	ModelAndView
	 * @throws	Exception
	 */
	@RequestMapping("/system/system/survey/survAction/survActionList_json.do")
	public ModelAndView survActionList_json(@ModelAttribute("searchVO") SurvActionVO searchVO) throws Exception {
		List<SurvActionVO> dataList = survActionService.selectList(searchVO);
		return makeGridJsonData(dataList, dataList.size(), searchVO);
	}

	/**
	 * 설문실시 조회
	 * @param	SurvActionVO searchVO
	 * @return	ModelAndView
	 * @throws	Exception
	 */
	@RequestMapping("/system/system/survey/survAction/selectDetail.do")
	public ModelAndView selectDetail(@ModelAttribute("searchVO") SurvActionVO searchVO) throws Exception {
		HashMap<String, Object> resultMap = new HashMap<String, Object>();

		// 설문목록 조회
		UserVO uservo = (UserVO)SessionUtil.getAttribute("loginVO");
		searchVO.setSurveyUserId(uservo.getUserId());
		SurvActionVO surveyVO = survActionService.selectDetail(searchVO);
		resultMap.put("surveyVO", surveyVO);

		// 질문목록 조회
		SurvQuesVO quesVO = new SurvQuesVO();
		quesVO.setFindSurveyId(searchVO.getSurveyId());
		quesVO.setSelectQuesLinkYn("N");
		resultMap.put("quesList", survQuesService.selectList(quesVO));

		// 연계질문목록 조회
		quesVO.setSelectQuesLinkYn("Y");
		resultMap.put("linkList", survQuesService.selectList(quesVO));

		// 답변목록 조회
		quesVO.setSurveyId(searchVO.getSurveyId());
		resultMap.put("itemList", survQuesService.selectItemList(quesVO));

		// 질문그룹목록 조회
		SurvQuesGrpVO quesGrpVO = new SurvQuesGrpVO();
		quesGrpVO.setFindSurveyId(searchVO.getSurveyId());
		resultMap.put("grpList", survQuesGrpService.selectList(quesGrpVO));

		// 답변결과 조회
		resultMap.put("resultList", survActionService.selectResultList(searchVO));

		// 본부 목록 조회
		searchVO.setSurveyYear(surveyVO.getSurveyYear());
		resultMap.put("bonbuList", survActionService.selectBonbuList(searchVO));

		return new ModelAndView("jsonView", resultMap);
	}

	/**
	 * 설문실시 저장
	 * @param	SurvActionVO dataVO
	 * @param	Model model
	 * @param	BindingResult bindingResult
	 * @return	ModelAndView
	 * @throws	Exception
	 */
	@RequestMapping("/system/system/survey/survAction/saveSurvAction.do")
	public ModelAndView saveSurvAction(@ModelAttribute("dataVO") SurvActionVO dataVO, Model model, BindingResult bindingResult) throws Exception {
		String rtnVal = survActionService.saveData(dataVO);
		if(CommonUtil.isEmpty(rtnVal)) {
			return makeFailJsonData();
		}
		return makeSuccessJsonDataDual(rtnVal);
	}
}

