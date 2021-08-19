/*************************************************************************
* CLASS 명	: CodeGrpDeployController
* 작 업 자	: 현걸욱
* 작 업 일	: 2018-07-04
* 기	능	: 슈퍼관리자 공통코드 배포관리 Controller
* ---------------------------- 변 경 이 력 --------------------------------
* 번호	작 업 자		작	업	일			변 경 내 용				비고
* ----	---------	----------------	---------------------	-----------
*	1	현걸욱		2018-07-04
**************************************************************************/
package kr.ispark.superMng.superMng.superMng.codeGrpDeploy.web;

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
import kr.ispark.superMng.superMng.superMng.codeGrpDeploy.service.impl.CodeGrpDeployServiceImpl;
import kr.ispark.superMng.superMng.superMng.codeGrpDeploy.service.CodeGrpDeployVO;
import kr.ispark.common.util.CommonUtil;
import kr.ispark.common.util.service.impl.CodeUtilServiceImpl;
/*공통 컨트롤러*/
import kr.ispark.common.web.BaseController;

@Controller
public class CodeGrpDeployController extends BaseController {
	
	@Autowired
	private CodeGrpDeployServiceImpl codeGrpDeployService;
	
	@Autowired
	public CodeUtilServiceImpl codeUtilService;
	
	/**
	 * 공통코그룹관리 목록 화면
	 * @param	CodeGrpDeployVO searchVO
	 * @param	Model model
	 * @return	String
	 * @throws	Exception
	 */
	@RequestMapping(value="/superMng/superMng/superMng/codeGrpDeploy/codeGrpDeployList.do")
	public String codeGrpDeployList(@ModelAttribute("searchVO") CodeGrpDeployVO searchVO, Model model) throws Exception {
		
		String codeGrpId = CommonUtil.nullToBlank(searchVO.getCodeGrpId());
		String findUseYn = CommonUtil.nullToBlank(searchVO.getFindUseYn());
		if("".equals(findUseYn)) searchVO.setFindUseYn("Y");
		
		if("".equals(codeGrpId)){
			List<CodeGrpDeployVO> dataList = codeGrpDeployService.selectList(searchVO);
			if(dataList != null && dataList.size() > 0){
				CodeGrpDeployVO vo = (CodeGrpDeployVO)dataList.get(0);
				searchVO.setCodeGrpId(vo.getCodeGrpId());
				searchVO.setCodeGrpNm(vo.getCodeGrpNm());
				searchVO.setCodeDefId(vo.getCodeDefId());
				searchVO.setYearYn(vo.getYearYn());
			}
		}
		
		List<CodeGrpDeployVO> langList = codeGrpDeployService.selectLangList(searchVO);
		model.addAttribute("langList", langList);
		
		return "/superMng/superMng/superMng/codeGrpDeploy/codeGrpDeployList." + searchVO.getLayout();
	}
	
	/**
	 * 공통코그룹관리 그리드 조회(json)
	 * @param searchVO
	 * @return	ModelAndView
	 * @throws Exception
	 */
	@RequestMapping(value="/superMng/superMng/superMng/codeGrpDeploy/codeGrpDeployList_json.do")
	public ModelAndView codeGrpDeployList_json(@ModelAttribute("searchVO") CodeGrpDeployVO searchVO)throws Exception {
		
		List<CodeGrpDeployVO> dataList = codeGrpDeployService.selectList(searchVO);
		
		return makeGridJsonData(dataList, dataList.size(), searchVO);
	}
	
	/**
	 * 공통코그룹관리 그리드 조회(json)
	 * @param searchVO
	 * @return	ModelAndView
	 * @throws Exception
	 */
	@RequestMapping(value="/superMng/superMng/superMng/codeGrpDeploy/codeDeployList_json.do")
	public ModelAndView codeDeployList_json(@ModelAttribute("searchVO") CodeGrpDeployVO searchVO)throws Exception {
		
		List<CodeGrpDeployVO> dataList = codeGrpDeployService.selectCodeList(searchVO);
		return makeGridJsonData(dataList, dataList.size(), searchVO);
	}
	
