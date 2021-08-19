/*************************************************************************
* CLASS 명	: CompUserMngController
* 작 업 자	: 박정현
* 작 업 일	: 2018-07-02
* 기	능	: 사용자관리 Controller
* ---------------------------- 변 경 이 력 --------------------------------
* 번호	작 업 자		작	업	일			변 경 내 용				비고
* ----	---------	----------------	---------------------	-----------
*	1	박정현		2018-07-02
**************************************************************************/
package kr.ispark.system.system.comp.compUserMng.web;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.StandardPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import egovframework.let.utl.fcc.service.EgovStringUtil;
import kr.ispark.common.exception.CustomException;
import kr.ispark.common.exception.ExcelParsingException;
import kr.ispark.common.system.service.impl.CommonServiceImpl;
import kr.ispark.common.util.CodeUtil;
import kr.ispark.common.util.CommonUtil;
import kr.ispark.common.util.SessionUtil;
import kr.ispark.common.util.service.CodeVO;
/*공통 컨트롤러*/
import kr.ispark.common.web.BaseController;
import kr.ispark.system.system.comp.compUserMng.service.CompUserMngVO;
import kr.ispark.system.system.comp.compUserMng.service.impl.CompUserMngServiceImpl;

@Controller
public class CompUserMngController extends BaseController {
	@Autowired
	private CompUserMngServiceImpl compUserMngService;

	@Autowired
	private CommonServiceImpl commonServiceImpl;

	@Autowired
	StandardPasswordEncoder passwordEncoder;

	/**
	 * 사용자관리 목록 화면
	 * @param	CompUserMngVO searchVO
	 * @param	Model model
	 * @return	String
	 * @throws	Exception
	 */
	@RequestMapping("/system/system/comp/compUserMng/compUserMngList.do")
	public String compUserMngList(@ModelAttribute("searchVO") CompUserMngVO searchVO, Model model) throws Exception {

		//findYear값을 현재 년도로 변경
		Calendar cal = new GregorianCalendar(); //new GregorianCalendar()생성자가 호출되는 시점에 날짜 관련된 모든 값을 cal에 넣어줌.
		int year = 	cal.get(Calendar.YEAR);
		String strYear = String.valueOf(year);
		searchVO.setFindYear(strYear);

		/*
		 * 관리자의 로그인 전환 구분을 위한 key
		 * CustomAuthenticationProvider에서 로그인 처리를 할 때 이 key가 있으면 관리자의 로그인 전환을 처리
		 */
		SessionUtil.setAttribute("ADMIN_LOGIN_KEY", passwordEncoder.encode(String.valueOf(System.currentTimeMillis())));

		return "/system/system/comp/compUserMng/compUserMngList." + searchVO.getLayout();
	}

	/**
	 * 사용자관리 그리드 조회(json)
	 * @param	CompUserMngVO searchVO
	 * @return	ModelAndView
	 * @throws	Exception
	 */
	@RequestMapping("/system/system/comp/compUserMng/compUserMngList_json.do")
	public ModelAndView compUserMngList_json(@ModelAttribute("searchVO") CompUserMngVO searchVO) throws Exception {
		List<CompUserMngVO> dataList = compUserMngService.selectList(searchVO);
		return makeGridJsonData(dataList, dataList.size(), searchVO);
	}

	/**
	 * 사용자관리 조회
	 * @param	CompUserMngVO searchVO
	 * @return	ModelAndView
	 * @throws	Exception
	 */
	@RequestMapping("/system/system/comp/compUserMng/selectDetail.do")
	public ModelAndView selectDetail(@ModelAttribute("searchVO") CompUserMngVO searchVO) throws Exception {
		return makeJsonData(compUserMngService.selectDetail(searchVO));
	}

	/**
	 * 패스워드 초기화
	 * @param	CompUserMngVO dataVO
	 * @return	ModelAndView
	 * @throws	Exception
	 */
	@RequestMapping("/system/system/comp/compUserMng/passwordReset.do")
	public ModelAndView passwordReset(@ModelAttribute("dataVO") CompUserMngVO dataVO) throws Exception {
		if(CommonUtil.isEmpty(dataVO.getUserId())) {
			return makeFailJsonData();
		} else {
			return makeJsonDataByResultCnt(compUserMngService.updatePasswordReset(dataVO));
		}
	}

