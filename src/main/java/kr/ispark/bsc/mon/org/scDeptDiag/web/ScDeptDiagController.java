/*************************************************************************
* CLASS 명	: ScDeptDiagController
* 작 업 자	: kimyh
* 작 업 일	: 2018. 2. 2.
* 기	능	: 성과조직도 Controller
* ---------------------------- 변 경 이 력 --------------------------------
* 번호	작 업 자		작	업	일			변 경 내 용				비고
* ----	---------	----------------	---------------------	-----------
*	1	kimyh		2018. 2. 2.			최 초 작 업
**************************************************************************/
package kr.ispark.bsc.mon.org.scDeptDiag.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import kr.ispark.bsc.base.scDept.scDeptDiagMng.service.ScDeptDiagMngVO;
import kr.ispark.bsc.base.scDept.scDeptDiagMng.service.impl.ScDeptDiagMngServiceImpl;
import kr.ispark.bsc.mon.org.scDeptDiag.service.ScDeptDiagVO;
import kr.ispark.bsc.mon.org.scDeptDiag.service.impl.ScDeptDiagServiceImpl;
import kr.ispark.common.system.service.impl.CommonServiceImpl;
import kr.ispark.common.web.BaseController;

@Controller
public class ScDeptDiagController extends BaseController {

	@Autowired
	private ScDeptDiagMngServiceImpl scDeptDiagMngService;

	@Autowired
	private ScDeptDiagServiceImpl scDeptDiagService;

	@Autowired
	private CommonServiceImpl commmonService;

	/**
	 * 성과조직도 화면
	 * @param	ScDeptDiagVO searchVO
	 * @param	Model model
	 * @return	String
	 * @throws	Exception
	 */
	@RequestMapping("/bsc/mon/org/scDeptDiag/scDeptDiag.do")
	public String scDeptDiag(@ModelAttribute("searchVO") ScDeptDiagMngVO searchVO, Model model) throws Exception {
		return "/bsc/mon/org/scDeptDiag/scDeptDiag." + searchVO.getLayout();
	}

	/**
	 * 성과조직도 정보 조회
	 * @param	ScDeptDiagVO searchVO
	 * @param	Model model
	 * @return	String
	 * @throws	Exception
	 */
	@RequestMapping("/bsc/mon/org/scDeptDiag/selectScDeptData.do")
	public ModelAndView selectScDeptData(@ModelAttribute("searchVO") ScDeptDiagMngVO searchVO, Model model) throws Exception {
		String monitoringRootScDeptId = commmonService.selectMonitoringRootScDeptId(searchVO);
		searchVO.setFindScDeptId(monitoringRootScDeptId);

		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("scDeptList", scDeptDiagMngService.selectList(searchVO));
		resultMap.put("signalList", scDeptDiagMngService.selectSignalList(searchVO));
		return new ModelAndView("jsonView", resultMap);
	}

	/**
	 * 성과조직도 지표 목록 조회
	 * @param	ScDeptDiagVO searchVO
	 * @return	ModelAndView
	 * @throws	Exception
	 */
	@RequestMapping(value="/bsc/mon/org/scDeptDiag/selectList.do")
	public ModelAndView selectList(@ModelAttribute("searchVO") ScDeptDiagVO searchVO) throws Exception {
		searchVO.setFindMonitoringRootScDeptId(commmonService.selectMonitoringRootScDeptIdForSearch(searchVO));
		return makeGridJsonData(scDeptDiagService.selectList(searchVO));
	}

	/**
	 * 성과조직도 차트 조회
	 * @param	ScDeptDiagVO searchVO
	 * @return	ModelAndView
	 * @throws	Exception
	 */
	@RequestMapping(value="/bsc/mon/org/scDeptDiag/selectChartData.do")
	public ModelAndView selectChartData(@ModelAttribute("searchVO") ScDeptDiagVO searchVO) throws Exception {
		searchVO.setFindMonitoringRootScDeptId(commmonService.selectMonitoringRootScDeptIdForSearch(searchVO));
		return makeJsonListData(scDeptDiagService.selectChartData(searchVO));
	}
}
