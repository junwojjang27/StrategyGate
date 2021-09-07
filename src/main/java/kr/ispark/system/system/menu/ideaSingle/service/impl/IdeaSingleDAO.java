/*************************************************************************
* CLASS 명	: IdeaSingleDAO
* 작 업 자	: 하성준
* 작 업 일	: 2021-09-07
* 기	능	: 간단제안 DAO
* ---------------------------- 변 경 이 력 --------------------------------
* 번호	작 업 자		작	업	일			변 경 내 용				비고
* ----	---------	----------------	---------------------	-----------
*	1	하성준		2021-09-07
**************************************************************************/
package kr.ispark.system.system.menu.ideaSingle.service.impl;


import java.util.List;

import org.springframework.stereotype.Repository;

import egovframework.com.cmm.service.impl.EgovComAbstractDAO;
import kr.ispark.system.system.menu.ideaSingle.service.IdeaSingleVO;

@Repository
public class IdeaSingleDAO extends EgovComAbstractDAO {
	/**
	 * 간단제안 목록 조회
	 * @param	IdeaSingleVO searchVO
	 * @return	List<IdeaSingleVO>
	 * @throws	Exception
	 */
	public List<IdeaSingleVO> selectList(IdeaSingleVO searchVO) throws Exception {
		return selectList("system.menu.ideaSingle.selectList", searchVO);
	}
	
	/**
	 * 간단제안 상세 조회
	 * @param	IdeaSingleVO searchVO
	 * @return	IdeaSingleVO
	 * @throws	Exception
	 */
	public IdeaSingleVO selectDetail(IdeaSingleVO searchVO) throws Exception {
		return (IdeaSingleVO)selectOne("system.menu.ideaSingle.selectDetail", searchVO);
	}
	
	/**
	 * 간단제안 정렬순서저장
	 * @param	IdeaSingleVO searchVO
	 * @return	int
	 * @throws	Exception
	 */
	public int updateSortOrder(IdeaSingleVO searchVO) throws Exception {
		return update("system.menu.ideaSingle.updateSortOrder", searchVO);
	}

	/**
	 * 간단제안 삭제
	 * @param	IdeaSingleVO searchVO
	 * @return	int
	 * @throws	Exception
	 */
	public int deleteIdeaSingle(IdeaSingleVO searchVO) throws Exception {
		return update("system.menu.ideaSingle.deleteIdeaSingle", searchVO);
	}
	
	/**
	 * 간단제안 저장
	 * @param	IdeaSingleVO searchVO
	 * @return	int
	 * @throws	Exception
	 */
	public int insertData(IdeaSingleVO searchVO) throws Exception {
		return insert("system.menu.ideaSingle.insertData", searchVO);
	}

	/**
	 * 간단제안 수정
	 * @param	IdeaSingleVO searchVO
	 * @return	int
	 * @throws	Exception
	 */
	public int updateData(IdeaSingleVO searchVO) throws Exception {
		return update("system.menu.ideaSingle.updateData", searchVO);
	}
}

