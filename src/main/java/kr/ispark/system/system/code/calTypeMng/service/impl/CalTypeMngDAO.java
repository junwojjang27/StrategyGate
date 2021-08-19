/*************************************************************************
* CLASS 명	: CalTypeMngDAO
* 작 업 자	: 현걸욱
* 작 업 일	: 2018-2-6
* 기	능	: 산식관리 DAO
* ---------------------------- 변 경 이 력 --------------------------------
* 번호	작 업 자		작	업	일			변 경 내 용				비고
* ----	---------	----------------	---------------------	-----------
*	1	현걸욱		2018-2-6
**************************************************************************/
package kr.ispark.system.system.code.calTypeMng.service.impl;


import java.util.List;

import org.springframework.stereotype.Repository;

import egovframework.com.cmm.service.impl.EgovComAbstractDAO;
import kr.ispark.system.system.code.calTypeMng.service.CalTypeMngVO;

@Repository
public class CalTypeMngDAO extends EgovComAbstractDAO {
	/**
	 * 산식관리 조회
	 */
	public List<CalTypeMngVO> selectList(CalTypeMngVO searchVO) throws Exception {
		return selectList("system.code.calTypeMng.selectList", searchVO);
	}
	
	/**
	 * 산식관리 상세 조회
	 */
	public CalTypeMngVO selectDetail(CalTypeMngVO searchVO) throws Exception {
		return (CalTypeMngVO)selectOne("system.code.calTypeMng.selectDetail", searchVO);
	}
	
	/**
	 * 산식관리 정렬순서저장
	 */
	public int updateSortOrder(CalTypeMngVO searchVO) {
		return update("system.code.calTypeMng.updateSortOrder", searchVO);
	}

	/**
	 * 산식관리삭제
	 */
	public int deleteCalTypeMng(CalTypeMngVO searchVO) {
		return update("system.code.calTypeMng.deleteCalTypeMng", searchVO);
	}
	
	/**
	 * 산식관리 저장
	 */
	public int insertData(CalTypeMngVO searchVO) {
		return update("system.code.calTypeMng.insertData", searchVO);
	}

	/**
	 * 산식관리 수정
	 */
	public int updateData(CalTypeMngVO searchVO) {
		return update("system.code.calTypeMng.updateData", searchVO);
	}
}

