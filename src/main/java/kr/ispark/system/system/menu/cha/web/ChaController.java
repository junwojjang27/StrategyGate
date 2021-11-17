/*************************************************************************
* CLASS 명	: ChaController
* 작 업 자	: 하성준
* 작 업 일	: 2021-11-09
* 기	능	: 문화재청 Controller
* ---------------------------- 변 경 이 력 --------------------------------
* 번호	작 업 자		작	업	일			변 경 내 용				비고
* ----	---------	----------------	---------------------	-----------
*	1	하성준		2021-11-09
**************************************************************************/
package kr.ispark.system.system.menu.cha.web;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import egovframework.com.cmm.service.EgovFileMngService;
import egovframework.com.cmm.service.FileVO;
import egovframework.com.utl.sim.service.EgovFileTool;
import kr.ispark.common.util.CommonUtil;
import kr.ispark.common.util.CustomEgovFileMngUtil;
import kr.ispark.common.util.PropertyUtil;
import kr.ispark.common.util.SessionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import kr.ispark.system.system.menu.cha.service.impl.ChaServiceImpl;
import kr.ispark.system.system.menu.cha.service.ChaVO;
/*공통 컨트롤러*/
import kr.ispark.common.web.BaseController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

@Controller
public class ChaController extends BaseController {
	@Autowired
	private ChaServiceImpl chaService;

	@Resource(name = "CustomEgovFileMngUtil")
	private CustomEgovFileMngUtil fileUtil;


	@Resource(name = "EgovFileMngService")
	private EgovFileMngService fileService;

	/**
	 * 문화재청 목록 화면
	 * @param	ChaVO searchVO
	 * @param	Model model
	 * @return	String
	 * @throws	Exception
	 */
	@RequestMapping("/system/system/menu/cha/chaList.do")
	public String chaList(@ModelAttribute("searchVO") ChaVO searchVO, Model model) throws Exception {

		System.out.println("안되나?1111111111");

		ChaVO vo = new ChaVO();
		List<ChaVO> straList = chaService.selectList9(vo);
		model.addAttribute("straList", straList);

		System.out.println("안되나?");
		System.out.println(straList);

		return "/system/system/menu/cha/chaList." + searchVO.getLayout();
	}

	/**
	 * 문화재청 그리드 조회(json)
	 * @param	ChaVO searchVO
	 * @return	ModelAndView
	 * @throws	Exception
	 */
	@RequestMapping("/system/system/menu/cha/chaList_json.do")
	public ModelAndView chaList_json(@ModelAttribute("searchVO") ChaVO searchVO) throws Exception {
		List<ChaVO> dataList = chaService.selectList(searchVO);
		return makeGridJsonData(dataList, dataList.size(), searchVO);
	}

	/**
	 * 문화재청 성과목표 그리드 조회(json)
	 * @param	ChaVO searchVO
	 * @return	ModelAndView
	 * @throws	Exception
	 */
	@RequestMapping("/system/system/menu/cha/chaList2_json.do")
	public ModelAndView chaList2_json(@ModelAttribute("searchVO") ChaVO searchVO) throws Exception {
		List<ChaVO> dataList = chaService.selectList2(searchVO);
		return makeGridJsonData(dataList, dataList.size(), searchVO);
	}

	/**
	 * 문화재청 전략목표 그리드 조회(json)
	 * @param	ChaVO searchVO
	 * @return	ModelAndView
	 * @throws	Exception
	 */
	@RequestMapping("/system/system/menu/cha/chaList3_json.do")
	public ModelAndView chaList3_json(@ModelAttribute("searchVO") ChaVO searchVO) throws Exception {
		List<ChaVO> dataList = chaService.selectList3(searchVO);
		return makeGridJsonData(dataList, dataList.size(), searchVO);
	}


	/**
	 * 문화재청 조회
	 * @param	ChaVO searchVO
	 * @return	ModelAndView
	 * @throws	Exception
	 */
	@RequestMapping("/system/system/menu/cha/selectDetail.do")
	public ModelAndView selectDetail(@ModelAttribute("searchVO") ChaVO searchVO) throws Exception {
		return makeJsonData(chaService.selectDetail(searchVO));
	}
	
