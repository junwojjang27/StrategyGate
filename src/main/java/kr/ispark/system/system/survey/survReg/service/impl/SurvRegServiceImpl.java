/*************************************************************************
* CLASS 명	: SurvRegServiceIpml
* 작 업 자	: 최학룡
* 작 업 일	: 2018-10-05
* 기	능	: 설문등록 ServiceIpml
* ---------------------------- 변 경 이 력 --------------------------------
* 번호	작 업 자		작	업	일			변 경 내 용				비고
* ----	---------	----------------	---------------------	-----------
*	1	최학룡		2018-10-05
**************************************************************************/
package kr.ispark.system.system.survey.survReg.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;
import kr.ispark.system.system.survey.survReg.service.SurvRegVO;
import kr.ispark.common.system.service.impl.IdGenServiceImpl;
import kr.ispark.common.util.CommonUtil;

@Service
public class SurvRegServiceImpl extends EgovAbstractServiceImpl {

	@Resource
	private IdGenServiceImpl idgenService;

	@Resource
	private SurvRegDAO survRegDAO;

	/**
	 * 설문등록 목록 조회
	 * @param	SurvRegVO searchVO
	 * @return	List<SurvRegVO>
	 * @throws	Exception
	 */
	public List<SurvRegVO> selectList(SurvRegVO searchVO) throws Exception {
		return survRegDAO.selectList(searchVO);
	}

	/**
	 * 설문등록 상세 조회
	 * @param	SurvRegVO searchVO
	 * @return	SurvRegVO
	 * @throws	Exception
	 */
	public SurvRegVO selectDetail(SurvRegVO searchVO) throws Exception {
		return survRegDAO.selectDetail(searchVO);
	}

	/**
	 * 설문등록 설문복사
	 * @param	SurvRegVO searchVO
	 * @return	int
	 * @throws	Exception
	 */
	public int insertSurveyCopy(SurvRegVO dataVO) throws Exception {
		int resultCnt = 0;
		String key = idgenService.selectNextSeq("SUR_SURVEY", "S", 6, "0");
		dataVO.setNewSurveyId(key);
		resultCnt += survRegDAO.insertSurveyCopy(dataVO);
		resultCnt += survRegDAO.insertQuesCopy(dataVO);
		resultCnt += survRegDAO.insertAnsCopy(dataVO);
		resultCnt += survRegDAO.insertGrpCopy(dataVO);

		return resultCnt;
	}

	/**
	 * 설문등록 삭제
	 * @param	SurvRegVO searchVO
	 * @return	int
	 * @throws	Exception
	 */
	public int deleteSurvReg(SurvRegVO dataVO) throws Exception {
		return survRegDAO.deleteSurvReg(dataVO);
	}

	/**
	 * 설문등록 저장
	 * @param	SurvRegVO searchVO
	 * @return	int
	 * @throws	Exception
	 */
	public String saveData(SurvRegVO dataVO) throws Exception {
		int resultCnt = 0;
		String key = "";
		String rtnVal = "";
		if(CommonUtil.isEmpty(dataVO.getSurveyId())) {
			key = idgenService.selectNextSeq("SUR_SURVEY", "S", 6, "0");
			dataVO.setSurveyId(key);
			resultCnt = survRegDAO.insertData(dataVO);
			if(resultCnt > 0) {
				rtnVal = key;
			}
		} else {
			resultCnt = survRegDAO.updateData(dataVO);
			if(resultCnt > 0) {
				rtnVal = dataVO.getSurveyId();
			}
		}
		return rtnVal;
	}
}

