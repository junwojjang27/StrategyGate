/*************************************************************************
* CLASS 명	: CompDeptMngServiceImpl
* 작 업 자	: kimyh
* 작 업 일	: 2018. 6. 29.
* 기	능	: 조직관리 Service
* ---------------------------- 변 경 이 력 --------------------------------
* 번호	작 업 자		작	업	일			변 경 내 용				비고
* ----	---------	----------------	---------------------	-----------
*	1	kimyh		2018. 6. 29.		최 초 작 업
**************************************************************************/
package kr.ispark.system.system.comp.compDeptMng.service.impl;

import java.io.InputStream;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import egovframework.com.cmm.EgovMessageSource;
import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;
import kr.ispark.common.CommonVO;
import kr.ispark.common.exception.CustomException;
import kr.ispark.common.system.service.ScDeptVO;
import kr.ispark.common.util.CommonUtil;
import kr.ispark.common.util.ExcelParser;
import kr.ispark.common.util.PropertyUtil;
import kr.ispark.common.util.SessionUtil;

@Service
public class CompDeptMngServiceImpl extends EgovAbstractServiceImpl {

	@Resource(name="egovMessageSource")
	public EgovMessageSource egovMessageSource;

	@Resource
	private CompDeptMngDAO compDeptMngDAO;

	@Autowired
	private ExcelParser excelParser;

	// 조직관리 목록 조회
	public List<ScDeptVO> selectList(ScDeptVO searchVO) throws Exception {
		/*
		 * 하위 조직이 존재하면 : 현재의 조직과 하위 조직 조회
		 * 하위 조직이 존재하지 않으면 : 현재의 조직의 상위 조직과 그 하위 조직들 조회
		 */
		if(!searchVO.getFindScDeptId().equals(PropertyUtil.getProperty("default.rootScDeptId"))
				&& compDeptMngDAO.selectSubScDeptCount(searchVO) == 0) {
			searchVO.setFindScDeptId(compDeptMngDAO.selectUpScDeptId(searchVO));
		}

		return compDeptMngDAO.selectList(searchVO);
	}

	// 조직관리 목록 조회
	public List<ScDeptVO> selectExcelList(ScDeptVO searchVO) throws Exception {
		return compDeptMngDAO.selectExcelList(searchVO);
	}

	/**
	 * 등급배분표 엑셀 업로드
	 * @param	EvalGradeCntVO searchVO
	 * @param	InputStream f
	 * @return	int
	 * @throws	Exception
	 */
	public int excelUploadProcess(ScDeptVO dataVO, InputStream f) throws Exception {

		int resultCnt = 0;
		List<ScDeptVO> list
			= excelParser.excelToList(f,
					3,	// 몇 번째 row부터 읽을 것인가. 0부터 시작.
					ScDeptVO.class,	// row를 파싱할 class 타입
					new String[] {	// excel의 각 열을 매칭할 vo의 변수명 (열 순서대로)
						"scDeptId",			"scDeptNm",		"upScDeptId",
						"managerUserId",	"bscUserId",	"useYn",
						"sortOrder"
					},
					new int[] {	// 각 열 별 속성. 뒤에 _NOTNULL이 있으면 유효성 검사시 필수값 체크를 수행함
						ExcelParser.CELL_TYPE_STRING_NOTNULL, ExcelParser.CELL_TYPE_STRING_NOTNULL, ExcelParser.CELL_TYPE_STRING,
						ExcelParser.CELL_TYPE_STRING, ExcelParser.CELL_TYPE_STRING, ExcelParser.CELL_TYPE_STRING,
						ExcelParser.CELL_TYPE_NUMBER
					}
			);

		// 조직 전체 삭제
		//compDeptMngDAO.deleteScDeptAll(dataVO);

		// 최상위 조직 수
		int rootCnt = 0;

		for(ScDeptVO paramVO : list) {
			paramVO.setYear(dataVO.getYear());
			int deptCnt = compDeptMngDAO.selectDeptCnt(paramVO);
			
			// 조직코드가 rootScDeptId인 경우 null 처리. null로 안할경우 공백으로 들어가고, 
			if(paramVO.getScDeptId().equals(PropertyUtil.getProperty("default.rootScDeptId"))
					&& CommonUtil.isEmpty(paramVO.getUpScDeptId())) {
				paramVO.setUpScDeptId(null);
			}
			
			if(deptCnt>0){
				resultCnt += compDeptMngDAO.updateOrinData(paramVO);
			}else{
				resultCnt += compDeptMngDAO.insertData(paramVO);
			}

			if(paramVO.getScDeptId().equals(PropertyUtil.getProperty("default.rootScDeptId"))
				&& CommonUtil.isEmpty(paramVO.getUpScDeptId())) {
				rootCnt++;
			}

			// 조직코드가 D000001이 아니면서 상위조직을 입력하지 않은 경우 조직 재정렬할 때 조회가 안됨.
			if(!paramVO.getScDeptId().equals(PropertyUtil.getProperty("default.rootScDeptId"))
				&& CommonUtil.isEmpty(paramVO.getUpScDeptId())) {
				throw new CustomException(egovMessageSource.getMessage("common.required.msg", new String[] {egovMessageSource.getMessage("word.upOrg")}) + " (" + egovMessageSource.getMessage("word.orgNm") + " : " + paramVO.getScDeptNm() + ")");
			}
		}

		if(rootCnt == 0) {
			throw new CustomException(egovMessageSource.getMessage("system.system.comp.compDeptMng.error1", new String[] {PropertyUtil.getProperty("default.rootScDeptId")}));
		}

		if(resultCnt > 0) {
			updateReDefineData(dataVO);
			compDeptMngDAO.insertDeptinfoAndMapping(dataVO);
			insertAuth(dataVO);
		}

		return resultCnt;
	}