	/**
	 * 문화재청 정렬순서저장
	 * @param	ChaVO dataVO
	 * @param	Model model
	 * @param	BindingResult bindingResult
	 * @return	ModelAndView
	 * @throws	Exception
	 */
	@RequestMapping("/system/system/menu/cha/saveSortOrder.do")
	public ModelAndView saveSortOrder(@ModelAttribute("dataVO") ChaVO dataVO, Model model, BindingResult bindingResult) throws Exception {
		// list 유효성 체크
		validateList(dataVO.getGridDataList(), bindingResult);
		if(bindingResult.hasErrors()) {
			return makeFailJsonData(getListErrorMsg(bindingResult));
		}
		
		return makeJsonDataByResultCnt(chaService.updateSortOrder(dataVO));
	}
	
	/**
	 * 문화재청 삭제
	 * @param	ChaVO dataVO
	 * @param	Model model
	 * @return	ModelAndView
	 * @throws	Exception
	 */
	@RequestMapping("/system/system/menu/cha/deleteCha.do")
	public ModelAndView deleteCha(@ModelAttribute("dataVO") ChaVO dataVO, Model model) throws Exception {
		int resultCnt = chaService.deleteCha(dataVO);
		if(resultCnt == 0) {
			return makeFailJsonData();
		}
		return makeSuccessJsonData();
		//return makeJsonDataByResultCnt(chaService.deleteCha(dataVO));
	}
	
	/**
	 * 문화재청 저장(임무)
	 * @param	ChaVO dataVO
	 * @param	Model model
	 * @param	BindingResult bindingResult
	 * @return	ModelAndView
	 * @throws	Exception
	 */
	@RequestMapping("/system/system/menu/cha/saveCha.do")
	public ModelAndView saveCha(@ModelAttribute("dataVO") ChaVO dataVO, Model model, BindingResult bindingResult) throws Exception {
		System.out.println("컨트롤러");

		return makeJsonDataByResultCnt(chaService.saveData(dataVO));
	}

	/**
	 * 문화재청 저장(임무 첨부파일)
	 * @param	ChaVO dataVO
	 * @param	Model model
	 * @param	BindingResult bindingResult
	 * @return	ModelAndView
	 * @throws	Exception
	 */
	@RequestMapping("/system/system/menu/cha/saveCha11.do")
	public void saveData11 (
			final MultipartHttpServletRequest multiRequest,
			HttpServletResponse response,
			@ModelAttribute("dataVO") ChaVO dataVO,
			Model model, BindingResult bindingResult) throws Exception {
		System.out.println("컨트롤러");
		System.out.println(dataVO.getAtchFileId());
		System.out.println(dataVO.getMatchFileId());
		System.out.println("dataVO : " + dataVO);
		dataVO.setAtchFileId("");


		// 첨부파일 유효성 체크
		String errMsg;
		errMsg = fileUtil.validation(multiRequest, "upFile1", dataVO.getAtchFileId(), (long)5242880, 5, PropertyUtil.getProperty("fileUpload.allowFileExts").split(","), dataVO.getChkAttachFiles());
		if(errMsg != null) {
			resultHandling(false, multiRequest, response, dataVO, errMsg);
			return;
		}

		System.out.println("dataVO : " + dataVO);

		// 첨부파일을 List로 변환
		List<FileVO> fileList1 = fileUtil.parseFileInf(multiRequest, "upFile1", "example", dataVO.getAtchFileId());

		try {
			// 등록
			if(!CommonUtil.isEmpty(dataVO.getMatchFileId())) {
				// 수정
//				List<FileVO> deleteFileList = chaService.saveData11(dataVO, fileList1);
//				try {
//					for(FileVO fileVO : deleteFileList) {
//						EgovFileTool.deletePath(fileVO.getFileStreCours()+fileVO.getStreFileNm());
//					}
//				} catch(RuntimeException re) {
//					log.error("error : "+re.getCause());
//				} catch(Exception e) {
//					log.error("error : "+e.getCause());
//				}
				System.out.println("수정쪽..");
			} else {
				dataVO.setUserId(SessionUtil.getUserId());
				chaService.saveData11(dataVO, fileList1);
			}
		} catch(SQLException sqe) {
			log.error("error : "+sqe.getCause());
			// 업로드한 파일들 물리적으로 삭제
			for(FileVO fileVO : fileList1) {
				EgovFileTool.deletePath(fileVO.getFileStreCours()+fileVO.getStreFileNm());
			}
			resultHandling(false, multiRequest, response, dataVO);
		} catch(Exception e) {
			log.error(e.getMessage());
			// 업로드한 파일들 물리적으로 삭제
			for(FileVO fileVO : fileList1) {
				EgovFileTool.deletePath(fileVO.getFileStreCours()+fileVO.getStreFileNm());
			}
		}
		resultHandling(true, multiRequest, response, dataVO);

		//return makeJsonDataByResultCnt(chaService.saveData11(dataVO));
	}

