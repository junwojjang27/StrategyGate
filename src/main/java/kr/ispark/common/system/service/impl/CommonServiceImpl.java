/*************************************************************************
* CLASS 명	: CommonServiceImpl
* 작 업 자	: kimyh
* 작 업 일	: 2018. 01. 24.
* 기	능	: 공통모듈 service
* ---------------------------- 변 경 이 력 --------------------------------
* 번호	작 업 자		작	업	일			변 경 내 용				비고
* ----	---------	----------------	---------------------	-----------
*	1	kimyh		2018. 01. 24.		최 초 작 업
**************************************************************************/
package kr.ispark.common.system.service.impl;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.annotation.Resource;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import egovframework.com.cmm.EgovMessageSource;
import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;
import egovframework.rte.psl.dataaccess.util.EgovMap;
import kr.ispark.bsc.base.scDept.scDeptMng.service.impl.ScDeptMngServiceImpl;
import kr.ispark.common.CommonVO;
import kr.ispark.common.security.service.UserVO;
import kr.ispark.common.system.service.DeptVO;
import kr.ispark.common.system.service.MenuVO;
import kr.ispark.common.system.service.ScDeptVO;
import kr.ispark.common.util.CommonUtil;
import kr.ispark.common.util.PropertyUtil;
import kr.ispark.common.util.SessionUtil;

@Service
public class CommonServiceImpl extends EgovAbstractServiceImpl {
	protected Logger log = LoggerFactory.getLogger(this.getClass());

	@Resource
	private CommonDAO commonDAO;

	@Autowired
	private ScDeptMngServiceImpl scDeptMngService;

	@Resource(name="egovMessageSource")
	public EgovMessageSource egovMessageSource;

	@Autowired
	private IdGenServiceImpl idgenService;

	// 메일발송 설정들
	private final String MAIL_HOST = PropertyUtil.getProperty("mail.smtp.host");
	private final String MAIL_PORT = PropertyUtil.getProperty("mail.smtp.port");
	private final String MAIL_USERNAME = PropertyUtil.getProperty("mail.smtp.userName");
	private final String MAIL_PASSWORD = PropertyUtil.getProperty("mail.smtp.password");

	/**
	 * 모니터링 권한에 따른 최상위 성과조직ID 조회 (성과조직 tree 생성용)
	 * @param	EgovMap egovMap
	 * @return	String
	 * @throws	SQLException
	 */
	public String selectMonitoringRootScDeptId(CommonVO searchVO) throws SQLException {
		EgovMap egovMap = new EgovMap();
		egovMap.put("rootScDeptId", PropertyUtil.getProperty("default.rootScDeptId"));
		egovMap.put("userId", SessionUtil.getUserId());
		egovMap.put("findYear", searchVO.getFindYear());

		return CommonUtil.removeNull(commonDAO.selectMonitoringRootScDeptId(egovMap), PropertyUtil.getProperty("default.rootScDeptId"));
	}

	/**
	 * 모니터링 권한에 따른 최상위 성과조직ID 조회 (like 조회용)
	 * @param	EgovMap egovMap
	 * @return	String
	 * @throws	SQLException
	 */
	public String selectMonitoringRootScDeptIdForSearch(CommonVO searchVO) throws SQLException {
		EgovMap egovMap = new EgovMap();
		egovMap.put("userId", SessionUtil.getUserId());
		egovMap.put("findYear", searchVO.getFindYear());

		return commonDAO.selectMonitoringRootScDeptId(egovMap);
	}

	/**
	 * 성과조직 목록 조회
	 * @param	CommonVO searchVO
	 * @return	List<ScDeptVO>
	 * @throws	SQLException
	 */
	public List<ScDeptVO> selectScDeptList(CommonVO searchVO) throws Exception {
		List<ScDeptVO> scDeptList = commonDAO.selectScDeptList(searchVO);

		// 성과조직이 없으면 회사명으로 최상위 성과조직을 생성
		if(scDeptList.size() == 0) {
			ScDeptVO scDeptVO = new ScDeptVO();
			scDeptVO.setYear(searchVO.getFindYear());
			scDeptVO.setScDeptId(PropertyUtil.getProperty("default.rootScDeptId"));
			idgenService.selectNextSeqByYear("BSC_SC_DEPT", searchVO.getFindYear(), "D", 6, "0");
			commonDAO.insertRootScDeptBySystem(scDeptVO);

			scDeptList = commonDAO.selectScDeptList(searchVO);
		}
		return scDeptList;
	}

