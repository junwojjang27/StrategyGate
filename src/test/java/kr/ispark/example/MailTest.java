package kr.ispark.example;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class MailTest {

	static final String FROM = "yhkim@ispark.kr";
	static final String FROMNAME = "StrategyGate";
	static final String TO = "yhkim@ispark.kr";
	static final String SMTP_USERNAME = "yhkim@ispark.kr";
	static final String SMTP_PASSWORD = "1q2w3e4r5t!";

	static final String HOST = "smtps.hiworks.com";
	static final int PORT = 465;

	static final String SUBJECT = "메일테스트";

	public static void main(String[] args) throws Exception {
		Properties props = System.getProperties();
		props.put("mail.smtp.host", HOST);
		props.put("mail.smtp.port", PORT);
		props.put("mail.smtp.auth", "true");
		props.put("mail.transport.protocol", "smtp");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.ssl.enable", "true");
		props.put("mail.smtp.ssl.trust", HOST);

		Session session = Session.getDefaultInstance(props);

		MimeMessage msg = new MimeMessage(session);
		msg.setFrom(new InternetAddress(FROM,FROMNAME));
		//msg.setRecipient(Message.RecipientType.TO, new InternetAddress(TO));
		msg.addRecipient(Message.RecipientType.TO, new InternetAddress(TO));
		msg.setSubject(SUBJECT);
		msg.setContent("한글 ^_^<br/><h3>HTML</h3>","text/html;charset=UTF-8");

		Transport transport = session.getTransport();

		try {
			System.out.println("Sending...");

			transport.connect(HOST, SMTP_USERNAME, SMTP_PASSWORD);
			transport.sendMessage(msg, msg.getAllRecipients());

			System.out.println("Email sent!");
		} catch (Exception ex) {
			System.out.println("The email was not sent.");
			System.out.println("Error message: " + ex.getMessage());
		} finally {
			transport.close();
		}
	}

}
