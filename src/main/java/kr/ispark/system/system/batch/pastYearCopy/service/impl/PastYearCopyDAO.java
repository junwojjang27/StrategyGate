/*************************************************************************
* CLASS 명	: PastYearCopyDAO
* 작 업 자	: 박정현
* 작 업 일	: 2018-06-29
* 기	능	: 전년데이터일괄적용 DAO
* ---------------------------- 변 경 이 력 --------------------------------
* 번호	작 업 자		작	업	일			변 경 내 용				비고
* ----	---------	----------------	---------------------	-----------
*	1	박정현		2018-06-29
**************************************************************************/
package kr.ispark.system.system.batch.pastYearCopy.service.impl;


import java.util.List;

import org.springframework.stereotype.Repository;

import egovframework.com.cmm.service.impl.EgovComAbstractDAO;
import kr.ispark.system.system.batch.pastYearCopy.service.PastYearCopyVO;

@Repository
public class PastYearCopyDAO extends EgovComAbstractDAO {
	/**
	 * 전년데이터일괄적용 목록 조회
	 * @param	PastYearCopyVO searchVO
	 * @return	List<PastYearCopyVO>
	 * @throws	Exception
	 */
	public List<PastYearCopyVO> selectList(PastYearCopyVO searchVO) throws Exception {
		return selectList("system.batch.pastYearCopy.selectList", searchVO);
	}

	/**
	 * 전년도 데이터 복사 - 시퀀스 테이블
	 * @param	PastYearCopyVO dataVO
	 * @return	int
	 */
	/*
	public int insertComSeq(PastYearCopyVO dataVO) {
		return insert("system.batch.pastYearCopy.insertComSeq", dataVO);
	}
	*/
	
	
	/**
	 * 전년도 데이터 복사 - 시퀀스 테이블
	 * @param	PastYearCopyVO dataVO
	 * @return	int
	 */
	public int insertComSeqData(PastYearCopyVO dataVO) {
		return insert("system.batch.pastYearCopy.insertComSeqData", dataVO);
	}
	
	public int deleteComSeqData(PastYearCopyVO dataVO) {
		return delete("system.batch.pastYearCopy.deleteComSeqData", dataVO);
	}

	/**
	 * 공통코드 삭제
	 */
	public int deleteComCode(PastYearCopyVO searchVO) {
		return delete("system.batch.pastYearCopy.deleteComCode", searchVO);
	}

	/**
	 * 공통코드 등록
	 */
	public int insertComCode(PastYearCopyVO searchVO) {
		return insert("system.batch.pastYearCopy.insertComCode", searchVO);
	}

	/**
	 * 신호등 삭제
	 */
	public int deleteComSignal(PastYearCopyVO searchVO) {
		return delete("system.batch.pastYearCopy.deleteComSignal", searchVO);
	}

	/**
	 * 신호등 등록
	 */
	public int insertComSignal(PastYearCopyVO searchVO) {
		return insert("system.batch.pastYearCopy.insertComSignal", searchVO);
	}

	/**
	 * 평가등급-배분표 삭제
	 */
	public int deleteEvalGradeCnt(PastYearCopyVO searchVO) {
		return delete("system.batch.pastYearCopy.deleteEvalGradeCnt", searchVO);
	}

	/**
	 * 평가등급-배분표 등록
	 */
	public int insertEvalGradeCnt(PastYearCopyVO searchVO) {
		return insert("system.batch.pastYearCopy.insertEvalGradeCnt", searchVO);
	}

	/**
	 * 평가등급-등급 삭제
	 */
	public int deleteEvalGrade(PastYearCopyVO searchVO) {
		return delete("system.batch.pastYearCopy.deleteEvalGrade", searchVO);
	}

	/**
	 * 평가등급-등급 등록
	 */
	public int insertEvalGrade(PastYearCopyVO searchVO) {
		return insert("system.batch.pastYearCopy.insertEvalGrade", searchVO);
	}

	/**
	 * 평가등급 삭제
	 */
	public int deleteEvalMethod(PastYearCopyVO searchVO) {
		return delete("system.batch.pastYearCopy.deleteEvalMethod", searchVO);
	}