	/**
	 * 사용자관리 삭제
	 * @param	CompUserMngVO dataVO
	 * @param	Model model
	 * @return	ModelAndView
	 * @throws	Exception
	 */
	@RequestMapping("/system/system/comp/compUserMng/deleteCompUserMng.do")
	public ModelAndView deleteCompUserMng(@ModelAttribute("dataVO") CompUserMngVO dataVO, Model model) throws Exception {
		return makeJsonDataByResultCnt(compUserMngService.deleteCompUserMng(dataVO));
	}

	/**
	 * 사용자관리 저장
	 * @param	CompUserMngVO dataVO
	 * @param	Model model
	 * @param	BindingResult bindingResult
	 * @return	ModelAndView
	 * @throws	Exception
	 */
	@RequestMapping("/system/system/comp/compUserMng/saveCompUserMng.do")
	public ModelAndView saveCompUserMng(@ModelAttribute("dataVO") CompUserMngVO dataVO, Model model, BindingResult bindingResult) throws Exception {
		beanValidator.validate(dataVO, bindingResult);
		if(bindingResult.hasErrors()){
			return makeFailJsonData(getListErrorMsg(bindingResult));
		}

		int idCk = 0;
		if("N".equals(dataVO.getUpdateYn())) {
			idCk = compUserMngService.selectIdCnt(dataVO);
		}
		if(idCk > 0) {
			return makeFailJsonData(egovMessageSource.getMessage("system.system.comp.compUserMng.idImpossible", new String[]{dataVO.getUserId()}));
		} else {
			return makeJsonDataByResultCnt(compUserMngService.saveData(dataVO));
		}
	}

	/**
	 * 사용자관리 엑셀 업로드 팝업
	 * @param	CompUserMngVO searchVO
	 * @param	Model model
	 * @return	String
	 * @throws	Exception
	 */
	@RequestMapping("/system/system/comp/compUserMng/popExcelUploadForm.do")
	public String popExcelUpload(@ModelAttribute("searchVO") CompUserMngVO searchVO, Model model) throws Exception {
		return "/system/system/comp/compUserMng/popExcelUpload." + searchVO.getLayout();
	}

