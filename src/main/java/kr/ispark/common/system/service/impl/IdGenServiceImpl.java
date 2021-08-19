/*************************************************************************
* CLASS 명	: IdGenServiceImpl
* 작 업 자	: kimyh
* 작 업 일	: 2017. 12. 22.
* 기	능	: ID 채번용 Service 
* ---------------------------- 변 경 이 력 --------------------------------
* 번호	작 업 자		작	업	일			변 경 내 용				비고
* ----	---------	----------------	---------------------	-----------
*	1	kimyh		2017. 12. 22.		최 초 작 업
**************************************************************************/
package kr.ispark.common.system.service.impl;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;
import kr.ispark.common.system.service.IdGenVO;
import kr.ispark.common.util.CommonUtil;

@Service
public class IdGenServiceImpl extends EgovAbstractServiceImpl {
	@Resource
	private IdGenDAO idGenDAO;

	public String selectNextSeq(String tableName, int length) throws Exception {
		return selectNextSeq(tableName, null, length, "0");
	}
	
	public String selectNextSeq(String tableName, int length, String fillChar) throws Exception {
		return selectNextSeq(tableName, null, length, fillChar);
	}
	
	public String selectNextSeq(String tableName, String prefix, int length, String fillChar) throws Exception {
		IdGenVO vo = new IdGenVO();
		vo.setTableName(tableName);
		vo.setYear("9999");
		vo.setPrefix(prefix);
		vo.setLength(length);
		vo.setFillChar(fillChar);
		return selectNextSeq(vo);
	}
	
	public String selectNextSeqByYear(String tableName, String year, int length) throws Exception {
		return selectNextSeqByYear(tableName, year, null, length, "0");
	}
	
	public String selectNextSeqByYear(String tableName, String year, int length, String fillChar) throws Exception {
		return selectNextSeqByYear(tableName, year, null, length, fillChar);
	}
	
	public String selectNextSeqByYear(String tableName, String year, String prefix, int length, String fillChar) throws Exception {
		IdGenVO vo = new IdGenVO();
		vo.setTableName(tableName);
		vo.setYear(year);
		vo.setPrefix(prefix);
		vo.setLength(length);
		vo.setFillChar(fillChar);
		return selectNextSeq(vo);
	}
	
	public String selectCommonNextSeq(String tableName, String prefix, int length, String fillChar) throws Exception {
		IdGenVO vo = new IdGenVO();
		vo.setTableName(tableName);
		vo.setYear("9999");
		vo.setPrefix(prefix);
		vo.setLength(length);
		vo.setFillChar(fillChar);
		vo.setCommonYn("Y");
		return selectNextSeq(vo);
	}
	
	public String selectCommonNextSeqByYear(String tableName, String year, String prefix, int length, String fillChar) throws Exception {
		IdGenVO vo = new IdGenVO();
		vo.setTableName(tableName);
		vo.setYear(year);
		vo.setPrefix(prefix);
		vo.setLength(length);
		vo.setFillChar(fillChar);
		vo.setCommonYn("Y");
		return selectNextSeq(vo);
	}
	
	public String selectTemplateNextSeq(String tableName, String prefix, int length, String fillChar) throws Exception {
		IdGenVO vo = new IdGenVO();
		vo.setTableName(tableName);
		vo.setYear("9999");
		vo.setPrefix(prefix);
		vo.setLength(length);
		vo.setFillChar(fillChar);
		vo.setTemplateYn("Y");
		return selectNextSeq(vo);
	}

	public String selectNextSeq(IdGenVO vo) throws Exception {
		int seq = idGenDAO.selectNextSeq(vo);
		return (CommonUtil.isEmpty(vo.getPrefix()) ? "" : vo.getPrefix()) + StringUtils.leftPad(String.valueOf(seq), vo.getLength(), vo.getFillChar());
	}
}
