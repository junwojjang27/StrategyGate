package kr.ispark.common.aspect;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springmodules.validation.util.lang.ReflectionUtils;

import kr.ispark.common.util.CommonUtil;
import kr.ispark.common.util.ContextHolder;
import kr.ispark.common.util.ContextHolderInfo;
import kr.ispark.common.util.DataSourceType;
import kr.ispark.common.util.SessionUtil;

/*
 * DAO 호출시 Map이나 VO에 compId와 locale을 세팅해줌
 */
@Aspect
public class CompIdAspect {

	protected Logger logger = LoggerFactory.getLogger(this.getClass());

	public void setCompId(JoinPoint joinPoint) throws Exception {
		StringBuffer debugBuf = new StringBuffer();
		debugBuf.append("\n");
		debugBuf.append("##########################################################\n");
		debugBuf.append("* DAO : " + joinPoint.getTarget().getClass().getName() + "에 대한 요청 시도\n");
		
		//String compId = SessionUtil.getCompId();
		//String masterDbId = (String)SessionUtil.getAttribute("masterDbId");
		String lang = LocaleContextHolder.getLocale().toString();
		if(lang != null) {
			//debugBuf.append("* compId : " + compId + "\n");
			//debugBuf.append("* masterDbId : " + masterDbId + "\n");
			debugBuf.append("* lang : " + lang + "\n");
			
			if(joinPoint.getArgs() != null) {
				Object[] getParams = joinPoint.getArgs();
				for(int tmpcParamSize = 0; tmpcParamSize < getParams.length; ++tmpcParamSize) {
					Object obj = getParams[tmpcParamSize];
					if(obj == null) continue;
					
					debugBuf.append("* getName() : " + obj.getClass().getName() + "\n");
								
					if(obj instanceof List) {
						int size = ((List)obj).size();
						debugBuf.append("- List (size : " + size + ")\n");
						for(int i=0; i<size; i++) {
							setCompId(((List) obj).get(i), lang, debugBuf);
						}
					} else {
						setCompId(obj, lang, debugBuf);
					}
				}
			}
		}
		
		if(lang != null) {
			logger.info(debugBuf.toString());
		}
		
	}
	
	private void setCompId(Object obj, String lang, StringBuffer debugBuf) throws Exception {
		// map일 경우
		if(obj != null){
			if(obj instanceof Map) {
				debugBuf.append("- Map" + "\n");
				Map map = (Map)obj;
				//map.put("compId", compId);
				//map.put("masterDbId", masterDbId);
				map.put("lang", lang);
			} else {
				// vo일 경우 setCompId 메소드가 존재할 경우
				//Method m = ReflectionUtils.findMethod(obj.getClass(), "setCompId", new Class[]{String.class});
				/*
				if(m != null) {
					debugBuf.append("- setCompId() exists" + "\n");
					m.invoke(obj, compId);
				}
				*/
				Method m = ReflectionUtils.findMethod(obj.getClass(), "setLang", new Class[]{String.class});
				if(m != null) {
					m.invoke(obj, lang);
				}
				/*
				m = ReflectionUtils.findMethod(obj.getClass(), "setMasterDbId", new Class[]{String.class});
				if(m != null) {
					m.invoke(obj, masterDbId);
				}
				*/
			}
		}
	}
}