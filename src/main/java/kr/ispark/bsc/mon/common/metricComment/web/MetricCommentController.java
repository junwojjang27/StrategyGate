/*************************************************************************
* CLASS 명	: MetricCommentController.java
* 작 업 자	: kimyh
* 작 업 일	: 2018. 05. 29.
* 기	능	: 댓글 Controller
* ---------------------------- 변 경 이 력 --------------------------------
* 번호	작 업 자		작	업	일			변 경 내 용				비고
* ----	---------	----------------	---------------------	-----------
*	1	kimyh		2018. 05. 29.		최 초 작 업
**************************************************************************/
package kr.ispark.bsc.mon.common.metricComment.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import egovframework.com.cmm.util.EgovUserDetailsHelper;
import kr.ispark.bsc.mon.common.metricComment.service.MetricCommentVO;
import kr.ispark.bsc.mon.common.metricComment.service.impl.MetricCommentServiceImpl;
import kr.ispark.common.util.CommonUtil;
import kr.ispark.common.util.SessionUtil;
import kr.ispark.common.web.BaseController;

@Controller
public class MetricCommentController extends BaseController {

	@Autowired
	private MetricCommentServiceImpl metricCommentService;

	/**
	 * 댓글 목록 화면
	 * @param	MetricCommentVO searchVO
	 * @param	Model model
	 * @return	String
	 * @throws	Exception
	 */
	@RequestMapping(value="/bsc/mon/common/metricComment/metricCommentList.do")
	public String metricCommentList(@ModelAttribute("searchVO") MetricCommentVO searchVO, Model model) throws Exception {
		// 로그인 사용자만 접근 가능
		Boolean isAuthenticated = EgovUserDetailsHelper.isAuthenticated();
		if(!isAuthenticated) {
			return "forward:/error/accessDenied.do";
		}

		return "/bsc/mon/common/metricComment/metricCommentList." + searchVO.getLayout();
	}

	/**
	 * 댓글 목록 조회(json)
	 * @param	MetricCommentVO searchVO
	 * @return	ModelAndView
	 * @throws	Exception
	 */
	@RequestMapping(value="/bsc/mon/common/metricComment/metricCommentList_json.do")
	public ModelAndView metricCommentList_json(@ModelAttribute MetricCommentVO searchVO) throws Exception {
		// 로그인 사용자만 접근 가능
		Boolean isAuthenticated = EgovUserDetailsHelper.isAuthenticated();
		if(!isAuthenticated) {
			return makeFailJsonData(egovMessageSource.getMessage("info.noAuth.msg"));
		}

		searchVO.setRows(100);	// 20180529 kimyh - 임의로 100건까지만 조회되도록 처리, 댓글 사용이 많을 경우 화면쪽에서 페이징 처리 구현 필요
		List<MetricCommentVO> dataList = metricCommentService.selectList(searchVO);
		return makeJsonListData(dataList);
	}

	/**
	 * 댓글 저장
	 * @param MetricCommentVO dataVO
	 * @param Model model
	 * @param BindingResult bindingResult
	 * @throws Exception
	 */
	@RequestMapping(value="/bsc/mon/common/metricComment/saveMetricComment.do")
	public ModelAndView saveMetricComment(@ModelAttribute MetricCommentVO dataVO,Model model, BindingResult bindingResult) throws Exception {
		// 로그인 사용자만 접근 가능
		Boolean isAuthenticated = EgovUserDetailsHelper.isAuthenticated();
		if(!isAuthenticated) {
			return makeFailJsonData(egovMessageSource.getMessage("info.noAuth.msg"));
		}

		// 수정 또는 대댓글인 경우
		if(dataVO.getSeq() != -1 || dataVO.getUpSeq() != -1) {
			dataVO.setContents(dataVO.getEditContents());
		}

		// 유효성 체크
		beanValidator.validate(dataVO, bindingResult);
		if(bindingResult.hasErrors()){
			return makeFailJsonData(getListErrorMsg(bindingResult));
		}

		// 수정
		String userId = SessionUtil.getUserId()!=null?SessionUtil.getUserId():"";
		if(dataVO.getSeq() != -1) {
			MetricCommentVO metricCommentVO = metricCommentService.selectMetricComment(dataVO);
			if(metricCommentVO != null){
				// 수정 권한 체크
				if(!SessionUtil.hasRole("01")
						&& !userId.equals(metricCommentVO.getInsertUserId())) {
					return makeFailJsonData(egovMessageSource.getMessage("info.noAuth.msg"));
				}
				return makeJsonDataByResultCnt(metricCommentService.updateData(dataVO));
			}else{
				return makeFailJsonData();
			}
			
		} else if(!SessionUtil.hasRole("01")) {
			// 중복 게시 방지 시간 체크(30초)
			dataVO.setUserId(userId);
			if(metricCommentService.selectDuplCount(dataVO) > 0) {
				return makeFailJsonData(egovMessageSource.getMessage("bsc.mon.metricCommon.duplCheck"));
			}
		}
		return makeJsonDataByResultCnt(metricCommentService.insertData(dataVO));
	}

	/**
	 * 댓글 일괄 삭제
	 * @param	MetricCommentVO dataVO
	 * @param	Model model
	 * @return	ModelAndView
	 * @throws	Exception
	 */
	@RequestMapping(value="/bsc/mon/common/metricComment/deleteMetricCommentAll.do")
	public ModelAndView deleteMetricCommentAll(@ModelAttribute MetricCommentVO dataVO, Model model) throws Exception {

		// 일괄 삭제는 관리자만 가능
		if(!SessionUtil.hasRole("01")) {
			return makeFailJsonData(egovMessageSource.getMessage("info.noAuth.msg"));
		}

		return makeJsonDataByResultCnt(metricCommentService.deleteData(dataVO));
	}

	/**
	 * 댓글 삭제
	 * @param	MetricCommentVO dataVO
	 * @param	Model model
	 * @return	ModelAndView
	 * @throws	Exception
	 */
	@RequestMapping(value="/bsc/mon/common/metricComment/deleteMetricComment.do")
	public ModelAndView deleteMetricComment(@ModelAttribute MetricCommentVO dataVO, Model model) throws Exception {
		// 로그인 사용자만 접근 가능
		Boolean isAuthenticated = EgovUserDetailsHelper.isAuthenticated();
		if(!isAuthenticated) {
			return makeFailJsonData(egovMessageSource.getMessage("info.noAuth.msg"));
		}

		MetricCommentVO metricCommentVO = metricCommentService.selectMetricComment(dataVO);
		
		String userId = SessionUtil.getUserId()!=null?SessionUtil.getUserId():"";
		if(metricCommentVO != null){
			// 삭제 권한 체크
			if(!SessionUtil.hasRole("01")
					&& !userId.equals(metricCommentVO.getInsertUserId())) {
				return makeFailJsonData(egovMessageSource.getMessage("info.noAuth.msg"));
			}

			// 대댓글 체크
			if(metricCommentVO.getReplyCnt() > 0) {
				return makeFailJsonData(egovMessageSource.getMessage("bsc.mon.metricCommon.cantDelete"));
			}
			dataVO.setUpSeq(metricCommentVO.getUpSeq());
			// 일괄삭제 방지
			dataVO.setKeys(null);

			return makeJsonDataByResultCnt(metricCommentService.deleteData(dataVO));
		}else{
			return makeFailJsonData();
		}
	}
}
