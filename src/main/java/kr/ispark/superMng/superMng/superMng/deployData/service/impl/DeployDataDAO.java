/*************************************************************************
* CLASS 명	: DeployDataDAO
* 작 업 자	: 현걸욱
* 작 업 일	: 2018-07-09
* 기	능	: 고객사별 전년데이터 일괄적용 DAO
* ---------------------------- 변 경 이 력 --------------------------------
* 번호	작 업 자		작	업	일			변 경 내 용				비고
* ----	---------	----------------	---------------------	-----------
*	1	현걸욱		2018-07-09
**************************************************************************/
package kr.ispark.superMng.superMng.superMng.deployData.service.impl;


import java.util.List;

import org.springframework.stereotype.Repository;

import egovframework.com.cmm.service.impl.EgovComAbstractDAO;
import kr.ispark.superMng.superMng.superMng.deployData.service.DeployDataVO;

@Repository
public class DeployDataDAO extends EgovComAbstractDAO {
	/**
	 * 고객사별 전년데이터 일괄적용 목록 조회
	 * @param	DeployDataVO searchVO
	 * @return	List<DeployDataVO>
	 * @throws	Exception
	 */
	public List<DeployDataVO> selectList(DeployDataVO searchVO) throws Exception {
		return selectList("superMng.superMng.deployData.selectList", searchVO);
	}
	
	/**
	 * 공통코드 삭제
	 */
	public int deleteComCode(DeployDataVO searchVO) {
		return delete("superMng.superMng.deployData.deleteComCode", searchVO);
	}

	/**
	 * 공통코드 등록
	 */
	public int insertComCode(DeployDataVO searchVO) {
		return insert("superMng.superMng.deployData.insertComCode", searchVO);
	}
	
	/**
	 * 공통코드 삭제
	 */
	public int deleteComCodeNm(DeployDataVO searchVO) {
		return delete("superMng.superMng.deployData.deleteComCodeNm", searchVO);
	}

	/**
	 * 공통코드 등록
	 */
	public int insertComCodeNm(DeployDataVO searchVO) {
		return insert("superMng.superMng.deployData.insertComCodeNm", searchVO);
	}

	/**
	 * 신호등 삭제
	 */
	public int deleteComSignal(DeployDataVO searchVO) {
		return delete("superMng.superMng.deployData.deleteComSignal", searchVO);
	}

	/**
	 * 신호등 등록
	 */
	public int insertComSignal(DeployDataVO searchVO) {
		return insert("superMng.superMng.deployData.insertComSignal", searchVO);
	}
	
	/**
	 * 평가등급-배분표 삭제
	 */
	public int deleteEvalGradeCnt(DeployDataVO searchVO) {
		return delete("superMng.superMng.deployData.deleteEvalGradeCnt", searchVO);
	}

	/**
	 * 평가등급-배분표 등록
	 */
	public int insertEvalGradeCnt(DeployDataVO searchVO) {
		return insert("superMng.superMng.deployData.insertEvalGradeCnt", searchVO);
	}

	/**
	 * 평가등급-등급 삭제
	 */
	public int deleteEvalGrade(DeployDataVO searchVO) {
		return delete("superMng.superMng.deployData.deleteEvalGrade", searchVO);
	}

	/**
	 * 평가등급-등급 등록
	 */
	public int insertEvalGrade(DeployDataVO searchVO) {
		return insert("superMng.superMng.deployData.insertEvalGrade", searchVO);
	}

	/**
	 * 평가등급 삭제
	 */
	public int deleteEvalMethod(DeployDataVO searchVO) {
		return delete("superMng.superMng.deployData.deleteEvalMethod", searchVO);
	}

	/**
	 * 평가등급 등록
	 */
	public int insertEvalMethod(DeployDataVO searchVO) {
		return insert("superMng.superMng.deployData.insertEvalMethod", searchVO);
	}

	/**
	 * 시스템연계항목 삭제
	 */
	public int deleteBscSystemItem(DeployDataVO searchVO) {
		return delete("superMng.superMng.deployData.deleteBscSystemItem", searchVO);
	}

	/**
	 * 시스템연계항목 등록
	 */
	public int insertBscSystemItem(DeployDataVO searchVO) {
		return insert("superMng.superMng.deployData.insertBscSystemItem", searchVO);
	}
	

	/**
	 * 관점 삭제
	 */
	public int deleteBscPerspective(DeployDataVO searchVO) {
		return delete("superMng.superMng.deployData.deleteBscPerspective", searchVO);
	}

