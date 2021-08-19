/*************************************************************************
* CLASS 명	: BoardServiceImpl
* 작 업 자	: kimyh
* 작 업 일	: 2018. 04. 17.
* 기	능	: 게시판 Service
* ---------------------------- 변 경 이 력 --------------------------------
* 번호	작 업 자		작	업	일			변 경 내 용				비고
* ----	---------	----------------	---------------------	-----------
*	1	kimyh		2018. 04. 17.		최 초 작 업
**************************************************************************/

package kr.ispark.system.system.board.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import egovframework.com.cmm.EgovMessageSource;
import egovframework.com.cmm.service.EgovFileMngService;
import egovframework.com.cmm.service.FileVO;
import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;
import kr.ispark.common.security.service.UserVO;
import kr.ispark.common.util.CommonUtil;
import kr.ispark.common.util.CustomEgovFileMngUtil;
import kr.ispark.common.util.SessionUtil;
import kr.ispark.system.system.board.service.BoardVO;

@Service
public class BoardServiceImpl extends EgovAbstractServiceImpl {
	public final Logger log = LoggerFactory.getLogger(this.getClass());

	@Resource(name="CustomEgovFileMngUtil")
	private CustomEgovFileMngUtil fileUtil;
	
	@Resource(name="EgovFileMngService")
	private EgovFileMngService fileMngService;

	@Resource(name="egovMessageSource")
	public EgovMessageSource egovMessageSource;
	
	@Resource
	private BoardDAO boardDAO;
	
	/**
	 * 게시판 목록 조회
	 * @param	BoardVO searchVO
	 * @return	List<BoardVO>
	 * @throws	Exception
	 */
	public List<BoardVO> selectList(BoardVO searchVO) throws Exception {
		searchVO.setUserId(SessionUtil.getUserId());
		return boardDAO.selectList(searchVO);
	}

	/**
	 * 게시판 목록수 조회
	 * @param	BoardVO searchVO
	 * @return	int
	 * @throws	Exception
	 */
	public int selectListCount(BoardVO searchVO) throws Exception {
		searchVO.setUserId(SessionUtil.getUserId());
		return boardDAO.selectListCount(searchVO);
	}
	
	/**
	 * 게시판 설정 조회
	 * @param	BoardVO searchVO
	 * @return	BoardVO
	 * @throws	Exception
	 */
	public BoardVO selectBoardSetting(BoardVO searchVO) throws Exception {
		BoardVO boardSetting = boardDAO.selectBoardSetting(searchVO);
		if(boardSetting == null) {
			throw new Exception(egovMessageSource.getMessage("system.system.board.notExist"));
		}
		
		// 글 작성 권한 소유 여부 세팅
		if(SessionUtil.hasRole("01")) {
			boardSetting.setWritable(true);
		} else {
			UserVO uvo = SessionUtil.getUserVO();
			List<String> adminGubunList;
			if(uvo != null){
				adminGubunList = uvo.getAdminGubunList();
			}else{
				adminGubunList = new ArrayList<String>(0);
			}
			for(String adminGubun : adminGubunList) {
				if(boardSetting.getWriteAuthList().indexOf(adminGubun) != -1) {
					boardSetting.setWritable(true);
					break;
				}
			}
		}
		
		// 답글 작성 권한 소유 여부 세팅
		if(SessionUtil.hasRole("01")) {
			boardSetting.setReplyWritable(true);
		} else {
			UserVO uvo = SessionUtil.getUserVO();
			List<String> adminGubunList;
			if(uvo != null){
				adminGubunList = uvo.getAdminGubunList();
			}else{
				adminGubunList = new ArrayList<String>(0);
			}
			for(String adminGubun : adminGubunList) {
				if(boardSetting.getReplyAuthList().indexOf(adminGubun) != -1) {
					boardSetting.setReplyWritable(true);
					break;
				}
			}
		}
		
		return boardSetting;
	}
	
