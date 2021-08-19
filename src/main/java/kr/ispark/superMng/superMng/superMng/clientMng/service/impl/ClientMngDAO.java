/*************************************************************************
* CLASS 명	: ClientMngDAO
* 작 업 자	: kimyh
* 작 업 일	: 2018. 07. 03.
* 기	능	: 고객사 관리 DAO
* ---------------------------- 변 경 이 력 --------------------------------
* 번호	작 업 자		작	업	일			변 경 내 용				비고
* ----	---------	----------------	---------------------	-----------
*	1	kimyh		2018. 07. 03.		최 초 작 업
**************************************************************************/
package kr.ispark.superMng.superMng.superMng.clientMng.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Repository;

import egovframework.com.cmm.EgovMessageSource;
import egovframework.com.cmm.service.impl.EgovComAbstractDAO;
import kr.ispark.common.exception.CustomException;
import kr.ispark.common.util.CommonUtil;
import kr.ispark.common.util.ContextHolder;
import kr.ispark.common.util.DataSourceType;
import kr.ispark.superMng.superMng.superMng.clientMng.service.ClientMngVO;

@Repository
public class ClientMngDAO extends EgovComAbstractDAO {
	@Resource(name="egovMessageSource")
	public EgovMessageSource egovMessageSource;

	/**
	* 고객사 목록 조회
	* @param	ClientMngVO searchVO
	* @return	List<ClientMngVO>
	* @throws	Exception
	*/
	public List<ClientMngVO> selectList(ClientMngVO searchVO) throws Exception {
		return selectList("superMng.superMng.superMng.clientMng.selectList", searchVO);
	}
	
	/**
	 * 고객사 목록 조회
	 * @param	ClientMngVO searchVO
	 * @return	List<ClientMngVO>
	 * @throws	Exception
	 */
	public List<ClientMngVO> selectTargetCompList(ClientMngVO searchVO) throws Exception {
		return selectList("superMng.superMng.superMng.clientMng.selectTargetCompList", searchVO);
	}

	/**
	* 고객사 목록 수
	* @param	ClientMngVO searchVO
	* @return	int
	* @throws	Exception
	*/
	public int selectListCount(ClientMngVO searchVO) throws Exception {
		return selectOne("superMng.superMng.superMng.clientMng.selectListCount", searchVO);
	}

	/**
	 * 서비스 중지/사용 처리
	 * @param	ClientMngVO dataVO
	 * @return	int
	 * @throws	Exception
	 */
	public int updateUseYn(ClientMngVO dataVO) throws Exception {
		return update("superMng.superMng.superMng.clientMng.updateUseYn", dataVO);
	}

