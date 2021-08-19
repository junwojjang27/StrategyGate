/*************************************************************************
* CLASS 명	: UserMngDAO
* 작 업 자	: 현걸욱
* 작 업 일	: 2018-1-21
* 기	능	: 사용자관리 DAO
* ---------------------------- 변 경 이 력 --------------------------------
* 번호	작 업 자		작	업	일			변 경 내 용				비고
* ----	---------	----------------	---------------------	-----------
*	1	현걸욱		2018-1-21
**************************************************************************/
package kr.ispark.system.system.menu.userMng.service.impl;


import java.util.List;

import org.springframework.stereotype.Repository;

import egovframework.com.cmm.service.impl.EgovComAbstractDAO;
import kr.ispark.system.system.menu.userMng.service.UserMngVO;

@Repository
public class UserMngDAO extends EgovComAbstractDAO {
	/**
	 * 사용자관리 조회
	 */
	public List<UserMngVO> selectList(UserMngVO searchVO) throws Exception {
		return selectList("system.menu.userMng.selectList", searchVO);
	}
}

