/*************************************************************************
* CLASS 명	: ClilentMngServiceImpl
* 작 업 자	: kimyh
* 작 업 일	: 2018. 07. 03.
* 기	능	: 고객사 관리 Service
* ---------------------------- 변 경 이 력 --------------------------------
* 번호	작 업 자		작	업	일			변 경 내 용				비고
* ----	---------	----------------	---------------------	-----------
*	1	kimyh		2018. 07. 03.		최 초 작 업
**************************************************************************/

package kr.ispark.superMng.superMng.superMng.clientMng.service.impl;

import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.StandardPasswordEncoder;
import org.springframework.stereotype.Service;

import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;
import egovframework.rte.psl.dataaccess.util.EgovMap;
import kr.ispark.common.SchemaCreateModule;
import kr.ispark.common.system.service.impl.CommonServiceImpl;
import kr.ispark.common.util.CommonUtil;
import kr.ispark.common.util.PropertyUtil;
import kr.ispark.common.util.service.impl.CodeUtilServiceImpl;
import kr.ispark.superMng.superMng.superMng.clientMng.service.ClientMngVO;

@Service
public class ClientMngServiceImpl extends EgovAbstractServiceImpl {
	public final Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired
	public CodeUtilServiceImpl codeUtilService;

	@Resource
	private ClientMngDAO clientMngDAO;

	@Autowired
	StandardPasswordEncoder passwordEncoder;
	
	@Autowired
	public CommonServiceImpl commonServiceImpl;
	
	@Resource(name="SchemaCreateModule")
	public SchemaCreateModule schemaCreateModule;

	/**
	* 고객사 목록 조회
	* @param	ClientMngVO searchVO
	* @return	List<ClientMngVO>
	* @throws	Exception
	*/
	public List<ClientMngVO> selectList(ClientMngVO searchVO) throws Exception {
		return clientMngDAO.selectList(searchVO);
	}

	/**
	* 고객사 목록 수
	* @param	ClientMngVO searchVO
	* @return	int
	* @throws	Exception
	*/
	public int selectListCount(ClientMngVO searchVO) throws Exception {
		return clientMngDAO.selectListCount(searchVO);
	}

	/**
	* 서비스 중지/사용 처리
	* @param	ClientMngVO dataVO
	* @return	int
	* @throws	Exception
	*/
	public int updateUseYn(ClientMngVO dataVO) throws Exception {
		return clientMngDAO.updateUseYn(dataVO);
	}

	/**
	* 서비스 초기화
	* @param	ClientMngVO dataVO
	* @throws	Exception
	*/
	public void updateReset(ClientMngVO dataVO) throws Exception {
		dataVO.setTemplateId(PropertyUtil.getProperty("Template.CompId"));
		dataVO.setDefaultDeptId(PropertyUtil.getProperty("default.rootDeptId"));
		dataVO.setUserId(dataVO.getNewCompId() + "_admin");
		dataVO.setAdminPassword(passwordEncoder.encode(dataVO.getNewCompId() + "_admin"));
		
		/*스키마 생성 OFFLINE*/
		
		/*데이터 INSERT*/
		// 먼저 newCompId에 대한  DB_ID 를 가져온다
		EgovMap map = new EgovMap();
		map.put("paramCompId",dataVO.getNewCompId());
		dataVO.setTargetDbId(commonServiceImpl.selectDbId(map));
		clientMngDAO.updateReset(dataVO);
		
		/*데이터베이스 업데이트  OFFLINE*/
		
	}
	
	/**
	* 서비스 초기화
	* @param	ClientMngVO dataVO
	* @throws	Exception
	*/
	public void updateDemoReset(ClientMngVO dataVO) throws Exception {
		
		if(!"".equals(CommonUtil.nullToBlank(dataVO.getCopyTargetCompId()))){
			dataVO.setTemplateId(PropertyUtil.getProperty("Template.CompId"));
			dataVO.setDefaultDeptId(PropertyUtil.getProperty("default.rootDeptId"));
			dataVO.setUserId(dataVO.getNewCompId() + "_admin");
			dataVO.setAdminPassword(passwordEncoder.encode(dataVO.getNewCompId() + "_admin"));
			
			/*데이터 INSERT*/
			// 먼저 newCompId에 대한  DB_ID 를 가져온다
			EgovMap map = new EgovMap();
			map.put("paramCompId",dataVO.getNewCompId());
			dataVO.setTargetDbId(commonServiceImpl.selectDbId(map));
			map.put("paramCompId",dataVO.getCopyTargetCompId());
			dataVO.setCopyTargetDbId(commonServiceImpl.selectDbId(map));
			clientMngDAO.updateDemoReset(dataVO);
		}
	}

	/**
	 * 언어 목록 조회
	 * @param	ClientMngVO searchVO
	 * @return	List<ClientMngVO>
	 * @throws	Exception
	 */
	public List<ClientMngVO> langList(ClientMngVO searchVO) throws Exception {
		return clientMngDAO.langList(searchVO);
	}

	/**
	 * 상세 조회
	 * @param	ClientMngVO searchVO
	 * @return	ClientMngVO
	 * @throws	Exception
	 */
	public ClientMngVO selectDetail(ClientMngVO searchVO) throws Exception {
		return clientMngDAO.selectDetail(searchVO);
	}
	
