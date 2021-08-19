package kr.ispark.common.util;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import egovframework.com.cmm.service.EgovProperties;

public class MultiReloadUtil{

	private static String[] connectionReLoadUrls;

	private static void setConnectionReLoadUrl(){
		String tempUrls = EgovProperties.getProperty("TARGET_CONNECTION_RELOAD_URL");
		if(tempUrls != null && !"".equals(tempUrls)){
			connectionReLoadUrls = tempUrls.split("\\|");
		}else{
			connectionReLoadUrls = null;
		}
	}

    public static String connectionReLoad()throws Exception {
		String tempResult = "";
		if(connectionReLoadUrls == null){
			setConnectionReLoadUrl();
		}

		if(connectionReLoadUrls != null && connectionReLoadUrls.length > 0){
			for(int i=0; i<connectionReLoadUrls.length; i++){
				tempResult = getHttpPage(connectionReLoadUrls[i]+"/connectionReload.do");
			}
		}
		
		return tempResult;
	}

	/**
     * targetUrl에 해당하는 화면을 조회
     * @param targetUrl 조회할 화면의 URL(Http://localhost:80/test.html)
     * @return
     */
    public static String getHttpPage(String targetUrl) {
        URL url = null;
        URLConnection con = null;
        DataOutputStream out = null;
        InputStream in = null;
        InputStreamReader insr = null;

        BufferedReader br = null;
        StringBuffer buf = new StringBuffer();
        String str = "";
        try {
            url = new URL(targetUrl);
            con = url.openConnection();

            con.setDoInput(true);
            con.setDoOutput(true);

            con.setUseCaches(false);
            con.setRequestProperty("content-Type", "application/x-www-form-urlencoded;charset=utf-8");

            in = con.getInputStream();

            insr = new InputStreamReader(in, "utf-8");
            br = new BufferedReader(insr);

            String temp = null;

            while((temp = br.readLine())!=null) {
                buf.append(temp);
                buf.append("\n");
            }

            System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$ : "+buf.toString());
            
            return buf.toString();
        }
        catch(Exception e) {
            e.printStackTrace(System.err);
            return null;
        }
        finally {
            try {
                if( out != null ) {
                    out.flush();
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            }
            catch(Exception e) {
            }
        }
    }


}

