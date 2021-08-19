/*************************************************************************
* CLASS 명	: ExampleDAO
* 작 업 자	: kimyh
* 작 업 일	: 2017. 12. 15.
* 기	능	: 
* ---------------------------- 변 경 이 력 --------------------------------
* 번호	작 업 자		작	업	일			변 경 내 용				비고
* ----	---------	----------------	---------------------	-----------
*	1	kimyh		2017. 12. 15.		최 초 작 업
**************************************************************************/
package kr.ispark.example.service.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import egovframework.com.cmm.service.impl.EgovComAbstractDAO;
import kr.ispark.example.service.ExampleVO;

@Repository
public class ExampleDAO extends EgovComAbstractDAO {
	/**
	 * 관점설정 목록 조회
	 */
	public List<ExampleVO> selectList(ExampleVO searchVO) throws Exception {
		return selectList("example.selectList", searchVO);
	}

	/**
	 * 관점설정 목록 페이징 조회
	 */
	public List<ExampleVO> selectListPaging(ExampleVO searchVO) throws Exception {
		return selectList("example.selectListPaging", searchVO);
	}
	
	/**
	 * 관점설정 목록수 조회
	 */
	public int selectListCount(ExampleVO searchVO) throws Exception {
		return (Integer)selectOne("example.selectListCount", searchVO);
	}
	
	/**
	 * 관점설정 등록
	 */
	public int insertData(ExampleVO dataVO) throws Exception {
		return insert("example.insertData", dataVO);
	}

	/**
	 * 관점설정 수정
	 */
	public int updateData(ExampleVO dataVO) throws Exception {
		return update("example.updateData", dataVO);
	}

	/**
	 * 관점설정 삭제
	 */
	public int deleteData(ExampleVO dataVO) throws Exception {
		return delete("example.deleteData", dataVO);
	}
	
	/**
	 *	사용자 목록 조회
	 */
	public List<ExampleVO> selectUserList(ExampleVO searchVO) throws Exception {
		return selectList("example.selectUserList", searchVO);
	}
	
	/**
	 *	부서 목록 조회
	 */
	public List<ExampleVO> selectScDeptList(ExampleVO searchVO) throws Exception {
		return selectList("example.selectScDeptList", searchVO);
	}
}
