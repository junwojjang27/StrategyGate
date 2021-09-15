package kr.ispark.system.system.menu.ideaSingle.service.impl;


import java.util.List;

import org.springframework.stereotype.Repository;

import egovframework.com.cmm.service.impl.EgovComAbstractDAO;
import kr.ispark.system.system.menu.ideaSingle.service.IdeaSingleVO;

@Repository
public class IdeaSingleDAO extends EgovComAbstractDAO {
	/**
	 * 간단 IDEA+ 목록 조회
	 * @param	IdeaSingleVO searchVO
	 * @return	List<IdeaSingleVO>
	 * @throws	Exception
	 */
	public List<IdeaSingleVO> selectList(IdeaSingleVO searchVO) throws Exception {
		return selectList("system.menu.ideaSingle.selectList", searchVO);
	}

	/**
	 * 간단 IDEA+ 상세 조회
	 * @param	IdeaSingleVO searchVO
	 * @return	IdeaSingleVO
	 * @throws	Exception
	 */
	public IdeaSingleVO selectDetail(IdeaSingleVO searchVO) throws Exception {
		return (IdeaSingleVO)selectOne("system.menu.ideaSingle.selectDetail", searchVO);
	}

	/**
	 * 간단 IDEA+ 정렬순서저장
	 * @param	IdeaSingleVO searchVO
	 * @return	int
	 * @throws	Exception
	 */
	public int updateSortOrder(IdeaSingleVO searchVO) throws Exception {
		return update("system.menu.ideaSingle.updateSortOrder", searchVO);
	}

	/**
	 * 간단 IDEA+ 삭제
	 * @param	IdeaSingleVO searchVO
	 * @return	int
	 * @throws	Exception
	 */
	public int deleteIdeaSingle(IdeaSingleVO searchVO) throws Exception {
		return update("system.menu.ideaSingle.deleteIdeaSingle", searchVO);
	}

	/**
	 * 간단 IDEA+ 저장
	 * @param	IdeaSingleVO searchVO
	 * @return	int
	 * @throws	Exception
	 */
	public int insertData(IdeaSingleVO searchVO) throws Exception {
		return update("system.menu.ideaSingle.insertData", searchVO);
	}

	/**
	 * 간단 IDEA+ 수정
	 * @param	IdeaSingleVO searchVO
	 * @return	int
	 * @throws	Exception
	 */
	public int updateData(IdeaSingleVO searchVO) throws Exception {
		return update("system.menu.ideaSingle.updateData", searchVO);
	}
}