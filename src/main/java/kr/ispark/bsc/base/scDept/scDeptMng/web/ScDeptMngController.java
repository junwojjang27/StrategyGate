/*************************************************************************
* CLASS 명	: ScDeptMngController
* 작 업 자	: kimyh
* 작 업 일	: 2018. 1. 23.
* 기	능	: 성과조직관리 Controller
* ---------------------------- 변 경 이 력 --------------------------------
* 번호	작 업 자		작	업	일			변 경 내 용				비고
* ----	---------	----------------	---------------------	-----------
*	1	kimyh		2018. 1. 23.		최 초 작 업
**************************************************************************/
package kr.ispark.bsc.base.scDept.scDeptMng.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import egovframework.let.utl.fcc.service.EgovStringUtil;
import kr.ispark.bsc.base.scDept.scDeptMng.service.impl.ScDeptMngServiceImpl;
import kr.ispark.common.system.service.ScDeptVO;
import kr.ispark.common.util.CommonUtil;
import kr.ispark.common.web.BaseController;

@Controller
public class ScDeptMngController extends BaseController {
	@Autowired
	private ScDeptMngServiceImpl scDeptMngService;
	
	/**
	 * 성과조직관리 목록 화면
	 * @param	ScDeptVO searchVO
	 * @param	Model model
	 * @return	String
	 * @throws	Exception
	 */
	@RequestMapping(value="/bsc/base/scDept/scDeptMng/scDeptMngList.do")
	public String scDeptMngList(@ModelAttribute("searchVO") ScDeptVO searchVO, Model model) throws Exception {
		return "/bsc/base/scDept/scDeptMng/scDeptMngList." + searchVO.getLayout();
	}
	
	/**
	 * 성과조직관리 그리드 조회(json)
	 * @param searchVO
	 * @return	ModelAndView
	 * @throws Exception
	 */
	@RequestMapping(value="/bsc/base/scDept/scDeptMng/scDeptMngList_json.do")
	public ModelAndView scDeptMngList_json(@ModelAttribute("searchVO") ScDeptVO searchVO)throws Exception {
		
		List<ScDeptVO> dataList = scDeptMngService.selectList(searchVO);
		return makeGridJsonData(dataList, dataList.size(), searchVO);
	}
	
	/**
	 * 엑셀양식다운로드
	 */
	@RequestMapping("/bsc/base/scDept/scDeptMng/excelDownload.do")
	public String excelDownload(@ModelAttribute("searchVO") ScDeptVO searchVO,
			Model model)throws Exception {

		List<ScDeptVO> dataList = scDeptMngService.selectExcelList(searchVO);
		
		// 타이틀
		model.addAttribute("title", egovMessageSource.getMessage("word.scDeptManage"));
		
		// 검색조건
		model.addAttribute("year", egovMessageSource.getMessage("word.year"));
		model.addAttribute("findYear", searchVO.getFindYear());
		
		// header
		model.addAttribute("scDeptNm", egovMessageSource.getMessage("word.scDeptNm"));			// 성과조직
		model.addAttribute("scDeptGrpNm", egovMessageSource.getMessage("word.evalGrp"));		// 평가군
		model.addAttribute("bscUserNm", egovMessageSource.getMessage("word.meticInCharge"));	// 지표담당자
		model.addAttribute("managerUserNm", egovMessageSource.getMessage("word.manager"));		// 부서장
		model.addAttribute("sortOrder", egovMessageSource.getMessage("word.sortOrder"));		// 정렬순서
		
		// 조직 데이터
		model.addAttribute("dataList", dataList);
		
		// 시트명
		model.addAttribute("sheetName", egovMessageSource.getMessage("word.scDeptNm"));
		
		model.addAttribute("destJxlsFileName", egovMessageSource.getMessage("word.scDeptNm") + "_" + EgovStringUtil.getTimeStamp()+".xlsx");
		model.addAttribute("templateJxlsFileName", "scDeptMngList.xlsx");

		return "excelDownloadView";
	}

	/**
	 * 성과조직 상세 조회
	 * @param searchVO
	 * @return	ModelAndView
	 * @throws Exception
	 */
	@RequestMapping(value="/bsc/base/scDept/scDeptMng/selectDetail.do")
	public ModelAndView selectDetail(@ModelAttribute("searchVO") ScDeptVO searchVO)throws Exception {
		return makeJsonData(scDeptMngService.selectDetail(searchVO));
	}
	
	/**
	 * 성과조직관리 정렬순서저장
	 * @param dataVO
	 * @param model
	 * @param bindingResult
	 * @return	ModelAndView
	 * @throws Exception
	 */
	@RequestMapping("/bsc/base/scDept/scDeptMng/saveSortOrder.do")
	public ModelAndView saveSortOrder(@ModelAttribute("dataVO") ScDeptVO dataVO, Model model, BindingResult bindingResult) throws Exception {
		// list 유효성 체크
		validateList(dataVO.getGridDataList(), bindingResult);
		if(bindingResult.hasErrors()) {
			return makeFailJsonData(getListErrorMsg(bindingResult));
		}
		
		int resultCnt = scDeptMngService.updateSortOrder(dataVO);
		if(resultCnt == 0) {
			return makeFailJsonData();
		}
		return makeSuccessJsonData();
	}
	
	/**
	 * 성과조직 삭제
	 * @param dataVO
	 * @param model
	 * @return	ModelAndView
	 * @throws Exception
	 */
	@RequestMapping("/bsc/base/scDept/scDeptMng/deleteScDeptMng.do")
	public ModelAndView deleteScDeptMng(@ModelAttribute("dataVO") ScDeptVO dataVO, Model model) throws Exception {
		int resultCnt = scDeptMngService.deleteScDeptMng(dataVO);
		if(resultCnt == 0) {
			return makeFailJsonData();
		}
		return makeSuccessJsonData();
	}
	
	/**
	 * 성과조직 저장
	 * @param dataVO
	 * @param model
	 * @param bindingResult
	 * @return	ModelAndView
	 * @throws Exception
	 */
	@RequestMapping("/bsc/base/scDept/scDeptMng/saveScDeptMng.do")
	public ModelAndView saveScDeptMng(@ModelAttribute("dataVO") ScDeptVO dataVO, Model model, BindingResult bindingResult) throws Exception {
		beanValidator.validate(dataVO, bindingResult);
		if(bindingResult.hasErrors()){
			return makeFailJsonData(getListErrorMsg(bindingResult));
		}

		// 신규인 경우
		if(CommonUtil.isEmpty(dataVO.getScDeptId())) {
			if(CommonUtil.isEmpty(dataVO.getUpScDeptId())) {
				return makeFailJsonData(egovMessageSource.getMessage("common.required.msg", new String[] {egovMessageSource.getMessage("word.upOrg")}));
			}
		} else {
			ScDeptVO scDeptVO = scDeptMngService.selectDetail(dataVO);
			
			// 최상위 조직의 상위조직 수정을 방지하기 위한 처리
			if(scDeptVO.getLevelId().equals("1")) {
				dataVO.setUpScDeptId("");
			} else if(CommonUtil.isEmpty(dataVO.getUpScDeptId())) {
				return makeFailJsonData(egovMessageSource.getMessage("common.required.msg", new String[] {egovMessageSource.getMessage("word.upOrg")}));
			}
		}

		int resultCnt = scDeptMngService.saveData(dataVO);
		if(resultCnt == 0) {
			return makeFailJsonData();
		}
		return makeSuccessJsonData();
	}
}