	/**
	 * 지표POOL 업로드 양식 다운로드
	 * @param	CompUserMngVO searchVO
	 * @return	String
	 * @throws	Exception
	 */
	@RequestMapping("/system/system/comp/compUserMng/excelFormDownload.do")
	public String excelFormDownload(@ModelAttribute("searchVO") CompUserMngVO searchVO, Model model) throws Exception {
		List<String> codeList1 = new ArrayList<String>();
		// 직위
		for(CodeVO codeVO : CodeUtil.getCodeList("344")) {
			codeList1.add(codeVO.getCodeId() + ":" + codeVO.getCodeNm());
		}
		// 직급
		List<String> codeList2 = new ArrayList<String>();
		for(CodeVO codeVO : CodeUtil.getCodeList("345")) {
			codeList2.add(codeVO.getCodeId() + ":" + codeVO.getCodeNm());
		}
		// 직무
		List<String> codeList3 = new ArrayList<String>();
		for(CodeVO codeVO : CodeUtil.getCodeList("343")) {
			codeList3.add(codeVO.getCodeId() + ":" + codeVO.getCodeNm());
		}
		// 재직구분
		List<String> codeList4 = new ArrayList<String>();
		for(CodeVO codeVO : CodeUtil.getCodeList("368")) {
			codeList4.add(codeVO.getCodeId() + ":" + codeVO.getCodeNm());
		}

		model.addAttribute("codeList1", codeList1);
		model.addAttribute("codeList2", codeList2);
		model.addAttribute("codeList3", codeList3);
		model.addAttribute("codeList4", codeList4);

		model.addAttribute("notice", egovMessageSource.getMessage("word.notice"));
		model.addAttribute("info1", egovMessageSource.getMessage("system.system.comp.compUserMng.excelUpload.info1"));
		model.addAttribute("info2", egovMessageSource.getMessage("system.system.comp.compUserMng.excelUpload.info2"));
		model.addAttribute("info3", egovMessageSource.getMessage("system.system.comp.compUserMng.excelUpload.info3"));
		model.addAttribute("info4", egovMessageSource.getMessage("system.system.comp.compUserMng.excelUpload.info4"));
		model.addAttribute("info5", egovMessageSource.getMessage("system.system.comp.compUserMng.excelUpload.info5"));
		model.addAttribute("info6", egovMessageSource.getMessage("system.system.comp.compUserMng.excelUpload.info6"));
		model.addAttribute("info7", egovMessageSource.getMessage("system.system.comp.compUserMng.excelUpload.info7"));
		model.addAttribute("info8", egovMessageSource.getMessage("system.system.comp.compUserMng.excelUpload.info8"));

		// 추가
		model.addAttribute("title", egovMessageSource.getMessage("word.userMng") + " - " + egovMessageSource.getMessage("button.add"));
		model.addAttribute("col1", egovMessageSource.getMessage("word.empNum"));
		model.addAttribute("col2", egovMessageSource.getMessage("word.name2"));
		model.addAttribute("col3", egovMessageSource.getMessage("word.pos"));
		model.addAttribute("col4", egovMessageSource.getMessage("word.jikgub"));
		model.addAttribute("col5", egovMessageSource.getMessage("word.job"));
		model.addAttribute("col6", egovMessageSource.getMessage("word.orgCode"));
		model.addAttribute("col7", egovMessageSource.getMessage("word.email"));
		model.addAttribute("col8", egovMessageSource.getMessage("word.beingYn"));

		// 수정
		model.addAttribute("title2", egovMessageSource.getMessage("word.userMng") + " - " + egovMessageSource.getMessage("word.modify"));
		List<CompUserMngVO> dataList = compUserMngService.selectListForExcelForm(searchVO);
		model.addAttribute("dataList", dataList);

		// 조직 목록
		model.addAttribute("title3", egovMessageSource.getMessage("word.deptManage"));
		model.addAttribute("condition1", egovMessageSource.getMessage("word.year"));
		model.addAttribute("findYear", CodeUtil.getCodeName("017", searchVO.getFindYear()));
		model.addAttribute("deptCol1", egovMessageSource.getMessage("word.orgCode"));
		model.addAttribute("deptCol2", egovMessageSource.getMessage("word.org"));
		model.addAttribute("deptList", commonServiceImpl.selectDeptList(searchVO));

		model.addAttribute("sheetNames", new String[] {
			egovMessageSource.getMessage("word.notice"),
			egovMessageSource.getMessage("button.add"),
			egovMessageSource.getMessage("word.modify"),
			egovMessageSource.getMessage("word.orgCode")
		});

		model.addAttribute("destJxlsFileName", egovMessageSource.getMessage("word.userMng").replaceAll(" ", "_") + "(" + CodeUtil.getCodeName("017", searchVO.getFindYear()) + ")_" + EgovStringUtil.getTimeStamp() + ".xlsx");
		model.addAttribute("templateJxlsFileName", "compUserMngForm.xlsx");

		return "excelDownloadView";
	}

	/**
	 * 엑셀업로드 처리
	 * @param	MultipartHttpServletRequest multiRequest
	 * @param	HttpServletResponse searchVO
	 * @param	CompUserMngVO dataVO
	 * @param	Model model
	 * @return	ModelAndView
	 * @throws	Exception
	 */
	@RequestMapping(value="/system/system/comp/compUserMng/popExcelUpload.do")
	public void popExcelUpload (
			final MultipartHttpServletRequest multiRequest,
			HttpServletResponse response,
			@ModelAttribute("dataVO") CompUserMngVO dataVO, Model model) throws Exception {

		final Map<String, MultipartFile> files = multiRequest.getFileMap();
		Iterator<Entry<String, MultipartFile>> itr = files.entrySet().iterator();
		MultipartFile file;

		try {
			int resultCnt = 0;
			while(itr.hasNext()) {
				Entry<String, MultipartFile> entry = itr.next();
				file = entry.getValue();
				if(!"".equals(file.getOriginalFilename())) {
					if (CommonUtil.isExcelFile(file.getOriginalFilename())) {
						resultCnt = compUserMngService.excelUploadProcess(dataVO, file.getInputStream());
					} else {
						throw new Exception(egovMessageSource.getMessage("errors.excelType"));
					}
				}
			}

			if(resultCnt > 0) {
				resultHandling(true, multiRequest, response, dataVO);
			} else {
				resultHandling(false, multiRequest, response, dataVO);
			}
		} catch(CustomException | ExcelParsingException ep) {
			resultHandling(false, multiRequest, response, dataVO, ep.getMessage());
			log.error("error : "+ep.getCause());
		} catch(Exception e) {
			resultHandling(false, multiRequest, response, dataVO);
			log.error("error : "+e.getCause());
		}
	}
}