	/**
	 * 조직 목록 조회
	 * @param	CommonVO searchVO
	 * @return	List<DeptVO>
	 * @throws	SQLException
	 */
	public List<DeptVO> selectDeptList(CommonVO searchVO) throws Exception {
		List<DeptVO> deptList = commonDAO.selectDeptList(searchVO);

		// 조직이 없으면 회사명으로 최상위 조직을 생성
		if(deptList.size() == 0) {
			commonDAO.insertRootDeptBySystem(searchVO);
			deptList = commonDAO.selectDeptList(searchVO);
		}
		return deptList;
	}

	/**
	 * 사용자 목록 조회
	 * @param	UserVO searchVO
	 * @return	List<UserVO>
	 * @throws	SQLException
	 */
	public List<UserVO> selectUserList(UserVO searchVO) throws SQLException {
		return commonDAO.selectUserList(searchVO);
	}

	/**
	 * 즐겨찾기 목록 조회
	 * @param	MenuVO searchVO
	 * @return	List<MenuVO>
	 * @throws	SQLException
	 */
	public List<MenuVO> selectBookmarkList(MenuVO searchVO) throws SQLException {
		searchVO.setUserId(SessionUtil.getUserId());
		return commonDAO.selectBookmarkList(searchVO);
	}

	/**
	 * 즐겨찾기 삭제
	 * @param	MenuVO dataVO
	 * @return	int
	 * @throws	SQLException
	 */
	public int deleteBookmark(MenuVO dataVO) throws SQLException {
		dataVO.setUserId(SessionUtil.getUserId());
		return commonDAO.deleteBookmark(dataVO);
	}

	/**
	 * 즐겨찾기 등록
	 * @param	MenuVO dataVO
	 * @return	int
	 * @throws	Exception
	 */
	public int insertBookmark(MenuVO dataVO) throws Exception {
		dataVO.setUserId(SessionUtil.getUserId());

		// 즐겨찾기 설정 가능한 메뉴인지 확인
		if(commonDAO.selectCheckPgmId(dataVO) == 0) {
			throw new Exception(egovMessageSource.getMessage("info.noAuth.msg"));
		}

		// 기존에 등록된 즐겨찾기에서 해당 메뉴 삭제 (중복 등록 방지를 위함)
		dataVO.getKeys().add(dataVO.getPgmId());
		commonDAO.deleteBookmark(dataVO);

		return commonDAO.insertBookmark(dataVO);
	}

	/**
	 * 승인사용설정 확인
	 * @param	CommonVO searchVO
	 * @return	EgovMap
	 * @throws	Exception
	 */
	public EgovMap selectApproveUse(CommonVO searchVO) throws Exception {
		EgovMap resultMap = commonDAO.selectApproveUse(searchVO);
		if(resultMap == null) {
			resultMap = new EgovMap();
			resultMap.put("metricApproveUseYn", "Y");
			resultMap.put("actApproveUseYn", "Y");
		}
		return resultMap;
	}
	public EgovMap selectApproveUseByDataVO(CommonVO dataVO) throws Exception {
		CommonVO searchVO = new CommonVO();
		searchVO.setFindYear(dataVO.getYear());
		return selectApproveUse(searchVO);
	}

	/**
	 * 지표승인사용여부 확인
	 * @param	CommonVO searchVO
	 * @return	EgovMap
	 * @throws	Exception
	 */
	public String selectMetricApproveUseYn(CommonVO searchVO) throws Exception {
		return CommonUtil.removeNull(selectApproveUse(searchVO).get("metricApproveUseYn"));
	}
	public String selectMetricApproveUseYnByDataVO(CommonVO dataVO) throws Exception {
		return CommonUtil.removeNull(selectApproveUseByDataVO(dataVO).get("metricApproveUseYn"));
	}

	/**
	 * 실적승인사용여부 확인
	 * @param	CommonVO searchVO
	 * @return	EgovMap
	 * @throws	Exception
	 */
	public String selectActApproveUseYn(CommonVO searchVO) throws Exception {
		return CommonUtil.removeNull(selectApproveUse(searchVO).get("actApproveUseYn"));
	}
	public String selectActApproveUseYnByDataVO(CommonVO dataVO) throws Exception {
		return CommonUtil.removeNull(selectApproveUseByDataVO(dataVO).get("actApproveUseYn"));
	}

