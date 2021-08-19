/*************************************************************************
* CLASS 명	: MenuDeployMngServiceIpml
* 작 업 자	: 현걸욱
* 작 업 일	: 2018-07-06
* 기	능	: 메뉴배포관리 ServiceIpml
* ---------------------------- 변 경 이 력 --------------------------------
* 번호	작 업 자		작	업	일			변 경 내 용				비고
* ----	---------	----------------	---------------------	-----------
*	1	현걸욱		2018-07-06
**************************************************************************/
package kr.ispark.superMng.superMng.superMng.menuDeployMng.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;
import egovframework.rte.psl.dataaccess.util.EgovMap;
import kr.ispark.common.CommonVO;
import kr.ispark.common.system.service.impl.CommonServiceImpl;
import kr.ispark.common.system.service.impl.IdGenServiceImpl;
import kr.ispark.common.util.CommonUtil;
import kr.ispark.common.util.PropertyUtil;
import kr.ispark.superMng.superMng.superMng.menuDeployMng.service.MenuDeployMngVO;

@Service
public class MenuDeployMngServiceImpl extends EgovAbstractServiceImpl {

	@Resource
	private IdGenServiceImpl idgenService;

	@Resource
	private MenuDeployMngDAO menuDeployMngDAO;
	
	@Autowired
	private CommonServiceImpl commonServiceImpl;

	private String templateCompId = PropertyUtil.getProperty("Template.CompId");

	/**
	 * 메뉴배포관리 목록 조회
	 * @param	MenuDeployMngVO searchVO
	 * @return	List<MenuDeployMngVO>
	 * @throws	Exception
	 */
	public List<MenuDeployMngVO> selectDeployList(MenuDeployMngVO searchVO) throws Exception {
		searchVO.setTemplateCompId(templateCompId);
		
		List<CommonVO> dbList = commonServiceImpl.selectDbList();
		searchVO.setDbList(dbList);
		
		return menuDeployMngDAO.selectDeployList(searchVO);
	}

	/**
	 * 메뉴배포관리 목록 조회
	 * @param	MenuDeployMngVO searchVO
	 * @return	List<MenuDeployMngVO>
	 * @throws	Exception
	 */
	public List<MenuDeployMngVO> selectDeployCompList(MenuDeployMngVO searchVO) throws Exception {
		searchVO.setTemplateCompId(templateCompId);
		
		List<CommonVO> dbList = commonServiceImpl.selectDbList();
		searchVO.setDbList(dbList);
		
		return menuDeployMngDAO.selectDeployCompList(searchVO);
	}

	/**
	 * 메뉴배포관리 저장
	 * @param	MenuDeployMngVO searchVO
	 * @return	int
	 * @throws	Exception
	 */
	public int saveMenuDeployMng(MenuDeployMngVO dataVO) throws Exception {
		dataVO.setTemplateCompId(templateCompId);
		int resultCnt = 0;

		List<MenuDeployMngVO> list = dataVO.getGridDataList();
		if(list != null && list.size() > 0){
			for(MenuDeployMngVO paramVO : list){
				paramVO.setTemplateCompId(templateCompId);
				resultCnt += menuDeployMngDAO.updateDeployData(paramVO);
			}
		}

		return resultCnt;
	}

	/**
	 * 메뉴배포관리 저장
	 * @param	MenuDeployMngVO searchVO
	 * @return	int
	 * @throws	Exception
	 */
	public int updateDeploy(MenuDeployMngVO dataVO) throws Exception {
		dataVO.setTemplateCompId(templateCompId);
		int resultCnt = 0;
		String[] tempCompIds = dataVO.getTempCompIds().split("\\|",0);

		if(tempCompIds != null && tempCompIds.length > 0){
			for(String tempCompId : tempCompIds){
				dataVO.setTempCompId(tempCompId);
				
				EgovMap map = new EgovMap();
				map.put("paramCompId",tempCompId);
				String targetDbId = commonServiceImpl.selectDbId(map);
				dataVO.setTargetDbId(targetDbId);
				
				menuDeployMngDAO.deleteDeployedMenu(dataVO);
				menuDeployMngDAO.deleteDeployedMenuNm(dataVO);
				menuDeployMngDAO.deleteDeployedMenuAuth(dataVO);

				resultCnt += menuDeployMngDAO.insertDeployMenu(dataVO);
				resultCnt += menuDeployMngDAO.insertDeployMenuNm(dataVO);
				resultCnt += menuDeployMngDAO.insertDeployMenuAuth(dataVO);
				
				/*전체 정렬순서 저장*/
				List<MenuDeployMngVO> reSortList = menuDeployMngDAO.selectCompReSortList(dataVO);
				if(reSortList != null && 0<reSortList.size()){
					for(MenuDeployMngVO sortvo : reSortList){
						//sortvo.setRealOrder(order++);
						sortvo.setTargetDbId(targetDbId);
						sortvo.setTempCompId(tempCompId);
						menuDeployMngDAO.updateCompReSortOrder(sortvo);
					}
				}

				/*fullnm 저장*/
				List<MenuDeployMngVO> langList = menuDeployMngDAO.selectCompLangList(dataVO);
				List<MenuDeployMngVO> fullNameList = new ArrayList<MenuDeployMngVO>(0);
				if(langList != null && 0<langList.size()){
					for(MenuDeployMngVO langvo : langList){
						langvo.setTargetDbId(targetDbId);
						langvo.setTempCompId(tempCompId);
						fullNameList = menuDeployMngDAO.selectCompFullNameList(langvo);
						if(fullNameList != null && 0<fullNameList.size()){
							for(MenuDeployMngVO nmvo : fullNameList){
								nmvo.setTargetDbId(targetDbId);
								nmvo.setTempCompId(tempCompId);
								menuDeployMngDAO.updateCompFullName(nmvo);
							}
						}
					}
				}
				
			}
		}

		return resultCnt;
	}

