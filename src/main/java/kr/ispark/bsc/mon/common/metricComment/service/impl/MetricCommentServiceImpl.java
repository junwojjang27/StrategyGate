/*************************************************************************
* CLASS 명	: MetricCommentServiceImpl
* 작 업 자	: kimyh
* 작 업 일	: 2018. 04. 17.
* 기	능	: 댓글 Service
* ---------------------------- 변 경 이 력 --------------------------------
* 번호	작 업 자		작	업	일			변 경 내 용				비고
* ----	---------	----------------	---------------------	-----------
*	1	kimyh		2018. 04. 17.		최 초 작 업
**************************************************************************/

package kr.ispark.bsc.mon.common.metricComment.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import egovframework.com.cmm.EgovMessageSource;
import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;
import kr.ispark.bsc.mon.common.metricComment.service.MetricCommentVO;
import kr.ispark.common.util.SessionUtil;

@Service
public class MetricCommentServiceImpl extends EgovAbstractServiceImpl {
	public final Logger log = LoggerFactory.getLogger(this.getClass());

	@Resource(name="egovMessageSource")
	public EgovMessageSource egovMessageSource;
	
	@Resource
	private MetricCommentDAO metricCommentDAO;
	
	/**
	 * 댓글 목록 조회
	 * @param	MetricCommentVO searchVO
	 * @return	List<MetricCommentVO>
	 * @throws	Exception
	 */
	public List<MetricCommentVO> selectList(MetricCommentVO searchVO) throws Exception {
		return metricCommentDAO.selectList(searchVO);
	}
	
	/**
	 * 댓글 조회
	 * @param	MetricCommentVO searchVO
	 * @return	MetricCommentVO
	 * @throws	Exception
	 */
	public MetricCommentVO selectMetricComment(MetricCommentVO searchVO) throws Exception {
		return metricCommentDAO.selectMetricComment(searchVO);
	}

	/**
	 * 댓글 목록수 조회
	 * @param	MetricCommentVO searchVO
	 * @return	int
	 * @throws	Exception
	 */
	public int selectListCount(MetricCommentVO searchVO) throws Exception {
		searchVO.setUserId(SessionUtil.getUserId());
		return metricCommentDAO.selectListCount(searchVO);
	}
	
	/**
	 * 중복 게시 제한 시간내 댓글 수 체크
	 * @param	MetricCommentVO searchVO
	 * @return	int
	 * @throws	Exception
	 */
	public int selectDuplCount(MetricCommentVO searchVO) throws Exception {
		return metricCommentDAO.selectDuplCount(searchVO);
	}
	
	/**
	 * 댓글 등록
	 * @param	MetricCommentVO dataVO
	 * @return	int
	 * @throws	Exception
	 */
	public int insertData(MetricCommentVO dataVO) throws Exception {
    	int seq = metricCommentDAO.selectNextSeq(dataVO);
		dataVO.setSeq(seq);
		dataVO.setUserId(SessionUtil.getUserId());
		dataVO.setInsertUserIp(SessionUtil.getUserVO()!= null?SessionUtil.getUserVO().getIp():null);
		
		// 신규
		if(dataVO.getUpSeq() == -1) {
			dataVO.setGroupSeq(metricCommentDAO.selectNextGroupSeq(dataVO));
			dataVO.setGroupOrder(0);
			dataVO.setGroupLevel(0);
		} else { // 대댓글
			dataVO.setSeq(dataVO.getUpSeq());
			MetricCommentVO metricCommentVO = metricCommentDAO.selectMetricComment(dataVO);
			
			dataVO.setSeq(seq);
			dataVO.setGroupSeq(metricCommentVO.getGroupSeq());
			dataVO.setGroupOrder(metricCommentVO.getGroupOrder());
			dataVO.setGroupLevel(metricCommentVO.getGroupLevel());
			
			int nextGroupOrder = metricCommentDAO.selectMinGroupOrder(dataVO);
			if(nextGroupOrder == -1) {	// 맨 밑에 위치
				nextGroupOrder = metricCommentDAO.selectNextGroupOrder(dataVO);
				dataVO.setGroupOrder(nextGroupOrder);
			} else {	// 중간에 위치
				// 답글 이후 글들의 순서를 1씩 증가
				dataVO.setGroupOrder(nextGroupOrder);
				metricCommentDAO.updateGroupOrder(dataVO);
			}
			dataVO.setGroupLevel(dataVO.getGroupLevel() + 1);
		}
		
		return metricCommentDAO.insertData(dataVO);
	}

	/**
	 * 댓글 수정
	 * @param	MetricCommentVO dataVO
	 * @return	int
	 * @throws	Exception
	 */
	public int updateData(MetricCommentVO dataVO) throws Exception {
		return metricCommentDAO.updateData(dataVO);
	}
	
	/**
	 * 댓글 삭제
	 * @param	MetricCommentVO dataVO
	 * @return	int
	 * @throws	Exception
	 */
	public int deleteData(MetricCommentVO dataVO) throws Exception {
		return metricCommentDAO.deleteData(dataVO);
	}
}