	/**
	 * 평가등급 등록
	 */
	public int insertEvalMethod(PastYearCopyVO searchVO) {
		return insert("system.batch.pastYearCopy.insertEvalMethod", searchVO);
	}

	/**
	 * 시스템연계항목 삭제
	 */
	public int deleteBscSystemItem(PastYearCopyVO searchVO) {
		return delete("system.batch.pastYearCopy.deleteBscSystemItem", searchVO);
	}

	/**
	 * 시스템연계항목 등록
	 */
	public int insertBscSystemItem(PastYearCopyVO searchVO) {
		return insert("system.batch.pastYearCopy.insertBscSystemItem", searchVO);
	}


	/**
	 * 관점 삭제
	 */
	public int deleteBscPerspective(PastYearCopyVO searchVO) {
		return delete("system.batch.pastYearCopy.deleteBscPerspective", searchVO);
	}

	/**
	 * 관점 등록
	 */
	public int insertBscPerspective(PastYearCopyVO searchVO) {
		return insert("system.batch.pastYearCopy.insertBscPerspective", searchVO);
	}

	/**
	 * 전략목표체계도 삭제
	 */
	public int deleteBscStrategyMap(PastYearCopyVO searchVO) {
		return delete("system.batch.pastYearCopy.deleteBscStrategyMap", searchVO);
	}

	/**
	 * 전략목표체계도 등록
	 */
	public int insertBscStrategyMap(PastYearCopyVO searchVO) {
		return insert("system.batch.pastYearCopy.insertBscStrategyMap", searchVO);
	}

	/**
	 * 전략목표 삭제
	 */
	public int deleteBscStrategy(PastYearCopyVO searchVO) {
		return delete("system.batch.pastYearCopy.deleteBscStrategy", searchVO);
	}

	/**
	 * 전략목표 등록
	 */
	public int insertBscStrategy(PastYearCopyVO searchVO) {
		return insert("system.batch.pastYearCopy.insertBscStrategy", searchVO);
	}

	/**
	 * 성과조직도 삭제
	 */
	public int deleteBscScDeptMap(PastYearCopyVO searchVO) {
		return delete("system.batch.pastYearCopy.deleteBscScDeptMap", searchVO);
	}

	/**
	 * 성과조직도 등록
	 */
	public int insertBscScDeptMap(PastYearCopyVO searchVO) {
		return insert("system.batch.pastYearCopy.insertBscScDeptMap", searchVO);
	}

	/**
	 * 성과조직매핑 삭제
	 */
	public int deleteBscScDeptMapping(PastYearCopyVO searchVO) {
		return delete("system.batch.pastYearCopy.deleteBscScDeptMapping", searchVO);
	}

	/**
	 * 성과조직매핑 등록
	 */
	public int insertBscScDeptMapping(PastYearCopyVO searchVO) {
		return insert("system.batch.pastYearCopy.insertBscScDeptMapping", searchVO);
	}

	/**
	 * 성과조직 삭제
	 */
	public int deleteBscScDept(PastYearCopyVO searchVO) {
		return delete("system.batch.pastYearCopy.deleteBscScDept", searchVO);
	}

	/**
	 * 성과조직 등록
	 */
	public int insertBscScDept(PastYearCopyVO searchVO) {
		return insert("system.batch.pastYearCopy.insertBscScDept", searchVO);
	}

	/**
	 * 지표 전년도 데이터 복사 전 지표 관련 데이터 삭제
	 * @param	PastYearCopyVO dataVO
	 * @return	int
	 * @throws	Exception
	 */
	/*
	public int deleteBscMetricDatas(PastYearCopyVO dataVO) throws Exception {
		return delete("system.batch.pastYearCopy.deleteBscMetricDatas", dataVO);
	}
	*/
	
