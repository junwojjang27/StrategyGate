/*************************************************************************
* CLASS 명	: calculationServiceImpl
* 작 업 자	: Joey Hyun
* 작 업 일	: 2018. 03. 30.
* 기	능	: 집계 Service
* ---------------------------- 변 경 이 력 --------------------------------
* 번호	작 업 자		작	업	일			변 경 내 용				비고
* ----	---------	----------------	---------------------	-----------
*	1	Joey Hyun		2018. 03. 30.		최 초 작 업
**************************************************************************/

package kr.ispark.common.calculation.service.cal;

import java.sql.SQLDataException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.ispark.common.calculation.service.CalculationVO;
import kr.ispark.common.security.service.UserVO;
import kr.ispark.common.system.service.impl.CommonServiceImpl;
import kr.ispark.common.system.service.impl.IdGenServiceImpl;
import kr.ispark.common.util.CommonUtil;
import kr.ispark.common.util.SessionUtil;

@Service("CalculationServiceImpl")
public class CalculationServiceImpl {

	@Autowired
	private CommonServiceImpl commonService;

	@Resource
	CalculationDAO cDAO;

	@Resource
	private IdGenServiceImpl idgenService;

	protected CalculationVO cVO;

	/*로그 변수*/
	protected int resultCnt;
	protected String successYn;
	protected String successFinalYn = "Y";
	protected String exceptionText;
	protected String procId;
	protected String message = "";
	protected String seq;
	protected String finalSeq;

	/*집계변수*/
	protected int startMon;
	protected int endMon;

	/*연계지표 담을 list*/
	protected List<CalculationVO> calList;

	/*param 설정*/
	public void setParam(String year, String mon, String metricId, String approveYn) throws Exception{
		
		UserVO uVO = (UserVO)SessionUtil.getAttribute("loginVO");
		cVO = new CalculationVO();
		cVO.setYear(year);
		cVO.setMon(mon);
		cVO.setMetricId(metricId);
		String kpiApproveUseYn = commonService.selectMetricApproveUseYn(cVO);
		String actApproveUseYn = commonService.selectActApproveUseYn(cVO);
		cVO.setKpiApproveUseYn(kpiApproveUseYn);
		cVO.setActApproveUseYn(actApproveUseYn);
		cVO.setExecUserId(uVO!=null?uVO.getUserId():null);
		cVO.setApproveYn(approveYn);
		
	}

	/*집계 param 설정*/
	public void setAllParam(String year, String mon) throws Exception{
		setParam(year, mon, "", "");
	}

	/*parameter 초기화*/
	public void resetParam(){
		cVO = null;
	}

	/*로그 파라미터 초기화*/
	public void resetProcessParam(){
		resultCnt = 0;
		successYn = "Y";
		exceptionText = "";
		procId = "";
	}

	/*실적저장시 실행되는 method*/
	//@Async("asyncExecuter")
	public  void execActual(){
		if(!"".equals(CommonUtil.nullToBlank(cVO.getMetricId()))){
			deleteRollUp();
			insertRollup();
			updateActual();
			updateScore();
			updateFinalScore();
			insertShareData();

			insertCalMetricActual();

			resetParam();
		}
	}

	/*집계시 실행되는 method*/
	
