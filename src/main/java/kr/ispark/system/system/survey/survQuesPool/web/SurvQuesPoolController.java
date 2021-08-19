/*************************************************************************
* CLASS 명	: SurvQuesPoolController
* 작 업 자	: 최학룡
* 작 업 일	: 2018-10-04
* 기	능	: 설문질문pool Controller
* ---------------------------- 변 경 이 력 --------------------------------
* 번호	작 업 자		작	업	일			변 경 내 용				비고
* ----	---------	----------------	---------------------	-----------
*	1	최학룡		2018-10-04
**************************************************************************/
package kr.ispark.system.system.survey.survQuesPool.web;

import java.util.List;

import kr.ispark.common.util.CommonUtil;
/*공통 컨트롤러*/
import kr.ispark.common.web.BaseController;
import kr.ispark.system.system.survey.survQuesPool.service.SurvQuesPoolVO;
import kr.ispark.system.system.survey.survQuesPool.service.impl.SurvQuesPoolServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class SurvQuesPoolController extends BaseController {
	@Autowired
	private SurvQuesPoolServiceImpl survQuesPoolService;

	/**
	 * 설문질문pool 목록 화면
	 * @param	SurvQuesPoolVO searchVO
	 * @param	Model model
	 * @return	String
	 * @throws	Exception
	 */
	@RequestMapping("/system/system/survey/survQuesPool/survQuesPoolList.do")
	public String survQuesPoolList(@ModelAttribute("searchVO") SurvQuesPoolVO searchVO, Model model) throws Exception {
		return "/system/system/survey/survQuesPool/survQuesPoolList." + searchVO.getLayout();
	}

	/**
	 * 설문질문pool 그리드 조회(json)
	 * @param	SurvQuesPoolVO searchVO
	 * @return	ModelAndView
	 * @throws	Exception
	 */
	@RequestMapping("/system/system/survey/survQuesPool/survQuesPoolList_json.do")
	public ModelAndView survQuesPoolList_json(@ModelAttribute("searchVO") SurvQuesPoolVO searchVO) throws Exception {
		List<SurvQuesPoolVO> dataList = survQuesPoolService.selectList(searchVO);
		return makeGridJsonData(dataList, dataList.size(), searchVO);
	}

	/**
	 * 설문질문pool 조회
	 * @param	SurvQuesPoolVO searchVO
	 * @return	ModelAndView
	 * @throws	Exception
	 */
	@RequestMapping("/system/system/survey/survQuesPool/selectDetail.do")
	public ModelAndView selectDetail(@ModelAttribute("searchVO") SurvQuesPoolVO searchVO) throws Exception {
		return makeJsonData(survQuesPoolService.selectDetail(searchVO));
	}

	/**
	 * 설문질문pool답변항목 목록조회
	 */
	@RequestMapping("/system/system/survey/survQuesPool/selectItemList.do")
	public ModelAndView selectItemList(@ModelAttribute("searchVO") SurvQuesPoolVO searchVO) throws Exception {
		return makeJsonListData(survQuesPoolService.selectItemList(searchVO));
	}

	/**
	 * 설문질문pool 삭제
	 * @param	SurvQuesPoolVO dataVO
	 * @param	Model model
	 * @return	ModelAndView
	 * @throws	Exception
	 */
	@RequestMapping("/system/system/survey/survQuesPool/deleteSurvQuesPool.do")
	public ModelAndView deleteSurvQuesPool(@ModelAttribute("dataVO") SurvQuesPoolVO dataVO, Model model) throws Exception {
		return makeJsonDataByResultCnt(survQuesPoolService.deleteSurvQuesPool(dataVO));
	}

	/**
	 * 설문질문pool 저장
	 * @param	SurvQuesPoolVO dataVO
	 * @param	Model model
	 * @param	BindingResult bindingResult
	 * @return	ModelAndView
	 * @throws	Exception
	 */
	@RequestMapping("/system/system/survey/survQuesPool/saveSurvQuesPool.do")
	public ModelAndView saveSurvQuesPool(@ModelAttribute("dataVO") SurvQuesPoolVO dataVO, Model model, BindingResult bindingResult) throws Exception {
		beanValidator.validate(dataVO, bindingResult);
		if(bindingResult.hasErrors()){
			return makeFailJsonData(getListErrorMsg(bindingResult));
		}
		String rtnVal = survQuesPoolService.saveData(dataVO);
		if(CommonUtil.isEmpty(rtnVal)) {
			return makeFailJsonData();
		}
		return makeSuccessJsonDataDual(rtnVal);
	}

	/**
	 * 설문질문Pool 팝업
	 */
	@RequestMapping("/system/system/survey/survQuesPool/popSurvQuesPoolList.do")
	public String popSurvQuesPoolList(@ModelAttribute("searchVO") SurvQuesPoolVO searchVO, Model model) throws Exception {
		return "/system/system/survey/survQuesPool/popSurvQuesPoolList." + searchVO.getLayout();
	}

	/**
	 * 설문질문Pool 그리드 조회(json)
	 */
	@RequestMapping(value="/system/system/survey/survQuesPool/popSurvQuesPoolList_json.do")
	public ModelAndView popSurvQuesPoolList_json(@ModelAttribute("searchVO") SurvQuesPoolVO searchVO) throws Exception {
		searchVO.setFindUseYn("Y");
		List<SurvQuesPoolVO> dataList = survQuesPoolService.selectList(searchVO);
		return makeGridJsonData(dataList, dataList.size(), searchVO);
	}
}

