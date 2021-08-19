/*************************************************************************
* CLASS 명	: ClientMngController.java
* 작 업 자	: kimyh
* 작 업 일	: 2018. 07. 03.
* 기	능	: 고객사 관리 Controller
* ---------------------------- 변 경 이 력 --------------------------------
* 번호	작 업 자		작	업	일			변 경 내 용				비고
* ----	---------	----------------	---------------------	-----------
*	1	kimyh		2018. 07. 03.		최 초 작 업
**************************************************************************/
package kr.ispark.superMng.superMng.superMng.clientMng.web;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import kr.ispark.common.exception.CustomException;
import kr.ispark.common.security.service.impl.LoginServiceImpl;
import kr.ispark.common.util.CodeUtil;
import kr.ispark.common.util.CommonUtil;
import kr.ispark.common.util.MultiReloadUtil;
import kr.ispark.common.util.PropertyUtil;
import kr.ispark.common.util.SessionUtil;
import kr.ispark.common.util.service.CodeVO;
import kr.ispark.common.util.service.impl.CodeUtilServiceImpl;
import kr.ispark.common.web.BaseController;
import kr.ispark.superMng.superMng.superMng.clientMng.service.ClientMngVO;
import kr.ispark.superMng.superMng.superMng.clientMng.service.impl.ClientMngServiceImpl;

@Controller
public class ClientMngController extends BaseController {

	@Autowired
	private ClientMngServiceImpl clientMngService;

	@Autowired
	public CodeUtilServiceImpl codeUtilService;

	@Autowired
	public LoginServiceImpl loginService;

	/**
	 * 고객사 목록 화면
	 * @param	ClientMngVO searchVO
	 * @param	Model model
	 * @return	String
	 * @throws	Exception
	 */
	@RequestMapping("/superMng/superMng/superMng/clientMng/clientMngList.do")
	public String clientMngList(@ModelAttribute("searchVO") ClientMngVO searchVO,Model model) throws Exception {
		// SUPER 관리자만 접근 가능하도록 처리
		if(!SessionUtil.hasAuth(PropertyUtil.getProperty("Super.AuthCodeId"))) {
			return "forward:/error/accessDenied.do";
		}

		return "/superMng/superMng/superMng/clientMng/clientMngList." + searchVO.getLayout();
	}

	/**
	 * 고객사 목록 조회
	 * @param	ClientMngVO searchVO
	 * @param	Model model
	 * @return	ModelAndView
	 * @throws	Exception
	 */
	@RequestMapping("/superMng/superMng/superMng/clientMng/clientMngList_json.do")
	public ModelAndView clientMngList_json(@ModelAttribute("searchVO") ClientMngVO searchVO) throws Exception {
		// SUPER 관리자만 접근 가능하도록 처리
		if(!SessionUtil.hasAuth(PropertyUtil.getProperty("Super.AuthCodeId"))) {
			return null;
		}

		List<ClientMngVO> dataList = clientMngService.selectList(searchVO);
		int listCnt = clientMngService.selectListCount(searchVO);
		return makeGridJsonData(dataList, listCnt, searchVO);
	}

	/**
	 * 서비스 중지/사용 처리
	 * @param	ClientMngVO dataVO
	 * @return	ModelAndView
	 * @throws	Exception
	 */
	@RequestMapping("/superMng/superMng/superMng/clientMng/updateService.do")
	public ModelAndView updateService(@ModelAttribute("searchVO") ClientMngVO searchVO) throws Exception {
		// SUPER 관리자만 접근 가능하도록 처리
		if(!SessionUtil.hasAuth(PropertyUtil.getProperty("Super.AuthCodeId"))) {
			return makeFailJsonData(egovMessageSource.getMessage("info.noAuth.msg"));
		}

		int resultCnt = clientMngService.updateUseYn(searchVO);
		return makeJsonDataByResultCnt(resultCnt);
	}

	/**
	 * 초기화 확인 팝업
	 * @param	ClientMngVO searchVO
	 * @param	Model model
	 * @return	String
	 * @throws	Exception
	 */
	@RequestMapping("/superMng/superMng/superMng/clientMng/popUpdateReset.do")
	public String popUpdateReset(@ModelAttribute("searchVO") ClientMngVO searchVO,Model model) throws Exception {
		// SUPER 관리자만 접근 가능하도록 처리
		if(!SessionUtil.hasAuth(PropertyUtil.getProperty("Super.AuthCodeId"))) {
			return "forward:/error/accessDenied.do";
		}

		return "/superMng/superMng/superMng/clientMng/popUpdateReset." + searchVO.getLayout();
	}
	
