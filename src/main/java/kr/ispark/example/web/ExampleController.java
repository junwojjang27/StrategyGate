/**
 * @Class Name	:	ExampleController.java
 * @Description	:	예제용 Controller
 * @author	:	kimyh
 * @date	:	2017-12-15
 */
package kr.ispark.example.web;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.StandardPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import egovframework.com.cmm.service.EgovFileMngService;
import egovframework.let.utl.fcc.service.EgovStringUtil;
import kr.ispark.common.CommonVO;
import kr.ispark.common.exception.CustomException;
import kr.ispark.common.exception.ExcelParsingException;
import kr.ispark.common.util.CommonUtil;
import kr.ispark.common.web.BaseController;
import kr.ispark.example.service.ExampleBoardVO;
import kr.ispark.example.service.ExampleVO;
import kr.ispark.example.service.impl.ExampleServiceImpl;

@Controller
public class ExampleController extends BaseController {

	@Autowired
	private ExampleServiceImpl exampleService;

	@Resource(name = "EgovFileMngService")
	private EgovFileMngService fileService;

	@Autowired
	StandardPasswordEncoder passwordEncoder;

	/**
	 * 모니터링 테스트
	 * @return	String
	 * @exception	Exception
	 */
	@RequestMapping("/example/common/dragdrop.do")
	public String dragdrop(@ModelAttribute("searchVO") ExampleVO searchVO, Model model) throws Exception {
		return "/example/common/dragdrop." + searchVO.getLayout();
	}

	/**
	 * 모니터링 테스트
	 * @return	String
	 * @exception	Exception
	 */
	@RequestMapping("/example/common/dragdrop_jquery.do")
	public String dragdrop_jquery(@ModelAttribute("searchVO") ExampleVO searchVO, Model model) throws Exception {
		return "/example/common/dragdrop_jquery." + searchVO.getLayout();
	}

	/**
	 * 그리드 조회 & 링크 화면
	 * @return	String
	 * @exception	Exception
	 */
	@RequestMapping("/example/grid/grid.do")
	public String grid(@ModelAttribute("searchVO") ExampleVO searchVO, Model model) throws Exception {
		return "/example/grid/grid." + searchVO.getLayout();
	}

	/**
	 * 그리드 조회(json)
	 */
	@RequestMapping("/example/grid/gridList_json.do")
	public ModelAndView gridList_json(@ModelAttribute("searchVO") ExampleVO searchVO)throws Exception {

		List<ExampleVO> dataList = exampleService.selectList(searchVO);
		return makeGridJsonData(dataList);
	}

	/**
	 * 그리드 페이징 - grid 자체
	 * @return	String
	 * @exception	Exception
	 */
	@RequestMapping("/example/grid/gridPaging.do")
	public String gridPaging(@ModelAttribute("searchVO") ExampleVO searchVO, Model model) throws Exception {
		return "/example/grid/gridPaging." + searchVO.getLayout();
	}

	/**
	 * 그리드 페이징 - DB
	 * @return	String
	 * @exception	Exception
	 */
	@RequestMapping("/example/grid/gridPaging2.do")
	public String gridPaging2(@ModelAttribute("searchVO") ExampleVO searchVO, Model model) throws Exception {
		return "/example/grid/gridPaging2." + searchVO.getLayout();
	}

	/**
	 * 관점설정 페이징 데이터 조회(json)
	 */
	@RequestMapping("/example/grid/gridListPaging_json.do")
	public ModelAndView gridListPaging_json(@ModelAttribute("searchVO") ExampleVO searchVO) throws Exception {

		List<ExampleVO> dataList = exampleService.selectListPaging(searchVO);
		int listCnt = exampleService.selectListCount(searchVO);

		return makeGridJsonData(dataList, listCnt, searchVO);
	}

	/**
	 * 그리드 Cell edit
	 * @return	String
	 * @exception	Exception
	 */
	@RequestMapping("/example/grid/gridCellEdit.do")
	public String gridCellEdit(@ModelAttribute("searchVO") ExampleVO searchVO, Model model) throws Exception {
		return "/example/grid/gridCellEdit." + searchVO.getLayout();
	}