	/**
	 * 게시물 조회
	 * @param	BoardVO searchVO
	 * @return	BoardVO
	 * @throws	Exception
	 */
	public BoardVO selectBoard(BoardVO searchVO) throws Exception {
		return selectBoard(searchVO, false);
	}
	public BoardVO selectBoard(BoardVO searchVO, boolean updateHit) throws Exception {
		searchVO.setUserId(SessionUtil.getUserId());
		BoardVO dataVO = boardDAO.selectBoard(searchVO);
		UserVO uvo = SessionUtil.getUserVO();
		if(dataVO != null && uvo != null){
			dataVO.setReadable(true);
			// 관리자 또는 작성자만 수정, 삭제 가능
			if(SessionUtil.hasRole("01")
					|| uvo.getUserId().equals(dataVO.getInsertUserId())) {
				dataVO.setEditable(true);
				dataVO.setDeletable(true);
			} else {	// 관리자도 아니고 본인 글도 아님
				// 1:1 게시판일 경우 본인글에 달린 답변이 아니면 읽을 수 없음
				if(dataVO.getPrivateYn().equals("Y") && !uvo.getUserId().equals(dataVO.getUpInsertUserId())) {
					dataVO.setReadable(false);
				}
			}
			
			// 조회수 +1
			if(dataVO.isReadable()
					&& updateHit
					&& !uvo.getUserId().equals(dataVO.getInsertUserId())) {
				boardDAO.updateHit(dataVO);
			}
			
		}else{
			dataVO = new BoardVO();
		}
		
		return dataVO;
	}
	
	/**
	 * 중복 게시 제한 시간내 게시물 수 체크
	 * @param	BoardVO searchVO
	 * @return	int
	 * @throws	Exception
	 */
	public int selectDuplCount(BoardVO searchVO) throws Exception {
		return boardDAO.selectDuplCount(searchVO);
	}
	
	/**
	 * 게시판 등록
	 * @param	BoardVO dataVO
	 * @return	int
	 * @throws	Exception
	 */
	public int insertData(BoardVO dataVO, List<FileVO> fileList) throws Exception {
    	int seq = boardDAO.selectNextSeq(dataVO);
    	UserVO uvo = SessionUtil.getUserVO();
		dataVO.setSeq(seq);
		dataVO.setUserId(uvo!=null?uvo.getUserId():null);
		dataVO.setInsertUserIp(uvo!=null?uvo.getIp():null);
		
		// 신규
		if(dataVO.getUpSeq() == -1) {
			dataVO.setGroupSeq(boardDAO.selectNextGroupSeq(dataVO));
			dataVO.setGroupOrder(0);
			dataVO.setGroupLevel(0);
		} else { // 답글
			// 상위글에 접근이 가능한지 확인
			dataVO.setSeq(dataVO.getUpSeq());
			BoardVO boardVO = selectBoard(dataVO);
			if(!boardVO.isReadable()) {
				throw new Exception(egovMessageSource.getMessage("info.noAuth.msg"));
			}
			
			dataVO.setSeq(seq);
			dataVO.setGroupSeq(boardVO.getGroupSeq());
			dataVO.setGroupOrder(boardVO.getGroupOrder());
			dataVO.setGroupLevel(boardVO.getGroupLevel());
			
			int nextGroupOrder = boardDAO.selectMinGroupOrder(dataVO);
			if(nextGroupOrder == -1) {	// 맨 밑에 위치
				nextGroupOrder = boardDAO.selectNextGroupOrder(dataVO);
				dataVO.setGroupOrder(nextGroupOrder);
			} else {	// 중간에 위치
				// 답글 이후 글들의 순서를 1씩 증가
				dataVO.setGroupOrder(nextGroupOrder);
				boardDAO.updateGroupOrder(dataVO);
			}
			dataVO.setGroupLevel(dataVO.getGroupLevel() + 1);
		}
		
		if(!CommonUtil.isEmpty(fileList)) {
    		fileMngService.insertFileInfs(fileList);
    	}
    	dataVO.setAtchFileId(fileUtil.getAtchFileId(fileList));
		
    	int resultCnt = boardDAO.insertData(dataVO);
    	
    	// 답글일 경우 상위글의 답글 수 수정
    	if(dataVO.getUpSeq() != -1) {
    		updateReplyCount(dataVO);
    	}
		return resultCnt;
	}

