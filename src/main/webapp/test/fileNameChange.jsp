<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ page import="java.io.File" %>
<%@ page import="java.nio.charset.Charset" %>
<%

String filePath = "C:/work/workspace/StrategyGate/src/main/webapp/WEB-INF/DOC_remove";
String targetPath = "C:/work/workspace/StrategyGate/src/main/webapp/WEB-INF/DOC_remove";

File file = new File(filePath);

if(file.isDirectory()){
	File[] files = file.listFiles();
	
	String name = "";
	String newName = "";
	
	for(File f : files){
		name = f.getName();
		newName = new String(name.getBytes("euc-kr"), "ksc5601");
		
		out.print("name        : " + name + "*******************************************************<br>");
		out.print("utf-8 -> euc-kr        : " + new String(name.getBytes("utf-8"), "euc-kr") + "<br>");
		out.print("utf-8 -> euc-kr        : " + new String(name.getBytes("utf-8"), "euc-kr") + "<br>");
		out.print("utf-8 -> ksc5601       : " + new String(name.getBytes("utf-8"), "ksc5601") + "<br>");
		out.print("utf-8 -> x-windows-949 : " + new String(name.getBytes("utf-8"), "x-windows-949") + "<br>");
		out.print("utf-8 -> iso-8859-1    : " + new String(name.getBytes("utf-8"), "iso-8859-1") + "<br>");
		out.print("iso-8859-1 -> euc-kr        : " + new String(name.getBytes("iso-8859-1"), "euc-kr") + "<br>");
		out.print("iso-8859-1 -> ksc5601       : " + new String(name.getBytes("iso-8859-1"), "ksc5601") + "<br>");
		out.print("iso-8859-1 -> x-windows-949 : " + new String(name.getBytes("iso-8859-1"), "x-windows-949") + "<br>");
		out.print("iso-8859-1 -> utf-8         : " + new String(name.getBytes("iso-8859-1"), "utf-8") + "<br>");
		out.print("euc-kr -> utf-8         : " + new String(name.getBytes("euc-kr"), "utf-8") + "<br>");
		out.print("euc-kr -> ksc5601       : " + new String(name.getBytes("euc-kr"), "ksc5601") + "<br>");
		out.print("euc-kr -> x-windows-949 : " + new String(name.getBytes("euc-kr"), "x-windows-949") + "<br>");
		out.print("euc-kr -> iso-8859-1    : " + new String(name.getBytes("euc-kr"), "iso-8859-1") + "<br>");
		out.print("ksc5601 -> euc-kr        : " + new String(name.getBytes("ksc5601"), "euc-kr") + "<br>");
		out.print("ksc5601 -> utf-8         : " + new String(name.getBytes("ksc5601"), "utf-8") + "<br>");
		out.print("ksc5601 -> x-windows-949 : " + new String(name.getBytes("ksc5601"), "x-windows-949") + "<br>");
		out.print("ksc5601 -> iso-8859-1    : " + new String(name.getBytes("ksc5601"), "iso-8859-1") + "<br>");
		out.print("x-windows-949 -> euc-kr     : " + new String(name.getBytes("x-windows-949"), "euc-kr") + "<br>");
		out.print("x-windows-949 -> utf-8      : " + new String(name.getBytes("x-windows-949"), "utf-8") + "<br>");
		out.print("x-windows-949 -> ksc5601    : " + new String(name.getBytes("x-windows-949"), "ksc5601") + "<br>");
		out.print("x-windows-949 -> iso-8859-1 : " + new String(name.getBytes("x-windows-949"), "iso-8859-1") + "<br>");
		
		File newFile = new File(targetPath+f.separator+"new_"+newName);
		if(f.exists()){
			if(f.renameTo(newFile)){
				out.print("성공");		
			}else{
				out.print("실패");
			};
		}
	}
}

%>