	/**
	 * 그리드 다중 삭제
	 * @return	String
	 * @exception	Exception
	 */
	@RequestMapping("/example/grid/gridDelete.do")
	public String gridDelete(@ModelAttribute("searchVO") ExampleVO searchVO, Model model) throws Exception {
		return "/example/grid/gridDelete." + searchVO.getLayout();
	}

	/**
	 * 그리드 다중 삭제 처리
	 * @return	ModelAndView
	 * @exception	Exception
	 */
	@RequestMapping("/example/grid/gridDeleteProcess.do")
	public ModelAndView gridDeleteProcess(@ModelAttribute("dataVO") ExampleVO dataVO, Model model) throws Exception {

		int resultCnt = exampleService.deleteData(dataVO);
		if(resultCnt == 0) {
			return makeFailJsonData();
		}
		return makeSuccessJsonData();
	}

	/**
	 * 그리드 일괄 저장
	 * @return	String
	 * @exception	Exception
	 */
	@RequestMapping("/example/grid/gridSave.do")
	public String gridSave(@ModelAttribute("searchVO") ExampleVO searchVO, Model model) throws Exception {
		return "/example/grid/gridSave." + searchVO.getLayout();
	}

	/**
	 * 그리드 일괄 저장 처리
	 * @return	ModelAndView
	 * @exception	Exception
	 */
	@RequestMapping("/example/grid/gridSaveProcess.do")
	public ModelAndView gridSaveProcess(@ModelAttribute("dataVO") ExampleVO dataVO, Model model, BindingResult bindingResult) throws Exception {

		// list 유효성 체크
		validateList(dataVO.getGridDataList(), bindingResult);
		if(bindingResult.hasErrors()) {
			return makeFailJsonData(getListErrorMsg(bindingResult));
		}

		int resultCnt = exampleService.saveData(dataVO);
		if(resultCnt == 0) {
			return makeFailJsonData();
		}
		return makeSuccessJsonData();
	}

	/**
	 * 담당자 선택 팝업
	 * @return	String
	 * @exception	Exception
	 */
	@RequestMapping("/example/common/popUserList.do")
	public String popUserList(@ModelAttribute("searchVO") ExampleVO searchVO, Model model) throws Exception {
		model.addAttribute("userList", exampleService.selectUserList(searchVO));
		return "/example/common/popUserList." + searchVO.getLayout();
	}

	/**
	 * 유효성 체크 - 폼
	 * @return	String
	 * @exception	Exception
	 */
	@RequestMapping("/example/validation/validationForm.do")
	public String validationForm(@ModelAttribute("searchVO") ExampleVO searchVO, Model model) throws Exception {
		return "/example/validation/validationForm." + searchVO.getLayout();
	}

	/**
	 * 유효성 체크 - Server-side validation
	 * @return	ModelAndView
	 * @exception	Exception
	 */
	@RequestMapping("/example/validation/validationSave.do")
	public ModelAndView validationSave(@ModelAttribute("dataVO") ExampleVO dataVO, Model model, BindingResult bindingResult) throws Exception {
		beanValidator.validate(dataVO, bindingResult);
		if(bindingResult.hasErrors()){
			return makeFailJsonData(getListErrorMsg(bindingResult));
		}
		return makeSuccessJsonData();
	}

	/**
	 * 유효성 체크 - Grid
	 * @return	String
	 * @exception	Exception
	 */
	@RequestMapping("/example/validation/validationGrid.do")
	public String validationGrid(@ModelAttribute("searchVO") ExampleVO searchVO, Model model) throws Exception {
		return "/example/validation/validationGrid." + searchVO.getLayout();
	}

	/**
	 * 유효성 체크 - Grid
	 * @return	String
	 * @exception	Exception
	 */
	@RequestMapping("/example/validation/validationGridSave.do")
	public ModelAndView validationGridSave(@ModelAttribute("dataVO") ExampleVO dataVO, Model model, BindingResult bindingResult) throws Exception {
		// form 유효성 체크
		beanValidator.validate(dataVO, bindingResult);
		if(bindingResult.hasErrors()){
			return makeFailJsonData(getListErrorMsg(bindingResult));
		}

		// list 유효성 체크
		validateList(dataVO.getGridDataList(), bindingResult);
		if(bindingResult.hasErrors()) {
			return makeFailJsonData(getListErrorMsg(bindingResult));
		}

		return makeSuccessJsonData();
	}

