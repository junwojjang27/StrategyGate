/*************************************************************************
* CLASS 명	: IdeaUsDAO
* 작 업 자	: 하성준
* 작 업 일	: 2021-09-08
* 기	능	: 혁신제안 DAO
* ---------------------------- 변 경 이 력 --------------------------------
* 번호	작 업 자		작	업	일			변 경 내 용				비고
* ----	---------	----------------	---------------------	-----------
*	1	하성준		2021-09-08
**************************************************************************/
package kr.ispark.system.system.menu.ideaUs.service.impl;


import java.util.List;

import org.springframework.stereotype.Repository;

import egovframework.com.cmm.service.impl.EgovComAbstractDAO;
import kr.ispark.system.system.menu.ideaUs.service.IdeaUsVO;

@Repository
public class IdeaUsDAO extends EgovComAbstractDAO {
	/**
	 * 혁신제안 목록 조회
	 * @param	IdeaUsVO searchVO
	 * @return	List<IdeaUsVO>
	 * @throws	Exception
	 */
	public List<IdeaUsVO> selectList(IdeaUsVO searchVO) throws Exception {
		return selectList("system.menu.ideaUs.selectList", searchVO);
	}
	
	/**
	 * 혁신제안 상세 조회
	 * @param	IdeaUsVO searchVO
	 * @return	IdeaUsVO
	 * @throws	Exception
	 */
	public IdeaUsVO selectDetail(IdeaUsVO searchVO) throws Exception {
		return (IdeaUsVO)selectOne("system.menu.ideaUs.selectDetail", searchVO);
	}
	
	/**
	 * 혁신제안 정렬순서저장
	 * @param	IdeaUsVO searchVO
	 * @return	int
	 * @throws	Exception
	 */
	public int updateSortOrder(IdeaUsVO searchVO) throws Exception {
		return update("system.menu.ideaUs.updateSortOrder", searchVO);
	}

	/**
	 * 혁신제안 삭제
	 * @param	IdeaUsVO searchVO
	 * @return	int
	 * @throws	Exception
	 */
	public int deleteIdeaUs(IdeaUsVO searchVO) throws Exception {
		return update("system.menu.ideaUs.deleteIdeaUs", searchVO);
	}
	
	/**
	 * 혁신제안 저장
	 * @param	IdeaUsVO searchVO
	 * @return	int
	 * @throws	Exception
	 */
	public int insertData(IdeaUsVO searchVO) throws Exception {
		return update("system.menu.ideaUs.insertData", searchVO);
	}

	/**
	 * 혁신제안 수정
	 * @param	IdeaUsVO searchVO
	 * @return	int
	 * @throws	Exception
	 */
	public int updateData(IdeaUsVO searchVO) throws Exception {
		return update("system.menu.ideaUs.updateData", searchVO);
	}
}

