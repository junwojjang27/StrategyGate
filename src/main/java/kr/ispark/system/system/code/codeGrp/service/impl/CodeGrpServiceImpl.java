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
package kr.ispark.system.system.code.codeGrp.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;
import kr.ispark.common.system.service.impl.IdGenServiceImpl;
import kr.ispark.common.util.CommonUtil;
import kr.ispark.common.util.service.impl.CodeUtilServiceImpl;
import kr.ispark.system.system.code.codeGrp.service.CodeGrpVO;

@Service
public class CodeGrpServiceImpl extends EgovAbstractServiceImpl {

	@Resource
	private IdGenServiceImpl idgenService;
	
	@Resource
	private CodeGrpDAO codeGrpDAO;

	@Resource
	private CodeUtilServiceImpl codeUtilService;
	
	// 공통코드 그룹관리관리 목록 조회
	public List<CodeGrpVO> selectList(CodeGrpVO searchVO) throws Exception {
		
		List<CodeGrpVO> langList = selectLangList(searchVO);
		searchVO.setLangList(langList);
		
		return codeGrpDAO.selectList(searchVO);
	}
	
	// 공통코드 그룹관리관리 목록 조회
	public List<CodeGrpVO> selectLangList(CodeGrpVO searchVO) throws Exception {
		return codeGrpDAO.selectLangList(searchVO);
	}
	
	// 공통코드 그룹관리 삭제
	public int deleteCodeGrp(CodeGrpVO dataVO) throws Exception {
		int resultCnt = codeGrpDAO.deleteCodeGrp(dataVO);
		codeUtilService.insertCodeUpdateLog();
		return resultCnt;
	}
	
	// 공통코드 그룹관리 저장
	public int saveData(CodeGrpVO dataVO) throws Exception {
		List<CodeGrpVO> gridDataList = dataVO.getGridDataList();
		int resultCnt = 0;
		String key = "";
		
		String[] codeGrpNmLangs;
		String codeGrpNmLang;
		String codeGrpNm;
		
		if(gridDataList != null && 0<gridDataList.size()){
 			for(CodeGrpVO paramvo : gridDataList){
				if(CommonUtil.isEmpty(paramvo.getCodeGrpId())) {
					key = idgenService.selectNextSeq("COM_CODE_GRP",3);
					paramvo.setCodeGrpId(key);
					resultCnt += codeGrpDAO.insertData(paramvo);
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
									resultCnt += codeGrpDAO.insertCodeGrpNm(paramvo);
								}	
							}
						}
					}
				} else {
					resultCnt += codeGrpDAO.updateData(paramvo);
					resultCnt += codeGrpDAO.deleteCodeGrpNm(paramvo);
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
									resultCnt += codeGrpDAO.insertCodeGrpNm(paramvo);
								}	
							}
						}
					}
				}
			}
		}
		
		codeUtilService.insertCodeUpdateLog();
		
		return resultCnt;
	}
	
	// 공통코드 그룹관리관리 목록 조회
	public List<CodeGrpVO> selectCodeList(CodeGrpVO searchVO) throws Exception {
		
		List<CodeGrpVO> langList = selectLangList(searchVO);
		searchVO.setLangList(langList);
		
		return codeGrpDAO.selectCodeList(searchVO);
	}
	
	// 공통코드 그룹관리 삭제
	public int deleteCode(CodeGrpVO dataVO) throws Exception {
		int resultCnt = codeGrpDAO.deleteCode(dataVO);
		codeUtilService.insertCodeUpdateLog();
		return resultCnt;
	}
	
	// 공통코드 그룹관리 저장
	public int saveCodeData(CodeGrpVO dataVO) throws Exception {
		List<CodeGrpVO> gridDataList = dataVO.getGridDataList();
  		int resultCnt = 0;
		String key = "";
		
		String[] codeNmLangs;
		String codeNmLang;
		String codeNm;
		
		if("01".equals(CommonUtil.nullToBlank(dataVO.getCodeDefId()))){
			if(gridDataList != null && 0<gridDataList.size()){
				for(CodeGrpVO paramvo : gridDataList){
					if(CommonUtil.isEmpty(paramvo.getCodeId())) {
						paramvo.setCodeGrpId(dataVO.getCodeGrpId());
						paramvo.setFindYear(dataVO.getFindYear());
						paramvo.setYearYn(dataVO.getYearYn());
						key = codeGrpDAO.selectNextSeq(paramvo);
						paramvo.setCodeId(key);
						
						resultCnt += codeGrpDAO.insertCodeData(paramvo);
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
										resultCnt += codeGrpDAO.insertCodeNm(paramvo);
									}	
								}
							}
						}
					} else {
						paramvo.setCodeGrpId(dataVO.getCodeGrpId());
						paramvo.setFindYear(dataVO.getFindYear());
						paramvo.setYearYn(dataVO.getYearYn());
						
						resultCnt += codeGrpDAO.updateCodeData(paramvo);
						resultCnt += codeGrpDAO.deleteCodeNm(paramvo);
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
										resultCnt += codeGrpDAO.insertCodeNm(paramvo);
									}	
								}
							}
						}
					}
				}
			}
		}else{
			if(gridDataList != null && 0<gridDataList.size()){
				for(CodeGrpVO paramvo : gridDataList){
					
					paramvo.setCodeGrpId(dataVO.getCodeGrpId());
					paramvo.setFindYear(dataVO.getFindYear());
					paramvo.setYearYn(dataVO.getYearYn());
					
					String existYn = codeGrpDAO.selectCodeExistYn(paramvo);
					
					if("N".equals(existYn)) {
						
						resultCnt += codeGrpDAO.insertCodeData(paramvo);
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
										resultCnt += codeGrpDAO.insertCodeNm(paramvo);
									}	
								}
							}
						}
					} else {
						
						resultCnt += codeGrpDAO.updateCodeData(paramvo);
						resultCnt += codeGrpDAO.deleteCodeNm(paramvo);
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
										resultCnt += codeGrpDAO.insertCodeNm(paramvo);
									}	
								}
							}
						}
					}
				}
			}
		}
		
		codeUtilService.insertCodeUpdateLog();
		return resultCnt;
	}
}

