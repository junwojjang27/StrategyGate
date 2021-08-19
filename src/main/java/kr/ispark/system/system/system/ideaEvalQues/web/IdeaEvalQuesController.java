/*************************************************************************
* CLASS 명	: IdeaEvalQuesController
* 작 업 자	: 문은경
* 작 업 일	: 2019-05-21
* 기	능	: 평가 질문 Controller
* ---------------------------- 변 경 이 력 --------------------------------
* 번호	작 업 자		작	업	일			변 경 내 용				비고
* ----	---------	----------------	---------------------	-----------
*	1	문은경		2019-05-21
**************************************************************************/
package kr.ispark.system.system.system.ideaEvalQues.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import egovframework.let.utl.fcc.service.EgovStringUtil;
import kr.ispark.system.system.system.ideaEvalQues.service.impl.IdeaEvalQuesServiceImpl;
import kr.ispark.system.system.survey.survReg.service.SurvRegVO;
import kr.ispark.system.system.system.ideaEvalQues.service.IdeaEvalQuesVO;
/*공통 컨트롤러*/
import kr.ispark.common.web.BaseController;

@Controller
public class IdeaEvalQuesController extends BaseController {
	@Autowired
	private IdeaEvalQuesServiceImpl ideaEvalQuesService;
	
	/**
	 * 평가 질문 목록 화면
	 * @param	IdeaEvalQuesVO searchVO
	 * @param	Model model
	 * @return	String
	 * @throws	Exception
	 */
	@RequestMapping("/system/system/system/ideaEvalQues/ideaEvalQuesList.do")
	public String ideaEvalQuesList(@ModelAttribute("searchVO") IdeaEvalQuesVO searchVO, Model model) throws Exception {
		//model.addAttribute("dataVO", searchVO);
		IdeaEvalQuesVO vo = new IdeaEvalQuesVO();
		List<IdeaEvalQuesVO> surveyList = ideaEvalQuesService.selectList(vo);
		model.addAttribute("surveyList", surveyList);
		return "/system/system/system/ideaEvalQues/ideaEvalQuesList." + searchVO.getLayout();
	}
	
	/**
	 * 평가 질문 그리드 조회(json)
	 * @param	IdeaEvalQuesVO searchVO
	 * @return	ModelAndView
	 * @throws	Exception
	 */
	@RequestMapping("/system/system/system/ideaEvalQues/ideaEvalQuesList_json.do")
	public ModelAndView ideaEvalQuesList_json(@ModelAttribute("searchVO") IdeaEvalQuesVO searchVO) throws Exception {
		List<IdeaEvalQuesVO> dataList = ideaEvalQuesService.selectList(searchVO);
		return makeGridJsonData(dataList, dataList.size(), searchVO);
	}
	
	//excelDownload
	/**
	 * 엑셀양식다운로드
	 * @param	IdeaEvalQuesVO searchVO
	 * @param	Model model
	 * @return	ModelAndView
	 * @throws	Exception
	 */
	@RequestMapping("/system/system/system/ideaEvalQues/excelDownload.do")
	public String excelDownload(@ModelAttribute("searchVO") IdeaEvalQuesVO searchVO, Model model) throws Exception {
		
		/*
		 * 현재페이지 화면에 맞게 반드시 수정할 것.
		 */
		
		List<IdeaEvalQuesVO> dataList = ideaEvalQuesService.selectList(searchVO);
		
		// 타이틀
		model.addAttribute("title", egovMessageSource.getMessage("word.ideaEvalQuesManage"));
		
		// 검색조건
		model.addAttribute("year", egovMessageSource.getMessage("word.year"));
		model.addAttribute("findYear", searchVO.getFindYear());
		
		// header
		model.addAttribute("ideaEvalQuesNm", egovMessageSource.getMessage("word.ideaEvalQuesNm"));	// 평가 질문
		model.addAttribute("sortOrder", egovMessageSource.getMessage("word.sortOrder"));			// 정렬순서
		
		// 조직 데이터
		model.addAttribute("dataList", dataList);
		
		// 시트명
		model.addAttribute("sheetName", egovMessageSource.getMessage("word.ideaEvalQuesNm"));
		
		model.addAttribute("destJxlsFileName", egovMessageSource.getMessage("word.ideaEvalQuesNm") + "_" + EgovStringUtil.getTimeStamp()+".xlsx");
		model.addAttribute("templateJxlsFileName", "ideaEvalQuesList.xlsx");

		return "excelDownloadView";
	}

	/**
	 * 평가 질문 조회
	 * @param	IdeaEvalQuesVO searchVO
	 * @return	ModelAndView
	 * @throws	Exception
	 */
	@RequestMapping("/system/system/system/ideaEvalQues/selectDetail.do")
	public ModelAndView selectDetail(@ModelAttribute("searchVO") IdeaEvalQuesVO searchVO) throws Exception {
		return makeJsonData(ideaEvalQuesService.selectDetail(searchVO));
	}	
	
	
	/**
	 * 평가 질문 삭제
	 * @param	IdeaEvalQuesVO dataVO
	 * @param	Model model
	 * @return	ModelAndView
	 * @throws	Exception
	 */
	@RequestMapping("/system/system/system/ideaEvalQues/deleteIdeaEvalQues.do")
	public ModelAndView deleteIdeaEvalQues(@ModelAttribute("dataVO") IdeaEvalQuesVO dataVO, Model model) throws Exception {
		return makeJsonDataByResultCnt(ideaEvalQuesService.deleteIdeaEvalQues(dataVO));
	}
	
	/**
	 * 평가 질문 저장
	 * @param	IdeaEvalQuesVO dataVO
	 * @param	Model model
	 * @param	BindingResult bindingResult
	 * @return	ModelAndView
	 * @throws	Exception
	 */
	@RequestMapping("/system/system/system/ideaEvalQues/saveIdeaEvalQues.do")
	public ModelAndView saveIdeaEvalQues(@ModelAttribute("dataVO") IdeaEvalQuesVO dataVO, Model model, BindingResult bindingResult) throws Exception {
		beanValidator.validate(dataVO, bindingResult);
		if(bindingResult.hasErrors()){
			return makeFailJsonData(getListErrorMsg(bindingResult));
		}
		return makeJsonDataByResultCnt(ideaEvalQuesService.saveData(dataVO));
	}
}

