/*************************************************************************
* CLASS 명	: YearBatchServiceIpml
* 작 업 자	: 현걸욱
* 작 업 일	: 2018-04-23
* 기	능	: 년배치 ServiceIpml
* ---------------------------- 변 경 이 력 --------------------------------
* 번호	작 업 자		작	업	일			변 경 내 용				비고
* ----	---------	----------------	---------------------	-----------
*	1	현걸욱		2018-04-23
**************************************************************************/
package kr.ispark.system.system.batch.yearBatch.service.impl;

import java.sql.SQLDataException;
import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.Future;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;
import kr.ispark.system.system.batch.yearBatch.service.YearBatchVO;
import lombok.Synchronized;
import kr.ispark.common.calculation.service.CalculationVO;
import kr.ispark.common.calculation.service.cal.CalculationDAO;
import kr.ispark.common.calculation.service.cal.CalculationServiceImpl;
import kr.ispark.common.security.service.UserVO;
import kr.ispark.common.system.service.impl.CommonServiceImpl;
import kr.ispark.common.system.service.impl.IdGenServiceImpl;
import kr.ispark.common.util.CommonUtil;
import kr.ispark.common.util.SessionUtil;

@Service
public class YearBatchServiceImpl extends EgovAbstractServiceImpl {

	@Resource
	private IdGenServiceImpl idgenService;

	/*
	@Resource
	private CalculationServiceImpl calService;
	*/

	@Resource
	private YearBatchDAO yearBatchDAO;
	
	@Autowired
	private CommonServiceImpl commonService;
	
	@Resource
	private CalculationDAO cDAO;

	/**
	 * 년배치 목록 조회
	 * @param	YearBatchVO searchVO
	 * @return	List<YearBatchVO>
	 * @throws	Exception
	 */
	public List<YearBatchVO> selectList(YearBatchVO searchVO) throws Exception {
		return yearBatchDAO.selectList(searchVO);
	}

	/**
	 * 년배치 상세 조회
	 * @param	YearBatchVO searchVO
	 * @return	YearBatchVO
	 * @throws	Exception
	 */
	public YearBatchVO selectDetail(YearBatchVO searchVO) throws Exception {
		return yearBatchDAO.selectDetail(searchVO);
	}

	/**
	 * 년배치 정렬순서저장
	 * @param	YearBatchVO searchVO
	 * @return	int
	 * @throws	Exception
	 */
	public int updateSortOrder(YearBatchVO dataVO) throws Exception {
		int resultCnt = 0;
		for(YearBatchVO paramVO : dataVO.getGridDataList()) {
			resultCnt += yearBatchDAO.updateSortOrder(paramVO);
		}
		return resultCnt;
	}

	/**
	 * 년배치 삭제
	 * @param	YearBatchVO searchVO
	 * @return	int
	 * @throws	Exception
	 */
	public int deleteYearBatch(YearBatchVO dataVO) throws Exception {
		return yearBatchDAO.deleteYearBatch(dataVO);
	}

