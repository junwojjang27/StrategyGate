/*************************************************************************
* CLASS 명	: SurvAnsPoolController
* 작 업 자	: 최학룡
* 작 업 일	: 2018-10-02
* 기	능	: 설문답변pool Controller
* ---------------------------- 변 경 이 력 --------------------------------
* 번호	작 업 자		작	업	일			변 경 내 용				비고
* ----	---------	----------------	---------------------	-----------
*	1	최학룡		2018-10-02
**************************************************************************/
package kr.ispark.system.system.survey.survAnsPool.web;

import java.util.List;

import kr.ispark.common.util.CommonUtil;
/*공통 컨트롤러*/
import kr.ispark.common.web.BaseController;
import kr.ispark.system.system.survey.survAnsPool.service.SurvAnsPoolVO;
import kr.ispark.system.system.survey.survAnsPool.service.impl.SurvAnsPoolServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class SurvAnsPoolController extends BaseController {
	@Autowired
	private SurvAnsPoolServiceImpl survAnsPoolService;

	/**
	 * 설문답변pool 목록 화면
	 * @param	SurvAnsPoolVO searchVO
	 * @param	Model model
	 * @return	String
	 * @throws	Exception
	 */
	@RequestMapping("/system/system/survey/survAnsPool/survAnsPoolList.do")
	public String survAnsPoolList(@ModelAttribute("searchVO") SurvAnsPoolVO searchVO, Model model) throws Exception {
		return "/system/system/survey/survAnsPool/survAnsPoolList." + searchVO.getLayout();
	}

	/**
	 * 설문답변pool 그리드 조회(json)
	 * @param	SurvAnsPoolVO searchVO
	 * @return	ModelAndView
	 * @throws	Exception
	 */
	@RequestMapping("/system/system/survey/survAnsPool/survAnsPoolList_json.do")
	public ModelAndView survAnsPoolList_json(@ModelAttribute("searchVO") SurvAnsPoolVO searchVO) throws Exception {
		List<SurvAnsPoolVO> dataList = survAnsPoolService.selectList(searchVO);
		return makeGridJsonData(dataList, dataList.size(), searchVO);
	}

	/**
	 * 설문답변pool 조회
	 * @param	SurvAnsPoolVO searchVO
	 * @return	ModelAndView
	 * @throws	Exception
	 */
	@RequestMapping("/system/system/survey/survAnsPool/selectDetail.do")
	public ModelAndView selectDetail(@ModelAttribute("searchVO") SurvAnsPoolVO searchVO) throws Exception {
		return makeJsonData(survAnsPoolService.selectDetail(searchVO));
	}

	/**
	 * 설문답변pool상세 목록조회
	 */
	@RequestMapping("/system/system/survey/survAnsPool/selectItemList.do")
	public ModelAndView selectItemList(@ModelAttribute("searchVO") SurvAnsPoolVO searchVO) throws Exception {
		return makeJsonListData(survAnsPoolService.selectItemList(searchVO));
	}

	/**
	 * 설문답변pool 일괄저장
	 * @param	SurvAnsPoolVO dataVO
	 * @param	Model model
	 * @param	BindingResult bindingResult
	 * @return	ModelAndView
	 * @throws	Exception
	 */
	@RequestMapping("/system/system/survey/survAnsPool/saveAllSurvAnsPool.do")
	public ModelAndView saveAllSurvAnsPool(@ModelAttribute("dataVO") SurvAnsPoolVO dataVO, Model model, BindingResult bindingResult) throws Exception {
		// list 유효성 체크
		validateList(dataVO.getGridDataList(), bindingResult);
		if(bindingResult.hasErrors()) {
			return makeFailJsonData(getListErrorMsg(bindingResult));
		}

		return makeJsonDataByResultCnt(survAnsPoolService.saveAllSurvAnsPool(dataVO));
	}

	/**
	 * 설문답변pool 삭제
	 * @param	SurvAnsPoolVO dataVO
	 * @param	Model model
	 * @return	ModelAndView
	 * @throws	Exception
	 */
	@RequestMapping("/system/system/survey/survAnsPool/deleteSurvAnsPool.do")
	public ModelAndView deleteSurvAnsPool(@ModelAttribute("dataVO") SurvAnsPoolVO dataVO, Model model) throws Exception {
		return makeJsonDataByResultCnt(survAnsPoolService.deleteSurvAnsPool(dataVO));
	}

	/**
	 * 설문답변pool 저장
	 * @param	SurvAnsPoolVO dataVO
	 * @param	Model model
	 * @param	BindingResult bindingResult
	 * @return	ModelAndView
	 * @throws	Exception
	 */
	@RequestMapping("/system/system/survey/survAnsPool/saveSurvAnsPool.do")
	public ModelAndView saveSurvAnsPool(@ModelAttribute("dataVO") SurvAnsPoolVO dataVO, Model model, BindingResult bindingResult) throws Exception {
		beanValidator.validate(dataVO, bindingResult);
		if(bindingResult.hasErrors()){
			return makeFailJsonData(getListErrorMsg(bindingResult));
		}
		String rtnVal = survAnsPoolService.saveData(dataVO);
		if(CommonUtil.isEmpty(rtnVal)) {
			return makeFailJsonData();
		}
		return makeSuccessJsonDataDual(rtnVal);
	}

	/**
	 * 설문답변Pool 팝업
	 */
	@RequestMapping("/system/system/survey/survAnsPool/popSurvAnsPoolList.do")
	public String popSurvAnsPoolList(@ModelAttribute("searchVO") SurvAnsPoolVO searchVO, Model model) throws Exception {
		return "/system/system/survey/survAnsPool/popSurvAnsPoolList." + searchVO.getLayout();
	}

	/**
	 * 설문답변Pool 그리드 조회(json)
	 */
	@RequestMapping(value="/system/system/survey/survAnsPool/popSurvAnsPoolList_json.do")
	public ModelAndView popSurvAnsPoolList_json(@ModelAttribute("searchVO") SurvAnsPoolVO searchVO) throws Exception {
		List<SurvAnsPoolVO> dataList = survAnsPoolService.selectPopList(searchVO);
		return makeGridJsonData(dataList, dataList.size(), searchVO);
	}
	
	/**
	 * 답변항목대표여부 카운트
	 */
	@RequestMapping("/system/system/survey/survAnsPool/mainItemCnt.do")
	public ModelAndView mainItemCnt(@ModelAttribute("searchVO") SurvAnsPoolVO searchVO) throws Exception {
		return makeJsonData(survAnsPoolService.mainItemCnt(searchVO));
	}
}

