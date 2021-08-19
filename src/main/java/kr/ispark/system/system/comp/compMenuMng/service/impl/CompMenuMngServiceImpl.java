/*************************************************************************
* CLASS 명	: MenuMngServiceIpml
* 작 업 자	: joey
* 작 업 일	: 2018-1-7
* 기	능	: 메뉴관리 ServiceIpml
* ---------------------------- 변 경 이 력 --------------------------------
* 번호	작 업 자		작	업	일			변 경 내 용				비고
* ----	---------	----------------	---------------------	-----------
*	1	joey		2018-1-7
**************************************************************************/
package kr.ispark.system.system.comp.compMenuMng.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import kr.ispark.common.system.service.impl.IdGenServiceImpl;
import kr.ispark.common.util.CommonUtil;
import kr.ispark.system.system.comp.compMenuMng.service.CompMenuMngVO;

import org.springframework.stereotype.Service;

import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;

@Service
public class CompMenuMngServiceImpl extends EgovAbstractServiceImpl {

	@Resource
	private IdGenServiceImpl idgenService;
	
	@Resource
	private CompMenuMngDAO compMenuMngDAO;
	
	// 메뉴관리관리 목록 조회
	public List<CompMenuMngVO> selectRoleList(CompMenuMngVO searchVO) throws Exception {
		return compMenuMngDAO.selectRoleList(searchVO);
	}

	// 메뉴관리관리 목록 조회
	public List<CompMenuMngVO> selectList(CompMenuMngVO searchVO) throws Exception {
		
		List<CompMenuMngVO> authPgmList = compMenuMngDAO.selectAuthPgmList(searchVO);
		
		//메뉴별 권한정보 담는 파라미터 맵
		HashMap<String,String> pgmAuthMap = new HashMap<String,String>();
		HashMap<String,String> pgmAuthNmMap = new HashMap<String,String>();
		
		if(authPgmList != null && 0<authPgmList.size()){
			int size = authPgmList.size();
			String authGubuns = "";
			String authGubunNms = "";
			String lastPgmId = "";
			for(int idx=0 ; idx<size ; idx++){
				
				CompMenuMngVO setVO = (CompMenuMngVO)authPgmList.get(idx);
				
				if("".equals(lastPgmId)){
					lastPgmId = setVO.getPgmId();
					authGubuns += setVO.getAuthGubun();
					authGubunNms += setVO.getAuthGubunNm();
				}else if(lastPgmId.equals(setVO.getPgmId())){
					authGubuns += ","+setVO.getAuthGubun();
					authGubunNms += ","+setVO.getAuthGubunNm();
				}else{	
					pgmAuthMap.put(lastPgmId, authGubuns);
					pgmAuthNmMap.put(lastPgmId, authGubunNms);
					lastPgmId = setVO.getPgmId();
					authGubuns = "";
					authGubunNms = "";
					authGubuns += setVO.getAuthGubun();
					authGubunNms += setVO.getAuthGubunNm();
				}
				
				if(idx == size-1){
					pgmAuthMap.put(lastPgmId, authGubuns);
					pgmAuthNmMap.put(lastPgmId, authGubunNms);
				}
			}
		}
		
		//lang 정보를 담고있는 String 배열
		List<CompMenuMngVO> langList = selectLangList(searchVO);
		
		searchVO.setPgmAuthMap(pgmAuthMap);
		searchVO.setPgmAuthNmMap(pgmAuthNmMap);
		searchVO.setLangList(langList);
		
		List<CompMenuMngVO> selectList = new ArrayList<CompMenuMngVO>(0);
		if(0<langList.size()){
			selectList = compMenuMngDAO.selectList(searchVO);
		}
		
		return selectList;
	}
	
	// 메뉴관리 상세 조회
	public List<CompMenuMngVO> selectLangList(CompMenuMngVO searchVO) throws Exception {
		return compMenuMngDAO.selectLangList(searchVO);
	}

