/*************************************************************************
* CLASS 명	: ScDeptMappingController
* 작 업 자	: kimyh
* 작 업 일	: 2018. 1. 29.
* 기	능	: 성과조직매핑 Controller
* ---------------------------- 변 경 이 력 --------------------------------
* 번호	작 업 자		작	업	일			변 경 내 용				비고
* ----	---------	----------------	---------------------	-----------
*	1	kimyh		2018. 1. 29.		최 초 작 업
**************************************************************************/
package kr.ispark.bsc.base.scDept.scDeptMapping.web;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import egovframework.let.utl.fcc.service.EgovStringUtil;
import kr.ispark.bsc.base.scDept.scDeptMapping.service.impl.ScDeptMappingServiceImpl;
import kr.ispark.common.system.service.ScDeptVO;
import kr.ispark.common.web.BaseController;

@Controller
public class ScDeptMappingController extends BaseController {
	
	@Autowired
	private ScDeptMappingServiceImpl scDeptMappingService;
	
	/**
	 * 성과조직매핑 화면
	 * @param	ScDeptVO searchVO
	 * @param	Model model
	 * @return	String
	 * @throws	Exception
	 */
	@RequestMapping("/bsc/base/scDept/scDeptMapping/scDeptMapping.do")
	public String scDeptMngList(@ModelAttribute("searchVO") ScDeptVO searchVO, Model model) throws Exception {
		return "/bsc/base/scDept/scDeptMapping/scDeptMapping." + searchVO.getLayout();
	}
	
	/**
	 * 성과조직 목록
	 * @param	ScDeptVO searchVO
	 * @return	ModelAndView
	 * @throws	Exception
	 */
	@RequestMapping("/bsc/base/scDept/scDeptMapping/scDeptList_json.do")
	public ModelAndView scDeptList_json(@ModelAttribute("searchVO") ScDeptVO searchVO)throws Exception {
		
		List<ScDeptVO> scDeptList = scDeptMappingService.selectScDeptList(searchVO);
		List<ScDeptVO> deptList = scDeptMappingService.selectDeptList(searchVO);
		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("scDeptList", scDeptList);
		resultMap.put("deptList", deptList);
		return new ModelAndView("jsonView", resultMap);
	}
	
	/**
	 * 성과조직 매핑 목록
	 * @param	ScDeptVO searchVO
	 * @return	ModelAndView
	 * @throws	Exception
	 */
	@RequestMapping("/bsc/base/scDept/scDeptMapping/scDeptMappingList_json.do")
	public ModelAndView scDeptMappingList_json(@ModelAttribute("searchVO") ScDeptVO searchVO)throws Exception {
		
		List<ScDeptVO> mappingList = scDeptMappingService.selectMappingList(searchVO);
		return makeJsonListData(mappingList);
	}
	
	/**
	 * 성과조직 매핑 정보 저장
	 * @param	ScDeptVO dataVO
	 * @return	ModelAndView
	 * @throws	Exception
	 */
	@RequestMapping("/bsc/base/scDept/scDeptMapping/saveMapping.do")
	public ModelAndView saveMapping(@ModelAttribute("dataVO") ScDeptVO dataVO)throws Exception {
		try {
			// 인사조직으로 매핑할 때는 성과조직을 한 곳만 선택 가능
			if(dataVO.getFindMappingBase().equals("2")
					&& dataVO.getDeptIds() != null
					&& dataVO.getDeptIds().size() > 1
					) {
				return makeFailJsonData();
			}
			
			int result = scDeptMappingService.saveMapping(dataVO);
		} catch(SQLException sqe) {
			log.error("error : "+sqe.getMessage());
			return makeFailJsonData();
		} catch(Exception e) {
			log.error("error : "+e.getMessage());
			return makeFailJsonData();
		}
		return makeSuccessJsonData();
	}
	
	/**
	 * 엑셀다운로드
	 */
	@RequestMapping("/bsc/base/scDept/scDeptMapping/scDeptMappingListExcel.do")
	public String scDeptMappingListExcel(@ModelAttribute("searchVO") ScDeptVO searchVO,	Model model)throws Exception {

		// 타이틀
		model.addAttribute("title", egovMessageSource.getMessage("word.scDeptMapping"));
		
		// 검색조건
		model.addAttribute("year", egovMessageSource.getMessage("word.year"));
		model.addAttribute("findYear", searchVO.getFindYear());
		
		// Header
		model.addAttribute("scDeptNm", egovMessageSource.getMessage("word.scDeptNm"));
		model.addAttribute("deptNm", egovMessageSource.getMessage("word.personnelOrg"));
		
		// 데이터
		model.addAttribute("dataList", scDeptMappingService.selectMappingListForExcel(searchVO));
		model.addAttribute("dataList2", scDeptMappingService.selectMappingListForExcel2(searchVO));
		
		// 시트명
		List sheetNames = new ArrayList();
		sheetNames.add(egovMessageSource.getMessage("word.scDeptNm") + " > " + egovMessageSource.getMessage("word.personnelOrg"));
		sheetNames.add(egovMessageSource.getMessage("word.personnelOrg") + " > " + egovMessageSource.getMessage("word.scDeptNm"));
		
		model.addAttribute("sheetNames", sheetNames);
		model.addAttribute("destJxlsFileName", egovMessageSource.getMessage("word.scDeptMapping") + "_" + EgovStringUtil.getTimeStamp()+".xlsx");
		model.addAttribute("templateJxlsFileName", "scDeptMappingList.xlsx");

		return "excelDownloadView";

	}
}
