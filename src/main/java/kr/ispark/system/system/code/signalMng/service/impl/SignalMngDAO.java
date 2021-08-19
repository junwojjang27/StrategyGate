/*************************************************************************
* CLASS 명	: SignalMngDAO
* 작 업 자	: 현걸욱
* 작 업 일	: 2018-2-6
* 기	능	: 신호등관리 DAO
* ---------------------------- 변 경 이 력 --------------------------------
* 번호	작 업 자		작	업	일			변 경 내 용				비고
* ----	---------	----------------	---------------------	-----------
*	1	현걸욱		2018-2-6
**************************************************************************/
package kr.ispark.system.system.code.signalMng.service.impl;


import java.util.List;

import org.springframework.stereotype.Repository;

import egovframework.com.cmm.service.impl.EgovComAbstractDAO;
import kr.ispark.system.system.code.signalMng.service.SignalMngVO;

@Repository
public class SignalMngDAO extends EgovComAbstractDAO {
	/**
	 * 신호등관리 조회
	 */
	public List<SignalMngVO> selectList(SignalMngVO searchVO) throws Exception {
		return selectList("system.code.signalMng.selectList", searchVO);
	}
	
	/**
	 * 신호등관리 저장
	 */
	public int insertData(SignalMngVO searchVO) {
		return update("system.code.signalMng.insertData", searchVO);
	}

	/**
	 * 신호등관리 수정
	 */
	public int updateData(SignalMngVO searchVO) {
		return update("system.code.signalMng.updateData", searchVO);
	}
}

