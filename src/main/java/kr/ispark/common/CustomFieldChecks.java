package kr.ispark.common;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.apache.commons.validator.Field;
import org.apache.commons.validator.ValidatorAction;
import org.springframework.validation.Errors;
import org.springmodules.validation.commons.FieldChecks;

@SuppressWarnings("serial")
public class CustomFieldChecks extends FieldChecks {
	public static boolean validateMaxByteLength(Object bean, ValidatorAction va, Field field, Errors errors) {
		String value = extractValue(bean, field);
		if (value != null) {
			try {
				int max = Integer.parseInt(field.getVarValue("maxByteLength"));
				if (!(maxByteLength(value, max))) {
					rejectValue(errors, field, va);
					return false;
				}
			} catch (NullPointerException npe) {
				rejectValue(errors, field, va);
				return false;
			} catch (Exception e) {
				rejectValue(errors, field, va);
				return false;
			}
		}
		return true;
	}
	
	public static boolean maxByteLength(String value, int max) {
		if(value == null) {
			value = "";
		}
		boolean result;
		try {
			result = (value.getBytes("UTF-8").length <= max);
		} catch (UnsupportedEncodingException uee) {
			result = (value.getBytes().length <= max);
		} catch (Exception e) {
			result = (value.getBytes().length <= max);
		}
		return result;
	}
}
