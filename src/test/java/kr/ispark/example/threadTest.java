package kr.ispark.example;

import java.lang.reflect.Method;

public class threadTest extends Thread{
	
	String methodNm = "";
	
	public threadTest(String methodNm){
		this.methodNm = methodNm; 
	}
	
	public threadTest(){
	}
	
	public void run(){
		
		try{
			Class<?> c = threadTest.class;
			Method[] m = c.getDeclaredMethods();
			for(Method method : m){
				if(methodNm.equals(method.getName())){
					method.setAccessible(true);
					
					Object o = c.newInstance();
					method.invoke(o);
				}
			}
			
		}catch(Exception e){
		}
	}
	
	public void first(){
		System.out.println("first thread");
	}
	
	public void second(){
		System.out.println("second thread");
	}
	
	public void third(){
		System.out.println("third thread");
	}
	
	public void forth(){
		System.out.println("forth thread");
	}
	
	public void fifth(){
		System.out.println("fifth thread");
	}
}
