/*************************************************************************
* CLASS 명	: SignalMngController
* 작 업 자	: 현걸욱
* 작 업 일	: 2018-2-6
* 기	능	: 신호등관리 Controller
* ---------------------------- 변 경 이 력 --------------------------------
* 번호	작 업 자		작	업	일			변 경 내 용				비고
* ----	---------	----------------	---------------------	-----------
*	1	현걸욱		2018-2-6
**************************************************************************/
package kr.ispark.system.system.code.signalMng.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import egovframework.let.utl.fcc.service.EgovStringUtil;
import kr.ispark.system.system.code.signalMng.service.impl.SignalMngServiceImpl;
import kr.ispark.system.system.code.signalMng.service.SignalMngVO;
/*공통 컨트롤러*/
import kr.ispark.common.web.BaseController;

@Controller
public class SignalMngController extends BaseController {
	@Autowired
	private SignalMngServiceImpl signalMngService;
	
	/**
	 * 신호등관리 목록 화면
	 * @param	SignalMngVO searchVO
	 * @param	Model model
	 * @return	String
	 * @throws	Exception
	 */
	@RequestMapping(value="/system/system/code/signalMng/signalMngList.do")
	public String signalMngList(@ModelAttribute("searchVO") SignalMngVO searchVO, Model model) throws Exception {
		return "/system/system/code/signalMng/signalMngList." + searchVO.getLayout();
	}
	
	/**
	 * 신호등관리 그리드 조회(json)
	 * @param searchVO
	 * @return	ModelAndView
	 * @throws Exception
	 */
	@RequestMapping(value="/system/system/code/signalMng/signalMngList_json.do")
	public ModelAndView signalMngList_json(@ModelAttribute("searchVO") SignalMngVO searchVO)throws Exception {
		
		List<SignalMngVO> dataList = signalMngService.selectList(searchVO);
		return makeGridJsonData(dataList, dataList.size(), searchVO);
	}

	/**
	 * 신호등관리 저장
	 * @param dataVO
	 * @param model
	 * @param bindingResult
	 * @return	ModelAndView
	 * @throws Exception
	 */
	@RequestMapping("/system/system/code/signalMng/saveSignalMng.do")
	public ModelAndView saveSignalMng(@ModelAttribute("dataVO") SignalMngVO dataVO, Model model, BindingResult bindingResult) throws Exception {
		
		validateList(dataVO.getGridDataList(),bindingResult);
		if(bindingResult.hasErrors()){
			return makeFailJsonData(getListErrorMsg(bindingResult));
		}
		
		int resultCnt = signalMngService.saveData(dataVO);
		if(resultCnt == 0) {
			return makeFailJsonData();
		}
		return makeSuccessJsonData();
	}
	
	/*팝업*/
	
	/**
	 * 신호등관리 목록 화면
	 * @param	SignalMngVO searchVO
	 * @param	Model model
	 * @return	String
	 * @throws	Exception
	 */
	@RequestMapping(value="/comPop/system/system/code/signalMng/signalMngList.do")
	public String popSignalMngList(@ModelAttribute("searchVO") SignalMngVO searchVO, Model model) throws Exception {
		return "/system/system/code/signalMng/popSignalMngList." + searchVO.getLayout();
	}
	
	/**
	 * 신호등관리 그리드 조회(json)
	 * @param searchVO
	 * @return	ModelAndView
	 * @throws Exception
	 */
	@RequestMapping(value="/comPop/system/system/code/signalMng/signalMngList_json.do")
	public ModelAndView popSignalMngList_json(@ModelAttribute("searchVO") SignalMngVO searchVO)throws Exception {
		return signalMngList_json(searchVO);
	}
}