	/**
	 * 서비스 초기화
	 * BSC_CLOUD의 SP_MAKE_DEFAULT_DATA를 DB 호환을 위해 일반 쿼리로 컨버팅했음
	 * @param	ClientMngVO dataVO
	 * @return	int
	 * @throws	Exception
	 */
	public void updateReset(ClientMngVO dataVO) throws Exception {
		
		// 먼저 newCompId에 대한  DB_ID 를 가져온다
		
		
		// 0. 공통코드 테이블에 현재 연도의 데이터가 없는 경우 연도를 현재 연도로 UPDATE
		String year = selectOne("superMng.superMng.superMng.clientMng.makeDefaultDataStep0_0", dataVO);
		dataVO.setYear(year);
		String maxYear = selectOne("superMng.superMng.superMng.clientMng.makeDefaultDataStep0_1", dataVO);
		dataVO.setMaxYear(maxYear);
		if(maxYear.compareTo(year) < 0) {
			update("superMng.superMng.superMng.clientMng.makeDefaultDataStep0_2", dataVO);
			update("superMng.superMng.superMng.clientMng.makeDefaultDataStep0_3", dataVO);
		}

		// 1. 회사정보 체크
		int cnt = selectOne("superMng.superMng.superMng.clientMng.makeDefaultDataStep1", dataVO);
		if(cnt < 1) {
			throw new CustomException(egovMessageSource.getMessage("info.nodata.msg"));
		}

		/* 메뉴 업무별 구분값
		 * 01 공통
		 * 02 조직성과
		 * 03 개인업적평가
		 * 04 개인역량평가
		 * 05 평가종합
		 *
		 * serviceType
		 * 1(조직성과) -> 01,02
		 * 2(개인업적) -> 01,03
		 * 3(개인역량) -> 01,04
		 * 4(평가종합) -> 01,05
		 */

		/* 공통코드 업무별 구분값
		 * 01 공통
		 * 02 조직성과
		 * 03 개인업적평가
		 * 04 개인역량평가
		 * 05 평가종합
		 *
		 * serviceType
		 * 1(조직성과) -> 01,02
		 * 2(개인업적) -> 01,03
		 * 3(개인역량) -> 01,03,04
		 * 4(평가종합) -> 01,03,04,05
		 */
		
		/*
		List<String> codeServiceTypeList = new ArrayList<String>(0);
		List<String> menuServiceTypeList = new ArrayList<String>(0);
		String serviceType = selectOne("superMng.superMng.superMng.clientMng.selectServiceTypeData", dataVO);
		if(!"".equals(CommonUtil.nullToBlank(serviceType))){
			String[] serviceTypsStrings = serviceType.split(",",0);
			if(serviceTypsStrings != null && serviceTypsStrings.length > 0){
				codeServiceTypeList.add("01");
				menuServiceTypeList.add("01");
				for(String type : serviceTypsStrings){
					if("1".equals(CommonUtil.nullToBlank(type))){
						codeServiceTypeList.add("02");
						menuServiceTypeList.add("02");
					}else if("2".equals(CommonUtil.nullToBlank(type))){
						codeServiceTypeList.add("03");
						codeServiceTypeList.add("04");
						menuServiceTypeList.add("03");
					}else if("3".equals(CommonUtil.nullToBlank(type))){
						codeServiceTypeList.add("03");
						codeServiceTypeList.add("04");
						menuServiceTypeList.add("04");
					}else if("4".equals(CommonUtil.nullToBlank(type))){
						codeServiceTypeList.add("03");
						codeServiceTypeList.add("04");
						codeServiceTypeList.add("05");
						menuServiceTypeList.add("05");
					}
				}
			}
		}

		dataVO.setCodeServiceTypes(codeServiceTypeList);
		dataVO.setMenuServiceTypes(menuServiceTypeList);
		*/

		// 2. 기존 데이터 삭제
		delete("superMng.superMng.superMng.clientMng.makeDefaultDataStep2", dataVO);
		/*
		delete("superMng.superMng.superMng.clientMng.makeDefaultDataStep2_1", dataVO);
		delete("superMng.superMng.superMng.clientMng.makeDefaultDataStep2_1_1", dataVO);
		delete("superMng.superMng.superMng.clientMng.makeDefaultDataStep2_1_2", dataVO);
		delete("superMng.superMng.superMng.clientMng.makeDefaultDataStep2_2", dataVO);
		delete("superMng.superMng.superMng.clientMng.makeDefaultDataStep2_3", dataVO);
		delete("superMng.superMng.superMng.clientMng.makeDefaultDataStep2_4", dataVO);
		delete("superMng.superMng.superMng.clientMng.makeDefaultDataStep2_5", dataVO);
		delete("superMng.superMng.superMng.clientMng.makeDefaultDataStep2_6", dataVO);
		delete("superMng.superMng.superMng.clientMng.makeDefaultDataStep2_7", dataVO);
		delete("superMng.superMng.superMng.clientMng.makeDefaultDataStep2_8", dataVO);
		delete("superMng.superMng.superMng.clientMng.makeDefaultDataStep2_9", dataVO);
		delete("superMng.superMng.superMng.clientMng.makeDefaultDataStep2_10", dataVO);
		delete("superMng.superMng.superMng.clientMng.makeDefaultDataStep2_11", dataVO);
		delete("superMng.superMng.superMng.clientMng.makeDefaultDataStep2_12", dataVO);
		delete("superMng.superMng.superMng.clientMng.makeDefaultDataStep2_13", dataVO);
		delete("superMng.superMng.superMng.clientMng.makeDefaultDataStep2_14", dataVO);
		delete("superMng.superMng.superMng.clientMng.makeDefaultDataStep2_15", dataVO);
		delete("superMng.superMng.superMng.clientMng.makeDefaultDataStep2_16", dataVO); // DASHBOARD ITEMS
		delete("superMng.superMng.superMng.clientMng.makeDefaultDataStep2_17", dataVO);
		*/

		// 3. 공통코드 복사
		insert("superMng.superMng.superMng.clientMng.makeDefaultDataStep3_1", dataVO);
		insert("superMng.superMng.superMng.clientMng.makeDefaultDataStep3_2", dataVO);

		// 4. 메뉴 복사
		insert("superMng.superMng.superMng.clientMng.makeDefaultDataStep4", dataVO);

		// 5. 권한 복사
		insert("superMng.superMng.superMng.clientMng.makeDefaultDataStep5", dataVO);

		// 6. 다국어 관련 테이블 복사
		List<String> langList = selectList("superMng.superMng.superMng.clientMng.makeDefaultDataStep6_1", dataVO);
		for(String lang : langList) {
			dataVO.setLang(lang);
			insert("superMng.superMng.superMng.clientMng.makeDefaultDataStep6_2", dataVO);
			insert("superMng.superMng.superMng.clientMng.makeDefaultDataStep6_3", dataVO);
			insert("superMng.superMng.superMng.clientMng.makeDefaultDataStep6_4", dataVO);
		}

		// 7. 시스템 관리자 생성
		insert("superMng.superMng.superMng.clientMng.makeDefaultDataStep7", dataVO);

		// 8. 관리자용 최상위 부서 임의 생성
		insert("superMng.superMng.superMng.clientMng.makeDefaultDataStep8_1", dataVO);
		insert("superMng.superMng.superMng.clientMng.makeDefaultDataStep8_2", dataVO);
		insert("superMng.superMng.superMng.clientMng.makeDefaultDataStep8_3", dataVO);

		// 9. 시스템 관리자에 권한 부여
		insert("superMng.superMng.superMng.clientMng.makeDefaultDataStep9", dataVO);

		// 10. 데이터 집계 프로시저 목록
		insert("superMng.superMng.superMng.clientMng.makeDefaultDataStep10", dataVO);

		// 11. 득점산식
		insert("superMng.superMng.superMng.clientMng.makeDefaultDataStep11", dataVO);

		// 12. 등급설정
		insert("superMng.superMng.superMng.clientMng.makeDefaultDataStep12", dataVO);

		// 13. 평가방법 관리
		insert("superMng.superMng.superMng.clientMng.makeDefaultDataStep13", dataVO);

		// 14. 시퀀스
		insert("superMng.superMng.superMng.clientMng.makeDefaultDataStep14", dataVO);

		// 15. DASHBOARD ITEMS
		insert("superMng.superMng.superMng.clientMng.makeDefaultDataStep15", dataVO);

		// 16. 게시판 설정
		insert("superMng.superMng.superMng.clientMng.makeDefaultDataStep16", dataVO);
		
		// 17_1. 프로세스 흐름도
		insert("superMng.superMng.superMng.clientMng.makeDefaultDataStep17_1", dataVO);
		
		// 17_2. 프로세스 흐름도 언어
		insert("superMng.superMng.superMng.clientMng.makeDefaultDataStep17_2", dataVO);

		// 20. 기초 데이터 생성 여부 update
		update("superMng.superMng.superMng.clientMng.makeDefaultDataStep20", dataVO);
	}
	
