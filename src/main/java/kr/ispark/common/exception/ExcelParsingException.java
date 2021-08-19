/*************************************************************************
* CLASS 명	: Excel Parser용 Exception
* 작 업 자	: kimyh
* 작 업 일	: 2017. 12. 21.
* 기	능	: 엑셀 파싱 중 오류가 발생했을 때 이 class를 사용하여 에러 메시지를 전달
* ---------------------------- 변 경 이 력 --------------------------------
* 번호	작 업 자		작	업	일			변 경 내 용				비고
* ----	---------	----------------	---------------------	-----------
*	1	kimyh		2017. 12. 21.		최 초 작 업
**************************************************************************/
package kr.ispark.common.exception;

public class ExcelParsingException extends Exception {
	private static final long serialVersionUID = -7114454058396795800L;

	private int lineNum = -1;

	public ExcelParsingException() {

	};

	public ExcelParsingException(String message) {
		super(message);
	}

	public ExcelParsingException(String message, int lineNum) {
		super(message);
		this.lineNum = lineNum;
	}

	public int getLineNum() {
		return lineNum;
	}
}
