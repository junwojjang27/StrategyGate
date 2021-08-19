/*************************************************************************
* CLASS 명	: PastYearCopyController
* 작 업 자	: 박정현
* 작 업 일	: 2018-06-29
* 기	능	: 전년데이터일괄적용 Controller
* ---------------------------- 변 경 이 력 --------------------------------
* 번호	작 업 자		작	업	일			변 경 내 용				비고
* ----	---------	----------------	---------------------	-----------
*	1	박정현		2018-06-29
**************************************************************************/
package kr.ispark.system.system.batch.pastYearCopy.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/*공통 컨트롤러*/
import kr.ispark.common.web.BaseController;
import kr.ispark.system.system.batch.pastYearCopy.service.PastYearCopyVO;
import kr.ispark.system.system.batch.pastYearCopy.service.impl.PastYearCopyServiceImpl;

@Controller
public class PastYearCopyController extends BaseController {
	@Autowired
	private PastYearCopyServiceImpl pastYearCopyService;
	
	/**
	 * 전년데이터일괄적용 목록 화면
	 * @param	PastYearCopyVO searchVO
	 * @param	Model model
	 * @return	String
	 * @throws	Exception
	 */
	@RequestMapping("/system/system/batch/pastYearCopy/pastYearCopyList.do")
	public String pastYearCopyList(@ModelAttribute("searchVO") PastYearCopyVO searchVO, Model model) throws Exception {
		return "/system/system/batch/pastYearCopy/pastYearCopyList." + searchVO.getLayout();
	}
	
	/**
	 * 전년데이터일괄적용 그리드 조회(json)
	 * @param	PastYearCopyVO searchVO
	 * @return	ModelAndView
	 * @throws	Exception
	 */
	@RequestMapping("/system/system/batch/pastYearCopy/pastYearCopyList_json.do")
	public ModelAndView pastYearCopyList_json(@ModelAttribute("searchVO") PastYearCopyVO searchVO) throws Exception {
		List<PastYearCopyVO> dataList = pastYearCopyService.selectList(searchVO);
		return makeGridJsonData(dataList, dataList.size(), searchVO);
	}

	/**
	 * 전년데이터일괄적용 적용
	 * @param dataVO
	 * @param model
	 * @return	ModelAndView
	 * @throws Exception
	 */
	@RequestMapping("/system/system/batch/pastYearCopy/applyPastYearCopy.do")
	public ModelAndView applyPastYearCopy(@ModelAttribute("dataVO") PastYearCopyVO dataVO, Model model) throws Exception {
		
		int resultCnt = pastYearCopyService.applyData(dataVO);
		if(resultCnt == 0) {
			return makeFailJsonData();
		}
		return makeSuccessJsonData();
	}
	
	
}

