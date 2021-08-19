/*************************************************************************
* CLASS 명	: ExampleServiceImpl
* 작 업 자	: kimyh
* 작 업 일	: 2017. 12. 15.
* 기	능	:
* ---------------------------- 변 경 이 력 --------------------------------
* 번호	작 업 자		작	업	일			변 경 내 용				비고
* ----	---------	----------------	---------------------	-----------
*	1	kimyh		2017. 12. 15.		최 초 작 업
**************************************************************************/

package kr.ispark.example.service.impl;

import java.io.InputStream;
import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;
import kr.ispark.common.system.service.impl.IdGenServiceImpl;
import kr.ispark.common.util.CommonUtil;
import kr.ispark.common.util.ExcelParser;
import kr.ispark.common.util.service.impl.ExcelDAO;
import kr.ispark.example.service.ExampleVO;

@Service
public class ExampleServiceImpl extends EgovAbstractServiceImpl {
	public final Logger log = LoggerFactory.getLogger(this.getClass());

	@Resource
    private ExampleDAO exampleDAO;

	@Autowired
	private IdGenServiceImpl idgenService;

	@Autowired
	private ExcelParser excelParser;

	@Resource
	private ExcelDAO excelDAO;

	/**
	 * 관점설정 목록 조회
	 */
    public List<ExampleVO> selectList(ExampleVO searchVO) throws Exception {
        return exampleDAO.selectList(searchVO);
    }

    /**
     * 관점설정 목록 페이징 조회
     */
    public List<ExampleVO> selectListPaging(ExampleVO searchVO) throws Exception {
    	return exampleDAO.selectListPaging(searchVO);
    }

    /**
     * 관점설정 목록수 조회
     */
    public int selectListCount(ExampleVO searchVO) throws Exception {
    	return exampleDAO.selectListCount(searchVO);
    }

	/**
	 * 관점설정 저장
	 */
    public int saveData(ExampleVO dataVO) throws Exception {
    	int resultCnt = 0;
    	ExampleVO paramVO;
    	for(int i=0, iLen=dataVO.getGridDataList().size(); i<iLen; i++) {
    		paramVO = dataVO.getGridDataList().get(i);
    		if(CommonUtil.isEmpty(paramVO.getPerspectiveId())) {
    			paramVO.setPerspectiveId(idgenService.selectNextSeqByYear("EXAMPLE_PERSPECTIVE", paramVO.getYear(), "P", 6, "0"));
    			exampleDAO.insertData(paramVO);
				resultCnt++;
    		} else {
    			resultCnt += exampleDAO.updateData(paramVO);
    		}
    	}
        return resultCnt;
    }

    /**
	 * 관점설정 삭제
	 */
    public int deleteData(ExampleVO dataVO) throws Exception {
    	return exampleDAO.deleteData(dataVO);
    }

    /**
     * 담당자 목록 조회
     */
    public List<ExampleVO> selectUserList(ExampleVO searchVO) throws Exception {
    	return exampleDAO.selectUserList(searchVO);
    }

    /**
     * 담당자 목록 조회
     */
    public List<ExampleVO> selectScDeptList(ExampleVO searchVO) throws Exception {
    	return exampleDAO.selectScDeptList(searchVO);
    }

    /**
     * 엑셀 파싱
     * @param	ExampleVO searchVO
     * @param	InputStream f
     * @return	int
     * @throws	Exception
     */
    public int updateExcelProcess(ExampleVO searchVO, InputStream f) throws Exception {
    	int resultcnt = 0;
    	List<ExampleVO> list
			= excelParser.excelToList(f,
					3,	// 몇 번째 row부터 읽을 것인가. 0부터 시작.
					ExampleVO.class,	// row를 파싱할 class 타입
					new String[] {	// excel의 각 열을 매칭할 vo의 변수명 (열 순서대로)
						"scDeptId",			"scDeptNm",		"upScDeptId",
						"managerUserId",	"bscUserId",	"sortOrder",
						"useYn"
					},
					new int[] {	// 각 열 별 속성. 뒤에 _NOTNULL이 있으면 유효성 검사시 필수값 체크를 수행함
						ExcelParser.CELL_TYPE_STRING_NOTNULL, ExcelParser.CELL_TYPE_STRING_NOTNULL, ExcelParser.CELL_TYPE_STRING,
						ExcelParser.CELL_TYPE_STRING, ExcelParser.CELL_TYPE_STRING, ExcelParser.CELL_TYPE_NUMBER,
						ExcelParser.CELL_TYPE_STRING
					}
			);

		// 추가적인 validation이나 값 세팅은 여기에서
		for(ExampleVO vo : list) {
			vo.setScDeptId(idgenService.selectNextSeqByYear("EXAMPLE_BSC_SC_DEPT", searchVO.getFindYear(), "D", 6, "0"));
			vo.setYear(searchVO.getFindYear());
			log.debug(vo.toString());
			resultcnt++;
		}

		// 엑셀 업로드시 dao는 excelDAO를 사용할 것
		resultcnt = excelDAO.batchInsert("example.insertScDeptData", list);

		return resultcnt;
	}
}
