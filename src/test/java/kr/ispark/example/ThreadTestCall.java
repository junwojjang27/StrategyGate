package kr.ispark.example;

import org.junit.Test;

public class ThreadTestCall{
	
	@Test
	public void test1(){
		
		try {
			threadTest t1 = new threadTest("first");
			t1.start();
			t1.join();
			threadTest t2 = new threadTest("second");
			t2.start();
			t2.join();
			threadTest t3 = new threadTest("third");
			t3.start();
			t3.join();
			threadTest t4 = new threadTest("forth");
			t4.start();
			t4.join();
			threadTest t5 = new threadTest("fifth");
			t5.start();
			t5.join();
		
        }catch(Exception e) {
        }
		
		System.out.println("---end thread call---");
	}
	
}
