/*************************************************************************
* CLASS 명	: SurvQuesGrpServiceIpml
* 작 업 자	: 최학룡
* 작 업 일	: 2018-10-11
* 기	능	: 설문질문그룹 ServiceIpml
* ---------------------------- 변 경 이 력 --------------------------------
* 번호	작 업 자		작	업	일			변 경 내 용				비고
* ----	---------	----------------	---------------------	-----------
*	1	최학룡		2018-10-11
**************************************************************************/
package kr.ispark.system.system.survey.survQuesGrp.service.impl;

import java.util.List;

import javax.annotation.Resource;

import kr.ispark.common.system.service.impl.IdGenServiceImpl;
import kr.ispark.common.util.CommonUtil;
import kr.ispark.system.system.survey.survQuesGrp.service.SurvQuesGrpVO;

import org.springframework.stereotype.Service;

import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;

@Service
public class SurvQuesGrpServiceImpl extends EgovAbstractServiceImpl {

	@Resource
	private IdGenServiceImpl idgenService;

	@Resource
	private SurvQuesGrpDAO survQuesGrpDAO;

	/**
	 * 설문질문그룹 목록 조회
	 * @param	SurvQuesGrpVO searchVO
	 * @return	List<SurvQuesGrpVO>
	 * @throws	Exception
	 */
	public List<SurvQuesGrpVO> selectList(SurvQuesGrpVO searchVO) throws Exception {
		return survQuesGrpDAO.selectList(searchVO);
	}

	/**
	 * 설문질문그룹별 설문매핑 목록 조회
	 */
	public List<SurvQuesGrpVO> selectListForMap(SurvQuesGrpVO searchVO) throws Exception {
		return survQuesGrpDAO.selectListForMap(searchVO);
	}

	/**
	 * 설문질문그룹 삭제
	 * @param	SurvQuesGrpVO searchVO
	 * @return	int
	 * @throws	Exception
	 */
	public int deleteSurvQuesGrp(SurvQuesGrpVO dataVO) throws Exception {
		survQuesGrpDAO.deleteSurvQues(dataVO);
		return survQuesGrpDAO.deleteSurvQuesGrp(dataVO);
	}

	/**
	 * 설문질문그룹 저장
	 * @param	SurvQuesGrpVO searchVO
	 * @return	int
	 * @throws	Exception
	 */
	public int saveData(SurvQuesGrpVO dataVO) throws Exception {
		List<SurvQuesGrpVO> gridDataList = dataVO.getGridDataList();
		int resultCnt = 0;
		String key = "";

		if(gridDataList != null && 0<gridDataList.size()){
			for(SurvQuesGrpVO paramvo : gridDataList){
				paramvo.setSurveyId(dataVO.getFindSurveyId());
				if(CommonUtil.isEmpty(paramvo.getQuesGrpId())) {
					key = idgenService.selectNextSeq("SUR_QUES_GRP", "QG", 5, "0");
					paramvo.setQuesGrpId(key);
					resultCnt += survQuesGrpDAO.insertData(paramvo);
				} else {
					resultCnt += survQuesGrpDAO.updateData(paramvo);
				}
			}
		}
		return resultCnt;
	}

	/**
	 * 설문질문그룹별 설문매핑 저장
	 */
	public int saveDataForMap(SurvQuesGrpVO dataVO) throws Exception {
		List<SurvQuesGrpVO> gridDataList = dataVO.getGridDataList();
		int resultCnt = 0;

		if(gridDataList != null && 0<gridDataList.size()){
			for(SurvQuesGrpVO paramvo : gridDataList){
				paramvo.setSurveyId(dataVO.getFindSurveyId());
					resultCnt += survQuesGrpDAO.updateDataForMap(paramvo);
			}
		}
		return resultCnt;
	}
}

