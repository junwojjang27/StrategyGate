/*************************************************************************
* CLASS 명	: MetricCommentDAO
* 작 업 자	: kimyh
* 작 업 일	: 2018. 05. 29.
* 기	능	: 모니터링용 댓글 DAO
* ---------------------------- 변 경 이 력 --------------------------------
* 번호	작 업 자		작	업	일			변 경 내 용				비고
* ----	---------	----------------	---------------------	-----------
*	1	kimyh		2018. 05. 29.		최 초 작 업
**************************************************************************/
package kr.ispark.bsc.mon.common.metricComment.service.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import egovframework.com.cmm.service.impl.EgovComAbstractDAO;
import kr.ispark.bsc.mon.common.metricComment.service.MetricCommentVO;

@Repository
public class MetricCommentDAO extends EgovComAbstractDAO {
	/**
	 * 댓글 목록 조회
	 * @param	MetricCommentVO searchVO
	 * @return	List<MetricCommentVO>
	 * @throws	Exception
	 */
	public List<MetricCommentVO> selectList(MetricCommentVO searchVO) throws Exception {
		return selectList("bsc.mon.common.metricComment.selectList", searchVO);
	}
	
	/**
	 * 댓글 조회
	 * @param	MetricCommentVO searchVO
	 * @return	List<MetricCommentVO>
	 * @throws	Exception
	 */
	public MetricCommentVO selectMetricComment(MetricCommentVO searchVO) throws Exception {
		return selectOne("bsc.mon.common.metricComment.selectMetricComment", searchVO);
	}

	/**
	 * 게시물 목록 수 조회
	 * @param	MetricCommentVO searchVO
	 * @return	int
	 * @throws	Exception
	 */
	public int selectListCount(MetricCommentVO searchVO) throws Exception {
		return (Integer)selectOne("bsc.mon.common.metricComment.selectListCount", searchVO);
	}
	
	/**
	 * 중복 게시 제한 시간내 게시물 수 체크
	 * @param	MetricCommentVO searchVO
	 * @return	int
	 * @throws	Exception
	 */
	public int selectDuplCount(MetricCommentVO searchVO) throws Exception {
		return selectOne("bsc.mon.common.metricComment.selectDuplCount", searchVO);
	}
	
	/**
	 * 신규 게시물 채번
	 * @param	MetricCommentVO searchVO
	 * @return	int
	 * @throws	Exception
	 */
	public int selectNextSeq(MetricCommentVO searchVO) throws Exception {
		return selectOne("bsc.mon.common.metricComment.selectNextSeq", searchVO);
	}
	
	/**
	 * 신규 게시물 그룹번호 채번
	 * @param	MetricCommentVO searchVO
	 * @return	int
	 * @throws	Exception
	 */
	public int selectNextGroupSeq(MetricCommentVO searchVO) throws Exception {
		return selectOne("bsc.mon.common.metricComment.selectNextGroupSeq", searchVO);
	}
	
	/**
	 * 댓글 등록
	 * @param	MetricCommentVO dataVO
	 * @return	int
	 * @throws	Exception
	 */
	public int insertData(MetricCommentVO dataVO) throws Exception {
		return insert("bsc.mon.common.metricComment.insertData", dataVO);
	}

	/**
	 * 댓글 수정
	 * @param	MetricCommentVO dataVO
	 * @return	int
	 * @throws	Exception
	 */
	public int updateData(MetricCommentVO dataVO) throws Exception {
		return update("bsc.mon.common.metricComment.updateData", dataVO);
	}

	/**
	 * 댓글 삭제
	 * @param	MetricCommentVO dataVO
	 * @return	int
	 * @throws	Exception
	 */
	public int deleteData(MetricCommentVO dataVO) throws Exception {
		return delete("bsc.mon.common.metricComment.deleteData", dataVO);
	}
	
	/**
	 * 대댓글용 그룹 순서 최소값 조회
	 * @param	MetricCommentVO searchVO
	 * @return	int
	 * @throws	Exception
	 */
	public Integer selectMinGroupOrder(MetricCommentVO searchVO) throws Exception {
		return selectOne("bsc.mon.common.metricComment.selectMinGroupOrder", searchVO);
	}
	
	/**
	 * 대댓글용 그룹 순서 채번
	 * @param	MetricCommentVO searchVO
	 * @return	int
	 * @throws	Exception
	 */
	public int selectNextGroupOrder(MetricCommentVO searchVO) throws Exception {
		return selectOne("bsc.mon.common.metricComment.selectNextGroupOrder", searchVO);
	}
	
	/**
	 * 대댓글용 그룹 순서 조정
	 * @param	MetricCommentVO dataVO
	 * @return	int
	 * @throws	Exception
	 */
	public int updateGroupOrder(MetricCommentVO dataVO) throws Exception {
		return update("bsc.mon.common.metricComment.updateGroupOrder", dataVO);
	}
}