	/**
	 * 데모데이터 복사
	 * 1. 해당 고객사 데이터 모두 삭제 
	 * 2. 데모대상 고객사 데이터 봇사
	 * 3. 모든 테이블의 comp_id 를 해당고객사의 comp_id 로 변경 - 추후 comp_id컬럼이 삭제되면 모두 삭제 할 예정
	 * 4-1. 촤상위 무서명을 해당고객사명으로 변경
	 * 4-2. 전체관리자 id, passwd 변경
	 * 4-3. 퀀한테이블에 관리자 id 변경  
	 * @param	ClientMngVO dataVO
	 * @return	int
	 * @throws	Exception
	 */
	public void updateDemoReset(ClientMngVO dataVO) throws Exception {
		
		/*테이블 목록을 조회 후 테이블 삭제 */
		List<String> tableNameList = selectList("superMng.superMng.superMng.clientMng.selectTableList",dataVO);
		if(tableNameList != null && tableNameList.size() > 0){
			for(String tableName : tableNameList){
				dataVO.setTableName(tableName);
				//delete
				delete("superMng.superMng.superMng.clientMng.deleteTableData",dataVO);
				//insert
				insert("superMng.superMng.superMng.clientMng.insertDemoTableData",dataVO);
				//update comp_id = #{newCompId}
				update("superMng.superMng.superMng.clientMng.updateDemoTableData",dataVO);
			}
			
			//1.성과조직 최상위부서 명칭 update
			update("superMng.superMng.superMng.clientMng.updateDemoData01",dataVO);
			//2.실조직 최상위부서 명칭 update
			update("superMng.superMng.superMng.clientMng.updateDemoData02",dataVO);
			//3.deptscore 최상위부서 명칭 update
			update("superMng.superMng.superMng.clientMng.updateDemoData03",dataVO);
			//4.metricscore 최상위부서 명칭 update
			update("superMng.superMng.superMng.clientMng.updateDemoData04",dataVO);
			//5.BSC_DASHBOARD_ITEM_USER 사용자id update
			update("superMng.superMng.superMng.clientMng.updateDemoData05",dataVO);
			//6.user_info 사용자id update
			if(update("superMng.superMng.superMng.clientMng.updateDemoData06",dataVO) == 0){
				insert("superMng.superMng.superMng.clientMng.makeDefaultDataStep7", dataVO);
			}
			//7.admin_gubun 사용자id update
			if(update("superMng.superMng.superMng.clientMng.updateDemoData07",dataVO) == 0){
				insert("superMng.superMng.superMng.clientMng.makeDefaultDataStep9", dataVO);
			}
		}
		
	}
	
