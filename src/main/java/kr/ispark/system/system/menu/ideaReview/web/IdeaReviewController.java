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

import kr.ispark.common.util.CommonUtil;
import kr.ispark.system.system.menu.ideaUs.service.IdeaUsVO;
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

		int resultCnt = ideaReviewService.saveData(dataVO);
		if(resultCnt == 0) {
			return makeFailJsonData();
		}
		return makeSuccessJsonData();
	}

	/**
	 * IDEA+검토 첨부파일
	 */
	@RequestMapping("/system/system/menu/ideaReview/ideaReviewDetail.do")
	public String getAtchFileForm(@ModelAttribute("searchVO") IdeaReviewVO ideaReviewVO, Model model) {
		//model.addAttribute("searchVO",IdeaUsVO);
		return "/system/system/menu/ideaReview/ideaReviewAtchFileForm";
	}
}

