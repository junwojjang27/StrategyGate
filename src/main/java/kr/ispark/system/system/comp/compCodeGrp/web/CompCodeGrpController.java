/*************************************************************************
* CLASS 명	: CodeGrpController
* 작 업 자	: 현걸욱
* 작 업 일	: 2018-1-26
* 기	능	: 공통코그룹관리 Controller
* ---------------------------- 변 경 이 력 --------------------------------
* 번호	작 업 자		작	업	일			변 경 내 용				비고
* ----	---------	----------------	---------------------	-----------
*	1	현걸욱		2018-1-26
**************************************************************************/
package kr.ispark.system.system.comp.compCodeGrp.web;

import java.util.ArrayList;
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

import kr.ispark.common.util.CommonUtil;
import kr.ispark.common.util.service.impl.CodeUtilServiceImpl;
/*공통 컨트롤러*/
import kr.ispark.common.web.BaseController;
import kr.ispark.system.system.comp.compCodeGrp.service.CompCodeGrpVO;
import kr.ispark.system.system.comp.compCodeGrp.service.impl.CompCodeGrpServiceImpl;

@Controller
public class CompCodeGrpController extends BaseController {
	
	@Autowired
	private CompCodeGrpServiceImpl compCodeGrpService;
	
	@Autowired
	public CodeUtilServiceImpl codeUtilService;
	
	/**
	 * 공통코그룹관리 목록 화면
	 * @param	CompCodeGrpVO searchVO
	 * @param	Model model
	 * @return	String
	 * @throws	Exception
	 */
	@RequestMapping(value="/system/system/comp/compCodeGrp/compCodeGrpList.do")
	public String codeGrpList(@ModelAttribute("searchVO") CompCodeGrpVO searchVO, Model model) throws Exception {
		
		String codeGrpId = CommonUtil.nullToBlank(searchVO.getCodeGrpId());
		String findUseYn = CommonUtil.nullToBlank(searchVO.getFindUseYn());
		if("".equals(findUseYn)) searchVO.setFindUseYn("Y");
		
		if("".equals(codeGrpId)){
			List<CompCodeGrpVO> dataList = compCodeGrpService.selectList(searchVO);
			if(dataList != null && dataList.size() > 0){
				CompCodeGrpVO vo = (CompCodeGrpVO)dataList.get(0);
				searchVO.setCodeGrpId(vo.getCodeGrpId());
				searchVO.setCodeGrpNm(vo.getCodeGrpNm());
				searchVO.setCodeDefId(vo.getCodeDefId());
				searchVO.setYearYn(vo.getYearYn());
			}
		}
		
		List<CompCodeGrpVO> langList = compCodeGrpService.selectLangList(searchVO);
		model.addAttribute("langList", langList);
		
		return "/system/system/comp/compCodeGrp/compCodeGrpList." + searchVO.getLayout();
	}
	
	/**
	 * 공통코그룹관리 그리드 조회(json)
	 * @param searchVO
	 * @return	ModelAndView
	 * @throws Exception
	 */
	@RequestMapping(value="/system/system/comp/compCodeGrp/compCodeGrpList_json.do")
	public ModelAndView codeGrpList_json(@ModelAttribute("searchVO") CompCodeGrpVO searchVO)throws Exception {
		
		List<CompCodeGrpVO> dataList = compCodeGrpService.selectList(searchVO);
		
		return makeGridJsonData(dataList, dataList.size(), searchVO);
	}
	
	/**
	 * 공통코그룹관리 그리드 조회(json)
	 * @param searchVO
	 * @return	ModelAndView
	 * @throws Exception
	 */
	@RequestMapping(value="/system/system/comp/compCodeGrp/compCodeList_json.do")
	public ModelAndView codeList_json(@ModelAttribute("searchVO") CompCodeGrpVO searchVO)throws Exception {
		
		List<CompCodeGrpVO> dataList = compCodeGrpService.selectCodeList(searchVO);
		return makeGridJsonData(dataList, dataList.size(), searchVO);
	}
	
	/**
	 * 공통코그룹관리 삭제
	 * @param dataVO
	 * @param model
	 * @param bindingResult
	 * @return	ModelAndView
	 * @throws Exception
	 */
	@RequestMapping("/system/system/comp/compCodeGrp/deletecCompCodeGrp.do")
	public ModelAndView deleteCodeGrp(@ModelAttribute("dataVO") CompCodeGrpVO dataVO, Model model) throws Exception {
		int resultCnt = compCodeGrpService.deleteCodeGrp(dataVO);
		if(resultCnt == 0) {
			return makeFailJsonData();
		}
		
		//codeUtilService.selectUpdateCompIdList();
		
		return makeSuccessJsonData();
	}
	