	/**
	 * 성과조직 정보 조회
	 * @param	String findYear
	 * @param	String scDeptId
	 * @return	ScDeptVO
	 * @throws	Exception
	 */
	public ScDeptVO getScDeptInfo(String findYear, String scDeptId) throws Exception {
		ScDeptVO searchVO = new ScDeptVO();
		searchVO.setFindYear(findYear);
		searchVO.setScDeptId(scDeptId);
		return scDeptMngService.selectDetail(searchVO);
	}

	/**
	 * 사용자 email 조회
	 * @param	String userId
	 * @return	String
	 * @throws	Exception
	 */
	public String getUserEmail(String userId) throws Exception {
		CommonVO searchVO = new ScDeptVO();
		searchVO.setUserId(userId);
		return CommonUtil.removeNull(commonDAO.selectUserEmail(searchVO));
	}

	/**
	 * 부서장 email 조회
	 * @param	String findYear
	 * @param	String scDeptId
	 * @return	String
	 * @throws	Exception
	 */
	public String getBscUserEmail(String findYear, String scDeptId) throws Exception {
		ScDeptVO searchVO = new ScDeptVO();
		searchVO.setFindYear(findYear);
		searchVO.setScDeptId(scDeptId);
		searchVO = scDeptMngService.selectDetail(searchVO);
		if(searchVO == null) {
			return null;
		} else {
			return searchVO.getBscUserEmail();
		}
	}

	/**
	 * 지표담당자 email 조회
	 * @param	String findYear
	 * @param	String scDeptId
	 * @return	String
	 * @throws	Exception
	 */
	public String getManagerUserEmail(String findYear, String scDeptId) throws Exception {
		ScDeptVO searchVO = new ScDeptVO();
		searchVO.setFindYear(findYear);
		searchVO.setScDeptId(scDeptId);
		searchVO = scDeptMngService.selectDetail(searchVO);
		if(searchVO == null) {
			return null;
		} else {
			return searchVO.getManagerUserEmail();
		}
	}

	/**
	 * userId로 메일을 보냄
	 * @param	String title
	 * @param	String contents
	 * @param	String userId 또는 String[] userIds 또는 List<String> userIdList
	 * @return
	 */
	public int sendMail(String title, String contents, final String userId) {
		ArrayList<String> userIdList = new ArrayList<String>();
		userIdList.add(userId);
		return sendMail(title, contents, userIdList);
	}
	public int sendMail(String title, String contents, String[] userIds) {
		return sendMail(title, contents, Arrays.asList(userIds));
	}
	public int sendMail(String title, String contents, List<String> userIdList) {
		int sendCnt = 0;

		Properties props = System.getProperties();
		
		props.put("mail.smtp.host", MAIL_HOST);
		props.put("mail.smtp.port", MAIL_PORT);
		props.put("mail.smtp.auth", "true");
		props.put("mail.transport.protocol", "smtp");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.ssl.enable", "true");
		props.put("mail.smtp.ssl.trust", MAIL_HOST);
		
		Session session = Session.getDefaultInstance(props);

		Transport transport = null;

		try {
			MimeMessage msg = new MimeMessage(session);
			UserVO loginUserVO = SessionUtil.getUserVO();
			msg.setFrom(new InternetAddress(loginUserVO!=null?loginUserVO.getEmail():null, loginUserVO!=null?loginUserVO.getUserNm():null));
			msg.setSubject(title);

			//msg.setContent(contents, "text/html;charset=UTF-8");	// 본문에 HTML을 허용할 경우
			msg.setText(contents, "UTF-8");	// 본문에 HTML을 허용하지 않을 경우

			Map<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put("userIdList", userIdList);
			List<String> emailList = commonDAO.selectUserEmailList(paramMap);

			if(CommonUtil.isEmpty(emailList)) {
				return 0;
			}

			for(String email : emailList) {
				if(CommonUtil.isNotEmpty(email)) {
					msg.addRecipient(Message.RecipientType.TO, new InternetAddress(email));
					sendCnt++;
				}
			}

			log.debug("# Email Sending...");

			transport = session.getTransport();
			transport.connect(MAIL_HOST, MAIL_USERNAME, MAIL_PASSWORD);
			transport.sendMessage(msg, msg.getAllRecipients());

			log.debug("# Email sent!");
		} catch (IOException ie){
			log.error("error : "+ie.getCause());
			sendCnt = -1;
		} catch (Exception e) {
			log.error("error : "+e.getCause());
			sendCnt = -1;
		} finally {
			if(transport != null) {
				try {
					transport.close();
				} catch (MessagingException e) {
					log.error(e.getMessage());
				}
			}
		}

		return sendCnt;
	}

