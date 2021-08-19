/*************************************************************************
* CLASS 명	: NoticeDAO
* 작 업 자	: 박정현
* 작 업 일	: 2018-03-29
* 기	능	: 공지사항 DAO
* ---------------------------- 변 경 이 력 --------------------------------
* 번호	작 업 자		작	업	일			변 경 내 용				비고
* ----	---------	----------------	---------------------	-----------
*	1	박정현		2018-03-29
**************************************************************************/
package kr.ispark.bsc.system.system.notice.service.impl;


import java.util.List;

import org.springframework.stereotype.Repository;

import egovframework.com.cmm.service.impl.EgovComAbstractDAO;
import kr.ispark.bsc.system.system.notice.service.NoticeVO;
import kr.ispark.common.CommonVO;
import kr.ispark.common.util.PropertyUtil;

@Repository
public class NoticeDAO extends EgovComAbstractDAO {
	/**
	 * 공지사항 목록 조회
	 * @param	NoticeVO searchVO
	 * @return	List<NoticeVO>
	 * @throws	Exception
	 */
	public List<NoticeVO> selectList(NoticeVO searchVO) throws Exception {
		return selectList("system.system.notice.selectList", searchVO);
	}

	/**
	 * 공지사항 상세 조회
	 * @param	NoticeVO searchVO
	 * @return	NoticeVO
	 * @throws	Exception
	 */
	public NoticeVO selectDetail(NoticeVO searchVO) throws Exception {
		return (NoticeVO)selectOne("system.system.notice.selectDetail", searchVO);
	}

	/**
	 * 공지사항 삭제
	 * @param	NoticeVO searchVO
	 * @return	int
	 * @throws	Exception
	 */
	public int deleteNotice(NoticeVO searchVO) throws Exception {
		return update("system.system.notice.deleteNotice", searchVO);
	}

	/**
	 * 공지사항 저장
	 * @param	NoticeVO searchVO
	 * @return	int
	 * @throws	Exception
	 */
	public String insertData(NoticeVO dataVO) throws Exception {
		insert("system.system.notice.insertData", dataVO);
		return dataVO.getId();
	}

	/**
	 * 공지사항 수정
	 * @param	NoticeVO searchVO
	 * @return	int
	 * @throws	Exception
	 */
	public int updateData(NoticeVO searchVO) throws Exception {
		return update("system.system.notice.updateData", searchVO);
	}

	/**
	 * 삭제할 게시물에 첨부된 파일ID 목록 조회
	 */
	public List<String> selectAtchFileIdListForDelete(NoticeVO dataVO) throws Exception {
		return selectList("system.system.notice.selectAtchFileIdListForDelete", dataVO);
	}

	/**
	 * 공지사항 목록 조회 (로그인 화면용)
	 * @param	NoticeVO searchVO
	 * @return	List<NoticeVO>
	 * @throws	Exception
	 */
	public List<NoticeVO> selectListForAll(CommonVO searchVO) throws Exception {
		return selectList("system.system.notice.selectListForAll", searchVO);
	}

	/**
	 * 전체 공지사항 상세 조회 (로그인 화면용)
	 * @param	NoticeVO searchVO
	 * @return	NoticeVO
	 * @throws	Exception
	 */
	public NoticeVO selectDetailForAll(NoticeVO searchVO) throws Exception {
		return (NoticeVO)selectOne("system.system.notice.selectDetail", searchVO);
	}

	/**
	 * 팝업 공지사항 목록 조회
	 * @param	CommonVO searchVO
	 * @return	List<NoticeVO>
	 * @throws	Exception
	 */
	public List<NoticeVO> selectPopNoticeList(CommonVO searchVO) throws Exception {
		return selectList("system.system.notice.selectPopNoticeList", searchVO);
	}
}
