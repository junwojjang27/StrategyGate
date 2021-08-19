/*************************************************************************
* CLASS 명	: CalTypeMngController
* 작 업 자	: 현걸욱
* 작 업 일	: 2018-2-6
* 기	능	: 산식관리 Controller
* ---------------------------- 변 경 이 력 --------------------------------
* 번호	작 업 자		작	업	일			변 경 내 용				비고
* ----	---------	----------------	---------------------	-----------
*	1	현걸욱		2018-2-6
**************************************************************************/
package kr.ispark.system.system.code.calTypeMng.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import egovframework.let.utl.fcc.service.EgovStringUtil;
import kr.ispark.system.system.code.calTypeMng.service.impl.CalTypeMngServiceImpl;
import kr.ispark.system.system.code.calTypeMng.service.CalTypeMngVO;
import kr.ispark.common.util.CodeUtil;
/*공통 컨트롤러*/
import kr.ispark.common.web.BaseController;

@Controller
public class CalTypeMngController extends BaseController {
	@Autowired
	private CalTypeMngServiceImpl calTypeMngService;
	
	/**
	 * 산식관리 목록 화면
	 * @param	CalTypeMngVO searchVO
	 * @param	Model model
	 * @return	String
	 * @throws	Exception
	 */
	@RequestMapping(value="/system/system/code/calTypeMng/calTypeMngList.do")
	public String calTypeMngList(@ModelAttribute("searchVO") CalTypeMngVO searchVO, Model model) throws Exception {
		return "/system/system/code/calTypeMng/calTypeMngList." + searchVO.getLayout();
	}
	
	/**
	 * 산식관리 그리드 조회(json)
	 * @param searchVO
	 * @return	ModelAndView
	 * @throws Exception
	 */
	@RequestMapping(value="/system/system/code/calTypeMng/calTypeMngList_json.do")
	public ModelAndView calTypeMngList_json(@ModelAttribute("searchVO") CalTypeMngVO searchVO)throws Exception {
		
		List<CalTypeMngVO> dataList = calTypeMngService.selectList(searchVO);
		return makeGridJsonData(dataList, dataList.size(), searchVO);
	}
	
	//excelDownload
	/**
	 * 엑셀양식다운로드
	 */
	@RequestMapping("/system/system/code/calTypeMng/excelDownload.do")
	public String excelDownload(@ModelAttribute("searchVO") CalTypeMngVO searchVO,
			Model model)throws Exception {
		
		List<CalTypeMngVO> dataList = calTypeMngService.selectList(searchVO);
		
		// 데이터
		model.addAttribute("dataList", dataList);
		// 타이틀
		model.addAttribute("title", egovMessageSource.getMessage("word.calTypeMng"));
		// 검색조건
		model.addAttribute("useYn", egovMessageSource.getMessage("word.useYn"));
		model.addAttribute("findUseYn", CodeUtil.getCodeName("011", searchVO.getFindUseYn()));
		// header
		model.addAttribute("calTypeId", egovMessageSource.getMessage("word.addScoreCalTypeId"));	
		model.addAttribute("calTypeNm", egovMessageSource.getMessage("word.addScoreCalType"));	
		model.addAttribute("calType", egovMessageSource.getMessage("word.calPatternCalculus"));	
		model.addAttribute("metricCnt", egovMessageSource.getMessage("word.metricCnt"));			
		
		// 시트명
		model.addAttribute("sheetName", egovMessageSource.getMessage("word.calTypeMng"));
		
		model.addAttribute("destJxlsFileName", egovMessageSource.getMessage("word.calTypeMng") + "_" + EgovStringUtil.getTimeStamp()+".xlsx");
		model.addAttribute("templateJxlsFileName", "calTypeMngList.xlsx");

		return "excelDownloadView";
	}

	/**
	 * 산식관리 조회
	 * @param searchVO
	 * @return	ModelAndView
	 * @throws Exception
	 */
	@RequestMapping(value="/system/system/code/calTypeMng/selectDetail.do")
	public ModelAndView selectDetail(@ModelAttribute("searchVO") CalTypeMngVO searchVO)throws Exception {
		return makeJsonData(calTypeMngService.selectDetail(searchVO));
	}
	
	/**
	 * 산식관리 정렬순서저장
	 * @param dataVO
	 * @param model
	 * @param bindingResult
	 * @return	ModelAndView
	 * @throws Exception
	 */
	@RequestMapping("/system/system/code/calTypeMng/saveSortOrder.do")
	public ModelAndView saveSortOrder(@ModelAttribute("dataVO") CalTypeMngVO dataVO, Model model, BindingResult bindingResult) throws Exception {
		// list 유효성 체크
		validateList(dataVO.getGridDataList(), bindingResult);
		if(bindingResult.hasErrors()) {
			return makeFailJsonData(getListErrorMsg(bindingResult));
		}
		
		int resultCnt = calTypeMngService.updateSortOrder(dataVO);
		if(resultCnt == 0) {
			return makeFailJsonData();
		}
		return makeSuccessJsonData();
	}
	
	/**
	 * 산식관리 삭제
	 * @param dataVO
	 * @param model
	 * @param bindingResult
	 * @return	ModelAndView
	 * @throws Exception
	 */
	@RequestMapping("/system/system/code/calTypeMng/deleteCalTypeMng.do")
	public ModelAndView deleteCalTypeMng(@ModelAttribute("dataVO") CalTypeMngVO dataVO, Model model) throws Exception {
		int resultCnt = calTypeMngService.deleteCalTypeMng(dataVO);
		if(resultCnt == 0) {
			return makeFailJsonData();
		}
		return makeSuccessJsonData();
	}
	
	/**
	 * 산식관리 저장
	 * @param dataVO
	 * @param model
	 * @param bindingResult
	 * @return	ModelAndView
	 * @throws Exception
	 */
	@RequestMapping("/system/system/code/calTypeMng/saveCalTypeMng.do")
	public ModelAndView saveCalTypeMng(@ModelAttribute("dataVO") CalTypeMngVO dataVO, Model model, BindingResult bindingResult) throws Exception {
		beanValidator.validate(dataVO, bindingResult);
		if(bindingResult.hasErrors()){
			return makeFailJsonData(getListErrorMsg(bindingResult));
		}

		int resultCnt = calTypeMngService.saveData(dataVO);
		if(resultCnt == 0) {
			return makeFailJsonData();
		}
		return makeSuccessJsonData();
	}
}

