/*************************************************************************
* CLASS 명	: CodeGrpDeployServiceIpml
* 작 업 자	: 현걸욱
* 작 업 일	: 2018-07-04
* 기	능	: 슈퍼관리자 공통코드 배포관리 ServiceIpml
* ---------------------------- 변 경 이 력 --------------------------------
* 번호	작 업 자		작	업	일			변 경 내 용				비고
* ----	---------	----------------	---------------------	-----------
*	1	현걸욱		2018-07-04
**************************************************************************/
package kr.ispark.superMng.superMng.superMng.codeGrpDeploy.service.impl;

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
import kr.ispark.common.util.service.impl.CodeUtilServiceImpl;
import kr.ispark.superMng.superMng.superMng.codeGrpDeploy.service.CodeGrpDeployVO;

@Service
public class CodeGrpDeployServiceImpl extends EgovAbstractServiceImpl {

	@Resource
	private IdGenServiceImpl idgenService;

	@Resource
	private CodeGrpDeployDAO codeGrpDeployDAO;

	@Resource
	private CodeUtilServiceImpl codeUtilService;
	
	@Autowired
    private CommonServiceImpl commonServiceImpl;

	// 공통코드 그룹관리관리 목록 조회
	public List<CodeGrpDeployVO> selectList(CodeGrpDeployVO searchVO) throws Exception {

		List<CodeGrpDeployVO> langList = selectLangList(searchVO);
		searchVO.setLangList(langList);
		
		List<CommonVO> dbList = commonServiceImpl.selectDbList();
		searchVO.setDbList(dbList);

		return codeGrpDeployDAO.selectList(searchVO);
	}

	public List<CodeGrpDeployVO> selectDeployCompList(CodeGrpDeployVO searchVO) throws Exception {
		
		List<CommonVO> dbList = commonServiceImpl.selectDbList();
		searchVO.setDbList(dbList);
		
		return codeGrpDeployDAO.selectDeployCompList(searchVO);
	}

	// 공통코드 그룹관리관리 목록 조회
	public List<CodeGrpDeployVO> selectLangList(CodeGrpDeployVO searchVO) throws Exception {
		return codeGrpDeployDAO.selectLangList(searchVO);
	}

	// 공통코드 그룹관리 삭제
	public int deleteCodeGrp(CodeGrpDeployVO dataVO) throws Exception {
		int resultCnt = codeGrpDeployDAO.deleteCodeGrp(dataVO);
		codeUtilService.insertCodeUpdateLog();
		return resultCnt;
	}

