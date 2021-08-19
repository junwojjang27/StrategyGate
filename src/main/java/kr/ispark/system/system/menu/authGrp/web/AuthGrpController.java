/*************************************************************************
* CLASS 명	: AuthGrpController
* 작 업 자	: joey
* 작 업 일	: 2018-1-12
* 기	능	: 그룹별권한 Controller
* ---------------------------- 변 경 이 력 --------------------------------
* 번호	작 업 자		작	업	일			변 경 내 용				비고
* ----	---------	----------------	---------------------	-----------
*	1	joey		2018-1-12
**************************************************************************/
package kr.ispark.system.system.menu.authGrp.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import egovframework.let.utl.fcc.service.EgovStringUtil;
import kr.ispark.system.system.menu.authGrp.service.impl.AuthGrpServiceImpl;
import kr.ispark.system.system.menu.authGrp.service.AuthGrpVO;
/*공통 컨트롤러*/
import kr.ispark.common.web.BaseController;

@Controller
public class AuthGrpController extends BaseController {
	@Autowired
	private AuthGrpServiceImpl authGrpService;
	
	/**
	 * 그룹별권한 목록 화면
	 * @param	AuthGrpVO searchVO
	 * @param	Model model
	 * @return	String
	 * @throws	Exception
	 */
	@RequestMapping(value="/system/system/menu/authGrp/authGrpList.do")
	public String authGrpList(@ModelAttribute("searchVO") AuthGrpVO searchVO, Model model) throws Exception {
		return "/system/system/menu/authGrp/authGrpList." + searchVO.getLayout();
	}
	
	/**
	 * 그룹별권한 그리드 조회(json)
	 * @param searchVO
	 * @return	ModelAndView
	 * @throws Exception
	 */
	@RequestMapping(value="/system/system/menu/authGrp/authGrpList_json.do")
	public ModelAndView authGrpList_json(@ModelAttribute("searchVO") AuthGrpVO searchVO)throws Exception {
		
		List<AuthGrpVO> dataList = authGrpService.selectList(searchVO);
		return makeGridJsonData(dataList, dataList.size(), searchVO);
	}
	
	/**
	 * 그룹별권한 그리드 조회(json)
	 * @param searchVO
	 * @return	ModelAndView
	 * @throws Exception
	 */
	@RequestMapping(value="/system/system/menu/authGrp/menuList_json.do")
	public ModelAndView menuList_json(@ModelAttribute("searchVO") AuthGrpVO searchVO)throws Exception {
		
		List<AuthGrpVO> dataList = authGrpService.selectMenuList(searchVO);
		return makeJsonListData(dataList);
	}
	
	/**
	 * 그룹별권한 그리드 조회(json)
	 * @param searchVO
	 * @return	ModelAndView
	 * @throws Exception
	 */
	@RequestMapping(value="/system/system/menu/authGrp/menuSelectedList_json.do")
	public ModelAndView menuSelectedList_json(@ModelAttribute("searchVO") AuthGrpVO searchVO)throws Exception {
		
		List<AuthGrpVO> dataList = authGrpService.selectMenuSelectedList(searchVO);
		return makeJsonListData(dataList);
	}
	
	//excelDownload
	/**
	 * 엑셀양식다운로드
	 */
	@RequestMapping("/system/system/menu/authGrp/excelDownload.do")
	public String excelDownload(@ModelAttribute("searchVO") AuthGrpVO searchVO,
			Model model)throws Exception {
		
		/*
		 * 현재페이지 화면에 맞게 반드시 수정할 것.
		 */
		
		List<AuthGrpVO> dataList = authGrpService.selectList(searchVO);
		
		// 타이틀
		//model.addAttribute("title", egovMessageSource.getMessage("word.authGrpManage"));
		
		// 검색조건
		model.addAttribute("year", egovMessageSource.getMessage("word.year"));
		model.addAttribute("findYear", searchVO.getFindYear());
		
		// header
		//model.addAttribute("authGrpNm", egovMessageSource.getMessage("word.authGrpNm"));	// 그룹별권한
		model.addAttribute("sortOrder", egovMessageSource.getMessage("word.sortOrder"));			// 정렬순서
		
		// 조직 데이터
		model.addAttribute("dataList", dataList);
		
		// 시트명
		//model.addAttribute("sheetName", egovMessageSource.getMessage("word.authGrpNm"));
		
		//model.addAttribute("destJxlsFileName", egovMessageSource.getMessage("word.authGrpNm") + "_" + EgovStringUtil.getTimeStamp()+".xlsx");
		model.addAttribute("templateJxlsFileName", "authGrpList.xlsx");

		return "excelDownloadView";
	}

	/**
	 * 그룹별권한 조회
	 * @param searchVO
	 * @return	ModelAndView
	 * @throws Exception
	 */
	@RequestMapping(value="/system/system/menu/authGrp/selectDetail.do")
	public ModelAndView selectDetail(@ModelAttribute("searchVO") AuthGrpVO searchVO)throws Exception {
		return makeJsonData(authGrpService.selectDetail(searchVO));
	}
	
	/**
	 * 그룹별권한 정렬순서저장
	 * @param dataVO
	 * @param model
	 * @param bindingResult
	 * @return	ModelAndView
	 * @throws Exception
	 */
	@RequestMapping("/system/system/menu/authGrp/saveSortOrder.do")
	public ModelAndView saveSortOrder(@ModelAttribute("dataVO") AuthGrpVO dataVO, Model model, BindingResult bindingResult) throws Exception {
		// list 유효성 체크
		validateList(dataVO.getGridDataList(), bindingResult);
		if(bindingResult.hasErrors()) {
			return makeFailJsonData(getListErrorMsg(bindingResult));
		}
		
		int resultCnt = authGrpService.updateSortOrder(dataVO);
		if(resultCnt == 0) {
			return makeFailJsonData();
		}
		return makeSuccessJsonData();
	}
	
	/**
	 * 그룹별권한 삭제
	 * @param dataVO
	 * @param model
	 * @param bindingResult
	 * @return	ModelAndView
	 * @throws Exception
	 */
	@RequestMapping("/system/system/menu/authGrp/deleteAuthGrp.do")
	public ModelAndView deleteAuthGrp(@ModelAttribute("dataVO") AuthGrpVO dataVO, Model model) throws Exception {
		int resultCnt = authGrpService.deleteAuthGrp(dataVO);
		if(resultCnt == 0) {
			return makeFailJsonData();
		}
		return makeSuccessJsonData();
	}
	
	/**
	 * 그룹별권한 저장
	 * @param dataVO
	 * @param model
	 * @param bindingResult
	 * @return	ModelAndView
	 * @throws Exception
	 */
	@RequestMapping("/system/system/menu/authGrp/saveAuthGrp.do")
	public ModelAndView saveAuthGrp(@ModelAttribute("dataVO") AuthGrpVO dataVO, Model model, BindingResult bindingResult) throws Exception {
		//beanValidator.validate(dataVO, bindingResult);
		//if(bindingResult.hasErrors()){
		//	return makeFailJsonData(getListErrorMsg(bindingResult));
		//}

		int resultCnt = authGrpService.saveData(dataVO);
		if(resultCnt == 0) {
			return makeFailJsonData();
		}
		return makeSuccessJsonData();
	}
}

