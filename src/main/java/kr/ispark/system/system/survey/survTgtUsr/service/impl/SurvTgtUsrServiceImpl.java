/*************************************************************************
* CLASS 명	: SurvTgtUsrServiceIpml
* 작 업 자	: 최학룡
* 작 업 일	: 2018-10-12
* 기	능	: 설문대상자 ServiceIpml
* ---------------------------- 변 경 이 력 --------------------------------
* 번호	작 업 자		작	업	일			변 경 내 용				비고
* ----	---------	----------------	---------------------	-----------
*	1	최학룡		2018-10-12
**************************************************************************/
package kr.ispark.system.system.survey.survTgtUsr.service.impl;

import java.util.List;

import javax.annotation.Resource;

import kr.ispark.common.system.service.impl.IdGenServiceImpl;
import kr.ispark.system.system.survey.survTgtUsr.service.SurvTgtUsrVO;

import org.springframework.stereotype.Service;

import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;

@Service
public class SurvTgtUsrServiceImpl extends EgovAbstractServiceImpl {

	@Resource
	private IdGenServiceImpl idgenService;

	@Resource
	private SurvTgtUsrDAO survTgtUsrDAO;

	/**
	 * 설문대상자 목록 조회
	 * @param	SurvTgtUsrVO searchVO
	 * @return	List<SurvTgtUsrVO>
	 * @throws	Exception
	 */
	public List<SurvTgtUsrVO> selectList(SurvTgtUsrVO searchVO) throws Exception {
		return survTgtUsrDAO.selectList(searchVO);
	}

	/**
	 * 설문대상자 삭제
	 * @param	SurvTgtUsrVO searchVO
	 * @return	int
	 * @throws	Exception
	 */
	public int deleteSurvTgtUsr(SurvTgtUsrVO dataVO) throws Exception {
		return survTgtUsrDAO.deleteSurvTgtUsr(dataVO);
	}

	/**
	 * 설문대상자 저장
	 * @param	SurvTgtUsrVO searchVO
	 * @return	int
	 * @throws	Exception
	 */
	public int saveData(SurvTgtUsrVO dataVO) throws Exception {
		List<SurvTgtUsrVO> gridDataList = dataVO.getGridDataList();
		int resultCnt = 0;

		survTgtUsrDAO.deleteAllSurvTgtUsr(dataVO);
		if(gridDataList != null && 0<gridDataList.size()){
			for(SurvTgtUsrVO paramvo : gridDataList){
				paramvo.setSurveyId(dataVO.getFindSurveyId());
				resultCnt += survTgtUsrDAO.insertData(paramvo);
			}
		}
		return resultCnt;
	}

	/**
	 * 전직원추가
	 */
	public int saveAllUser(SurvTgtUsrVO dataVO) throws Exception {
		int resultCnt = 0;
		resultCnt += survTgtUsrDAO.deleteAllUser(dataVO);
		resultCnt += survTgtUsrDAO.insertAllUser(dataVO);
		return resultCnt;
	}
}

