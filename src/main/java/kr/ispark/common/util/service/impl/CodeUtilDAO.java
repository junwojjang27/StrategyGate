/*************************************************************************
* CLASS 명  	: CodeUtil
* 작 업 자  	: 박재현
* 작 업 일  	: 2009.07.15
* 기    능  	: CodeUtilDao
* ---------------------------- 변 경 이 력 --------------------------------
* 번호  작 업 자     작     업     일        변 경 내 용                 비고
* ----  --------  -----------------  -------------------------    --------
*   1    박재현		 2009.07.15			  최 초 작 업
**************************************************************************/
package kr.ispark.common.util.service.impl;


import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import egovframework.com.cmm.service.impl.EgovComAbstractDAO;
import egovframework.rte.psl.dataaccess.util.EgovMap;
import kr.ispark.common.util.CommonUtil;
import kr.ispark.common.util.service.CodeVO;

@Repository
public class CodeUtilDAO extends EgovComAbstractDAO {

	/**
	 * 코드그룹목록
	 * @param sqlMap
	 * @return List
	 * @throws SQLException
	*/
	public List<CodeVO> getCodeGrpList(CodeVO vo) throws SQLException {
		return selectList("codeUtil.getCodeGrpList", vo);
	}

	/**
	 * 코드목록
	 * @param sqlMap
	 * @return List
	 * @throws SQLException
	*/
	public List<CodeVO> getCodeList(CodeVO vo) throws SQLException {
		return selectList("codeUtil.getCodeList", vo);
	}
	
	/**
	 * 최근 10분 사이에 공통코드가 변경된 compId 조회
	 * @return	List<String>
	 * @throws	Exception
	 */
	public List<String> selectUpdateCompIdList() throws Exception {
		return selectList("codeUtil.selectUpdateCompIdList");
	}
	
	/**
	 * 공통코드 변경 이력 등록
	 * @param	EgovMap map
	 * @return	int
	 * @throws	Exception
	 */
	public int insertCodeUpdateLog(EgovMap map) throws Exception {
		delete("codeUtil.deleteCodeUpdateLog");
		return insert("codeUtil.insertCodeUpdateLog", map);
	}
	
	public List<CodeVO> selectDBCodeList(EgovMap emap) throws Exception {
		return selectList("codeUtil.selectDBCodeList",emap);
	}
	
	public String selectDBCodeGrpDetail(EgovMap emap) throws Exception {
		return selectOne("codeUtil.selectDBCodeGrpDetail",emap);
	}
	
	public String selectDBCodeDetail(EgovMap emap) throws Exception {
		return selectOne("codeUtil.selectDBCodeDetail",emap);
	}
	
	
	
	
	
}
