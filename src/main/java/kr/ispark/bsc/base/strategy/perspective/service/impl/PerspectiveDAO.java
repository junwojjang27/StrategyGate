/*************************************************************************
* CLASS 명	: PerspectiveDAO
* 작 업 자	: joey
* 작 업 일	: 2018. 1. 29.
* 기	능	:  관점 DAO
* ---------------------------- 변 경 이 력 --------------------------------
* 번호	작 업 자		작	업	일			변 경 내 용				비고
* ----	---------	----------------	---------------------	-----------
*	1	joey		2018. 1. 29.		최 초 작 업
**************************************************************************/
package kr.ispark.bsc.base.strategy.perspective.service.impl;


import java.util.List;

import org.springframework.stereotype.Repository;

import egovframework.com.cmm.service.impl.EgovComAbstractDAO;
import kr.ispark.bsc.base.strategy.perspective.service.PerspectiveVO;

@Repository
public class PerspectiveDAO extends EgovComAbstractDAO {
	/**
	 * 관점 조회
	 */
	public List<PerspectiveVO> selectList(PerspectiveVO searchVO) throws Exception {
		return selectList("base.strategy.perspective.selectList", searchVO);
	}
	
	/**
	 * 관점 상세 조회
	 */
	public PerspectiveVO selectDetail(PerspectiveVO searchVO) throws Exception {
		return (PerspectiveVO)selectOne("base.strategy.perspective.selectDetail", searchVO);
	}
	
	/**
	 * 정렬순서저장
	 */
	public int updateSortOrder(PerspectiveVO searchVO) {
		return update("base.strategy.perspective.updateSortOrder", searchVO);
	}

	/**
	 * 관점삭제
	 */
	public int deletePerspective(PerspectiveVO searchVO) {
		return update("base.strategy.perspective.deletePerspective", searchVO);
	}
	
	/**
	 * 관점 저장
	 */
	public int insertData(PerspectiveVO searchVO) {
		return update("base.strategy.perspective.insertData", searchVO);
	}

	/**
	 * 관점 수정
	 */
	public int updateData(PerspectiveVO searchVO) {
		return update("base.strategy.perspective.updateData", searchVO);
	}
}