	/**
	 * 공통코그룹관리 삭제
	 * @param dataVO
	 * @param model
	 * @param bindingResult
	 * @return	ModelAndView
	 * @throws Exception
	 */
	@RequestMapping("/system/system/comp/compCodeGrp/deleteCompCode.do")
	public ModelAndView deleteCode(@ModelAttribute("dataVO") CompCodeGrpVO dataVO, Model model) throws Exception {
		
		int resultCnt = compCodeGrpService.deleteCode(dataVO);
		if(resultCnt == 0) {
			return makeFailJsonData();
		}
		
		//codeUtilService.selectUpdateCompIdList();
		
		return makeSuccessJsonData();
	}
	
	/**
	 * 공통코그룹관리 저장
	 * @param dataVO
	 * @param model
	 * @param bindingResult
	 * @return	ModelAndView
	 * @throws Exception
	 */
	@RequestMapping("/system/system/comp/compCodeGrp/saveCompCodeGrp.do")
	public ModelAndView saveCodeGrp(@ModelAttribute("dataVO") CompCodeGrpVO dataVO, Model model, BindingResult bindingResult) throws Exception {
		
		//spring validation check
		validateList(dataVO.getGridDataList(),bindingResult);
		if(bindingResult.hasErrors()){
			return makeFailJsonData(getListErrorMsg(bindingResult));
		}
		
		//수동체크
		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		boolean isOk = true;
		for(CompCodeGrpVO vo : dataVO.getGridDataList()){
			if("".equals(CommonUtil.nullToBlank(vo.getCodeGrpNmLang()))){
				isOk = false;
				resultMap.put("result", AJAX_FAIL);
				resultMap.put("msg", egovMessageSource.getMessage("common.required.msg", new String[] {egovMessageSource.getMessage("word.codeId")}));
				break;
			}
		}
		if(!isOk){
			return new ModelAndView("jsonView",resultMap);
		}
		
		int resultCnt = compCodeGrpService.saveData(dataVO);
		if(resultCnt == 0) {
			return makeFailJsonData();
		}
		
		//codeUtilService.selectUpdateCompIdList();
		
		return makeSuccessJsonData();
	}
	
	/**
	 * 공통코그룹관리 저장
	 * @param dataVO
	 * @param model
	 * @param bindingResult
	 * @return	ModelAndView
	 * @throws Exception
	 */
	@RequestMapping("/system/system/comp/compCodeGrp/saveCompCode.do")
	public ModelAndView saveCode(@ModelAttribute("dataVO") CompCodeGrpVO dataVO, Model model, BindingResult bindingResult) throws Exception {
		
		//spring validation check
		validateList(dataVO.getGridDataList(),bindingResult);
		if(bindingResult.hasErrors()){
			return makeFailJsonData(getListErrorMsg(bindingResult));
		}
		
		//수동체크
		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		boolean isOk = true;
		ArrayList<String> codeIds = new ArrayList<String>();
		int i=0;
		for(CompCodeGrpVO vo : dataVO.getGridDataList()){
			if("".equals(CommonUtil.nullToBlank(vo.getCodeNmLang()))){
				isOk = false;
				resultMap.put("result", AJAX_FAIL);
				resultMap.put("msg", egovMessageSource.getMessage("common.required.msg", new String[] {egovMessageSource.getMessage("word.codeId")}));
				break;
			}
			codeIds.add(vo.getCodeId());
			i++;
		}
		
		//codeId 중복체크
		int chk;
		if(isOk && codeIds != null && codeIds.size() > 0){
			for(String codeId : codeIds){
				if(!"".equals(CommonUtil.nullToBlank(codeId))){
					chk = 0;
					for(String codeCompareId : codeIds){
						if(!"".equals(CommonUtil.nullToBlank(codeCompareId)) && codeId.equals(codeCompareId)){
							chk++;
							if(chk > 1){
								isOk = false;
								resultMap.put("result", AJAX_FAIL);
								resultMap.put("msg", egovMessageSource.getMessage("common.required.msg", new String[] {egovMessageSource.getMessage("info.dupdata.msg")}));
								break;
							}
						}
					}
				}
			}
		}
		
		if(!isOk){
			return new ModelAndView("jsonView",resultMap);
		}
		
		int resultCnt = compCodeGrpService.saveCodeData(dataVO);
		if(resultCnt == 0) {
			return makeFailJsonData();
		}
		
		//codeUtilService.selectUpdateCompIdList();
		
		return makeSuccessJsonData();
	}
}

