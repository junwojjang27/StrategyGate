/*************************************************************************
* CLASS 명	: IdeaUsServiceIpml
* 작 업 자	: 하성준
* 작 업 일	: 2021-09-08
* 기	능	: 혁신제안 ServiceIpml
* ---------------------------- 변 경 이 력 --------------------------------
* 번호	작 업 자		작	업	일			변 경 내 용				비고
* ----	---------	----------------	---------------------	-----------
*	1	하성준		2021-09-08
**************************************************************************/
package kr.ispark.system.system.menu.ideaUs.service.impl;

import java.util.List;

import javax.annotation.Resource;

import kr.ispark.common.security.service.UserVO;
import kr.ispark.common.util.SessionUtil;
import org.springframework.stereotype.Service;

import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;
import kr.ispark.system.system.menu.ideaUs.service.IdeaUsVO;
import kr.ispark.common.system.service.impl.IdGenServiceImpl;
import kr.ispark.common.util.CommonUtil;

@Service
public class IdeaUsServiceImpl extends EgovAbstractServiceImpl {

	@Resource
	private IdGenServiceImpl idgenService;
	
	@Resource
	private IdeaUsDAO ideaUsDAO;

	/**
	 * 혁신제안 목록 조회
	 * @param	IdeaUsVO searchVO
	 * @return	List<IdeaUsVO>
	 * @throws	Exception
	 */
	public List<IdeaUsVO> selectList(IdeaUsVO searchVO) throws Exception {
		return ideaUsDAO.selectList(searchVO);
	}
	
	/**
	 * 혁신제안 상세 조회
	 * @param	IdeaUsVO searchVO
	 * @return	IdeaUsVO
	 * @throws	Exception
	 */
	public IdeaUsVO selectDetail(IdeaUsVO searchVO) throws Exception {
		return ideaUsDAO.selectDetail(searchVO);
	}
	
	/**
	 * 혁신제안 정렬순서저장
	 * @param	IdeaUsVO searchVO
	 * @return	int
	 * @throws	Exception
	 */
	public int updateSortOrder(IdeaUsVO dataVO) throws Exception {
		int resultCnt = 0;
		for(IdeaUsVO paramVO : dataVO.getGridDataList()) {
			resultCnt += ideaUsDAO.updateSortOrder(paramVO);
		}
		return resultCnt;
	}

	/**
	 * 혁신제안 삭제
	 * @param	IdeaUsVO searchVO
	 * @return	int
	 * @throws	Exception
	 */
	public int deleteIdeaUs(IdeaUsVO dataVO) throws Exception {
		return ideaUsDAO.deleteIdeaUs(dataVO);
	}
	
	/**
	 * 혁신제안 저장
	 * @param	IdeaUsVO searchVO
	 * @return	int
	 * @throws	Exception
	 */
	public int saveData(IdeaUsVO dataVO) throws Exception {

		UserVO uvo = SessionUtil.getUserVO();
		//dataVO.setSeq(seq);
		dataVO.setUserId(uvo!=null?uvo.getUserId():null);
		//dataVO.setInsertUserIp(uvo!=null?uvo.getIp():null);

		String key = "";
		if(CommonUtil.isEmpty(dataVO.getIdeaCd())) {
			key = idgenService.selectNextSeqByYear("IDEA_INFO", dataVO.getYear(), "S", 6, "0");
			dataVO.setIdeaCd(key);
			return ideaUsDAO.insertData(dataVO);
		} else {
			return ideaUsDAO.updateData(dataVO);
		}
	}
}