	/**
	 * 초기화 확인 팝업
	 * @param	ClientMngVO searchVO
	 * @param	Model model
	 * @return	String
	 * @throws	Exception
	 */
	@RequestMapping("/superMng/superMng/superMng/clientMng/popDemoReset.do")
	public String popDemoReset(@ModelAttribute("searchVO") ClientMngVO searchVO,Model model) throws Exception {
		// SUPER 관리자만 접근 가능하도록 처리
		if(!SessionUtil.hasAuth(PropertyUtil.getProperty("Super.AuthCodeId"))) {
			return "forward:/error/accessDenied.do";
		}
		
		List<ClientMngVO> targetCompList = clientMngService.selectTargetCompList(searchVO);
		model.addAttribute("targetCompList", targetCompList);

		return "/superMng/superMng/superMng/clientMng/popDemoReset." + searchVO.getLayout();
	}

	/**
	 * 초기화 처리
	 * @param	ClientMngVO dataVO
	 * @return	ModelAndView
	 * @throws	Exception
	 */
	@RequestMapping("/superMng/superMng/superMng/clientMng/updateReset.do")
	public ModelAndView updateReset(@ModelAttribute("searchVO") ClientMngVO searchVO) throws Exception {
		// SUPER 관리자만 접근 가능하도록 처리
		if(!SessionUtil.hasAuth(PropertyUtil.getProperty("Super.AuthCodeId"))) {
			return makeFailJsonData(egovMessageSource.getMessage("info.noAuth.msg"));
		}

		try  {
			clientMngService.updateReset(searchVO);
			// 새로운 사용자의 코드를 생성했으므로 코드를 새로 로딩해야함.
			codeUtilService.insertCodeUpdateLog(searchVO.getNewCompId());

			//code reset

		} catch(CustomException ce) {
			return makeFailJsonData(ce.getMessage());
		} catch(Exception e) {
			log.error(e.getMessage());
			return makeFailJsonData();
		}

		codeUtilService.selectUpdateCompIdList();

		return makeSuccessJsonData();
	}
	
	/**
	 * 초기화 처리
	 * @param	ClientMngVO dataVO
	 * @return	ModelAndView
	 * @throws	Exception
	 */
	@RequestMapping("/superMng/superMng/superMng/clientMng/updateDemoReset.do")
	public ModelAndView updateDemoReset(@ModelAttribute("searchVO") ClientMngVO searchVO) throws Exception {
		// SUPER 관리자만 접근 가능하도록 처리
		if(!SessionUtil.hasAuth(PropertyUtil.getProperty("Super.AuthCodeId"))) {
			return makeFailJsonData(egovMessageSource.getMessage("info.noAuth.msg"));
		}

		try  {
			clientMngService.updateDemoReset(searchVO);
			// 새로운 사용자의 코드를 생성했으므로 코드를 새로 로딩해야함.
			//codeUtilService.insertCodeUpdateLog(searchVO.getNewCompId());

			//code reset

		} catch(CustomException ce) {
			return makeFailJsonData(ce.getMessage());
		} catch(Exception e) {
			log.error(e.getMessage());
			return makeFailJsonData();
		}

		//codeUtilService.selectUpdateCompIdList();

		return makeSuccessJsonData();
	}

	/**
	 * 고객사 등록 화면
	 * @param	ClientMngVO searchVO
	 * @param	Model model
	 * @return	String
	 * @throws	Exception
	 */
	@RequestMapping("/superMng/superMng/superMng/clientMng/clientMngForm.do")
	public String clientMngForm(@ModelAttribute("searchVO") ClientMngVO searchVO, Model model) throws Exception {
		// SUPER 관리자만 접근 가능하도록 처리
		if(!SessionUtil.hasAuth(PropertyUtil.getProperty("Super.AuthCodeId"))) {
			return "forward:/error/accessDenied.do";
		}

		List<ClientMngVO> langList = clientMngService.langList(searchVO);
		model.addAttribute("lang", langList);
		ClientMngVO dataVO = new ClientMngVO();
		dataVO.setIsNew("Y");
		model.addAttribute("dataVO", dataVO);
		return "/superMng/superMng/superMng/clientMng/clientMngForm."+searchVO.getLayout();
	}

