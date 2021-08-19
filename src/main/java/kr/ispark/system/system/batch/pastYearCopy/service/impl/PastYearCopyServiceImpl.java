/*************************************************************************
* CLASS 명	: PastYearCopyServiceIpml
* 작 업 자	: 박정현
* 작 업 일	: 2018-06-29
* 기	능	: 전년데이터일괄적용 ServiceIpml
* ---------------------------- 변 경 이 력 --------------------------------
* 번호	작 업 자		작	업	일			변 경 내 용				비고
* ----	---------	----------------	---------------------	-----------
*	1	박정현		2018-06-29
**************************************************************************/
package kr.ispark.system.system.batch.pastYearCopy.service.impl;

import java.sql.SQLException;
import java.util.Calendar;
import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;
import egovframework.rte.psl.dataaccess.util.EgovMap;
import kr.ispark.common.CommonVO;
import kr.ispark.common.system.service.impl.CommonServiceImpl;
import kr.ispark.common.system.service.impl.IdGenServiceImpl;
import kr.ispark.common.util.CommonUtil;
import kr.ispark.common.util.PropertyUtil;
import kr.ispark.common.util.service.impl.CodeUtilServiceImpl;
import kr.ispark.system.system.batch.pastYearCopy.service.PastYearCopyVO;

@Service
public class PastYearCopyServiceImpl extends EgovAbstractServiceImpl {
	public final Logger log = LoggerFactory.getLogger(this.getClass());

	@Resource
	private IdGenServiceImpl idgenService;

	@Resource
	private PastYearCopyDAO pastYearCopyDAO;

	@Autowired
	public CodeUtilServiceImpl codeUtilService;
	
	@Autowired
    private CommonServiceImpl commonServiceImpl;

	/**
	 * 전년데이터일괄적용 목록 조회
	 * @param	PastYearCopyVO searchVO
	 * @return	List<PastYearCopyVO>
	 * @throws	Exception
	 */
	public List<PastYearCopyVO> selectList(PastYearCopyVO searchVO) throws Exception {
		int pastYear = Integer.parseInt(searchVO.getFindYear())-1;
		String pastYearStr = Integer.toString(pastYear);
		searchVO.setPastYear(pastYearStr);
		return pastYearCopyDAO.selectList(searchVO);
	}

