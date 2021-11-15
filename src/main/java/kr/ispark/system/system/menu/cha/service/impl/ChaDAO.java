/*************************************************************************
* CLASS 명	: ChaDAO
* 작 업 자	: 하성준
* 작 업 일	: 2021-11-09
* 기	능	: 문화재청 DAO
* ---------------------------- 변 경 이 력 --------------------------------
* 번호	작 업 자		작	업	일			변 경 내 용				비고
* ----	---------	----------------	---------------------	-----------
*	1	하성준		2021-11-09
**************************************************************************/
package kr.ispark.system.system.menu.cha.service.impl;


import java.util.List;

import kr.ispark.system.system.survey.survReg.service.SurvRegVO;
import org.springframework.stereotype.Repository;

import egovframework.com.cmm.service.impl.EgovComAbstractDAO;
import kr.ispark.system.system.menu.cha.service.ChaVO;

@Repository
public class ChaDAO extends EgovComAbstractDAO {
	/**
	 * 문화재청 목록 조회
	 * @param	ChaVO searchVO
	 * @return	List<ChaVO>
	 * @throws	Exception
	 */
	public List<ChaVO> selectList(ChaVO searchVO) throws Exception {
		return selectList("system.menu.cha.selectList", searchVO);
	}
	
	/**
	 * 문화재청 상세 조회
	 * @param	ChaVO searchVO
	 * @return	ChaVO
	 * @throws	Exception
	 */
	public ChaVO selectDetail(ChaVO searchVO) throws Exception {
		return (ChaVO)selectOne("system.menu.cha.selectDetail", searchVO);
	}
	
	/**
	 * 문화재청 정렬순서저장
	 * @param	ChaVO searchVO
	 * @return	int
	 * @throws	Exception
	 */
	public int updateSortOrder(ChaVO searchVO) throws Exception {
		return update("system.menu.cha.updateSortOrder", searchVO);
	}

	/**
	 * 문화재청 삭제
	 * @param	ChaVO searchVO
	 * @return	int
	 * @throws	Exception
	 */
	public int deleteCha(ChaVO searchVO) throws Exception {
		return update("system.menu.cha.deleteCha", searchVO);
	}
	
	/**
	 * 문화재청 저장(임무)
	 * @param	ChaVO searchVO
	 * @return	int
	 * @throws	Exception
	 */
	public int insertData(ChaVO searchVO) throws Exception {
		System.out.println("저장 DAO");
		System.out.println("searchVO : " + searchVO);
		return insert("system.menu.cha.insertData", searchVO);
	}

	/**
	 * 문화재청 저장(임무첨부파일)
	 * @param	ChaVO searchVO
	 * @return	int
	 * @throws	Exception
	 */
	public String insertData11(ChaVO searchVO) throws Exception {
		System.out.println("저장 DAO");
		System.out.println("searchVO : " + searchVO);
		insert("system.menu.cha.insertData11", searchVO);


		System.out.println("DAO 종료 : " + searchVO.getFindYear());
		return searchVO.getFindYear();
	}

	/**
	 * 문화재청 저장(비전)
	 * @param	ChaVO searchVO
	 * @return	int
	 * @throws	Exception
	 */
	public int insertData2(ChaVO searchVO) throws Exception {
		System.out.println("저장 DAO");
		System.out.println("searchVO : " + searchVO);
		return insert("system.menu.cha.insertData2", searchVO);
	}

	/**
	 * 문화재청 저장(임무첨부파일)
	 * @param	ChaVO searchVO
	 * @return	int
	 * @throws	Exception
	 */
	public String insertData22(ChaVO searchVO) throws Exception {
		System.out.println("저장 DAO");
		System.out.println("searchVO : " + searchVO);
		insert("system.menu.cha.insertData22", searchVO);


		System.out.println("DAO 종료 : " + searchVO.getFindYear());
		return searchVO.getFindYear();
	}