	/*
	@Async("asyncExecuter")
	public Future<String> execAll() throws SQLException{
		
		int errCnt = 0;

		startMon = 1;
		endMon = Integer.parseInt(cVO.getMon());

		while(startMon <= endMon){

			if(startMon < 10){
				cVO.setMon("0"+startMon);
			}else{
				cVO.setMon(""+startMon);
			}

			deleteRollUp();
			insertRollup();
			updateActual();
			updateScore();
			updateFinalScore();
			insertShareData();
			if(!insertSummarry()) errCnt++;

			startMon ++;
		}

		insertProcAdminLog();
		resetParam();
		
		String result = "";
		if(errCnt > 0){
			result = "fail";
		}else{
			result = "success";
		}

		return new AsyncResult<String>(result);
	}
	*/
	
	
	public  String execAll() throws SQLException{
		int errCnt = 0;

		startMon = 1;
		endMon = Integer.parseInt(cVO.getMon());

		while(startMon <= endMon){

			if(startMon < 10){
				cVO.setMon("0"+startMon);
			}else{
				cVO.setMon(""+startMon);
			}
			
			deleteRollUp();
			insertRollup();
			updateActual();
			updateScore();
			updateFinalScore();
			insertShareData();
			if(!insertSummarry()) errCnt++;

			startMon ++;
		}

		insertProcAdminLog();
		resetParam();
		
		String result = "";
		if(errCnt > 0){
			result = "fail";
		}else{
			result = "success";
		}

		return result;
	}

	/* 로그 저장하는 method
	 * resultCnt : 실행결과값 수 - 의미가 없어서 사용하지않을 예정
	 * successYn : 정상완료 여부
	 * exceptionText : 에러 로그 (간략한 내용만)
	 * interfaceId : 각 단계별 프로세스 명
	 * */
	private void insertLog(int resultCnt, String successYn, String exceptionText, String interfaceId){
		cVO.setResultCnt(resultCnt);
		cVO.setExceptionText(exceptionText);
		cVO.setSuccessYn(successYn);
		cVO.setProcId(procId);

		try {
			seq = cDAO.selectNextSeq(cVO);
		} catch (SQLException sqe) {
			sqe.getCause();
		} catch (Exception e) {
			e.getCause();
		}
		cVO.setSeq(seq);

		cDAO.insertCalLogData(cVO);

		if("N".equals(successYn)){
			cVO.setSuccessFinalYn("N");
		}

		resetProcessParam();
	}


	/*집계 logic
	 * 1. rollup 삭제
	 * 2.항목별 롤업(분석주기'Y' 데이터 생성)
	 * 3.실적계산
	 * 4.점수 및 최종점수 상태값 계산
	 * 5.공유지표 목표/실적/점수 복사
	 * 6.집계테이블 생성 (관점,전략,부서)
	 */


	//rollup 삭제
	private void deleteRollUp(){

		procId = "DELETE TIME_ROLLUP DATA";

		//2.actual테이블의 "Y" 데이터 삭제
		try{
			cDAO.deleteRollupData(cVO);
		} catch (SQLException sqe) {
			successYn = "N";
			exceptionText = sqe.getCause().toString();
		}catch(Exception e){
			successYn = "N";
			exceptionText = e.getCause().toString();
		}finally{
			insertLog(resultCnt,successYn,exceptionText,procId);
		}
	}

	//산식항목rollup --입력구분이 시스템이건 excel이건, 모두  rollup한다.
	private void insertRollup(){

		procId = "INSERT TIME_ROLLUP DATA";

		//2.actual테이블의 "Y" 데이터 삭제
		try{
			cDAO.insertRollupData(cVO);
		} catch (SQLException sqe) {
			successYn = "N";
			exceptionText = sqe.getCause().toString();	
		}catch(Exception e){
			successYn = "N";
			exceptionText = e.getCause().toString();
		}finally{
			insertLog(resultCnt,successYn,exceptionText,procId);
		}
	}

