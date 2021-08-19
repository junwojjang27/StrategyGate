/*************************************************************************
* CLASS 명	: CodeGrpDAO
* 작 업 자	: 현걸욱
* 작 업 일	: 2018-1-26
* 기	능	: 공통코그룹관리 DAO
* ---------------------------- 변 경 이 력 --------------------------------
* 번호	작 업 자		작	업	일			변 경 내 용				비고
* ----	---------	----------------	---------------------	-----------
*	1	현걸욱		2018-1-26
**************************************************************************/
package kr.ispark.system.system.code.codeGrp.service.impl;


import java.util.List;

import org.springframework.stereotype.Repository;

import egovframework.com.cmm.service.impl.EgovComAbstractDAO;
import kr.ispark.system.system.code.codeGrp.service.CodeGrpVO;

@Repository
public class CodeGrpDAO extends EgovComAbstractDAO {
	/**
	 * 공통코그룹관리 조회
	 */
	public List<CodeGrpVO> selectList(CodeGrpVO searchVO) throws Exception {
		return selectList("system.code.codeGrp.selectList", searchVO);
	}
	
	/**
	 * 공통코그룹관리 조회
	 */
	public List<CodeGrpVO> selectLangList(CodeGrpVO searchVO) throws Exception {
		return selectList("system.code.codeGrp.selectLangList", searchVO);
	}
	
	/**
	 * 공통코그룹관리삭제
	 */
	public int deleteCodeGrp(CodeGrpVO searchVO) {
		return update("system.code.codeGrp.deleteCodeGrp", searchVO);
	}
	
	/**
	 * 공통코그룹관리삭제
	 */
	public int deleteCodeGrpNm(CodeGrpVO searchVO) {
		return delete("system.code.codeGrp.deleteCodeGrpNm", searchVO);
	}
	
	/**
	 * 공통코그룹관리 저장
	 */
	public int insertData(CodeGrpVO searchVO) {
		return update("system.code.codeGrp.insertData", searchVO);
	}
	
	/**
	 * 공통코그룹관리 저장
	 */
	public int insertCodeGrpNm(CodeGrpVO searchVO) {
		return insert("system.code.codeGrp.insertCodeGrpNm", searchVO);
	}

	/**
	 * 공통코그룹관리 수정
	 */
	public int updateData(CodeGrpVO searchVO) {
		return update("system.code.codeGrp.updateData", searchVO);
	}
	
	/**
	 * 공통코그룹관리 조회
	 */
	public List<CodeGrpVO> selectCodeList(CodeGrpVO searchVO) throws Exception {
		return selectList("system.code.codeGrp.selectCodeList", searchVO);
	}
	
	/**
	 * 공통코그룹관리 조회
	 */
	public String selectCodeExistYn(CodeGrpVO searchVO) throws Exception {
		return selectOne("system.code.codeGrp.selectCodeExistYn", searchVO);
	}
	
	/**
	 * 공통코그룹관리삭제
	 */
	public int deleteCode(CodeGrpVO searchVO) {
		return update("system.code.codeGrp.deleteCode", searchVO);
	}
	
	/**
	 * 공통코그룹관리삭제
	 */
	public int deleteCodeNm(CodeGrpVO searchVO) {
		return delete("system.code.codeGrp.deleteCodeNm", searchVO);
	}
	
	/**
	 * 공통코그룹관리 저장
	 */
	public int insertCodeData(CodeGrpVO searchVO) {
		return update("system.code.codeGrp.insertCodeData", searchVO);
	}
	
	/**
	 * 공통코그룹관리 저장
	 */
	public int insertCodeNm(CodeGrpVO searchVO) {
		return insert("system.code.codeGrp.insertCodeNm", searchVO);
	}

	/**
	 * 공통코그룹관리 수정
	 */
	public int updateCodeData(CodeGrpVO searchVO) {
		return update("system.code.codeGrp.updateCodeData", searchVO);
	}
	
	/**
	 * 공통코그룹관리 수정
	 */
	public String selectNextSeq(CodeGrpVO searchVO) {
		return selectOne("system.code.codeGrp.selectNextSeq", searchVO);
	}
	
	
	
}

