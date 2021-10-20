/*************************************************************************
* CLASS 명	: BoardController.java
* 작 업 자	: kimyh
* 작 업 일	: 2018. 04. 17.
* 기	능	: 게시판 Controller
* ---------------------------- 변 경 이 력 --------------------------------
* 번호	작 업 자		작	업	일			변 경 내 용				비고
* ----	---------	----------------	---------------------	-----------
*	1	kimyh		2018. 04. 17.		최 초 작 업
**************************************************************************/
package kr.ispark.system.system.board.web;

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
import kr.ispark.system.system.board.service.BoardVO;
import kr.ispark.system.system.board.service.impl.BoardServiceImpl;

@Controller
public class BoardController extends BaseController {

	@Autowired
	private BoardServiceImpl boardService;

	@Resource(name="CustomEgovFileMngUtil")
	private CustomEgovFileMngUtil fileUtil;

	/**
	 * 게시물 목록 화면
	 * @param	BoardVO searchVO
	 * @param	Model model
	 * @return	String
	 * @throws	Exception
	 */
	@RequestMapping(value="/system/system/board/boardList.do")
	public String boardList(@ModelAttribute("searchVO") BoardVO searchVO, Model model) throws Exception {
		model.addAttribute("boardSetting", boardService.selectBoardSetting(searchVO));
		return "/system/system/board/boardList." + searchVO.getLayout();
	}

	/**
	 * 게시물 목록 조회(json)
	 * @param	BoardVO searchVO
	 * @return	ModelAndView
	 * @throws	Exception
	 */
	@RequestMapping(value="/system/system/board/boardList_json.do")
	public ModelAndView boardList_json(@ModelAttribute BoardVO searchVO) throws Exception {
		// 게시판이 존재하는지 체크
		boardService.selectBoardSetting(searchVO);

		List<BoardVO> dataList = boardService.selectList(searchVO);
		int listCnt = boardService.selectListCount(searchVO);

		return makeGridJsonData(dataList, listCnt, searchVO);
	}

	/**
	 * 게시물 상세 화면
	 * @param	BoardVO searchVO
	 * @param	Model model
	 * @return	String
	 * @throws	Exception
	 */
	@RequestMapping(value="/system/system/board/boardDetail.do")
	public String boardDetail(@ModelAttribute BoardVO searchVO, Model model) throws Exception {
		System.out.println(searchVO.getSeq());
		BoardVO boardSetting = boardService.selectBoardSetting(searchVO);
		BoardVO dataVO = boardService.selectBoard(searchVO, true);
		if(!dataVO.isReadable()) {
			throw new Exception(egovMessageSource.getMessage("info.noAuth.msg"));
		}

		model.addAttribute("boardSetting", boardSetting);
		model.addAttribute("searchVO", searchVO);
		model.addAttribute("dataVO", dataVO);
		return "/system/system/board/boardDetail." + searchVO.getLayout();
	}

	/**
	 * 게시물 등록 화면
	 * @param	BoardVO dataVO
	 * @param	Model model
	 * @return	String
	 * @throws	Exception
	 */
	@RequestMapping(value="/system/system/board/boardForm.do")
	public String boardForm(@ModelAttribute("dataVO") BoardVO dataVO, Model model) throws Exception {
		BoardVO boardSetting = boardService.selectBoardSetting(dataVO);
		model.addAttribute("boardSetting", boardSetting);
		model.addAttribute("searchVO", dataVO);
		return "/system/system/board/boardForm." + dataVO.getLayout();
	}

	/**
	 * 게시물 수정 화면
	 * @param	BoardVO searchVO
	 * @param	Model model
	 * @return	String
	 * @throws	Exception
	 */
	@RequestMapping(value="/system/system/board/boardUpdateForm.do")
	public String boardUpdateForm(@ModelAttribute("dataVO") BoardVO searchVO, Model model) throws Exception {
		BoardVO boardSetting = boardService.selectBoardSetting(searchVO);
		BoardVO dataVO = boardService.selectBoard(searchVO);
		if(!dataVO.isReadable()) {
			throw new Exception(egovMessageSource.getMessage("info.noAuth.msg"));
		}

		model.addAttribute("boardSetting", boardSetting);
		model.addAttribute("dataVO", dataVO);
		model.addAttribute("searchVO", searchVO);
		return "/system/system/board/boardForm." + searchVO.getLayout();
	}

