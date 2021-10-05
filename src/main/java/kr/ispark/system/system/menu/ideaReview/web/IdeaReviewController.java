/*************************************************************************
* CLASS 명	: IdeaReviewController
* 작 업 자	: 하성준
* 작 업 일	: 2021-10-05
* 기	능	: IDEA+검토 Controller
* ---------------------------- 변 경 이 력 --------------------------------
* 번호	작 업 자		작	업	일			변 경 내 용				비고
* ----	---------	----------------	---------------------	-----------
*	1	하성준		2021-10-05
**************************************************************************/
package kr.ispark.system.system.menu.ideaReview.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import egovframework.let.utl.fcc.service.EgovStringUtil;
import kr.ispark.system.system.menu.ideaReview.service.impl.IdeaReviewServiceImpl;
import kr.ispark.system.system.menu.ideaReview.service.IdeaReviewVO;
/*공통 컨트롤러*/
import kr.ispark.common.web.BaseController;

@Controller
public class IdeaReviewController extends BaseController {
	@Autowired
	private IdeaReviewServiceImpl ideaReviewService;
	
	/**
	 * IDEA+검토 목록 화면
	 * @param	IdeaReviewVO searchVO
	 * @param	Model model
	 * @return	String
	 * @throws	Exception
	 */
	@RequestMapping("/system/system/menu/ideaReview/ideaReviewList.do")
	public String ideaReviewList(@ModelAttribute("searchVO") IdeaReviewVO searchVO, Model model) throws Exception {
		return "/system/system/menu/ideaReview/ideaReviewList." + searchVO.getLayout();
	}
	
	/**
	 * IDEA+검토 그리드 조회(json)
	 * @param	IdeaReviewVO searchVO
	 * @return	ModelAndView
	 * @throws	Exception
	 */
	@RequestMapping("/system/system/menu/ideaReview/ideaReviewList_json.do")
	public ModelAndView ideaReviewList_json(@ModelAttribute("searchVO") IdeaReviewVO searchVO) throws Exception {
		List<IdeaReviewVO> dataList = ideaReviewService.selectList(searchVO);
		return makeGridJsonData(dataList, dataList.size(), searchVO);
	}
	
	//excelDownload
	/**
	 * 엑셀양식다운로드
	 * @param	IdeaReviewVO searchVO
	 * @param	Model model
	 * @return	ModelAndView
	 * @throws	Exception
	 */
	@RequestMapping("/system/system/menu/ideaReview/excelDownload.do")
	public String excelDownload(@ModelAttribute("searchVO") IdeaReviewVO searchVO, Model model) throws Exception {
		
		/*
		 * 현재페이지 화면에 맞게 반드시 수정할 것.
		 */
		
		List<IdeaReviewVO> dataList = ideaReviewService.selectList(searchVO);
		
		// 타이틀
		model.addAttribute("title", egovMessageSource.getMessage("word.ideaReviewManage"));
		
		// 검색조건
		model.addAttribute("year", egovMessageSource.getMessage("word.year"));
		model.addAttribute("findYear", searchVO.getFindYear());
		
		// header
		model.addAttribute("ideaReviewNm", egovMessageSource.getMessage("word.ideaReviewNm"));	// IDEA+검토
		model.addAttribute("sortOrder", egovMessageSource.getMessage("word.sortOrder"));			// 정렬순서
		
		// 조직 데이터
		model.addAttribute("dataList", dataList);
		
		// 시트명
		model.addAttribute("sheetName", egovMessageSource.getMessage("word.ideaReviewNm"));
		
		model.addAttribute("destJxlsFileName", egovMessageSource.getMessage("word.ideaReviewNm") + "_" + EgovStringUtil.getTimeStamp()+".xlsx");
		model.addAttribute("templateJxlsFileName", "ideaReviewList.xlsx");

		return "excelDownloadView";
	}

	/**
	 * IDEA+검토 조회
	 * @param	IdeaReviewVO searchVO
	 * @return	ModelAndView
	 * @throws	Exception
	 */
	@RequestMapping("/system/system/menu/ideaReview/selectDetail.do")
	public ModelAndView selectDetail(@ModelAttribute("searchVO") IdeaReviewVO searchVO) throws Exception {
		return makeJsonData(ideaReviewService.selectDetail(searchVO));
	}
	
	/**
	 * IDEA+검토 정렬순서저장
	 * @param	IdeaReviewVO dataVO
	 * @param	Model model
	 * @param	BindingResult bindingResult
	 * @return	ModelAndView
	 * @throws	Exception
	 */
	@RequestMapping("/system/system/menu/ideaReview/saveSortOrder.do")
	public ModelAndView saveSortOrder(@ModelAttribute("dataVO") IdeaReviewVO dataVO, Model model, BindingResult bindingResult) throws Exception {
		// list 유효성 체크
		validateList(dataVO.getGridDataList(), bindingResult);
		if(bindingResult.hasErrors()) {
			return makeFailJsonData(getListErrorMsg(bindingResult));
		}
		
		return makeJsonDataByResultCnt(ideaReviewService.updateSortOrder(dataVO));
	}
	
	/**
	 * IDEA+검토 삭제
	 * @param	IdeaReviewVO dataVO
	 * @param	Model model
	 * @return	ModelAndView
	 * @throws	Exception
	 */
	@RequestMapping("/system/system/menu/ideaReview/deleteIdeaReview.do")
	public ModelAndView deleteIdeaReview(@ModelAttribute("dataVO") IdeaReviewVO dataVO, Model model) throws Exception {
		return makeJsonDataByResultCnt(ideaReviewService.deleteIdeaReview(dataVO));
	}
	
	/**
	 * IDEA+검토 저장
	 * @param	IdeaReviewVO dataVO
	 * @param	Model model
	 * @param	BindingResult bindingResult
	 * @return	ModelAndView
	 * @throws	Exception
	 */
	@RequestMapping("/system/system/menu/ideaReview/saveIdeaReview.do")
	public ModelAndView saveIdeaReview(@ModelAttribute("dataVO") IdeaReviewVO dataVO, Model model, BindingResult bindingResult) throws Exception {
		beanValidator.validate(dataVO, bindingResult);
		if(bindingResult.hasErrors()){
			return makeFailJsonData(getListErrorMsg(bindingResult));
		}

		return makeJsonDataByResultCnt(ideaReviewService.saveData(dataVO));
	}
}

