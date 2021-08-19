/*************************************************************************
* CLASS 명	: FileExampleController.java
* 작 업 자	: kimyh
* 작 업 일	: 2017. 12. 26.
* 기	능	: 파일 첨부 예제용 Controller
* ---------------------------- 변 경 이 력 --------------------------------
* 번호	작 업 자		작	업	일			변 경 내 용				비고
* ----	---------	----------------	---------------------	-----------
*	1	kimyh		2017. 12. 26.		최 초 작 업
**************************************************************************/
package kr.ispark.example.web;

import java.io.IOException;
import java.nio.file.NotDirectoryException;
import java.sql.SQLException;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import egovframework.com.cmm.service.FileVO;
import egovframework.com.utl.sim.service.EgovFileTool;
import kr.ispark.common.util.CommonUtil;
import kr.ispark.common.util.CustomEgovFileMngUtil;
import kr.ispark.common.util.PropertyUtil;
import kr.ispark.common.util.SessionUtil;
import kr.ispark.common.web.BaseController;
import kr.ispark.example.service.ExampleBoardVO;
import kr.ispark.example.service.impl.ExampleBoardServiceImpl;

@Controller
public class ExampleBoardController extends BaseController {

	@Autowired
	private ExampleBoardServiceImpl exampleBoardService;

	@Resource(name="CustomEgovFileMngUtil")
	private CustomEgovFileMngUtil fileUtil;

	/**
	 * 게시물 목록 화면 (파일 다중 첨부)
	 * @param	ExampleBoardVO searchVO
	 * @param	Model model
	 * @return	String
	 * @throws	Exception
	 */
	@RequestMapping(value="/example/board/boardList.do")
	public String boardList(@ModelAttribute("searchVO") ExampleBoardVO searchVO, Model model) throws Exception {
		return "/example/board/boardList." + searchVO.getLayout();
	}

	/**
	 * 그리드 조회(json, 파일 다중 첨부)
	 */
	@RequestMapping(value="/example/board/boardList_json.do")
	public ModelAndView boardList_json(@ModelAttribute("searchVO") ExampleBoardVO searchVO)throws Exception {

		List<ExampleBoardVO> dataList = exampleBoardService.selectList(searchVO);
		int listCnt = exampleBoardService.selectListCount(searchVO);

		return makeGridJsonData(dataList, listCnt, searchVO);
	}

	/**
	 * 게시물 상세 화면 (파일 다중 첨부)
	 * @param	searchVO
	 * @param	model
	 * @return
	 * @throws	Exception
	 */
	@RequestMapping(value="/example/board/boardDetail.do")
	public String boardDetail(@ModelAttribute("dataVO") ExampleBoardVO searchVO, Model model) throws Exception {
		model.addAttribute("dataVO", exampleBoardService.selectBoard(searchVO));
		return "/example/board/boardDetail." + searchVO.getLayout();
	}

	/**
	 * 게시물 등록 화면 (파일 다중 첨부)
	 * @param	searchVO
	 * @param	model
	 * @return
	 * @throws	Exception
	 */
	@RequestMapping(value="/example/board/boardForm.do")
	public String boardForm(@ModelAttribute("dataVO") ExampleBoardVO dataVO, Model model) throws Exception {
		return "/example/board/boardForm." + dataVO.getLayout();
	}

	/**
	 * 게시물 수정 화면 (파일 다중 첨부)
	 * @param	searchVO
	 * @param	model
	 * @return
	 * @throws	Exception
	 */
	@RequestMapping(value="/example/board/boardUpdateForm.do")
	public String boardUpdateForm(@ModelAttribute("dataVO") ExampleBoardVO dataVO, Model model) throws Exception {
		model.addAttribute("dataVO", exampleBoardService.selectBoard(dataVO));
		return "/example/board/boardForm." + dataVO.getLayout();
	}

