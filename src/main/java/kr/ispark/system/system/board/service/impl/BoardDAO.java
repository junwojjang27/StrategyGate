/*************************************************************************
* CLASS 명	: BoardDAO
* 작 업 자	: kimyh
* 작 업 일	: 2018. 04. 17.
* 기	능	: 게시판 DAO
* ---------------------------- 변 경 이 력 --------------------------------
* 번호	작 업 자		작	업	일			변 경 내 용				비고
* ----	---------	----------------	---------------------	-----------
*	1	kimyh		2018. 04. 17.		최 초 작 업
**************************************************************************/
package kr.ispark.system.system.board.service.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import egovframework.com.cmm.service.impl.EgovComAbstractDAO;
import kr.ispark.system.system.board.service.BoardVO;

@Repository
public class BoardDAO extends EgovComAbstractDAO {
	/**
	 * 게시판 목록 조회
	 * @param	BoardVO searchVO
	 * @return	List<BoardVO>
	 * @throws	Exception
	 */
	public List<BoardVO> selectList(BoardVO searchVO) throws Exception {
		return selectList("system.system.board.selectList", searchVO);
	}

	/**
	 * 게시물 목록 수 조회
	 * @param	BoardVO searchVO
	 * @return	int
	 * @throws	Exception
	 */
	public int selectListCount(BoardVO searchVO) throws Exception {
		return (Integer)selectOne("system.system.board.selectListCount", searchVO);
	}
	
	/**
	 * 게시판 설정 조회
	 * @param	BoardVO searchVO
	 * @return	BoardVO
	 * @throws	Exception
	 */
	public BoardVO selectBoardSetting(BoardVO searchVO) throws Exception {
		return selectOne("system.system.board.selectBoardSetting", searchVO);
	}
	
	/**
	 * 게시물 조회
	 * @param	BoardVO searchVO
	 * @return	BoardVO
	 * @throws	Exception
	 */
	public BoardVO selectBoard(BoardVO searchVO) throws Exception {
		return selectOne("system.system.board.selectBoard", searchVO);
	}
	
	/**
	 * 게시물 조회수 증가
	 * @param	BoardVO dataVO
	 * @return	int
	 * @throws	Exception
	 */
	public int updateHit(BoardVO dataVO) throws Exception {
		return update("system.system.board.updateHit", dataVO);
	}
	
	/**
	 * 신규 게시물 채번
	 * @param	BoardVO searchVO
	 * @return	int
	 * @throws	Exception
	 */
	public int selectNextSeq(BoardVO searchVO) throws Exception {
		return selectOne("system.system.board.selectNextSeq", searchVO);
	}
	
	/**
	 * 중복 게시 제한 시간내 게시물 수 체크
	 * @param	BoardVO searchVO
	 * @return	int
	 * @throws	Exception
	 */
	public int selectDuplCount(BoardVO searchVO) throws Exception {
		return selectOne("system.system.board.selectDuplCount", searchVO);
	}
	
	/**
	 * 신규 게시물 그룹번호 채번
	 * @param	BoardVO searchVO
	 * @return	int
	 * @throws	Exception
	 */
	public int selectNextGroupSeq(BoardVO searchVO) throws Exception {
		return selectOne("system.system.board.selectNextGroupSeq", searchVO);
	}
	
	/**
	 * 게시판 등록
	 * @param	BoardVO dataVO
	 * @return	int
	 * @throws	Exception
	 */
	public int insertData(BoardVO dataVO) throws Exception {
		return insert("system.system.board.insertData", dataVO);
	}

	/**
	 * 게시판 수정
	 * @param	BoardVO dataVO
	 * @return	int
	 * @throws	Exception
	 */
	public int updateData(BoardVO dataVO) throws Exception {
		return update("system.system.board.updateData", dataVO);
	}

	/**
	 * 게시판 삭제
	 * @param	BoardVO dataVO
	 * @return	int
	 * @throws	Exception
	 */
	public int deleteData(BoardVO dataVO) throws Exception {
		return delete("system.system.board.deleteData", dataVO);
	}
	
	/**
	 * 삭제할 게시물에 첨부된 파일ID 목록 조회
	 * @param	BoardVO searchVO
	 * @return	List<String>
	 * @throws	Exception
	 */
	public List<String> selectAtchFileIdListForDelete(BoardVO dataVO) throws Exception {
		return selectList("system.system.board.selectAtchFileIdListForDelete", dataVO);
	}
	
	/**
	 * 답글용 그룹 순서 최소값 조회
	 * @param	BoardVO searchVO
	 * @return	int
	 * @throws	Exception
	 */
	public Integer selectMinGroupOrder(BoardVO searchVO) throws Exception {
		return selectOne("system.system.board.selectMinGroupOrder", searchVO);
	}
	
	/**
	 * 답글용 그룹 순서 채번
	 * @param	BoardVO searchVO
	 * @return	int
	 * @throws	Exception
	 */
	public int selectNextGroupOrder(BoardVO searchVO) throws Exception {
		return selectOne("system.system.board.selectNextGroupOrder", searchVO);
	}
	
	/**
	 * 답글용 그룹 순서 조정
	 * @param	BoardVO dataVO
	 * @return	int
	 * @throws	Exception
	 */
	public int updateGroupOrder(BoardVO dataVO) throws Exception {
		return update("system.system.board.updateGroupOrder", dataVO);
	}

	/**
	 * 상위글 답글 수 수정
	 * @param	BoardVO dataVO
	 * @return	int
	 * @throws	Exception
	 */
	public int updateReplyCount(BoardVO dataVO) throws Exception {
		return update("system.system.board.updateReplyCount", dataVO);
	}
	
	/**
	 * 전체글의 답글 수 수정
	 * @param	BoardVO dataVO
	 * @return	int
	 * @throws	Exception
	 */
	public int updateReplyCountAll(BoardVO dataVO) throws Exception {
		return update("system.system.board.updateReplyCountAll", dataVO);
	}
	
// 댓글
	/**
	 * 댓글 목록 조회
	 * @param	BoardVO searchVO
	 * @return	List<BoardVO>
	 * @throws	Exception
	 */
	public List<BoardVO> selectCommentList(BoardVO searchVO) throws Exception {
		return selectList("system.system.board.selectCommentList", searchVO);
	}
	
	/**
	 * 신규 댓글 채번
	 * @param	BoardVO dataVO
	 * @return	int
	 * @throws	Exception
	 */
	public int selectNextCommentSeq(BoardVO dataVO) throws Exception {
		return selectOne("system.system.board.selectNextCommentSeq", dataVO);
	}
	
	/**
	 * 댓글 저장
	 * @param	BoardVO dataVO
	 * @return	int
	 * @throws	Exception
	 */
	public int insertComment(BoardVO dataVO) throws Exception {
		return insert("system.system.board.insertComment", dataVO);
	}
	
	/**
	 * 댓글수 갱신
	 * @param	BoardVO dataVO
	 * @return	int
	 * @throws	Exception
	 */
	public int updateCommentCount(BoardVO dataVO) throws Exception {
		return update("system.system.board.updateCommentCount", dataVO);
	}
	
	/**
	 * 댓글 수정
	 * @param	BoardVO dataVO
	 * @return	int
	 * @throws	Exception
	 */
	public int updateComment(BoardVO dataVO) throws Exception {
		return update("system.system.board.updateComment", dataVO);
	}
	
	/**
	 * 댓글 삭제
	 * @param	BoardVO dataVO
	 * @return	int
	 * @throws	Exception
	 */
	public int deleteComment(BoardVO dataVO) throws Exception {
		return update("system.system.board.deleteComment", dataVO);
	}
}