	/**
	 * 공통코그룹관리 목록 화면
	 * @param	CodeGrpDeployVO searchVO
	 * @param	Model model
	 * @return	String
	 * @throws	Exception
	 */
	@RequestMapping(value="/superMng/superMng/superMng/codeGrpDeploy/popCodeGrpDeployList.do")
	public String popCodeGrpDeployList(@ModelAttribute("searchVO") CodeGrpDeployVO searchVO, Model model) throws Exception {
		
		return "/superMng/superMng/superMng/codeGrpDeploy/popCodeGrpDeployList." + searchVO.getLayout();
	}
	
	/**
	 * 공통코그룹관리 그리드 조회(json)
	 * @return	ModelAndView
	 * @param searchVO
	 * @throws Exception
	 */
	@RequestMapping(value="/superMng/superMng/superMng/codeGrpDeploy/popCodeGrpDeployList_json.do")
	public ModelAndView popCodeGrpDeployList_json(@ModelAttribute("searchVO") CodeGrpDeployVO searchVO)throws Exception {
		
		List<CodeGrpDeployVO> dataList = codeGrpDeployService.selectDeployCompList(searchVO);
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
	@RequestMapping("/superMng/superMng/superMng/codeGrpDeploy/deleteCodeGrpDeploy.do")
	public ModelAndView deleteCodeGrpDeploy(@ModelAttribute("dataVO") CodeGrpDeployVO dataVO, Model model) throws Exception {
		int resultCnt = codeGrpDeployService.deleteCodeGrp(dataVO);
		if(resultCnt == 0) {
			return makeFailJsonData();
		}
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
	@RequestMapping("/superMng/superMng/superMng/codeGrpDeploy/deleteCodeDeploy.do")
	public ModelAndView deleteCodeDeploy(@ModelAttribute("dataVO") CodeGrpDeployVO dataVO, Model model) throws Exception {
		
		int resultCnt = codeGrpDeployService.deleteCode(dataVO);
		if(resultCnt == 0) {
			return makeFailJsonData();
		}
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
	@RequestMapping("/superMng/superMng/superMng/codeGrpDeploy/saveCodeGrpDeploy.do")
	public ModelAndView saveCodeGrpDeploy(@ModelAttribute("dataVO") CodeGrpDeployVO dataVO, Model model, BindingResult bindingResult) throws Exception {
		
		//spring validation check
		validateList(dataVO.getGridDataList(),bindingResult);
		if(bindingResult.hasErrors()){
			return makeFailJsonData(getListErrorMsg(bindingResult));
		}
		
		//수동체크
		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		boolean isOk = true;
		for(CodeGrpDeployVO vo : dataVO.getGridDataList()){
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
		
		int resultCnt = codeGrpDeployService.saveData(dataVO);
		
		codeUtilService.selectUpdateCompIdList();
		
		if(resultCnt == 0) {
			return makeFailJsonData();
		}
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
	@RequestMapping("/superMng/superMng/superMng/codeGrpDeploy/saveCodeDeploy.do")
	public ModelAndView saveCodeDeploy(@ModelAttribute("dataVO") CodeGrpDeployVO dataVO, Model model, BindingResult bindingResult) throws Exception {
		
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
		for(CodeGrpDeployVO vo : dataVO.getGridDataList()){
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
		
		int resultCnt = codeGrpDeployService.saveCodeData(dataVO);
		
		codeUtilService.selectUpdateCompIdList();
		
		if(resultCnt == 0) {
			return makeFailJsonData();
		}
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
	@RequestMapping("/superMng/superMng/superMng/codeGrpDeploy/updateDeploy.do")
	public ModelAndView updateDeploy(@ModelAttribute("dataVO") CodeGrpDeployVO dataVO, Model model) throws Exception {
		
		int resultCnt = codeGrpDeployService.updateDeploy(dataVO);
		if(resultCnt == 0) {
			return makeFailJsonData();
		}
		
		codeUtilService.selectUpdateCompIdList();
		
		return makeSuccessJsonData();
	}
}