	//실적계산
	private void updateActual(){

		procId = "UPDATE ACTUAL DATA";

		try{
			String calTypeActual = "";
			List<CalculationVO> calTypeList = cDAO.selectCalTypeList(cVO);
			if(calTypeList != null && calTypeList.size() > 0){
				for(CalculationVO calVO : calTypeList){

					calTypeActual = calVO.getCalType();

					if(!"".equals(CommonUtil.nullToBlank(calTypeActual))){
						if(calTypeActual.contains("A")){calTypeActual = calTypeActual.replace("A", CommonUtil.removeNull(calVO.getA(),"0"));}
						if(calTypeActual.contains("B")){calTypeActual = calTypeActual.replace("B", CommonUtil.removeNull(calVO.getB(),"0"));}
						if(calTypeActual.contains("C")){calTypeActual = calTypeActual.replace("C", CommonUtil.removeNull(calVO.getC(),"0"));}
						if(calTypeActual.contains("D")){calTypeActual = calTypeActual.replace("D", CommonUtil.removeNull(calVO.getD(),"0"));}
						if(calTypeActual.contains("E")){calTypeActual = calTypeActual.replace("E", CommonUtil.removeNull(calVO.getE(),"0"));}
						if(calTypeActual.contains("F")){calTypeActual = calTypeActual.replace("F", CommonUtil.removeNull(calVO.getF(),"0"));}
						if(calTypeActual.contains("G")){calTypeActual = calTypeActual.replace("G", CommonUtil.removeNull(calVO.getG(),"0"));}
						if(calTypeActual.contains("H")){calTypeActual = calTypeActual.replace("H", CommonUtil.removeNull(calVO.getH(),"0"));}
						if(calTypeActual.contains("I")){calTypeActual = calTypeActual.replace("I", CommonUtil.removeNull(calVO.getI(),"0"));}
						if(calTypeActual.contains("J")){calTypeActual = calTypeActual.replace("J", CommonUtil.removeNull(calVO.getJ(),"0"));}
						if(calTypeActual.contains("K")){calTypeActual = calTypeActual.replace("K", CommonUtil.removeNull(calVO.getK(),"0"));}
						if(calTypeActual.contains("L")){calTypeActual = calTypeActual.replace("L", CommonUtil.removeNull(calVO.getL(),"0"));}
						if(calTypeActual.contains("M")){calTypeActual = calTypeActual.replace("N", CommonUtil.removeNull(calVO.getN(),"0"));}
						if(calTypeActual.contains("O")){calTypeActual = calTypeActual.replace("O", CommonUtil.removeNull(calVO.getO(),"0"));}
						if(calTypeActual.contains("P")){calTypeActual = calTypeActual.replace("P", CommonUtil.removeNull(calVO.getP(),"0"));}
						if(calTypeActual.contains("Q")){calTypeActual = calTypeActual.replace("Q", CommonUtil.removeNull(calVO.getQ(),"0"));}
						if(calTypeActual.contains("R")){calTypeActual = calTypeActual.replace("R", CommonUtil.removeNull(calVO.getR(),"0"));}
						if(calTypeActual.contains("S")){calTypeActual = calTypeActual.replace("S", CommonUtil.removeNull(calVO.getS(),"0"));}
						if(calTypeActual.contains("T")){calTypeActual = calTypeActual.replace("T", CommonUtil.removeNull(calVO.getT(),"0"));}
						if(calTypeActual.contains("U")){calTypeActual = calTypeActual.replace("U", CommonUtil.removeNull(calVO.getU(),"0"));}
						if(calTypeActual.contains("V")){calTypeActual = calTypeActual.replace("V", CommonUtil.removeNull(calVO.getV(),"0"));}
						if(calTypeActual.contains("W")){calTypeActual = calTypeActual.replace("W", CommonUtil.removeNull(calVO.getW(),"0"));}
						if(calTypeActual.contains("X")){calTypeActual = calTypeActual.replace("X", CommonUtil.removeNull(calVO.getX(),"0"));}
						if(calTypeActual.contains("Y")){calTypeActual = calTypeActual.replace("Y", CommonUtil.removeNull(calVO.getY(),"0"));}
						if(calTypeActual.contains("Z")){calTypeActual = calTypeActual.replace("Z", CommonUtil.removeNull(calVO.getZ(),"0"));}
					}

					try{
						// 제수가 0일 때 mysql은 return null이므로 0으로 처리함
						if(CommonUtil.isEmpty(cDAO.selectChkData(calTypeActual))) {
							calTypeActual = "0";
						};
					}catch(SQLDataException sqe){
						calTypeActual="0";
					}catch(Exception e){
						// 제수가 0일 때 oracle exception에 대한 처리
						if(e.getMessage().contains("ORA-01476")) {
							calTypeActual="0";
						} else if(e.getMessage().contains("JDBC-5070")) {	// tibero 처리
							calTypeActual="0";
						}
					}finally{
						calVO.setCalTypeActual(calTypeActual);
						cDAO.updateActualData(calVO);
					}
				}
			}
		}catch(SQLException sqe){
			successYn = "N";
			exceptionText = sqe.getCause().toString();
		}catch(Exception e){
			successYn = "N";
			exceptionText = e.getCause().toString();
		}finally{
			insertLog(resultCnt,successYn,exceptionText,procId);
		}
	}

