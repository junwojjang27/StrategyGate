/*************************************************************************
* CLASS 명	: SystemSettingController
* 작 업 자	: 현걸욱
* 작 업 일	: 2018-11-01
* 기	능	: 시스템설정 Controller
* ---------------------------- 변 경 이 력 --------------------------------
* 번호	작 업 자		작	업	일			변 경 내 용				비고
* ----	---------	----------------	---------------------	-----------
*	1	현걸욱		2018-11-01
**************************************************************************/
package kr.ispark.system.system.comp.systemSetting.web;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import egovframework.let.utl.fcc.service.EgovStringUtil;
import kr.ispark.system.system.comp.systemSetting.service.impl.SystemSettingServiceImpl;
import kr.ispark.system.system.comp.systemSetting.service.SystemSettingVO;
import kr.ispark.bsc.base.strategy.strategy.service.StrategyVO;
import kr.ispark.common.util.CodeUtil;
import kr.ispark.common.util.service.CodeVO;
/*공통 컨트롤러*/
import kr.ispark.common.web.BaseController;

@Controller
public class SystemSettingController extends BaseController {
	@Autowired
	private SystemSettingServiceImpl systemSettingService;
	
	/**
	  * 시스템설정 조회
	 * @param	SystemSettingVO searchVO
	 * @return	String
	 * @throws	Exception
	 */
	@RequestMapping(value="/system/system/comp/systemSetting/systemSettingList.do")
	public String systemSettingList(@ModelAttribute("searchVO") SystemSettingVO searchVO, Model model) throws Exception {
		
		return "/system/system/comp/systemSetting/systemSettingList." + searchVO.getLayout();
	}
	
	/**
	 * 시스템설정 조회
	 * @param	SystemSettingVO searchVO
	 * @return	ModelAndView
	 * @throws	Exception
	 */
	@RequestMapping("/system/system/comp/systemSetting/selectDetail.do")
	public ModelAndView selectDetail(@ModelAttribute("searchVO") SystemSettingVO searchVO) throws Exception {
		
		SystemSettingVO approve = systemSettingService.selectApproveDetail(searchVO);
		SystemSettingVO score = systemSettingService.selectScoreDetail(searchVO);
		
		HashMap<String,Object> resultMap = new HashMap<String,Object>(); 
		if(approve != null && score != null){
			resultMap.put("approve", approve);
			resultMap.put("score", score);
		}
		
		return new ModelAndView("jsonView", resultMap);
	}
	
	/**
	 * 시스템설정 저장
	 * @param	SystemSettingVO dataVO
	 * @param	Model model
	 * @param	BindingResult bindingResult
	 * @return	ModelAndView
	 * @throws	Exception
	 */
	@RequestMapping("/system/system/comp/systemSetting/saveSystemSetting.do")
	public ModelAndView saveSystemSetting(@ModelAttribute("dataVO") SystemSettingVO dataVO, Model model, BindingResult bindingResult) throws Exception {
		beanValidator.validate(dataVO, bindingResult);
		if(bindingResult.hasErrors()){
			return makeFailJsonData(getListErrorMsg(bindingResult));
		}

		return makeJsonDataByResultCnt(systemSettingService.saveData(dataVO));
	}
}

