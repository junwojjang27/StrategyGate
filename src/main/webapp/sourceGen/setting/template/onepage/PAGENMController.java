/*************************************************************************
* CLASS 명	: nonCamelPageNmController
* 작 업 자	: devNm
* 작 업 일	: devDate
* 기	능	: koPageNm Controller
* ---------------------------- 변 경 이 력 --------------------------------
* 번호	작 업 자		작	업	일			변 경 내 용				비고
* ----	---------	----------------	---------------------	-----------
*	1	devNm		devDate
**************************************************************************/
package fullPackageDotPath.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import egovframework.let.utl.fcc.service.EgovStringUtil;
import fullPackageDotPath.service.impl.nonCamelPageNmServiceImpl;
import fullPackageDotPath.service.nonCamelPageNmVO;
/*공통 컨트롤러*/
import kr.ispark.common.web.BaseController;

@Controller
public class nonCamelPageNmController extends BaseController {
	@Autowired
	private nonCamelPageNmServiceImpl camelPageNmService;
	
	/**
	 * koPageNm 목록 화면
	 * @param	nonCamelPageNmVO searchVO
	 * @param	Model model
	 * @return	String
	 * @throws	Exception
	 */
	@RequestMapping("packageBarPath/camelPageNmList.do")
	public String camelPageNmList(@ModelAttribute("searchVO") nonCamelPageNmVO searchVO, Model model) throws Exception {
		return "packageBarPath/camelPageNmList." + searchVO.getLayout();
	}
	
	/**
	 * koPageNm 그리드 조회(json)
	 * @param	nonCamelPageNmVO searchVO
	 * @return	ModelAndView
	 * @throws	Exception
	 */
	@RequestMapping("packageBarPath/camelPageNmList_json.do")
	public ModelAndView camelPageNmList_json(@ModelAttribute("searchVO") nonCamelPageNmVO searchVO) throws Exception {
		List<nonCamelPageNmVO> dataList = camelPageNmService.selectList(searchVO);
		return makeGridJsonData(dataList, dataList.size(), searchVO);
	}
	
	//excelDownload
	/**
	 * 엑셀양식다운로드
	 * @param	nonCamelPageNmVO searchVO
	 * @param	Model model
	 * @return	ModelAndView
	 * @throws	Exception
	 */
	@RequestMapping("packageBarPath/excelDownload.do")
	public String excelDownload(@ModelAttribute("searchVO") nonCamelPageNmVO searchVO, Model model) throws Exception {
		
		/*
		 * 현재페이지 화면에 맞게 반드시 수정할 것.
		 */
		
		List<nonCamelPageNmVO> dataList = camelPageNmService.selectList(searchVO);
		
		// 타이틀
		model.addAttribute("title", egovMessageSource.getMessage("word.camelPageNmManage"));
		
		// 검색조건
		model.addAttribute("year", egovMessageSource.getMessage("word.year"));
		model.addAttribute("findYear", searchVO.getFindYear());
		
		// header
		model.addAttribute("camelPageNmNm", egovMessageSource.getMessage("word.camelPageNmNm"));	// koPageNm
		model.addAttribute("sortOrder", egovMessageSource.getMessage("word.sortOrder"));			// 정렬순서
		
		// 조직 데이터
		model.addAttribute("dataList", dataList);
		
		// 시트명
		model.addAttribute("sheetName", egovMessageSource.getMessage("word.camelPageNmNm"));
		
		model.addAttribute("destJxlsFileName", egovMessageSource.getMessage("word.camelPageNmNm") + "_" + EgovStringUtil.getTimeStamp()+".xlsx");
		model.addAttribute("templateJxlsFileName", "camelPageNmList.xlsx");

		return "excelDownloadView";
	}

	/**
	 * koPageNm 조회
	 * @param	nonCamelPageNmVO searchVO
	 * @return	ModelAndView
	 * @throws	Exception
	 */
	@RequestMapping("packageBarPath/selectDetail.do")
	public ModelAndView selectDetail(@ModelAttribute("searchVO") nonCamelPageNmVO searchVO) throws Exception {
		return makeJsonData(camelPageNmService.selectDetail(searchVO));
	}
	
	/**
	 * koPageNm 정렬순서저장
	 * @param	nonCamelPageNmVO dataVO
	 * @param	Model model
	 * @param	BindingResult bindingResult
	 * @return	ModelAndView
	 * @throws	Exception
	 */
	@RequestMapping("packageBarPath/saveSortOrder.do")
	public ModelAndView saveSortOrder(@ModelAttribute("dataVO") nonCamelPageNmVO dataVO, Model model, BindingResult bindingResult) throws Exception {
		// list 유효성 체크
		validateList(dataVO.getGridDataList(), bindingResult);
		if(bindingResult.hasErrors()) {
			return makeFailJsonData(getListErrorMsg(bindingResult));
		}
		
		return makeJsonDataByResultCnt(camelPageNmService.updateSortOrder(dataVO));
	}
	
	/**
	 * koPageNm 삭제
	 * @param	nonCamelPageNmVO dataVO
	 * @param	Model model
	 * @return	ModelAndView
	 * @throws	Exception
	 */
	@RequestMapping("packageBarPath/deletenonCamelPageNm.do")
	public ModelAndView deletenonCamelPageNm(@ModelAttribute("dataVO") nonCamelPageNmVO dataVO, Model model) throws Exception {
		return makeJsonDataByResultCnt(camelPageNmService.deletenonCamelPageNm(dataVO));
	}
	
	/**
	 * koPageNm 저장
	 * @param	nonCamelPageNmVO dataVO
	 * @param	Model model
	 * @param	BindingResult bindingResult
	 * @return	ModelAndView
	 * @throws	Exception
	 */
	@RequestMapping("packageBarPath/savenonCamelPageNm.do")
	public ModelAndView savenonCamelPageNm(@ModelAttribute("dataVO") nonCamelPageNmVO dataVO, Model model, BindingResult bindingResult) throws Exception {
		beanValidator.validate(dataVO, bindingResult);
		if(bindingResult.hasErrors()){
			return makeFailJsonData(getListErrorMsg(bindingResult));
		}

		return makeJsonDataByResultCnt(camelPageNmService.saveData(dataVO));
	}
}