	public int deleteBscMetricData1(PastYearCopyVO dataVO) throws Exception {
		return delete("system.batch.pastYearCopy.deleteBscMetricData1", dataVO);
	}
	public int deleteBscMetricData2(PastYearCopyVO dataVO) throws Exception {
		return delete("system.batch.pastYearCopy.deleteBscMetricData2", dataVO);
	}
	public int deleteBscMetricData3(PastYearCopyVO dataVO) throws Exception {
		return delete("system.batch.pastYearCopy.deleteBscMetricData3", dataVO);
	}
	public int deleteBscMetricData4(PastYearCopyVO dataVO) throws Exception {
		return delete("system.batch.pastYearCopy.deleteBscMetricData4", dataVO);
	}
	public int deleteBscMetricData5(PastYearCopyVO dataVO) throws Exception {
		return delete("system.batch.pastYearCopy.deleteBscMetricData5", dataVO);
	}
	public int deleteBscMetricData6(PastYearCopyVO dataVO) throws Exception {
		return delete("system.batch.pastYearCopy.deleteBscMetricData6", dataVO);
	}
	public int deleteBscMetricData7(PastYearCopyVO dataVO) throws Exception {
		return delete("system.batch.pastYearCopy.deleteBscMetricData7", dataVO);
	}
	public int deleteBscMetricData8(PastYearCopyVO dataVO) throws Exception {
		return delete("system.batch.pastYearCopy.deleteBscMetricData8", dataVO);
	}
	public int deleteBscMetricData9(PastYearCopyVO dataVO) throws Exception {
		return delete("system.batch.pastYearCopy.deleteBscMetricData9", dataVO);
	}
	public int deleteBscMetricData10(PastYearCopyVO dataVO) throws Exception {
		return delete("system.batch.pastYearCopy.deleteBscMetricData10", dataVO);
	}
	public int deleteBscMetricData11(PastYearCopyVO dataVO) throws Exception {
		return delete("system.batch.pastYearCopy.deleteBscMetricData11", dataVO);
	}
	public int deleteBscMetricData12(PastYearCopyVO dataVO) throws Exception {
		return delete("system.batch.pastYearCopy.deleteBscMetricData12", dataVO);
	}
	public int deleteBscMetricData13(PastYearCopyVO dataVO) throws Exception {
		return delete("system.batch.pastYearCopy.deleteBscMetricData13", dataVO);
	}
	public int deleteBscMetricData14(PastYearCopyVO dataVO) throws Exception {
		return delete("system.batch.pastYearCopy.deleteBscMetricData14", dataVO);
	}
	public int deleteBscMetricData15(PastYearCopyVO dataVO) throws Exception {
		return delete("system.batch.pastYearCopy.deleteBscMetricData15", dataVO);
	}
	public int deleteBscMetricData16(PastYearCopyVO dataVO) throws Exception {
		return delete("system.batch.pastYearCopy.deleteBscMetricData16", dataVO);
	}
	public int deleteBscMetricData17(PastYearCopyVO dataVO) throws Exception {
		return delete("system.batch.pastYearCopy.deleteBscMetricData17", dataVO);
	}
	public int deleteBscMetricData18(PastYearCopyVO dataVO) throws Exception {
		return delete("system.batch.pastYearCopy.deleteBscMetricData18", dataVO);
	}
	public int deleteBscMetricData19(PastYearCopyVO dataVO) throws Exception {
		return delete("system.batch.pastYearCopy.deleteBscMetricData19", dataVO);
	}
	public int deleteBscMetricData20(PastYearCopyVO dataVO) throws Exception {
		return delete("system.batch.pastYearCopy.deleteBscMetricData20", dataVO);
	}
	public int deleteBscMetricData21(PastYearCopyVO dataVO) throws Exception {
		return delete("system.batch.pastYearCopy.deleteBscMetricData21", dataVO);
	}
	public int deleteBscMetricData22(PastYearCopyVO dataVO) throws Exception {
		return delete("system.batch.pastYearCopy.deleteBscMetricData22", dataVO);
	}

	/**
	 * 지표POOL구간대 삭제
	 */
	public int deleteBscMetricGrpSection(PastYearCopyVO searchVO) {
		return delete("system.batch.pastYearCopy.deleteBscMetricGrpSection", searchVO);
	}

	/**
	 * 지표POOL구간대 등록
	 */
	public int insertBscMetricGrpSection(PastYearCopyVO searchVO) {
		return insert("system.batch.pastYearCopy.insertBscMetricGrpSection", searchVO);
	}

	/**
	 * 지표POOL산식항목 삭제
	 */
	public int deleteBscMetricGrpCol(PastYearCopyVO searchVO) {
		return delete("system.batch.pastYearCopy.deleteBscMetricGrpCol", searchVO);
	}

