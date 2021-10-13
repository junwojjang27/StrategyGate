/*************************************************************************
* CLASS 명	: IdeaEvalItemController
* 작 업 자	: 하성준
* 작 업 일	: 2021-09-27
* 기	능	: 평가항목관리 Controller
* ---------------------------- 변 경 이 력 --------------------------------
* 번호	작 업 자		작	업	일			변 경 내 용				비고
* ----	---------	----------------	---------------------	-----------
*	1	하성준		2021-09-27
**************************************************************************/
package kr.ispark.system.system.menu.ideaEvalItem.web;

import java.util.List;

import kr.ispark.system.system.menu.ideaSingle.service.IdeaSingleVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import egovframework.let.utl.fcc.service.EgovStringUtil;
import kr.ispark.system.system.menu.ideaEvalItem.service.impl.IdeaEvalItemServiceImpl;
import kr.ispark.system.system.menu.ideaEvalItem.service.IdeaEvalItemVO;
/*공통 컨트롤러*/
import kr.ispark.common.web.BaseController;

@Controller
public class IdeaEvalItemController extends BaseController {
	@Autowired
	private IdeaEvalItemServiceImpl ideaEvalItemService;
	
	/**
	 * 평가항목관리 목록 화면
	 * @param	IdeaEvalItemVO searchVO
	 * @param	Model model
	 * @return	String
	 * @throws	Exception
	 */
	@RequestMapping("/system/system/menu/ideaEvalItem/ideaEvalItemList.do")
	public String ideaEvalItemList(@ModelAttribute("searchVO") IdeaEvalItemVO searchVO, Model model) throws Exception {
		return "/system/system/menu/ideaEvalItem/ideaEvalItemList." + searchVO.getLayout();
	}
	
	/**
	 * 평가항목관리 그리드 조회(json)
	 * @param	IdeaEvalItemVO searchVO
	 * @return	ModelAndView
	 * @throws	Exception
	 */
	@RequestMapping("/system/system/menu/ideaEvalItem/ideaEvalItemList_json.do")
	public ModelAndView ideaEvalItemList_json(@ModelAttribute("searchVO") IdeaEvalItemVO searchVO) throws Exception {
		List<IdeaEvalItemVO> dataList = ideaEvalItemService.selectList(searchVO);
		return makeGridJsonData(dataList, dataList.size(), searchVO);
	}

	//excelDownload
	/**
	 * 엑셀양식다운로드
	 * @param	IdeaEvalItemVO searchVO
	 * @param	Model model
	 * @return	ModelAndView
	 * @throws	Exception
	 */
	@RequestMapping("/system/system/menu/ideaEvalItem/excelDownload.do")
	public String excelDownload(@ModelAttribute("searchVO") IdeaEvalItemVO searchVO, Model model) throws Exception {

		List<IdeaEvalItemVO> dataList = ideaEvalItemService.selectExcelList(searchVO);

		// 타이틀
		model.addAttribute("ideaEvalItemManage", egovMessageSource.getMessage("word.ideaEvalItemManage"));
		// 검색조건
//		model.addAttribute("year", egovMessageSource.getMessage("word.year"));
//		model.addAttribute("findYear", searchVO.getFindYear());
		// header
		model.addAttribute("year", egovMessageSource.getMessage("word.year"));
		model.addAttribute("evalDegreeId", egovMessageSource.getMessage("word.evalDegreeId"));
		model.addAttribute("deleteDt", egovMessageSource.getMessage("word.deleteDT"));
		model.addAttribute("evalItemTitle", egovMessageSource.getMessage("word.evalItemTitle"));
		model.addAttribute("evalItemContent", egovMessageSource.getMessage("word.evalItemContent"));
		model.addAttribute("weightId", egovMessageSource.getMessage("word.weightId"));
		model.addAttribute("particalTypeId", egovMessageSource.getMessage("word.particalTypeId"));
		// 조직 데이터
		model.addAttribute("dataList", dataList);
		// 시트명
		model.addAttribute("sheetName", egovMessageSource.getMessage("word.ideaEvalItemNm"));
		model.addAttribute("destJxlsFileName", egovMessageSource.getMessage("word.ideaEvalItemNm") + "_" + EgovStringUtil.getTimeStamp()+".xlsx");
		model.addAttribute("templateJxlsFileName", "ideaEvalItemList.xlsx");
		return "excelDownloadView";
	}

	/**
	 * 평가항목관리 조회
	 * @param	IdeaEvalItemVO searchVO
	 * @return	ModelAndView
	 * @throws	Exception
	 */
	@RequestMapping("/system/system/menu/ideaEvalItem/selectDetail.do")
	public ModelAndView selectDetail(@ModelAttribute("searchVO") IdeaEvalItemVO searchVO) throws Exception {
		return makeJsonData(ideaEvalItemService.selectDetail(searchVO));
	}

	/**
	 * 평가항목관리 정렬순서저장
	 * @param	IdeaEvalItemVO dataVO
	 * @param	Model model
	 * @param	BindingResult bindingResult
	 * @return	ModelAndView
	 * @throws	Exception
	 */
	@RequestMapping("/system/system/menu/ideaEvalItem/saveSortOrder.do")
	public ModelAndView saveSortOrder(@ModelAttribute("dataVO") IdeaEvalItemVO dataVO, Model model, BindingResult bindingResult) throws Exception {
		// list 유효성 체크
		validateList(dataVO.getGridDataList(), bindingResult);
		if(bindingResult.hasErrors()) {
			return makeFailJsonData(getListErrorMsg(bindingResult));
		}

		return makeJsonDataByResultCnt(ideaEvalItemService.updateSortOrder(dataVO));	//화면에서 해당버튼 누르면 처리된 개수가 나오는데 처리된 개수에 따라 성공 실패
	}

	/**
	 * 평가항목관리 삭제
	 * @param	IdeaEvalItemVO dataVO
	 * @param	Model model
	 * @return	ModelAndView
	 * @throws	Exception
	 */
	@RequestMapping("/system/system/menu/ideaEvalItem/deleteIdeaEvalItem.do")
	public ModelAndView deleteIdeaEvalItem(@ModelAttribute("dataVO") IdeaEvalItemVO dataVO, Model model) throws Exception {
		int resultCnt = ideaEvalItemService.deleteIdeaEvalItem(dataVO);
		if(resultCnt == 0) {
			return makeFailJsonData();
		}
		return makeSuccessJsonData();
	}
	
	/**
	 * 평가항목관리 저장
	 * @param	IdeaEvalItemVO dataVO
	 * @param	Model model
	 * @param	BindingResult bindingResult
	 * @return	ModelAndView
	 * @throws	Exception
	 */
	@RequestMapping("/system/system/menu/ideaEvalItem/saveIdeaEvalItem.do")
	public ModelAndView saveIdeaEvalItem(@ModelAttribute("dataVO") IdeaEvalItemVO dataVO, Model model, BindingResult bindingResult) throws Exception {


		//아래줄 설명 : array로 들어온 모든 그리트데이타 넣어서 한건한건 문제가 있는지 검증
		validateList(dataVO.getGridDataList(), bindingResult);
		if(bindingResult.hasErrors()){
			return makeFailJsonData(getListErrorMsg(bindingResult));
		}
		int resultCnt = ideaEvalItemService.saveData(dataVO);
		if(resultCnt == 0) {
			return makeFailJsonData();
		}
		return makeSuccessJsonData();
	}
}

