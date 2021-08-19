/*************************************************************************
* CLASS 명	: MenuDeployMngVO
* 작 업 자	: 현걸욱
* 작 업 일	: 2018-07-06
* 기	능	: 메뉴배포관리 VO
* ---------------------------- 변 경 이 력 --------------------------------
* 번호	작 업 자		작	업	일			변 경 내 용				비고
* ----	---------	----------------	---------------------	-----------
*	1	현걸욱		2018-07-06
**************************************************************************/
package kr.ispark.superMng.superMng.superMng.menuDeployMng.service;

import java.util.HashMap;
import java.util.List;

import kr.ispark.common.CommonVO;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class MenuDeployMngVO extends CommonVO {
	private static final long serialVersionUID = -7555616568320156540L;

	private String templateCompId;
	
	private String findLang;
	private String pgmId;
	private String pgmNm;
	private String pgmNmLang;
	private String fullPgmNm;
	private String fullDeptNm;
	private String mainPgmNm;
	private String upPgmId;
	private String upPgmNm;
	private String pgmLevelId;
	private String levelId;
	private String url;
	private String sortOrder;
	private int realOrder;
	private String content;
	private String param;
	private String urlPattern;
	private String createDt;
	private String deleteDt;
	private String inputLang;
	
	private String isLeaf;
	private String hasLeaf;
	private String expanded;
	private String leafCnt;
	
	private String lang;
	private String langNm;
	
	private String fullPgmId;
	private String checkYn;
	private String authCnt;
	
	private String authGubun;
	private String authGubunNm;
	private String authGubuns;
	private String authGubunNms;
	private HashMap<String,String> pgmAuthMap = new HashMap<String,String>();
	private HashMap<String,String> pgmAuthNmMap = new HashMap<String,String>();
	private List<MenuDeployMngVO> langList;
	private String[] AuthGubunArray;
	
	private String deployCnt;
	private String deployTargetYn;
	private String compCnt;
	
	private String compId;
	private String compNm;
	private String deployYn;
	
	private String tempCompIds;
	private String tempCompId;
	private String pgmGbnId;
	
	private List<MenuDeployMngVO> gridDataList;
}

