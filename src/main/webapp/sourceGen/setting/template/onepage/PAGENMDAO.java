/*************************************************************************
* CLASS 명	: nonCamelPageNmDAO
* 작 업 자	: devNm
* 작 업 일	: devDate
* 기	능	: koPageNm DAO
* ---------------------------- 변 경 이 력 --------------------------------
* 번호	작 업 자		작	업	일			변 경 내 용				비고
* ----	---------	----------------	---------------------	-----------
*	1	devNm		devDate
**************************************************************************/
package fullPackageDotPath.service.impl;


import java.util.List;

import org.springframework.stereotype.Repository;

import egovframework.com.cmm.service.impl.EgovComAbstractDAO;
import fullPackageDotPath.service.nonCamelPageNmVO;

@Repository
public class nonCamelPageNmDAO extends EgovComAbstractDAO {
	/**
	 * koPageNm 목록 조회
	 * @param	nonCamelPageNmVO searchVO
	 * @return	List<nonCamelPageNmVO>
	 * @throws	Exception
	 */
	public List<nonCamelPageNmVO> selectList(nonCamelPageNmVO searchVO) throws Exception {
		return selectList("queryNamePath.selectList", searchVO);
	}
	
	/**
	 * koPageNm 상세 조회
	 * @param	nonCamelPageNmVO searchVO
	 * @return	nonCamelPageNmVO
	 * @throws	Exception
	 */
	public nonCamelPageNmVO selectDetail(nonCamelPageNmVO searchVO) throws Exception {
		return (nonCamelPageNmVO)selectOne("queryNamePath.selectDetail", searchVO);
	}
	
	/**
	 * koPageNm 정렬순서저장
	 * @param	nonCamelPageNmVO searchVO
	 * @return	int
	 * @throws	Exception
	 */
	public int updateSortOrder(nonCamelPageNmVO searchVO) throws Exception {
		return update("queryNamePath.updateSortOrder", searchVO);
	}

	/**
	 * koPageNm 삭제
	 * @param	nonCamelPageNmVO searchVO
	 * @return	int
	 * @throws	Exception
	 */
	public int deletenonCamelPageNm(nonCamelPageNmVO searchVO) throws Exception {
		return update("queryNamePath.deletenonCamelPageNm", searchVO);
	}
	
	/**
	 * koPageNm 저장
	 * @param	nonCamelPageNmVO searchVO
	 * @return	int
	 * @throws	Exception
	 */
	public int insertData(nonCamelPageNmVO searchVO) throws Exception {
		return insert("queryNamePath.insertData", searchVO);
	}

	/**
	 * koPageNm 수정
	 * @param	nonCamelPageNmVO searchVO
	 * @return	int
	 * @throws	Exception
	 */
	public int updateData(nonCamelPageNmVO searchVO) throws Exception {
		return update("queryNamePath.updateData", searchVO);
	}
}
