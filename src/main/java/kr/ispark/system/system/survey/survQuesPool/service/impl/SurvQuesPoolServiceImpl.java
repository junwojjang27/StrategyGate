/*************************************************************************
* CLASS 명	: SurvQuesPoolServiceIpml
* 작 업 자	: 최학룡
* 작 업 일	: 2018-10-04
* 기	능	: 설문질문pool ServiceIpml
* ---------------------------- 변 경 이 력 --------------------------------
* 번호	작 업 자		작	업	일			변 경 내 용				비고
* ----	---------	----------------	---------------------	-----------
*	1	최학룡		2018-10-04
**************************************************************************/
package kr.ispark.system.system.survey.survQuesPool.service.impl;

import java.util.List;

import javax.annotation.Resource;

import kr.ispark.common.system.service.impl.IdGenServiceImpl;
import kr.ispark.common.util.CommonUtil;
import kr.ispark.system.system.survey.survQuesPool.service.SurvQuesPoolVO;

import org.springframework.stereotype.Service;

import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;

@Service
public class SurvQuesPoolServiceImpl extends EgovAbstractServiceImpl {

	@Resource
	private IdGenServiceImpl idgenService;

	@Resource
	private SurvQuesPoolDAO survQuesPoolDAO;

	/**
	 * 설문질문pool 목록 조회
	 * @param	SurvQuesPoolVO searchVO
	 * @return	List<SurvQuesPoolVO>
	 * @throws	Exception
	 */
	public List<SurvQuesPoolVO> selectList(SurvQuesPoolVO searchVO) throws Exception {
		return survQuesPoolDAO.selectList(searchVO);
	}

	/**
	 * 설문질문pool 상세 조회
	 * @param	SurvQuesPoolVO searchVO
	 * @return	SurvQuesPoolVO
	 * @throws	Exception
	 */
	public SurvQuesPoolVO selectDetail(SurvQuesPoolVO searchVO) throws Exception {
		return survQuesPoolDAO.selectDetail(searchVO);
	}

	/**
	 * 설문질문pool답변항목 목록 조회
	 */
	public List<SurvQuesPoolVO> selectItemList(SurvQuesPoolVO searchVO) throws Exception {
		return survQuesPoolDAO.selectItemList(searchVO);
	}

	/**
	 * 설문질문pool 삭제
	 * @param	SurvQuesPoolVO searchVO
	 * @return	int
	 * @throws	Exception
	 */
	public int deleteSurvQuesPool(SurvQuesPoolVO dataVO) throws Exception {
		return survQuesPoolDAO.deleteSurvQuesPool(dataVO);
	}

	/**
	 * 설문질문pool 저장
	 * @param	SurvQuesPoolVO searchVO
	 * @return	int
	 * @throws	Exception
	 */
	public String saveData(SurvQuesPoolVO dataVO) throws Exception {
		int resultCnt = 0;
		String key = "";
		String rtnVal = "";
		if(CommonUtil.isEmpty(dataVO.getQuesPoolId())) {
			key = idgenService.selectNextSeq("SUR_QUES_POOL", "QP", 5, "0");
			dataVO.setQuesPoolId(key);
			resultCnt = survQuesPoolDAO.insertData(dataVO);
			quesPoolItemProc(dataVO, resultCnt);
			rtnVal = key;
		} else {
			resultCnt = survQuesPoolDAO.updateData(dataVO);
			quesPoolItemProc(dataVO, resultCnt);
			rtnVal = dataVO.getQuesPoolId();
		}
		return rtnVal;
	}

	public int quesPoolItemProc(SurvQuesPoolVO dataVO, int resultCnt) throws Exception {
		String subKey = "";
		int subResultCnt = 0;
		if(resultCnt > 0) {
			subResultCnt += survQuesPoolDAO.deleteQuesPoolItem(dataVO);
			if(!"002".equals(dataVO.getQuesGbnId())) {
				String[] itemContentArr = dataVO.getItemContent().split("\\,",0);

				for(int i=0; i<Integer.parseInt(dataVO.getItemCntId()); i++) {
					subKey = idgenService.selectNextSeq("SUR_QUES_POOL_ITEM", "QI", 5, "0");
					dataVO.setQuesItemId(subKey);
					dataVO.setItemNum((i+1));
					dataVO.setItemContent(itemContentArr[i]);
					subResultCnt += survQuesPoolDAO.insertQuesPoolItem(dataVO);
				}
			}
		}
		return subResultCnt;
	}
}