	// 조직관리 일괄저장
	public int saveAll(ScDeptVO dataVO) throws Exception {
		int resultCnt = 0;
		for(ScDeptVO paramVO : dataVO.getGridDataList()) {
			if(paramVO.getIsNew().equals("Y")) {
				paramVO.setScDeptId(compDeptMngDAO.selectNewScDeptId(dataVO));
				resultCnt += compDeptMngDAO.insertData(paramVO);
			} else {
				resultCnt += compDeptMngDAO.updateData(paramVO);
			}

			if(paramVO.getResetAllYn().equals("Y")) {
				compDeptMngDAO.updateMetricUserId(paramVO);
			}
		}

		if(resultCnt > 0){
			//updateReDefineData(dataVO);
			List<ScDeptVO> redefineList = compDeptMngDAO.selectReDefineData(dataVO);

			if(redefineList != null && 0<redefineList.size()){
				for(ScDeptVO vo:redefineList){
					//vo.setRealSortOrder(order++);
					compDeptMngDAO.updateReDefineData(vo);
					
				}
			}
			compDeptMngDAO.insertDeptinfoAndMapping(dataVO);
			insertAuth(dataVO);
		}

		return resultCnt;
	}

	// 조직 삭제
	public int deleteCompDeptMng(ScDeptVO dataVO) throws Exception {
		int resultCnt = 0;

		// 지표가 등록된 조직이 있으면 처리하지 않음
		if(compDeptMngDAO.selectMetricCountList(dataVO).size() > 0) {
			return 0;
		}

		resultCnt = compDeptMngDAO.deleteCompDeptMng(dataVO);

		if(0<resultCnt){
			updateReDefineData(dataVO);
			compDeptMngDAO.insertDeptinfoAndMapping(dataVO);
			insertAuth(dataVO);
		}

		return resultCnt;
	}

	// 권한 갱신
	public void insertAuth(ScDeptVO dataVO) throws Exception {
		compDeptMngDAO.insertAuth(dataVO);
	}

	public void updateReDefineData(ScDeptVO dataVO) throws Exception {

		//int order = 1;
		List<ScDeptVO> redefineList = compDeptMngDAO.selectReDefineData(dataVO);

		if(redefineList != null && 0<redefineList.size()){
			for(ScDeptVO vo:redefineList){
				//vo.setRealSortOrder(order++);
				compDeptMngDAO.updateReDefineData(vo);
				
			}
		}
	}

	/**
	 * 지표담당자용 조직 조회
	 * @param	CommonVO searchVO
	 * @return	List<ScDeptVO>
	 * @throws	Exception
	 */
	public List<ScDeptVO> selectScDeptListByBscUserId(CommonVO searchVO) throws Exception {
		searchVO.setUserId(SessionUtil.getUserId());
		return compDeptMngDAO.selectScDeptListByBscUserId(searchVO);
	}

	/**
	 * 부서장용 조직 조회
	 * @param	CommonVO searchVO
	 * @return	List<ScDeptVO>
	 * @throws	Exception
	 */
	public List<ScDeptVO> selectScDeptListByManagerUserId(CommonVO searchVO) throws Exception {
		searchVO.setUserId(SessionUtil.getUserId());
		return compDeptMngDAO.selectScDeptListByManagerUserId(searchVO);
	}
}
