/*************************************************************************
* CLASS 명	: YearBatchDAO
* 작 업 자	: 현걸욱
* 작 업 일	: 2018-04-23
* 기	능	: 년배치 DAO
* ---------------------------- 변 경 이 력 --------------------------------
* 번호	작 업 자		작	업	일			변 경 내 용				비고
* ----	---------	----------------	---------------------	-----------
*	1	현걸욱		2018-04-23
**************************************************************************/
package kr.ispark.system.system.batch.yearBatch.service.impl;


import java.util.List;

import org.springframework.stereotype.Repository;

import egovframework.com.cmm.service.impl.EgovComAbstractDAO;
import kr.ispark.system.system.batch.yearBatch.service.YearBatchVO;

@Repository
public class YearBatchDAO extends EgovComAbstractDAO {
	/**
	 * 년배치 목록 조회
	 * @param	YearBatchVO searchVO
	 * @return	List<YearBatchVO>
	 * @throws	Exception
	 */
	public List<YearBatchVO> selectList(YearBatchVO searchVO) throws Exception {
		return selectList("system.batch.yearBatch.selectList", searchVO);
	}
	
	/**
	 * 년배치 상세 조회
	 * @param	YearBatchVO searchVO
	 * @return	YearBatchVO
	 * @throws	Exception
	 */
	public YearBatchVO selectDetail(YearBatchVO searchVO) throws Exception {
		return (YearBatchVO)selectOne("system.batch.yearBatch.selectDetail", searchVO);
	}
	
	/**
	 * 년배치 정렬순서저장
	 * @param	YearBatchVO searchVO
	 * @return	int
	 * @throws	Exception
	 */
	public int updateSortOrder(YearBatchVO searchVO) throws Exception {
		return update("system.batch.yearBatch.updateSortOrder", searchVO);
	}

	/**
	 * 년배치 삭제
	 * @param	YearBatchVO searchVO
	 * @return	int
	 * @throws	Exception
	 */
	public int deleteYearBatch(YearBatchVO searchVO) throws Exception {
		return update("system.batch.yearBatch.deleteYearBatch", searchVO);
	}
	
	/**
	 * 년배치 저장
	 * @param	YearBatchVO searchVO
	 * @return	int
	 * @throws	Exception
	 */
	public int insertData(YearBatchVO searchVO) throws Exception {
		return update("system.batch.yearBatch.insertData", searchVO);
	}

	/**
	 * 년배치 수정
	 * @param	YearBatchVO searchVO
	 * @return	int
	 * @throws	Exception
	 */
	public int updateData(YearBatchVO searchVO) throws Exception {
		return update("system.batch.yearBatch.updateData", searchVO);
	}
}

