/*************************************************************************
* CLASS 명	: ScDeptDiagMngServiceImpl
* 작 업 자	: kimyh
* 작 업 일	: 2018. 1. 31.
* 기	능	: 성과조직도 관리 Service
* ---------------------------- 변 경 이 력 --------------------------------
* 번호	작 업 자		작	업	일			변 경 내 용				비고
* ----	---------	----------------	---------------------	-----------
*	1	kimyh		2018. 1. 31.		최 초 작 업
**************************************************************************/
package kr.ispark.bsc.base.scDept.scDeptDiagMng.service.impl;

import java.math.BigDecimal;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;
import kr.ispark.bsc.base.scDept.scDeptDiagMng.service.ScDeptDiagMngVO;

@Service
public class ScDeptDiagMngServiceImpl extends EgovAbstractServiceImpl {

	@Resource
	private ScDeptDiagMngDAO scDeptDiagMngDAO;

	/**
	 * 성과조직 목록 조회
	 * @param	ScDeptDiagMngVO searchVO
	 * @return	List<ScDeptDiagMngVO>
	 * @throws	Exception
	 */
	public List<ScDeptDiagMngVO> selectList(ScDeptDiagMngVO searchVO) throws Exception {
		return scDeptDiagMngDAO.selectList(searchVO);
	}

	/**
	 * 신호등 조회
	 * @param	ScDeptDiagMngVO searchVO
	 * @return	List<ScDeptDiagMngVO>
	 * @throws	Exception
	 */
	public List<ScDeptDiagMngVO> selectSignalList(ScDeptDiagMngVO searchVO) throws Exception {
		return scDeptDiagMngDAO.selectSignalList(searchVO);
	}

	/**
	 * 성과조직도 조회 조회
	 * @param	ScDeptDiagMngVO searchVO
	 * @return	List<ScDeptDiagMngVO>
	 * @throws	Exception
	 */
	public List<ScDeptDiagMngVO> getList(ScDeptDiagMngVO searchVO) throws Exception {
		return scDeptDiagMngDAO.getList(searchVO);
	}

	/**
	 * 신호등 조회
	 * @param	ScDeptDiagMngVO searchVO
	 * @return	List<ScDeptDiagMngVO>
	 * @throws	Exception
	 */
	public List<ScDeptDiagMngVO> getSignal(ScDeptDiagMngVO searchVO) throws Exception {
		return scDeptDiagMngDAO.getSignal(searchVO);
	}

	/**
	 * 좌표 등록 여부 확인
	 * @param	ScDeptDiagMngVO searchVO
	 * @return	int
	 * @throws	Exception
	 */
	public int selectOrgChartCnt(ScDeptDiagMngVO searchVO) throws Exception {
		return scDeptDiagMngDAO.selectOrgChartCnt(searchVO);
	}

	/**
	 * 조직성과도 좌표수정
	 */
	public int insertData(ScDeptDiagMngVO dataVO) throws Exception {
		int resultCnt = 0;
		String codes = dataVO.getCode();
		String arr_code[] 	= codes.split(";");

		//좌표를 저장하기 전에 모든것을 삭제하도록 하겠습니다 ~
		//트렌젝션을 위해서 서비스단에서 처리할게요.싫으면 컨트롤러로 빼도록 만약에 그렇게 된다면 데이터마가 삭제하고 실질적으로 저장하는데서 에러가 나도, 데이터가 삭제되버리니 참고바람
		scDeptDiagMngDAO.deleteData(dataVO);

		if(null != arr_code){
			for(int i=0; i< arr_code.length;i++){
				if(arr_code[i].trim().length()>0){
					String arr_values[] = arr_code[i].trim().split(",");
					String dwDeptId	   	= arr_values[0].trim();
					String subDeptXPos   	= arr_values[1].trim();
					String subDeptYPos   	= arr_values[2].trim();

					dwDeptId	 	= dwDeptId.substring(dwDeptId.indexOf('=') + 1);
					subDeptXPos 	= subDeptXPos.substring(subDeptXPos.indexOf('=') + 1);
					subDeptYPos 	= subDeptYPos.substring(subDeptYPos.indexOf('=') + 1);

					dataVO.setDwDeptId(dwDeptId);
					dataVO.setSubDeptXPos(new BigDecimal(subDeptXPos));
					dataVO.setSubDeptYPos(new BigDecimal(subDeptYPos));

					resultCnt += scDeptDiagMngDAO.insertData(dataVO);
				}
			}
		}

		return resultCnt;
	}
}