	/**
	 * 사용자의 성과조직 정보 조회
	 * @param	CommonVO searchVO
	 * @return	ScDeptVO
	 * @throws	Exception
	 */
	public ScDeptVO selectScDeptByUser(CommonVO searchVO) throws Exception {
		searchVO.setUserId(SessionUtil.getUserId());
		ScDeptVO scDeptVO = commonDAO.selectScDeptByUser(searchVO);
		if(scDeptVO == null) {
			scDeptVO = new ScDeptVO();
		}
		return scDeptVO;
	}

	/**
	 * 성과조직 평가군ID 조회
	 * @param	CommonVO searchVO
	 * @return	String
	 * @throws	Exception
	 */
	public String selectScDeptGrpId(CommonVO searchVO) throws Exception {
		return commonDAO.selectScDeptGrpId(searchVO);
	}

	/**
	 * 실적 월마감 조회
	 * @param	String findYear
	 * @param	String findMon
	 * @return	String
	 * @throws	Exception
	 */
	public String selectActualCloseYn(CommonVO dataVO) throws Exception {
		return commonDAO.selectActualCloseYn(dataVO);
	}

	/**
	 * 최상위조직  정조 조회
	 * @param	String findYear
	 * @return	ScDeptVO
	 * @throws	Exception
	 */
	public ScDeptVO selectTopScDeptInfo(String findYear) throws Exception {
		return commonDAO.selectTopScDeptInfo(findYear);
	}

	/**
	 * 디자인 테마 조회
	 * @param	CommonVO searchVO
	 * @return	String
	 * @throws	Exception
	 */
	public String selectTheme(CommonVO searchVO) throws Exception {
		return commonDAO.selectTheme(searchVO);
	}
	
	public List<MenuVO> selectProcessList(MenuVO searchVO) throws Exception {
		return commonDAO.selectProcessList(searchVO);
	}
	
	public int updateProcessData(MenuVO dataVO) throws Exception {
		int resultCnt = 0;
		resultCnt += commonDAO.deleteProcessData(dataVO);
		resultCnt += commonDAO.insertProcessData(dataVO);
		
		return resultCnt;
	}
	
	public List<CommonVO> selectDbList() throws Exception {
		return commonDAO.selectDbList();
	}
	
	public List<CommonVO> selectDbResetList(EgovMap emap) throws Exception {
		
		List<CommonVO> dbList = commonDAO.selectDbResetList(emap);
		boolean isOk = true;
		
		if(dbList != null && dbList.size() > 0){
			for(CommonVO cvo : dbList){
				if(!isConn(cvo)){
					isOk = false;
					break;
				}
			}
		}
		if(!isOk){
			return null;
		}else{
			return commonDAO.selectDbResetList(emap);
		}
	}
	
	public String selectDbId(EgovMap emap) throws Exception {
		return commonDAO.selectDbId(emap);
	}
	
	public boolean isConn(CommonVO cvo){
		boolean isOk = true;
		Connection conn = null;
		try{
			Class.forName(cvo.getDbDriver());
			conn = DriverManager.getConnection(cvo.getDbUrl(), cvo.getDbUserId(), cvo.getDbUserPasswd());
		}catch(SQLException sqe){
			isOk = false;
			sqe.getCause();
		}catch(Exception e){
			isOk = false;
			e.getCause();
		}finally{
			try{
				if(conn == null){
					isOk = false;
					log.debug("!!!!!!!loop : "+cvo.getDbUrl()+":"+cvo.getDbUserId()+":"+cvo.getDbUserPasswd()+" --> isconn : " + isOk);
				}else{
					log.debug("!!!!!!!loop : "+cvo.getDbUrl()+":"+cvo.getDbUserId()+":"+cvo.getDbUserPasswd()+" --> isconn : " + isOk);
					conn.close();
				}
			}catch(SQLException e){
				e.getCause();
			}
		}
		
		return isOk;
		
	}
}
