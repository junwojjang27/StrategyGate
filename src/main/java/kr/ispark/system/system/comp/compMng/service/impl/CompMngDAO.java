/*************************************************************************
* CLASS 명	: ClientMngDAO
* 작 업 자	: kimyh
* 작 업 일	: 2018. 07. 09.
* 기	능	: 회사정보 관리 DAO
* ---------------------------- 변 경 이 력 --------------------------------
* 번호	작 업 자		작	업	일			변 경 내 용				비고
* ----	---------	----------------	---------------------	-----------
*	1	kimyh		2018. 07. 09.		최 초 작 업
**************************************************************************/
package kr.ispark.system.system.comp.compMng.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Repository;

import egovframework.com.cmm.EgovMessageSource;
import egovframework.com.cmm.service.impl.EgovComAbstractDAO;
import kr.ispark.superMng.superMng.superMng.clientMng.service.ClientMngVO;

@Repository
public class CompMngDAO extends EgovComAbstractDAO {
	@Resource(name="egovMessageSource")
	public EgovMessageSource egovMessageSource;

	/**
	 * 언어 목록 조회
	 * @param	ClientMngVO searchVO
	 * @return	List<ClientMngVO>
	 * @throws	Exception
	 */
	public List<ClientMngVO> langList(ClientMngVO searchVO) throws Exception {
		return selectList("system.system.comp.compMng.selectLang", searchVO);
	}
	
	/**
	 * 상세 조회
	 * @param	ClientMngVO searchVO
	 * @return	ClientMngVO
	 * @throws	Exception
	 */
	public ClientMngVO selectDetail(ClientMngVO searchVO) throws Exception {
		return selectOne("system.system.comp.compMng.selectDetail", searchVO);
	}
	
	/**
	 * 사용 언어 정보 삭제
	 * @param	ClientMngVO dataVO
	 * @return	int
	 * @throws	Exception
	 */
	public int deleteLang(ClientMngVO dataVO) throws Exception {
		return delete("system.system.comp.compMng.deleteLang", dataVO);
	}
	
	/**
	 * 사용 언어 정보 저장
	 * @param	ClientMngVO dataVO
	 * @return	int
	 * @throws	Exception
	 */
	public int insertLang(ClientMngVO dataVO) throws Exception {
		return insert("system.system.comp.compMng.insertLang", dataVO);
	}
	
	/**
	 * 수정
	 * @param	ClientMngVO dataVO
	 * @return	int
	 * @throws	Exception
	 */
	public int updateData(ClientMngVO dataVO) throws Exception {
		return update("system.system.comp.compMng.updateData", dataVO);
	}
}
