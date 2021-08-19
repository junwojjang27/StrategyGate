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
package kr.ispark.system.system.comp.compCodeGrp.service.impl;


import java.util.List;

import org.springframework.stereotype.Repository;

import egovframework.com.cmm.service.impl.EgovComAbstractDAO;
import kr.ispark.system.system.comp.compCodeGrp.service.CompCodeGrpVO;

@Repository
public class CompCodeGrpDAO extends EgovComAbstractDAO {
	/**
	 * 공통코그룹관리 조회
	 */
	public List<CompCodeGrpVO> selectList(CompCodeGrpVO searchVO) throws Exception {
		return selectList("system.comp.compCodeGrp.selectList", searchVO);
	}
	
	/**
	 * 공통코그룹관리 조회
	 */
	public List<CompCodeGrpVO> selectLangList(CompCodeGrpVO searchVO) throws Exception {
		return selectList("system.comp.compCodeGrp.selectLangList", searchVO);
	}
	
	/**
	 * 공통코그룹관리삭제
	 */
	public int deleteCodeGrp(CompCodeGrpVO searchVO) {
		return update("system.comp.compCodeGrp.deleteCodeGrp", searchVO);
	}
	
	/**
	 * 공통코그룹관리삭제
	 */
	public int deleteCodeGrpNm(CompCodeGrpVO searchVO) {
		return delete("system.comp.compCodeGrp.deleteCodeGrpNm", searchVO);
	}
	
	/**
	 * 공통코그룹관리 저장
	 */
	public int insertData(CompCodeGrpVO searchVO) {
		return update("system.comp.compCodeGrp.insertData", searchVO);
	}
	
	/**
	 * 공통코그룹관리 저장
	 */
	public int insertCodeGrpNm(CompCodeGrpVO searchVO) {
		return insert("system.comp.compCodeGrp.insertCodeGrpNm", searchVO);
	}

	/**
	 * 공통코그룹관리 수정
	 */
	public int updateData(CompCodeGrpVO searchVO) {
		return update("system.comp.compCodeGrp.updateData", searchVO);
	}
	
	/**
	 * 공통코그룹관리 조회
	 */
	public List<CompCodeGrpVO> selectCodeList(CompCodeGrpVO searchVO) throws Exception {
		return selectList("system.comp.compCodeGrp.selectCodeList", searchVO);
	}
	
	/**
	 * 공통코그룹관리 조회
	 */
	public String selectCodeExistYn(CompCodeGrpVO searchVO) throws Exception {
		return selectOne("system.comp.compCodeGrp.selectCodeExistYn", searchVO);
	}
	
	/**
	 * 공통코그룹관리삭제
	 */
	public int deleteCode(CompCodeGrpVO searchVO) {
		return update("system.comp.compCodeGrp.deleteCode", searchVO);
	}
	
	/**
	 * 공통코그룹관리삭제
	 */
	public int deleteCodeNm(CompCodeGrpVO searchVO) {
		return delete("system.comp.compCodeGrp.deleteCodeNm", searchVO);
	}
	
	/**
	 * 공통코그룹관리 저장
	 */
	public int insertCodeData(CompCodeGrpVO searchVO) {
		return update("system.comp.compCodeGrp.insertCodeData", searchVO);
	}
	
	/**
	 * 공통코그룹관리 저장
	 */
	public int insertCodeNm(CompCodeGrpVO searchVO) {
		return insert("system.comp.compCodeGrp.insertCodeNm", searchVO);
	}

	/**
	 * 공통코그룹관리 수정
	 */
	public int updateCodeData(CompCodeGrpVO searchVO) {
		return update("system.comp.compCodeGrp.updateCodeData", searchVO);
	}
	
	/**
	 * 공통코그룹관리 수정
	 */
	public String selectNextSeq(CompCodeGrpVO searchVO) {
		return selectOne("system.comp.compCodeGrp.selectNextSeq", searchVO);
	}
	
	
	
}

