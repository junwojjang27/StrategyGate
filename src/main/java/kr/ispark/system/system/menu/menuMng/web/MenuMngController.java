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
package kr.ispark.system.system.menu.menuMng.web;

import java.util.List;

/*공통 컨트롤러*/
import kr.ispark.common.web.BaseController;
import kr.ispark.system.system.menu.menuMng.service.MenuMngVO;
import kr.ispark.system.system.menu.menuMng.service.impl.MenuMngServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class MenuMngController extends BaseController {
	@Autowired
	private MenuMngServiceImpl menuMngService;
	
	/**
	 * 메뉴관리 목록 화면
	 * @param	MenuMngVO searchVO
	 * @param	Model model
	 * @return	String
	 * @throws	Exception
	 */
	@RequestMapping(value="/system/system/menu/menuMng/menuMngList.do")
	public String menuMngList(@ModelAttribute("searchVO") MenuMngVO searchVO, Model model) throws Exception {
		
		List<MenuMngVO> langList = menuMngService.selectLangList(searchVO);
		model.addAttribute("langList", langList);
		
		return "/system/system/menu/menuMng/menuMngList." + searchVO.getLayout();
	}
	
	/**
	 * 메뉴관리 그리드 조회(json)
	 * @param searchVO
	 * @return	ModelAndView
	 * @throws Exception
	 */
	@RequestMapping(value="/system/system/menu/menuMng/menuMngList_json.do")
	public ModelAndView menuMngList_json(@ModelAttribute("searchVO") MenuMngVO searchVO)throws Exception {
		
		List<MenuMngVO> dataList = menuMngService.selectList(searchVO);
		return makeGridJsonData(dataList, dataList.size(), searchVO);
	}
	
	/**
	 * 메뉴관리 목록 화면
	 * @param	MenuMngVO searchVO
	 * @param	Model model
	 * @return	String
	 * @throws	Exception
	 */
	@RequestMapping(value="/system/system/menu/menuMng/popRoleList.do")
	public String popRoleList(@ModelAttribute("searchVO") MenuMngVO searchVO, Model model) throws Exception {
		
		return "/system/system/menu/menuMng/popRoleList." + searchVO.getLayout();
	}
	
	/**
	 * 메뉴관리 그리드 조회(json)
	 * @param searchVO
	 * @return	ModelAndView
	 * @throws Exception
	 */
	@RequestMapping(value="/system/system/menu/menuMng/popRoleList_json.do")
	public ModelAndView popRoleList_json(@ModelAttribute("searchVO") MenuMngVO searchVO)throws Exception {
		
		List<MenuMngVO> dataList = menuMngService.selectRoleList(searchVO);
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
	@RequestMapping("/system/system/menu/menuMng/saveMenuMng.do")
	public ModelAndView saveMenuMng(@ModelAttribute("dataVO") MenuMngVO dataVO, Model model, BindingResult bindingResult) throws Exception {
		
		validateList(dataVO.getGridDataList(),bindingResult);
		
		if(bindingResult.hasErrors()){
			return makeFailJsonData(getListErrorMsg(bindingResult));
		}

		int resultCnt = menuMngService.saveData(dataVO);
		if(resultCnt == 0) {
			return makeFailJsonData();
		}
		return makeSuccessJsonData();
	}
	
	/**
	 * 메뉴 도움말 팝업 화면
	 * @param	MenuMngVO searchVO
	 * @param	Model model
	 * @return	String
	 * @throws	Exception
	 */
	@RequestMapping(value="/system/system/menu/menuMng/popGuideComment.do")
	public String popGuideComment(@ModelAttribute("searchVO") MenuMngVO searchVO, Model model) throws Exception {
		MenuMngVO dataVO = menuMngService.selectDetail(searchVO);
		model.addAttribute("dataVO", dataVO);
		model.addAttribute("searchVO", searchVO);
		return "/system/system/menu/menuMng/popGuideComment." + searchVO.getLayout();
	}
	/**
	 * 메뉴 도움말 저장
	 * @param dataVO
	 * @param model
	 * @param bindingResult
	 * @return	ModelAndView
	 * @throws Exception
	 */
	@RequestMapping("/system/system/menu/menuMng/saveGuideComment.do")
	public ModelAndView saveGuideComment(@ModelAttribute("dataVO") MenuMngVO dataVO, Model model, BindingResult bindingResult) throws Exception {
		
		int resultCnt = menuMngService.saveGuideComment(dataVO);
		if(resultCnt == 0) {
			return makeFailJsonData();
		}
		return makeSuccessJsonData();
	}
}