	/**
	 * 지표POOL산식항목 등록
	 */
	public int insertBscMetricGrpCol(PastYearCopyVO searchVO) {
		return insert("system.batch.pastYearCopy.insertBscMetricGrpCol", searchVO);
	}

	/**
	 * 지표POOL실적월 삭제
	 */
	public int deleteBscMetricGrpMon(PastYearCopyVO searchVO) {
		return delete("system.batch.pastYearCopy.deleteBscMetricGrpMon", searchVO);
	}

	/**
	 * 지표POOL실적월 등록
	 */
	public int insertBscMetricGrpMon(PastYearCopyVO searchVO) {
		return insert("system.batch.pastYearCopy.insertBscMetricGrpMon", searchVO);
	}

	/**
	 * 지표POOL 삭제
	 */
	public int deleteBscMetricGrp(PastYearCopyVO searchVO) {
		return delete("system.batch.pastYearCopy.deleteBscMetricGrp", searchVO);
	}

	/**
	 * 지표POOL 등록
	 */
	public int insertBscMetricGrp(PastYearCopyVO searchVO) {
		return insert("system.batch.pastYearCopy.insertBscMetricGrp", searchVO);
	}

	/**
	 * 지표구간대 삭제
	 */
	public int deleteBscMetricSection(PastYearCopyVO searchVO) {
		return delete("system.batch.pastYearCopy.deleteBscMetricSection", searchVO);
	}

	/**
	 * 지표구간대 등록
	 */
	public int insertBscMetricSection(PastYearCopyVO searchVO) {
		return insert("system.batch.pastYearCopy.insertBscMetricSection", searchVO);
	}

	/**
	 * 지표산식항목 삭제
	 */
	public int deleteBscMetricCol(PastYearCopyVO searchVO) {
		return delete("system.batch.pastYearCopy.deleteBscMetricCol", searchVO);
	}

	/**
	 * 지표산식항목 등록
	 */
	public int insertBscMetricCol(PastYearCopyVO searchVO) {
		return insert("system.batch.pastYearCopy.insertBscMetricCol", searchVO);
	}

	/**
	 * 지표실적월 삭제
	 */
	public int deleteBscMetricMon(PastYearCopyVO searchVO) {
		return delete("system.batch.pastYearCopy.deleteBscMetricMon", searchVO);
	}

	/**
	 * 지표실적월 등록
	 */
	public int insertBscMetricMon(PastYearCopyVO searchVO) {
		return insert("system.batch.pastYearCopy.insertBscMetricMon", searchVO);
	}

	/**
	 * 지표 삭제
	 */
	public int deleteBscMetric(PastYearCopyVO searchVO) {
		return delete("system.batch.pastYearCopy.deleteBscMetric", searchVO);
	}

	/**
	 * 지표 등록
	 */
	public int insertBscMetric(PastYearCopyVO searchVO) {
		return insert("system.batch.pastYearCopy.insertBscMetric", searchVO);
	}

	/**
	 * 경영평가범주 삭제
	 */
	public int deleteGovEvalCatGrp(PastYearCopyVO searchVO) {
		return delete("system.batch.pastYearCopy.deleteGovEvalCatGrp", searchVO);
	}

	/**
	 * 경영평가범주 등록
	 */
	public int insertGovEvalCatGrp(PastYearCopyVO searchVO) {
		return insert("system.batch.pastYearCopy.insertGovEvalCatGrp", searchVO);
	}

	/**
	 * 경영평가부문 삭제
	 */
	public int deleteGovEvalCat(PastYearCopyVO searchVO) {
		return delete("system.batch.pastYearCopy.deleteGovEvalCat", searchVO);
	}

	/**
	 * 경영평가부문 등록
	 */
	public int insertGovEvalCat(PastYearCopyVO searchVO) {
		return insert("system.batch.pastYearCopy.insertGovEvalCat", searchVO);
	}

	/**
	 * 경영평가지표착안사항 삭제
	 */
	public int deleteGovMetricEvalItem(PastYearCopyVO searchVO) {
		return delete("system.batch.pastYearCopy.deleteGovMetricEvalItem", searchVO);
	}

