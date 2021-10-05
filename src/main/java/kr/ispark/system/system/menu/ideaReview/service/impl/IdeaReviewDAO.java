/*************************************************************************
* CLASS 명	: IdeaReviewDAO
* 작 업 자	: 하성준
* 작 업 일	: 2021-10-05
* 기	능	: IDEA+검토 DAO
* ---------------------------- 변 경 이 력 --------------------------------
* 번호	작 업 자		작	업	일			변 경 내 용				비고
* ----	---------	----------------	---------------------	-----------
*	1	하성준		2021-10-05
**************************************************************************/
package kr.ispark.system.system.menu.ideaReview.service.impl;


import java.util.List;

import org.springframework.stereotype.Repository;

import egovframework.com.cmm.service.impl.EgovComAbstractDAO;
import kr.ispark.system.system.menu.ideaReview.service.IdeaReviewVO;

@Repository
public class IdeaReviewDAO extends EgovComAbstractDAO {
	/**
	 * IDEA+검토 목록 조회
	 * @param	IdeaReviewVO searchVO
	 * @return	List<IdeaReviewVO>
	 * @throws	Exception
	 */
	public List<IdeaReviewVO> selectList(IdeaReviewVO searchVO) throws Exception {
		return selectList("system.menu.ideaReview.selectList", searchVO);
	}
	
	/**
	 * IDEA+검토 상세 조회
	 * @param	IdeaReviewVO searchVO
	 * @return	IdeaReviewVO
	 * @throws	Exception
	 */
	public IdeaReviewVO selectDetail(IdeaReviewVO searchVO) throws Exception {
		return (IdeaReviewVO)selectOne("system.menu.ideaReview.selectDetail", searchVO);
	}
	
	/**
	 * IDEA+검토 정렬순서저장
	 * @param	IdeaReviewVO searchVO
	 * @return	int
	 * @throws	Exception
	 */
	public int updateSortOrder(IdeaReviewVO searchVO) throws Exception {
		return update("system.menu.ideaReview.updateSortOrder", searchVO);
	}

	/**
	 * IDEA+검토 삭제
	 * @param	IdeaReviewVO searchVO
	 * @return	int
	 * @throws	Exception
	 */
	public int deleteIdeaReview(IdeaReviewVO searchVO) throws Exception {
		return update("system.menu.ideaReview.deleteIdeaReview", searchVO);
	}
	
	/**
	 * IDEA+검토 저장
	 * @param	IdeaReviewVO searchVO
	 * @return	int
	 * @throws	Exception
	 */
	public int insertData(IdeaReviewVO searchVO) throws Exception {
		return insert("system.menu.ideaReview.insertData", searchVO);
	}

	/**
	 * IDEA+검토 수정
	 * @param	IdeaReviewVO searchVO
	 * @return	int
	 * @throws	Exception
	 */
	public int updateData(IdeaReviewVO searchVO) throws Exception {
		return update("system.menu.ideaReview.updateData", searchVO);
	}
}

