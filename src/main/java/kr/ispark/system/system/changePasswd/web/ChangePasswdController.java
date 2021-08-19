/*************************************************************************
* CLASS 명	: ChangePasswdController
* 작 업 자	: kimyh
* 작 업 일	: 2018-07-06
* 기	능	: 패스워드 변경 Controller
* ---------------------------- 변 경 이 력 --------------------------------
* 번호	작 업 자		작	업	일			변 경 내 용				비고
* ----	---------	----------------	---------------------	-----------
*	1	kimyh		2018-07-06
**************************************************************************/
package kr.ispark.system.system.changePasswd.web;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import kr.ispark.common.util.CommonUtil;
import kr.ispark.common.util.SessionUtil;
import kr.ispark.common.web.BaseController;
import kr.ispark.system.system.comp.compUserMng.service.CompUserMngVO;
import kr.ispark.system.system.comp.compUserMng.service.impl.CompUserMngServiceImpl;

@Controller
public class ChangePasswdController extends BaseController {
	
	@Autowired
	private CompUserMngServiceImpl compUserMngService;
	
	/**
	 * 패스워드 변경 화면
	 * @param	PceEvalGrpVO searchVO
	 * @param	Model model
	 * @return	String
	 * @throws	Exception
	 */
	@RequestMapping("/system/system/changePasswd/changePasswd/changePasswd.do")
	public String pceEvalGrpList(@ModelAttribute("searchVO") CompUserMngVO searchVO, Model model) throws Exception {
		// 본인 정보
		model.addAttribute("dataVO", SessionUtil.getUserVO());
		return "/system/system/changePasswd/changePasswd." + searchVO.getLayout();
	}

	/**
	 * 패스워드 변경 처리
	 * @param	PceEvalGrpVO dataVO
	 * @param	Model model
	 * @return	ModelAndView
	 * @throws	Exception
	 */
	@RequestMapping("/system/system/changePasswd/changePasswd/updatePassword.do")
	public ModelAndView updatePassword(@ModelAttribute("dataVO") CompUserMngVO dataVO, Model model) throws Exception {
		String pw = dataVO.getPasswd();
		if(CommonUtil.isEmpty(pw)) {
			return makeFailJsonData();
		}
		
    	Pattern pattern = Pattern.compile("^(?=^.{8,20}$)(?=.*\\d)(?=.*[a-zA-Z])(?=.*[\\!@#$%\\^&*()_\\-`~,\\.<>'\";:\\\\\\|+=\\/\\?\\[\\]\\{\\}])[\\da-zA-Z\\!@#$%\\^&*()_\\-`~,\\.<>'\";:\\\\\\|+=\\/\\?\\[\\]\\{\\}]*$");
    	Matcher matcher = pattern.matcher(pw);
    	if(matcher.find()){
    		return makeJsonDataByResultCnt(compUserMngService.updatePassword(dataVO));
    	} else {
    		return makeFailJsonData(egovMessageSource.getMessage("errors.validation.input18"));
    	}
	}
}