	// 메뉴관리 저장
	public int saveData(CompMenuMngVO dataVO) throws Exception {
		
		List<CompMenuMngVO> gridDataList = dataVO.getGridDataList();
		int resultCnt = 0;
		String key = "";
		String[] auths;
		
		String[] pgmNmLangs;
		String pgmNmLang;
		String pgmNm;
		
		if(gridDataList != null && 0 < gridDataList.size()){
			for(CompMenuMngVO vo: gridDataList ){
				
				if(CommonUtil.isEmpty(vo.getPgmId())) {
					key = idgenService.selectNextSeq("COM_PGM", "PGM", 4, "0");
					vo.setPgmId(key);
					
					resultCnt +=  compMenuMngDAO.insertData(vo);
					
					pgmNmLangs = vo.getPgmNmLang().split("\\$\\%\\^", 0);
					if(pgmNmLangs != null && pgmNmLangs.length>0){
						for(String pgmNmLangString:pgmNmLangs){
							String[] pgmNmLangStrings = pgmNmLangString.split("\\#\\$\\%", 0);
							if(pgmNmLangStrings != null && pgmNmLangStrings.length>1){
								pgmNmLang = pgmNmLangStrings[0];
								pgmNm = pgmNmLangStrings[1];
								if(pgmNm!= null){
									vo.setInputLang(pgmNmLang);
									vo.setPgmNm(pgmNm);
									resultCnt += compMenuMngDAO.insertPgmNmData(vo);
								}	
							}
						}
					}
				} else {
					resultCnt += compMenuMngDAO.updateData(vo);
					resultCnt += compMenuMngDAO.deletePgmNmData(vo);
					
					pgmNmLangs = vo.getPgmNmLang().split("\\$\\%\\^", 0);
					if(pgmNmLangs != null && pgmNmLangs.length>0){
						for(String pgmNmLangString:pgmNmLangs){
							String[] pgmNmLangStrings = pgmNmLangString.split("\\#\\$\\%", 0);
							if(pgmNmLangStrings != null && pgmNmLangStrings.length>1){
								pgmNmLang = pgmNmLangStrings[0];
								pgmNm = pgmNmLangStrings[1];
								if(pgmNm!= null){
									vo.setInputLang(pgmNmLang);
									vo.setPgmNm(pgmNm);
									resultCnt += compMenuMngDAO.insertPgmNmData(vo);
								}	
							}
						}
					}
				}
				
				//compMenuMngDAO.deletePgmNmData(vo);
				//compMenuMngDAO.insertPgmNmData(vo);
				
				/*메뉴별권한 처리 필요.*/
				compMenuMngDAO.deletePgmAuthData(vo);
				if(!"".equals(CommonUtil.nullToBlank(vo.getAuthGubuns()))){
					auths = vo.getAuthGubuns().split(",", -1);
					if(auths != null && 0<auths.length){
						for(String auth:auths){
							if(!"".equals(CommonUtil.nullToBlank(auth))){
								vo.setAuthGubun(auth);
								compMenuMngDAO.insertPgmAuthData(vo);
							}
						}
					}
					
				}
			}
		}
		
		
		/*전체 정렬순서 저장*/
		List<CompMenuMngVO> reSortList = compMenuMngDAO.selectReSortList(dataVO);
		if(reSortList != null && 0<reSortList.size()){
			//int order = 0;
			for(CompMenuMngVO sortvo : reSortList){
				//sortvo.setRealOrder(order++);
				compMenuMngDAO.updateReSortOrder(sortvo);
			}
		}
		
		/*fullnm 저장*/
		
		List<CompMenuMngVO> langList = selectLangList(dataVO);
		List<CompMenuMngVO> fullNameList = new ArrayList<CompMenuMngVO>(0);
		if(langList != null && 0<langList.size()){
			for(CompMenuMngVO langvo : langList){
				fullNameList = compMenuMngDAO.selectFullNameList(langvo);
				if(fullNameList != null && 0<fullNameList.size()){
					for(CompMenuMngVO nmvo : fullNameList){
						compMenuMngDAO.updateFullName(nmvo);
					}
				}
			}
		}
		
		
		return resultCnt;
	}

	// 메뉴 상세 조회
	public CompMenuMngVO selectDetail(CompMenuMngVO searchVO) throws Exception {
		return compMenuMngDAO.selectDetail(searchVO);
	}
	
	// 메뉴 도움말 저장
	public int saveGuideComment(CompMenuMngVO dataVO) throws Exception {
		int resultCnt = 0;
		resultCnt += compMenuMngDAO.updateGuideComment(dataVO);
		return resultCnt;
	}
	
}

