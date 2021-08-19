/*************************************************************************
* CLASS 명	: SurvProgStatServiceIpml
* 작 업 자	: 최학룡
* 작 업 일	: 2018-10-18
* 기	능	: 설문진행현황 ServiceIpml
* ---------------------------- 변 경 이 력 --------------------------------
* 번호	작 업 자		작	업	일			변 경 내 용				비고
* ----	---------	----------------	---------------------	-----------
*	1	최학룡		2018-10-18
**************************************************************************/
package kr.ispark.system.system.survey.survProgStat.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;
import kr.ispark.common.security.service.UserVO;
import kr.ispark.common.system.service.impl.CommonServiceImpl;
import kr.ispark.common.system.service.impl.IdGenServiceImpl;
//import kr.ispark.common.util.sendMailUtil;
import kr.ispark.system.system.survey.survProgStat.service.SurvProgStatVO;

@Service
public class SurvProgStatServiceImpl extends EgovAbstractServiceImpl {

	@Autowired
	private CommonServiceImpl commonService;
	
	@Resource
	private IdGenServiceImpl idgenService;

	@Resource
	private SurvProgStatDAO survProgStatDAO;

	/**
	 * 설문진행현황 목록 조회
	 * @param	SurvProgStatVO searchVO
	 * @return	List<SurvProgStatVO>
	 * @throws	Exception
	 */
	public List<SurvProgStatVO> selectList(SurvProgStatVO searchVO) throws Exception {
		return survProgStatDAO.selectList(searchVO);
	}

	/**
	 * 설문진행현황 메일발송
	 * @param	SurvProgStatVO searchVO
	 * @return	int
	 * @throws	Exception
	 */
	public int selectSendMail(String title, String contents, SurvProgStatVO dataVO) throws Exception {
		List<UserVO> userList = survProgStatDAO.selectSendMail(dataVO);
		ArrayList<String> userIdList = new ArrayList<String>();
		for (UserVO i : userList) {
			userIdList.add(i.getUserId());
		}
		return commonService.sendMail(title, contents, userIdList);
//		int sendCnt = 0;
//		List<UserVO> userList = survProgStatDAO.selectSendMail(dataVO);
//		for(UserVO vo : userList) {
//			//메일발송
//			String subject = "설문조사 미응답자 참여 안내 메일";
//			String message = "<body><div>설문 참여 부탁드립니다.</div></body>";
//			String senderMail = "hrchoi@ispark.co.kr";
//			String senderName = "성과관리시스템";
////			sendCnt += sendMailUtil.sendMail(subject, message, vo.getEmail(), senderMail, senderName);
//		}
//		return sendCnt;
	}

	/**
	 * 설문진행현황 마감
	 * @param	SurvProgStatVO searchVO
	 * @return	int
	 * @throws	Exception
	 */
	public int saveData(SurvProgStatVO dataVO) throws Exception {
		survProgStatDAO.deleteItemTotalData(dataVO);
		if("N".equals(dataVO.getCloseYn())) { // 마감상태가 아닐때 마감치므로 결과취합
			survProgStatDAO.insertItemTotalForAll(dataVO);			// 전체
		}

		return survProgStatDAO.updateData(dataVO);
	}
}

