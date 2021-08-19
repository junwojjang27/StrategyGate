/*************************************************************************
* CLASS 명	: SurvResultController
* 작 업 자	: 최학룡
* 작 업 일	: 2018-10-22
* 기	능	: 설문결과 Controller
* ---------------------------- 변 경 이 력 --------------------------------
* 번호	작 업 자		작	업	일			변 경 내 용				비고
* ----	---------	----------------	---------------------	-----------
*	1	최학룡		2018-10-22
**************************************************************************/
package kr.ispark.system.system.survey.survResult.web;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import kr.ispark.common.util.CommonUtil;
/*공통 컨트롤러*/
import kr.ispark.common.web.BaseController;
import kr.ispark.system.system.survey.survReg.service.SurvRegVO;
import kr.ispark.system.system.survey.survReg.service.impl.SurvRegServiceImpl;
import kr.ispark.system.system.survey.survResult.service.SurvResultVO;
import kr.ispark.system.system.survey.survResult.service.impl.SurvResultServiceImpl;

@Controller
public class SurvResultController extends BaseController {
	@Autowired
	private SurvResultServiceImpl survResultService;

	@Autowired
	private SurvRegServiceImpl survRegService;

	/**
	 * 설문결과 목록 화면
	 * @param	SurvResultVO searchVO
	 * @param	Model model
	 * @return	String
	 * @throws	Exception
	 */
	@RequestMapping("/system/system/survey/survResult/survResultList.do")
	public String survResultList(@ModelAttribute("searchVO") SurvResultVO searchVO, Model model) throws Exception {
		SurvRegVO vo = new SurvRegVO();
		vo.setFindUseYn("Y");
		List<SurvRegVO> surveyList = survRegService.selectList(vo);
		model.addAttribute("surveyList", surveyList);

		if(CommonUtil.isEmpty(searchVO.getSurveyYear())) {
			if(surveyList.size() > 0) {
				searchVO.setSurveyYear(surveyList.get(0).getSurveyYear());
			}
		}
		if(CommonUtil.isEmpty(searchVO.getFindSurveyId())) {
			if(surveyList.size() > 0) {
				searchVO.setSurveyId(surveyList.get(0).getSurveyId());
			}
		}else{
			searchVO.setSurveyId(searchVO.getFindSurveyId());
		}
		searchVO.setSelectQuesLinkYn("N");
		model.addAttribute("quesList", survResultService.selectQuesList(searchVO));
		model.addAttribute("itemList", survResultService.selectItemList(searchVO));
		searchVO.setSelectQuesLinkYn("Y");
		model.addAttribute("linkList", survResultService.selectQuesList(searchVO));
		return "/system/system/survey/survResult/survResultList." + searchVO.getLayout();
	}

	/**
	 * 설문결과 조회
	 * @param	SurvResultVO searchVO
	 * @return	ModelAndView
	 * @throws	Exception
	 */
	@RequestMapping("/system/system/survey/survResult/selectDetail.do")
	public ModelAndView selectDetail(@ModelAttribute("searchVO") SurvResultVO searchVO) throws Exception {
		HashMap<String, Object> resultMap = new HashMap<String, Object>();

		SurvResultVO selectDetail = survResultService.selectDetail(searchVO);
		resultMap.put("selectDetail", selectDetail);

		// 질문목록 조회
		searchVO.setSelectQuesLinkYn("N");
		resultMap.put("quesList", survResultService.selectQuesList(searchVO));

		// 답변목록 조회
		resultMap.put("itemList", survResultService.selectItemList(searchVO));

		// 연계질문목록 조회
		searchVO.setSelectQuesLinkYn("Y");
		resultMap.put("linkList", survResultService.selectQuesList(searchVO));

		return new ModelAndView("jsonView", resultMap);
	}

	/**
	 * 차트 데이터 목록 조회
	 */
	@RequestMapping("/system/system/survey/survResult/selectChartData.do")
	public ModelAndView selectChartData(@ModelAttribute("searchVO") SurvResultVO searchVO) throws Exception {
		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("list", survResultService.selectChartData(searchVO));
		resultMap.put("idx", searchVO.getIdx());
		resultMap.put("subIdx", searchVO.getSubIdx());
		return new ModelAndView("jsonView", resultMap);
	}

	/**
	 * 답변보기 팝업
	 */
	@RequestMapping("/system/system/survey/survResult/popEssayQuesList.do")
	public String popEssayQues(@ModelAttribute("searchVO") SurvResultVO searchVO) throws Exception {
		return "/system/system/survey/survResult/popEssayQuesList." + searchVO.getLayout();
	}

	/**
	 * 답변보기 팝업 그리드 조회(json)
	 */
	@RequestMapping("/system/system/survey/survResult/essayQuesList_json.do")
	public ModelAndView popSignalMngList_json(@ModelAttribute("searchVO") SurvResultVO searchVO)throws Exception {
		List<SurvResultVO> dataList = survResultService.selectEssayList(searchVO);
		return makeGridJsonData(dataList, dataList.size(), searchVO);
	}
}

