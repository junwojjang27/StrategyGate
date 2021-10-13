/*************************************************************************
* CLASS 명	: IdeaEvalItemDAO
* 작 업 자	: 하성준
* 작 업 일	: 2021-09-27
* 기	능	: 평가항목관리 DAO
* ---------------------------- 변 경 이 력 --------------------------------
* 번호	작 업 자		작	업	일			변 경 내 용				비고
* ----	---------	----------------	---------------------	-----------
*	1	하성준		2021-09-27
**************************************************************************/
package kr.ispark.system.system.menu.ideaEvalItem.service.impl;


import java.util.List;

import kr.ispark.system.system.menu.ideaSingle.service.IdeaSingleVO;
import org.springframework.stereotype.Repository;

import egovframework.com.cmm.service.impl.EgovComAbstractDAO;
import kr.ispark.system.system.menu.ideaEvalItem.service.IdeaEvalItemVO;

@Repository
public class IdeaEvalItemDAO extends EgovComAbstractDAO {
	/**
	 * 평가항목관리 목록 조회
	 * @param	IdeaEvalItemVO searchVO
	 * @return	List<IdeaEvalItemVO>
	 * @throws	Exception
	 */
	public List<IdeaEvalItemVO> selectList(IdeaEvalItemVO searchVO) throws Exception {
		return selectList("system.menu.ideaEvalItem.selectList", searchVO);
	}

	/**
	 * 간단 IDEA+ 엑셀용 조회
	 */
	public List<IdeaEvalItemVO> selectExcelList(IdeaEvalItemVO searchVO) throws Exception {
		System.out.println("searchVO : " + searchVO);
		return selectList("system.menu.ideaEvalItem.selectExcelList", searchVO);
	}

	/**
	 * 평가항목관리 상세 조회
	 * @param	IdeaEvalItemVO searchVO
	 * @return	IdeaEvalItemVO
	 * @throws	Exception
	 */
	public IdeaEvalItemVO selectDetail(IdeaEvalItemVO searchVO) throws Exception {
		return (IdeaEvalItemVO)selectOne("system.menu.ideaEvalItem.selectDetail", searchVO);
	}

	/**
	 * 평가항목관리 정렬순서저장
	 * @param	IdeaEvalItemVO searchVO
	 * @return	int
	 * @throws	Exception
	 */
	public int updateSortOrder(IdeaEvalItemVO searchVO) throws Exception {
		return update("system.menu.ideaEvalItem.updateSortOrder", searchVO);
	}



	/**
	 * 평가항목관리 삭제
	 * @param	IdeaEvalItemVO searchVO
	 * @return	int
	 * @throws	Exception
	 */
	public int deleteIdeaEvalItem(IdeaEvalItemVO searchVO) throws Exception {
		return update("system.menu.ideaEvalItem.deleteIdeaEvalItem", searchVO);
	}
	
	/**
	 * 평가항목관리 저장
	 * @param	IdeaEvalItemVO searchVO
	 * @return	int
	 * @throws	Exception
	 */
	public int insertData(IdeaEvalItemVO searchVO) throws Exception {
		return update("system.menu.ideaEvalItem.insertData", searchVO);
	}

	/**
	 * 평가항목관리 수정
	 * @param	IdeaEvalItemVO searchVO
	 * @return	int
	 * @throws	Exception
	 */
	public int updateData(IdeaEvalItemVO searchVO) throws Exception {
		return update("system.menu.ideaEvalItem.updateData", searchVO);
	}
}

