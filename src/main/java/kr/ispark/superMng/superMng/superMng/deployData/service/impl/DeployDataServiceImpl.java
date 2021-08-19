/*************************************************************************
* CLASS 명	: DeployDataServiceIpml
* 작 업 자	: 현걸욱
* 작 업 일	: 2018-07-09
* 기	능	: 고객사별 전년데이터 일괄적용 ServiceIpml
* ---------------------------- 변 경 이 력 --------------------------------
* 번호	작 업 자		작	업	일			변 경 내 용				비고
* ----	---------	----------------	---------------------	-----------
*	1	현걸욱		2018-07-09
**************************************************************************/
package kr.ispark.superMng.superMng.superMng.deployData.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;
import egovframework.rte.psl.dataaccess.util.EgovMap;
import kr.ispark.superMng.superMng.superMng.deployData.service.DeployDataVO;
import kr.ispark.common.CommonVO;
import kr.ispark.common.system.service.impl.CommonServiceImpl;
import kr.ispark.common.system.service.impl.IdGenServiceImpl;
import kr.ispark.common.util.CommonUtil;

@Service
public class DeployDataServiceImpl extends EgovAbstractServiceImpl {

	@Resource
	private IdGenServiceImpl idgenService;
	
	@Resource
	private DeployDataDAO deployDataDAO;

	@Autowired
    private CommonServiceImpl commonServiceImpl;
	
	/**
	 * 고객사별 전년데이터 일괄적용 목록 조회
	 * @param	DeployDataVO searchVO
	 * @return	List<DeployDataVO>
	 * @throws	Exception
	 */
	public List<DeployDataVO> selectList(DeployDataVO searchVO) throws Exception {
		
		List<CommonVO> dbList = commonServiceImpl.selectDbList();
		searchVO.setDbList(dbList);
		
		return deployDataDAO.selectList(searchVO);
	}
	
	/**
	 * 고객사별 전년데이터 일괄적용 저장
	 * @param	DeployDataVO searchVO
	 * @return	int
	 * @throws	Exception
	 */
	public int saveData(DeployDataVO dataVO) throws Exception {
		
		int resultCnt = 0;
		int pastYear = Integer.parseInt(dataVO.getFindYear())-1;
		dataVO.setPastYear(String.valueOf(pastYear));
		
		String[] compIds = dataVO.getCompIds().split("\\|",0);
		if(compIds != null && compIds.length > 0){
			for(String targetCompId : compIds){
				
				if(!"".equals(CommonUtil.nullToBlank(targetCompId))){
					dataVO.setTargetCompId(targetCompId);
					
					EgovMap map = new EgovMap();
					map.put("paramCompId",targetCompId);
					dataVO.setTargetDbId(commonServiceImpl.selectDbId(map));
					
					/*delete and insert*/
					resultCnt += deployDataDAO.deleteComCode(dataVO);
					resultCnt += deployDataDAO.insertComCode(dataVO);
					resultCnt += deployDataDAO.deleteComCodeNm(dataVO);
					resultCnt += deployDataDAO.insertComCodeNm(dataVO);
					resultCnt += deployDataDAO.deleteComSignal(dataVO);
					resultCnt += deployDataDAO.insertComSignal(dataVO);
					resultCnt += deployDataDAO.deleteEvalGradeCnt(dataVO);
					resultCnt += deployDataDAO.deleteEvalGrade(dataVO);
					resultCnt += deployDataDAO.deleteEvalMethod(dataVO);
					resultCnt += deployDataDAO.insertEvalMethod(dataVO);
					resultCnt += deployDataDAO.insertEvalGrade(dataVO);
					resultCnt += deployDataDAO.insertEvalGradeCnt(dataVO);
					resultCnt += deployDataDAO.deleteBscSystemItem(dataVO);
					resultCnt += deployDataDAO.insertBscSystemItem(dataVO);
					
					resultCnt += deployDataDAO.deleteBscPerspective(dataVO);
					resultCnt += deployDataDAO.insertBscPerspective(dataVO);
					resultCnt += deployDataDAO.deleteBscStrategyMap(dataVO);
					resultCnt += deployDataDAO.deleteBscStrategy(dataVO);
					resultCnt += deployDataDAO.insertBscStrategy(dataVO);
					resultCnt += deployDataDAO.insertBscStrategyMap(dataVO);
					resultCnt += deployDataDAO.deleteBscScDeptMap(dataVO);
					resultCnt += deployDataDAO.deleteBscScDeptMapping(dataVO);
					resultCnt += deployDataDAO.deleteBscScDept(dataVO);
					resultCnt += deployDataDAO.insertBscScDept(dataVO);
					resultCnt += deployDataDAO.deleteDept(dataVO);
					resultCnt += deployDataDAO.insertDept(dataVO);
					resultCnt += deployDataDAO.insertBscScDeptMapping(dataVO);
					resultCnt += deployDataDAO.insertBscScDeptMap(dataVO);
					resultCnt += deployDataDAO.deleteBscMetricGrpSection(dataVO);
					resultCnt += deployDataDAO.deleteBscMetricGrpCol(dataVO);
					resultCnt += deployDataDAO.deleteBscMetricGrpMon(dataVO);
					resultCnt += deployDataDAO.deleteBscMetricGrp(dataVO);
					resultCnt += deployDataDAO.insertBscMetricGrp(dataVO);
					resultCnt += deployDataDAO.insertBscMetricGrpMon(dataVO);
					resultCnt += deployDataDAO.insertBscMetricGrpCol(dataVO);
					resultCnt += deployDataDAO.insertBscMetricGrpSection(dataVO);
					resultCnt += deployDataDAO.deleteBscMetricSection(dataVO);
					resultCnt += deployDataDAO.deleteBscMetricCol(dataVO);
					resultCnt += deployDataDAO.deleteBscMetricMon(dataVO);
					resultCnt += deployDataDAO.deleteBscMetric(dataVO);
					resultCnt += deployDataDAO.insertBscMetric(dataVO);
					resultCnt += deployDataDAO.insertBscMetricMon(dataVO);
					resultCnt += deployDataDAO.insertBscMetricCol(dataVO);
					resultCnt += deployDataDAO.insertBscMetricSection(dataVO);
					/*cloud 에서는 정부경영평가 없음.
					resultCnt += deployDataDAO.deleteGovEvalCatGrp(dataVO);
					resultCnt += deployDataDAO.insertGovEvalCatGrp(dataVO);
					resultCnt += deployDataDAO.deleteGovEvalCat(dataVO);
					resultCnt += deployDataDAO.insertGovEvalCat(dataVO);
					resultCnt += deployDataDAO.deleteGovMetricEvalItem(dataVO);
					resultCnt += deployDataDAO.deleteGovCalTypeCol(dataVO);
					resultCnt += deployDataDAO.deleteGovMetric(dataVO);
					resultCnt += deployDataDAO.insertGovMetric(dataVO);
					resultCnt += deployDataDAO.insertGovCalTypeCol(dataVO);
					resultCnt += deployDataDAO.insertGovMetricEvalItem(dataVO);
					*/
				}
			}
		}
		return resultCnt;
	}
}

