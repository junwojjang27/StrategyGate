package kr.ispark.system.system.menu.ideaSingle.web;

import java.util.List;

import kr.ispark.bsc.system.system.notice.service.NoticeVO;
import kr.ispark.common.util.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import egovframework.let.utl.fcc.service.EgovStringUtil;
import kr.ispark.system.system.menu.ideaSingle.service.impl.IdeaSingleServiceImpl;
import kr.ispark.system.system.menu.ideaSingle.service.IdeaSingleVO;
/*공통 컨트롤러*/
import kr.ispark.common.web.BaseController;

@Controller
public class IdeaSingleController extends BaseController {
	@Autowired
	private IdeaSingleServiceImpl ideaSingleService;

	/**
	 * 간단 IDEA+ 목록 화면
	 * @param	IdeaSingleVO searchVO
	 * @param	Model model
	 * @return	String
	 * @throws	Exception
	 */
	@RequestMapping("/system/system/menu/ideaSingle/ideaSingleList.do")
	public String ideaSingleList(@ModelAttribute("searchVO") IdeaSingleVO searchVO, Model model) throws Exception {
		return "/system/system/menu/ideaSingle/ideaSingleList." + searchVO.getLayout();
	}

	/**
	 * 간단 IDEA+ 그리드 조회(json)
	 * @param	IdeaSingleVO searchVO
	 * @return	ModelAndView
	 * @throws	Exception
	 */
	@RequestMapping("/system/system/menu/ideaSingle/ideaSingleList_json.do")
	public ModelAndView ideaSingleList_json(@ModelAttribute("searchVO") IdeaSingleVO searchVO) throws Exception {
		List<IdeaSingleVO> dataList = ideaSingleService.selectList(searchVO);
		return makeGridJsonData(dataList, dataList.size(), searchVO);
	}

	/**
	 * 간단 IDEA+ 조회
	 * @param	IdeaSingleVO searchVO
	 * @return	ModelAndView
	 * @throws	Exception
	 */
	@RequestMapping("/system/system/menu/ideaSingle/selectDetail.do")
	public ModelAndView selectDetail(@ModelAttribute("searchVO") IdeaSingleVO searchVO) throws Exception {
		return makeJsonData(ideaSingleService.selectDetail(searchVO));
	}

	/**
	 * 간단 IDEA+ 정렬순서저장
	 * @param	IdeaSingleVO dataVO
	 * @param	Model model
	 * @param	BindingResult bindingResult
	 * @return	ModelAndView
	 * @throws	Exception
	 */
	@RequestMapping("/system/system/menu/ideaSingle/saveSortOrder.do")
	public ModelAndView saveSortOrder(@ModelAttribute("dataVO") IdeaSingleVO dataVO, Model model, BindingResult bindingResult) throws Exception {
		validateList(dataVO.getGridDataList(), bindingResult); // list 유효성 체크
		if(bindingResult.hasErrors()) {	//바인딩 결과가 에러일 경우
			return makeFailJsonData(getListErrorMsg(bindingResult));
		}

		return makeJsonDataByResultCnt(ideaSingleService.updateSortOrder(dataVO));
	}

	/**
	 * 간단 IDEA+ 삭제
	 * @param	IdeaSingleVO dataVO
	 * @param	Model model
	 * @return	ModelAndView
	 * @throws	Exception
	 */
	@RequestMapping("/system/system/menu/ideaSingle/deleteIdeaSingle.do")
	public ModelAndView deleteIdeaSingle(@ModelAttribute("dataVO") IdeaSingleVO dataVO, Model model) throws Exception {
		//return makeJsonDataByResultCnt(ideaSingleService.deleteIdeaSingle(dataVO));
		int resultCnt = ideaSingleService.deleteIdeaSingle(dataVO);
		if(resultCnt == 0) {	//쿼리문이 정상적으로 실행된 횟수가 0일때 (오류)
			return makeFailJsonData();
		}
		return makeSuccessJsonData();
	}

	/**
	 * 간단 IDEA+ 저장
	 * @param	IdeaSingleVO dataVO
	 * @param	Model model
	 * @param	BindingResult bindingResult
	 * @return	ModelAndView
	 * @throws	Exception
	 */
	@RequestMapping("/system/system/menu/ideaSingle/saveIdeaSingle.do")
	public ModelAndView saveIdeaSingle(@ModelAttribute("dataVO") IdeaSingleVO dataVO, Model model, BindingResult bindingResult) throws Exception {
		beanValidator.validate(dataVO, bindingResult);
		if(bindingResult.hasErrors()){
			return makeFailJsonData(getListErrorMsg(bindingResult));
		}
		//return makeJsonDataByResultCnt(ideaSingleService.saveData(dataVO));

		int resultCnt = ideaSingleService.saveData(dataVO);
		if(resultCnt == 0) {
			return makeFailJsonData();
		}


		return makeSuccessJsonData();

	}
}
