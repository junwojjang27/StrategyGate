package kr.ispark.system.system.menu.ideaUs.service.impl;


import java.util.List;

import kr.ispark.system.system.menu.ideaSingle.service.IdeaSingleVO;
import org.springframework.stereotype.Repository;

import egovframework.com.cmm.service.impl.EgovComAbstractDAO;
import kr.ispark.system.system.menu.ideaUs.service.IdeaUsVO;

@Repository
public class IdeaUsDAO extends EgovComAbstractDAO {
	/**
	 * 혁신 IDEA+ 목록 조회
	 * @param	IdeaUsVO searchVO
	 * @return	List<IdeaUsVO>
	 * @throws	Exception
	 */
	public List<IdeaUsVO> selectList(IdeaUsVO searchVO) throws Exception {
		return selectList("system.menu.ideaUs.selectList", searchVO);
	}

	/**
	 * 간단 IDEA+ 엑셀용 조회
	 */
	public List<IdeaUsVO> selectExcelList(IdeaUsVO searchVO) throws Exception {
		System.out.println("searchVO : " + searchVO);
		return selectList("system.menu.ideaUs.selectExcelList", searchVO);
	}

	/**
	 * 혁신 IDEA+ 상세 조회
	 * @param	IdeaUsVO searchVO
	 * @return	IdeaUsVO
	 * @throws	Exception
	 */
	public IdeaUsVO selectDetail(IdeaUsVO searchVO) throws Exception {
		return (IdeaUsVO)selectOne("system.menu.ideaUs.selectDetail", searchVO);
	}

	/**
	 * 혁신 IDEA+ 정렬순서저장
	 * @param	IdeaUsVO searchVO
	 * @return	int
	 * @throws	Exception
	 */
	public int updateSortOrder(IdeaUsVO searchVO) throws Exception {
		return update("system.menu.ideaUs.updateSortOrder", searchVO);
	}

	/**
	 * 혁신 IDEA+ 삭제
	 * @param	IdeaUsVO searchVO
	 * @return	int
	 * @throws	Exception
	 */
	public int deleteIdeaUs(IdeaUsVO searchVO) throws Exception {
		return update("system.menu.ideaUs.deleteIdeaUs", searchVO);
	}

	/**
	 * 혁신 IDEA+ 저장
	 * @param	IdeaUsVO searchVO
	 * @return	int
	 * @throws	Exception
	 */
	public String insertData(IdeaUsVO searchVO) throws Exception {
		insert("system.menu.ideaUs.insertData", searchVO);
		System.out.println("저장 DAO");
		System.out.println("searchVO : " + searchVO);
		return searchVO.getIdeaCd();
	}

	/**
	 * 혁신 IDEA+ 수정
	 * @param	IdeaUsVO searchVO
	 * @return	int
	 * @throws	Exception
	 */
	public int updateData(IdeaUsVO searchVO) throws Exception {
		return update("system.menu.ideaUs.updateData", searchVO);
	}
}