	/**
	 * 게시물 저장 (파일 다중 첨부)
	 * @param multiRequest
	 * @param dataVO
	 * @param model
	 * @param bindingResult
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/example/board/saveBoard.do")
	public void saveBoard (
			final MultipartHttpServletRequest multiRequest,
			HttpServletResponse response,
			@ModelAttribute("dataVO") ExampleBoardVO dataVO,
			Model model, BindingResult bindingResult) throws Exception {

		// 유효성 체크
		beanValidator.validate(dataVO, bindingResult);
		if(bindingResult.hasErrors()){
			resultHandling(false, multiRequest, response, dataVO, getListErrorMsg(bindingResult));
			return;
		}

		// 수정
		if(!CommonUtil.isEmpty(dataVO.getId())) {
			ExampleBoardVO attachVO = exampleBoardService.selectBoard(dataVO);
			dataVO.setAtchFileId(attachVO.getAtchFileId());
			dataVO.setAtchFileId2(attachVO.getAtchFileId2());
		}

		// 첨부파일 유효성 체크
		String errMsg;
		errMsg = fileUtil.validation(multiRequest, "upFile1", dataVO.getAtchFileId(), (long)5242880, 5, PropertyUtil.getProperty("fileUpload.allowFileExts").split(","), dataVO.getChkAttachFiles());
		if(errMsg != null) {
			resultHandling(false, multiRequest, response, dataVO, errMsg);
			return;
		}

		errMsg = fileUtil.validation(multiRequest, "upFile2", dataVO.getAtchFileId2(), null, 1, null, dataVO.getChkAttachFiles2());
		if(errMsg != null) {
			resultHandling(false, multiRequest, response, dataVO, errMsg);
			return;
		}

		// 첨부파일을 List로 변환
		List<FileVO> fileList1 = fileUtil.parseFileInf(multiRequest, "upFile1", "example", dataVO.getAtchFileId());
		List<FileVO> fileList2 = fileUtil.parseFileInf(multiRequest, "upFile2", "example", dataVO.getAtchFileId2());

		try {
			// 등록
			if(CommonUtil.isEmpty(dataVO.getId())) {
				dataVO.setUserId(SessionUtil.getUserId());
				exampleBoardService.insertData(dataVO, fileList1, fileList2);
			} else {
				// 수정
				List<FileVO> deleteFileList = exampleBoardService.updateData(dataVO, fileList1, fileList2);
				try {
					/*
					 * 삭제 체크한 파일들을 물리적으로 삭제.
					 * DB에서는 삭제된 상태이기 때문에 파일 삭제 중 오류가 발생해도 성공으로 return
					 */
					for(FileVO fileVO : deleteFileList) {
						EgovFileTool.deletePath(fileVO.getFileStreCours()+fileVO.getStreFileNm());
					}
				} catch(RuntimeException re) {
					log.error("error : "+re.getCause());
				} catch(Exception e) {
					log.error("error : "+e.getCause());
				}
			}
		} catch(SQLException sqe) {
			log.error("error : "+sqe.getCause());
			// 업로드한 파일들 물리적으로 삭제
			for(FileVO fileVO : fileList1) {
				EgovFileTool.deletePath(fileVO.getFileStreCours()+fileVO.getStreFileNm());
			}
			// 첨부파일2 삭제
			for(FileVO fileVO : fileList2) {
				EgovFileTool.deletePath(fileVO.getFileStreCours()+fileVO.getStreFileNm());
			}
			resultHandling(false, multiRequest, response, dataVO);
		} catch(Exception e) {
			log.error("error : "+e.getCause());
			// 업로드한 파일들 물리적으로 삭제
			for(FileVO fileVO : fileList1) {
				EgovFileTool.deletePath(fileVO.getFileStreCours()+fileVO.getStreFileNm());
			}
			// 첨부파일2 삭제
			for(FileVO fileVO : fileList2) {
				EgovFileTool.deletePath(fileVO.getFileStreCours()+fileVO.getStreFileNm());
			}
			resultHandling(false, multiRequest, response, dataVO);
		}

		resultHandling(true, multiRequest, response, dataVO);
	}

	/**
	 * 게시물 일괄 삭제 (파일 다중 첨부)
	 * @param	dataVO
	 * @param	model
	 * @return	ModelAndView
	 * @throws	Exception
	 */
	@RequestMapping(value="/example/board/deleteBoard.do")
	public ModelAndView deleteBoard(@ModelAttribute("dataVO") ExampleBoardVO dataVO, Model model) throws Exception {
		List<FileVO> deleteFileList = exampleBoardService.deleteData(dataVO);
		try {
			/*
			 * 삭제 체크한 파일들을 물리적으로 삭제.
			 * DB에서는 삭제된 상태이기 때문에 파일 삭제 중 오류가 발생해도 성공으로 return
			 */
			for(FileVO fileVO : deleteFileList) {
				EgovFileTool.deletePath(fileVO.getFileStreCours()+fileVO.getStreFileNm());
			}
		} catch(RuntimeException re) {
			log.error("error : "+re.getCause());
		} catch(Exception e) {
			log.error("error : "+e.getCause());
		}

		return makeSuccessJsonData();
	}
}
