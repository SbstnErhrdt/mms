package controller;

import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;

public class EmailController {

	// Sender's email
	private final static String sender = "sopra@ex-studios.net";

	// Sending host
	private final static String host = "localhost";
	
	// message content
	private final static String messageContent = "Danke für Ihre Registrierung beim Modul Management System der Universität Ulm. \n" +
			"Damit Sie sich in Zukunft einloggen können, klicken Sie bitte auf diesen Link: \n";
	
	/**
	 * @param recipient
	 * @param hash
	 * @return true, if the email was successfully sent
	 */
	public static boolean sendEmail(String recipient, String hash) {

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
			
			
			String content = messageContent+"http://sopra.ex-studios.net/#/confirm?token="+hash;

			// set the actual message
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