	/**
	 * 엑셀 업로드 예제 화면
	 * @param searchVO
	 * @param model
	 * @return	String
	 * @throws Exception
	 */
	@RequestMapping("/example/excel/excel.do")
	public String excel(@ModelAttribute("searchVO") ExampleVO searchVO, Model model) throws Exception {
		return "/example/excel/excel." + searchVO.getLayout();
	}

	/**
	 * 엑셀 업로드 예제 목록 조회(json)
	 */
	@RequestMapping("/example/excel/excelList_json.do")
	public ModelAndView excelList_json(@ModelAttribute("searchVO") ExampleVO searchVO)throws Exception {

		List<ExampleVO> dataList = exampleService.selectScDeptList(searchVO);
		return makeGridJsonData(dataList);
	}

	/**
	 * 엑셀양식다운로드
	 */
	@RequestMapping("/example/excel/excelFormDownload.do")
	public String excelFormDownload(@ModelAttribute("searchVO") ExampleVO searchVO,
			Model model)throws Exception {

		List<ExampleVO> dataList = exampleService.selectScDeptList(searchVO);

		//타이틀
		model.addAttribute("title", egovMessageSource.getMessage("word.orgSelectList"));

		//검색조건
		model.addAttribute("year", egovMessageSource.getMessage("word.year"));
		model.addAttribute("findYear", searchVO.getFindYear());

		//header
		model.addAttribute("orgCode", egovMessageSource.getMessage("word.orgCode")); //조직코드
		model.addAttribute("orgNm", egovMessageSource.getMessage("word.orgNm")); //조직명
		model.addAttribute("upOrgCd", egovMessageSource.getMessage("word.upOrgCd")); //상위조직코드
		model.addAttribute("managerId", egovMessageSource.getMessage("word.managerId2")); //조직장ID
		model.addAttribute("inChargeId", egovMessageSource.getMessage("word.inChargeId")); //담당자ID
		model.addAttribute("sortOrder", egovMessageSource.getMessage("word.sortOrder")); //정렬순서
		model.addAttribute("useYn", egovMessageSource.getMessage("word.useYn")); //사용여부

		//조직, 사용자 데이터
		model.addAttribute("dataList", dataList);

		// 시트명
		model.addAttribute("sheetName", egovMessageSource.getMessage("word.org"));

		model.addAttribute("destJxlsFileName", egovMessageSource.getMessage("word.deptManage") + "_" + EgovStringUtil.getTimeStamp()+".xlsx");
		model.addAttribute("templateJxlsFileName", "excelExample.xlsx");

		return "excelDownloadView";

	}

	/**
	 * 엑셀 업로드 팝업
	 * @return	String
	 * @exception	Exception
	 */
	@RequestMapping("/example/excel/popExcelUpload.do")
	public String popExcelUpload(@ModelAttribute("searchVO") ExampleVO searchVO, Model model) throws Exception {
		model.addAttribute("userList", exampleService.selectUserList(searchVO));
		return "/example/excel/popExcelUpload." + searchVO.getLayout();
	}

	/**
	 * 엑셀업로드 처리
	 * @param multiRequest
	 * @param searchVO
	 * @param dataVO
	 * @param model
	 * @return	ModelAndView
	 * @throws Exception
	 */
	@RequestMapping("/example/excel/excelUploadProcess.do")
	public void insertExcelData(
			final MultipartHttpServletRequest multiRequest,
			HttpServletResponse response,
			@ModelAttribute("dataVO") ExampleVO dataVO, Model model) throws Exception {

		final Map<String, MultipartFile> files = multiRequest.getFileMap();
		Iterator<Entry<String, MultipartFile>> itr = files.entrySet().iterator();
		MultipartFile file;

		try {
			int resultCnt = 0;
			while(itr.hasNext()) {
				Entry<String, MultipartFile> entry = itr.next();
				file = entry.getValue();
				if(!"".equals(file.getOriginalFilename())) {
					// 2011.10.07 업로드 파일에 대한 확장자를 체크
					if (CommonUtil.isExcelFile(file.getOriginalFilename())) {
						resultCnt = exampleService.updateExcelProcess(dataVO, file.getInputStream());
					} else {
						throw new Exception(egovMessageSource.getMessage("errors.excelType"));
					}
				}
			}

			if(resultCnt > 0) {
				resultHandling(true, multiRequest, response, dataVO);
			} else {
				resultHandling(false, multiRequest, response, dataVO);
				return;
			}
		} catch(ExcelParsingException ep) {
			resultHandling(false, multiRequest, response, dataVO, ep.getMessage());
		} catch(Exception e) {
			log.error(e.getMessage());
			resultHandling(false, multiRequest, response, dataVO);
		}
	}

