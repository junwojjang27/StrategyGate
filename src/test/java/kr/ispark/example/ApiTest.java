/**
 * API 기능 테스트용 CLASS
 * @author 김영환
 * - 실제 사용하려면 context-security.xml 파일의 권한 체크 예외 URL(customSecurityMetadataSource)에
 *	<b:value>/api/apiTest.do</b:value>
 *	를 추가해줘야 함.
 */
package kr.ispark.example;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class ApiTest {
	public static void main(String[] args) {
		try {
			String apiURL = "http://localhost:8080/StrategyGate/api/apiTest.do";
			URL url = new URL(apiURL);
			HttpURLConnection con = (HttpURLConnection)url.openConnection();
			con.setDoOutput(true);
			con.setRequestMethod("POST");
			con.setRequestProperty("COMP_ID", "test");
			con.setRequestProperty("KEY", "9efee8bf91c0032139316416691684212a2f363de0ed4a46da3b4f8c3ca6c618134ccde68af3869b");
			con.setRequestProperty("PROCESS_ID", "METRIC");

			String postParams = "metricId=M000001&metricId=M000002&metricId=M000003";
			postParams += "&metricNm=지표1&metricNm=지표2&metricNm=지표3";
			postParams += "&value=값1&value=값2&value=값3";

			/*
			DataOutputStream wr = new DataOutputStream(con.getOutputStream());
			wr.writeBytes(postParams);
			*/
			OutputStream wr = con.getOutputStream();
			wr.write(postParams.getBytes("UTF-8"));
			wr.flush();
			wr.close();

			int responseCode = con.getResponseCode();
			BufferedReader br;
			if(responseCode==200) {	// 정상 호출
				br = new BufferedReader(new InputStreamReader(con.getInputStream()));
			} else {	// 에러 발생
				br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
			}
			String inputLine;
			StringBuffer response = new StringBuffer();
			while ((inputLine = br.readLine()) != null) {
				response.append(inputLine);
			}
			br.close();
			System.out.println(response.toString());

			con.disconnect();
		} catch (Exception e) {
			System.out.println(e);
		}
	}
}
