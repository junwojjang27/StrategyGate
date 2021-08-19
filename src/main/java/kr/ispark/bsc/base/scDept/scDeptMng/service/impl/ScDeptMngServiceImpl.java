/*************************************************************************
* CLASS 명	: ScDeptMngServiceImpl
* 작 업 자	: kimyh
* 작 업 일	: 2018. 1. 23.
* 기	능	: 성과조직관리 Service
* ---------------------------- 변 경 이 력 --------------------------------
* 번호	작 업 자		작	업	일			변 경 내 용				비고
* ----	---------	----------------	---------------------	-----------
*	1	kimyh		2018. 1. 23.		최 초 작 업
**************************************************************************/
package kr.ispark.bsc.base.scDept.scDeptMng.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;
import kr.ispark.common.CommonVO;
import kr.ispark.common.system.service.ScDeptVO;
import kr.ispark.common.system.service.impl.IdGenServiceImpl;
import kr.ispark.common.util.CommonUtil;
import kr.ispark.common.util.PropertyUtil;
import kr.ispark.common.util.SessionUtil;

@Service
public class ScDeptMngServiceImpl extends EgovAbstractServiceImpl {

	@Autowired
	private IdGenServiceImpl idgenService;
	
	@Resource
	private ScDeptMngDAO scDeptMngDAO;

	// 성과조직관리 목록 조회
	public List<ScDeptVO> selectList(ScDeptVO searchVO) throws Exception {
		/*
		 * 하위 성과조직이 존재하면 : 현재의 성과조직과 하위 성과조직 조회
		 * 하위 성과조직이 존재하지 않으면 : 현재의 성과조직의 상위 성과조직과 그 하위 성과조직들 조회
		 */
		if(!searchVO.getFindScDeptId().equals(PropertyUtil.getProperty("default.rootScDeptId"))
				&& scDeptMngDAO.selectSubScDeptCount(searchVO) == 0) {
			searchVO.setFindScDeptId(scDeptMngDAO.selectUpScDeptId(searchVO));
		}
		
		return scDeptMngDAO.selectList(searchVO);
	}
	
	// 성과조직관리 목록 조회
	public List<ScDeptVO> selectExcelList(ScDeptVO searchVO) throws Exception {
		return scDeptMngDAO.selectExcelList(searchVO);
	}
	
	// 성과조직 상세 조회
	public ScDeptVO selectDetail(ScDeptVO searchVO) throws Exception {
		return scDeptMngDAO.selectDetail(searchVO);
	}
	
	// 정렬순서저장
	public int updateSortOrder(ScDeptVO dataVO) throws Exception {
		int resultCnt = 0;
		for(ScDeptVO paramVO : dataVO.getGridDataList()) {
			resultCnt += scDeptMngDAO.updateSortOrder(paramVO);
		}
		
		if(0<resultCnt){
			updateReDefineData(dataVO);
		}
		
		return resultCnt;
	}

	// 성과조직 삭제
	public int deleteScDeptMng(ScDeptVO dataVO) throws Exception {
		int resultCnt = 0;
		
		// 지표가 등록된 성과조직이 있으면 처리하지 않음
		if(scDeptMngDAO.selectMetricCountList(dataVO).size() > 0) {
			return 0;
		}
		
		resultCnt = scDeptMngDAO.deleteScDeptMng(dataVO);
		
		if(0<resultCnt){
			updateReDefineData(dataVO);
			insertAuth(dataVO);
		}
		
		return resultCnt;
	}
	
	// 성과조직 저장
	public int saveData(ScDeptVO dataVO) throws Exception {
		
		int resultCnt = 0;
		
		if(CommonUtil.isEmpty(dataVO.getScDeptId())) {
			dataVO.setScDeptId(idgenService.selectNextSeqByYear("BSC_SC_DEPT", dataVO.getYear(), "D", 6, "0"));
			resultCnt = scDeptMngDAO.insertData(dataVO);
		} else {
			resultCnt= scDeptMngDAO.updateData(dataVO);
		}
		
		if(0<resultCnt){
			updateReDefineData(dataVO);
			insertAuth(dataVO);
		}
		
		return resultCnt;
	}
	
	// 권한 갱신
	public void insertAuth(ScDeptVO dataVO) throws Exception {
		scDeptMngDAO.insertAuth(dataVO);
	}
	
	public void updateReDefineData(ScDeptVO dataVO) throws Exception {
	
		int order = 1;
		List<ScDeptVO> redefineList = scDeptMngDAO.selectReDefineData(dataVO);
		
		if(redefineList != null && 0<redefineList.size()){
			for(ScDeptVO vo:redefineList){
				vo.setRealSortOrder(order++);
				scDeptMngDAO.updateReDefineData(vo);
			}
		}
	}
	
	/**
	 * 지표담당자용 성과조직 조회
	 * @param	CommonVO searchVO
	 * @return	List<ScDeptVO>
	 * @throws	Exception
	 */
	public List<ScDeptVO> selectScDeptListByBscUserId(CommonVO searchVO) throws Exception {
		searchVO.setUserId(SessionUtil.getUserId());
		return scDeptMngDAO.selectScDeptListByBscUserId(searchVO);
	}
	
	/**
	 * 부서장용 성과조직 조회
	 * @param	CommonVO searchVO
	 * @return	List<ScDeptVO>
	 * @throws	Exception
	 */
	public List<ScDeptVO> selectScDeptListByManagerUserId(CommonVO searchVO) throws Exception {
		searchVO.setUserId(SessionUtil.getUserId());
		return scDeptMngDAO.selectScDeptListByManagerUserId(searchVO);
	}
}
