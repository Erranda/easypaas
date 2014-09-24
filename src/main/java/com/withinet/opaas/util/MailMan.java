package com.withinet.opaas.util;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMessage.RecipientType;

import static com.withinet.opaas.controller.common.ServiceProperties.*;

public class MailMan {
	
	public void sendMessage(String subject, String address, String content) throws MessagingException {
		Message message = new MimeMessage(getSession());

		message.addRecipient(RecipientType.TO, new InternetAddress(address));
		if (EMAILER_USERNAME == null)
			throw new MessagingException ("Email not setup");
		message.addFrom(new InternetAddress[] { new InternetAddress(EMAILER_USERNAME) });

		message.setSubject(subject);
		message.setContent(content, "text/html");

		Transport.send(message);
	}

	private Session getSession() {
		Authenticator authenticator = new Authenticator();

		Properties properties = new Properties();
		properties.setProperty("mail.smtp.submitter", authenticator.getPasswordAuthentication().getUserName());
		properties.setProperty("mail.smtp.auth", EMAILER_SMTP_AUTH);
		properties.setProperty("mail.smtp.starttls.enable", EMAILER_STARTTLS_ENABLE);
		properties.setProperty("mail.smtp.host", EMAILER_SMTP_HOST);
		properties.setProperty("mail.smtp.port", EMAILER_SMTP_PORT);

		return Session.getInstance(properties, authenticator);
	}

	private class Authenticator extends javax.mail.Authenticator {
		private PasswordAuthentication authentication;

		public Authenticator() {
			String username = EMAILER_USERNAME;
			String password = EMAILER_PASSWORD;
			authentication = new PasswordAuthentication(username, password);
		}

		protected PasswordAuthentication getPasswordAuthentication() {
			return authentication;
		}
	}
}