	/**
	 * Fancybox 예제 화면
	 * @param	searchVO
	 * @param	model
	 * @return	String
	 * @throws	Exception
	 */
	@RequestMapping("/example/common/fancybox.do")
	public String fancybox(@ModelAttribute("searchVO") ExampleVO searchVO, Model model) throws Exception {
		return "/example/common/fancybox." + searchVO.getLayout();
	}

	/**
	 * 검색조건 설정 & 유지
	 * @param	ExampleVO searchVO
	 * @param	Model model
	 * @return	String
	 * @throws	Exception
	 */
	@RequestMapping("/example/common/findValue.do")
	public String findValue(HttpServletRequest request, @ModelAttribute("searchVO") ExampleVO searchVO, Model model) throws Exception {
		System.out.println("\n\n\n#### ExampleController.findValue ###");
		System.out.println("====================== searchVO");
		System.out.println(searchVO.getFindYear());
		System.out.println(searchVO.getFindMon());
		System.out.println(searchVO.getFindAnalCycle());
		System.out.println("====================== cookie");
		System.out.println(getCookie(request, "findYear"));
		System.out.println(getCookie(request, "findMon"));
		System.out.println(getCookie(request, "findAnalCycle"));
		System.out.println("#######################\n\n\n");

		return "/example/common/findValue." + searchVO.getLayout();
	}

	/**
	 * Test
	 * @param	CommonVO searchVO
	 * @param	Model model
	 * @return	String
	 * @throws	Exception
	 */
	@RequestMapping("/example/file/test.do")
	public String grid(@ModelAttribute("searchVO") CommonVO searchVO, Model model) throws Exception {
		return "/example/file/test." + searchVO.getLayout();
	}

	/**
	 * 첨부파일 저장
	 * @param multiRequest
	 * @param dataVO
	 * @param model
	 * @param bindingResult
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/example/file/saveFiles.do")
	public void saveFiles (
			final MultipartHttpServletRequest multiRequest,
			HttpServletResponse response,
			@ModelAttribute("dataVO") ExampleBoardVO dataVO,
			Model model, BindingResult bindingResult) throws Exception {

		try {
			MultiValueMap<String, MultipartFile> fileMap = multiRequest.getMultiFileMap();
			/*
			List<MultipartFile> fileList = fileMap.get(inputFileName);
			if(fileList != null) {
				for(MultipartFile file : fileList) {
			*/
			System.out.println("\n\n\n#### ExampleController.saveFiles	fileMap ###");
			System.out.println(fileMap);

			Set<String> keySet = fileMap.keySet();
			Iterator<String> it = keySet.iterator();
			String key;
			while(it.hasNext()) {
				key = it.next();
				System.out.println("- key : " + key);
				List<MultipartFile> fileList = fileMap.get(key);
				for(MultipartFile file : fileList) {
					if(!file.isEmpty()) {
						System.out.println(file.getOriginalFilename());
					}
				}
			}