	/**
	 * 경영평가지표착안사항 등록
	 */
	public int insertGovMetricEvalItem(PastYearCopyVO searchVO) {
		return insert("system.batch.pastYearCopy.insertGovMetricEvalItem", searchVO);
	}

	/**
	 * 경영평가지표산식항목 삭제
	 */
	public int deleteGovCalTypeCol(PastYearCopyVO searchVO) {
		return delete("system.batch.pastYearCopy.deleteGovCalTypeCol", searchVO);
	}

	/**
	 * 경영평가지표산식항목 등록
	 */
	public int insertGovCalTypeCol(PastYearCopyVO searchVO) {
		return insert("system.batch.pastYearCopy.insertGovCalTypeCol", searchVO);
	}

	/**
	 * 경영평가지표 삭제
	 */
	public int deleteGovMetric(PastYearCopyVO searchVO) {
		return delete("system.batch.pastYearCopy.deleteGovMetric", searchVO);
	}

	/**
	 * 경영평가지표 등록
	 */
	public int insertGovMetric(PastYearCopyVO searchVO) {
		return insert("system.batch.pastYearCopy.insertGovMetric", searchVO);
	}

	/**
	 * 전년도 시스템 데이터 복사
	 * @param	PastYearCopyVO dataVO
	 * @return	int
	 * @throws	Exception
	 */
	public int copySystemDataFromLastYear(PastYearCopyVO dataVO) throws Exception {
		int resultCnt = 0;
		
		//insert("system.batch.pastYearCopy.copySystemDataFromLastYear", dataVO);
		
		resultCnt = delete("system.batch.pastYearCopy.deleteScDeptFromLastYear", dataVO);
		resultCnt = delete("system.batch.pastYearCopy.deleteScDeptMapFromLastYear", dataVO);
		resultCnt = delete("system.batch.pastYearCopy.deleteDeptFromLastYear", dataVO);
		resultCnt = delete("system.batch.pastYearCopy.insertScDeptFromLastYear", dataVO);
		resultCnt = delete("system.batch.pastYearCopy.insertScDeptMapFromLastYear", dataVO);
		resultCnt = delete("system.batch.pastYearCopy.insertDeptFromLastYear", dataVO);
		resultCnt = delete("system.batch.pastYearCopy.deleteCodeFromLastYear", dataVO);
		resultCnt = delete("system.batch.pastYearCopy.deleteCodeNmFromLastYear", dataVO);
		resultCnt = delete("system.batch.pastYearCopy.insertCodeFromLastYear", dataVO);
		resultCnt = delete("system.batch.pastYearCopy.insertCodeNmFromLastYear", dataVO);
		resultCnt = delete("system.batch.pastYearCopy.deleteCodeYearFromLastYear", dataVO);
		resultCnt = delete("system.batch.pastYearCopy.deleteCodeNmYearFromLastYear", dataVO);
		resultCnt = delete("system.batch.pastYearCopy.insertCodeYearFromLastYear", dataVO);
		resultCnt = delete("system.batch.pastYearCopy.insertCodeNmYearFromLastYear", dataVO);
		resultCnt = delete("system.batch.pastYearCopy.deleteSignalFromLastYear", dataVO);
		resultCnt = delete("system.batch.pastYearCopy.insertSignalFromLastYear", dataVO);
		resultCnt = delete("system.batch.pastYearCopy.deleteGradeCntFromLastYear", dataVO);
		resultCnt = delete("system.batch.pastYearCopy.insertGradeCntFromLastYear", dataVO);
		resultCnt = delete("system.batch.pastYearCopy.deleteGradeFromLastYear", dataVO);
		resultCnt = delete("system.batch.pastYearCopy.insertGradeFromLastYear", dataVO);
		resultCnt = delete("system.batch.pastYearCopy.deleteMethodFromLastYear", dataVO);
		resultCnt = delete("system.batch.pastYearCopy.insertMethodFromLastYear", dataVO);
		resultCnt = delete("system.batch.pastYearCopy.deleteSystemItemFromLastYear", dataVO);
		resultCnt = delete("system.batch.pastYearCopy.insertSystemItemFromLastYear", dataVO);
		
		return resultCnt;
	}
}

