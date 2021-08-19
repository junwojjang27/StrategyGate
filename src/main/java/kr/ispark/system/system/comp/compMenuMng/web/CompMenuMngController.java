/*************************************************************************
* CLASS 명	: MenuMngController
* 작 업 자	: joey
* 작 업 일	: 2018-1-7
* 기	능	: 메뉴관리 Controller
* ---------------------------- 변 경 이 력 --------------------------------
* 번호	작 업 자		작	업	일			변 경 내 용				비고
* ----	---------	----------------	---------------------	-----------
*	1	joey		2018-1-7
**************************************************************************/
package kr.ispark.system.system.comp.compMenuMng.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/*공통 컨트롤러*/
import kr.ispark.common.web.BaseController;
import kr.ispark.system.system.comp.compMenuMng.service.CompMenuMngVO;
import kr.ispark.system.system.comp.compMenuMng.service.impl.CompMenuMngServiceImpl;

@Controller
public class CompMenuMngController extends BaseController {
	@Autowired
	private CompMenuMngServiceImpl compMenuMngService;

	/**
	 * 메뉴관리 목록 화면
	 * @param	CompMenuMngVO searchVO
	 * @param	Model model
	 * @return	String
	 * @throws	Exception
	 */
	@RequestMapping(value="/system/system/comp/compMenuMng/compMenuMngList.do")
	public String menuMngList(@ModelAttribute("searchVO") CompMenuMngVO searchVO, Model model) throws Exception {

		List<CompMenuMngVO> langList = compMenuMngService.selectLangList(searchVO);
		model.addAttribute("langList", langList);

		return "/system/system/comp/compMenuMng/compMenuMngList." + searchVO.getLayout();
	}

	/**
	 * 메뉴관리 그리드 조회(json)
	 * @param searchVO
	 * @return	ModelAndView
	 * @throws Exception
	 */
	@RequestMapping(value="/system/system/comp/compMenuMng/compMenuMngList_json.do")
	public ModelAndView menuMngList_json(@ModelAttribute("searchVO") CompMenuMngVO searchVO)throws Exception {

		List<CompMenuMngVO> dataList = compMenuMngService.selectList(searchVO);
		return makeGridJsonData(dataList, dataList.size(), searchVO);
	}

	/**
	 * 메뉴관리 목록 화면
	 * @param	CompMenuMngVO searchVO
	 * @param	Model model
	 * @return	String
	 * @throws	Exception
	 */
	@RequestMapping(value="/system/system/comp/compMenuMng/popRoleList.do")
	public String popRoleList(@ModelAttribute("searchVO") CompMenuMngVO searchVO, Model model) throws Exception {

		return "/system/system/comp/compMenuMng/popRoleList." + searchVO.getLayout();
	}

	/**
	 * 메뉴관리 그리드 조회(json)
	 * @param searchVO
	 * @return	ModelAndView
	 * @throws Exception
	 */
	@RequestMapping(value="/system/system/comp/compMenuMng/popRoleList_json.do")
	public ModelAndView popRoleList_json(@ModelAttribute("searchVO") CompMenuMngVO searchVO)throws Exception {

		List<CompMenuMngVO> dataList = compMenuMngService.selectRoleList(searchVO);
		return makeGridJsonData(dataList, dataList.size(), searchVO);
	}

	/**
	 * 메뉴관리 저장
	 * @param dataVO
	 * @param model
	 * @param bindingResult
	 * @return	ModelAndView
	 * @throws Exception
	 */
	@RequestMapping("/system/system/comp/compMenuMng/saveCompMenuMng.do")
	public ModelAndView saveMenuMng(@ModelAttribute("dataVO") CompMenuMngVO dataVO, Model model, BindingResult bindingResult) throws Exception {

		validateList(dataVO.getGridDataList(),bindingResult);

		if(bindingResult.hasErrors()){
			return makeFailJsonData(getListErrorMsg(bindingResult));
		}

		int resultCnt = compMenuMngService.saveData(dataVO);
		if(resultCnt == 0) {
			return makeFailJsonData();
		}
		return makeSuccessJsonData();
	}

	/**
	 * 메뉴 도움말 팝업 화면
	 * @param	CompMenuMngVO searchVO
	 * @param	Model model
	 * @return	String
	 * @throws	Exception
	 */
	@RequestMapping(value="/system/system/comp/compMenuMng/popGuideComment.do")
	public String popGuideComment(@ModelAttribute("searchVO") CompMenuMngVO searchVO, Model model) throws Exception {
		CompMenuMngVO dataVO = compMenuMngService.selectDetail(searchVO);
		model.addAttribute("dataVO", dataVO);
		model.addAttribute("searchVO", searchVO);
		return "/system/system/comp/compMenuMng/popGuideComment." + searchVO.getLayout();
	}
	/**
	 * 메뉴 도움말 저장
	 * @param dataVO
	 * @param model
	 * @param bindingResult
	 * @return	ModelAndView
	 * @throws Exception
	 */
	@RequestMapping("/system/system/comp/compMenuMng/saveGuideComment.do")
	public ModelAndView saveGuideComment(@ModelAttribute("dataVO") CompMenuMngVO dataVO, Model model, BindingResult bindingResult) throws Exception {

		int resultCnt = compMenuMngService.saveGuideComment(dataVO);
		if(resultCnt == 0) {
			return makeFailJsonData();
		}
		return makeSuccessJsonData();
	}
}

