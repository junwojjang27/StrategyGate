/*************************************************************************
* CLASS 명	: CodeGrpServiceIpml
* 작 업 자	: 현걸욱
* 작 업 일	: 2018-1-26
* 기	능	: 공통코드 그룹관리 ServiceIpml
* ---------------------------- 변 경 이 력 --------------------------------
* 번호	작 업 자		작	업	일			변 경 내 용				비고
* ----	---------	----------------	---------------------	-----------
*	1	현걸욱		2018-1-26
**************************************************************************/
package kr.ispark.system.system.comp.compCodeGrp.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;
import kr.ispark.common.system.service.impl.IdGenServiceImpl;
import kr.ispark.common.util.CommonUtil;
import kr.ispark.common.util.service.impl.CodeUtilServiceImpl;
import kr.ispark.system.system.comp.compCodeGrp.service.CompCodeGrpVO;

@Service
public class CompCodeGrpServiceImpl extends EgovAbstractServiceImpl {

	@Resource
	private IdGenServiceImpl idgenService;
	
	@Resource
	private CompCodeGrpDAO compCodeGrpDAO;

	@Resource
	private CodeUtilServiceImpl codeUtilService;
	
	// 공통코드 그룹관리관리 목록 조회
	public List<CompCodeGrpVO> selectList(CompCodeGrpVO searchVO) throws Exception {
		
		List<CompCodeGrpVO> langList = selectLangList(searchVO);
		searchVO.setLangList(langList);
		
		return compCodeGrpDAO.selectList(searchVO);
	}
	
	// 공통코드 그룹관리관리 목록 조회
	public List<CompCodeGrpVO> selectLangList(CompCodeGrpVO searchVO) throws Exception {
		return compCodeGrpDAO.selectLangList(searchVO);
	}
	
	// 공통코드 그룹관리 삭제
	public int deleteCodeGrp(CompCodeGrpVO dataVO) throws Exception {
		int resultCnt = compCodeGrpDAO.deleteCodeGrp(dataVO);
		codeUtilService.insertCodeUpdateLog();
		return resultCnt;
	}
	
	// 공통코드 그룹관리 저장
	public int saveData(CompCodeGrpVO dataVO) throws Exception {
		List<CompCodeGrpVO> gridDataList = dataVO.getGridDataList();
		int resultCnt = 0;
		String key = "";
		
		String[] codeGrpNmLangs;
		String codeGrpNmLang;
		String codeGrpNm;
		
		if(gridDataList != null && 0<gridDataList.size()){
 			for(CompCodeGrpVO paramvo : gridDataList){
				if(CommonUtil.isEmpty(paramvo.getCodeGrpId())) {
					key = idgenService.selectNextSeq("COM_CODE_GRP",3);
					paramvo.setCodeGrpId(key);
					resultCnt += compCodeGrpDAO.insertData(paramvo);
					codeGrpNmLangs = paramvo.getCodeGrpNmLang().split("\\$\\%\\^", 0);
					if(codeGrpNmLangs != null && codeGrpNmLangs.length>0){
						for(String codeGrpNmLangString:codeGrpNmLangs){
							String[] codeGrpNmLangStrings = codeGrpNmLangString.split("\\#\\$\\%", 0);
							if(codeGrpNmLangStrings != null && codeGrpNmLangStrings.length>1){
								codeGrpNmLang = codeGrpNmLangStrings[0];
								codeGrpNm = codeGrpNmLangStrings[1];
								if(codeGrpNm!= null){
									paramvo.setInputLang(codeGrpNmLang);
									paramvo.setCodeGrpNm(codeGrpNm);
									resultCnt += compCodeGrpDAO.insertCodeGrpNm(paramvo);
								}	
							}
						}
					}
				} else {
					resultCnt += compCodeGrpDAO.updateData(paramvo);
					resultCnt += compCodeGrpDAO.deleteCodeGrpNm(paramvo);
					codeGrpNmLangs = paramvo.getCodeGrpNmLang().split("\\$\\%\\^", 0);
					if(codeGrpNmLangs != null && codeGrpNmLangs.length>0){
						for(String codeGrpNmLangString:codeGrpNmLangs){
							String[] codeGrpNmLangStrings = codeGrpNmLangString.split("\\#\\$\\%", 0);
							if(codeGrpNmLangStrings != null && codeGrpNmLangStrings.length>1){
								codeGrpNmLang = codeGrpNmLangStrings[0];
								codeGrpNm = codeGrpNmLangStrings[1];
								if(codeGrpNm!= null){
									paramvo.setInputLang(codeGrpNmLang);
									paramvo.setCodeGrpNm(codeGrpNm);
									resultCnt += compCodeGrpDAO.insertCodeGrpNm(paramvo);
								}	
							}
						}
					}
				}
			}
		}
		
		//codeUtilService.insertCodeUpdateLog();
		
		return resultCnt;
	}
	
	// 공통코드 그룹관리관리 목록 조회
	public List<CompCodeGrpVO> selectCodeList(CompCodeGrpVO searchVO) throws Exception {
		
		List<CompCodeGrpVO> langList = selectLangList(searchVO);
		searchVO.setLangList(langList);
		
		return compCodeGrpDAO.selectCodeList(searchVO);
	}
	
