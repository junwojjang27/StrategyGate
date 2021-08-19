package kr.ispark.example;

import java.io.File;
import java.io.InputStream;
import java.util.Properties;

import org.junit.Test;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import kr.ispark.common.util.PropertyUtil;

@Component
@PropertySource("classpath:egovframework/egovProps/globals.properties")
public class atchFileTest {

	@Test
	public void test(){
		
		
		Properties prop = new Properties();
		InputStream in = getClass().getResourceAsStream("egovframework.egovProps.globals.properties");
		try{
			prop.load(in);
		}catch(Exception e){
			e.getMessage();
		}
		
		String path = prop.getProperty("Globals.fileStorePath");
		
		File upFile = new File(path+"manual-all");
		System.out.println("path : "+path);
		System.out.println("upFile.isDirectory : "+upFile.isDirectory());
	}
}
