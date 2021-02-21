package com.emailApplication.read_send_mail;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.GregorianCalendar;
import java.util.Properties;
import java.util.ResourceBundle;

import javax.mail.Folder;  
import javax.mail.Message;  
import javax.mail.MessagingException;  
import javax.mail.NoSuchProviderException;  
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMultipart;

import com.emailApplication.lucene.indexing.Indexer;
import com.emailApplication.lucene.model.IndexMessage;
import com.emailApplication.lucene.model.IndexUnit;
import com.emailApplication.lucene.model.UploadModel;
import com.emailApplication.model.Account;
import com.emailApplication.model.MyMessage;
import com.emailApplication.service.MessageService;
import com.emailApplication.tools.DateUtil;

import javax.mail.*;
public class ReadMail{  
	
	private static String DATA_DIR_PATH;
	
	static {
		ResourceBundle rb=ResourceBundle.getBundle("application");
		DATA_DIR_PATH=rb.getString("dataDir");
	}
  
 public static void receiveEmail(String pop3Host, String storeType,  
  Account account,GregorianCalendar maxDateTime,long count,String folder,MessageService messageService) throws ParseException {  
 	Message[] messages;
 	String password=proveraPassworda(account.getPassword());
 	//System.out.println("\nPassword: "+password+"<----------\n");
 	String prom="imaps";
 	String prom2="imap.";
 	String to="";
 	String cc="";
 	String bcc="";
	  try {
		  Properties properties = new Properties(); 
		  if(pop3Host.equals("yahoo.com")) {
		    	pop3Host="mail."+pop3Host;
		    	properties.put("mail.pop3.port", "993");
		    }
		  //System.out.println("\npop3Host"+pop3Host+"<-----------------\n");
	   //1) get the session object  
	  
	   properties.put("mail.imaps.ssl.trust", "*");    
	    Session emailSession = Session.getDefaultInstance(properties,
	   new javax.mail.Authenticator() {
	    protected PasswordAuthentication getPasswordAuthentication() {
	     return new PasswordAuthentication(account.getUsername(),password);
	    }
	   }); 
	   //2) create the POP3 store object and connect with the pop server  
	    //System.out.println(prom2+" "+pop3Host+" "+account.getUsername()+"@"+account.getSmtpAddress()+" "+password);
	   Store emailStore = emailSession.getStore(prom);
	 emailStore.connect(prom2+pop3Host,account.getUsername()+"@"+account.getSmtpAddress(), password);
	  
	   //3) create the folder object and open it  
	   Folder emailFolder = emailStore.getFolder(folder);  
	   emailFolder.open(Folder.READ_ONLY); 
	   
	  
	   //4) retrieve the messages from the folder in an array and print it  
	   messages = emailFolder.getMessages(); 
	   int start=messages.length-1;
	   if(count!=0) {
		   start=messages.length-(int)count;
	   }
	   
	   for (int i=start; i < messages.length; i++) {  
	    Message m = messages[i]; 
	    GregorianCalendar datumPorukeSaNeta=DateUtil.getGregorianCalendarFromDate(m.getSentDate());
//	    System.out.println("Proveravam: "+i+" poruku.");
//	    System.out.println("datumPorukeSaNeta je poslata: "+DateUtil.formatTimeWithSecond(datumPorukeSaNeta));
//	    System.out.println("maxDateTime je poslata: "+DateUtil.formatTimeWithSecond(maxDateTime));
	    if(datumPorukeSaNeta.getTimeInMillis()>maxDateTime.getTimeInMillis()) {
	    	//System.out.println("Postoji nova poruka! \nPocinjem sa upisivanjem!");
	    	MyMessage message=new MyMessage();
	    	String from="";
	    	//System.out.println("From: "+m.getFrom()[0].toString());
	    	if(m.getFrom()[0].toString().contains("<")){
	    		String[] mail=m.getFrom()[0].toString().split("<");
	    		from=mail[1].substring(0, mail[1].length()-1);
	    	}else {
	    		from=m.getFrom()[0].toString();
	    	}
	    	message.set_from(from);
	    	if(m.getRecipients(Message.RecipientType.TO)!=null) {
	    		 for (int j=0; j<m.getRecipients(Message.RecipientType.TO).length; j++) {
	    			 //System.out.println("Prvi email u To je: "+m.getRecipients(Message.RecipientType.TO)[j].toString());
	    			 if(m.getRecipients(Message.RecipientType.TO)[j].toString().contains("<")) {
				    		String[] mail=m.getRecipients(Message.RecipientType.TO)[j].toString().split("<");
				    			to=to+mail[1].substring(0, mail[1].length()-1)+",";
				    	}else {
				    			to=to+m.getRecipients(Message.RecipientType.TO)[j].toString()+",";
				    	}
	 		    }
	    	}
	    	if(m.getRecipients(Message.RecipientType.CC)!=null) {
			    for (int j=0; j<m.getRecipients(Message.RecipientType.CC).length; j++) {
			    	if(m.getRecipients(Message.RecipientType.CC)[j].toString().contains("<")) {
			    		String[] mail=m.getRecipients(Message.RecipientType.CC)[j].toString().split("<");
			    			cc=cc+mail[1].substring(0, mail[1].length()-1)+",";
			    	}else {
			    			cc=cc+m.getRecipients(Message.RecipientType.CC)[j].toString()+",";
			    	}
			    }
	    	}
	    	if(m.getRecipients(Message.RecipientType.BCC)!=null) {
			    for (int j=0; j<m.getRecipients(Message.RecipientType.BCC).length; j++) {
			    	if(j==(m.getRecipients(Message.RecipientType.BCC).length-1)) {
			    		bcc=bcc+m.getRecipients(Message.RecipientType.BCC)[j].toString();
		    		}else {
		    			bcc=bcc+m.getRecipients(Message.RecipientType.BCC)[j].toString()+",";
		    		}
			    }
	    	}
	    	if(!to.equals("")) {
	    		to=to.substring(0,to.length()-1);
	    	}
	    	if(!cc.equals("")) {
	    		cc=cc.substring(0,cc.length()-1);
	    	}
	    	if(!bcc.equals("")) {
	    		bcc=bcc.substring(0,bcc.length()-1);
	    	}
		    message.set_to(to);
		    message.set_cc(cc);
		    message.set_bcc(bcc);
		    message.setDateTime(DateUtil.getGregorianCalendarFromDate(m.getSentDate()));
		    message.setSubject(m.getSubject());
		    String content="";
		    // suppose 'message' is an object of type Message
		    String contentType = m.getContentType();
		    
		    if (contentType.contains("multipart")) {
	        // this message may contain attachment
		    	Multipart multiPart = (Multipart) m.getContent();
		    	
		    	for (int j = 0; j < multiPart.getCount(); j++) {
		    	    MimeBodyPart part = (MimeBodyPart) multiPart.getBodyPart(j);
		    	    if (Part.ATTACHMENT.equalsIgnoreCase(part.getDisposition())) {
		    	        // this part is attachment
		    	    	// this part is attachment
                        String fileName = part.getFileName();
                     
                        part.saveFile(DATA_DIR_PATH + File.separator + fileName);
                       // System.out.println(DATA_DIR_PATH + "/" + fileName);
                        message.setAttachment_location(DATA_DIR_PATH + "/" + fileName);
		    	    } else {
		    	    	content = getTextFromMimeMultipart(multiPart);
		    	    	//System.out.println("Text je: "+getTextFromMimeMultipart(multiPart)); 
		    	    }
		    	}
		    }
		    else if (contentType.contains("text/plain")
                    || contentType.contains("text/html")) {
                if (m.getContent() != null) {
                    content = m.getContent().toString();
                }
		    }
//		    System.out.println("Duzina contenta: "+content.length());
//		    System.out.println("Content: "+content);
		    message.setContent(content);
		    message.setAccount(account);
		    account.getMessages().add(message);
		    System.out.println("Upisujem mejl!");
		    //System.out.println("'nMessage: "+message.getSubject()+" From: "+message.get_from()+" Content: "+message.getContent());
	    	messageService.save(message);
			//System.out.println(i+". poruka je upisana.");
	    }
	   }  
	   //System.out.println("Zavrsio sa upisivanjem mejlova!");
	  
	   //5) close the store and folder objects  
	   emailFolder.close(false);  
	   emailStore.close(); 
	  } catch (NoSuchProviderException e) {e.printStackTrace();}   
	  catch (MessagingException e) {e.printStackTrace();} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
 }
 
