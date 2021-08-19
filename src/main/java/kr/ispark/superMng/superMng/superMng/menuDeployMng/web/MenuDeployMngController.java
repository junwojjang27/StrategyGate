/*************************************************************************
* CLASS 명	: MenuDeployMngController
* 작 업 자	: 현걸욱
* 작 업 일	: 2018-07-06
* 기	능	: 메뉴배포관리 Controller
* ---------------------------- 변 경 이 력 --------------------------------
* 번호	작 업 자		작	업	일			변 경 내 용				비고
* ----	---------	----------------	---------------------	-----------
*	1	현걸욱		2018-07-06
**************************************************************************/
package kr.ispark.superMng.superMng.superMng.menuDeployMng.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import egovframework.let.utl.fcc.service.EgovStringUtil;
import kr.ispark.superMng.superMng.superMng.menuDeployMng.service.impl.MenuDeployMngServiceImpl;
import kr.ispark.system.system.menu.menuMng.service.MenuMngVO;
import kr.ispark.superMng.superMng.superMng.menuDeployMng.service.MenuDeployMngVO;
/*공통 컨트롤러*/
import kr.ispark.common.web.BaseController;

@Controller
public class MenuDeployMngController extends BaseController {
	@Autowired
	private MenuDeployMngServiceImpl menuDeployMngService;
	
	/**
	 * 메뉴배포관리 목록 화면
	 * @param	MenuDeployMngVO searchVO
	 * @param	Model model
	 * @return	String
	 * @throws	Exception
	 */
	@RequestMapping("/superMng/superMng/superMng/menuDeployMng/menuDeployMngList.do")
	public String menuDeployMngList(@ModelAttribute("searchVO") MenuDeployMngVO searchVO, Model model) throws Exception {
		
		return "/superMng/superMng/superMng/menuDeployMng/menuDeployMngList." + searchVO.getLayout();
		
	}
	
	/**
	 * 메뉴배포관리 그리드 조회(json)
	 * @param	MenuDeployMngVO searchVO
	 * @return	ModelAndView
	 * @throws	Exception
	 */
	@RequestMapping("/superMng/superMng/superMng/menuDeployMng/menuDeployMngList_json.do")
	public ModelAndView menuDeployMngList_json(@ModelAttribute("searchVO") MenuDeployMngVO searchVO) throws Exception {
		List<MenuDeployMngVO> dataList = menuDeployMngService.selectDeployList(searchVO);
		return makeGridJsonData(dataList, dataList.size(), searchVO);
	}
	
	/**
	 * 메뉴배포관리 목록 화면
	 * @param	MenuDeployMngVO searchVO
	 * @param	Model model
	 * @return	String
	 * @throws	Exception
	 */
	@RequestMapping("/superMng/superMng/superMng/menuDeployMng/popMenuDeployList.do")
	public String popMenuDeployList(@ModelAttribute("searchVO") MenuDeployMngVO searchVO, Model model) throws Exception {
		
		return "/superMng/superMng/superMng/menuDeployMng/popMenuDeployList." + searchVO.getLayout();
		
	}
	
	/**
	 * 메뉴배포관리 그리드 조회(json)
	 * @param	MenuDeployMngVO searchVO
	 * @return	ModelAndView
	 * @throws	Exception
	 */
	@RequestMapping("/superMng/superMng/superMng/menuDeployMng/popMenuDeployList_json.do")
	public ModelAndView popMenuDeployList_json(@ModelAttribute("searchVO") MenuDeployMngVO searchVO) throws Exception {
		List<MenuDeployMngVO> dataList = menuDeployMngService.selectDeployCompList(searchVO);
		return makeGridJsonData(dataList, dataList.size(), searchVO);
	}

	/**
	 * 메뉴배포관리 저장
	 * @param	MenuDeployMngVO dataVO
	 * @param	Model model
	 * @param	BindingResult bindingResult
	 * @return	ModelAndView
	 * @throws	Exception
	 */
	@RequestMapping("/superMng/superMng/superMng/menuDeployMng/saveMenuDeployMng.do")
	public ModelAndView saveMenuDeployMng(@ModelAttribute("dataVO") MenuDeployMngVO dataVO, Model model, BindingResult bindingResult) throws Exception {
		
		/*
		validateList(dataVO.getGridDataList(), bindingResult);
		if(bindingResult.hasErrors()){
			return makeFailJsonData(getListErrorMsg(bindingResult));
		}
		*/

		return makeJsonDataByResultCnt(menuDeployMngService.saveMenuDeployMng(dataVO));
	}
	