			System.out.println("#######################\n\n\n");
		} catch(NullPointerException npe) {
			log.error("error : "+npe.getCause());
			resultHandling(false, multiRequest, response, dataVO);
		} catch(Exception e) {
			log.error("error : "+e.getCause());
			resultHandling(false, multiRequest, response, dataVO);
		}

		resultHandling(true, multiRequest, response, dataVO);
	}

	/**
	 * API Test
	 * @param	HttpServletRequest req
	 * @param	HttpServletResponse res
	 * @return	ModelAndView
	 * @throws	Exception
	 * @TODO	이력 남김, 처리 결과의 코드화
	 */
	@RequestMapping("/api/apiTest.do")
	public ModelAndView apiTest(HttpServletRequest req, HttpServletResponse res) throws Exception {
		/*
		log.debug("\n\n\n#### ExampleController.apiTest ###");
		log.debug("- URI : " + req.getRequestURI());
		log.debug("- method : " + req.getMethod());
		log.debug("- remoteAddr : " + req.getRemoteAddr());
		log.debug("");
		log.debug("- header info");

		// header 조회
		Enumeration<?> headerNames = req.getHeaderNames();
		String headerName;
		while(headerNames.hasMoreElements()) {
			headerName = (String)headerNames.nextElement();
			log.debug(headerName + " : " + req.getHeader(headerName));
		}

		// parameter 조회
		Enumeration<?> paramNames = req.getParameterNames();
		String paramName;
		String values[];
		StringBuffer sb;
		while(paramNames.hasMoreElements()) {
			sb = new StringBuffer();
			paramName = (String)paramNames.nextElement();
			values = req.getParameterValues(paramName);

			sb.append(paramName + " : ");
			if(values == null) {
				sb.append("null");
			} else if(values.length == 1) {
				sb.append(values[0]);
			} else {
				sb.append("[");
				int idx = 0;
				for(String value : values) {
					if(idx++ > 0) {
						sb.append(", ");
					};
					sb.append(value);
				}
				sb.append("]");
			}
			log.debug(sb.toString());
		}

		// request body 출력
		StringBuffer sb = new StringBuffer();
		String line;
		try(BufferedReader reader = req.getReader();) {
			while((line = reader.readLine()) != null) {
				sb.append(line);
			}
			log.debug(sb.toString());
		} catch(Exception e) {
			log.error(e.getMessage());
		}
		log.debug("#######################\n\n\n");
		*/

		String compId = CommonUtil.removeNull(req.getHeader("COMP_ID"));
		String key = CommonUtil.removeNull(req.getHeader("KEY"));
		String processeId = CommonUtil.removeNull(req.getHeader("PROCESS_ID"));
		boolean passed = false;

		int resultCnt = 0;
		String result = "FAIL";
		String resultMsg = "";

		try {
			// 사용자 인증 : COMP_ID와 KEY 비교
			passed = passwordEncoder.matches(compId, key);

			log.debug("compId : " + compId);
			log.debug("key : " + key);
			log.debug("matches : " + passed);
			log.debug("processeId : " + processeId);

			if(passed) {
				if(CommonUtil.isEmpty(processeId)) {
					throw new CustomException("PROCESS_ID가 없습니다.");
				}

				// 데이터 유효성 체크
				String[] metricIds = req.getParameterValues("metricId");
				String[] metricNms = req.getParameterValues("metricNm");
				String[] values = req.getParameterValues("value");
				if(metricIds == null || metricIds.length == 0
					|| metricNms == null || metricNms.length == 0
					|| values == null || values.length == 0
					|| metricIds.length != metricNms.length
					|| metricIds.length != values.length) {
					throw new CustomException("데이터가 존재하지 않거나 개수가 맞지 않습니다.");
				}

				// 데이터 처리
				for(int i=0; i<metricIds.length; i++) {
					log.debug(metricIds[i] + "\t" + metricNms[i] + "\t" + values[i]);
					resultCnt++;
				}

				if(resultCnt == 0) {
					throw new CustomException("처리된 내용이 없습니다.");
				}

				result = "SUCCESS";
				resultMsg = resultCnt + "건의 데이터 처리가 완료되었습니다.";
			} else {
				throw new CustomException("API 인증에 실패했습니다.");
			}
		} catch(CustomException ce) {
			result = "FAIL";
			resultMsg = ce.getMessage();
		} catch(Exception e) {
			log.error(e.getMessage());
			result = "FAIL";
			resultMsg = "API 인증에 실패했습니다.";
		}

		// TODO API 연동 로그

		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("result", result);
		resultMap.put("resultMsg", resultMsg);

		log.debug(resultMap.toString());

		return new ModelAndView("jsonView", resultMap);
	}
}
