/**
 * @Class Name	:	LoginServiceImpl.java
 * @Description	:	로그인 Service
 * @author	:	kimyh
 * @date	:	2017-11-17
 */
package kr.ispark.common.security.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;
import egovframework.rte.psl.dataaccess.util.EgovMap;
import kr.ispark.common.CommonVO;
import kr.ispark.common.security.service.UserVO;
import kr.ispark.common.system.service.LangVO;
import kr.ispark.common.system.service.impl.CommonServiceImpl;
import kr.ispark.common.util.PropertyUtil;

@Service
public class LoginServiceImpl extends EgovAbstractServiceImpl {
	@Resource
	LoginDAO loginDAO;
	
	@Autowired
	CommonServiceImpl commonServiceImpl;
	
	// 사용자 조회
	public UserVO selectUser(UserVO vo) throws Exception {
		return loginDAO.selectUser(vo);
	}
	
	// 권한 목록 조회
	public List<String> selectAdminGubunList(UserVO vo) throws Exception {
		return loginDAO.selectAdminGubunList(vo);
	}
	
	// 회사의 사용언어 목록 조회
	public List<LangVO> selectLangList(UserVO vo) throws Exception {
		return loginDAO.selectLangList(vo);
	}
	
	/**
	 * 사용자의 연도별 성과조직 목록 조회
	 * @param	UserVO vo
	 * @return	List<EgovMap>
	 * @throws	Exception
	 */
	public List<EgovMap> selectUserScDeptList(UserVO vo) throws Exception {
		return loginDAO.selectUserScDeptList(vo);
	}
	
	/**
	 * 서비스 사용여부 조회
	 * @param	UserVO vo
	 * @return	String
	 * @throws	Exception
	 */
	public String selectServiceUseYn(UserVO vo) throws Exception {
		return loginDAO.selectServiceUseYn(vo);
	}
}
