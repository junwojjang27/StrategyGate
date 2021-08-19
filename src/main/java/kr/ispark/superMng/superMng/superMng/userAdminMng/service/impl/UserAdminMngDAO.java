/*************************************************************************
* CLASS 명	: UserAdminMngDAO
* 작 업 자	: 박정현
* 작 업 일	: 2018-07-04
* 기	능	: 사용자관리 DAO
* ---------------------------- 변 경 이 력 --------------------------------
* 번호	작 업 자		작	업	일			변 경 내 용				비고
* ----	---------	----------------	---------------------	-----------
*	1	박정현		2018-07-04
**************************************************************************/
package kr.ispark.superMng.superMng.superMng.userAdminMng.service.impl;


import java.util.List;

import org.springframework.stereotype.Repository;

import egovframework.com.cmm.service.impl.EgovComAbstractDAO;
import kr.ispark.common.system.service.DeptVO;
import kr.ispark.superMng.superMng.superMng.userAdminMng.service.UserAdminMngVO;

@Repository
public class UserAdminMngDAO extends EgovComAbstractDAO {
	/**
	 * 사용자관리 목록 조회
	 * @param	UserAdminMngVO searchVO
	 * @return	List<UserAdminMngVO>
	 * @throws	Exception
	 */
	public List<UserAdminMngVO> selectList(UserAdminMngVO searchVO) throws Exception {
		return selectList("superMng.superMng.userAdminMng.selectList", searchVO);
	}
	
	/**
	 * 회사 목록 조회
	 * @param	UserAdminMngVO searchVO
	 * @return	List<UserAdminMngVO>
	 * @throws	Exception
	 */
	public List<UserAdminMngVO> selectCompList(UserAdminMngVO searchVO) throws Exception {
		return selectList("superMng.superMng.userAdminMng.selectCompList", searchVO);
	}
	
	/**
	 * 조직 목록 조회
	 * @param	UserAdminMngVO searchVO
	 * @return	List<DeptVO>
	 * @throws	Exception
	 */
	public List<DeptVO> selectDeptList(UserAdminMngVO searchVO) throws Exception {
		return selectList("superMng.superMng.userAdminMng.selectDeptList", searchVO);
	}
	
	/**
	 * 권한 목록 조회
	 * @param	UserAdminMngVO searchVO
	 * @return	List<UserAdminMngVO>
	 * @throws	Exception
	 */
	public List<UserAdminMngVO> selectAuthList(UserAdminMngVO searchVO) throws Exception {
		return selectList("superMng.superMng.userAdminMng.selectAuthList", searchVO);
	}
	
}
