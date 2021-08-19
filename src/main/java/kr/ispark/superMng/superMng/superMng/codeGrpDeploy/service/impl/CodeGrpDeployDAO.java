/*************************************************************************
* CLASS 명	: CodeGrpDeployDAO
* 작 업 자	: 현걸욱
* 작 업 일	: 2018-07-04
* 기	능	: 슈퍼관리자 공통코드 배포관리 DAO
* ---------------------------- 변 경 이 력 --------------------------------
* 번호	작 업 자		작	업	일			변 경 내 용				비고
* ----	---------	----------------	---------------------	-----------
*	1	현걸욱		2018-07-04
**************************************************************************/
package kr.ispark.superMng.superMng.superMng.codeGrpDeploy.service.impl;


import java.util.List;

import org.springframework.stereotype.Repository;

import egovframework.com.cmm.service.impl.EgovComAbstractDAO;
import kr.ispark.common.util.PropertyUtil;
import kr.ispark.superMng.superMng.superMng.codeGrpDeploy.service.CodeGrpDeployVO;

@Repository
public class CodeGrpDeployDAO extends EgovComAbstractDAO {
	/**
	 * 공통코그룹관리 조회
	 */
	public List<CodeGrpDeployVO> selectList(CodeGrpDeployVO searchVO) throws Exception {
		return selectList("superMng.superMng.codeGrpDeploy.selectList", searchVO);
	}

	/**
	 * 공통코그룹관리 조회
	 */
	public List<CodeGrpDeployVO> selectDeployCompList(CodeGrpDeployVO searchVO) throws Exception {
		return selectList("superMng.superMng.codeGrpDeploy.selectDeployCompList", searchVO);
	}

	/**
	 * 공통코그룹관리 조회
	 */
	public List<CodeGrpDeployVO> selectLangList(CodeGrpDeployVO searchVO) throws Exception {
		return selectList("superMng.superMng.codeGrpDeploy.selectLangList", searchVO);
	}

	/**
	 * 공통코그룹관리삭제
	 */
	public int deleteCodeGrp(CodeGrpDeployVO searchVO) {
		
		return update("superMng.superMng.codeGrpDeploy.deleteCodeGrp", searchVO);
	}

	/**
	 * 공통코그룹관리삭제
	 */
	public int deleteCodeGrpNm(CodeGrpDeployVO searchVO) {
		
		return delete("superMng.superMng.codeGrpDeploy.deleteCodeGrpNm", searchVO);
	}

	/**
	 * 공통코그룹관리 저장
	 */
	public int insertData(CodeGrpDeployVO searchVO) {
		
		return update("superMng.superMng.codeGrpDeploy.insertData", searchVO);
	}

	/**
	 * 공통코그룹관리 저장
	 */
	public int insertCodeGrpNm(CodeGrpDeployVO searchVO) {
		
		return insert("superMng.superMng.codeGrpDeploy.insertCodeGrpNm", searchVO);
	}

	/**
	 * 공통코그룹관리 수정
	 */
	public int updateData(CodeGrpDeployVO searchVO) {
		
		return update("superMng.superMng.codeGrpDeploy.updateData", searchVO);
	}

	/**
	 * 공통코그룹관리 조회
	 */
	public List<CodeGrpDeployVO> selectCodeList(CodeGrpDeployVO searchVO) throws Exception {
		
		return selectList("superMng.superMng.codeGrpDeploy.selectCodeList", searchVO);
	}

	/**
	 * 공통코그룹관리 조회
	 */
	public String selectCodeExistYn(CodeGrpDeployVO searchVO) throws Exception {
		
		return selectOne("superMng.superMng.codeGrpDeploy.selectCodeExistYn", searchVO);
	}

	/**
	 * 공통코그룹관리삭제
	 */
	public int deleteCode(CodeGrpDeployVO searchVO) {
		
		return update("superMng.superMng.codeGrpDeploy.deleteCode", searchVO);
	}

	/**
	 * 공통코그룹관리삭제
	 */
	public int deleteCodeNm(CodeGrpDeployVO searchVO) {
		
		return delete("superMng.superMng.codeGrpDeploy.deleteCodeNm", searchVO);
	}

	/**
	 * 공통코그룹관리 저장
	 */
	public int insertCodeData(CodeGrpDeployVO searchVO) {
		
		return update("superMng.superMng.codeGrpDeploy.insertCodeData", searchVO);
	}

	/**
	 * 공통코그룹관리 저장
	 */
	public int insertCodeNm(CodeGrpDeployVO searchVO) {
		
		return insert("superMng.superMng.codeGrpDeploy.insertCodeNm", searchVO);
	}

	/**
	 * 공통코그룹관리 수정
	 */
	public int updateCodeData(CodeGrpDeployVO searchVO) {
		
		return update("superMng.superMng.codeGrpDeploy.updateCodeData", searchVO);
	}

	/**
	 * 공통코그룹관리 수정
	 */
	public String selectNextSeq(CodeGrpDeployVO searchVO) {
		
		return selectOne("superMng.superMng.codeGrpDeploy.selectNextSeq", searchVO);
	}


	/**
	 * 공통코그룹관리삭제
	 */
	public int deleteDeployedCodeGrp(CodeGrpDeployVO searchVO) {
		
		return delete("superMng.superMng.codeGrpDeploy.deleteDeployedCodeGrp", searchVO);
	}

	/**
	 * 공통코그룹관리삭제
	 */
	public int deleteDeployedCodeGrpNm(CodeGrpDeployVO searchVO) {
		
		return delete("superMng.superMng.codeGrpDeploy.deleteDeployedCodeGrpNm", searchVO);
	}

	/**
	 * 공통코그룹관리삭제
	 */
	public int deleteDeployedCode(CodeGrpDeployVO searchVO) {
		
		return delete("superMng.superMng.codeGrpDeploy.deleteDeployedCode", searchVO);
	}

	/**
	 * 공통코그룹관리삭제
	 */
	public int deleteDeployedCodeNm(CodeGrpDeployVO searchVO) {
		
		return delete("superMng.superMng.codeGrpDeploy.deleteDeployedCodeNm", searchVO);
	}

	/**
	 * 공통코그룹관리 저장
	 */
	public int insertDeployCodeGrp(CodeGrpDeployVO searchVO) {
		
		return insert("superMng.superMng.codeGrpDeploy.insertDeployCodeGrp", searchVO);
	}


	/**
	 * 공통코그룹관리 저장
	 */
	public int insertDeployCodeGrpNm(CodeGrpDeployVO searchVO) {
		
		return insert("superMng.superMng.codeGrpDeploy.insertDeployCodeGrpNm", searchVO);
	}


	/**
	 * 공통코그룹관리 저장
	 */
	public int insertDeployCode(CodeGrpDeployVO searchVO) {
		
		return insert("superMng.superMng.codeGrpDeploy.insertDeployCode", searchVO);
	}


	/**
	 * 공통코그룹관리 저장
	 */
	public int insertDeployCodeNm(CodeGrpDeployVO searchVO) {
		
		return insert("superMng.superMng.codeGrpDeploy.insertDeployCodeNm", searchVO);
	}


	private String setTemplateCompId() {
		return PropertyUtil.getProperty("Template.CompId");
	}
}

