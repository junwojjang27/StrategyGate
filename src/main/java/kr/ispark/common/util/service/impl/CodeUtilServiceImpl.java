/*************************************************************************
* CLASS 명  	: CodeUtilService
* 작 업 자  	: 박재현
* 작 업 일  	: 2009.07.15
* 기    능  	: 코드유틸
* ---------------------------- 변 경 이 력 --------------------------------
* 번호  작 업 자     작   업   일         변 경 내 용                 비고
* ----  --------  -----------------  -------------------------    --------
*   1    박재현		 2009.07.15			  최 초 작 업
*   2    하윤식		 2012.06.25			  코드리스트반환수정
**************************************************************************/
package kr.ispark.common.util.service.impl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;
import egovframework.rte.psl.dataaccess.util.EgovMap;
import kr.ispark.common.util.CodeUtil;
import kr.ispark.common.util.PropertyUtil;
import kr.ispark.common.util.SessionUtil;
import kr.ispark.common.util.service.CodeVO;

@Service
public class CodeUtilServiceImpl extends EgovAbstractServiceImpl {

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	@Resource
	private CodeUtilDAO codeUtilDao;

	/**
	 * reload 공통코드
	 * @param keyword
	 * @return HashMap<String, ArrayList<CodeUtilVO>>
	*/
	/*
	public HashMap<String, HashMap<String, ArrayList<CodeVO>>> reLoadCodeHash(HashMap<String, HashMap<String, ArrayList<CodeVO>>> map, CodeVO vo) {
		/**********************************
		 * 회사코드_언어 별로 코드를 관리
		 *********************************
		HashMap<String, ArrayList<CodeVO>> compIdMap = null;
		ArrayList<CodeVO> subGrpList = null;
		ArrayList<CodeVO> subCodeList = null;
		String codeGrpId = "";
		String compId = null;
		CodeVO codeVO = null;
		StringBuffer debugSb = new StringBuffer();
		
		/*계정별 공통코드를 모아야 하므로...
		
		/**************************

		try {
			// 전체 database 조회
			ArrayList<CodeVO> dbList = (ArrayList<CodeVO>)codeUtilDao.getDbIdList(vo);
			
			ArrayList<CodeVO> grpList = new ArrayList<CodeVO>(0);
			ArrayList<CodeVO> codeList = new ArrayList<CodeVO>(0);
			
			ArrayList<CodeVO> loopGrpList = new ArrayList<CodeVO>(0);
			ArrayList<CodeVO> loopCodeList = new ArrayList<CodeVO>(0);
			
			if(dbList != null && dbList.size()>0){
				for(CodeVO loopVo : dbList){
					try{
						loopGrpList = (ArrayList<CodeVO>)codeUtilDao.getCodeGrpList(loopVo);
					}catch(SQLException e){
						e.getCause();
					}
					grpList.addAll(loopGrpList);
					
					try{
						loopCodeList = (ArrayList<CodeVO>)codeUtilDao.getCodeList(loopVo);
					}catch(SQLException e){
						e.getCause();
					}
					codeList.addAll(loopCodeList);
				}
			}
			
			
			// 전체 코드 그룹 목록
			ArrayList<CodeVO> grpList = (ArrayList<CodeVO>)codeUtilDao.getCodeGrpList(vo);
			// 전체 코드 상세 목록
			ArrayList<CodeVO> codeList = (ArrayList<CodeVO>)codeUtilDao.getCodeList(vo);
			

			if(grpList != null) {
				int grpListSize = grpList.size();
				int codeListSize = codeList.size();
				int idx = 0;
				if(grpListSize > 0) {

					debugSb.append("\n\n#######################\n");
					// 전체 코드를 새로 로딩할 경우
					if(vo == null || vo.getCompId() == null) {
						debugSb.append("# Reloading all of common-codes	\n");

						map = new HashMap<String, HashMap<String, ArrayList<CodeVO>>>();
					} else {
						// 특정 회사만 변경할 경우 map에서 해당 compId로 시작되는 key들의 값을 리셋
						Set<String> set = map.keySet();
						Iterator<String> it = set.iterator();
						String key;
						while(it.hasNext()) {
							key = it.next();
							if(key.startsWith(vo.getCompId() + "_")) {
								debugSb.append("# Reloading common-codes of " + key + "\n");
								map.put(key, new HashMap<String, ArrayList<CodeVO>>());
							}
						}
					}
					debugSb.append("#######################\n\n");
					log.debug(debugSb.toString());

					
					 * 전체 코드 그룹을 조회하며 "compId_lang"별로 구분하여 map에 저장
					 * 각 map은 코드그룹별 코드 목록을 ArrayList에 넣어 map에 저장
					 
					for(CodeVO grpVO : grpList) {
						compId = grpVO.getCompId();	// compId_lang
						compIdMap = map.get(compId);
						if(compIdMap == null) {
							compIdMap = new HashMap<>();
							map.put(compId, compIdMap);
						}

						subGrpList = compIdMap.get("CODE_GRP_LIST");
						if(subGrpList == null) {
							subGrpList = new ArrayList<CodeVO>();
							compIdMap.put("CODE_GRP_LIST", subGrpList);
						}
						subGrpList.add(grpVO);

						for(; idx<codeListSize; idx++) {
							codeVO = codeList.get(idx);

							codeGrpId = grpVO.getCodeGrpId();
							if(!codeGrpId.equals(codeVO.getCodeGrpId())) break;

							subCodeList = compIdMap.get(codeGrpId);
							if(subCodeList == null) {
								subCodeList = new ArrayList<CodeVO>();
								compIdMap.put(grpVO.getCodeGrpId(), subCodeList);
							}
							subCodeList.add(codeVO);
						}
					}
				}
			}
		} catch (SQLException e) {
			log.error("SQL 트랜잭션 오류 : " + e.getCause());
			map = null;
		} catch (Exception ex) {
			log.error("error : "+ex.getCause());
			map = null;
		}

		return map;
	}

	*/

