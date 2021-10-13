/*************************************************************************
* CLASS 명	: IdeaEvalDAO
* 작 업 자	: 하성준
* 작 업 일	: 2021-10-13
* 기	능	: 평가하기 DAO
* ---------------------------- 변 경 이 력 --------------------------------
* 번호	작 업 자		작	업	일			변 경 내 용				비고
* ----	---------	----------------	---------------------	-----------
*	1	하성준		2021-10-13
**************************************************************************/
package kr.ispark.system.system.menu.ideaEval.service.impl;


import java.util.List;

import org.springframework.stereotype.Repository;

import egovframework.com.cmm.service.impl.EgovComAbstractDAO;
import kr.ispark.system.system.menu.ideaEval.service.IdeaEvalVO;

@Repository
public class IdeaEvalDAO extends EgovComAbstractDAO {
	/**
	 * 평가하기 목록 조회
	 * @param	IdeaEvalVO searchVO
	 * @return	List<IdeaEvalVO>
	 * @throws	Exception
	 */
	public List<IdeaEvalVO> selectList(IdeaEvalVO searchVO) throws Exception {
		return selectList("system.menu.ideaEval.selectList", searchVO);
	}
	
	/**
	 * 평가하기 상세 조회
	 * @param	IdeaEvalVO searchVO
	 * @return	IdeaEvalVO
	 * @throws	Exception
	 */
	public IdeaEvalVO selectDetail(IdeaEvalVO searchVO) throws Exception {
		return (IdeaEvalVO)selectOne("system.menu.ideaEval.selectDetail", searchVO);
	}
	
	/**
	 * 평가하기 정렬순서저장
	 * @param	IdeaEvalVO searchVO
	 * @return	int
	 * @throws	Exception
	 */
	public int updateSortOrder(IdeaEvalVO searchVO) throws Exception {
		return update("system.menu.ideaEval.updateSortOrder", searchVO);
	}

	/**
	 * 평가하기 삭제
	 * @param	IdeaEvalVO searchVO
	 * @return	int
	 * @throws	Exception
	 */
	public int deleteIdeaEval(IdeaEvalVO searchVO) throws Exception {
		return update("system.menu.ideaEval.deleteIdeaEval", searchVO);
	}
	
	/**
	 * 평가하기 저장
	 * @param	IdeaEvalVO searchVO
	 * @return	int
	 * @throws	Exception
	 */
	public int insertData(IdeaEvalVO searchVO) throws Exception {
		return insert("system.menu.ideaEval.insertData", searchVO);
	}

	/**
	 * 평가하기 수정
	 * @param	IdeaEvalVO searchVO
	 * @return	int
	 * @throws	Exception
	 */
	public int updateData(IdeaEvalVO searchVO) throws Exception {
		return update("system.menu.ideaEval.updateData", searchVO);
	}
}

