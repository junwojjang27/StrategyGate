/*************************************************************************
* CLASS 명	: CompareScDeptGrpController
* 작 업 자	: kimyh
* 작 업 일	: 2018-04-27
* 기	능	: 평가군별 비교 Controller
* ---------------------------- 변 경 이 력 --------------------------------
* 번호	작 업 자		작	업	일			변 경 내 용				비고
* ----	---------	----------------	---------------------	-----------
*	1	kimyh		2018-04-27
**************************************************************************/
package kr.ispark.bsc.mon.compare.compareScDeptGrp.web;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import kr.ispark.bsc.mon.compare.compareScDeptGrp.service.CompareScDeptGrpVO;
import kr.ispark.bsc.mon.compare.compareScDeptGrp.service.impl.CompareScDeptGrpServiceImpl;
import kr.ispark.common.system.service.impl.CommonServiceImpl;
import kr.ispark.common.util.CodeUtil;
import kr.ispark.common.util.CommonUtil;
import kr.ispark.common.util.service.CodeVO;
/*공통 컨트롤러*/
import kr.ispark.common.web.BaseController;

@Controller
public class CompareScDeptGrpController extends BaseController {
	@Autowired
	private CommonServiceImpl commmonService;

	@Autowired
	private CompareScDeptGrpServiceImpl compareScDeptGrpService;

	/**
	 * 평가군별 비교 목록 화면
	 * @param	CompareScDeptGrpVO searchVO
	 * @param	Model model
	 * @return	String
	 * @throws	Exception
	 */
	@RequestMapping("/bsc/mon/compare/compareScDeptGrp/compareScDeptGrpList.do")
	public String compareScDeptGrpList(@ModelAttribute("searchVO") CompareScDeptGrpVO searchVO, Model model) throws Exception {
		// 평가군
		List<CodeVO> evalGrpList = CodeUtil.getCodeList("003", searchVO.getFindYear());

		// 사용자의 성과조직 정보를 조회해서 검색조건 초기값으로 세팅
		/*
		ScDeptVO scDeptVO = commmonService.selectScDeptByUser(searchVO);
		String scDeptId = CommonUtil.removeNull(scDeptVO.getScDeptId());
		String scDeptGrpId = CommonUtil.removeNull(scDeptVO.getScDeptGrpId());
		if(CommonUtil.isNotEmpty(scDeptGrpId)) {
			for(CodeVO codeVO : evalGrpList) {
				if(codeVO.getCodeId().equals(scDeptGrpId)) {
					searchVO.setFindEvalGrpId(scDeptGrpId);
				}
			}
		}
		if(CommonUtil.isNotEmpty(scDeptId)) {
			searchVO.setFindScDeptId(scDeptId);
		}
		*/
		String scDeptGrpId = commmonService.selectScDeptGrpId(searchVO);
		if(CommonUtil.isNotEmpty(scDeptGrpId)) {
			for(CodeVO codeVO : evalGrpList) {
				if(codeVO.getCodeId().equals(scDeptGrpId)) {
					searchVO.setFindEvalGrpId(scDeptGrpId);
				}
			}
		}

		model.addAttribute("searchVO", searchVO);
		model.addAttribute("evalGrpList", evalGrpList);
		return "/bsc/mon/compare/compareScDeptGrp/compareScDeptGrpList." + searchVO.getLayout();
	}

	/**
	 * 평가군별 비교 그리드 조회(json)
	 * @param	CompareScDeptGrpVO searchVO
	 * @return	ModelAndView
	 * @throws	Exception
	 */
	@RequestMapping("/bsc/mon/compare/compareScDeptGrp/compareScDeptGrpList_json.do")
	public ModelAndView compareScDeptGrpList_json(@ModelAttribute("searchVO") CompareScDeptGrpVO searchVO) throws Exception {
		searchVO.setFindMonitoringRootScDeptId(commmonService.selectMonitoringRootScDeptIdForSearch(searchVO));

		List<CompareScDeptGrpVO> dataList = compareScDeptGrpService.selectList(searchVO);
		return makeGridJsonData(dataList, dataList.size(), searchVO);
	}

	/**
	 * 평가군별 비교 상세 조회
	 * @param	CompareScDeptGrpVO searchVO
	 * @return	ModelAndView
	 * @throws	Exception
	 */
	@RequestMapping("/bsc/mon/compare/compareScDeptGrp/compareScDeptGrpDetail.do")
	public ModelAndView compareScDeptGrpDetail(@ModelAttribute("searchVO") CompareScDeptGrpVO searchVO) throws Exception {
		searchVO.setFindMonitoringRootScDeptId(commmonService.selectMonitoringRootScDeptIdForSearch(searchVO));

		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("scoreAnalysis", compareScDeptGrpService.selectScoreAnalysis(searchVO));
		resultMap.put("monthlyTrend", compareScDeptGrpService.selectMonthlyTrend(searchVO));
		resultMap.put("performanceList", compareScDeptGrpService.selectPerformanceList(searchVO));
		return new ModelAndView("jsonView", resultMap);
	}
}
