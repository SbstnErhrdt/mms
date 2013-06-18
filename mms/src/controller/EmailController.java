package controller;

import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;

public class EmailController {

	public static boolean sendEmail(String recipient, String hash) {
		
		// Sender's email
		String sender = "sopra@ex-studios.net";

		// Sending host
		String host = "localhost";

		System.out.println("trying to send email from "+sender+" with host " +
				host+" to "+recipient);
		
		// Get system properties
		Properties properties = System.getProperties();

		// Setup mail server
		properties.setProperty("mail.smtp.host", host);

		// Get the default Session object.
		Session session = Session.getDefaultInstance(properties);

		try {
			// Create a default MimeMessage object.
			MimeMessage message = new MimeMessage(session);

			// Set From: header field of the header.
			message.setFrom(new InternetAddress(sender));

			// Set To: header field of the header.
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(
					recipient));

			// Set Subject: header field
			message.setSubject("Email Confirmation");
			
			
			String content = "http://sopra.ex-studios.net/#/confirm?token="+hash;

			// Now set the actual message
			message.setText(content);

			// Send message
			Transport.send(message);
			System.out.println("email to: "+recipient+" sent successfully");
			return true;
		} catch (MessagingException e) {
			e.printStackTrace();
			return false;
		}
	}
}