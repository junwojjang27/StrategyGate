/*************************************************************************
* CLASS 명	: SystemSettingServiceIpml
* 작 업 자	: 현걸욱
* 작 업 일	: 2018-11-01
* 기	능	: 시스템설정 ServiceIpml
* ---------------------------- 변 경 이 력 --------------------------------
* 번호	작 업 자		작	업	일			변 경 내 용				비고
* ----	---------	----------------	---------------------	-----------
*	1	현걸욱		2018-11-01
**************************************************************************/
package kr.ispark.system.system.comp.systemSetting.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;
import kr.ispark.system.system.comp.systemSetting.service.SystemSettingVO;
import kr.ispark.common.system.service.impl.IdGenServiceImpl;
import kr.ispark.common.util.CommonUtil;

@Service
public class SystemSettingServiceImpl extends EgovAbstractServiceImpl {

	@Resource
	private IdGenServiceImpl idgenService;
	
	@Resource
	private SystemSettingDAO systemSettingDAO;

	/**
	 * 시스템설정 상세 조회
	 * @param	SystemSettingVO searchVO
	 * @return	SystemSettingVO
	 * @throws	Exception
	 */
	public SystemSettingVO selectApproveDetail(SystemSettingVO searchVO) throws Exception {
		return systemSettingDAO.selectApproveDetail(searchVO);
	}
	
	/**
	 * 시스템설정 상세 조회
	 * @param	SystemSettingVO searchVO
	 * @return	SystemSettingVO
	 * @throws	Exception
	 */
	public SystemSettingVO selectScoreDetail(SystemSettingVO searchVO) throws Exception {
		return systemSettingDAO.selectScoreDetail(searchVO);
	}
	
	/**
	 * 시스템설정 저장
	 * @param	SystemSettingVO searchVO
	 * @return	int
	 * @throws	Exception
	 */
	public int saveData(SystemSettingVO dataVO) throws Exception {
		int resultCnt = 0;
		
		resultCnt += systemSettingDAO.deleteApproveData(dataVO);
		resultCnt += systemSettingDAO.deleteScoreData(dataVO);
		resultCnt += systemSettingDAO.insertApproveData(dataVO);
		resultCnt += systemSettingDAO.insertScoreData(dataVO);
		
		return resultCnt;
	}
}

