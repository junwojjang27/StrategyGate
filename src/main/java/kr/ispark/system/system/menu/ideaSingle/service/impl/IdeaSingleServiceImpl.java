package kr.ispark.system.system.menu.ideaSingle.service.impl;

import java.util.List;

import javax.annotation.Resource;

import kr.ispark.common.security.service.UserVO;
import kr.ispark.common.util.SessionUtil;
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
	 * 간단 IDEA+ 목록 조회
	 * @param	IdeaSingleVO searchVO
	 * @return	List<IdeaSingleVO>
	 * @throws	Exception
	 */
	public List<IdeaSingleVO> selectList(IdeaSingleVO searchVO) throws Exception {
		return ideaSingleDAO.selectList(searchVO);
	}

	/**
	 * 간단 IDEA+ 상세 조회
	 * @param	IdeaSingleVO searchVO
	 * @return	IdeaSingleVO
	 * @throws	Exception
	 */
	public IdeaSingleVO selectDetail(IdeaSingleVO searchVO) throws Exception {
		return ideaSingleDAO.selectDetail(searchVO);
	}

	/**
	 * 간단 IDEA+ 정렬순서저장
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
	 * 간단 IDEA+ 삭제
	 * @param	IdeaSingleVO searchVO
	 * @return	int
	 * @throws	Exception
	 */
	public int deleteIdeaSingle(IdeaSingleVO dataVO) throws Exception {
		return ideaSingleDAO.deleteIdeaSingle(dataVO);
	}

	/**
	 * 간단 IDEA+ 저장
	 * @param	IdeaSingleVO searchVO
	 * @return	int
	 * @throws	Exception
	 */
	public int saveData(IdeaSingleVO dataVO) throws Exception {

		//id setting
		UserVO uvo = SessionUtil.getUserVO();
		dataVO.setUserId(uvo!=null?uvo.getUserId():null);

		String key = "";
		if(!CommonUtil.isEmpty(dataVO.getIdeaCd())) {
			return ideaSingleDAO.updateData(dataVO);
		} else {
			key = idgenService.selectNextSeqByYear("IDEA_INFO", dataVO.getYear(), "S", 6, "0");
			//key = idgenService.selectNextSeq("IDEA_INFO", 3);
			dataVO.setIdeaCd(key);
			return ideaSingleDAO.insertData(dataVO);
		}
	}
}