	/**
	 * 전년데이터일괄적용 목록 조회
	 * @param	PastYearCopyVO searchVO
	 * @return	List<PastYearCopyVO>
	 * @throws	Exception
	 */
	public int applyData(PastYearCopyVO dataVO) throws Exception {

		String[] tableIdArray = dataVO.getTableIds().split("\\|",0);
		int resultCnt = 0;

		int pastYear = Integer.parseInt(dataVO.getFindYear())-1;
		String pastYearStr = Integer.toString(pastYear);
		dataVO.setPastYear(pastYearStr);

		if(tableIdArray != null && tableIdArray.length > 0){
			for( int i = 0 ; i < tableIdArray.length ; i ++) {
				String tableNm = tableIdArray[i];

				if( tableNm.equals("COM_CODE") ){//공통코드
					resultCnt += pastYearCopyDAO.deleteComCode(dataVO);
					resultCnt += pastYearCopyDAO.insertComCode(dataVO);
				}else if(tableNm.equals("COM_SIGNAL_STATUS")){//신호등
					resultCnt += pastYearCopyDAO.deleteComSignal(dataVO);
					resultCnt += pastYearCopyDAO.insertComSignal(dataVO);
				}else if(tableNm.equals("BSC_EVAL_METHOD")){//평가등급
					resultCnt += pastYearCopyDAO.deleteEvalGradeCnt(dataVO);
					resultCnt += pastYearCopyDAO.deleteEvalGrade(dataVO);
					resultCnt += pastYearCopyDAO.deleteEvalMethod(dataVO);
					resultCnt += pastYearCopyDAO.insertEvalMethod(dataVO);
					resultCnt += pastYearCopyDAO.insertEvalGrade(dataVO);
					resultCnt += pastYearCopyDAO.insertEvalGradeCnt(dataVO);
				}else if(tableNm.equals("BSC_SYSTEM_ITEM")){//시스템연계항목
					resultCnt += pastYearCopyDAO.deleteBscSystemItem(dataVO);
					resultCnt += pastYearCopyDAO.insertBscSystemItem(dataVO);
				}else if(tableNm.equals("BSC_PERSPECTIVE")){//관점
					resultCnt += pastYearCopyDAO.deleteBscPerspective(dataVO);
					resultCnt += pastYearCopyDAO.insertBscPerspective(dataVO);
				}else if(tableNm.equals("BSC_STRATEGY")){//전략목표
					resultCnt += pastYearCopyDAO.deleteBscStrategyMap(dataVO);
					resultCnt += pastYearCopyDAO.deleteBscStrategy(dataVO);
					resultCnt += pastYearCopyDAO.insertBscStrategy(dataVO);
					resultCnt += pastYearCopyDAO.insertBscStrategyMap(dataVO);
				}else if(tableNm.equals("BSC_SC_DEPT")){//성과조직
					resultCnt += pastYearCopyDAO.deleteBscScDeptMap(dataVO);
					resultCnt += pastYearCopyDAO.deleteBscScDeptMapping(dataVO);
					resultCnt += pastYearCopyDAO.deleteBscScDept(dataVO);
					resultCnt += pastYearCopyDAO.insertBscScDept(dataVO);
					resultCnt += pastYearCopyDAO.insertBscScDeptMapping(dataVO);
					resultCnt += pastYearCopyDAO.insertBscScDeptMap(dataVO);
				}else if(tableNm.equals("BSC_METRIC_GRP")){//지표POOL
					resultCnt += pastYearCopyDAO.deleteBscMetricGrpSection(dataVO);
					resultCnt += pastYearCopyDAO.deleteBscMetricGrpCol(dataVO);
					resultCnt += pastYearCopyDAO.deleteBscMetricGrpMon(dataVO);
					resultCnt += pastYearCopyDAO.deleteBscMetricGrp(dataVO);
					resultCnt += pastYearCopyDAO.insertBscMetricGrp(dataVO);
					resultCnt += pastYearCopyDAO.insertBscMetricGrpMon(dataVO);
					resultCnt += pastYearCopyDAO.insertBscMetricGrpCol(dataVO);
					resultCnt += pastYearCopyDAO.insertBscMetricGrpSection(dataVO);
				}else if(tableNm.equals("BSC_METRIC")){//지표
					resultCnt += pastYearCopyDAO.deleteBscMetricSection(dataVO);
					resultCnt += pastYearCopyDAO.deleteBscMetricCol(dataVO);
					resultCnt += pastYearCopyDAO.deleteBscMetricMon(dataVO);
					resultCnt += pastYearCopyDAO.deleteBscMetric(dataVO);
					resultCnt += pastYearCopyDAO.insertBscMetric(dataVO);
					resultCnt += pastYearCopyDAO.insertBscMetricMon(dataVO);
					resultCnt += pastYearCopyDAO.insertBscMetricCol(dataVO);
					resultCnt += pastYearCopyDAO.insertBscMetricSection(dataVO);
				}else if(tableNm.equals("GOV_EVAL_CAT_GRP")){//경영평가범주
					resultCnt += pastYearCopyDAO.deleteGovEvalCatGrp(dataVO);
					resultCnt += pastYearCopyDAO.insertGovEvalCatGrp(dataVO);
				}else if(tableNm.equals("GOV_EVAL_CAT")){//경영평가부문
					resultCnt += pastYearCopyDAO.deleteGovEvalCat(dataVO);
					resultCnt += pastYearCopyDAO.insertGovEvalCat(dataVO);
				}else if(tableNm.equals("GOV_METRIC")){//경영평가지표
					resultCnt += pastYearCopyDAO.deleteGovMetricEvalItem(dataVO);
					resultCnt += pastYearCopyDAO.deleteGovCalTypeCol(dataVO);
					resultCnt += pastYearCopyDAO.deleteGovMetric(dataVO);
					resultCnt += pastYearCopyDAO.insertGovMetric(dataVO);
					resultCnt += pastYearCopyDAO.insertGovCalTypeCol(dataVO);
					resultCnt += pastYearCopyDAO.insertGovMetricEvalItem(dataVO);
				}
			}
		}

		return resultCnt;
	}

