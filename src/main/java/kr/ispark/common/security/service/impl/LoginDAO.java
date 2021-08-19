/**
 * @Class Name	:	LoginDAO.java
 * @Description	:	로그인 DAO
 * @author	:	kimyh
 * @date	:	2017-11-17
 */
package kr.ispark.common.security.service.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import egovframework.com.cmm.service.impl.EgovComAbstractDAO;
import egovframework.rte.psl.dataaccess.util.EgovMap;
import kr.ispark.common.security.service.UserVO;
import kr.ispark.common.system.service.LangVO;

@Repository
public class LoginDAO extends EgovComAbstractDAO {
	// 사용자 조회
	public UserVO selectUser(UserVO vo) throws Exception {
		return (UserVO)selectOne("login.selectUser", vo);
	}
	
	// 권한 목록 조회
	public List<String> selectAdminGubunList(UserVO vo) throws Exception {
		return selectList("login.selectAdminGubunList", vo);
	}
	
	// 회사의 사용언어 목록 조회
	public List<LangVO> selectLangList(UserVO vo) throws Exception {
		return selectList("login.selectLangList", vo);
	}
	
	/**
	 * 사용자의 연도별 성과조직 목록 조회
	 * @param	UserVO vo
	 * @return	List<EgovMap>
	 * @throws	Exception
	 */
	public List<EgovMap> selectUserScDeptList(UserVO vo) throws Exception {
		return selectList("login.selectUserScDeptList", vo);
	}
	
	/**
	 * 서비스 사용여부 조회
	 * @param	UserVO vo
	 * @return	String
	 * @throws	Exception
	 */
	public String selectServiceUseYn(UserVO vo) throws Exception {
		return selectOne("login.selectServiceUseYn", vo);
	}
}