	/**
	 * 관점 등록
	 */
	public int insertBscPerspective(DeployDataVO searchVO) {
		return insert("superMng.superMng.deployData.insertBscPerspective", searchVO);
	}

	/**
	 * 전략목표체계도 삭제
	 */
	public int deleteBscStrategyMap(DeployDataVO searchVO) {
		return delete("superMng.superMng.deployData.deleteBscStrategyMap", searchVO);
	}

	/**
	 * 전략목표체계도 등록
	 */
	public int insertBscStrategyMap(DeployDataVO searchVO) {
		return insert("superMng.superMng.deployData.insertBscStrategyMap", searchVO);
	}

	/**
	 * 전략목표 삭제
	 */
	public int deleteBscStrategy(DeployDataVO searchVO) {
		return delete("superMng.superMng.deployData.deleteBscStrategy", searchVO);
	}

	/**
	 * 전략목표 등록
	 */
	public int insertBscStrategy(DeployDataVO searchVO) {
		return insert("superMng.superMng.deployData.insertBscStrategy", searchVO);
	}

	/**
	 * 성과조직도 삭제
	 */
	public int deleteBscScDeptMap(DeployDataVO searchVO) {
		return delete("superMng.superMng.deployData.deleteBscScDeptMap", searchVO);
	}

	/**
	 * 성과조직도 등록
	 */
	public int insertBscScDeptMap(DeployDataVO searchVO) {
		return insert("superMng.superMng.deployData.insertBscScDeptMap", searchVO);
	}
	
	/**
	 * 성과조직매핑 삭제
	 */
	public int deleteBscScDeptMapping(DeployDataVO searchVO) {
		return delete("superMng.superMng.deployData.deleteBscScDeptMapping", searchVO);
	}
	
	/**
	 * 성과조직매핑 등록
	 */
	public int insertBscScDeptMapping(DeployDataVO searchVO) {
		return insert("superMng.superMng.deployData.insertBscScDeptMapping", searchVO);
	}
	
	/**
	 * 성과조직 삭제
	 */
	public int deleteBscScDept(DeployDataVO searchVO) {
		return delete("superMng.superMng.deployData.deleteBscScDept", searchVO);
	}
	
	/**
	 * 성과조직 등록
	 */
	public int insertBscScDept(DeployDataVO searchVO) {
		return insert("superMng.superMng.deployData.insertBscScDept", searchVO);
	}
	
	/**
	 * 성과조직 삭제
	 */
	public int deleteDept(DeployDataVO searchVO) {
		return delete("superMng.superMng.deployData.deleteDept", searchVO);
	}
	
	/**
	 * 성과조직 등록
	 */
	public int insertDept(DeployDataVO searchVO) {
		return insert("superMng.superMng.deployData.insertDept", searchVO);
	}
	
	/**
	 * 지표POOL구간대 삭제
	 */
	public int deleteBscMetricGrpSection(DeployDataVO searchVO) {
		return delete("superMng.superMng.deployData.deleteBscMetricGrpSection", searchVO);
	}
	
	/**
	 * 지표POOL구간대 등록
	 */
	public int insertBscMetricGrpSection(DeployDataVO searchVO) {
		return insert("superMng.superMng.deployData.insertBscMetricGrpSection", searchVO);
	}
	
	/**
	 * 지표POOL산식항목 삭제
	 */
	public int deleteBscMetricGrpCol(DeployDataVO searchVO) {
		return delete("superMng.superMng.deployData.deleteBscMetricGrpCol", searchVO);
	}
	
	/**
	 * 지표POOL산식항목 등록
	 */
	public int insertBscMetricGrpCol(DeployDataVO searchVO) {
		return insert("superMng.superMng.deployData.insertBscMetricGrpCol", searchVO);
	}
	
	/**
	 * 지표POOL실적월 삭제
	 */
	public int deleteBscMetricGrpMon(DeployDataVO searchVO) {
		return delete("superMng.superMng.deployData.deleteBscMetricGrpMon", searchVO);
	}
	
	/**
	 * 지표POOL실적월 등록
	 */
	public int insertBscMetricGrpMon(DeployDataVO searchVO) {
		return insert("superMng.superMng.deployData.insertBscMetricGrpMon", searchVO);
	}
	
