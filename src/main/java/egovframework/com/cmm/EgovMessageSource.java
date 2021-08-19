package egovframework.com.cmm;

import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

/**
 * 메시지 리소스 사용을 위한 MessageSource 인터페이스 및 ReloadableResourceBundleMessageSource 클래스의 구현체
 * @author 공통서비스 개발팀 이문준
 * @since 2009.06.01
 * @version 1.0
 * @see
 *
 * <pre>
 * << 개정이력(Modification Information) >>
 *   
 *   수정일      수정자           수정내용
 *  -------    --------    ---------------------------
 *   2009.03.11  이문준          최초 생성
 *
 * </pre>
 */

public class EgovMessageSource extends ReloadableResourceBundleMessageSource implements MessageSource {

	private ReloadableResourceBundleMessageSource reloadableResourceBundleMessageSource;

	/**
	 * getReloadableResourceBundleMessageSource() 
	 * @param reloadableResourceBundleMessageSource - resource MessageSource
	 * @return ReloadableResourceBundleMessageSource
	 */	
	public void setReloadableResourceBundleMessageSource(ReloadableResourceBundleMessageSource reloadableResourceBundleMessageSource) {
		this.reloadableResourceBundleMessageSource = reloadableResourceBundleMessageSource;
	}
	
	/**
	 * getReloadableResourceBundleMessageSource() 
	 * @return ReloadableResourceBundleMessageSource
	 */	
	public ReloadableResourceBundleMessageSource getReloadableResourceBundleMessageSource() {
		return reloadableResourceBundleMessageSource;
	}
	
	/**
	 * 정의된 메세지 조회
	 * @param code - 메세지 코드
	 * @return String
	 */	
	public String getMessage(String code) {
		return getReloadableResourceBundleMessageSource().getMessage(code, null, LocaleContextHolder.getLocale());
	}
	public String getMessage(String code, String s) {
		return getReloadableResourceBundleMessageSource().getMessage(code, new String[]{s}, LocaleContextHolder.getLocale());
	}
	public String getMessage(String code, Object[] args) {
		return getReloadableResourceBundleMessageSource().getMessage(code, args, LocaleContextHolder.getLocale());
	}
	
	// validator에서 사용
	public String getMessage(MessageSourceResolvable resolvable) throws NoSuchMessageException {
		String[] codes = resolvable.getCodes();
		if (codes == null) {
			codes = new String[0];
		}
		String[] args;
		String msg;
		for(String code : codes) {
			if(resolvable.getArguments() == null) {
				msg = getMessage(code);
			} else {
				args = new String[resolvable.getArguments().length];
				Object obj;
				for(int i=0, iLen=args.length; i<iLen; i++) {
					obj = resolvable.getArguments()[i];
					if(obj instanceof DefaultMessageSourceResolvable) {
						args[i] = getMessage((DefaultMessageSourceResolvable)obj);
					} else {
						args[i] = (String)obj;
					}
				}
				try {
					msg = getMessage(code, args);
				} catch(NoSuchMessageException e) {
					msg = null;
				}
			}
			
			if (msg != null) {
				return msg;
			}
		}
		
		String defaultMessage = resolvable.getDefaultMessage();
		if(defaultMessage != null) {
			return getMessage(defaultMessage);
		}
		
		if(codes.length > 0) {
			msg = getMessage(codes[0]);
			if(msg != null) {
				return msg;
			}
		}
		throw new NoSuchMessageException(codes.length > 0 ? codes[codes.length - 1] : null, LocaleContextHolder.getLocale());
	}
}