	/**
	 * 메뉴배포관리 저장
	 * @param	MenuDeployMngVO dataVO
	 * @param	Model model
	 * @param	BindingResult bindingResult
	 * @return	ModelAndView
	 * @throws	Exception
	 */
	@RequestMapping("/superMng/superMng/superMng/menuDeployMng/updateDeploy.do")
	public ModelAndView updateDeploy(@ModelAttribute("dataVO") MenuDeployMngVO dataVO, Model model, BindingResult bindingResult) throws Exception {
		
		/*
		beanValidator.validate(dataVO,bindingResult);
		if(bindingResult.hasErrors()){
			return makeFailJsonData(getListErrorMsg(bindingResult));
		}
		*/
		
		return makeJsonDataByResultCnt(menuDeployMngService.updateDeploy(dataVO));
	}
	
	/*메유 등록 화면 시작~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
	
	/**
	 * 메뉴관리 목록 화면
	 * @param	MenuMngVO searchVO
	 * @param	Model model
	 * @return	String
	 * @throws	Exception
	 */
	@RequestMapping(value="/superMng/superMng/superMng/menuDeployMng/menuDeployList.do")
	public String menuDeployList(@ModelAttribute("searchVO") MenuDeployMngVO searchVO, Model model) throws Exception {
		
		List<MenuDeployMngVO> langList = menuDeployMngService.selectLangList(searchVO);
		model.addAttribute("langList", langList);
		
		return "/superMng/superMng/superMng/menuDeployMng/menuDeployList." + searchVO.getLayout();
	}
	
	/**
	 * 메뉴관리 그리드 조회(json)
	 * @param searchVO
	 * @return	ModelAndView
	 * @throws Exception
	 */
	@RequestMapping(value="/superMng/superMng/superMng/menuDeployMng/menuDeployList_json.do")
	public ModelAndView menuDeployList_json(@ModelAttribute("searchVO") MenuDeployMngVO searchVO)throws Exception {
		
		List<MenuDeployMngVO> dataList = menuDeployMngService.selectList(searchVO);
		return makeGridJsonData(dataList, dataList.size(), searchVO);
	}
	
	/**
	 * 메뉴관리 목록 화면
	 * @param	MenuMngVO searchVO
	 * @param	Model model
	 * @return	String
	 * @throws	Exception
	 */
	@RequestMapping(value="/superMng/superMng/superMng/menuDeployMng/popRoleList.do")
	public String popRoleList(@ModelAttribute("searchVO") MenuDeployMngVO searchVO, Model model) throws Exception {
		
		return "/superMng/superMng/superMng/menuDeployMng/popRoleList." + searchVO.getLayout();
	}
	
	/**
	 * 메뉴관리 그리드 조회(json)
	 * @param searchVO
	 * @return	ModelAndView
	 * @throws Exception
	 */
	@RequestMapping(value="/superMng/superMng/superMng/menuDeployMng/popRoleList_json.do")
	public ModelAndView popRoleList_json(@ModelAttribute("searchVO") MenuDeployMngVO searchVO)throws Exception {
		
		List<MenuDeployMngVO> dataList = menuDeployMngService.selectRoleList(searchVO);
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
	@RequestMapping("/superMng/superMng/superMng/menuDeployMng/saveMenuDeploy.do")
	public ModelAndView saveMenuMng(@ModelAttribute("dataVO") MenuDeployMngVO dataVO, Model model, BindingResult bindingResult) throws Exception {
		
		validateList(dataVO.getGridDataList(),bindingResult);
		
		if(bindingResult.hasErrors()){
			return makeFailJsonData(getListErrorMsg(bindingResult));
		}

		int resultCnt = menuDeployMngService.saveData(dataVO);
		if(resultCnt == 0) {
			return makeFailJsonData();
		}
		return makeSuccessJsonData();
	}
	

}

