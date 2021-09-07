/*************************************************************************
* CLASS 명	: IdeaSingleServiceIpml
* 작 업 자	: 하성준
* 작 업 일	: 2021-09-07
* 기	능	: 간단제안 ServiceIpml
* ---------------------------- 변 경 이 력 --------------------------------
* 번호	작 업 자		작	업	일			변 경 내 용				비고
* ----	---------	----------------	---------------------	-----------
*	1	하성준		2021-09-07
**************************************************************************/
package kr.ispark.system.system.menu.ideaSingle.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;
import kr.ispark.system.system.menu.ideaSingle.service.IdeaSingleVO;
import kr.ispark.common.system.service.impl.IdGenServiceImpl;
import kr.ispark.common.util.CommonUtil;

@Service
public class IdeaSingleServiceImpl extends EgovAbstractServiceImpl {

	@Resource
	private IdGenServiceImpl idgenService;
	
	@Resource
	private IdeaSingleDAO ideaSingleDAO;

	/**
	 * 간단제안 목록 조회
	 * @param	IdeaSingleVO searchVO
	 * @return	List<IdeaSingleVO>
	 * @throws	Exception
	 */
	public List<IdeaSingleVO> selectList(IdeaSingleVO searchVO) throws Exception {
		return ideaSingleDAO.selectList(searchVO);
	}
	
	/**
	 * 간단제안 상세 조회
	 * @param	IdeaSingleVO searchVO
	 * @return	IdeaSingleVO
	 * @throws	Exception
	 */
	public IdeaSingleVO selectDetail(IdeaSingleVO searchVO) throws Exception {
		return ideaSingleDAO.selectDetail(searchVO);
	}
	
	/**
	 * 간단제안 정렬순서저장
	 * @param	IdeaSingleVO searchVO
	 * @return	int
	 * @throws	Exception
	 */
	public int updateSortOrder(IdeaSingleVO dataVO) throws Exception {
		int resultCnt = 0;
		for(IdeaSingleVO paramVO : dataVO.getGridDataList()) {
			resultCnt += ideaSingleDAO.updateSortOrder(paramVO);
		}
		return resultCnt;
	}

	/**
	 * 간단제안 삭제
	 * @param	IdeaSingleVO searchVO
	 * @return	int
	 * @throws	Exception
	 */
	public int deleteIdeaSingle(IdeaSingleVO dataVO) throws Exception {
		return ideaSingleDAO.deleteIdeaSingle(dataVO);
	}
	
	/**
	 * 간단제안 저장
	 * @param	IdeaSingleVO searchVO
	 * @return	int
	 * @throws	Exception
	 */
	public int saveData(IdeaSingleVO dataVO) throws Exception {
		String key = "";
		if(CommonUtil.isEmpty(dataVO.getUserId())) {
			key = idgenService.selectNextSeqByYear("originalTableName", dataVO.getYear(), "S", 6, "0");
			dataVO.setUserId(key);
			return ideaSingleDAO.insertData(dataVO);
		} else {
			return ideaSingleDAO.updateData(dataVO);
		}
	}
}

