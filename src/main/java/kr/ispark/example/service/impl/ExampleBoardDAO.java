/*************************************************************************
* CLASS 명	: ExampleBoardDAO
* 작 업 자	: kimyh
* 작 업 일	: 2017. 12. 26.
* 기	능	: 게시판 예제 DAO
* ---------------------------- 변 경 이 력 --------------------------------
* 번호	작 업 자		작	업	일			변 경 내 용				비고
* ----	---------	----------------	---------------------	-----------
*	1	kimyh		2017. 12. 26.		최 초 작 업
**************************************************************************/
package kr.ispark.example.service.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import egovframework.com.cmm.service.impl.EgovComAbstractDAO;
import kr.ispark.example.service.ExampleBoardVO;

@Repository
public class ExampleBoardDAO extends EgovComAbstractDAO {
	/**
	 * 게시판 목록 조회
	 */
	public List<ExampleBoardVO> selectList(ExampleBoardVO searchVO) throws Exception {
		return selectList("exampleBoard.selectList", searchVO);
	}

	/**
	 * 게시판 목록수 조회
	 */
	public int selectListCount(ExampleBoardVO searchVO) throws Exception {
		return (Integer)selectOne("exampleBoard.selectListCount", searchVO);
	}
	
	/**
	 * 게시물 조회
	 */
	public ExampleBoardVO selectBoard(ExampleBoardVO searchVO) throws Exception {
		return selectOne("exampleBoard.selectBoard", searchVO);
	}
	
	/**
	 * 게시판 등록
	 */
	public String insertData(ExampleBoardVO dataVO) throws Exception {
		insert("exampleBoard.insertData", dataVO);
		return dataVO.getId();
	}

	/**
	 * 게시판 수정
	 */
	public int updateData(ExampleBoardVO dataVO) throws Exception {
		return update("exampleBoard.updateData", dataVO);
	}

	/**
	 * 게시판 삭제
	 */
	public int deleteData(ExampleBoardVO dataVO) throws Exception {
		return delete("exampleBoard.deleteData", dataVO);
	}
	
	/**
	 * 삭제할 게시물에 첨부된 파일ID 목록 조회
	 */
	public List<String> selectAtchFileIdListForDelete(ExampleBoardVO dataVO) throws Exception {
		return selectList("exampleBoard.selectAtchFileIdListForDelete", dataVO);
	}
}
