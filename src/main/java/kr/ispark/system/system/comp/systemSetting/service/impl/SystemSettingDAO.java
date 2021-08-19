/*************************************************************************
* CLASS 명	: SystemSettingDAO
* 작 업 자	: 현걸욱
* 작 업 일	: 2018-11-01
* 기	능	: 시스템설정 DAO
* ---------------------------- 변 경 이 력 --------------------------------
* 번호	작 업 자		작	업	일			변 경 내 용				비고
* ----	---------	----------------	---------------------	-----------
*	1	현걸욱		2018-11-01
**************************************************************************/
package kr.ispark.system.system.comp.systemSetting.service.impl;


import java.util.List;

import org.springframework.stereotype.Repository;

import egovframework.com.cmm.service.impl.EgovComAbstractDAO;
import kr.ispark.system.system.comp.systemSetting.service.SystemSettingVO;

@Repository
public class SystemSettingDAO extends EgovComAbstractDAO {
	
	/**
	 * 시스템설정 상세 조회
	 * @param	SystemSettingVO searchVO
	 * @return	SystemSettingVO
	 * @throws	Exception
	 */
	public SystemSettingVO selectApproveDetail(SystemSettingVO searchVO) throws Exception {
		return (SystemSettingVO)selectOne("system.comp.systemSetting.selectApproveDetail", searchVO);
	}
	
	/**
	 * 시스템설정 상세 조회
	 * @param	SystemSettingVO searchVO
	 * @return	SystemSettingVO
	 * @throws	Exception
	 */
	public SystemSettingVO selectScoreDetail(SystemSettingVO searchVO) throws Exception {
		return (SystemSettingVO)selectOne("system.comp.systemSetting.selectScoreDetail", searchVO);
	}
	
	/**
	 * 시스템설정 삭제
	 * @param	SystemSettingVO searchVO
	 * @return	int
	 * @throws	Exception
	 */
	public int deleteApproveData(SystemSettingVO searchVO) throws Exception {
		return update("system.comp.systemSetting.deleteApproveData", searchVO);
	}
	
	/**
	 * 시스템설정 삭제
	 * @param	SystemSettingVO searchVO
	 * @return	int
	 * @throws	Exception
	 */
	public int deleteScoreData(SystemSettingVO searchVO) throws Exception {
		return update("system.comp.systemSetting.deleteScoreData", searchVO);
	}
	
	/**
	 * 시스템설정 저장
	 * @param	SystemSettingVO searchVO
	 * @return	int
	 * @throws	Exception
	 */
	public int insertApproveData(SystemSettingVO searchVO) throws Exception {
		return insert("system.comp.systemSetting.insertApproveData", searchVO);
	}
	
	/**
	 * 시스템설정 저장
	 * @param	SystemSettingVO searchVO
	 * @return	int
	 * @throws	Exception
	 */
	public int insertScoreData(SystemSettingVO searchVO) throws Exception {
		return insert("system.comp.systemSetting.insertScoreData", searchVO);
	}

}

