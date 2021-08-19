/*************************************************************************
* CLASS 명	: SurvQuesGrpController
* 작 업 자	: 최학룡
* 작 업 일	: 2018-10-11
* 기	능	: 설문질문그룹 Controller
* ---------------------------- 변 경 이 력 --------------------------------
* 번호	작 업 자		작	업	일			변 경 내 용				비고
* ----	---------	----------------	---------------------	-----------
*	1	최학룡		2018-10-11
**************************************************************************/
package kr.ispark.system.system.survey.survQuesGrp.web;

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
public class SurvQuesGrpController extends BaseController {
	@Autowired
	private SurvQuesGrpServiceImpl survQuesGrpService;

	@Autowired
	private SurvRegServiceImpl survRegService;

	@Autowired
	private SurvActionServiceImpl survActionService;

	@Autowired
	private SurvQuesServiceImpl survQuesService;

	/**
	 * 설문질문그룹 목록 화면
	 * @param	SurvQuesGrpVO searchVO
	 * @param	Model model
	 * @return	String
	 * @throws	Exception
	 */
	@RequestMapping("/system/system/survey/survQuesGrp/survQuesGrpList.do")
	public String survQuesGrpList(@ModelAttribute("searchVO") SurvQuesGrpVO searchVO, Model model) throws Exception {
		SurvRegVO vo = new SurvRegVO();
		vo.setFindUseYn("Y");
		List<SurvRegVO> surveyList = survRegService.selectList(vo);
		model.addAttribute("surveyList", surveyList);

		if(CommonUtil.isEmpty(searchVO.getFindSurveyId())) {
			if(surveyList.size() > 0) {
				searchVO.setFindSurveyId(surveyList.get(0).getSurveyId());
			}
		}
		List<SurvQuesGrpVO> quesGrpList = survQuesGrpService.selectList(searchVO);
		model.addAttribute("quesGrpList", quesGrpList);
		return "/system/system/survey/survQuesGrp/survQuesGrpList." + searchVO.getLayout();
	}

	/**
	 * 설문질문그룹 그리드 조회(json)
	 * @param	SurvQuesGrpVO searchVO
	 * @return	ModelAndView
	 * @throws	Exception
	 */
	@RequestMapping("/system/system/survey/survQuesGrp/survQuesGrpList_json.do")
	public ModelAndView survQuesGrpList_json(@ModelAttribute("searchVO") SurvQuesGrpVO searchVO) throws Exception {
		List<SurvQuesGrpVO> dataList = survQuesGrpService.selectList(searchVO);
		return makeGridJsonData(dataList, dataList.size(), searchVO);
	}

	/**
	 * 설문질문그룹별 설문매핑 그리드 조회(json)
	 */
	@RequestMapping("/system/system/survey/survQuesGrp/survQuesGrpForMapList_json.do")
	public ModelAndView survQuesGrpForMapList_json(@ModelAttribute("searchVO") SurvQuesGrpVO searchVO) throws Exception {
		List<SurvQuesGrpVO> dataList = survQuesGrpService.selectListForMap(searchVO);
		return makeGridJsonData(dataList, dataList.size(), searchVO);
	}

	/**
	 * 설문질문그룹 조회(json)
	 */
	@RequestMapping("/system/system/survey/survQuesGrp/selectQuesGrpList.do")
	public ModelAndView selectQuesGrpList(@ModelAttribute("searchVO") SurvQuesGrpVO searchVO) throws Exception {
		List<SurvQuesGrpVO> dataList = survQuesGrpService.selectList(searchVO);
		return makeJsonListData(dataList);
	}

	/**
	 * 설문질문그룹 삭제
	 * @param	SurvQuesGrpVO dataVO
	 * @param	Model model
	 * @return	ModelAndView
	 * @throws	Exception
	 */
	@RequestMapping("/system/system/survey/survQuesGrp/deleteSurvQuesGrp.do")
	public ModelAndView deleteSurvQuesGrp(@ModelAttribute("dataVO") SurvQuesGrpVO dataVO, Model model) throws Exception {
		return makeJsonDataByResultCnt(survQuesGrpService.deleteSurvQuesGrp(dataVO));
	}

	/**
	 * 설문질문그룹 저장
	 * @param	SurvQuesGrpVO dataVO
	 * @param	Model model
	 * @param	BindingResult bindingResult
	 * @return	ModelAndView
	 * @throws	Exception
	 */
	@RequestMapping("/system/system/survey/survQuesGrp/saveSurvQuesGrp.do")
	public ModelAndView saveSurvQuesGrp(@ModelAttribute("dataVO") SurvQuesGrpVO dataVO, Model model, BindingResult bindingResult) throws Exception {
		//spring validation check
		validateList(dataVO.getGridDataList(),bindingResult);
		if(bindingResult.hasErrors()){
			return makeFailJsonData(getListErrorMsg(bindingResult));
		}

		return makeJsonDataByResultCnt(survQuesGrpService.saveData(dataVO));
	}

	/**
	 * 설문질문그룹별 설문매핑 저장
	 */
	@RequestMapping("/system/system/survey/survQuesGrp/saveSurvQuesGrpForMap.do")
	public ModelAndView saveSurvQuesGrpForMap(@ModelAttribute("dataVO") SurvQuesGrpVO dataVO, Model model, BindingResult bindingResult) throws Exception {
		return makeJsonDataByResultCnt(survQuesGrpService.saveDataForMap(dataVO));
	}

	/**
	 * 미리보기 팝업
	 */
	@RequestMapping("/system/system/survey/survQuesGrp/popSurvActionList.do")
	public String popSurvActionList(@ModelAttribute("searchVO") SurvQuesGrpVO searchVO, Model model) throws Exception {
		return "/system/system/survey/survQuesGrp/popSurvActionList." + searchVO.getLayout();
	}

	/**
	 * 미리보기 팝업 데이터 조회
	 */
	@RequestMapping("/system/system/survey/survQuesGrp/popSelectDetail.do")
	public ModelAndView popSelectDetail(@ModelAttribute("searchVO") SurvQuesGrpVO searchVO) throws Exception {
		HashMap<String, Object> resultMap = new HashMap<String, Object>();

		// 설문목록 조회
		UserVO uservo = (UserVO)SessionUtil.getAttribute("loginVO");
		SurvActionVO actionVO = new SurvActionVO();
		actionVO.setSurveyId(searchVO.getSurveyId());
		actionVO.setSurveyUserId(uservo.getUserId());
		SurvActionVO surveyVO = survActionService.selectDetail(actionVO);
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

		return new ModelAndView("jsonView", resultMap);
	}
}

