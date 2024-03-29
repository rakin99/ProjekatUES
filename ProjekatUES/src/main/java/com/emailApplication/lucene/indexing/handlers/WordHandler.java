package com.emailApplication.lucene.indexing.handlers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import org.apache.lucene.document.DateTools;
import org.apache.poi.hpsf.SummaryInformation;
import org.apache.poi.hwpf.extractor.WordExtractor;

import com.emailApplication.lucene.model.IndexMessage;
import com.emailApplication.lucene.model.IndexUnit;

public class WordHandler extends DocumentHandler {

	public IndexMessage getIndexMessage(File file) {
		IndexMessage retVal = new IndexMessage();
		InputStream is;

		try {
			is = new FileInputStream(file);
			// pomocu WordExtractor objekta izvuci tekst
			WordExtractor we = new WordExtractor(is);
			String text = we.getText();
			retVal.setAttachment_content(text);
			
			// pomocu SummaryInformation objekta izvuci ostale metapodatke
//			SummaryInformation si = we.getSummaryInformation();
//			String title = si.getTitle();
			//retVal.setTitle(title);

//			String keywords = si.getKeywords();
//			if(keywords != null){
//				String[] splittedKeywords = keywords.split(" ");
//				retVal.setKeywords(new ArrayList<String>(Arrays.asList(splittedKeywords)));
//			}
			
			retVal.setPath(file.getCanonicalPath());
			
			//String modificationDate=DateTools.dateToString(new Date(file.lastModified()),DateTools.Resolution.DAY);
			//retVal.setFiledate(modificationDate);
			
			we.close();
		} catch (FileNotFoundException e1) {
			System.out.println("Dokument ne postoji");
		} catch (Exception e) {
			System.out.println("Problem pri parsiranju doc fajla");
		}

		return retVal;
	}

	@Override
	public String getText(File file) {
		String text = null;
		try {
			WordExtractor we = new WordExtractor(new FileInputStream(file));
			text = we.getText();
			we.close();
		} catch (FileNotFoundException e1) {
			System.out.println("Dokument ne postoji");
		} catch (Exception e) {
			System.out.println("Problem pri parsiranju doc fajla");
		}
		return text;
	}

}
