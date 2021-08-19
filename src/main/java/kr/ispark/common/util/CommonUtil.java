package kr.ispark.common.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

public class CommonUtil {

	protected final static int DECIMAL_SCALE = 2;
	protected final static int DECIMAL_ROUND_MODE = BigDecimal.ROUND_HALF_UP;
	protected final static boolean REMOVETRAILINGZERO = false;

	public static String nullToBlank(String s) {
		if(s == null) {
			return "";
		} else {
			return s;
		}
	}
	public static String nullToBlank(Object o) {
		return nullToBlank((String)o);
	}

	public static String removeNull(String s) {
		return nullToBlank(s);
	}

	public static String removeNull(Object s) {
		return nullToBlank((String)s);
	}

	public static String nvl(String s, String s2) {
		return removeNull(s, s2);
	}

	public static String removeNull(String s, String s2) {
		if(isEmpty(s)) {
			return s2;
		}
		return s;
	}

	public static String removeNullTrim(String s) {
		return nullToBlank(s).trim();
	}

	public static boolean isEmpty(String s) {
		return nullToBlank(s).trim().equals("");
	}

	public static boolean isNotEmpty(String s) {
		return !isEmpty(s);
	}

	public static boolean isEmpty(String[] s) {
		return (s == null || s.length == 0);
	}

	public static boolean isNotEmpty(String[] s) {
		return !isEmpty(s);
	}

	public static boolean isEmpty(List<?> list) {
		return (list == null || list.size() == 0);
	}

	public static boolean isNotEmpty(List<?> list) {
		return !isEmpty(list);
	}

	// 엑셀 파일 여부 확인
	public static boolean isExcelFile(String fileName) {
		fileName = fileName.toUpperCase();
		if(fileName.endsWith(".XLS") || fileName.endsWith(".XLSX")) {
			return true;
		} else {
			return false;
		}
	}

	// double을 String으로 변환
	public static String doubleToString(double d) {
		long lValue = (long)d;
		return ((lValue == d) ? Long.toString(lValue) : Double.toString(d));
	}

	// Class에서 변수의 Setter 메소드를 찾아서 리턴
	public static Method findSetMethod(Class<?> cls, String name) {
		Method methods[] = cls.getDeclaredMethods();
		Method m;
		name = "set" + name.substring(0, 1).toUpperCase() + name.substring(1);
		for(int i = 0; i < methods.length; ++i) {
			m = methods[i];
			if(name.equals(m.getName())) {
				return m;
			}
		}

		if(cls.getSuperclass() != null) {
			return findSetMethod(cls.getSuperclass(), name.substring(3));
		} else {
			return null;
		}
	}

	// Class에서 변수의 type을 찾아서 리턴
	public static Class<?> getFieldType(Class<?> cls, String name) {
		Field[] fields = cls.getDeclaredFields();
		for(Field field : fields) {
			if(name.equals(field.getName())) {
				return field.getType();
			}
		}

		if(cls.getSuperclass() != null) {
			return getFieldType(cls.getSuperclass(), name);
		} else {
			return String.class;
		}
	}

	// sql injection escape
	public static String escapeSqlString(String x) {
		StringBuilder sBuilder = new StringBuilder(x.length() * 11/10);

		int stringLength = x.length();

		for (int i = 0; i < stringLength; ++i) {
			char c = x.charAt(i);

			switch (c) {
			case 0: /* Must be escaped for 'mysql' */
				sBuilder.append('\\');
				sBuilder.append('0');

				break;

			case '\n': /* Must be escaped for logs */
				sBuilder.append('\\');
				sBuilder.append('n');

				break;

			case '\r':
				sBuilder.append('\\');
				sBuilder.append('r');

				break;

			case '\\':
				sBuilder.append('\\');
				sBuilder.append('\\');

				break;

			case '\'':
				sBuilder.append('\\');
				sBuilder.append('\'');

				break;

			case '"': /* Better safe than sorry */
				sBuilder.append('\\');
				sBuilder.append('"');

				break;

			case '\032': /* This gives problems on Win32 */
				sBuilder.append('\\');
				sBuilder.append('Z');

				break;

			case ';':
				break;

			case '\u00a5':
			case '\u20a9':
				// escape characters interpreted as backslash by mysql
				// fall through

			default:
				sBuilder.append(c);
			}
		}

		return sBuilder.toString();
	}

	public static String getNumberFormat(String val){
		return getNumberFormat(val,DECIMAL_SCALE,REMOVETRAILINGZERO);
	}

	public static String getNumberFormat(String val, Integer decimal){
		return getNumberFormat(val,decimal,REMOVETRAILINGZERO);
	}

	public static String getNumberFormat(String val, Integer decimal, Boolean removeZero){
		if("".equals(nullToBlank(val))) return null;

		BigDecimal b = new BigDecimal(val);

		if(removeZero) {
			return String.format("%,.DECIMALf".replace("DECIMAL",decimal+""), b.setScale(decimal, DECIMAL_ROUND_MODE).stripTrailingZeros().toPlainString());
		} else {
			return String.format("%,.DECIMALf".replace("DECIMAL",decimal+""), b.setScale(decimal, DECIMAL_ROUND_MODE));
		}
	}

	public static String getNumberComma(String val) {
		return getNumberComma(val, REMOVETRAILINGZERO);
	}

	public static String getNumberCommaRemoveZeros(String val) {
		return getNumberComma(val, true);
	}

	/**
	 * 숫자에 콤마 처리
	 * @param val
	 * @param removeTrailingZeros	소수점 이하 0 제거 여부
	 * @return
	 */
	public static String getNumberComma(String val, boolean removeTrailingZeros) {
		if(isEmpty(val)) return null;

		if(removeTrailingZeros) {
			val = val.replaceAll("((\\d+)(\\.0+$)|(\\.\\d*[1-9])0+)", "$2$4");
		}

		String[] arr = val.split("\\.");
		arr[0] = arr[0].replaceAll("(\\d)(?=(\\d{3})+$)", "$1,");
		return StringUtils.join(arr, ".");
	}
}
