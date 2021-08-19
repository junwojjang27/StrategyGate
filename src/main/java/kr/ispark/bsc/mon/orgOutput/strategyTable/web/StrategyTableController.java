/*************************************************************************
* CLASS 명	: StrategyTableController
* 작 업 자	: 현걸욱
* 작 업 일	: 2018-05-03
* 기	능	: 전략연계표 Controller
* ---------------------------- 변 경 이 력 --------------------------------
* 번호	작 업 자		작	업	일			변 경 내 용				비고
* ----	---------	----------------	---------------------	-----------
*	1	현걸욱		2018-05-03
**************************************************************************/
package kr.ispark.bsc.mon.orgOutput.strategyTable.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import egovframework.let.utl.fcc.service.EgovStringUtil;
import kr.ispark.bsc.mon.orgOutput.strategyTable.service.impl.StrategyTableServiceImpl;
import kr.ispark.bsc.mon.orgOutput.strategyTable.service.StrategyTableVO;
/*공통 컨트롤러*/
import kr.ispark.common.web.BaseController;

@Controller
public class StrategyTableController extends BaseController {
	@Autowired
	private StrategyTableServiceImpl strategyTableService;
	
	/**
	 * 전략연계표 목록 화면
	 * @param	StrategyTableVO searchVO
	 * @param	Model model
	 * @return	String
	 * @throws	Exception
	 */
	@RequestMapping("/bsc/mon/orgOutput/strategyTable/strategyTableList.do")
	public String strategyTableList(@ModelAttribute("searchVO") StrategyTableVO searchVO, Model model) throws Exception {
		
		List<StrategyTableVO> strategyList = strategyTableService.selectStrategyList(searchVO);
		model.addAttribute("strategyList", strategyList);
		
		return "/bsc/mon/orgOutput/strategyTable/strategyTableList." + searchVO.getLayout();
	}
	
	/**
	 * 전략연계표 그리드 조회(json)
	 * @param	StrategyTableVO searchVO
	 * @return	ModelAndView
	 * @throws	Exception
	 */
	@RequestMapping("/bsc/mon/orgOutput/strategyTable/strategyTableList_json.do")
	public ModelAndView strategyTableList_json(@ModelAttribute("searchVO") StrategyTableVO searchVO) throws Exception {
		List<StrategyTableVO> dataList = strategyTableService.selectList(searchVO);
		return makeGridJsonData(dataList, dataList.size(), searchVO);
	}
	
	/**
	 * 전략연계표 그리드 조회(json)
	 * @param	StrategyTableVO searchVO
	 * @return	ModelAndView
	 * @throws	Exception
	 */
	@RequestMapping("/bsc/mon/orgOutput/strategyTable/strategyList_json.do")
	public ModelAndView strategyList_json(@ModelAttribute("searchVO") StrategyTableVO searchVO) throws Exception {
		List<StrategyTableVO> dataList = strategyTableService.selectStrategyList(searchVO);
		return makeJsonListData(dataList);
	}
	
	//excelDownload
	/**
	 * 엑셀양식다운로드
	 * @param	StrategyTableVO searchVO
	 * @param	Model model
	 * @return	ModelAndView
	 * @throws	Exception
	 */
	@RequestMapping("/bsc/mon/orgOutput/strategyTable/excelDownload.do")
	public String excelDownload(@ModelAttribute("searchVO") StrategyTableVO searchVO, Model model) throws Exception {
		
		/*
		 * 현재페이지 화면에 맞게 반드시 수정할 것.
		 */
		
		List<StrategyTableVO> dataList = strategyTableService.selectList(searchVO);
		
		// 타이틀
		model.addAttribute("title", egovMessageSource.getMessage("word.strategyTableManage"));
		
		// 검색조건
		model.addAttribute("year", egovMessageSource.getMessage("word.year"));
		model.addAttribute("findYear", searchVO.getFindYear());
		
		// header
		model.addAttribute("strategyTableNm", egovMessageSource.getMessage("word.strategyTableNm"));	// 전략연계표
		model.addAttribute("sortOrder", egovMessageSource.getMessage("word.sortOrder"));			// 정렬순서
		
		// 조직 데이터
		model.addAttribute("dataList", dataList);
		
		// 시트명
		model.addAttribute("sheetName", egovMessageSource.getMessage("word.strategyTableNm"));
		
		model.addAttribute("destJxlsFileName", egovMessageSource.getMessage("word.strategyTableNm") + "_" + EgovStringUtil.getTimeStamp()+".xlsx");
		model.addAttribute("templateJxlsFileName", "strategyTableList.xlsx");

		return "excelDownloadView";
	}

	/**
	 * 전략연계표 조회
	 * @param	StrategyTableVO searchVO
	 * @return	ModelAndView
	 * @throws	Exception
	 */
	@RequestMapping("/bsc/mon/orgOutput/strategyTable/selectDetail.do")
	public ModelAndView selectDetail(@ModelAttribute("searchVO") StrategyTableVO searchVO) throws Exception {
		return makeJsonData(strategyTableService.selectDetail(searchVO));
	}
	
}
