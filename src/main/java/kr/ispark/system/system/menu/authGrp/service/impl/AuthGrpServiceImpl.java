/*************************************************************************
* CLASS 명	: AuthGrpServiceIpml
* 작 업 자	: joey
* 작 업 일	: 2018-1-12
* 기	능	: 그룹별권한 ServiceIpml
* ---------------------------- 변 경 이 력 --------------------------------
* 번호	작 업 자		작	업	일			변 경 내 용				비고
* ----	---------	----------------	---------------------	-----------
*	1	joey		2018-1-12
**************************************************************************/
package kr.ispark.system.system.menu.authGrp.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;
import kr.ispark.system.system.menu.authGrp.service.AuthGrpVO;
import kr.ispark.common.system.service.impl.IdGenServiceImpl;
import kr.ispark.common.util.CommonUtil;

@Service
public class AuthGrpServiceImpl extends EgovAbstractServiceImpl {

	@Resource
	private IdGenServiceImpl idgenService;
	
	@Resource
	private AuthGrpDAO authGrpDao;

	// 그룹별권한관리 목록 조회
	public List<AuthGrpVO> selectList(AuthGrpVO searchVO) throws Exception {
		return authGrpDao.selectList(searchVO);
	}
	
	// 그룹별권한관리 목록 조회
	public List<AuthGrpVO> selectMenuList(AuthGrpVO searchVO) throws Exception {
		return authGrpDao.selectMenuList(searchVO);
	}
	
	// 그룹별권한관리 목록 조회
	public List<AuthGrpVO> selectMenuSelectedList(AuthGrpVO searchVO) throws Exception {
		return authGrpDao.selectMenuSelectedList(searchVO);
	}
	
	// 그룹별권한 상세 조회
	public AuthGrpVO selectDetail(AuthGrpVO searchVO) throws Exception {
		return authGrpDao.selectDetail(searchVO);
	}
	
	// 그룹별권한 정렬순서저장
	public int updateSortOrder(AuthGrpVO dataVO) throws Exception {
		int resultCnt = 0;
		for(AuthGrpVO paramVO : dataVO.getGridDataList()) {
			resultCnt += authGrpDao.updateSortOrder(paramVO);
		}
		return resultCnt;
	}

	// 그룹별권한 삭제
	public int deleteAuthGrp(AuthGrpVO dataVO) throws Exception {
		return authGrpDao.deleteAuthGrp(dataVO);
	}
	
	// 그룹별권한 저장
	public int saveData(AuthGrpVO dataVO) throws Exception {
		
		int resultCnt = 0;
		
		resultCnt += authGrpDao.deleteData(dataVO);
		
		String[] authGrps = dataVO.getMenuSelectedDataList().split("\\|",0);
		if(authGrps != null && 0<authGrps.length){
			for(String pgmId:authGrps){
				dataVO.setPgmId(pgmId);
				resultCnt += authGrpDao.insertData(dataVO);
			}
		}
		
		return resultCnt;
		
	}
}