	//점수계산
	private void updateScore(){

		procId = "UPDATE SCORE DATA";

		try{
			String calTypeScore;
			String result;
			List<CalculationVO> calTypeScoreList = cDAO.selectCalTypeScoreList(cVO);
			if(calTypeScoreList != null && calTypeScoreList.size() > 0){
				for(CalculationVO calVO : calTypeScoreList){

					calTypeScore = calVO.getCalType().toUpperCase();
					if(!"".equals(CommonUtil.nullToBlank(calTypeScore))){
						if(calTypeScore.contains("ACTUAL")){calTypeScore = calTypeScore.replace("ACTUAL", CommonUtil.removeNull(calVO.getActual(),"0"));}
						if(calTypeScore.contains("TARGET")){calTypeScore = calTypeScore.replace("TARGET", CommonUtil.removeNull(calVO.getTarget(),"0"));}
					}

					try{
						// 제수가 0일 때 mysql은 return null이므로 0으로 처리함
						if(CommonUtil.isEmpty(cDAO.selectChkData(calTypeScore))) {
							calTypeScore = "0";
						};
					}catch(SQLException sqe){	
						// 제수가 0일 때 oracle exception에 대한 처리
						calTypeScore="0";
					}catch(Exception e){
						// 제수가 0일 때 oracle exception에 대한 처리
						if(e.getMessage().contains("ORA-01476")) {
							calTypeScore="0";
						} else if(e.getMessage().contains("JDBC-5070")) {	// tibero 처리
							calTypeScore="0";
						}
					}finally{
						calVO.setCalTypeScore(calTypeScore);
						cDAO.updateScoreData(calVO);
					}
				}
			}
		}catch(SQLException sqe){	
			successYn = "N";
			exceptionText = sqe.getCause().toString();
		}catch(Exception e){
			successYn = "N";
			exceptionText = e.getCause().toString();
		}finally{
			insertLog(resultCnt,successYn,exceptionText,procId);
		}
	}

	//최종점수 계산(상한/하한 적용)
	private void updateFinalScore(){

		procId = "INSERT FINALSCORE/STATUS DATA";

		try{
			cDAO.updateDownwardIntervalScoreData(cVO);
			cDAO.updateDownwardIntervalActualData(cVO);
			cDAO.updateUpwardIntervalScoreData(cVO);
			cDAO.updateUpwardIntervalActualData(cVO);
			cDAO.updateFinalScoreData(cVO);
			cDAO.updateSignalData(cVO);
		}catch(SQLException sqe){	
			successYn = "N";
			exceptionText = sqe.getCause().toString();	
		}catch(Exception e){
			successYn = "N";
			exceptionText = e.getCause().toString();
		}finally{
			insertLog(resultCnt,successYn,exceptionText,procId);
		}
	}

	//공유지표 실벅 복사
	public void insertShareData(){

		procId = "INSERT SHARE_ACTUAL DATA";

		try{
			cDAO.deleteShareActualData(cVO);
			cDAO.insertShareActualData(cVO);
		}catch(SQLException sqe){	
			successYn = "N";
			exceptionText = sqe.getCause().toString();	
		}catch(Exception e){
			successYn = "N";
			exceptionText = e.getCause().toString();
		}finally{
			insertLog(resultCnt,successYn,exceptionText,procId);
		}
	}

