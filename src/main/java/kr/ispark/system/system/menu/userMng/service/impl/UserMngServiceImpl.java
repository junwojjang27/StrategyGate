/*************************************************************************
* CLASS 명	: UserMngServiceIpml
* 작 업 자	: 현걸욱
* 작 업 일	: 2018-1-21
* 기	능	: 사용자관리 ServiceIpml
* ---------------------------- 변 경 이 력 --------------------------------
* 번호	작 업 자		작	업	일			변 경 내 용				비고
* ----	---------	----------------	---------------------	-----------
*	1	현걸욱		2018-1-21
**************************************************************************/
package kr.ispark.system.system.menu.userMng.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;
import kr.ispark.system.system.menu.userMng.service.UserMngVO;
import kr.ispark.common.system.service.impl.IdGenServiceImpl;
import kr.ispark.common.util.CommonUtil;

@Service
public class UserMngServiceImpl extends EgovAbstractServiceImpl {
	
	@Resource
	private UserMngDAO userMngDAO;

	// 사용자관리관리 목록 조회
	public List<UserMngVO> selectList(UserMngVO searchVO) throws Exception {
		return userMngDAO.selectList(searchVO);
	}
}

