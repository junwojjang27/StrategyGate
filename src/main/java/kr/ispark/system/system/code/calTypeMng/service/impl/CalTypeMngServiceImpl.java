/*************************************************************************
* CLASS 명	: CalTypeMngServiceIpml
* 작 업 자	: 현걸욱
* 작 업 일	: 2018-2-6
* 기	능	: 산식관리 ServiceIpml
* ---------------------------- 변 경 이 력 --------------------------------
* 번호	작 업 자		작	업	일			변 경 내 용				비고
* ----	---------	----------------	---------------------	-----------
*	1	현걸욱		2018-2-6
**************************************************************************/
package kr.ispark.system.system.code.calTypeMng.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;
import kr.ispark.system.system.code.calTypeMng.service.CalTypeMngVO;
import kr.ispark.common.system.service.impl.IdGenServiceImpl;
import kr.ispark.common.util.CommonUtil;

@Service
public class CalTypeMngServiceImpl extends EgovAbstractServiceImpl {

	@Resource
	private IdGenServiceImpl idgenService;
	
	@Resource
	private CalTypeMngDAO calTypeMngDAO;

	// 산식관리관리 목록 조회
	public List<CalTypeMngVO> selectList(CalTypeMngVO searchVO) throws Exception {
		return calTypeMngDAO.selectList(searchVO);
	}
	
	// 산식관리 상세 조회
	public CalTypeMngVO selectDetail(CalTypeMngVO searchVO) throws Exception {
		return calTypeMngDAO.selectDetail(searchVO);
	}
	
	// 산식관리 정렬순서저장
	public int updateSortOrder(CalTypeMngVO dataVO) throws Exception {
		int resultCnt = 0;
		for(CalTypeMngVO paramVO : dataVO.getGridDataList()) {
			resultCnt += calTypeMngDAO.updateSortOrder(paramVO);
		}
		return resultCnt;
	}

	// 산식관리 삭제
	public int deleteCalTypeMng(CalTypeMngVO dataVO) throws Exception {
		return calTypeMngDAO.deleteCalTypeMng(dataVO);
	}
	
	// 산식관리 저장
	public int saveData(CalTypeMngVO dataVO) throws Exception {
		String key = "";
		if(CommonUtil.isEmpty(dataVO.getCalTypeId())) {
			key = idgenService.selectNextSeq("COM_CAL_TYPE", 3);
			dataVO.setCalTypeId(key);
			return calTypeMngDAO.insertData(dataVO);
		} else {
			return calTypeMngDAO.updateData(dataVO);
		}
	}
}

