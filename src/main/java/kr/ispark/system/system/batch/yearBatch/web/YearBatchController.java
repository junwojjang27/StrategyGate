/*************************************************************************
* CLASS 명	: YearBatchController
* 작 업 자	: 현걸욱
* 작 업 일	: 2018-04-23
* 기	능	: 년배치 Controller
* ---------------------------- 변 경 이 력 --------------------------------
* 번호	작 업 자		작	업	일			변 경 내 용				비고
* ----	---------	----------------	---------------------	-----------
*	1	현걸욱		2018-04-23
**************************************************************************/
package kr.ispark.system.system.batch.yearBatch.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import egovframework.let.utl.fcc.service.EgovStringUtil;
import kr.ispark.system.system.batch.yearBatch.service.impl.YearBatchServiceImpl;
import kr.ispark.system.system.batch.yearBatch.service.YearBatchVO;
/*공통 컨트롤러*/
import kr.ispark.common.web.BaseController;

@Controller
public class YearBatchController extends BaseController {
	@Autowired
	private YearBatchServiceImpl yearBatchService;
	
	/**
	 * 년배치 목록 화면
	 * @param	YearBatchVO searchVO
	 * @param	Model model
	 * @return	String
	 * @throws	Exception
	 */
	@RequestMapping("/system/system/batch/yearBatch/yearBatchList.do")
	public String yearBatchList(@ModelAttribute("searchVO") YearBatchVO searchVO, Model model) throws Exception {
		return "/system/system/batch/yearBatch/yearBatchList." + searchVO.getLayout();
	}
	
	/**
	 * 년배치 그리드 조회(json)
	 * @param	YearBatchVO searchVO
	 * @return	ModelAndView
	 * @throws	Exception
	 */
	@RequestMapping("/system/system/batch/yearBatch/yearBatchList_json.do")
	public ModelAndView yearBatchList_json(@ModelAttribute("searchVO") YearBatchVO searchVO) throws Exception {
		List<YearBatchVO> dataList = yearBatchService.selectList(searchVO);
		return makeGridJsonData(dataList, dataList.size(), searchVO);
	}
	
	//excelDownload
	/**
	 * 엑셀양식다운로드
	 * @param	YearBatchVO searchVO
	 * @param	Model model
	 * @return	ModelAndView
	 * @throws	Exception
	 */
	@RequestMapping("/system/system/batch/yearBatch/excelDownload.do")
	public String excelDownload(@ModelAttribute("searchVO") YearBatchVO searchVO, Model model) throws Exception {
		
		/*
		 * 현재페이지 화면에 맞게 반드시 수정할 것.
		 */
		
		List<YearBatchVO> dataList = yearBatchService.selectList(searchVO);
		
		// 타이틀
		model.addAttribute("title", egovMessageSource.getMessage("word.yearBatchManage"));
		
		// 검색조건
		model.addAttribute("year", egovMessageSource.getMessage("word.year"));
		model.addAttribute("findYear", searchVO.getFindYear());
		
		// header
		model.addAttribute("yearBatchNm", egovMessageSource.getMessage("word.yearBatchNm"));	// 년배치
		model.addAttribute("sortOrder", egovMessageSource.getMessage("word.sortOrder"));			// 정렬순서
		
		// 조직 데이터
		model.addAttribute("dataList", dataList);
		
		// 시트명
		model.addAttribute("sheetName", egovMessageSource.getMessage("word.yearBatchNm"));
		
		model.addAttribute("destJxlsFileName", egovMessageSource.getMessage("word.yearBatchNm") + "_" + EgovStringUtil.getTimeStamp()+".xlsx");
		model.addAttribute("templateJxlsFileName", "yearBatchList.xlsx");

		return "excelDownloadView";
	}

	/**
	 * 년배치 조회
	 * @param	YearBatchVO searchVO
	 * @return	ModelAndView
	 * @throws	Exception
	 */
	@RequestMapping("/system/system/batch/yearBatch/selectDetail.do")
	public ModelAndView selectDetail(@ModelAttribute("searchVO") YearBatchVO searchVO) throws Exception {
		return makeJsonData(yearBatchService.selectDetail(searchVO));
	}
	
	/**
	 * 년배치 정렬순서저장
	 * @param	YearBatchVO dataVO
	 * @param	Model model
	 * @param	BindingResult bindingResult
	 * @return	ModelAndView
	 * @throws	Exception
	 */
	@RequestMapping("/system/system/batch/yearBatch/saveSortOrder.do")
	public ModelAndView saveSortOrder(@ModelAttribute("dataVO") YearBatchVO dataVO, Model model, BindingResult bindingResult) throws Exception {
		// list 유효성 체크
		validateList(dataVO.getGridDataList(), bindingResult);
		if(bindingResult.hasErrors()) {
			return makeFailJsonData(getListErrorMsg(bindingResult));
		}
		
		return makeJsonDataByResultCnt(yearBatchService.updateSortOrder(dataVO));
	}
	
	/**
	 * 년배치 삭제
	 * @param	YearBatchVO dataVO
	 * @param	Model model
	 * @return	ModelAndView
	 * @throws	Exception
	 */
	@RequestMapping("/system/system/batch/yearBatch/deleteYearBatch.do")
	public ModelAndView deleteYearBatch(@ModelAttribute("dataVO") YearBatchVO dataVO, Model model) throws Exception {
		return makeJsonDataByResultCnt(yearBatchService.deleteYearBatch(dataVO));
	}
	
	/**
	 * 년배치 저장
	 * @param	YearBatchVO dataVO
	 * @param	Model model
	 * @param	BindingResult bindingResult
	 * @return	ModelAndView
	 * @throws	Exception
	 */
	@RequestMapping("/system/system/batch/yearBatch/execYearBatch.do")
	public ModelAndView saveYearBatch(@ModelAttribute("dataVO") YearBatchVO dataVO, Model model, BindingResult bindingResult) throws Exception {
		beanValidator.validate(dataVO, bindingResult);
		
		int resultCnt = yearBatchService.updateYearBatch(dataVO);
		if(resultCnt == 0) {
			return makeFailJsonData();
		}
		return makeSuccessJsonData();
		
	}
}