	// 공통코드 그룹관리 저장
	public int saveData(CodeGrpDeployVO dataVO) throws Exception {
		List<CodeGrpDeployVO> gridDataList = dataVO.getGridDataList();
		int resultCnt = 0;
		String key = "";

		String[] codeGrpNmLangs;
		String codeGrpNmLang;
		String codeGrpNm;

		if(gridDataList != null && 0<gridDataList.size()){
 			for(CodeGrpDeployVO paramvo : gridDataList){
				if(CommonUtil.isEmpty(paramvo.getCodeGrpId())) {
					key = idgenService.selectTemplateNextSeq("COM_CODE_GRP", null, 3, "0");
					paramvo.setCodeGrpId(key);
					resultCnt += codeGrpDeployDAO.insertData(paramvo);
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
									resultCnt += codeGrpDeployDAO.insertCodeGrpNm(paramvo);
								}
							}
						}
					}
				} else {
					resultCnt += codeGrpDeployDAO.updateData(paramvo);
					resultCnt += codeGrpDeployDAO.deleteCodeGrpNm(paramvo);
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
									resultCnt += codeGrpDeployDAO.insertCodeGrpNm(paramvo);
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
	public List<CodeGrpDeployVO> selectCodeList(CodeGrpDeployVO searchVO) throws Exception {

		List<CodeGrpDeployVO> langList = selectLangList(searchVO);
		searchVO.setLangList(langList);

		return codeGrpDeployDAO.selectCodeList(searchVO);
	}

	// 공통코드 그룹관리 삭제
	public int deleteCode(CodeGrpDeployVO dataVO) throws Exception {
		int resultCnt = codeGrpDeployDAO.deleteCode(dataVO);
		codeUtilService.insertCodeUpdateLog();
		return resultCnt;
	}

	// 공통코드 그룹관리 저장
	public int saveCodeData(CodeGrpDeployVO dataVO) throws Exception {
		List<CodeGrpDeployVO> gridDataList = dataVO.getGridDataList();
  		int resultCnt = 0;
		String key = "";

		String[] codeNmLangs;
		String codeNmLang;
		String codeNm;

		if("01".equals(CommonUtil.nullToBlank(dataVO.getCodeDefId()))){
			if(gridDataList != null && 0<gridDataList.size()){
				for(CodeGrpDeployVO paramvo : gridDataList){
					if(CommonUtil.isEmpty(paramvo.getCodeId())) {
						paramvo.setCodeGrpId(dataVO.getCodeGrpId());
						paramvo.setFindYear(dataVO.getFindYear());
						paramvo.setYearYn(dataVO.getYearYn());
						key = codeGrpDeployDAO.selectNextSeq(paramvo);
						paramvo.setCodeId(key);

						resultCnt += codeGrpDeployDAO.insertCodeData(paramvo);
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
										resultCnt += codeGrpDeployDAO.insertCodeNm(paramvo);
									}
								}
							}
						}
					} else {
						paramvo.setCodeGrpId(dataVO.getCodeGrpId());
						paramvo.setFindYear(dataVO.getFindYear());
						paramvo.setYearYn(dataVO.getYearYn());

						resultCnt += codeGrpDeployDAO.updateCodeData(paramvo);
						resultCnt += codeGrpDeployDAO.deleteCodeNm(paramvo);
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
										resultCnt += codeGrpDeployDAO.insertCodeNm(paramvo);
									}
								}
							}
						}
					}
				}
			}
		}else{
			if(gridDataList != null && 0<gridDataList.size()){
				for(CodeGrpDeployVO paramvo : gridDataList){
					paramvo.setCodeGrpId(dataVO.getCodeGrpId());
					paramvo.setFindYear(dataVO.getFindYear());
					paramvo.setYearYn(dataVO.getYearYn());
					String existYn = codeGrpDeployDAO.selectCodeExistYn(paramvo);

					if("N".equals(existYn)) {

						resultCnt += codeGrpDeployDAO.insertCodeData(paramvo);
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
										resultCnt += codeGrpDeployDAO.insertCodeNm(paramvo);
									}
								}
							}
						}
					} else {

						resultCnt += codeGrpDeployDAO.updateCodeData(paramvo);
						resultCnt += codeGrpDeployDAO.deleteCodeNm(paramvo);
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
										resultCnt += codeGrpDeployDAO.insertCodeNm(paramvo);
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

	/**
	 * 공통코드 배포
	 */
	public int updateDeploy(CodeGrpDeployVO searchVO) throws Exception {
		int resultCnt = 0;
		if(CommonUtil.isEmpty(searchVO.getCompIds())) return 0;

		CodeGrpDeployVO dataVO = new CodeGrpDeployVO();
		dataVO.setCodeGrpId(searchVO.getCodeGrpId());
		dataVO.setTemplateCompId(PropertyUtil.getProperty("Template.CompId"));

		String[] compIds = searchVO.getCompIds().split("\\|");
		for(int i=0, iLen=compIds.length; i<iLen; i++) {
			if(compIds[i] != null && CommonUtil.nullToBlank(compIds[i]).equals("")) continue;
			dataVO.setTempCompId(compIds[i]);
			
			EgovMap map = new EgovMap();
			map.put("paramCompId",compIds[i]);
			dataVO.setTargetDbId(commonServiceImpl.selectDbId(map));

			// 공통코드 그룹, NM 삭제
			resultCnt += codeGrpDeployDAO.deleteDeployedCodeGrp(dataVO);
			resultCnt += codeGrpDeployDAO.deleteDeployedCodeGrpNm(dataVO);

			/* 공통코드, NM 삭제
			 * - 슈퍼관리자에게 권한 코드를 배포할 경우
			 * 	슈퍼관리자 권한 코드(SUPER)는 삭제 하지 않도록 처리
			 */
			if(dataVO.getTempCompId().equals(PropertyUtil.getProperty("Super.CompId"))
				&& dataVO.getCodeGrpId().equals(PropertyUtil.getProperty("Super.AuthCodeGrpId"))) {
				dataVO.setCompareCodeId(PropertyUtil.getProperty("Super.AuthCodeId"));
			} else {
				dataVO.setCompareCodeId(null);
			}
			resultCnt += codeGrpDeployDAO.deleteDeployedCode(dataVO);
			resultCnt += codeGrpDeployDAO.deleteDeployedCodeNm(dataVO);

			// 공통코드 그룹, nm insert
			resultCnt += codeGrpDeployDAO.insertDeployCodeGrp(dataVO);
			resultCnt += codeGrpDeployDAO.insertDeployCodeGrpNm(dataVO);

			// 공통코드, nm insert
			resultCnt += codeGrpDeployDAO.insertDeployCode(dataVO);
			resultCnt += codeGrpDeployDAO.insertDeployCodeNm(dataVO);
		}

		return resultCnt;
	}
}