	/**
	 * 고객사 수정 화면
	 * @param	ClientMngVO searchVO
	 * @param	Model model
	 * @return	String
	 * @throws	Exception
	 */
	@RequestMapping("/superMng/superMng/superMng/clientMng/clientMngUpdateForm.do")
	public String clientMngUpdateForm(@ModelAttribute("searchVO") ClientMngVO searchVO, Model model) throws Exception {
		// SUPER 관리자만 접근 가능하도록 처리
		if(!SessionUtil.hasAuth(PropertyUtil.getProperty("Super.AuthCodeId"))) {
			return "forward:/error/accessDenied.do";
		}

		ClientMngVO dataVO = clientMngService.selectDetail(searchVO);
		if(!"".equals(CommonUtil.nullToBlank(dataVO.getServiceType()))){
			String[] serviceTypeStrings = dataVO.getServiceType().split(",",0);
			if(serviceTypeStrings != null && serviceTypeStrings.length > 0){
				List<String> serviceTypeList = new ArrayList<String>(0);
				for(String serviceType : serviceTypeStrings){
					serviceTypeList.add(serviceType);
				}
				dataVO.setServiceTypes(serviceTypeList);
				serviceTypeList = null;
			}
			serviceTypeStrings = null;
		}

		model.addAttribute("dataVO", dataVO);
		List<ClientMngVO> langList = clientMngService.langList(searchVO);
		model.addAttribute("lang",langList);
		return "/superMng/superMng/superMng/clientMng/clientMngForm." + searchVO.getLayout();
	}

	/**
	 * ID 중복 체크
	 * @param	ClientMngVO searchVO
	 * @param	Model model
	 * @return	ModelAndView
	 * @throws	Exception
	 */
	@RequestMapping("/superMng/superMng/superMng/clientMng/idDuplicationCheck.do")
	public ModelAndView idDuplicationCheck(@ModelAttribute("searchVO") ClientMngVO searchVO) throws Exception {
		// SUPER 관리자만 접근 가능하도록 처리
		if(!SessionUtil.hasAuth(PropertyUtil.getProperty("Super.AuthCodeId"))) {
			return makeFailJsonData(egovMessageSource.getMessage("info.noAuth.msg"));
		}

		int idDupCk = clientMngService.selectIdCnt(searchVO);
		if(idDupCk > 0) {
			return makeFailJsonData(egovMessageSource.getMessage("superMng.superMng.clientMng.clientMngList.idImpossible", new String[]{searchVO.getNewCompId()}));
		} else {
			return makeSuccessJsonData(egovMessageSource.getMessage("superMng.superMng.clientMng.clientMngList.idPossible", new String[]{searchVO.getNewCompId()}));
		}
	}

	/**
	 * 고객사 이력 팝업
	 * @param	ClientMngVO searchVO
	 * @param	Model model
	 * @return	String
	 * @throws	Exception
	 */
	@RequestMapping("/superMng/superMng/superMng/clientMng/popClientHistory.do")
	public String popClientHistory(@ModelAttribute("searchVO") ClientMngVO searchVO, Model model) throws Exception {
		// SUPER 관리자만 접근 가능하도록 처리
		if(!SessionUtil.hasAuth(PropertyUtil.getProperty("Super.AuthCodeId"))) {
			return "forward:/error/accessDenied.do";
		}

		return "/superMng/superMng/superMng/clientMng/popClientHistory."+searchVO.getLayout();
	}

	/**
	 * 고객사 이력 목록 조회
	 * @param	ClientMngVO searchVO
	 * @param	Model model
	 * @return	ModelAndView
	 * @throws	Exception
	 */
	@RequestMapping("/superMng/superMng/superMng/clientMng/clientHistoryList_json.do")
	public ModelAndView clientHistoryList_json(@ModelAttribute("searchVO") ClientMngVO searchVO) throws Exception {
		// SUPER 관리자만 접근 가능하도록 처리
		if(!SessionUtil.hasAuth(PropertyUtil.getProperty("Super.AuthCodeId"))) {
			return null;
		}

		List<ClientMngVO> dataList = clientMngService.selectHistory(searchVO);

		List<CodeVO> serviceTypeList = CodeUtil.getCodeList("351", searchVO.getFindYear());

		if(dataList != null && dataList.size() > 0){
			for(ClientMngVO vo : dataList){
				String[] serviceTypeStrings = vo.getServiceType().split(",");
				String serviceType = "";
				if(serviceTypeStrings != null && serviceTypeStrings.length > 0){
					for(int i=0 ; i<serviceTypeStrings.length ; i++){

						for(CodeVO codevo : serviceTypeList){
							if(codevo.getCodeId().equals(CommonUtil.nullToBlank(serviceTypeStrings[i]))){
								serviceType += codevo.getCodeNm();
							}
						}
						if(i+1 != serviceTypeStrings.length){
							serviceType += ",";
						}

					}
				}
				vo.setServiceType(serviceType);
				serviceTypeStrings = null;
			}
		}

		return makeGridJsonData(dataList);
	}