	/**
	 * 문화재청 저장(비전)
	 * @param	ChaVO dataVO
	 * @param	Model model
	 * @param	BindingResult bindingResult
	 * @return	ModelAndView
	 * @throws	Exception
	 */
	@RequestMapping("/system/system/menu/cha/saveCha2.do")
	public ModelAndView saveCha2(@ModelAttribute("dataVO") ChaVO dataVO, Model model, BindingResult bindingResult) throws Exception {
		System.out.println("컨트롤러");

		return makeJsonDataByResultCnt(chaService.saveData2(dataVO));
	}

	/**
	 * 문화재청 저장(비전 첨부파일)
	 * @param	ChaVO dataVO
	 * @param	Model model
	 * @param	BindingResult bindingResult
	 * @return	ModelAndView
	 * @throws	Exception
	 */
	@RequestMapping("/system/system/menu/cha/saveCha22.do")
	public void saveData22 (
			final MultipartHttpServletRequest multiRequest,
			HttpServletResponse response,
			@ModelAttribute("dataVO") ChaVO dataVO,
			Model model, BindingResult bindingResult) throws Exception {
		System.out.println("컨트롤러");
		System.out.println(dataVO.getAtchFileId());
		System.out.println(dataVO.getMatchFileId());
		System.out.println("dataVO : " + dataVO);
		dataVO.setAtchFileId("");


		// 첨부파일 유효성 체크
		String errMsg;
		errMsg = fileUtil.validation(multiRequest, "upFile1", dataVO.getAtchFileId(), (long)5242880, 5, PropertyUtil.getProperty("fileUpload.allowFileExts").split(","), dataVO.getChkAttachFiles());
		if(errMsg != null) {
			resultHandling(false, multiRequest, response, dataVO, errMsg);
			return;
		}

		System.out.println("dataVO : " + dataVO);

		// 첨부파일을 List로 변환
		List<FileVO> fileList1 = fileUtil.parseFileInf(multiRequest, "upFile1", "example", dataVO.getAtchFileId());

		try {
			// 등록
			if(!CommonUtil.isEmpty(dataVO.getMatchFileId())) {
				// 수정
//				List<FileVO> deleteFileList = chaService.saveData11(dataVO, fileList1);
//				try {
//					for(FileVO fileVO : deleteFileList) {
//						EgovFileTool.deletePath(fileVO.getFileStreCours()+fileVO.getStreFileNm());
//					}
//				} catch(RuntimeException re) {
//					log.error("error : "+re.getCause());
//				} catch(Exception e) {
//					log.error("error : "+e.getCause());
//				}
				System.out.println("수정쪽..");
			} else {
				dataVO.setUserId(SessionUtil.getUserId());
				chaService.saveData22(dataVO, fileList1);
			}
		} catch(SQLException sqe) {
			log.error("error : "+sqe.getCause());
			// 업로드한 파일들 물리적으로 삭제
			for(FileVO fileVO : fileList1) {
				EgovFileTool.deletePath(fileVO.getFileStreCours()+fileVO.getStreFileNm());
			}
			resultHandling(false, multiRequest, response, dataVO);
		} catch(Exception e) {
			log.error(e.getMessage());
			// 업로드한 파일들 물리적으로 삭제
			for(FileVO fileVO : fileList1) {
				EgovFileTool.deletePath(fileVO.getFileStreCours()+fileVO.getStreFileNm());
			}
		}
		resultHandling(true, multiRequest, response, dataVO);

		//return makeJsonDataByResultCnt(chaService.saveData11(dataVO));
	}

