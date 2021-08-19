/*************************************************************************
* CLASS 명	: SurvResultServiceIpml
* 작 업 자	: 최학룡
* 작 업 일	: 2018-10-22
* 기	능	: 설문결과 ServiceIpml
* ---------------------------- 변 경 이 력 --------------------------------
* 번호	작 업 자		작	업	일			변 경 내 용				비고
* ----	---------	----------------	---------------------	-----------
*	1	최학룡		2018-10-22
**************************************************************************/
package kr.ispark.system.system.survey.survResult.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;
import kr.ispark.common.system.service.impl.IdGenServiceImpl;
import kr.ispark.system.system.survey.survResult.service.SurvResultVO;

@Service
public class SurvResultServiceImpl extends EgovAbstractServiceImpl {

	@Resource
	private IdGenServiceImpl idgenService;

	@Resource
	private SurvResultDAO survResultDAO;

	/**
	 * 질문그룹&최종(가중치,점수) 목록 조회
	 * @param	SurvResultVO searchVO
	 * @return	List<SurvResultVO>
	 * @throws	Exception
	 */
	public List<SurvResultVO> selectList(SurvResultVO searchVO) throws Exception {
		return survResultDAO.selectList(searchVO);
	}

	/**
	 * 설문결과 상세 조회
	 * @param	SurvResultVO searchVO
	 * @return	SurvResultVO
	 * @throws	Exception
	 */
	public SurvResultVO selectDetail(SurvResultVO searchVO) throws Exception {
		return survResultDAO.selectDetail(searchVO);
	}

	/**
	 * 설문질문 목록 조회
	 */
	public List<SurvResultVO> selectQuesList(SurvResultVO searchVO) throws Exception {
		return survResultDAO.selectQuesList(searchVO);
	}

	/**
	 * 설문답변 목록 조회
	 */
	public List<SurvResultVO> selectItemList(SurvResultVO searchVO) throws Exception {
		return survResultDAO.selectItemList(searchVO);
	}

	/**
	 * 차트 데이터 목록 조회
	 */
	public List<SurvResultVO> selectChartData(SurvResultVO searchVO) throws Exception {
		return survResultDAO.selectChartData(searchVO);
	}

	/**
	 * 주관식 목록 조회
	 */
	public List<SurvResultVO> selectEssayList(SurvResultVO searchVO) throws Exception {
		return survResultDAO.selectEssayList(searchVO);
	}
}

