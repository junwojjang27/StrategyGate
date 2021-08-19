package kr.ispark.common.exception;


import java.text.MessageFormat;
import java.util.Locale;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

import egovframework.rte.fdl.cmmn.exception.BaseException;

/**
 * SecureException : 보안이슈로 인해 발생시키는 Exception
 *
 * @author 윤태성
 * @since 2014.04.24
 * @version 1.0
 * @see
 *
 * <pre>
 * << 개정이력(Modification Information) >>
 *
 *   수정일      수정자           수정내용
 *  -------    --------    ---------------------------
 *   2014.04.24  윤태성        최초 생성
 *
 * </pre>
 */
public class SecureException extends BaseException {

	private static final long serialVersionUID = 1L;

	/**
	 * SecureException 생성자
	 */
	public SecureException() {
		this("BaseException without message", null, null);
	}

	/**
	 * SecureException 생성자
	 * @param defaultMessage 메세지 지정
	 */
	public SecureException(String defaultMessage) {
		this(defaultMessage, null, null);
	}
	/**
	 * SecureException 생성자
	 * @param defaultMessage 메세지 지정
	 * @param wrappedException 발생한 Exception 내포함
	 */
	public SecureException(String defaultMessage, Exception wrappedException) {
		this(defaultMessage, null, wrappedException);
	}

	/**
	 * SecureException 생성자
	 * @param defaultMessage 메세지 지정(변수지정)
	 * @param messageParameters 치환될 메세지 리스트
	 * @param wrappedException 발생한 Exception 내포함.
	 */
	public SecureException(String defaultMessage, Object[] messageParameters, Exception wrappedException) {
		String userMessage = defaultMessage;
		if (messageParameters != null) {
			userMessage = MessageFormat.format(defaultMessage, messageParameters);
		}
		this.message = userMessage;
		this.wrappedException = wrappedException;
	}
	/**
	 * SecureException 생성자
	 * @param messageSource 메세지 리소스
	 * @param messageKey 메세지키값
	 */
	public SecureException(MessageSource messageSource, String messageKey) {
		this(messageSource, messageKey, null, null, LocaleContextHolder.getLocale(), null);
	}
	/**
	 * SecureException 생성자
	 * @param messageSource 메세지 리소스
	 * @param messageKey 메세지키값
	 * @param wrappedException 발생한 Exception 내포함.
	 */
	public SecureException(MessageSource messageSource, String messageKey, Exception wrappedException) {
		this(messageSource, messageKey, null, null, LocaleContextHolder.getLocale(), wrappedException);
	}

	public SecureException(MessageSource messageSource, String messageKey, Locale locale, Exception wrappedException) {
		this(messageSource, messageKey, null, null, locale, wrappedException);
	}

	public SecureException(MessageSource messageSource, String messageKey, Object[] messageParameters, Locale locale,
	        Exception wrappedException) {
		this(messageSource, messageKey, messageParameters, null, locale, wrappedException);
	}

	public SecureException(MessageSource messageSource, String messageKey, Object[] messageParameters,
	        Exception wrappedException) {
		this(messageSource, messageKey, messageParameters, null, LocaleContextHolder.getLocale(), wrappedException);
	}

	public SecureException(MessageSource messageSource, String messageKey, Object[] messageParameters,
	        String defaultMessage, Exception wrappedException) {
		this(messageSource, messageKey, messageParameters, defaultMessage, LocaleContextHolder.getLocale(), wrappedException);
	}

	public SecureException(MessageSource messageSource, String messageKey, Object[] messageParameters,
	        String defaultMessage, Locale locale, Exception wrappedException) {
		this.messageKey = messageKey;
		this.messageParameters = messageParameters;
		this.message = messageSource.getMessage(messageKey, messageParameters, defaultMessage, locale);
		this.wrappedException = wrappedException;
	}

}