	// 공통코드 그룹관리 삭제
	public int deleteCode(CompCodeGrpVO dataVO) throws Exception {
		int resultCnt = compCodeGrpDAO.deleteCode(dataVO);
		//codeUtilService.insertCodeUpdateLog();
		return resultCnt;
	}
	
	// 공통코드 그룹관리 저장
	public int saveCodeData(CompCodeGrpVO dataVO) throws Exception {
		List<CompCodeGrpVO> gridDataList = dataVO.getGridDataList();
  		int resultCnt = 0;
		String key = "";
		
		String[] codeNmLangs;
		String codeNmLang;
		String codeNm;
		
		if("01".equals(CommonUtil.nullToBlank(dataVO.getCodeDefId()))){
			if(gridDataList != null && 0<gridDataList.size()){
				for(CompCodeGrpVO paramvo : gridDataList){
					if(CommonUtil.isEmpty(paramvo.getCodeId())) {
						paramvo.setCodeGrpId(dataVO.getCodeGrpId());
						paramvo.setFindYear(dataVO.getFindYear());
						paramvo.setYearYn(dataVO.getYearYn());
						key = compCodeGrpDAO.selectNextSeq(paramvo);
						paramvo.setCodeId(key);
						
						resultCnt += compCodeGrpDAO.insertCodeData(paramvo);
						codeNmLangs = paramvo.getCodeNmLang().split("\\$\\%\\^", 0);
						if(codeNmLangs != null && codeNmLangs.length>0){
							for(String codeNmLangString:codeNmLangs){
								String[] codeNmLangStrings = codeNmLangString.split("\\#\\$\\%", 0);
								if(codeNmLangStrings != null && codeNmLangStrings.length>1){
									codeNmLang = codeNmLangStrings[0];
									codeNm = codeNmLangStrings[1];
									if(codeNm!= null){
										paramvo.setInputLang(codeNmLang);
										paramvo.setCodeNm(codeNm);
										resultCnt += compCodeGrpDAO.insertCodeNm(paramvo);
									}	
								}
							}
						}
					} else {
						paramvo.setCodeGrpId(dataVO.getCodeGrpId());
						paramvo.setFindYear(dataVO.getFindYear());
						paramvo.setYearYn(dataVO.getYearYn());
						
						resultCnt += compCodeGrpDAO.updateCodeData(paramvo);
						resultCnt += compCodeGrpDAO.deleteCodeNm(paramvo);
						codeNmLangs = paramvo.getCodeNmLang().split("\\$\\%\\^", 0);
						if(codeNmLangs != null && codeNmLangs.length>0){
							for(String codeNmLangString:codeNmLangs){
								String[] codeNmLangStrings = codeNmLangString.split("\\#\\$\\%", 0);
								if(codeNmLangStrings != null && codeNmLangStrings.length>1){
									codeNmLang = codeNmLangStrings[0];
									codeNm = codeNmLangStrings[1];
									if(codeNm!= null){
										paramvo.setInputLang(codeNmLang);
										paramvo.setCodeNm(codeNm);
										resultCnt += compCodeGrpDAO.insertCodeNm(paramvo);
									}	
								}
							}
						}
					}
				}
			}
		}else{
			if(gridDataList != null && 0<gridDataList.size()){
				for(CompCodeGrpVO paramvo : gridDataList){
					paramvo.setCodeGrpId(dataVO.getCodeGrpId());
					paramvo.setFindYear(dataVO.getFindYear());
					paramvo.setYearYn(dataVO.getYearYn());
					String existYn = compCodeGrpDAO.selectCodeExistYn(paramvo);
					
					if("N".equals(existYn)) {
						
						resultCnt += compCodeGrpDAO.insertCodeData(paramvo);
						codeNmLangs = paramvo.getCodeNmLang().split("\\$\\%\\^", 0);
						if(codeNmLangs != null && codeNmLangs.length>0){
							for(String codeNmLangString:codeNmLangs){
								String[] codeNmLangStrings = codeNmLangString.split("\\#\\$\\%", 0);
								if(codeNmLangStrings != null && codeNmLangStrings.length>1){
									codeNmLang = codeNmLangStrings[0];
									codeNm = codeNmLangStrings[1];
									if(codeNm!= null){
										paramvo.setInputLang(codeNmLang);
										paramvo.setCodeNm(codeNm);
										resultCnt += compCodeGrpDAO.insertCodeNm(paramvo);
									}	
								}
							}
						}
					} else {
						
						resultCnt += compCodeGrpDAO.updateCodeData(paramvo);
						resultCnt += compCodeGrpDAO.deleteCodeNm(paramvo);
						codeNmLangs = paramvo.getCodeNmLang().split("\\$\\%\\^", 0);
						if(codeNmLangs != null && codeNmLangs.length>0){
							for(String codeNmLangString:codeNmLangs){
								String[] codeNmLangStrings = codeNmLangString.split("\\#\\$\\%", 0);
								if(codeNmLangStrings != null && codeNmLangStrings.length>1){
									codeNmLang = codeNmLangStrings[0];
									codeNm = codeNmLangStrings[1];
									if(codeNm!= null){
										paramvo.setInputLang(codeNmLang);
										paramvo.setCodeNm(codeNm);
										resultCnt += compCodeGrpDAO.insertCodeNm(paramvo);
									}	
								}
							}
						}
					}
				}
			}
		}
		
		//codeUtilService.insertCodeUpdateLog();
		return resultCnt;
	}
}

