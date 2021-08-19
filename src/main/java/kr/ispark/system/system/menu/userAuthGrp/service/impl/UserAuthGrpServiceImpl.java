/*************************************************************************
* CLASS 명	: UserAuthGrpServiceIpml
* 작 업 자	: 현걸욱
* 작 업 일	: 2018-1-19
* 기	능	: 사용자권한그룹설정 ServiceIpml
* ---------------------------- 변 경 이 력 --------------------------------
* 번호	작 업 자		작	업	일			변 경 내 용				비고
* ----	---------	----------------	---------------------	-----------
*	1	현걸욱		2018-1-19
**************************************************************************/
package kr.ispark.system.system.menu.userAuthGrp.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;
import kr.ispark.system.system.menu.userAuthGrp.service.UserAuthGrpVO;
import kr.ispark.common.system.service.impl.IdGenServiceImpl;
import kr.ispark.common.util.CommonUtil;

@Service
public class UserAuthGrpServiceImpl extends EgovAbstractServiceImpl {

	@Resource
	private IdGenServiceImpl idgenService;
	
	@Resource
	private UserAuthGrpDAO userAuthGrpDAO;

	// 사용자권한그룹설정관리 목록 조회
	public List<UserAuthGrpVO> selectList(UserAuthGrpVO searchVO) throws Exception {
		return userAuthGrpDAO.selectList(searchVO);
	}
	
	// 실조직 조회
	public List<UserAuthGrpVO> selectDeptList(UserAuthGrpVO searchVO) throws Exception {
		return userAuthGrpDAO.selectDeptList(searchVO);
	}

	// 직원조회 조회
	public List<UserAuthGrpVO> selectUserList(UserAuthGrpVO searchVO) throws Exception {
		return userAuthGrpDAO.selectUserList(searchVO);
	}
	
	// 등록된 직원  조회
	public List<UserAuthGrpVO> selectSelectedUserList(UserAuthGrpVO searchVO) throws Exception {
		return userAuthGrpDAO.selectSelectedUserList(searchVO);
	}
	
	// 사용자권한그룹설정 상세 조회
	public UserAuthGrpVO selectDetail(UserAuthGrpVO searchVO) throws Exception {
		return userAuthGrpDAO.selectDetail(searchVO);
	}
	
	// 사용자권한그룹설정 정렬순서저장
	public int updateSortOrder(UserAuthGrpVO dataVO) throws Exception {
		int resultCnt = 0;
		for(UserAuthGrpVO paramVO : dataVO.getGridDataList()) {
			resultCnt += userAuthGrpDAO.updateSortOrder(paramVO);
		}
		return resultCnt;
	}

	// 사용자권한그룹설정 삭제
	public int deleteUserAuthGrp(UserAuthGrpVO dataVO) throws Exception {
		return userAuthGrpDAO.deleteUserAuthGrp(dataVO);
	}
	
	// 사용자권한그룹설정 저장
	public int saveData(UserAuthGrpVO dataVO) throws Exception {
		
		int resultCnt = 0;
		String[] userSelectedDatas = dataVO.getUserSelectedData();
		String[] deptSelectedDatas = dataVO.getDeptSelectedData();
		
		resultCnt += userAuthGrpDAO.deleteUserAuthGrp(dataVO);
		
		if(userSelectedDatas != null && 0<userSelectedDatas.length && deptSelectedDatas != null && 0<deptSelectedDatas.length){
			for(int i=0 ; i<userSelectedDatas.length ; i++){
				dataVO.setUserId(userSelectedDatas[i]);
				dataVO.setDeptId(deptSelectedDatas[i]);
				resultCnt += userAuthGrpDAO.insertData(dataVO);
			}
		}
		return resultCnt;
	}
}

