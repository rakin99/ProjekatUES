package com.emailApplication.service.impl;

import java.io.File;
import java.io.IOException;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Service;

import com.emailApplication.lucene.indexing.Indexer;
import com.emailApplication.lucene.model.IndexMessage;
import com.emailApplication.model.Account;
import com.emailApplication.model.MyMessage;
import com.emailApplication.repository.MessageRepository;
 

@Service
public class MessageService implements com.emailApplication.service.MessageService{
	
	@Autowired
	MessageRepository messageRepository;
	
	@Override
	public MyMessage findById(long messageId) {
		return messageRepository.findById(messageId);
	}
	
	@Override
	public List<MyMessage> findByAccountOrderBySubjectAsc(Account account){
		return messageRepository.findByAccountOrderBySubjectAsc(account);
	}

	@Override
	public int maxId() {
		int maxId=messageRepository.findMaxId();
		return maxId;
	}

	@Override
	public long count(String username) {
		long count=messageRepository.count(username);
		return count;
	}
	
	@Override
	public MyMessage save(MyMessage message) {
		MyMessage mess=null;
		try {
			mess = messageRepository.save(message);
			indexNewMessage();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return mess;
	}

	@Override
	public GregorianCalendar getMaxDate(String username) {
		return messageRepository.getMaxDate(username);
	}

	@Override
	public List<MyMessage> findAllSentMessage(String username) {
		return messageRepository.findAllSentMessage(username);
	}

	@Override
	public List<MyMessage> findByAccountOrderByFromAsc(Account account) {
		return messageRepository.findByAccountOrderByFromAsc(account);
	}

	@Override
	public List<MyMessage> findByAccountOrderByDateTimeAsc(Account account) {
		return messageRepository.findByAccountOrderByDateTimeAsc(account);
	}

	@Override
	public List<MyMessage> findByAccountOrderBySubjectDesc(Account account) {
		return messageRepository.findByAccountOrderBySubjectDesc(account);
	}

	@Override
	public List<MyMessage> findByAccountOrderByFromDesc(Account account) {
		return messageRepository.findByAccountOrderByFromDesc(account);
	}

	@Override
	public List<MyMessage> findByAccountOrderByDateTimeDesc(Account account) {
		return messageRepository.findByAccountOrderByDateTimeDesc(account);
	}

	@Override
	public List<MyMessage> findAllByToReciver(String email) {
		return messageRepository.findAllByToReciver(email);
	}
	
	private void indexNewMessage() throws IOException{
		Indexer indexer = Indexer.getInstance();
		List<MyMessage> messages = messageRepository.findAll();
		//System.out.println("\n\tIndeksiram...");
		for (MyMessage message : messages) {
			IndexMessage indexMessage = new IndexMessage();
			if(!message.getAttachment_location().isEmpty()) {
				indexMessage = Indexer.getInstance().getHandler(message.getAttachment_location()).getIndexMessage(new File(message.getAttachment_location()));
			}
			indexMessage.setId(message.getId());
	     	indexMessage.setSubject(message.getSubject());
	     	indexMessage.setContent(message.getContent());
	     	indexMessage.setFromSender(message.get_from());
	     	indexMessage.setToReciver(message.get_to());
	     	//System.out.println(indexMessage.toString());
	     	//indexUnit.setKeywords(new ArrayList<String>(Arrays.asList(model.getKeywords().split(" "))));
	     	indexer.add(indexMessage.getLuceneDocument());
		}
}
}
