/*************************************************************************
* CLASS 명	: MenuDAO
* 작 업 자	: kimyh
* 작 업 일	: 2017. 11. 23.
* 기	능	: 메뉴 DAO
* ---------------------------- 변 경 이 력 --------------------------------
* 번호	작 업 자		작	업	일			변 경 내 용				비고
* ----	---------	----------------	---------------------	-----------
*	1	kimyh		2017. 11. 23.		최 초 작 업
**************************************************************************/

package kr.ispark.common.system.service.impl;


import java.sql.SQLException;
import java.util.List;

import org.springframework.stereotype.Repository;

import egovframework.com.cmm.service.impl.EgovComAbstractDAO;
import kr.ispark.common.security.service.UserVO;
import kr.ispark.common.system.service.MenuVO;

@Repository
public class MenuDAO extends EgovComAbstractDAO {
	/**
	 * 메뉴 목록 조회
	 * @param	UserVO searchVO
	 * @return	ArrayList<MenuVO>
	 * @throws	SQLException
	 */
	public List<MenuVO> selectList(UserVO searchVO) throws SQLException {
		return selectList("menu.selectList", searchVO);
	}

	/**
	 * 메뉴 접근 로그 등록
	 * @param	MenuVO dataVO
	 * @return	int
	 * @throws Exception
	 */
	public int insertMenuAccessLog(MenuVO dataVO) throws Exception {
		return insert("menu.insertMenuAccessLog", dataVO);
	}

	/**
	 * 도움말 조회
	 * @param	MenuVO searchVO
	 * @return	List<MenuVO>
	 * @throws	SQLException
	 */
	public List<MenuVO> selectGuideCommentList(MenuVO searchVO) throws SQLException {
		return selectList("menu.selectGuideCommentList", searchVO);
	}

}
