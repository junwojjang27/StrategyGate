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
package kr.ispark.system.system.menu.menuMng.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import kr.ispark.common.system.service.impl.IdGenServiceImpl;
import kr.ispark.common.util.CommonUtil;
import kr.ispark.system.system.menu.menuMng.service.MenuMngVO;

import org.springframework.stereotype.Service;

import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;

@Service
public class MenuMngServiceImpl extends EgovAbstractServiceImpl {

	@Resource
	private IdGenServiceImpl idgenService;
	
	@Resource
	private MenuMngDAO menuMngDAO;
	
	// 메뉴관리관리 목록 조회
	public List<MenuMngVO> selectRoleList(MenuMngVO searchVO) throws Exception {
		return menuMngDAO.selectRoleList(searchVO);
	}

	// 메뉴관리관리 목록 조회
	public List<MenuMngVO> selectList(MenuMngVO searchVO) throws Exception {
		
		List<MenuMngVO> authPgmList = menuMngDAO.selectAuthPgmList(searchVO);
		
		//메뉴별 권한정보 담는 파라미터 맵
		HashMap<String,String> pgmAuthMap = new HashMap<String,String>();
		HashMap<String,String> pgmAuthNmMap = new HashMap<String,String>();
		
		if(authPgmList != null && 0<authPgmList.size()){
			int size = authPgmList.size();
			String authGubuns = "";
			String authGubunNms = "";
			String lastPgmId = "";
			for(int idx=0 ; idx<size ; idx++){
				
				MenuMngVO setVO = (MenuMngVO)authPgmList.get(idx);
				
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
		List<MenuMngVO> langList = selectLangList(searchVO);
		
		searchVO.setPgmAuthMap(pgmAuthMap);
		searchVO.setPgmAuthNmMap(pgmAuthNmMap);
		searchVO.setLangList(langList);
		
		List<MenuMngVO> selectList = new ArrayList<MenuMngVO>(0);
		if(0<langList.size()){
			selectList = menuMngDAO.selectList(searchVO);
		}
		
		return selectList;
	}
	
	// 메뉴관리 상세 조회
	public List<MenuMngVO> selectLangList(MenuMngVO searchVO) throws Exception {
		return menuMngDAO.selectLangList(searchVO);
	}

	// 메뉴관리 저장
	public int saveData(MenuMngVO dataVO) throws Exception {
		
		List<MenuMngVO> gridDataList = dataVO.getGridDataList();
		int resultCnt = 0;
		String key = "";
		String[] auths;
		
		String[] pgmNmLangs;
		String pgmNmLang;
		String pgmNm;
		
		if(gridDataList != null && 0 < gridDataList.size()){
			for(MenuMngVO vo: gridDataList ){
				
				if(CommonUtil.isEmpty(vo.getPgmId())) {
					key = idgenService.selectNextSeq("COM_PGM", "PGM", 4, "0");
					vo.setPgmId(key);
					
					resultCnt +=  menuMngDAO.insertData(vo);
					
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
									resultCnt += menuMngDAO.insertPgmNmData(vo);
								}	
							}
						}
					}
				} else {
					resultCnt += menuMngDAO.updateData(vo);
					resultCnt += menuMngDAO.deletePgmNmData(vo);
					
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
									resultCnt += menuMngDAO.insertPgmNmData(vo);
								}	
							}
						}
					}
				}
				
				//menuMngDAO.deletePgmNmData(vo);
				//menuMngDAO.insertPgmNmData(vo);
				
				/*메뉴별권한 처리 필요.*/
				menuMngDAO.deletePgmAuthData(vo);
				if(!"".equals(CommonUtil.nullToBlank(vo.getAuthGubuns()))){
					auths = vo.getAuthGubuns().split(",", -1);
					if(auths != null && 0<auths.length){
						for(String auth:auths){
							if(!"".equals(CommonUtil.nullToBlank(auth))){
								vo.setAuthGubun(auth);
								menuMngDAO.insertPgmAuthData(vo);
							}
						}
					}
					
				}
			}
		}
		
		
		/*전체 정렬순서 저장*/
		List<MenuMngVO> reSortList = menuMngDAO.selectReSortList(dataVO);
		if(reSortList != null && 0<reSortList.size()){
			int order = 0;
			for(MenuMngVO sortvo : reSortList){
				//sortvo.setRealOrder(order++);
				menuMngDAO.updateReSortOrder(sortvo);
			}
		}
		
		/*fullnm 저장*/
		
		List<MenuMngVO> langList = selectLangList(dataVO);
		List<MenuMngVO> fullNameList = new ArrayList<MenuMngVO>(0);
		if(langList != null && 0<langList.size()){
			for(MenuMngVO langvo : langList){
				fullNameList = menuMngDAO.selectFullNameList(langvo);
				if(fullNameList != null && 0<fullNameList.size()){
					for(MenuMngVO nmvo : fullNameList){
						menuMngDAO.updateFullName(nmvo);
					}
				}
			}
		}
		
		/*
		int levelId = 0;
		int sortOrder = 0;
		String upPgmId = "";
		MenuMngVO resultVO = new MenuMngVO();
		MenuMngVO paramVO = new MenuMngVO();
		List<MenuMngVO> resultList = new ArrayList<MenuMngVO>(0);
		//1.최상위 메뉴코드 정보 조회 upPgmId가 null인 메뉴코드 가져오기
		List<MenuMngVO> fList = menuMngDAO.selectPgmList(paramVO);
		if(fList != null && fList.size() > 0){
			for(MenuMngVO fvo:fList){
				sortOrder++;
				levelId = 1;
				paramVO.setUpPgmId();
				for()
			}
		}
		*/
		
		
		
		return resultCnt;
	}

	// 메뉴 상세 조회
	public MenuMngVO selectDetail(MenuMngVO searchVO) throws Exception {
		return menuMngDAO.selectDetail(searchVO);
	}
	
	// 메뉴 도움말 저장
	public int saveGuideComment(MenuMngVO dataVO) throws Exception {
		int resultCnt = 0;
		resultCnt += menuMngDAO.updateGuideComment(dataVO);
		return resultCnt;
	}
	
}

