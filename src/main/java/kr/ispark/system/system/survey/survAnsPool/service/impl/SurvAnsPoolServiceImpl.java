/*************************************************************************
* CLASS 명	: SurvAnsPoolServiceIpml
* 작 업 자	: 최학룡
* 작 업 일	: 2018-10-02
* 기	능	: 설문답변pool ServiceIpml
* ---------------------------- 변 경 이 력 --------------------------------
* 번호	작 업 자		작	업	일			변 경 내 용				비고
* ----	---------	----------------	---------------------	-----------
*	1	최학룡		2018-10-02
**************************************************************************/
package kr.ispark.system.system.survey.survAnsPool.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;
import kr.ispark.system.system.survey.survAnsPool.service.SurvAnsPoolVO;
import kr.ispark.common.system.service.impl.IdGenServiceImpl;
import kr.ispark.common.util.CommonUtil;

@Service
public class SurvAnsPoolServiceImpl extends EgovAbstractServiceImpl {

	@Resource
	private IdGenServiceImpl idgenService;

	@Resource
	private SurvAnsPoolDAO survAnsPoolDAO;

	/**
	 * 설문답변pool 목록 조회
	 * @param	SurvAnsPoolVO searchVO
	 * @return	List<SurvAnsPoolVO>
	 * @throws	Exception
	 */
	public List<SurvAnsPoolVO> selectList(SurvAnsPoolVO searchVO) throws Exception {
		return survAnsPoolDAO.selectList(searchVO);
	}

	/**
	 * 설문답변pool 상세 조회
	 * @param	SurvAnsPoolVO searchVO
	 * @return	SurvAnsPoolVO
	 * @throws	Exception
	 */
	public SurvAnsPoolVO selectDetail(SurvAnsPoolVO searchVO) throws Exception {
		return survAnsPoolDAO.selectDetail(searchVO);
	}

	/**
	 * 설문답변pool상세 목록 조회
	 */
	public List<SurvAnsPoolVO> selectItemList(SurvAnsPoolVO searchVO) throws Exception {
		return survAnsPoolDAO.selectItemList(searchVO);
	}

	/**
	 * 설문답변pool 팝업 목록 조회
	 */
	public List<SurvAnsPoolVO> selectPopList(SurvAnsPoolVO searchVO) throws Exception {
		return survAnsPoolDAO.selectPopList(searchVO);
	}

	/**
	 * 설문답변pool 일괄저장
	 * @param	SurvAnsPoolVO searchVO
	 * @return	int
	 * @throws	Exception
	 */
	public int saveAllSurvAnsPool(SurvAnsPoolVO dataVO) throws Exception {
		int resultCnt = 0;
		for(SurvAnsPoolVO paramVO : dataVO.getGridDataList()) {
			resultCnt += survAnsPoolDAO.saveAllSurvAnsPool(paramVO);
		}
		return resultCnt;
	}

	/**
	 * 설문답변pool 삭제
	 * @param	SurvAnsPoolVO searchVO
	 * @return	int
	 * @throws	Exception
	 */
	public int deleteSurvAnsPool(SurvAnsPoolVO dataVO) throws Exception {
		return survAnsPoolDAO.deleteSurvAnsPool(dataVO);
	}

	/**
	 * 설문답변pool 저장
	 * @param	SurvAnsPoolVO searchVO
	 * @return	int
	 * @throws	Exception
	 */
	public String saveData(SurvAnsPoolVO dataVO) throws Exception {
		int resultCnt = 0, subResultCnt = 0;
		String key = "";
		String rtnVal = "";
		if(CommonUtil.isEmpty(dataVO.getItemPoolId())) {
			key = idgenService.selectNextSeq("SUR_ITEM_POOL", "IP", 5, "0");
			dataVO.setItemPoolId(key);
			resultCnt= survAnsPoolDAO.insertData(dataVO);
			subResultCnt = itemDetailProc(dataVO, resultCnt);
			if(subResultCnt > 0) {
				rtnVal = key;
			}
		} else {
			resultCnt= survAnsPoolDAO.updateData(dataVO);
			subResultCnt = itemDetailProc(dataVO, resultCnt);
			if(subResultCnt > 0) {
				rtnVal = dataVO.getItemPoolId();
			}
		}

		return rtnVal;
	}

	public int itemDetailProc(SurvAnsPoolVO dataVO, int resultCnt) throws Exception {
		String subKey = "";
		int subResultCnt = 0;
		if(resultCnt > 0) {
			String[] itemContentArr = dataVO.getItemContent().split("\\,",0);

			survAnsPoolDAO.deleteItemDetail(dataVO);
			for(int i=0; i<Integer.parseInt(dataVO.getItemCntId()); i++) {
				subKey = idgenService.selectNextSeq("SUR_ITEM_DETAIL", "I", 6, "0");
				dataVO.setItemId(subKey);
				dataVO.setItemNum((i+1));
				dataVO.setItemContent(itemContentArr[i]);
				subResultCnt += survAnsPoolDAO.insertItemDetail(dataVO);
			}
		}
		return subResultCnt;
	}
	
	/**
	 * 답변항목대표여부 카운트
	 * @param	SurvAnsPoolVO searchVO
	 * @return	int
	 * @throws	Exception
	 */
	public SurvAnsPoolVO mainItemCnt(SurvAnsPoolVO dataVO) throws Exception {
		return survAnsPoolDAO.mainItemCnt(dataVO);
	}
}

