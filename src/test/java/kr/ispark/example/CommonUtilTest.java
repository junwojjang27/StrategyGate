package kr.ispark.example;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.springframework.test.context.web.WebAppConfiguration;


@WebAppConfiguration
public class CommonUtilTest {

	public static String getNumberComma(String val){
		StringBuilder  num = new StringBuilder();
		String dot = "";
		String result = "";
		if(val.indexOf(".") > -1){
			num = num.append(val.substring(0, val.indexOf(".")-1)).reverse();
			dot = val.substring(val.indexOf("."));
		}else{
			num = num.append(val).reverse();
		}

		char[] c = num.toString().toCharArray();

		for(int i = c.length-1 ; i >= 0 ; i--){
			result += c[i];
			if(i%3 == 0 && i != 0){result += ",";}
		}

		return result+dot;
	}

	public static String getNumberComma2(String val){
		StringBuilder  num = new StringBuilder();
		String dot = "";
		String result = "";
		if(val.indexOf(".") > -1){
			num = num.append(val.substring(0, val.indexOf("."))).reverse();
			dot = val.substring(val.indexOf("."));
		}else{
			num = num.append(val).reverse();
		}

		char[] c = num.toString().toCharArray();

		for(int i = c.length-1 ; i >= 0 ; i--){
			result += c[i];
			if(i%3 == 0 && i != 0){result += ",";}
		}

		return result+dot;
	}

	public static String getNumberCommaNew(String val) {
		val = val.replaceAll("((\\d+)(\\.0+$)|(\\.\\d*[1-9])0+)", "$2$4");

		String[] arr = val.split("\\.");
		arr[0] = arr[0].replaceAll("(\\d)(?=(\\d{3})+$)", "$1,");
		return StringUtils.join(arr, ".");
	}


	@Test
	public void testSelect() throws Exception {
		System.out.println("\n\n\n#### CommonUtilTest.testSelect ###");


		String[] arr = new String[] {
				"1",
				"10",
				"12",
				"123",
				"1234",
				"12345",
				"123456",
				"1234567",
				"12345678",
				"123456789",
				"1234567890",
				"1.0000000000",
				"10.0000000000",
				"12.0000000000",
				"123.0000000000",
				"1234.0000000000",
				"12345.0000000000",
				"123456.0000000000",
				"1234567.0000000000",
				"12345678.0000000000",
				"123456789.0000000000",
				"1234567890.0000000000",
				"-1",
				"-10",
				"-12",
				"-123",
				"-1234",
				"-12345",
				"-123456",
				"-1234567",
				"-12345678",
				"-123456789",
				"-1234567890",
				"-1.0000000000",
				"-10.0000000010",
				"-12.0000000010",
				"-123.0000000100",
				"-1234.0000001000",
				"-12345.0000010000",
				"-123456.0000100000",
				"-1234567.0001000000",
				"-12345678.0010000000",
				"-123456789.0100000000",
				"-1234567890.1000000000"
		};
		for(String s : arr) {
			//System.out.printf("%s ->\t%s\t%s\t%s\n", s, getNumberComma(s), getNumberComma2(s), getNumberCommaNew(s));
			System.out.printf("%s ->\t%s\n", s, getNumberCommaNew(s));
		}

		System.out.println("#######################\n\n\n");
	}
}