	/**
	 * 년배치 저장
	 * @param	YearBatchVO searchVO
	 * @return	int
	 * @throws	Exception
	 */
	public int updateYearBatch(YearBatchVO dataVO) throws Exception {

		/*
		UserVO uVO = (UserVO)SessionUtil.getAttribute("loginVO");
		CalculationVO cVO = new CalculationVO();
		cVO.setYear(dataVO.getFindYear());
		cVO.setMon(dataVO.getFindMon());
		cVO.setMetricId("");
		cVO.setKpiApproveUseYn("Y");
		cVO.setActApproveUseYn("Y");
		cVO.setExecUserId(uVO!=null?uVO.getUserId():null);
		cVO.setApproveYn("");
		cVO.setCompId(SessionUtil.getCompId());
		
		int startMon = 1;
		int endMon = Integer.parseInt(cVO.getMon());
		
		while(startMon <= endMon){

			if(startMon < 10){
				cVO.setMon("0"+startMon);
			}else{
				cVO.setMon(""+startMon);
			}

			//deleteRollUp();
			cDAO.deleteRollupData(cVO);
			//insertRollup();
			cDAO.insertRollupData(cVO);
			//updateActual();
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
				sqe.getCause();
			}catch(Exception e){
				e.getCause();
			}
			//updateScore();
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
				sqe.getCause();
			}catch(Exception e){
				e.getCause();
			}
			
			//updateFinalScore();
			cDAO.updateDownwardIntervalScoreData(cVO);
			cDAO.updateDownwardIntervalActualData(cVO);
			cDAO.updateUpwardIntervalScoreData(cVO);
			cDAO.updateUpwardIntervalActualData(cVO);
			cDAO.updateFinalScoreData(cVO);
			cDAO.updateSignalData(cVO);
			cDAO.deleteShareActualData(cVO);
			cDAO.insertShareActualData(cVO);
			//insertShareData();
			//insertSummarry();
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

			startMon ++;
		}
		
		//insertProcAdminLog();
		cDAO.selectFinalNextSeq(cVO);
		return 1;
		*/
		//CalculationServiceImpl calService = new CalculationServiceImpl();
		String kpiApproveUseYn = commonService.selectMetricApproveUseYn(dataVO);
		String actApproveUseYn = commonService.selectActApproveUseYn(dataVO);
		//calService.setAllParam(dataVO.getFindYear(), dataVO.getFindMon());
		String result = execAll(dataVO.getFindYear(), dataVO.getFindMon(), kpiApproveUseYn, actApproveUseYn);
		
		if("success".equals(result.toLowerCase())){
			return 1;
		}else{
			return 0;
		}
		
		
		
		
		/*
		calService.setAllParam(dataVO.getFindYear(), dataVO.getFindMon());
		Future<String> result = calService.execAll();
		String resultString = result.get();
		
		System.out.println("-----------------------Future<String> result : "+result.get());
		
		if("success".equals(resultString.toLowerCase())){
			return 1;
		}else{
			return 0;
		}
		*/
		
	}
	
	public String execAll(String year, String mon, String kpiApproveUseYn, String actApproveUseYn) throws Exception {
		
		/*로그 변수*/
		int resultCnt = 0;
		String successYn = "";
		String successFinalYn = "Y";
		String exceptionText = "";
		String procId = "";
		String message = "";
		String seq = "";
		String finalSeq = "";
		int errorCnt = 0;
		
		UserVO uVO = (UserVO)SessionUtil.getAttribute("loginVO");
		CalculationVO cVO = new CalculationVO();
		cVO.setYear(year);
		cVO.setMon(mon);
		cVO.setKpiApproveUseYn(kpiApproveUseYn);
		cVO.setActApproveUseYn(actApproveUseYn);
		cVO.setExecUserId(uVO!=null?uVO.getUserId():null);
		
		int startMon = 1;
		int endMon = Integer.parseInt(cVO.getMon());
		
		while(startMon <= endMon){

			if(startMon < 10){
				cVO.setMon("0"+startMon);
			}else{
				cVO.setMon(""+startMon);
			}

			
			// 1. delete rollup
			procId = "DELETE TIME_ROLLUP DATA";
			try{
				cDAO.deleteRollupData(cVO);
			} catch (SQLException sqe) {
				successYn = "N";
				exceptionText = sqe.getCause().toString();
			}catch(Exception e){
				successYn = "N";
				exceptionText = e.getCause().toString();
			}finally{
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
				
				resultCnt = 0;
				successYn = "Y";
				exceptionText = "";
				procId = "";
			}
			
			// 2. insert rollup actual
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
				
				resultCnt = 0;
				successYn = "Y";
				exceptionText = "";
				procId = "";
			}
			
			// 3. actual 계산
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
				
				resultCnt = 0;
				successYn = "Y";
				exceptionText = "";
				procId = "";
			}
			
			// 4.update score
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
				
				resultCnt = 0;
				successYn = "Y";
				exceptionText = "";
				procId = "";
			}
			
			// 5.INSERT FINALSCORE/STATUS 
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
				
				resultCnt = 0;
				successYn = "Y";
				exceptionText = "";
				procId = "";
			}
			
			// 6.INSERT SHARE_ACTUAL DATA
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
				
				resultCnt = 0;
				successYn = "Y";
				exceptionText = "";
				procId = "";
			}
			
			//7. 
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
				errorCnt++;
			}catch(Exception e){
				successYn = "N";
				exceptionText = e.getCause().toString();
				errorCnt++;
			}finally{
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
				
				resultCnt = 0;
				successYn = "Y";
				exceptionText = "";
				procId = "";
			}
			startMon ++;
		}
		
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
		
		
		String result = "";
		if(errorCnt > 0){
			result = "fail";
		}else{
			result = "success";
		}

		return result;
	}
	
}
