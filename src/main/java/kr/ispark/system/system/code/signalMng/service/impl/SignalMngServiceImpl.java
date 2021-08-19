/*************************************************************************
* CLASS 명	: SignalMngServiceIpml
* 작 업 자	: 현걸욱
* 작 업 일	: 2018-2-6
* 기	능	: 신호등관리 ServiceIpml
* ---------------------------- 변 경 이 력 --------------------------------
* 번호	작 업 자		작	업	일			변 경 내 용				비고
* ----	---------	----------------	---------------------	-----------
*	1	현걸욱		2018-2-6
**************************************************************************/
package kr.ispark.system.system.code.signalMng.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;
import kr.ispark.common.system.service.impl.IdGenServiceImpl;
import kr.ispark.common.util.CommonUtil;
import kr.ispark.system.system.code.signalMng.service.SignalMngVO;

@Service
public class SignalMngServiceImpl extends EgovAbstractServiceImpl {

	@Resource
	private IdGenServiceImpl idgenService;
	
	@Resource
	private SignalMngDAO signalMngDAO;

	// 신호등관리관리 목록 조회
	public List<SignalMngVO> selectList(SignalMngVO searchVO) throws Exception {
		return signalMngDAO.selectList(searchVO);
	}
	
	// 신호등관리 저장
	public int saveData(SignalMngVO dataVO) throws Exception {
		
		List<SignalMngVO> gridDataList = dataVO.getGridDataList();
	    int resultCnt = 0;
		
		if(gridDataList != null && gridDataList.size()>0){
			for(SignalMngVO vo : gridDataList){
				
				if(CommonUtil.isEmpty(vo.getStatusId())) {
					resultCnt += signalMngDAO.insertData(vo);
				} else {
					resultCnt +=  signalMngDAO.updateData(vo);
				}
			}
		}
		
		return resultCnt;
	}
}

