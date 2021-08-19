package kr.ispark.common.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.i18n.LocaleContextHolder;

@Aspect
public class CtrlAspect {

	protected Logger logger = LoggerFactory.getLogger(this.getClass());

	public void getInfoController(JoinPoint joinPoint) throws Exception {
		StringBuffer debugBuf = new StringBuffer();

		debugBuf.append("\n** EgovFramework : Controller : " + joinPoint.getTarget().getClass().getName() + "에 대한 요청 시도\n");
		debugBuf.append("기본 로케일 : " + LocaleContextHolder.getLocale() + "\n");
		debugBuf.append("조인포인트 종류 : " + joinPoint.getKind() + "\n");
		debugBuf.append("시그니쳐 타입 : " + joinPoint.getSignature().getDeclaringTypeName() + "\n");
		debugBuf.append("시그니쳐 명 : " + joinPoint.getSignature().getName() + "\n");

		if ((joinPoint.getSignature().getDeclaringTypeName().equals("egovframework.com.cmm.web.EgovComUtlController"))
				|| (joinPoint.getSignature().getName().equals("validate"))) {
			return;
		}

		if (joinPoint.getArgs() != null) {
			Object[] getParams = joinPoint.getArgs();
			for (int tmpcParamSize = 0; tmpcParamSize < getParams.length; ++tmpcParamSize) {
				Object retVal = getParams[tmpcParamSize];
				debugBuf.append("요청에 적용된 파라미터 정보: " + retVal + "\n");
				debugBuf.append("요청에 적용된 파라미터 정보(배열) : " + retVal.toString() + "\n");
			}

		}

		debugBuf.append("대상 클래스 : " + joinPoint.getTarget().getClass().getName() + "\n");
		debugBuf.append("현재 클래스 : " + joinPoint.getThis().getClass().getName() + "\n");

		logger.debug(debugBuf.toString());
	}
}