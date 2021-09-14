/*************************************************************************
* CLASS 명	: IdeaUsController
* 작 업 자	: 하성준
* 작 업 일	: 2021-09-08
* 기	능	: 혁신제안 Controller
* ---------------------------- 변 경 이 력 --------------------------------
* 번호	작 업 자		작	업	일			변 경 내 용				비고
* ----	---------	----------------	---------------------	-----------
*	1	하성준		2021-09-08
**************************************************************************/
package kr.ispark.system.system.menu.ideaUs.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import egovframework.let.utl.fcc.service.EgovStringUtil;
import kr.ispark.system.system.menu.ideaUs.service.impl.IdeaUsServiceImpl;
import kr.ispark.system.system.menu.ideaUs.service.IdeaUsVO;
/*공통 컨트롤러*/
import kr.ispark.common.web.BaseController;

@Controller
public class IdeaUsController extends BaseController {
	@Autowired
	private IdeaUsServiceImpl ideaUsService;
	
	/**
	 * 혁신제안 목록 화면
	 * @param	IdeaUsVO searchVO
	 * @param	Model model
	 * @return	String
	 * @throws	Exception
	 */
	@RequestMapping("/system/system/menu/ideaUs/ideaUsList.do")
	public String ideaUsList(@ModelAttribute("searchVO") IdeaUsVO searchVO, Model model) throws Exception {
		return "/system/system/menu/ideaUs/ideaUsList." + searchVO.getLayout();
	}
	
	/**
	 * 혁신제안 그리드 조회(json)
	 * @param	IdeaUsVO searchVO
	 * @return	ModelAndView
	 * @throws	Exception
	 */
	@RequestMapping("/system/system/menu/ideaUs/ideaUsList_json.do")
	public ModelAndView ideaUsList_json(@ModelAttribute("searchVO") IdeaUsVO searchVO) throws Exception {
		List<IdeaUsVO> dataList = ideaUsService.selectList(searchVO);
		return makeGridJsonData(dataList, dataList.size(), searchVO);
	}
	
	//excelDownload
	/**
	 * 엑셀양식다운로드
	 * @param	IdeaUsVO searchVO
	 * @param	Model model
	 * @return	ModelAndView
	 * @throws	Exception
	 */
	@RequestMapping("/system/system/menu/ideaUs/excelDownload.do")
	public String excelDownload(@ModelAttribute("searchVO") IdeaUsVO searchVO, Model model) throws Exception {
		
		/*
		 * 현재페이지 화면에 맞게 반드시 수정할 것.
		 */
		
		List<IdeaUsVO> dataList = ideaUsService.selectList(searchVO);
		
		// 타이틀
		model.addAttribute("title", egovMessageSource.getMessage("word.ideaUsManage"));
		
		// 검색조건
		model.addAttribute("year", egovMessageSource.getMessage("word.year"));
		model.addAttribute("findYear", searchVO.getFindYear());
		
		// header
		model.addAttribute("ideaUsNm", egovMessageSource.getMessage("word.ideaUsNm"));	// 혁신제안
		model.addAttribute("sortOrder", egovMessageSource.getMessage("word.sortOrder"));			// 정렬순서
		
		// 조직 데이터
		model.addAttribute("dataList", dataList);
		
		// 시트명
		model.addAttribute("sheetName", egovMessageSource.getMessage("word.ideaUsNm"));
		
		model.addAttribute("destJxlsFileName", egovMessageSource.getMessage("word.ideaUsNm") + "_" + EgovStringUtil.getTimeStamp()+".xlsx");
		model.addAttribute("templateJxlsFileName", "ideaUsList.xlsx");

		return "excelDownloadView";
	}

	/**
	 * 혁신제안 조회
	 * @param	IdeaUsVO searchVO
	 * @return	ModelAndView
	 * @throws	Exception
	 */
	@RequestMapping("/system/system/menu/ideaUs/selectDetail.do")
	public ModelAndView selectDetail(@ModelAttribute("searchVO") IdeaUsVO searchVO) throws Exception {
		return makeJsonData(ideaUsService.selectDetail(searchVO));
	}
	
	/**
	 * 혁신제안 정렬순서저장
	 * @param	IdeaUsVO dataVO
	 * @param	Model model
	 * @param	BindingResult bindingResult
	 * @return	ModelAndView
	 * @throws	Exception
	 */
	@RequestMapping("/system/system/menu/ideaUs/saveSortOrder.do")
	public ModelAndView saveSortOrder(@ModelAttribute("dataVO") IdeaUsVO dataVO, Model model, BindingResult bindingResult) throws Exception {
		// list 유효성 체크
		validateList(dataVO.getGridDataList(), bindingResult);
		if(bindingResult.hasErrors()) {
			return makeFailJsonData(getListErrorMsg(bindingResult));
		}
		
		return makeJsonDataByResultCnt(ideaUsService.updateSortOrder(dataVO));
	}
	
	/**
	 * 혁신제안 삭제
	 * @param	IdeaUsVO dataVO
	 * @param	Model model
	 * @return	ModelAndView
	 * @throws	Exception
	 */
	@RequestMapping("/system/system/menu/ideaUs/deleteIdeaUs.do")
	public ModelAndView deleteIdeaUs(@ModelAttribute("dataVO") IdeaUsVO dataVO, Model model) throws Exception {
		return makeJsonDataByResultCnt(ideaUsService.deleteIdeaUs(dataVO));
	}
	
	/**
	 * 혁신제안 저장
	 * @param	IdeaUsVO dataVO
	 * @param	Model model
	 * @param	BindingResult bindingResult
	 * @return	ModelAndView
	 * @throws	Exception
	 */
	@RequestMapping("/system/system/menu/ideaUs/saveIdeaUs.do")
	public ModelAndView saveIdeaUs(@ModelAttribute("dataVO") IdeaUsVO dataVO, Model model, BindingResult bindingResult) throws Exception {
		beanValidator.validate(dataVO, bindingResult);
		if(bindingResult.hasErrors()){
			return makeFailJsonData(getListErrorMsg(bindingResult));
		}
		/*추가한부분*/
		int resultCnt = ideaUsService.saveData(dataVO);
		if(resultCnt == 0) {
			return makeFailJsonData();
		}
		/**/
		//return makeJsonDataByResultCnt(ideaSingleService.saveData(dataVO));
		return makeSuccessJsonData();

		//return makeJsonDataByResultCnt(ideaUsService.saveData(dataVO));
	}
}

