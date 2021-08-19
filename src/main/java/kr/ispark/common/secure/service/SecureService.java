package kr.ispark.common.secure.service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.apache.commons.codec.binary.Base64;
import org.springframework.stereotype.Component;

import egovframework.rte.fdl.cryptography.EgovCryptoService;


/**
 * 보안과 관련된 유틸 모음
 * @author mist
 *
 */
@Component
public class SecureService {

	@Resource(name="ARIACryptoService")
	private EgovCryptoService cryptoService;
	private static final String SECURE_KEY = "lexken_bsc_5";


	private static EgovCryptoService staticCryptoService;

	@PostConstruct
    public void init() {
		staticCryptoService = cryptoService;
    }

	public static String encryptStr(String str) throws Exception {
		byte[] encrypted = staticCryptoService.encrypt(str.getBytes("UTF-8"), SECURE_KEY);
		String encryptedStr = new String(Base64.encodeBase64(encrypted),"UTF-8");
		return encryptedStr;
	}

	public static String decryptStr(String str) throws Exception {
		byte[] decrypted = staticCryptoService.decrypt(Base64.decodeBase64(str.getBytes("UTF-8")), SECURE_KEY);
		String decryptedStr = new String(decrypted,"UTF-8");
		return decryptedStr;
	}

}