	/**
	 * 저장
	 * @param	ClientMngVO dataVO
	 * @param	BindingResult bindingResult
	 * @param	Model model
	 * @return	ModelAndView
	 * @throws	Exception
	 */
	@RequestMapping("/superMng/superMng/superMng/clientMng/saveData.do")
	public ModelAndView saveData(@ModelAttribute("dataVO") ClientMngVO dataVO, BindingResult bindingResult, Model model) throws Exception {
		// SUPER 관리자만 접근 가능하도록 처리
		if(!SessionUtil.hasAuth(PropertyUtil.getProperty("Super.AuthCodeId"))) {
			return makeFailJsonData(egovMessageSource.getMessage("info.noAuth.msg"));
		}

		beanValidator.validate(dataVO, bindingResult);
		if(bindingResult.hasErrors()){
			return makeFailJsonData(getListErrorMsg(bindingResult));
		}

		if(dataVO.getIsNew().equals("Y")) {
			String compId = CommonUtil.removeNull(dataVO.getNewCompId());
			Pattern pattern = Pattern.compile("([A-Za-z]{1}[A-Za-z0-9]{1,30})");
			Matcher matcher = pattern.matcher(compId);

			// 아이디 중복체크
			int idDupCk = clientMngService.selectIdCnt(dataVO);
			if(idDupCk > 0) {
				return makeFailJsonData(egovMessageSource.getMessage("superMng.superMng.clientMng.clientMngList.idImpossible", new String[]{dataVO.getNewCompId()}));
			} else if(matcher.find() && compId.length() > 30 && compId.length() < 1) {
				return makeFailJsonData(egovMessageSource.getMessage("superMng.superMng.clientMng.clientMngList.compIdPattern"));
			} else {
				return makeJsonDataByResultCnt(clientMngService.insertData(dataVO));
			}
		} else {
			return makeJsonDataByResultCnt(clientMngService.updateData(dataVO));
		}
	}

	/**
	 * 계약 정보 저장
	 * @param	ClientMngVO dataVO
	 * @param	Model model
	 * @return	ModelAndView
	 * @throws	Exception
	 */
	@RequestMapping("/superMng/superMng/superMng/clientMng/saveContractData.do")
	public ModelAndView updateContract(@ModelAttribute("searchVO") ClientMngVO dataVO, Model model) throws Exception {
		// SUPER 관리자만 접근 가능하도록 처리
		if(!SessionUtil.hasAuth(PropertyUtil.getProperty("Super.AuthCodeId"))) {
			return makeFailJsonData(egovMessageSource.getMessage("info.noAuth.msg"));
		}

		int resultCnt = 0;
		if(dataVO.getRenewYn().equals("01")) {
			resultCnt = clientMngService.updateContract(dataVO);
		} else if(dataVO.getRenewYn().equals("02") || dataVO.getRenewYn() == null) {
			clientMngService.updateCurrentSeq(dataVO);
			resultCnt = clientMngService.insertContract(dataVO);
		}

		return makeJsonDataByResultCnt(resultCnt);
	}
	
	/**
	 * 계약 정보 저장
	 * @param	ClientMngVO dataVO
	 * @param	Model model
	 * @return	ModelAndView
	 * @throws	Exception
	 */
	@RequestMapping("/superMng/superMng/superMng/clientMng/resetDbInfo.do")
	public ModelAndView resetDbInfo(@ModelAttribute("searchVO") ClientMngVO dataVO, Model model) throws Exception {
		
		String tempResult = MultiReloadUtil.connectionReLoad();
		if(tempResult.contains("fail")){
			return makeFailJsonData();
		}else{
			return makeSuccessJsonData();
		}
	}
	
	/**
	 * 계약 정보 저장
	 * @param	ClientMngVO dataVO
	 * @param	Model model
	 * @return	ModelAndView
	 * @throws	Exception
	 */
	@RequestMapping("/superMng/superMng/superMng/clientMng/createScheme.do")
	public ModelAndView createScheme(@ModelAttribute("searchVO") ClientMngVO dataVO, Model model) throws Exception {
		// SUPER 관리자만 접근 가능하도록 처리
		
		String msg = clientMngService.updateSchemeData(dataVO);
		if("".equals(CommonUtil.nullToBlank(msg))){
			return makeSuccessJsonData();
		}else{
			return makeFailJsonData(msg);
		}
	}
}