	/**
	 * 게시물 답글 화면
	 * @param	BoardVO searchVO
	 * @param	Model model
	 * @return	String
	 * @throws	Exception
	 */
	@RequestMapping(value="/system/system/board/boardReplyForm.do")
	public String boardReplyForm(@ModelAttribute("dataVO") BoardVO searchVO, Model model) throws Exception {
		BoardVO boardSetting = boardService.selectBoardSetting(searchVO);
		BoardVO boardVO = boardService.selectBoard(searchVO);
		if(!boardVO.isReadable()) {
			throw new Exception(egovMessageSource.getMessage("info.noAuth.msg"));
		}

		BoardVO dataVO = new BoardVO();
		dataVO.setBoardId(boardVO.getBoardId());
		dataVO.setUpSeq(boardVO.getSeq());
		dataVO.setSeq(-1);
		dataVO.setTitle("Re: " + boardVO.getTitle());

		model.addAttribute("boardSetting", boardSetting);
		model.addAttribute("dataVO", dataVO);
		model.addAttribute("searchVO", searchVO);
		return "/system/system/board/boardForm." + searchVO.getLayout();
	}

	/**
	 * 게시물 저장
	 * @param MultipartHttpServletRequest multiRequest
	 * @param HttpServletResponse dataVO
	 * @param BoardVO dataVO
	 * @param Model model
	 * @param BindingResult bindingResult
	 * @throws Exception
	 */
	@RequestMapping(value="/system/system/board/saveBoard.do")
	public void saveBoard (
			final MultipartHttpServletRequest multiRequest,
			HttpServletResponse response,
			@ModelAttribute BoardVO dataVO,
			Model model, BindingResult bindingResult) throws Exception {

		BoardVO boardSetting = boardService.selectBoardSetting(dataVO);

		// 유효성 체크
		beanValidator.validate(dataVO, bindingResult);
		if(bindingResult.hasErrors()){
			resultHandling(false, multiRequest, response, dataVO, getListErrorMsg(bindingResult));
			return;
		}

		// 등록 가능 여부 체크
		if(!boardSetting.isWritable()) {
			resultHandling(false, multiRequest, response, dataVO, egovMessageSource.getMessage("info.noAuth.msg"));
			return;
		}

		// 답글인 경우 권한 체크
		if(dataVO.getUpSeq() != -1 && !boardSetting.isReplyWritable()) {
			resultHandling(false, multiRequest, response, dataVO, egovMessageSource.getMessage("info.noAuth.msg"));
			return;
		}

		// 수정
		if(dataVO.getSeq() != -1) {
			BoardVO boardVO = boardService.selectBoard(dataVO);
			dataVO.setAtchFileId(boardVO.getAtchFileId());

			// 수정 권한 체크
			if(!boardVO.isEditable()) {
				resultHandling(false, multiRequest, response, dataVO, egovMessageSource.getMessage("info.noAuth.msg"));
				return;
			}
		} else if(!SessionUtil.hasRole("01")) {
			// 중복 게시 방지 시간 체크
			dataVO.setUserId(SessionUtil.getUserId());
			if(boardService.selectDuplCount(dataVO) > 0) {
				resultHandling(false, multiRequest, response, dataVO, egovMessageSource.getMessage("system.system.board.duplCheck", new String[] {String.valueOf(boardSetting.getDuplLimitMin())}));
				return;
			}
		}

		// 첨부파일 유효성 체크
		String errMsg = null;
		if(boardSetting.getUseAtchFileYn().equals("Y")) {
			errMsg = fileUtil.validation(multiRequest, "upFile1", dataVO.getAtchFileId(), boardSetting.getMaxUploadSize(), boardSetting.getMaxUploadCnt(), PropertyUtil.getProperty("fileUpload.allowFileExts").split(","), dataVO.getChkAttachFiles());
		}
		if(errMsg != null) {
			resultHandling(false, multiRequest, response, dataVO, errMsg);
			return;
		}

		// 첨부파일을 List로 변환
		List<FileVO> fileList = null;
		if(boardSetting.getUseAtchFileYn().equals("Y")) {
			fileList = fileUtil.parseFileInf(multiRequest, "upFile1", "example", dataVO.getAtchFileId());
		}

		try {
			// 등록
			if(dataVO.getSeq() == -1) {
				boardService.insertData(dataVO, fileList);
			} else {
				// 수정
				List<FileVO> deleteFileList = boardService.updateData(dataVO, fileList);
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
			for(FileVO fileVO : fileList) {
				EgovFileTool.deletePath(fileVO.getFileStreCours()+fileVO.getStreFileNm());
			}
			resultHandling(false, multiRequest, response, dataVO);	
		} catch(Exception e) {
			log.error("error : "+e.getCause());
			// 업로드한 파일들 물리적으로 삭제
			for(FileVO fileVO : fileList) {
				EgovFileTool.deletePath(fileVO.getFileStreCours()+fileVO.getStreFileNm());
			}
			resultHandling(false, multiRequest, response, dataVO);
		}

		resultHandling(true, multiRequest, response, dataVO);
	}

	/**
	 * 게시물 일괄 삭제
	 * @param	BoardVO dataVO
	 * @param	Model model
	 * @return	ModelAndView
	 * @throws	Exception
	 */
	@RequestMapping(value="/system/system/board/deleteBoardAll.do")
	public ModelAndView deleteBoardAll(@ModelAttribute BoardVO dataVO, Model model) throws Exception {

		// 일괄 삭제는 관리자만 가능
		if(!SessionUtil.hasRole("01")) {
			return makeFailJsonData(egovMessageSource.getMessage("info.noAuth.msg"));
		}

		List<FileVO> deleteFileList = boardService.deleteData(dataVO);
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

	/**
	 * 게시물 삭제
	 * @param	BoardVO dataVO
	 * @param	Model model
	 * @return	ModelAndView
	 * @throws	Exception
	 */
	@RequestMapping(value="/system/system/board/deleteBoard.do")
	public ModelAndView deleteBoard(@ModelAttribute BoardVO dataVO, Model model) throws Exception {
		// 삭제 권한 체크
		BoardVO boardVO = boardService.selectBoard(dataVO);
		if(!boardVO.isDeletable()) {
			return makeFailJsonData(egovMessageSource.getMessage("info.noAuth.msg"));
		} else if(boardVO.getReplyCnt() > 0) {
			return makeFailJsonData(egovMessageSource.getMessage("system.system.board.cantDelete"));
		}
		dataVO.setUpSeq(boardVO.getUpSeq());
		// 일괄삭제 방지
		dataVO.setKeys(null);

		List<FileVO> deleteFileList = boardService.deleteData(dataVO);
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

// 댓글
	/**
	 * 댓글 목록 조회(json)
	 * @param	BoardVO searchVO
	 * @return	ModelAndView
	 * @throws	Exception
	 */
	@RequestMapping(value="/system/system/board/boardCommentList_json.do")
	public ModelAndView boardCommentList_json(@ModelAttribute BoardVO searchVO) throws Exception {
		BoardVO boardVO = boardService.selectBoard(searchVO);
		if(!boardVO.isReadable() || boardVO.getUseCommentYn().equals("N")) {
			throw new Exception(egovMessageSource.getMessage("info.noAuth.msg"));
		}

		List<BoardVO> dataList = boardService.selectCommentList(searchVO);
		return makeJsonListData(dataList);
	}

	/**
	 * 댓글 저장
	 * @param	BoardVO dataVO
	 * @param	Model model
	 * @return	ModelAndView
	 * @throws	Exception
	 */
	@RequestMapping(value="/system/system/board/insertComment.do")
	public ModelAndView insertComment(@ModelAttribute BoardVO dataVO, Model model) throws Exception {
		BoardVO boardVO = boardService.selectBoard(dataVO);
		if(!boardVO.isReadable() || boardVO.getUseCommentYn().equals("N")) {
			throw new Exception(egovMessageSource.getMessage("info.noAuth.msg"));
		}

		if(CommonUtil.isEmpty(dataVO.getContents())) {
			return makeFailJsonData(egovMessageSource.getMessage("common.required.msg", new String[] {egovMessageSource.getMessage("word.content")}));
		}

		return makeJsonDataByResultCnt(boardService.insertComment(dataVO));
	}

	/**
	 * 댓글 수정
	 * @param	BoardVO dataVO
	 * @param	Model model
	 * @return	ModelAndView
	 * @throws	Exception
	 */
	@RequestMapping(value="/system/system/board/updateComment.do")
	public ModelAndView updateComment(@ModelAttribute BoardVO dataVO, Model model) throws Exception {
		BoardVO boardVO = boardService.selectBoard(dataVO);
		if(!boardVO.isReadable() || boardVO.getUseCommentYn().equals("N")) {
			throw new Exception(egovMessageSource.getMessage("info.noAuth.msg"));
		}

		if(CommonUtil.isEmpty(dataVO.getContents())) {
			return makeFailJsonData(egovMessageSource.getMessage("common.required.msg", new String[] {egovMessageSource.getMessage("word.content")}));
		}

		return makeJsonDataByResultCnt(boardService.updateComment(dataVO));
	}

	/**
	 * 댓글 삭제
	 * @param	BoardVO dataVO
	 * @param	Model model
	 * @return	ModelAndView
	 * @throws	Exception
	 */
	@RequestMapping(value="/system/system/board/deleteComment.do")
	public ModelAndView deleteComment(@ModelAttribute BoardVO dataVO, Model model) throws Exception {
		BoardVO boardVO = boardService.selectBoard(dataVO);
		if(!boardVO.isReadable() || boardVO.getUseCommentYn().equals("N")) {
			throw new Exception(egovMessageSource.getMessage("info.noAuth.msg"));
		}

		return makeJsonDataByResultCnt(boardService.deleteComment(dataVO));
	}

	/*팝업화면*/

	/**
	 * 게시물 목록 화면
	 * @param	BoardVO searchVO
	 * @param	Model model
	 * @return	String
	 * @throws	Exception
	 */
	@RequestMapping(value="/comPop/system/system/board/boardList.do")
	public String popBoardList(@ModelAttribute("searchVO") BoardVO searchVO, Model model) throws Exception {
		model.addAttribute("boardSetting", boardService.selectBoardSetting(searchVO));
		return "/system/system/board/popBoardList." + searchVO.getLayout();
	}

	/**
	 * 게시물 목록 조회(json)
	 * @param	BoardVO searchVO
	 * @return	ModelAndView
	 * @throws	Exception
	 */
	@RequestMapping(value="/comPop/system/system/board/boardList_json.do")
	public ModelAndView popBoardList_json(@ModelAttribute BoardVO searchVO) throws Exception {
		return boardList_json(searchVO);
	}

	/**
	 * 게시물 상세 화면
	 * @param	BoardVO searchVO
	 * @param	Model model
	 * @return	String
	 * @throws	Exception
	 */
	@RequestMapping(value="/comPop/system/system/board/boardDetail.do")
	public String popBoardDetail(@ModelAttribute BoardVO searchVO, Model model) throws Exception {
		BoardVO boardSetting = boardService.selectBoardSetting(searchVO);
		BoardVO dataVO = boardService.selectBoard(searchVO, true);
		if(!dataVO.isReadable()) {
			throw new Exception(egovMessageSource.getMessage("info.noAuth.msg"));
		}

		model.addAttribute("boardSetting", boardSetting);
		model.addAttribute("searchVO", searchVO);
		model.addAttribute("dataVO", dataVO);
		return "/system/system/board/popBoardDetail." + searchVO.getLayout();
	}

	/**
	 * 게시물 등록 화면
	 * @param	BoardVO dataVO
	 * @param	Model model
	 * @return	String
	 * @throws	Exception
	 */
	@RequestMapping(value="/comPop/system/system/board/boardForm.do")
	public String popBoardForm(@ModelAttribute("dataVO") BoardVO dataVO, Model model) throws Exception {
		BoardVO boardSetting = boardService.selectBoardSetting(dataVO);
		model.addAttribute("boardSetting", boardSetting);
		model.addAttribute("searchVO", dataVO);
		return "/system/system/board/popBoardForm." + dataVO.getLayout();
	}

	/**
	 * 게시물 수정 화면
	 * @param	BoardVO searchVO
	 * @param	Model model
	 * @return	String
	 * @throws	Exception
	 */
	@RequestMapping(value="/comPop/system/system/board/boardUpdateForm.do")
	public String popBoardUpdateForm(@ModelAttribute("dataVO") BoardVO searchVO, Model model) throws Exception {
		BoardVO boardSetting = boardService.selectBoardSetting(searchVO);
		BoardVO dataVO = boardService.selectBoard(searchVO);
		if(!dataVO.isReadable()) {
			throw new Exception(egovMessageSource.getMessage("info.noAuth.msg"));
		}

		model.addAttribute("boardSetting", boardSetting);
		model.addAttribute("dataVO", dataVO);
		model.addAttribute("searchVO", searchVO);
		return "/system/system/board/popBoardForm." + searchVO.getLayout();
	}

	/**
	 * 게시물 답글 화면
	 * @param	BoardVO searchVO
	 * @param	Model model
	 * @return	String
	 * @throws	Exception
	 */
	@RequestMapping(value="/comPop/system/system/board/boardReplyForm.do")
	public String popBoardReplyForm(@ModelAttribute("dataVO") BoardVO searchVO, Model model) throws Exception {
		BoardVO boardSetting = boardService.selectBoardSetting(searchVO);
		BoardVO boardVO = boardService.selectBoard(searchVO);
		if(!boardVO.isReadable()) {
			throw new Exception(egovMessageSource.getMessage("info.noAuth.msg"));
		}

		BoardVO dataVO = new BoardVO();
		dataVO.setBoardId(boardVO.getBoardId());
		dataVO.setUpSeq(boardVO.getSeq());
		dataVO.setSeq(-1);
		dataVO.setTitle("Re: " + boardVO.getTitle());

		model.addAttribute("boardSetting", boardSetting);
		model.addAttribute("dataVO", dataVO);
		model.addAttribute("searchVO", searchVO);
		return "/system/system/board/popBoardForm." + searchVO.getLayout();
	}

	/**
	 * 게시물 저장
	 * @param MultipartHttpServletRequest multiRequest
	 * @param HttpServletResponse dataVO
	 * @param BoardVO dataVO
	 * @param Model model
	 * @param BindingResult bindingResult
	 * @throws Exception
	 */
	@RequestMapping(value="/comPop/system/system/board/saveBoard.do")
	public void popSaveBoard (
			final MultipartHttpServletRequest multiRequest,
			HttpServletResponse response,
			@ModelAttribute BoardVO dataVO,
			Model model, BindingResult bindingResult) throws Exception {

		saveBoard(multiRequest, response, dataVO, model, bindingResult);
	}

	/**
	 * 게시물 일괄 삭제
	 * @param	BoardVO dataVO
	 * @param	Model model
	 * @return	ModelAndView
	 * @throws	Exception
	 */
	@RequestMapping(value="/comPop/system/system/board/deleteBoardAll.do")
	public ModelAndView popDeleteBoardAll(@ModelAttribute BoardVO dataVO, Model model) throws Exception {
		return deleteBoardAll(dataVO, model);
	}

	/**
	 * 게시물 삭제
	 * @param	BoardVO dataVO
	 * @param	Model model
	 * @return	ModelAndView
	 * @throws	Exception
	 */
	@RequestMapping(value="/comPop/system/system/board/deleteBoard.do")
	public ModelAndView popDeleteBoard(@ModelAttribute BoardVO dataVO, Model model) throws Exception {

		return deleteBoard(dataVO, model);
	}

// 댓글
	/**
	 * 댓글 목록 조회(json)
	 * @param	BoardVO searchVO
	 * @return	ModelAndView
	 * @throws	Exception
	 */
	@RequestMapping(value="/comPop/system/system/board/boardCommentList_json.do")
	public ModelAndView popBoardCommentList_json(@ModelAttribute BoardVO searchVO) throws Exception {
		return boardCommentList_json(searchVO);
	}

	/**
	 * 댓글 저장
	 * @param	BoardVO dataVO
	 * @param	Model model
	 * @return	ModelAndView
	 * @throws	Exception
	 */
	@RequestMapping(value="/comPop/system/system/board/insertComment.do")
	public ModelAndView popInsertComment(@ModelAttribute BoardVO dataVO, Model model) throws Exception {
		return insertComment(dataVO, model);
	}

	/**
	 * 댓글 수정
	 * @param	BoardVO dataVO
	 * @param	Model model
	 * @return	ModelAndView
	 * @throws	Exception
	 */
	@RequestMapping(value="/comPop/system/system/board/updateComment.do")
	public ModelAndView popUpdateComment(@ModelAttribute BoardVO dataVO, Model model) throws Exception {
		return updateComment(dataVO, model);
	}

	/**
	 * 댓글 삭제
	 * @param	BoardVO dataVO
	 * @param	Model model
	 * @return	ModelAndView
	 * @throws	Exception
	 */
	@RequestMapping(value="/comPop/system/system/board/deleteComment.do")
	public ModelAndView popDeleteComment(@ModelAttribute BoardVO dataVO, Model model) throws Exception {
		return deleteComment(dataVO, model);
	}

}