	/**
	 * 언어 목록 조회
	 * @param	ClientMngVO searchVO
	 * @return	List<ClientMngVO>
	 * @throws	Exception
	 */
	public List<ClientMngVO> langList(ClientMngVO searchVO) throws Exception {
		return selectList("superMng.superMng.superMng.clientMng.selectLang", searchVO);
	}

	/**
	 * 상세 조회
	 * @param	ClientMngVO searchVO
	 * @return	ClientMngVO
	 * @throws	Exception
	 */
	public ClientMngVO selectDetail(ClientMngVO searchVO) throws Exception {
		return selectOne("superMng.superMng.superMng.clientMng.selectDetail", searchVO);
	}

	/**
	 * 이력수 조회
	 * @param	ClientMngVO searchVO
	 * @return	int
	 * @throws	Exception
	 */
	public int selectHistoryCount(ClientMngVO searchVO) throws Exception {
		return selectOne("superMng.superMng.superMng.clientMng.selectHistoryCount", searchVO);
	}

	/**
	 * 이력 조회
	 * @param	ClientMngVO searchVO
	 * @return	List<ClientMngVO>
	 * @throws	Exception
	 */
	public List<ClientMngVO> selectHistory(ClientMngVO searchVO) throws Exception {
		return selectList("superMng.superMng.superMng.clientMng.selectHistory", searchVO);
	}

	/**
	 * ID 중복 체크
	 * @param	ClientMngVO searchVO
	 * @return	int
	 * @throws	Exception
	 */
	public int selectIdCnt(ClientMngVO searchVO) throws Exception{
		return selectOne("superMng.superMng.superMng.clientMng.selectIdCnt", searchVO);
	}

	/**
	 * 사용 언어 정보 삭제
	 * @param	ClientMngVO dataVO
	 * @return	int
	 * @throws	Exception
	 */
	public int deleteLang(ClientMngVO dataVO) throws Exception {
		return delete("superMng.superMng.superMng.clientMng.deleteLang", dataVO);
	}

	/**
	 * 사용 언어 정보 저장
	 * @param	ClientMngVO dataVO
	 * @return	int
	 * @throws	Exception
	 */
	public int insertLang(ClientMngVO dataVO) throws Exception {
		return insert("superMng.superMng.superMng.clientMng.insertLang", dataVO);
	}
	
	/**
	 * 저장
	 * @param	ClientMngVO dataVO
	 * @return	int
	 * @throws	Exception
	 */
	public int deleteDbData(ClientMngVO dataVO) throws Exception {
		return delete("superMng.superMng.superMng.clientMng.deleteDbData", dataVO);
	}
	
