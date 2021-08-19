/*************************************************************************
* CLASS 명	: nonCamelPageNmServiceIpml
* 작 업 자	: devNm
* 작 업 일	: devDate
* 기	능	: koPageNm ServiceIpml
* ---------------------------- 변 경 이 력 --------------------------------
* 번호	작 업 자		작	업	일			변 경 내 용				비고
* ----	---------	----------------	---------------------	-----------
*	1	devNm		devDate
**************************************************************************/
package fullPackageDotPath.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;
import fullPackageDotPath.service.nonCamelPageNmVO;
import kr.ispark.common.system.service.impl.IdGenServiceImpl;
import kr.ispark.common.util.CommonUtil;

@Service
public class nonCamelPageNmServiceImpl extends EgovAbstractServiceImpl {

	@Resource
	private IdGenServiceImpl idgenService;
	
	@Resource
	private nonCamelPageNmDAO camelPageNmDAO;

	/**
	 * koPageNm 목록 조회
	 * @param	nonCamelPageNmVO searchVO
	 * @return	List<nonCamelPageNmVO>
	 * @throws	Exception
	 */
	public List<nonCamelPageNmVO> selectList(nonCamelPageNmVO searchVO) throws Exception {
		return camelPageNmDAO.selectList(searchVO);
	}
	
	/**
	 * koPageNm 상세 조회
	 * @param	nonCamelPageNmVO searchVO
	 * @return	nonCamelPageNmVO
	 * @throws	Exception
	 */
	public nonCamelPageNmVO selectDetail(nonCamelPageNmVO searchVO) throws Exception {
		return camelPageNmDAO.selectDetail(searchVO);
	}
	
	/**
	 * koPageNm 정렬순서저장
	 * @param	nonCamelPageNmVO searchVO
	 * @return	int
	 * @throws	Exception
	 */
	public int updateSortOrder(nonCamelPageNmVO dataVO) throws Exception {
		int resultCnt = 0;
		for(nonCamelPageNmVO paramVO : dataVO.getGridDataList()) {
			resultCnt += camelPageNmDAO.updateSortOrder(paramVO);
		}
		return resultCnt;
	}

	/**
	 * koPageNm 삭제
	 * @param	nonCamelPageNmVO searchVO
	 * @return	int
	 * @throws	Exception
	 */
	public int deletenonCamelPageNm(nonCamelPageNmVO dataVO) throws Exception {
		return camelPageNmDAO.deletenonCamelPageNm(dataVO);
	}
	
	/**
	 * koPageNm 저장
	 * @param	nonCamelPageNmVO searchVO
	 * @return	int
	 * @throws	Exception
	 */
	public int saveData(nonCamelPageNmVO dataVO) throws Exception {
		String key = "";
		if(CommonUtil.isEmpty(dataVO.getnonCamelLastKey())) {
			key = idgenService.selectNextSeqByYear("originalTableName", dataVO.getYear(), "S", 6, "0");
			dataVO.setnonCamelLastKey(key);
			return camelPageNmDAO.insertData(dataVO);
		} else {
			return camelPageNmDAO.updateData(dataVO);
		}
	}
}