	/**
	 * 최근 10분 사이에 공통코드가 변경된 compId를 조회해서 리로드
	 * @return	List<String>
	 * @throws	Exception
	 */
	public void selectUpdateCompIdList() throws Exception {
		List<String> compIdList = codeUtilDao.selectUpdateCompIdList();

		// TEMPLATE나 전체 관리자가 수정한 경우 전체 코드 리로드
		if(compIdList.indexOf(PropertyUtil.getProperty("Template.CompId")) > -1
				|| compIdList.indexOf(PropertyUtil.getProperty("Super.CompId")) > -1) {
			//CodeUtil.reLoadCodeHash(null);
		} else {
			// 그 외엔 해당 업체의 코드만 리로드
			for(String compId : compIdList) {
				//CodeUtil.reLoadCodeHash(compId);
			}
		}
	}

	/**
	 * 공통코드 변경 이력 등록
	 * @param	EgovMap map
	 * @return	int
	 * @throws	Exception
	 */
	public int insertCodeUpdateLog() throws Exception {
		EgovMap map = new EgovMap();
		return insertCodeUpdateLog(map);
	}
	public int insertCodeUpdateLog(String compId) throws Exception {
		EgovMap map = new EgovMap();
		return insertCodeUpdateLog(map);
	}
	public int insertCodeUpdateLog(EgovMap map) throws Exception {
		return codeUtilDao.insertCodeUpdateLog(map);
	}
	
	public List<CodeVO> selectDBCodeList(EgovMap emap){
		
		try{
			return codeUtilDao.selectDBCodeList(emap);
		}catch(SQLException sqe){
			sqe.getCause();
			return null;
		}catch(Exception e){
			e.getCause();
			return null;
		}
	}
	
	public String selectDBCodeGrpDetail(EgovMap emap){
		
		try{
			return codeUtilDao.selectDBCodeGrpDetail(emap);
		}catch(SQLException sqe){
			sqe.getCause();
			return null;
		}catch(Exception e){
			e.getCause();
			return null;
		}
	}
	
	public String selectDBCodeDetail(EgovMap emap){
		
		try{
			return codeUtilDao.selectDBCodeDetail(emap);
		}catch(SQLException sqe){
			sqe.getCause();
			return null;
		}catch(Exception e){
			e.getCause();
			return null;
		}
		
	}
}
