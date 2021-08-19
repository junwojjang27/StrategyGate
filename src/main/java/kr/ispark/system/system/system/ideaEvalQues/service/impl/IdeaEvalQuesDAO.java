/*************************************************************************
* CLASS 명	: IdeaEvalQuesDAO
* 작 업 자	: 문은경
* 작 업 일	: 2019-05-21
* 기	능	: 평가 질문 DAO
* ---------------------------- 변 경 이 력 --------------------------------
* 번호	작 업 자		작	업	일			변 경 내 용				비고
* ----	---------	----------------	---------------------	-----------
*	1	문은경		2019-05-21
**************************************************************************/
package kr.ispark.system.system.system.ideaEvalQues.service.impl;


import java.util.List;

import org.springframework.stereotype.Repository;

import egovframework.com.cmm.service.impl.EgovComAbstractDAO;
import kr.ispark.system.system.system.ideaEvalQues.service.IdeaEvalQuesVO;

@Repository
public class IdeaEvalQuesDAO extends EgovComAbstractDAO {
	/**
	 * 평가 질문 목록 조회
	 * @param	IdeaEvalQuesVO searchVO
	 * @return	List<IdeaEvalQuesVO>
	 * @throws	Exception
	 */
	public List<IdeaEvalQuesVO> selectList(IdeaEvalQuesVO searchVO) throws Exception {
		return selectList("system.system.ideaEvalQues.selectList", searchVO);
	}
	
	/**
	 * 평가 질문 상세 조회
	 * @param	IdeaEvalQuesVO searchVO
	 * @return	IdeaEvalQuesVO
	 * @throws	Exception
	 */
	public IdeaEvalQuesVO selectDetail(IdeaEvalQuesVO searchVO) throws Exception {
		return (IdeaEvalQuesVO)selectOne("system.system.ideaEvalQues.selectDetail", searchVO);
	}
	
	/**
	 * 평가 질문 정렬순서저장
	 * @param	IdeaEvalQuesVO searchVO
	 * @return	int
	 * @throws	Exception
	 */
	public int updateSortOrder(IdeaEvalQuesVO searchVO) throws Exception {
		return update("system.system.ideaEvalQues.updateSortOrder", searchVO);
	}

	/**
	 * 평가 질문 삭제
	 * @param	IdeaEvalQuesVO searchVO
	 * @return	int
	 * @throws	Exception
	 */
	public int deleteIdeaEvalQues(IdeaEvalQuesVO searchVO) throws Exception {
		return update("system.system.ideaEvalQues.deleteIdeaEvalQues", searchVO);
	}
	
	/**
	 * 평가 질문 저장
	 * @param	IdeaEvalQuesVO searchVO
	 * @return	int
	 * @throws	Exception
	 */
	public int insertData(IdeaEvalQuesVO searchVO) throws Exception {
		return insert("system.system.ideaEvalQues.insertData", searchVO);
	}

	/**
	 * 평가 질문 수정
	 * @param	IdeaEvalQuesVO searchVO
	 * @return	int
	 * @throws	Exception
	 */
	public int updateData(IdeaEvalQuesVO searchVO) throws Exception {
		return update("system.system.ideaEvalQues.updateData", searchVO);
	}
}

