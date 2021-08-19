/*************************************************************************
* CLASS 명	: CustomException
* 작 업 자	: kimyh
* 작 업 일	: 2018. 05. 10.
* 기	능	: Controller로 에러 메시지 전달용 Exception
* ---------------------------- 변 경 이 력 --------------------------------
* 번호	작 업 자		작	업	일			변 경 내 용				비고
* ----	---------	----------------	---------------------	-----------
*	1	kimyh		2018. 05. 10.		최 초 작 업
**************************************************************************/
package kr.ispark.common.exception;

public class CustomException extends Exception {
	private static final long serialVersionUID = 6854835683583821628L;

	public CustomException() {
		
	};
	
	public CustomException(String message) {
		super(message);
	}
}