 public static String getTextFromMimeMultipart(
	        Multipart mimeMultipart)  throws MessagingException, IOException{
	    String result = "";
	    int count = mimeMultipart.getCount();
	    for (int i = 0; i < count; i++) {
	        BodyPart bodyPart = mimeMultipart.getBodyPart(i);
	        if (bodyPart.isMimeType("text/plain")) {
	            result = result + "\n" + bodyPart.getContent();
	            break; // without break same text appears twice in my tests
	        } else if (bodyPart.isMimeType("text/html")) {
	            String html = (String) bodyPart.getContent();
	            result = result + "\n" + org.jsoup.Jsoup.parse(html).text();
	        } else if (bodyPart.getContent() instanceof Multipart){
	            result = result + getTextFromMimeMultipart((Multipart)bodyPart.getContent());
	        }
	    }
	    return result;
	}
 
 public static String proveraPassworda(String pass) {
	 int count = pass.lastIndexOf("|");
	 //System.out.println("index | je: "+count+"<-----------------------------\n");
	 String returnValue=pass.substring(count+1);
	 return returnValue;
 }

// public static void main(String[] args) {  
//  
//  String host = "smtp.gmail.com";//change accordingly  
//  String mailStoreType = "pop3";  
//  final String username= "rakindejan@gmail.com";  
//  final String password= "pexlqolkzswsczrj";//change accordingly  
//  
//  receiveEmail(host, mailStoreType, username, password);  
//  
// }  
}
