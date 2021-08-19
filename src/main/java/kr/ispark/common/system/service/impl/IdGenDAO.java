/*************************************************************************
* CLASS 명	: IdGenDAO
* 작 업 자	: kimyh
* 작 업 일	: 2017. 12. 22.
* 기	능	: ID 채번 DAO
* ---------------------------- 변 경 이 력 --------------------------------
* 번호	작 업 자		작	업	일			변 경 내 용				비고
* ----	---------	----------------	---------------------	-----------
*	1	kimyh		2017. 12. 22.		최 초 작 업
**************************************************************************/

package kr.ispark.common.system.service.impl;


import java.sql.SQLException;

import org.springframework.stereotype.Repository;

import egovframework.com.cmm.service.impl.EgovComAbstractDAO;
import kr.ispark.common.system.service.IdGenVO;
import kr.ispark.common.util.PropertyUtil;

@Repository
public class IdGenDAO extends EgovComAbstractDAO {
	/**
	 * ID 채번
	 * @param	IdGenVO vo
	 * @return	int
	 * @throws	SQLException
	 */
	public int selectNextSeq(IdGenVO vo) throws SQLException {
		
		int seq = selectOne("idgen.selectNextSeq", vo);
		vo.setSeq(seq);

		if(seq == 1) {
			insert("idgen.insertNextSeq", vo);
		} else {
			update("idgen.updateNextSeq", vo);
		}
		return seq;
	}
}
