package com.emailApplication.read_send_mail;
import java.text.ParseException;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import com.emailApplication.model.Account;
import com.emailApplication.model.MyMessage;
import com.emailApplication.tools.DateUtil;

public class SendMail {
   public static void send(MyMessage myMessage,Account account) throws ParseException {
      // Recipient's email ID needs to be mentioned.

      // Sender's email ID needs to be mentioned
      final String username = account.getDisplayname();//change accordingly
      final String password = ReadMail.proveraPassworda(account.getPassword());//change accordingly

      // Assuming you are sending email through relay.jangosmtp.net
//025793117|qacuhygtjzorzokq
        Properties props = new Properties();
        System.out.println("Host: "+account.getSmtpAddress());
        props.put("mail.smtp.auth", "true");
        //props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp."+account.getSmtpAddress());
        props.put("mail.smtp.port", "587");
        props.put("mail.imaps.ssl.trust", "*");    

      // Get the Session object.
      Session session = Session.getInstance(props,
         new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
               return new PasswordAuthentication(username, password);
    }
         });

      try {
    // Create a default MimeMessage object.
    Message message = new MimeMessage(session);
 
    // Set From: header field of the header.
    message.setFrom(new InternetAddress(username));
 
    if(!myMessage.get_to().equals("")) {
        // Set To: header field of the header.
        message.setRecipients(Message.RecipientType.TO,
                   InternetAddress.parse(myMessage.get_to()));
    }
 
    if(!myMessage.get_cc().equals("")) {
    	message.setRecipients(Message.RecipientType.CC,
                InternetAddress.parse(myMessage.get_cc()));
    }
    
    if(!myMessage.get_bcc().equals("")) {
    	message.setRecipients(Message.RecipientType.BCC,
                InternetAddress.parse(myMessage.get_bcc()));
    }
 
    // Set Subject: header field
    message.setSubject(myMessage.getSubject());
    
    message.setSentDate(DateUtil.getDateFromGregorianCalendar(myMessage.getDateTime()));
    
    // Now set the actual message
    message.setText(myMessage.getContent());

    // Send message
    Transport.send(message);

    System.out.println("Sent message successfully....");

      } catch (MessagingException e) {
         throw new RuntimeException(e);
      }
   }
}