	/*메뉴등록 ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/

	// 메뉴관리관리 목록 조회
	public List<MenuDeployMngVO> selectRoleList(MenuDeployMngVO searchVO) throws Exception {
		searchVO.setTemplateCompId(templateCompId);
		return menuDeployMngDAO.selectRoleList(searchVO);
	}

	// 메뉴관리관리 목록 조회
	public List<MenuDeployMngVO> selectList(MenuDeployMngVO searchVO) throws Exception {
		searchVO.setTemplateCompId(templateCompId);
		List<MenuDeployMngVO> authPgmList = menuDeployMngDAO.selectAuthPgmList(searchVO);

		//메뉴별 권한정보 담는 파라미터 맵
		HashMap<String,String> pgmAuthMap = new HashMap<String,String>();
		HashMap<String,String> pgmAuthNmMap = new HashMap<String,String>();

		if(authPgmList != null && 0<authPgmList.size()){
			int size = authPgmList.size();
			String authGubuns = "";
			String authGubunNms = "";
			String lastPgmId = "";
			for(int idx=0 ; idx<size ; idx++){

				MenuDeployMngVO setVO = authPgmList.get(idx);

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
		List<MenuDeployMngVO> langList = selectLangList(searchVO);

		searchVO.setPgmAuthMap(pgmAuthMap);
		searchVO.setPgmAuthNmMap(pgmAuthNmMap);
		searchVO.setLangList(langList);

		List<MenuDeployMngVO> selectList = new ArrayList<MenuDeployMngVO>(0);
		if(0<langList.size()){
			selectList = menuDeployMngDAO.selectList(searchVO);
		}

		return selectList;
	}

	// 메뉴관리 상세 조회
	public List<MenuDeployMngVO> selectLangList(MenuDeployMngVO searchVO) throws Exception {
		return menuDeployMngDAO.selectLangList(searchVO);
	}

	// 메뉴관리 저장
	public int saveData(MenuDeployMngVO dataVO) throws Exception {

		List<MenuDeployMngVO> gridDataList = dataVO.getGridDataList();
		int resultCnt = 0;
		String key = "";
		String[] auths;

		String[] pgmNmLangs;
		String pgmNmLang;
		String pgmNm;

		if(gridDataList != null && 0 < gridDataList.size()){
			for(MenuDeployMngVO vo: gridDataList ){

				vo.setTemplateCompId(templateCompId);

				if(CommonUtil.isEmpty(vo.getPgmId())) {
					key = idgenService.selectTemplateNextSeq("COM_PGM", "PGM", 4, "0");
					vo.setPgmId(key);
					resultCnt +=  menuDeployMngDAO.insertData(vo);

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
									resultCnt += menuDeployMngDAO.insertPgmNmData(vo);
								}
							}
						}
					}
				} else {
					resultCnt += menuDeployMngDAO.updateData(vo);
					resultCnt += menuDeployMngDAO.deletePgmNmData(vo);

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
									resultCnt += menuDeployMngDAO.insertPgmNmData(vo);
								}
							}
						}
					}
				}

				//menuMngDAO.deletePgmNmData(vo);
				//menuMngDAO.insertPgmNmData(vo);

				/*메뉴별권한 처리 필요.*/

				menuDeployMngDAO.deletePgmAuthData(vo);

				if(!"".equals(CommonUtil.nullToBlank(vo.getAuthGubuns()))){

					auths = vo.getAuthGubuns().split(",", -1);
					if(auths != null && 0<auths.length){
						for(String auth:auths){
							if(!"".equals(CommonUtil.nullToBlank(auth))){
								vo.setAuthGubun(auth);
								menuDeployMngDAO.insertPgmAuthData(vo);
							}
						}
					}
				}
			}
		}


		/*전체 정렬순서 저장*/
		dataVO.setTemplateCompId(templateCompId);
		List<MenuDeployMngVO> reSortList = menuDeployMngDAO.selectReSortList(dataVO);
		if(reSortList != null && 0<reSortList.size()){
			for(MenuDeployMngVO sortvo : reSortList){
				//sortvo.setRealOrder(order++);
				sortvo.setTemplateCompId(templateCompId);
				menuDeployMngDAO.updateReSortOrder(sortvo);
			}
		}

		/*fullnm 저장*/
		dataVO.setTemplateCompId(templateCompId);
		List<MenuDeployMngVO> langList = selectLangList(dataVO);
		List<MenuDeployMngVO> fullNameList = new ArrayList<MenuDeployMngVO>(0);
		if(langList != null && 0<langList.size()){
			for(MenuDeployMngVO langvo : langList){
				langvo.setTemplateCompId(templateCompId);
				fullNameList = menuDeployMngDAO.selectFullNameList(langvo);
				if(fullNameList != null && 0<fullNameList.size()){
					for(MenuDeployMngVO nmvo : fullNameList){
						nmvo.setTemplateCompId(templateCompId);
						menuDeployMngDAO.updateFullName(nmvo);
					}
				}
			}
		}

		return resultCnt;
	}

}

