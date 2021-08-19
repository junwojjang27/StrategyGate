/*************************************************************************
* CLASS 명	: CompMngController.java
* 작 업 자	: kimyh
* 작 업 일	: 2018. 07. 09.
* 기	능	: 회사정보 관리 Controller
* ---------------------------- 변 경 이 력 --------------------------------
* 번호	작 업 자		작	업	일			변 경 내 용				비고
* ----	---------	----------------	---------------------	-----------
*	1	kimyh		2018. 07. 09.		최 초 작 업
**************************************************************************/
package kr.ispark.system.system.comp.compMng.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import kr.ispark.common.security.service.impl.LoginServiceImpl;
import kr.ispark.common.system.service.LangVO;
import kr.ispark.common.util.CodeUtil;
import kr.ispark.common.util.SessionUtil;
import kr.ispark.common.util.service.CodeVO;
import kr.ispark.common.web.BaseController;
import kr.ispark.superMng.superMng.superMng.clientMng.service.ClientMngVO;
import kr.ispark.system.system.comp.compMng.service.impl.CompMngServiceImpl;

@Controller
public class CompMngController extends BaseController {

	@Autowired
	private CompMngServiceImpl compMngService;
	
	@Autowired
	private LoginServiceImpl loginService;
	
	/**
	 * 고객사 수정 화면
	 * @param	ClientMngVO searchVO
	 * @param	Model model
	 * @return	String
	 * @throws	Exception
	 */
	@RequestMapping("/system/system/comp/compMng/compMngForm.do")
	public String compMngForm(@ModelAttribute("searchVO") ClientMngVO searchVO, Model model) throws Exception {
		ClientMngVO dataVO = compMngService.selectDetail(searchVO);
		if(dataVO != null){
			String[] serviceIds = dataVO.getServiceType().split(",", -1);
			String serviceNm = "";
			if(serviceIds != null && serviceIds.length > 0){
				for(int i=0 ; i<serviceIds.length ; i++){
					if(serviceIds[i] != null && serviceIds[i].length() > 0){
						serviceNm += CodeUtil.getCodeName("002", serviceIds[i]);
					}
					if(i+1 != serviceIds.length){
						serviceNm += ",";
					}
					
				}
			}
			
			dataVO.setServiceTypeNm(serviceNm);
		}
		
		List<ClientMngVO> langList = compMngService.langList(searchVO);
		model.addAttribute("dataVO", dataVO);
		model.addAttribute("lang",langList);
		
		return "/system/system/comp/compMng/compMngForm." + searchVO.getLayout();
	}

	/**
	 * 저장
	 * @param	ClientMngVO dataVO
	 * @param	BindingResult bindingResult
	 * @param	Model model
	 * @return	ModelAndView
	 * @throws	Exception
	 */
	@RequestMapping("/system/system/comp/compMng/saveData.do")
	public ModelAndView saveData(@ModelAttribute("dataVO") ClientMngVO dataVO, BindingResult bindingResult, Model model) throws Exception {
		beanValidator.validate(dataVO, bindingResult);
		if(bindingResult.hasErrors()){
			return makeFailJsonData(getListErrorMsg(bindingResult));
		}
		
		int resultCnt = compMngService.updateData(dataVO);
		if(resultCnt > 0) {
			// 언어목록 조회
			List<LangVO> langList = loginService.selectLangList(SessionUtil.getUserVO());
			SessionUtil.setAttribute("langList", langList);
		}
		
		return makeJsonDataByResultCnt(resultCnt);
	}
}
