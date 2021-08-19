/*************************************************************************
* CLASS 명	: StrategyController
* 작 업 자	: joey
* 작 업 일	: 2018. 1. 29.
* 기	능	:  전략목표 Controller
* ---------------------------- 변 경 이 력 --------------------------------
* 번호	작 업 자		작	업	일			변 경 내 용				비고
* ----	---------	----------------	---------------------	-----------
*	1	joey		2018. 1. 29.		최 초 작 업
**************************************************************************/
package kr.ispark.bsc.base.strategy.strategy.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import egovframework.let.utl.fcc.service.EgovStringUtil;
import kr.ispark.bsc.base.strategy.perspective.service.PerspectiveVO;
import kr.ispark.bsc.base.strategy.perspective.service.impl.PerspectiveServiceImpl;
import kr.ispark.bsc.base.strategy.strategy.service.StrategyVO;
import kr.ispark.bsc.base.strategy.strategy.service.impl.StrategyServiceImpl;
import kr.ispark.common.util.CodeUtil;
/*공통 컨트롤러*/
import kr.ispark.common.web.BaseController;

@Controller
public class StrategyController extends BaseController {
	@Autowired
	private StrategyServiceImpl strategyService;

	@Autowired
	private PerspectiveServiceImpl perspectiveService;

	/**
	 * 전략목표 목록 화면
	 * @param	StrategyVO searchVO
	 * @param	Model model
	 * @return	String
	 * @throws	Exception
	 */
	@RequestMapping(value="/bsc/base/strategy/strategy/strategyList.do")
	public String StrategyList(@ModelAttribute("searchVO") StrategyVO searchVO, Model model) throws Exception {

		List upStrategyList = strategyService.selectUpStrategyList(searchVO);
		List perspectiveList = strategyService.selectPerspectiveList(searchVO);

		model.addAttribute("upStrategyList", upStrategyList);
		model.addAttribute("perspectiveList", perspectiveList);

		return "/bsc/base/strategy/strategy/strategyList." + searchVO.getLayout();
	}

	/**
	 * 전략목표 그리드 조회(json)
	 * @param searchVO
	 * @return	ModelAndView
	 * @throws Exception
	 */
	@RequestMapping(value="/bsc/base/strategy/strategy/strategyList_json.do")
	public ModelAndView StrategyList_json(@ModelAttribute("searchVO") StrategyVO searchVO)throws Exception {

		List<StrategyVO> dataList = strategyService.selectList(searchVO);
		return makeGridJsonData(dataList, dataList.size(), searchVO);
	}

	/**
	 * 전략목표 그리드 조회(json)
	 * @param searchVO
	 * @return	ModelAndView
	 * @throws Exception
	 */
	@RequestMapping(value="/bsc/base/strategy/strategy/perspectiveData_json.do")
	public ModelAndView strategyData_json(@ModelAttribute("searchVO") StrategyVO searchVO)throws Exception {

		PerspectiveVO pvo = new PerspectiveVO();
		pvo.setFindYear(searchVO.getFindYear());
		pvo.setFindUseYn("Y");

		List<PerspectiveVO> dataList = perspectiveService.selectList(pvo);
		return makeJsonListData(dataList);
	}

	//excelDownload
	/**
	 * 엑셀양식다운로드
	 */
	@RequestMapping("/bsc/base/strategy/strategy/excelDownload.do")
	public String excelDownload(@ModelAttribute("searchVO") StrategyVO searchVO,
			Model model)throws Exception {

		List<StrategyVO> dataList = strategyService.selectList(searchVO);

		// 타이틀
		model.addAttribute("title", egovMessageSource.getMessage("word.strategy"));

		// 검색조건
		model.addAttribute("yearSearchNm", egovMessageSource.getMessage("word.year"));
		model.addAttribute("yearSearchValue", CodeUtil.getCodeName("017", searchVO.getFindYear()));
		model.addAttribute("useYnSearchNm", egovMessageSource.getMessage("word.useYn"));
		model.addAttribute("useYnSearchValue", CodeUtil.getCodeName("011", searchVO.getFindUseYn()));

		// header
		model.addAttribute("strategyNmHeader", egovMessageSource.getMessage("word.strategy"));	// 전략목표
		model.addAttribute("perspectiveNmHeader", egovMessageSource.getMessage("word.perspective"));	// 전략목표
		model.addAttribute("useYnHeader", egovMessageSource.getMessage("word.useYn"));			// 정렬순서
		model.addAttribute("sortOrderHeader", egovMessageSource.getMessage("word.sortOrder"));			// 정렬순서

		// 조직 데이터
		model.addAttribute("dataList", dataList);

		// 시트명
		model.addAttribute("sheetName", egovMessageSource.getMessage("word.strategy"));

		model.addAttribute("destJxlsFileName", egovMessageSource.getMessage("word.strategy") + "_" + EgovStringUtil.getTimeStamp()+".xlsx");
		model.addAttribute("templateJxlsFileName", "strategyList.xlsx");

		return "excelDownloadView";
	}

	/**
	 * 전략목표 삭제
	 * @param dataVO
	 * @param model
	 * @param bindingResult
	 * @return	ModelAndView
	 * @throws Exception
	 */
	@RequestMapping("/bsc/base/strategy/strategy/deleteStrategy.do")
	public ModelAndView deleteStrategy(@ModelAttribute("dataVO") StrategyVO dataVO, Model model) throws Exception {
		int resultCnt = strategyService.deleteStrategy(dataVO);
		if(resultCnt == 0) {
			return makeFailJsonData();
		}
		return makeSuccessJsonData();
	}

	/**
	 * 전략목표 저장
	 * @param dataVO
	 * @param model
	 * @param bindingResult
	 * @return	ModelAndView
	 * @throws Exception
	 */
	@RequestMapping("/bsc/base/strategy/strategy/saveStrategy.do")
	public ModelAndView saveStrategy(@ModelAttribute("dataVO") StrategyVO dataVO, Model model, BindingResult bindingResult) throws Exception {
		
		validateList(dataVO.getGridDataList(), bindingResult);
		if(bindingResult.hasErrors()){
			return makeFailJsonData(getListErrorMsg(bindingResult));
		}

		int resultCnt = strategyService.saveData(dataVO);
		if(resultCnt == 0) {
			return makeFailJsonData();
		}
		return makeSuccessJsonData();
		
	}
}