	//집계테이블 저장
	private boolean insertSummarry(){

		procId = "INSERT SUMMARY DATA";

		//2.actual테이블의 "Y" 데이터 삭제
		try{
			cDAO.deleteMetricscoreData(cVO);
			cDAO.deletePerspectivescoreData(cVO);
			cDAO.deleteStrategyscoreData(cVO);
			cDAO.deleteDeptscoreData(cVO);

			cDAO.insertMetricscoreData(cVO);
			cDAO.insertDeptscoreData(cVO);
			cDAO.insertStrategyscoreData(cVO);
			cDAO.insertPerspectivescoreData(cVO);

			cDAO.updatePerspectivescoreStatusData(cVO);
			cDAO.updateStrategyscoreStatusData(cVO);
			cDAO.updateDeptscoreStatusData(cVO);
		}catch(SQLException sqe){	
			successYn = "N";
			exceptionText = sqe.getCause().toString();
			return false;
		}catch(Exception e){
			successYn = "N";
			exceptionText = e.getCause().toString();
			return false;
		}finally{
			insertLog(resultCnt,successYn,exceptionText,procId);
		}

		return true;
	}

	//집계테이블 저장
	private void insertProcAdminLog() throws SQLException{

		try {
			finalSeq = cDAO.selectFinalNextSeq(cVO);
		}catch(SQLException sqe){	
			sqe.getCause();
		} catch (Exception e) {
			e.getCause();
		}
		cVO.setFinalSeq(finalSeq);
		cVO.setProcId("METRICSCORE_BATCH");

		resultCnt = cDAO.insertProcAdminLog(cVO);

	}



	//계산된지표 실적 및 점수 산식항목실적 반영
	/*실적 저장 시 마지막에 사용
	 * 1. 계산된지표 실적반영
	 * 2. ROLLUP 반영
	 * 2. 실적계산
	 * 3. 점수계산*/
	private void insertCalMetricActual(){

		procId = "INSERT CAL_METRIC INSERT/ACTUAL/SCORE DATA";

		List<CalculationVO> metricList = new ArrayList<CalculationVO>();
		List<CalculationVO> metricUpList = new ArrayList<CalculationVO>();
		metricList.add(cVO);

		try{

			while(metricList != null && metricList.size() > 0){

				if(metricList != null && metricList.size() > 0){
					for(CalculationVO metricVO : metricList){

						calList = cDAO.selectCalMetricData(metricVO);
						if(calList != null && calList.size() > 0){
							for(CalculationVO calvo : calList){
								if("Y".equals(calvo.getMainActYn())){
									cDAO.updateCalActualData(calvo);
								}else{
									cDAO.insertCalActualData(calvo);
								}

								/*
								 * 자동 계산이 필요하다면 하위 메소드 실행
								 * 연계지표 입력 후 모든 산식항목에 값이 있으면  계산하도록 함. 아니면 실적 입력만 하도록 함.
								 */

								metricVO.setMetricId(calvo.getMainMetricId());
								if("Y".equals(cDAO.selectColActAllYn(metricVO))){
									deleteRollUp();
									insertRollup();
									updateActual();
									updateScore();
									updateFinalScore();
									insertShareData();

									cVO.setMetricId(metricVO.getMetricId());
									metricUpList.add(cVO);

								}
							}
						}
					}
					metricList.clear();
					metricList = metricUpList;
				}
			}
		}catch(SQLException sqe){	
			successYn = "N";
			exceptionText = sqe.getCause().toString();	
		}catch(Exception e){
			successYn = "N";
			exceptionText = e.getCause().toString();
		}finally{
			insertLog(resultCnt,successYn,exceptionText,procId);
		}
	}


}
