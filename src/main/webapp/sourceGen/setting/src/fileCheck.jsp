<%@ page language="java" contentType="application/json" pageEncoding="utf-8"%>
<%@ page import="java.sql.*" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="java.util.Enumeration"%>
<%@ page import="java.io.*"%>
<%@ include file="config.jsp" %>
<%

	//var param = "pathLev1="+$("#pathLev1").val()+"&pathLev2="+$("#pathLev2").val()+"&pathLev3="+$("#pathLev3").val()+"&pathLev4="+$("#pathLev4").val();
	
	String existYn = "N";

	String pathLev1 = request.getParameter("pathLev1");
	String pathLev2 = request.getParameter("pathLev2");
	String pathLev3 = request.getParameter("pathLev3");
	String pathLev4 = request.getParameter("pathLev4");

	String fullPath = "kr/ispark/"+pathLev1+"/"+pathLev2+"/"+pathLev3+"/"+pathLev4;
	String path = pathLev1+"/"+pathLev2+"/"+pathLev3+"/"+pathLev4;
	
	System.out.println(fullPath);
	System.out.println(path);
	
	String javaBaseRoot = baseRoot+"/src/main/java/";
	String javaRoot = javaBaseRoot+fullPath;
	String jspRoot = baseRoot+"/src/main/webapp/WEB-INF/jsp/"+path;
	
	System.out.println(javaBaseRoot);
	System.out.println(javaRoot);
	System.out.println(jspRoot);
	
	File fileJava = new File(javaRoot);
	if(fileJava.exists()){
		existYn = "Y";
	}
	File fileJsp = new File(jspRoot);
	if(fileJsp.exists()){
		existYn = "Y";
	}
	
	fileJava = null;
	fileJsp = null;
	System.out.print("{\"existYn\":\""+existYn+"\"}");
	
	out.print("{\"existYn\":\""+existYn+"\"}");
		
%>