	/**
	 * 지표POOL 삭제
	 */
	public int deleteBscMetricGrp(DeployDataVO searchVO) {
		return delete("superMng.superMng.deployData.deleteBscMetricGrp", searchVO);
	}
	
	/**
	 * 지표POOL 등록
	 */
	public int insertBscMetricGrp(DeployDataVO searchVO) {
		return insert("superMng.superMng.deployData.insertBscMetricGrp", searchVO);
	}
	
	/**
	 * 지표구간대 삭제
	 */
	public int deleteBscMetricSection(DeployDataVO searchVO) {
		return delete("superMng.superMng.deployData.deleteBscMetricSection", searchVO);
	}
	
	/**
	 * 지표구간대 등록
	 */
	public int insertBscMetricSection(DeployDataVO searchVO) {
		return insert("superMng.superMng.deployData.insertBscMetricSection", searchVO);
	}
	
	/**
	 * 지표산식항목 삭제
	 */
	public int deleteBscMetricCol(DeployDataVO searchVO) {
		return delete("superMng.superMng.deployData.deleteBscMetricCol", searchVO);
	}
	
	/**
	 * 지표산식항목 등록
	 */
	public int insertBscMetricCol(DeployDataVO searchVO) {
		return insert("superMng.superMng.deployData.insertBscMetricCol", searchVO);
	}
	
	/**
	 * 지표실적월 삭제
	 */
	public int deleteBscMetricMon(DeployDataVO searchVO) {
		return delete("superMng.superMng.deployData.deleteBscMetricMon", searchVO);
	}
	
	/**
	 * 지표실적월 등록
	 */
	public int insertBscMetricMon(DeployDataVO searchVO) {
		return insert("superMng.superMng.deployData.insertBscMetricMon", searchVO);
	}
	
	/**
	 * 지표 삭제
	 */
	public int deleteBscMetric(DeployDataVO searchVO) {
		return delete("superMng.superMng.deployData.deleteBscMetric", searchVO);
	}
	
	/**
	 * 지표 등록
	 */
	public int insertBscMetric(DeployDataVO searchVO) {
		return insert("superMng.superMng.deployData.insertBscMetric", searchVO);
	}

	/**
	 * 경영평가범주 삭제
	 */
	public int deleteGovEvalCatGrp(DeployDataVO searchVO) {
		return delete("superMng.superMng.deployData.deleteGovEvalCatGrp", searchVO);
	}
	
	/**
	 * 경영평가범주 등록
	 */
	public int insertGovEvalCatGrp(DeployDataVO searchVO) {
		return insert("superMng.superMng.deployData.insertGovEvalCatGrp", searchVO);
	}
	
	/**
	 * 경영평가부문 삭제
	 */
	public int deleteGovEvalCat(DeployDataVO searchVO) {
		return delete("superMng.superMng.deployData.deleteGovEvalCat", searchVO);
	}
	
	/**
	 * 경영평가부문 등록
	 */
	public int insertGovEvalCat(DeployDataVO searchVO) {
		return insert("superMng.superMng.deployData.insertGovEvalCat", searchVO);
	}
	
	/**
	 * 경영평가지표착안사항 삭제
	 */
	public int deleteGovMetricEvalItem(DeployDataVO searchVO) {
		return delete("superMng.superMng.deployData.deleteGovMetricEvalItem", searchVO);
	}
	
	/**
	 * 경영평가지표착안사항 등록
	 */
	public int insertGovMetricEvalItem(DeployDataVO searchVO) {
		return insert("superMng.superMng.deployData.insertGovMetricEvalItem", searchVO);
	}

	/**
	 * 경영평가지표산식항목 삭제
	 */
	public int deleteGovCalTypeCol(DeployDataVO searchVO) {
		return delete("superMng.superMng.deployData.deleteGovCalTypeCol", searchVO);
	}
	
	/**
	 * 경영평가지표산식항목 등록
	 */
	public int insertGovCalTypeCol(DeployDataVO searchVO) {
		return insert("superMng.superMng.deployData.insertGovCalTypeCol", searchVO);
	}
	
	/**
	 * 경영평가지표 삭제
	 */
	public int deleteGovMetric(DeployDataVO searchVO) {
		return delete("superMng.superMng.deployData.deleteGovMetric", searchVO);
	}
	
	/**
	 * 경영평가지표 등록
	 */
	public int insertGovMetric(DeployDataVO searchVO) {
		return insert("superMng.superMng.deployData.insertGovMetric", searchVO);
	}
}

