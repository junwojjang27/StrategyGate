/*************************************************************************
* CLASS 명	: CompDeptMngController
* 작 업 자	: kimyh
* 작 업 일	: 2018. 6. 29.
* 기	능	: 조직관리 Controller
* ---------------------------- 변 경 이 력 --------------------------------
* 번호	작 업 자		작	업	일			변 경 내 용				비고
* ----	---------	----------------	---------------------	-----------
*	1	kimyh		2018. 6. 29.		최 초 작 업
**************************************************************************/
package kr.ispark.system.system.comp.compDeptMng.web;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import kr.ispark.common.exception.CustomException;
import kr.ispark.common.exception.ExcelParsingException;
import kr.ispark.common.system.service.ScDeptVO;
import kr.ispark.common.util.CodeUtil;
import kr.ispark.common.util.CommonUtil;
import kr.ispark.common.util.PropertyUtil;
import kr.ispark.common.web.BaseController;
import kr.ispark.system.system.comp.compDeptMng.service.impl.CompDeptMngServiceImpl;

@Controller
public class CompDeptMngController extends BaseController {
	@Autowired
	private CompDeptMngServiceImpl compDeptMngService;
	
	/**
	 * 조직관리 목록 화면
	 * @param	ScDeptVO searchVO
	 * @param	Model model
	 * @return	String
	 * @throws	Exception
	 */
	@RequestMapping(value="/system/system/comp/compDeptMng/compDeptMngList.do")
	public String compDeptMngList(@ModelAttribute("searchVO") ScDeptVO searchVO, Model model) throws Exception {
		return "/system/system/comp/compDeptMng/compDeptMngList." + searchVO.getLayout();
	}
	
	/**
	 * 조직관리 그리드 조회(json)
	 * @param searchVO
	 * @return	ModelAndView
	 * @throws Exception
	 */
	@RequestMapping(value="/system/system/comp/compDeptMng/compDeptMngList_json.do")
	public ModelAndView compDeptMngList_json(@ModelAttribute("searchVO") ScDeptVO searchVO)throws Exception {
		
		List<ScDeptVO> dataList = compDeptMngService.selectList(searchVO);
		return makeGridJsonData(dataList, dataList.size(), searchVO);
	}
	
	/**
	 * 엑셀양식다운로드
	 */
	@RequestMapping("/system/system/comp/compDeptMng/excelDownload.do")
	public String excelDownload(@ModelAttribute("searchVO") ScDeptVO searchVO,
			Model model)throws Exception {

		List<ScDeptVO> dataList = compDeptMngService.selectExcelList(searchVO);
		String title = egovMessageSource.getMessage("word.deptManage").replace("/", "_");
		
		model.addAttribute("dataList", dataList);
		model.addAttribute("title", title);
		model.addAttribute("sheetName", title);
		
		// 검색조건
		model.addAttribute("condition1", egovMessageSource.getMessage("word.year"));
		model.addAttribute("findValue1", CodeUtil.getCodeName("017", searchVO.getFindYear()));
		
		// header
		model.addAttribute("col1", egovMessageSource.getMessage("word.orgCode"));			// 조직코드
		model.addAttribute("col2", egovMessageSource.getMessage("word.orgNm"));			// 조직명
		model.addAttribute("col3", egovMessageSource.getMessage("word.upOrgCd"));			// 상위조직코드
		model.addAttribute("col4", egovMessageSource.getMessage("word.managerId"));		// 부서장ID
		model.addAttribute("col5", egovMessageSource.getMessage("word.deptInChargeId"));	// 지표담당자ID
		model.addAttribute("col6", egovMessageSource.getMessage("word.useYn") + "(Y/N)");	// 사용여부(Y/N)
		model.addAttribute("col7", egovMessageSource.getMessage("word.sortOrder"));	// 정렬순서
		
		model.addAttribute("destJxlsFileName", title + "_" + searchVO.getFindYear() + ".xlsx");
		model.addAttribute("templateJxlsFileName", "compDeptMngList.xlsx");

		return "excelDownloadView";
	}
	