	/**
	 * 문화재청 저장(전략목표 윗부분)
	 * @param	ChaVO dataVO
	 * @param	Model model
	 * @param	BindingResult bindingResult
	 * @return	ModelAndView
	 * @throws	Exception
	 */
	@RequestMapping("/system/system/menu/cha/saveCha3.do")
	public void saveData3 (
			final MultipartHttpServletRequest multiRequest,
			HttpServletResponse response,
			@ModelAttribute("dataVO") ChaVO dataVO,
			Model model, BindingResult bindingResult) throws Exception {
		System.out.println("컨트롤러");
		System.out.println(dataVO.getAtchFileId());
		System.out.println(dataVO.getMatchFileId());
		System.out.println("dataVO : " + dataVO);
		dataVO.setAtchFileId("");


		// 첨부파일 유효성 체크
		String errMsg;
		errMsg = fileUtil.validation(multiRequest, "upFile2", dataVO.getAtchFileId(), (long)5242880, 5, PropertyUtil.getProperty("fileUpload.allowFileExts").split(","), dataVO.getChkAttachFiles());
		if(errMsg != null) {
			resultHandling(false, multiRequest, response, dataVO, errMsg);
			return;
		}

		System.out.println("dataVO : " + dataVO);

		// 첨부파일을 List로 변환
		List<FileVO> fileList2 = fileUtil.parseFileInf(multiRequest, "upFile2", "example", dataVO.getAtchFileId());

		try {
			// 등록
			if(!CommonUtil.isEmpty(dataVO.getMatchFileId())) {
				 //수정
//				List<FileVO> deleteFileList = chaService.saveData3(dataVO, fileList2);
//				try {
//					for(FileVO fileVO : deleteFileList) {
//						EgovFileTool.deletePath(fileVO.getFileStreCours()+fileVO.getStreFileNm());
//					}
//				} catch(RuntimeException re) {
//					log.error("error : "+re.getCause());
//				} catch(Exception e) {
//					log.error("error : "+e.getCause());
//				}
				System.out.println("수정쪽..");
			} else {
				dataVO.setUserId(SessionUtil.getUserId());
				chaService.saveData3(dataVO, fileList2);
			}
		} catch(SQLException sqe) {
			log.error("error : "+sqe.getCause());
			// 업로드한 파일들 물리적으로 삭제
			for(FileVO fileVO : fileList2) {
				EgovFileTool.deletePath(fileVO.getFileStreCours()+fileVO.getStreFileNm());
			}
			resultHandling(false, multiRequest, response, dataVO);
		} catch(Exception e) {
			log.error(e.getMessage());
			// 업로드한 파일들 물리적으로 삭제
			for(FileVO fileVO : fileList2) {
				EgovFileTool.deletePath(fileVO.getFileStreCours()+fileVO.getStreFileNm());
			}
		}
		resultHandling(true, multiRequest, response, dataVO);

		//return makeJsonDataByResultCnt(chaService.saveData11(dataVO));
	}

	/**
	 * 문화재청 저장(전략목표 아래부분)
	 * @param	ChaVO dataVO
	 * @param	Model model
	 * @param	BindingResult bindingResult
	 * @return	ModelAndView
	 * @throws	Exception
	 */
	@RequestMapping("/system/system/menu/cha/saveCha33.do")
	public ModelAndView saveData33(@ModelAttribute("dataVO") ChaVO dataVO, Model model, BindingResult bindingResult) throws Exception {

		int resultCnt = chaService.saveData33(dataVO);
		if(resultCnt == 0) {
			return makeFailJsonData();
		}
		return makeSuccessJsonData();
	}