	/**
	 * 문화재청 저장(전략목표 윗부분)
	 * @param	ChaVO searchVO
	 * @return	int
	 * @throws	Exception
	 */
	public int insertData3(ChaVO searchVO) throws Exception {

		int resultCnt = 0 ;

		System.out.println("위저장 DAO");
		System.out.println("searchVO : " + searchVO);

		//resultCnt += insert("system.menu.cha.insertData33", searchVO);
		resultCnt += insert("system.menu.cha.insertData3", searchVO);

		return resultCnt;
	}

	/**
	 * 문화재청 저장(전략목표 아래부분)
	 * @param	ChaVO searchVO
	 * @return	int
	 * @throws	Exception
	 */
	public int insertData33(ChaVO searchVO) throws Exception {

		int resultCnt = 1 ;

		System.out.println("아래저장 DAO");
		System.out.println("searchVO : " + searchVO);

		resultCnt += insert("system.menu.cha.insertData33", searchVO);
		//resultCnt += insert("system.menu.cha.insertData3", searchVO);

		return resultCnt;
	}

	/**
	 * 문화재청 저장(성과목표 윗부분)
	 * @param	ChaVO searchVO
	 * @return	int
	 * @throws	Exception
	 */
	public int insertData4(ChaVO searchVO) throws Exception {

		int resultCnt = 0 ;

		System.out.println("위저장 DAO");
		System.out.println("searchVO : " + searchVO);

		//resultCnt += insert("system.menu.cha.insertData33", searchVO);
		resultCnt += insert("system.menu.cha.insertData4", searchVO);

		return resultCnt;
	}

	/**
	 * 문화재청 저장(전략목표 아래부분)
	 * @param	ChaVO searchVO
	 * @return	int
	 * @throws	Exception
	 */
	public int insertData44(ChaVO searchVO) throws Exception {

		int resultCnt = 1 ;

		System.out.println("아래저장 DAO");
		System.out.println("searchVO : " + searchVO);

		resultCnt += insert("system.menu.cha.insertData44", searchVO);
		//resultCnt += insert("system.menu.cha.insertData3", searchVO);

		return resultCnt;
	}

	/**
	 * 문화재청 수정(임무)
	 * @param	ChaVO searchVO
	 * @return	int
	 * @throws	Exception
	 */
	public int updateData(ChaVO searchVO) throws Exception {
		System.out.println("수정 DAO");
		System.out.println("searchVO : " + searchVO);
		return update("system.menu.cha.updateData", searchVO);
	}

	/**
	 * 문화재청 수정(임무 첨부파일)
	 * @param	ChaVO searchVO
	 * @return	int
	 * @throws	Exception
	 */
	public String updateData11(ChaVO searchVO) throws Exception {
		System.out.println("수정 DAO");
		System.out.println("searchVO : " + searchVO);
		update("system.menu.cha.updateData11", searchVO);
		return searchVO.getFindYear();
	}

	/**
	 * 문화재청 수정(비전)
	 * @param	ChaVO searchVO
	 * @return	int
	 * @throws	Exception
	 */
	public int updateData2(ChaVO searchVO) throws Exception {
		System.out.println("수정 DAO");
		System.out.println("searchVO : " + searchVO);
		return update("system.menu.cha.updateData2", searchVO);
	}

	/**
	 * 문화재청 수정(비전 첨부파일)
	 * @param	ChaVO searchVO
	 * @return	int
	 * @throws	Exception
	 */
	public String updateData22(ChaVO searchVO) throws Exception {
		System.out.println("수정 DAO");
		System.out.println("searchVO : " + searchVO);
		update("system.menu.cha.updateData22", searchVO);
		return searchVO.getFindYear();
	}

	/**
	 * 콤보박스용ㅇ..
	 */
	public List<ChaVO> selectList9(ChaVO searchVO) throws Exception {
		System.out.println("안되나?33333333333333333");
		return selectList("system.menu.cha.selectList9", searchVO);
	}

	/**
	 * 문화재청 상세 조회
	 * @param	ChaVO searchVO
	 * @return	ChaVO
	 * @throws	Exception
	 */
	public ChaVO selectDetail7(ChaVO searchVO) throws Exception {
		return (ChaVO)selectOne("system.menu.cha.selectDetail7", searchVO);
	}
}

