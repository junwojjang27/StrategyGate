/*************************************************************************
* CLASS 명	: UserAdminMngServiceIpml
* 작 업 자	: 박정현
* 작 업 일	: 2018-07-04
* 기	능	: 사용자관리 ServiceIpml
* ---------------------------- 변 경 이 력 --------------------------------
* 번호	작 업 자		작	업	일			변 경 내 용				비고
* ----	---------	----------------	---------------------	-----------
*	1	박정현		2018-07-04
**************************************************************************/
package kr.ispark.superMng.superMng.superMng.userAdminMng.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;
import egovframework.rte.psl.dataaccess.util.EgovMap;
import kr.ispark.common.system.service.DeptVO;
import kr.ispark.common.system.service.impl.CommonServiceImpl;
import kr.ispark.superMng.superMng.superMng.userAdminMng.service.UserAdminMngVO;

@Service
public class UserAdminMngServiceImpl extends EgovAbstractServiceImpl {

	@Resource
	private UserAdminMngDAO userAdminMngDAO;
	
	@Autowired
	CommonServiceImpl commonServiceImpl;

	/**
	 * 사용자관리 목록 조회
	 * @param	UserAdminMngVO searchVO
	 * @return	List<UserAdminMngVO>
	 * @throws	Exception
	 */
	public List<UserAdminMngVO> selectList(UserAdminMngVO searchVO) throws Exception {
		EgovMap emap = new EgovMap();
		emap.put("paramCompId", searchVO.getFindCompId());
		searchVO.setTargetDbId(commonServiceImpl.selectDbId(emap));
		return userAdminMngDAO.selectList(searchVO);
	}
	
	/**
	 * 회사 목록 조회
	 * @param	UserAdminMngVO searchVO
	 * @return	List<UserAdminMngVO>
	 * @throws	Exception
	 */
	public List<UserAdminMngVO> selectCompList(UserAdminMngVO searchVO) throws Exception {
		EgovMap emap = new EgovMap();
		emap.put("paramCompId", searchVO.getFindCompId());
		searchVO.setTargetDbId(commonServiceImpl.selectDbId(emap));
		return userAdminMngDAO.selectCompList(searchVO);
	}
	
	/**
	 * 조직 목록 조회
	 * @param	UserAdminMngVO searchVO
	 * @return	List<DeptVO>
	 * @throws	Exception
	 */
	public List<DeptVO> selectDeptList(UserAdminMngVO searchVO) throws Exception {
		EgovMap emap = new EgovMap();
		emap.put("paramCompId", searchVO.getFindCompId());
		searchVO.setTargetDbId(commonServiceImpl.selectDbId(emap));
		return userAdminMngDAO.selectDeptList(searchVO);
	}

	/**
	 * 권한 목록 조회
	 * @param	UserAdminMngVO searchVO
	 * @return	List<UserAdminMngVO>
	 * @throws	Exception
	 */
	public List<UserAdminMngVO> selectAuthList(UserAdminMngVO searchVO) throws Exception {
		EgovMap emap = new EgovMap();
		emap.put("paramCompId", searchVO.getFindCompId());
		searchVO.setTargetDbId(commonServiceImpl.selectDbId(emap));
		return userAdminMngDAO.selectAuthList(searchVO);
	}
}

