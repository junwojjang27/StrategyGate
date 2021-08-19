/*************************************************************************
* CLASS 명	: CompMngServiceImpl
* 작 업 자	: kimyh
* 작 업 일	: 2018. 07. 09.
* 기	능	: 회사정보 관리 Service
* ---------------------------- 변 경 이 력 --------------------------------
* 번호	작 업 자		작	업	일			변 경 내 용				비고
* ----	---------	----------------	---------------------	-----------
*	1	kimyh		2018. 07. 09.		최 초 작 업
**************************************************************************/

package kr.ispark.system.system.comp.compMng.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;
import egovframework.rte.psl.dataaccess.util.EgovMap;
import kr.ispark.common.system.service.impl.CommonServiceImpl;
import kr.ispark.common.util.PropertyUtil;
import kr.ispark.common.util.SessionUtil;
import kr.ispark.common.util.service.impl.CodeUtilServiceImpl;
import kr.ispark.superMng.superMng.superMng.clientMng.service.ClientMngVO;
import kr.ispark.superMng.superMng.superMng.clientMng.service.impl.ClientMngDAO;

@Service
public class CompMngServiceImpl extends EgovAbstractServiceImpl {
	public final Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired
	public CodeUtilServiceImpl codeUtilService;
	
	@Resource
	private CompMngDAO compMngDAO;
	
	@Resource
	private ClientMngDAO clientMngDAO;
	
	@Autowired
	private CommonServiceImpl commonServiceImpl;
	
	/**
	 * 언어 목록 조회
	 * @param	ClientMngVO searchVO
	 * @return	List<CompMngVO>
	 * @throws	Exception
	 */
	public List<ClientMngVO> langList(ClientMngVO searchVO) throws Exception {
		return compMngDAO.langList(searchVO);
	}
	
	/**
	 * 상세 조회
	 * @param	ClientMngVO searchVO
	 * @return	CompMngVO
	 * @throws	Exception
	 */
	public ClientMngVO selectDetail(ClientMngVO searchVO) throws Exception {
		return compMngDAO.selectDetail(searchVO);
	}
	
	/**
	 * 수정
	 * @param	ClientMngVO dataVO
	 * @return	int
	 * @throws	Exception
	 */
	public int updateData(ClientMngVO dataVO) throws Exception {
		int resultCnt = 0;
		
		// 언어 설정 삭제 후 새로 insert
		compMngDAO.deleteLang(dataVO);
		for(int i = 0; i < dataVO.getLangChk().length; i++) {
			dataVO.setNewLang(dataVO.getLangChk()[i]);
			compMngDAO.insertLang(dataVO);
		}
		
		resultCnt = compMngDAO.updateData(dataVO);
		
		if(resultCnt > 0) {
			dataVO.setIsSuper("N");
			dataVO.setTemplateId(PropertyUtil.getProperty("Template.CompId"));

			dataVO.setTargetDbId((String)SessionUtil.getAttribute("compDbId"));
			clientMngDAO.updateLangData(dataVO);
			codeUtilService.insertCodeUpdateLog(dataVO.getNewCompId());
		}
		
		return resultCnt;
	}
}