	/**
	 * 조직관리 엑셀업로드 팝업 화면
	 * @param	ScDeptVO searchVO
	 * @param	Model model
	 * @return	String
	 * @throws	Exception
	 */
	@RequestMapping(value="/system/system/comp/compDeptMng/popExcelUploadForm.do")
	public String popExcelUploadForm(@ModelAttribute("searchVO") ScDeptVO searchVO, Model model) throws Exception {
		return "/system/system/comp/compDeptMng/popExcelUploadForm." + searchVO.getLayout();
	}
	
	/**
	 * 엑셀업로드 처리
	 * @param	MultipartHttpServletRequest multiRequest
	 * @param	HttpServletResponse searchVO
	 * @param	TargetMngVO dataVO
	 * @param	Model model
	 * @return	ModelAndView
	 * @throws	Exception
	 */
	@RequestMapping(value="/system/system/comp/compDeptMng/popExcelUpload.do")
	public void popExcelUpload (
			final MultipartHttpServletRequest multiRequest,
			HttpServletResponse response,
			@ModelAttribute("dataVO") ScDeptVO dataVO, Model model) throws Exception {
		
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
						resultCnt = compDeptMngService.excelUploadProcess(dataVO, file.getInputStream());
					} else {
						throw new CustomException(egovMessageSource.getMessage("errors.excelType"));
					}
				}
			}
			
			if(resultCnt > 0) {
				resultHandling(true, multiRequest, response, dataVO);
			} else {
				resultHandling(false, multiRequest, response, dataVO);
			}
		} catch(CustomException|ExcelParsingException ep) {
			resultHandling(false, multiRequest, response, dataVO, ep.getMessage());
			log.error("error : "+ep.getCause());
		} catch(Exception e) {
			resultHandling(false, multiRequest, response, dataVO);
			log.error("error : "+e.getCause());
		}
	}
	
	/**
	 * 조직관리 일괄저장
	 * @param dataVO
	 * @param model
	 * @param bindingResult
	 * @return	ModelAndView
	 * @throws Exception
	 */
	@RequestMapping("/system/system/comp/compDeptMng/saveAll.do")
	public ModelAndView saveAll(@ModelAttribute("dataVO") ScDeptVO dataVO, Model model, BindingResult bindingResult) throws Exception {
		// list 유효성 체크
		validateList(dataVO.getGridDataList(), bindingResult);
		if(bindingResult.hasErrors()) {
			return makeFailJsonData(getListErrorMsg(bindingResult));
		}
		
		for(ScDeptVO vo : dataVO.getGridDataList()) {
			if(vo.getIsNew().equals("Y")) {
				if(CommonUtil.isEmpty(vo.getUpScDeptId())) {
					return makeFailJsonData(egovMessageSource.getMessage("common.required.msg", new String[] {egovMessageSource.getMessage("word.upOrg")}));
				}
			}
		}
		
		int resultCnt = compDeptMngService.saveAll(dataVO);
		if(resultCnt == 0) {
			return makeFailJsonData();
		}
		return makeSuccessJsonData();
	}
	
	/**
	 * 조직 삭제
	 * @param dataVO
	 * @param model
	 * @return	ModelAndView
	 * @throws Exception
	 */
	@RequestMapping("/system/system/comp/compDeptMng/deleteCompDeptMng.do")
	public ModelAndView deleteCompDeptMng(@ModelAttribute("dataVO") ScDeptVO dataVO, Model model) throws Exception {
		// 최상위조직 삭제 방지
		for(String key : dataVO.getKeys()) {
			if(key.equals(PropertyUtil.getProperty("default.rootScDeptId"))) {
				return makeFailJsonData(egovMessageSource.getMessage("system.system.comp.compDeptMng.error2"));
			}
		}
		
		int resultCnt = compDeptMngService.deleteCompDeptMng(dataVO);
		if(resultCnt == 0) {
			return makeFailJsonData();
		}
		return makeSuccessJsonData();
	}
}
