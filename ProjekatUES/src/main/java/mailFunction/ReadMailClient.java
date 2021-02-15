package mailFunction;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.Certificate;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.List;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import com.emailApplication.model.Account;
import com.emailApplication.model.MyMessage;
import com.emailApplication.model.User;
import com.emailApplication.modelDTO.MessageDTO;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.model.Message;

import support.MailHelper;
import support.MailReader;

public class ReadMailClient extends MailClient {

	public static long PAGE_SIZE = 5;
	public static boolean ONLY_FIRST_PAGE = true;
	
	public static List<MyMessage> readMessage(Account a) throws IOException, InvalidKeyException, NoSuchAlgorithmException, InvalidKeySpecException, IllegalBlockSizeException, BadPaddingException, MessagingException, NoSuchPaddingException, InvalidAlgorithmParameterException, NoSuchProviderException {
        
		System.out.println("Email of user: "+a.getUsername());
		
		// Build a new authorized API client service.
        List<MyMessage> mess=new ArrayList<MyMessage>();
		
		Gmail service = getGmailService();
        ArrayList<MimeMessage> mimeMessages = new ArrayList<MimeMessage>();
        
        String user = "me";
        String query = "is:unread label:INBOX";
        
        List<Message> messages = MailReader.listMessagesMatchingQuery(service, user, query, PAGE_SIZE, ONLY_FIRST_PAGE);
        for(int i=0; i<messages.size(); i++) {
        	Message fullM = MailReader.getMessage(service, user, messages.get(i).getId());
        	
        	MimeMessage mimeMessage;
			try {
				
				mimeMessage = MailReader.getMimeMessage(service, user, fullM.getId());
				
				System.out.println("\n Message number " + i);
				System.out.println("From: " + mimeMessage.getHeader("From", null));
				System.out.println("Subject: " + mimeMessage.getSubject());
				System.out.println("Body: " + MailHelper.getText(mimeMessage));
				System.out.println("\n");
				
				MyMessage message= new MyMessage();
				message.set_from(mimeMessage.getHeader("From", null));
				message.set_to(a.getUsername());
				
				mimeMessages.add(mimeMessage);
				mess.add(message);
	        
			} catch (MessagingException e) {
				e.printStackTrace();
			}	
        }
        
//        System.out.println("Select a message to decrypt:");
//        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
//	        
//	    String answerStr = reader.readLine();
//	    Integer answer = Integer.parseInt(answerStr);
        
      //MimeMessage chosenMessage = mimeMessages.get(answer);
        
        /*for (MimeMessage chosenMessage : mimeMessages) {
        	
        	String email=chosenMessage.getHeader("From", null);
        	String content=chosenMessage.getContent().toString();
        	try {
        		
        		MessageDTO messageDTO= new MessageDTO();
        		messageDTO.setContent(decompressedBodyText);
        		messageDTO.setSubject(decompressedSubjectTxt);
        		messageDTO.setEmailAddress(email);
        		
        		if(!messageDTO.getEmailAddress().contains("<notification@facebookmail.com>")) {
        			mess.add(messageDTO);
        		}
        	}catch (Exception e) {
        		MessageDTO messageDTO= new MessageDTO();
        		messageDTO.setContent(MailHelper.getText(chosenMessage));
        		messageDTO.setSubject(chosenMessage.getSubject());
        		messageDTO.setEmailAddress(email);
        		if(!messageDTO.getEmailAddress().contains("<notification@facebookmail.com>")) {
        			mess.add(messageDTO);
        		}
			}
		}*/
	    return mess;
	}
}