	/**
	 * 저장
	 * @param	ClientMngVO dataVO
	 * @return	int
	 * @throws	Exception
	 */
	public int insertDbData(ClientMngVO dataVO) throws Exception {
		return insert("superMng.superMng.superMng.clientMng.insertDbData", dataVO);
	}

	/**
	 * 저장
	 * @param	ClientMngVO dataVO
	 * @return	int
	 * @throws	Exception
	 */
	public int insertData(ClientMngVO dataVO) throws Exception {
		return insert("superMng.superMng.superMng.clientMng.insertData", dataVO);
	}

	/**
	 * 수정
	 * @param	ClientMngVO dataVO
	 * @return	int
	 * @throws	Exception
	 */
	public int updateData(ClientMngVO dataVO) throws Exception {
		return update("superMng.superMng.superMng.clientMng.updateData", dataVO);
	}

	/**
	 * 계약 정보의 compId 수정
	 * @param	ClientMngVO dataVO
	 * @return	int
	 * @throws	Exception
	 */
	public int updateContractCompId(ClientMngVO dataVO) throws Exception {
		return update("superMng.superMng.superMng.clientMng.updateContractCompId", dataVO);
	}

	/**
	 * 사용하는 언어의 메뉴, 공통코드 생성
	 * BSC_CLOUD의 SP_MAKE_LANG_DATA를 DB 호환을 위해 일반 쿼리로 컨버팅했음
	 * @param	ClientMngVO dataVO
	 * @return	int
	 * @throws	Exception
	 */
	public int updateLangData(ClientMngVO dataVO) throws Exception {
		int resultCnt = 0;
		if(dataVO.getIsSuper().equals("N")) {
			// 1. 사용하지 않는 언어의 메뉴 데이터 삭제
			delete("superMng.superMng.superMng.clientMng.makeLangDataStep1", dataVO);
			// 2. 추가된 언어의 메뉴 데이터 생성
			insert("superMng.superMng.superMng.clientMng.makeLangDataStep2", dataVO);
		}
		// 3. 사용하지 않는 언어의 공통코드 데이터 삭제
		delete("superMng.superMng.superMng.clientMng.makeLangDataStep3", dataVO);
		delete("superMng.superMng.superMng.clientMng.makeLangDataStep4", dataVO);

		// 4. 추가된 언어의 공통코드 데이터 생성
		resultCnt += insert("superMng.superMng.superMng.clientMng.makeLangDataStep5", dataVO);
		resultCnt += insert("superMng.superMng.superMng.clientMng.makeLangDataStep6", dataVO);
		
		// 5. 추가된 언어의 프로세스 데이터 생성
		delete("superMng.superMng.superMng.clientMng.makeLangDataStep7", dataVO);
		
		// 6. 추가된 언어의 프로세스 데이터 생성
		resultCnt += insert("superMng.superMng.superMng.clientMng.makeLangDataStep8", dataVO);

		return resultCnt;
	}

	/**
	 * 계약 수정
	 * @param	ClientMngVO dataVO
	 * @return	int
	 * @throws	Exception
	 */
	public int updateContract(ClientMngVO dataVO) throws Exception {
		return update("superMng.superMng.superMng.clientMng.updateContract", dataVO);
	}

	/**
	 * 계약 이력 CURRNET SEQ 수정
	 * @param	ClientMngVO dataVO
	 * @return	int
	 * @throws	Exception
	 */
	public int updateCurrentSeq(ClientMngVO dataVO) throws Exception {
		return update("superMng.superMng.superMng.clientMng.updateCurrentSeq", dataVO);
	}

	/**
	 * 계약 저장
	 * @param	ClientMngVO dataVO
	 * @return	int
	 * @throws	Exception
	 */
	public int insertContract(ClientMngVO dataVO) throws Exception {
		return insert("superMng.superMng.superMng.clientMng.insertContract", dataVO);
	}
	
	public HashMap<String,String> selectSourceDataBaseInfo(ClientMngVO dataVO) throws Exception {
		return selectOne("superMng.superMng.superMng.clientMng.selectSourceDataBaseInfo", dataVO);
	}
	
	public HashMap<String,String> selectTargetDataBaseInfo(ClientMngVO dataVO) throws Exception {
		return selectOne("superMng.superMng.superMng.clientMng.selectTargetDataBaseInfo", dataVO);
	}
}