	/**
	 * 전년도 데이터 복사
	 * @param	PastYearCopyVO dataVO
	 * @return	int
	 * @throws	Exception
	 */
	public int copyDataFromLastYear(PastYearCopyVO dataVO) throws Exception {
		int resultCnt = 0;

		String tableId = CommonUtil.removeNull(dataVO.getTableId());
		int pastYear = Integer.parseInt(dataVO.getFindYear())-1;
		String pastYearStr = Integer.toString(pastYear);
		dataVO.setPastYear(pastYearStr);

		if(tableId.equals("BSC_PERSPECTIVE")){	// 관점
			resultCnt += pastYearCopyDAO.deleteBscPerspective(dataVO);
			resultCnt += pastYearCopyDAO.insertBscPerspective(dataVO);
			insertComSeq(dataVO);
		} else if(tableId.equals("BSC_STRATEGY")){	// 전략목표
			resultCnt += pastYearCopyDAO.deleteBscStrategy(dataVO);
			resultCnt += pastYearCopyDAO.insertBscStrategy(dataVO);
			insertComSeq(dataVO);
		} else if(tableId.equals("BSC_STRATEGY_MAP")){	// 전략체계도
			resultCnt += pastYearCopyDAO.deleteBscStrategyMap(dataVO);
			resultCnt += pastYearCopyDAO.insertBscStrategyMap(dataVO);
		} else if(tableId.equals("GOV_EVAL_CAT_GRP")){	// 경영평가범주
			resultCnt += pastYearCopyDAO.deleteGovEvalCatGrp(dataVO);
			resultCnt += pastYearCopyDAO.insertGovEvalCatGrp(dataVO);
			insertComSeq(dataVO);

			dataVO.setTableId("GOV_EVAL_CAT");	// 경영평가부문
			resultCnt += pastYearCopyDAO.deleteGovEvalCat(dataVO);
			resultCnt += pastYearCopyDAO.insertGovEvalCat(dataVO);
			insertComSeq(dataVO);
		} else if(tableId.equals("BSC_METRIC_GRP")){
			// 지표 전년도 데이터 복사 전 지표 관련 데이터 삭제
			//pastYearCopyDAO.deleteBscMetricDatas(dataVO);
			pastYearCopyDAO.deleteBscMetricData1(dataVO);
			pastYearCopyDAO.deleteBscMetricData2(dataVO);
			pastYearCopyDAO.deleteBscMetricData3(dataVO);
			pastYearCopyDAO.deleteBscMetricData4(dataVO);
			pastYearCopyDAO.deleteBscMetricData5(dataVO);
			pastYearCopyDAO.deleteBscMetricData6(dataVO);
			pastYearCopyDAO.deleteBscMetricData7(dataVO);
			pastYearCopyDAO.deleteBscMetricData8(dataVO);
			pastYearCopyDAO.deleteBscMetricData9(dataVO);
			pastYearCopyDAO.deleteBscMetricData10(dataVO);
			pastYearCopyDAO.deleteBscMetricData11(dataVO);
			pastYearCopyDAO.deleteBscMetricData12(dataVO);
			pastYearCopyDAO.deleteBscMetricData13(dataVO);
			pastYearCopyDAO.deleteBscMetricData14(dataVO);
			pastYearCopyDAO.deleteBscMetricData15(dataVO);
			pastYearCopyDAO.deleteBscMetricData16(dataVO);
			pastYearCopyDAO.deleteBscMetricData17(dataVO);
			pastYearCopyDAO.deleteBscMetricData18(dataVO);
			pastYearCopyDAO.deleteBscMetricData19(dataVO);
			pastYearCopyDAO.deleteBscMetricData20(dataVO);
			pastYearCopyDAO.deleteBscMetricData21(dataVO);
			pastYearCopyDAO.deleteBscMetricData22(dataVO);

			// 지표POOL
			resultCnt += pastYearCopyDAO.insertBscMetricGrp(dataVO);
			resultCnt += pastYearCopyDAO.insertBscMetricGrpMon(dataVO);
			resultCnt += pastYearCopyDAO.insertBscMetricGrpCol(dataVO);
			resultCnt += pastYearCopyDAO.insertBscMetricGrpSection(dataVO);
			insertComSeq(dataVO);

			// 지표
			dataVO.setTableId("BSC_METRIC");
			resultCnt += pastYearCopyDAO.insertBscMetric(dataVO);
			resultCnt += pastYearCopyDAO.insertBscMetricMon(dataVO);
			resultCnt += pastYearCopyDAO.insertBscMetricCol(dataVO);
			resultCnt += pastYearCopyDAO.insertBscMetricSection(dataVO);
			insertComSeq(dataVO);
		}

		return resultCnt;
	}
	
	public void insertComSeq(PastYearCopyVO dataVO) throws Exception {
		pastYearCopyDAO.deleteComSeqData(dataVO);
		pastYearCopyDAO.insertComSeqData(dataVO);
	}

	/**
	 * 전년도 시스템 데이터 복사
	 * @return	int
	 * @throws	Exception
	 */
	public void copySystemDataFromLastYear() throws Exception {
		int currentYear = Calendar.getInstance().get(Calendar.YEAR);

		log.debug("# 전년도 시스템 데이터 복사 시작");
		log.debug("# " + (currentYear-1) + "년 -> " + currentYear + "년");

		try {
			
			List<CommonVO> dbList = commonServiceImpl.selectDbList();
			
			if(dbList != null && dbList.size() > 0){
				for(CommonVO targetCommVO : dbList){
					if(!"".equals(CommonUtil.nullToBlank(targetCommVO.getParamCompId()))){
						
						PastYearCopyVO dataVO = new PastYearCopyVO();
						
						dataVO.setTargetCompId(targetCommVO.getParamCompId());
						
						EgovMap map = new EgovMap();
						map.put("paramCompId",targetCommVO.getParamCompId());
						dataVO.setTargetDbId(commonServiceImpl.selectDbId(map));
						
						dataVO.setFindYear(String.valueOf(currentYear));
						dataVO.setPastYear(String.valueOf(currentYear-1));
		
						pastYearCopyDAO.copySystemDataFromLastYear(dataVO);
					}
				}
			}
			
			log.debug("# 복사 완료");

			// 공통코드 리로드
			codeUtilService.insertCodeUpdateLog(PropertyUtil.getProperty("Super.CompId"));
		} catch(SQLException sqe) {
			log.error("error : "+sqe.getCause());
		} catch(Exception e) {
			log.error("error : "+e.getCause());
		}
	}
}