	/**
	 * 문화재청 저장(성과목표 윗부분)
	 * @param	ChaVO dataVO
	 * @param	Model model
	 * @param	BindingResult bindingResult
	 * @return	ModelAndView
	 * @throws	Exception
	 */
	@RequestMapping("/system/system/menu/cha/saveCha4.do")
	public void saveData4 (
			final MultipartHttpServletRequest multiRequest,
			HttpServletResponse response,
			@ModelAttribute("dataVO") ChaVO dataVO,
			Model model, BindingResult bindingResult) throws Exception {
		System.out.println("컨트롤러");
		System.out.println("dataVO : " + dataVO);
		dataVO.setAtchFileId("");


		// 첨부파일 유효성 체크
		String errMsg;
		errMsg = fileUtil.validation(multiRequest, "upFile2", dataVO.getAtchFileId(), (long)5242880, 5, PropertyUtil.getProperty("fileUpload.allowFileExts").split(","), dataVO.getChkAttachFiles());
		if(errMsg != null) {
			resultHandling(false, multiRequest, response, dataVO, errMsg);
			return;
		}

		System.out.println("dataVO : " + dataVO);

		// 첨부파일을 List로 변환
		List<FileVO> fileList2 = fileUtil.parseFileInf(multiRequest, "upFile2", "example", dataVO.getAtchFileId());

		try {
			// 등록
			if(!CommonUtil.isEmpty(dataVO.getMatchFileId())) {
				//수정
//				List<FileVO> deleteFileList = chaService.saveData3(dataVO, fileList2);
//				try {
//					for(FileVO fileVO : deleteFileList) {
//						EgovFileTool.deletePath(fileVO.getFileStreCours()+fileVO.getStreFileNm());
//					}
//				} catch(RuntimeException re) {
//					log.error("error : "+re.getCause());
//				} catch(Exception e) {
//					log.error("error : "+e.getCause());
//				}
				System.out.println("수정쪽..");
			} else {
				dataVO.setUserId(SessionUtil.getUserId());
				chaService.saveData4(dataVO, fileList2);
			}
		} catch(SQLException sqe) {
			log.error("error : "+sqe.getCause());
			// 업로드한 파일들 물리적으로 삭제
			for(FileVO fileVO : fileList2) {
				EgovFileTool.deletePath(fileVO.getFileStreCours()+fileVO.getStreFileNm());
			}
			resultHandling(false, multiRequest, response, dataVO);
		} catch(Exception e) {
			log.error(e.getMessage());
			// 업로드한 파일들 물리적으로 삭제
			for(FileVO fileVO : fileList2) {
				EgovFileTool.deletePath(fileVO.getFileStreCours()+fileVO.getStreFileNm());
			}
		}
		resultHandling(true, multiRequest, response, dataVO);

		//return makeJsonDataByResultCnt(chaService.saveData11(dataVO));
	}

	/**
	 * 문화재청 저장(성과목표 아래부분)
	 * @param	ChaVO dataVO
	 * @param	Model model
	 * @param	BindingResult bindingResult
	 * @return	ModelAndView
	 * @throws	Exception
	 */
	@RequestMapping("/system/system/menu/cha/saveCha44.do")
	public ModelAndView saveData44(@ModelAttribute("dataVO") ChaVO dataVO, Model model, BindingResult bindingResult) throws Exception {

		int resultCnt = chaService.saveData44(dataVO);
		if(resultCnt == 0) {
			return makeFailJsonData();
		}
		return makeSuccessJsonData();
	}


	/**
	 * 첨부파일 팝업 화면1
	 * @param	ChaVO searchVO
	 * @param	Model model
	 * @return	String
	 * @throws	Exception
	 */
	@RequestMapping(value="/system/system/menu/cha/chaListDetail.do")
	public String chaListDetail(@ModelAttribute("searchVO") ChaVO searchVO, Model model) throws Exception {
		ChaVO dataVO = chaService.selectDetail(searchVO);
		model.addAttribute("dataVO", dataVO);
		model.addAttribute("searchVO", searchVO);
		System.out.println("디테일 : " + dataVO);
		return "/system/system/menu/cha/chaListDetail." + searchVO.getLayout();
	}


	/**
	 * 첨부파일 팝업 화면2
	 * @param	ChaVO searchVO
	 * @param	Model model
	 * @return	String
	 * @throws	Exception
	 */
	@RequestMapping(value="/system/system/menu/cha/popAtchFile.do")
	public String popGuideComment(@ModelAttribute("searchVO") ChaVO ChaVO, Model model) {
		//ChaVO dataVO = chaService.selectDetail(searchVO);
		//model.addAttribute("dataVO", dataVO);
		//model.addAttribute("searchVO", searchVO);
		System.out.println("팝업");
		System.out.println("ChaVO : " + ChaVO);
		return "/system/system/menu/cha/popAtchFile";
	}

