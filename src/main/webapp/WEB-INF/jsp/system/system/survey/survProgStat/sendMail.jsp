<%@ page language="java" contentType="text/html; charset=euc-kr" pageEncoding="euc-kr"%>
<%@ page import="java.util.Properties"%>
<%@ page import="javax.activation.*"%>
<%@ page import="javax.mail.*"%>
<%@ page import="javax.mail.internet.*"%>
<% request.setCharacterEncoding("euc-kr"); %>

<%
	String receiverMail = request.getParameter("receiverMail") == null ? "" : request.getParameter("receiverMail");
	String subject = request.getParameter("subject") == null ? " " : request.getParameter("subject");
	String message = request.getParameter("message") == null ? " " : request.getParameter("message");
	String senderMail = request.getParameter("senderMail") == null ? "no_reply@knfc.co.kr" : request.getParameter("senderMail");
	String senderName = request.getParameter("senderName") == null ? "전자결재" : request.getParameter("senderName");
	String smtpHost  = "email.knfc.co.kr";
	
	if(senderMail.equals(receiverMail)){
		senderMail = "no_reply@knfc.co.kr";
		senderName = "전자결재";
	}
	
	try {
		boolean sessionDebug = false;

		Properties props = new Properties();
		props.put("mail.smtp.host", smtpHost);
		
		//props.put("mail.transport.protocol", "smtp");
		//props.put("mail.smtp.port", "25");
		//props.put("mail.smtp.auth", "true");
		
		//SmtpAuthenticator auth = new SmtpAuthenticator("", "");
		
		Session sess = javax.mail.Session.getInstance(props,null);
		
		//sess.setDebug(sessionDebug);
		
		InternetAddress addr = new InternetAddress();
		//addr.setPersonal("전자결재","UTF-8");
		//addr.setAddress("gw_trans@knfc.co.kr");
		addr.setPersonal(senderName,"UTF-8");
		addr.setAddress(senderMail);
		
		// create a message
		Message msg = new MimeMessage(sess);
		msg.setFrom(addr);         
		msg.setSubject(MimeUtility.encodeText(subject, "euc-kr","B"));
		msg.setContent(message, "text/html;charset=euc-kr");
		msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(receiverMail));
		
		Transport.send(msg);
	} catch (Exception e) {
		e.printStackTrace();
		out.println("no send");
	}
	out.println("ok");
%> 

<%!
public class SmtpAuthenticator extends Authenticator
{ 
   private PasswordAuthentication password_auth;

   public SmtpAuthenticator(String smtp_user, String smtp_password) {
      password_auth = new PasswordAuthentication(smtp_user, smtp_password);
   }

   public PasswordAuthentication getPasswordAuthentication(){
      return password_auth;
   }
}
%>