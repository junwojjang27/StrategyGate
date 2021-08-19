/*************************************************************************
* CLASS 명	: PerspectiveController
* 작 업 자	: joey
* 작 업 일	: 2018. 1. 29.
* 기	능	:  관점 Controller
* ---------------------------- 변 경 이 력 --------------------------------
* 번호	작 업 자		작	업	일			변 경 내 용				비고
* ----	---------	----------------	---------------------	-----------
*	1	joey		2018. 1. 29.		최 초 작 업
**************************************************************************/
package kr.ispark.bsc.base.strategy.perspective.web;

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
import kr.ispark.common.util.CodeUtil;
/*공통 컨트롤러*/
import kr.ispark.common.web.BaseController;

@Controller
public class PerspectiveController extends BaseController {
	@Autowired
	private PerspectiveServiceImpl perspectiveService;

	/**
	 * 관점 목록 화면
	 * @param	PerspectiveVO searchVO
	 * @param	Model model
	 * @return	String
	 * @throws	Exception
	 */
	@RequestMapping(value="/bsc/base/strategy/perspective/perspectiveList.do")
	public String perspectiveList(@ModelAttribute("searchVO") PerspectiveVO searchVO, Model model) throws Exception {
		return "/bsc/base/strategy/perspective/perspectiveList." + searchVO.getLayout();
	}

	/**
	 * 관점 그리드 조회(json)
	 * @param searchVO
	 * @return	ModelAndView
	 * @throws Exception
	 */
	@RequestMapping(value="/bsc/base/strategy/perspective/perspectiveList_json.do")
	public ModelAndView perspectiveList_json(@ModelAttribute("searchVO") PerspectiveVO searchVO)throws Exception {

		List<PerspectiveVO> dataList = perspectiveService.selectList(searchVO);
		return makeGridJsonData(dataList, dataList.size(), searchVO);
	}

	/**
	 * 엑셀양식다운로드
	 */
	@RequestMapping("/bsc/base/strategy/perspective/excelDownload.do")
	public String excelDownload(@ModelAttribute("searchVO") PerspectiveVO searchVO,
			Model model)throws Exception {

		// 타이틀
		model.addAttribute("title", egovMessageSource.getMessage("word.perspective"));

		// 검색조건
		model.addAttribute("yearSearchNm", egovMessageSource.getMessage("word.year"));
		model.addAttribute("yearSearchValue", CodeUtil.getCodeName("017", searchVO.getFindYear()));
		model.addAttribute("useYnSearchNm", egovMessageSource.getMessage("word.useYn"));
		model.addAttribute("useYnSearchValue", CodeUtil.getCodeName("011", searchVO.getFindUseYn()));

		// header
		model.addAttribute("perspectiveNmHeader", egovMessageSource.getMessage("word.perspective"));	// 관점
		model.addAttribute("useYnHeader", egovMessageSource.getMessage("word.useYn"));	// 관점
		model.addAttribute("sortOrderHeader", egovMessageSource.getMessage("word.sortOrder"));			// 정렬순서

		// 조직 데이터
		List<PerspectiveVO> dataList = perspectiveService.selectList(searchVO);
		model.addAttribute("dataList", dataList);

		// 시트명
		model.addAttribute("sheetName", egovMessageSource.getMessage("word.perspectiveNm"));

		model.addAttribute("destJxlsFileName", egovMessageSource.getMessage("word.perspectiveNm") + "_" + EgovStringUtil.getTimeStamp()+".xlsx");
		model.addAttribute("templateJxlsFileName", "perspectiveList.xlsx");

		return "excelDownloadView";
	}

	/**
	 * 관점 조회
	 * @param searchVO
	 * @return	ModelAndView
	 * @throws Exception
	 */
	@RequestMapping(value="/bsc/base/strategy/perspective/selectDetail.do")
	public ModelAndView selectDetail(@ModelAttribute("searchVO") PerspectiveVO searchVO)throws Exception {
		return makeJsonData(perspectiveService.selectDetail(searchVO));
	}

	/**
	 * 성과조직관리 정렬순서저장
	 * @param dataVO
	 * @param model
	 * @param bindingResult
	 * @return	ModelAndView
	 * @throws Exception
	 */
	@RequestMapping("/bsc/base/strategy/perspective/saveSortOrder.do")
	public ModelAndView saveSortOrder(@ModelAttribute("dataVO") PerspectiveVO dataVO, Model model, BindingResult bindingResult) throws Exception {
		// list 유효성 체크
		validateList(dataVO.getGridDataList(), bindingResult);
		if(bindingResult.hasErrors()) {
			return makeFailJsonData(getListErrorMsg(bindingResult));
		}

		int resultCnt = perspectiveService.updateSortOrder(dataVO);
		if(resultCnt == 0) {
			return makeFailJsonData();
		}
		return makeSuccessJsonData();
	}

	/**
	 * 성과조직 삭제
	 * @param dataVO
	 * @param model
	 * @param bindingResult
	 * @return	ModelAndView
	 * @throws Exception
	 */
	@RequestMapping("/bsc/base/strategy/perspective/deletePerspective.do")
	public ModelAndView deleteScDeptMng(@ModelAttribute("dataVO") PerspectiveVO dataVO, Model model) throws Exception {
		int resultCnt = perspectiveService.deletePerspective(dataVO);
		if(resultCnt == 0) {
			return makeFailJsonData();
		}
		return makeSuccessJsonData();
	}

	/**
	 * 성과조직 저장
	 * @param dataVO
	 * @param model
	 * @param bindingResult
	 * @return	ModelAndView
	 * @throws Exception
	 */
	@RequestMapping("/bsc/base/strategy/perspective/savePerspective.do")
	public ModelAndView savePerspective(@ModelAttribute("dataVO") PerspectiveVO dataVO, Model model, BindingResult bindingResult) throws Exception {

		validateList(dataVO.getGridDataList(), bindingResult);
		if(bindingResult.hasErrors()){
			return makeFailJsonData(getListErrorMsg(bindingResult));
		}

		int resultCnt = perspectiveService.saveData(dataVO);
		if(resultCnt == 0) {
			return makeFailJsonData();
		}
		return makeSuccessJsonData();
	}
}
