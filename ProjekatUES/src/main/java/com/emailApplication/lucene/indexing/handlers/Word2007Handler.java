package com.emailApplication.lucene.indexing.handlers;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import org.apache.lucene.document.DateTools;
import org.apache.poi.POIXMLProperties;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

import com.emailApplication.lucene.model.IndexMessage;
import com.emailApplication.lucene.model.IndexUnit;

public class Word2007Handler extends DocumentHandler {

	public IndexMessage getIndexMessage(File file) {
		IndexMessage retVal = new IndexMessage();

		try {
			XWPFDocument wordDoc = new XWPFDocument(new FileInputStream(file));
			XWPFWordExtractor we = new XWPFWordExtractor(wordDoc);

			String text = we.getText();
			retVal.setAttachment_content(text);

			//POIXMLProperties props = wordDoc.getProperties();

//			String title = props.getCoreProperties().getTitle();
//			//retVal.setTitle(title);
//
//			String keywords = props.getCoreProperties()
//					.getUnderlyingProperties().getKeywordsProperty().getValue();
//			if(keywords != null){
//				String[] splittedKeywords = keywords.split(" ");
//				retVal.setKeywords(new ArrayList<String>(Arrays.asList(splittedKeywords)));
//			}
			retVal.setPath(file.getCanonicalPath());
			
			//String modificationDate=DateTools.dateToString(new Date(file.lastModified()),DateTools.Resolution.DAY);
			//retVal.setFiledate(modificationDate);
			
			we.close();

		} catch (Exception e) {
			System.out.println("Problem pri parsiranju docx fajla");
		}

		return retVal;
	}

	@Override
	public String getText(File file) {
		String text = null;
		try {
			XWPFDocument wordDoc = new XWPFDocument(new FileInputStream(file));
			XWPFWordExtractor we = new XWPFWordExtractor(wordDoc);
			text = we.getText();
			we.close();
		}catch (Exception e) {
			System.out.println("Problem pri parsiranju docx fajla");
		}
		return text;
	}

}