	/**
	 * 데모복사대상 회사 목록 조회
	 * @param	ClientMngVO searchVO
	 * @return	List<ClientMngVO>
	 * @throws	Exception
	 */
	public List<ClientMngVO> selectTargetCompList(ClientMngVO searchVO) throws Exception {
		return clientMngDAO.selectTargetCompList(searchVO);
	}

	/**
	 * ID 중복 체크
	 * @param	ClientMngVO searchVO
	 * @return	int
	 * @throws	Exception
	 */
	public int selectIdCnt(ClientMngVO searchVO) throws Exception {
		// compId로 'TEMPLATE'를 사용하는 것을 방지하기 위한 처리
		if(searchVO.getNewCompId().equals(PropertyUtil.getProperty("Template.CompId"))) {
			return 1;
		}
		return clientMngDAO.selectIdCnt(searchVO);
	}

	/**
	 * 이력 조회
	 * @param	ClientMngVO searchVO
	 * @return	List<ClientMngVO>
	 * @throws	Exception
	 */
	public List<ClientMngVO> selectHistory(ClientMngVO searchVO) throws Exception {
		return clientMngDAO.selectHistory(searchVO);
	}

	/**
	 * 저장
	 * @param	ClientMngVO dataVO
	 * @return	int
	 * @throws	Exception
	 */
	public int insertData(ClientMngVO dataVO) throws Exception {
		for(int i = 0; i < dataVO.getLangChk().length; i++) {
			dataVO.setNewLang(dataVO.getLangChk()[i]);
			clientMngDAO.insertLang(dataVO);
		}
		clientMngDAO.insertDbData(dataVO);
		
		return clientMngDAO.insertData(dataVO);
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
		clientMngDAO.deleteLang(dataVO);
		for(int i = 0; i < dataVO.getLangChk().length; i++) {
			dataVO.setNewLang(dataVO.getLangChk()[i]);
			clientMngDAO.insertLang(dataVO);
		}

		resultCnt = clientMngDAO.updateData(dataVO);

		if(resultCnt > 0) {
			// comp_id 변경시 계약 데이터의 comp_id도 변경
			if(!dataVO.getNewCompId().equals(dataVO.getOldCompId())) {
				resultCnt = clientMngDAO.updateContractCompId(dataVO);
			}

			// 코드, 메뉴 등의 데이터가 존재하는 경우 갱신
			ClientMngVO clientVO = clientMngDAO.selectDetail(dataVO);
			if(CommonUtil.nullToBlank(clientVO.getHasDefaultDataYn()).equals("Y")) {
				if(dataVO.getNewCompId().equals(PropertyUtil.getProperty("Super.CompId"))) {
					dataVO.setIsSuper("Y");
				} else {
					dataVO.setIsSuper("N");
				}

				dataVO.setTemplateId(PropertyUtil.getProperty("Template.CompId"));
				clientMngDAO.updateLangData(dataVO);
				codeUtilService.insertCodeUpdateLog(dataVO.getNewCompId());
			}
		}
		clientMngDAO.deleteDbData(dataVO);
		clientMngDAO.insertDbData(dataVO);
		
		return resultCnt;
	}

	/**
	 * 계약 수정
	 * @param	ClientMngVO dataVO
	 * @return	int
	 * @throws	Exception
	 */
	public int updateContract(ClientMngVO dataVO) throws Exception {

		String serviceType = "";
		if(dataVO.getServiceTypes() != null && dataVO.getServiceTypes().size() > 0){
			for(int i=0 ; i<dataVO.getServiceTypes().size() ; i++){
				serviceType += dataVO.getServiceTypes().get(i);
				if(i+1 != dataVO.getServiceTypes().size()){
					serviceType += ",";
				}
			}
			dataVO.setServiceType(serviceType);
		}

		if(clientMngDAO.selectHistoryCount(dataVO) == 0) {
			return clientMngDAO.insertContract(dataVO);
		} else {
			return clientMngDAO.updateContract(dataVO);
		}
	}

	/**
	 * 계약 이력 CURRNET SEQ 수정
	 * @param	ClientMngVO dataVO
	 * @return	int
	 * @throws	Exception
	 */
	public int updateCurrentSeq(ClientMngVO dataVO) throws Exception {
		return clientMngDAO.updateCurrentSeq(dataVO);
	}

	/**
	 * 계약 저장
	 * @param	ClientMngVO dataVO
	 * @return	int
	 * @throws	Exception
	 */
	public int insertContract(ClientMngVO dataVO) throws Exception {

		String serviceType = "";
		if(dataVO.getServiceTypes() != null && dataVO.getServiceTypes().size() > 0){
			for(int i=0 ; i<dataVO.getServiceTypes().size() ; i++){
				serviceType += dataVO.getServiceTypes().get(i);
				if(i+1 != dataVO.getServiceTypes().size()){
					serviceType += ",";
				}
			}
			dataVO.setServiceType(serviceType);
		}

		return clientMngDAO.insertContract(dataVO);
	}
	
	/**
	 * 저장
	 * @param	ClientMngVO dataVO
	 * @return	int
	 * @throws	Exception
	 */
	public String updateSchemeData(ClientMngVO dataVO) throws Exception {
		
		HashMap<String,String> smap = clientMngDAO.selectSourceDataBaseInfo(dataVO);
		HashMap<String,String> tmap = clientMngDAO.selectTargetDataBaseInfo(dataVO);
		
		String msg = schemaCreateModule.createScheme(smap,tmap,PropertyUtil.getProperty("Globals.DbType"));
		
		return msg;
	}
}
