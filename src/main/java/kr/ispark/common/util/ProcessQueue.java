package kr.ispark.common.util;

import java.util.HashSet;
import java.util.Set;

public class ProcessQueue {
	public static Set<String> processQueue = new HashSet<>();
	
	public static void setProcessQueue(String sessionId_methodId){
		processQueue.add(sessionId_methodId);
	}
	
	public static boolean removeProcessQueue(String sessionId_methodId){
		return processQueue.remove(sessionId_methodId);
	}
	
	public static boolean isProcessQueue(String sessionId_methodId){
		return processQueue == null?false:processQueue.contains(sessionId_methodId);
	}
}