	/**
	 * 게시물 수정
	 * @param	BoardVO dataVO
	 * @return	int
	 * @throws	Exception
	 * - 처리순서
	 * 	1) 업로드한 파일 정보를 DB에 저장
	 * 	2) 게시물 정보 update
	 *  3) 삭제 체크한 파일이 있으면 DB에서 delete하고 물리적으로 삭제할 파일 목록 생성
	 *  4) 삭제할 파일 목록을 리턴 (controller에서 삭제 처리)
	 */
	public List<FileVO> updateData(BoardVO dataVO, List<FileVO> fileList) throws Exception {
		// 업로드한 파일이 있으면
		if(!CommonUtil.isEmpty(fileList)) {
			// 새로 업로드 한 경우에는 atchFileId 새로 채번
			if(CommonUtil.isEmpty(dataVO.getAtchFileId())) {
				dataVO.setAtchFileId(fileMngService.insertFileInfs(fileList));
			} else {
				// 추가로 업로드 한 경우
				fileMngService.updateFileInfs(fileList);
			}
    	}
		
		// 게시물 정보 update
		boardDAO.updateData(dataVO);
		
		List<FileVO> deleteFileList = new ArrayList<FileVO>();
		// 삭제 체크한 파일들 DB에서 삭제
		FileVO fvo = new FileVO();
		if(!CommonUtil.isEmpty(dataVO.getChkAttachFiles())) {
			fvo.setAtchFileId(dataVO.getAtchFileId());
			fvo.setChkAttachFiles(dataVO.getChkAttachFiles());
			deleteFileList.addAll(fileMngService.deleteFileInfsAndDisk(fvo));
		}
		
		// 삭제할 파일 목록을 controller에서 삭제 처리 (DB transaction 이후)
		return deleteFileList;
	}
	
	/**
	 * 게시물 삭제
	 * @param	BoardVO dataVO
	 * @return	int
	 * @throws	Exception
	 */
	public List<FileVO> deleteData(BoardVO dataVO) throws Exception {
		// 삭제할 파일ID 정보
		List<String> fileIdList = boardDAO.selectAtchFileIdListForDelete(dataVO);
		List<FileVO> fileList = new ArrayList<FileVO>();
		
		// 게시물 삭제
		boardDAO.deleteData(dataVO);
		
		// 답글일 경우 상위글의 답글 수 수정
    	if(dataVO.getUpSeq() != -1) {
    		updateReplyCount(dataVO);
    	}
    	// 일괄 삭제인 경우 전체글의 답글 수 수정
    	if(dataVO.getKeys() != null) {
    		boardDAO.updateReplyCountAll(dataVO);
    	}
		
		// DB에서 파일 정보 삭제 & 물리적으로 삭제할 파일 목록 병합
		if(!CommonUtil.isEmpty(fileIdList)) {
			for(String atchFileId : fileIdList) {
				fileList.addAll(fileMngService.deleteFileInfsAndDiskByAtchFileId(atchFileId));
			}
		}
		
		return fileList;
	}
	
	/**
	 * 상위글 답글 수 수정
	 * @param	BoardVO dataVO
	 * @return	int
	 * @throws	Exception
	 */
	public int updateReplyCount(BoardVO dataVO) throws Exception {
		return boardDAO.updateReplyCount(dataVO);
	}
	
// 댓글
	/**
	 * 댓글 목록 조회
	 * @param	BoardVO searchVO
	 * @return	List<BoardVO>
	 * @throws	Exception
	 */
	public List<BoardVO> selectCommentList(BoardVO searchVO) throws Exception {
		return boardDAO.selectCommentList(searchVO);
	}
	
	/**
	 * 댓글 저장
	 * @param	BoardVO dataVO
	 * @return	int
	 * @throws	Exception
	 */
	public int insertComment(BoardVO dataVO) throws Exception {
		
		UserVO uvo = SessionUtil.getUserVO();
		
		dataVO.setUserId(uvo!=null?uvo.getUserId():null);
		dataVO.setInsertUserIp(uvo!=null?uvo.getIp():null);
		dataVO.setCommentSeq(boardDAO.selectNextCommentSeq(dataVO));
		
		int resultCnt = boardDAO.insertComment(dataVO);
		boardDAO.updateCommentCount(dataVO);
		return resultCnt;
	}
	
	/**
	 * 댓글 수정
	 * @param	BoardVO dataVO
	 * @return	int
	 * @throws	Exception
	 */
	public int updateComment(BoardVO dataVO) throws Exception {
		if(!SessionUtil.hasRole("01")) {
			dataVO.setUserId(SessionUtil.getUserId());
		}
		return boardDAO.updateComment(dataVO);
	}
	
	/**
	 * 댓글 삭제
	 * @param	BoardVO dataVO
	 * @return	int
	 * @throws	Exception
	 */
	public int deleteComment(BoardVO dataVO) throws Exception {
		if(!SessionUtil.hasRole("01")) {
			dataVO.setUserId(SessionUtil.getUserId());
		}
		
		int resultCnt = boardDAO.deleteComment(dataVO);
		boardDAO.updateCommentCount(dataVO);
		return resultCnt;
	}
}
