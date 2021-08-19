package kr.ispark.common.calculation.service.cal;

import java.sql.SQLException;
import java.util.List;

import org.springframework.stereotype.Repository;

import egovframework.com.cmm.service.impl.EgovComAbstractDAO;
import kr.ispark.common.calculation.service.CalculationVO;

@Repository
public class CalculationDAO extends EgovComAbstractDAO {

	/*log 등록*/
	public void insertCalLogData(CalculationVO cVO){
		insert("calculation.insertCalLogData", cVO);
	}

	/*rollup 삭제*/
	public int deleteRollupData(CalculationVO cVO) throws SQLException {
		return update("calculation.deleteRollupData", cVO);
	}

	/*rollup insert*/
	public int insertRollupData(CalculationVO cVO) throws SQLException {
		return insert("calculation.insertRollupData", cVO);
	}

	/*실적산식 select*/
	public List<CalculationVO> selectCalTypeList(CalculationVO cVO) throws SQLException {
		return selectList("calculation.selectCalTypeList", cVO);
	}

	/*실적계산 update*/
	public int updateActualData(CalculationVO cVO) throws SQLException {
		return update("calculation.updateActualData", cVO);
	}

	/*계산결과 에러 확인 select*/
	public String selectChkData(String cal) throws SQLException {
		return selectOne("calculation.selectChkData", cal);
	}

	/*득점산식 select*/
	public List<CalculationVO> selectCalTypeScoreList(CalculationVO cVO) throws SQLException {
		return selectList("calculation.selectCalTypeScoreList", cVO);
	}

	/*점수계산 update*/
	public int updateScoreData(CalculationVO cVO) throws SQLException {
		return update("calculation.updateScoreData", cVO);
	}

	/*하향등급구간대 점수 update*/
	public int updateDownwardIntervalScoreData(CalculationVO cVO) throws SQLException {
		return update("calculation.updateDownwardIntervalScoreData", cVO);
	}

	/*상향등급구간대 점수 update*/
	public int updateUpwardIntervalActualData(CalculationVO cVO) throws SQLException {
		return update("calculation.updateUpwardIntervalActualData", cVO);
	}

	/*하향등급구간대 점수 update*/
	public int updateDownwardIntervalActualData(CalculationVO cVO) throws SQLException {
		return update("calculation.updateDownwardIntervalActualData", cVO);
	}

	/*상향등급구간대 점수 update*/
	public int updateUpwardIntervalScoreData(CalculationVO cVO) throws SQLException {
		return update("calculation.updateUpwardIntervalScoreData", cVO);
	}

	/*최종점수 update*/
	public int updateFinalScoreData(CalculationVO cVO) throws SQLException {
		return update("calculation.updateFinalScoreData", cVO);
	}

	/*신호등 update*/
	public int updateSignalData(CalculationVO cVO) throws SQLException {
		return update("calculation.updateSignalData", cVO);
	}

	/*공유지표 실적 삭제*/
	public int deleteShareActualData(CalculationVO cVO) throws SQLException {
		return delete("calculation.deleteShareActualData", cVO);
	}

	/*공유지표 실적 INSERT*/
	public int insertShareActualData(CalculationVO cVO) throws SQLException {
		return insert("calculation.insertShareActualData", cVO);
	}

	/*지표집계테이블 삭제 DELETE*/
	public int deleteMetricscoreData(CalculationVO cVO) throws SQLException {
		return delete("calculation.deleteMetricscoreData", cVO);
	}

	/*부서집계테이블 삭제 DELETE*/
	public int deleteDeptscoreData(CalculationVO cVO) throws SQLException {
		return delete("calculation.deleteDeptscoreData", cVO);
	}

	/*전략집계테이블 삭제 DELETE*/
	public int deleteStrategyscoreData(CalculationVO cVO) throws SQLException {
		return delete("calculation.deleteStrategyscoreData", cVO);
	}

	/*관점집계테이블 삭제 DELETE*/
	public int deletePerspectivescoreData(CalculationVO cVO) throws SQLException {
		return delete("calculation.deletePerspectivescoreData", cVO);
	}

	/*지표집계테이블 insert*/
	public int insertMetricscoreData(CalculationVO cVO) throws SQLException {
		return insert("calculation.insertMetricscoreData", cVO);
	}

	/*부서집계테이블 insert*/
	public int insertDeptscoreData(CalculationVO cVO) throws SQLException {
		return insert("calculation.insertDeptscoreData", cVO);
	}

	/*전략집계테이블 insert*/
	public int insertStrategyscoreData(CalculationVO cVO) throws SQLException {
		return insert("calculation.insertStrategyscoreData", cVO);
	}

	/*관점집계테이블 insert*/
	public int insertPerspectivescoreData(CalculationVO cVO) throws SQLException {
		return insert("calculation.insertPerspectivescoreData", cVO);
	}

	/*조직 집계테이블 상태값 update*/
	public int updateDeptscoreStatusData(CalculationVO cVO) throws SQLException {
		return update("calculation.updateDeptscoreStatusData", cVO);
	}

	/*전략목표 집계테이블 상태값 update*/
	public int updateStrategyscoreStatusData(CalculationVO cVO) throws SQLException {
		return update("calculation.updateStrategyscoreStatusData", cVO);
	}

	/*관점집계테이블 상태값 update*/
	public int updatePerspectivescoreStatusData(CalculationVO cVO) throws SQLException {
		return update("calculation.updatePerspectivescoreStatusData", cVO);
	}

	/*집계테이블 로그 insert*/
	public int insertProcAdminLog(CalculationVO cVO) throws SQLException {
		return insert("calculation.insertProcAdminLog", cVO);
	}

	/*연계지표 조회 select*/
	public List<CalculationVO> selectCalMetricData(CalculationVO cVO) throws SQLException {
		return selectList("calculation.selectCalMetricData", cVO);
	}

	/*연계지표 실적 insert*/
	public int insertCalActualData(CalculationVO cVO) throws SQLException {
		return insert("calculation.insertCalActualData", cVO);
	}

	/*연계지표 실적 update*/
	public int updateCalActualData(CalculationVO cVO) throws SQLException {
		return insert("calculation.updateCalActualData", cVO);
	}

	/*집계테이블 로그 insert*/
	public String selectColActAllYn(CalculationVO cVO) throws SQLException {
		return selectOne("calculation.selectColActAllYn", cVO);
	}
	
	/*집계테이블 로그 insert*/
	public String selectNextSeq(CalculationVO cVO) throws SQLException {
		return selectOne("calculation.selectNextSeq", cVO);
	}
	
	/*집계테이블 로그 insert*/
	public String selectFinalNextSeq(CalculationVO cVO) throws SQLException {
		return selectOne("calculation.selectFinalNextSeq", cVO);
	}
}
