/*************************************************************************
* CLASS 명	: IdeaEvalController
* 작 업 자	: 하성준
* 작 업 일	: 2021-10-13
* 기	능	: 평가하기 Controller
* ---------------------------- 변 경 이 력 --------------------------------
* 번호	작 업 자		작	업	일			변 경 내 용				비고
* ----	---------	----------------	---------------------	-----------
*	1	하성준		2021-10-13
**************************************************************************/
package kr.ispark.system.system.menu.ideaEval.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import egovframework.let.utl.fcc.service.EgovStringUtil;
import kr.ispark.system.system.menu.ideaEval.service.impl.IdeaEvalServiceImpl;
import kr.ispark.system.system.menu.ideaEval.service.IdeaEvalVO;
/*공통 컨트롤러*/
import kr.ispark.common.web.BaseController;

@Controller
public class IdeaEvalController extends BaseController {
	@Autowired
	private IdeaEvalServiceImpl ideaEvalService;
	
	/**
	 * 평가하기 목록 화면
	 * @param	IdeaEvalVO searchVO
	 * @param	Model model
	 * @return	String
	 * @throws	Exception
	 */
	@RequestMapping("/system/system/menu/ideaEval/ideaEvalList.do")
	public String ideaEvalList(@ModelAttribute("searchVO") IdeaEvalVO searchVO, Model model) throws Exception {
		return "/system/system/menu/ideaEval/ideaEvalList." + searchVO.getLayout();
	}
	
	/**
	 * 평가하기 그리드 조회(json)
	 * @param	IdeaEvalVO searchVO
	 * @return	ModelAndView
	 * @throws	Exception
	 */
	@RequestMapping("/system/system/menu/ideaEval/ideaEvalList_json.do")
	public ModelAndView ideaEvalList_json(@ModelAttribute("searchVO") IdeaEvalVO searchVO) throws Exception {
		List<IdeaEvalVO> dataList = ideaEvalService.selectList(searchVO);
		return makeGridJsonData(dataList, dataList.size(), searchVO);
	}
	
	//excelDownload
	/**
	 * 엑셀양식다운로드
	 * @param	IdeaEvalVO searchVO
	 * @param	Model model
	 * @return	ModelAndView
	 * @throws	Exception
	 */
	@RequestMapping("/system/system/menu/ideaEval/excelDownload.do")
	public String excelDownload(@ModelAttribute("searchVO") IdeaEvalVO searchVO, Model model) throws Exception {
		
		/*
		 * 현재페이지 화면에 맞게 반드시 수정할 것.
		 */
		
		List<IdeaEvalVO> dataList = ideaEvalService.selectList(searchVO);
		
		// 타이틀
		model.addAttribute("title", egovMessageSource.getMessage("word.ideaEvalManage"));
		
		// 검색조건
		model.addAttribute("year", egovMessageSource.getMessage("word.year"));
		model.addAttribute("findYear", searchVO.getFindYear());
		
		// header
		model.addAttribute("ideaEvalNm", egovMessageSource.getMessage("word.ideaEvalNm"));	// 평가하기
		model.addAttribute("sortOrder", egovMessageSource.getMessage("word.sortOrder"));			// 정렬순서
		
		// 조직 데이터
		model.addAttribute("dataList", dataList);
		
		// 시트명
		model.addAttribute("sheetName", egovMessageSource.getMessage("word.ideaEvalNm"));
		
		model.addAttribute("destJxlsFileName", egovMessageSource.getMessage("word.ideaEvalNm") + "_" + EgovStringUtil.getTimeStamp()+".xlsx");
		model.addAttribute("templateJxlsFileName", "ideaEvalList.xlsx");

		return "excelDownloadView";
	}

	/**
	 * 평가하기 조회
	 * @param	IdeaEvalVO searchVO
	 * @return	ModelAndView
	 * @throws	Exception
	 */
	@RequestMapping("/system/system/menu/ideaEval/ideaEvalDetail.do")
	public ModelAndView selectDetail(@ModelAttribute("searchVO") IdeaEvalVO searchVO) throws Exception {
		return makeJsonData(ideaEvalService.selectDetail(searchVO));
	}
	
	/**
	 * 평가하기 정렬순서저장
	 * @param	IdeaEvalVO dataVO
	 * @param	Model model
	 * @param	BindingResult bindingResult
	 * @return	ModelAndView
	 * @throws	Exception
	 */
	@RequestMapping("/system/system/menu/ideaEval/saveSortOrder.do")
	public ModelAndView saveSortOrder(@ModelAttribute("dataVO") IdeaEvalVO dataVO, Model model, BindingResult bindingResult) throws Exception {
		// list 유효성 체크
		validateList(dataVO.getGridDataList(), bindingResult);
		if(bindingResult.hasErrors()) {
			return makeFailJsonData(getListErrorMsg(bindingResult));
		}
		
		return makeJsonDataByResultCnt(ideaEvalService.updateSortOrder(dataVO));
	}
	
	/**
	 * 평가하기 삭제
	 * @param	IdeaEvalVO dataVO
	 * @param	Model model
	 * @return	ModelAndView
	 * @throws	Exception
	 */
	@RequestMapping("/system/system/menu/ideaEval/deleteIdeaEval.do")
	public ModelAndView deleteIdeaEval(@ModelAttribute("dataVO") IdeaEvalVO dataVO, Model model) throws Exception {
		return makeJsonDataByResultCnt(ideaEvalService.deleteIdeaEval(dataVO));
	}
	
	/**
	 * 평가하기 저장
	 * @param	IdeaEvalVO dataVO
	 * @param	Model model
	 * @param	BindingResult bindingResult
	 * @return	ModelAndView
	 * @throws	Exception
	 */
	@RequestMapping("/system/system/menu/ideaEval/saveIdeaEval.do")
	public ModelAndView saveIdeaEval(@ModelAttribute("dataVO") IdeaEvalVO dataVO, Model model, BindingResult bindingResult) throws Exception {
		beanValidator.validate(dataVO, bindingResult);
		if(bindingResult.hasErrors()){
			return makeFailJsonData(getListErrorMsg(bindingResult));
		}

		return makeJsonDataByResultCnt(ideaEvalService.saveData(dataVO));
	}
}