	/**
	 * 첨부파일 팝업 화면11
	 * @param	ChaVO searchVO
	 * @param	Model model
	 * @return	String
	 * @throws	Exception
	 */
	@RequestMapping(value="/system/system/menu/cha/chaListDetail2.do")
	public String chaListDetail2(@ModelAttribute("searchVO") ChaVO searchVO, Model model) throws Exception {
		ChaVO dataVO = chaService.selectDetail(searchVO);
		model.addAttribute("dataVO", dataVO);
		model.addAttribute("searchVO", searchVO);
		System.out.println("디테일");
		return "/system/system/menu/cha/chaListDetail2." + searchVO.getLayout();
	}


	/**
	 * 첨부파일 팝업 화면22
	 * @param	ChaVO searchVO
	 * @param	Model model
	 * @return	String
	 * @throws	Exception
	 */
	@RequestMapping(value="/system/system/menu/cha/popAtchFile2.do")
	public String popGuideComment2(@ModelAttribute("searchVO") ChaVO ChaVO, Model model) {
		//ChaVO dataVO = chaService.selectDetail(searchVO);
		//model.addAttribute("dataVO", dataVO);
		//model.addAttribute("searchVO", searchVO);
		System.out.println("팝업");
		System.out.println("ChaVO : " + ChaVO);
		return "/system/system/menu/cha/popAtchFile2";
	}


	/**
	 * 전략목표 첨부파일=
	 * @param	ChaVO searchVO
	 * @param	Model model
	 * @return	String
	 * @throws	Exception
	 */
	@RequestMapping(value="/system/system/menu/cha/chaAtchFile.do")
	public String chaAtchFile(@ModelAttribute("searchVO") ChaVO ChaVO, Model model) {
		//ChaVO dataVO = chaService.selectDetail(searchVO);
		//model.addAttribute("dataVO", dataVO);
		//model.addAttribute("searchVO", searchVO);
		System.out.println("팝업");
		System.out.println("ChaVO : " + ChaVO);
		return "/system/system/menu/cha/chaAtchFile";
	}

	/**
	 * 성과목표 조회
	 * @param	ChaVO searchVO
	 * @return	ModelAndView
	 * @throws	Exception
	 */
	@RequestMapping("/system/system/menu/cha/selectDetail7.do")
	public ModelAndView selectDetail7(@ModelAttribute("searchVO") ChaVO searchVO) throws Exception {
		return makeJsonData(chaService.selectDetail7(searchVO));
	}

	/**
	 * 성과목표 조회
	 * @param	ChaVO searchVO
	 * @return	ModelAndView
	 * @throws	Exception
	 */
	@RequestMapping("/system/system/menu/cha/selectDetail77.do")
	public ModelAndView selectDetail77(@ModelAttribute("searchVO") ChaVO searchVO) throws Exception {
		return makeJsonData(chaService.selectDetail77(searchVO));
	}

	/*@RequestMapping("/system/system/menu/cha/testAtchFile.do")
	public String testAtchFile(@ModelAttribute("searchVO") ChaVO chaVO,  Model model) {
		//model.addAttribute("searchVO",IdeaUsVO);
		System.out.println("ChaVO@@@@@@ : " + chaVO);
		return "testAtchFile";
	}*/

	/**
	 * 첨부파일에 대한 목록을 조회한다.
	 * @param ChaVO
	 * @param atchFileId
	 * @param sessionVO
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/system/system/menu/cha/testAtchFile.do")
	public String testAtchFile(@ModelAttribute("searchVO") FileVO fileVO, @RequestParam Map<String, Object> commandMap, ModelMap model) throws Exception {
		String atchFileId = (String)commandMap.get("param_atchFileId");

		fileVO.setAtchFileId(atchFileId);
		List<FileVO> fileList = fileService.selectFileInfs(fileVO);

		model.addAttribute("fileList", fileList);
		model.addAttribute("updateFlag", "N");
		model.addAttribute("fileListCnt", fileList.size());
		model.addAttribute("atchFileId", atchFileId);

		return "/system/system/menu/cha/chaFileList";
	}

}

