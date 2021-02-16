package com.emailApplication.lucene.indexing.handlers;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import org.apache.lucene.document.DateTools;
import org.apache.pdfbox.io.RandomAccessFile;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentInformation;
import org.apache.pdfbox.text.PDFTextStripper;

import com.emailApplication.lucene.model.IndexMessage;
import com.emailApplication.lucene.model.IndexUnit;

public class PDFHandler extends DocumentHandler {

	@Override
	public IndexMessage getIndexMessage(File file) {
		IndexMessage retVal = new IndexMessage();
		try {
			PDFParser parser = new PDFParser(new RandomAccessFile(file, "r"));
			parser.parse();
			String text = getText(parser);
			retVal.setAttachment_content(text);

			// metadata extraction
			//PDDocument pdf = parser.getPDDocument();
			//PDDocumentInformation info = pdf.getDocumentInformation();

			//String title = ""+info.getTitle();
			//retVal.setS(title);

			//String keywords = ""+info.getKeywords();
//			if(keywords != null){
//				String[] splittedKeywords = keywords.split(" ");
//				retVal.setKeywords(new ArrayList<String>(Arrays.asList(splittedKeywords)));
//			}
			
			retVal.setPath(file.getCanonicalPath());
			
			//String modificationDate=DateTools.dateToString(new Date(file.lastModified()),DateTools.Resolution.DAY);
			//retVal.setFiledate(modificationDate);
			
			//pdf.close();
		} catch (IOException e) {
			System.out.println("Greksa pri konvertovanju dokumenta u pdf");
		}

		return retVal;
	}

	@Override
	public String getText(File file) {
		try {
			PDFParser parser = new PDFParser(new RandomAccessFile(file, "r"));
			parser.parse();
			PDFTextStripper textStripper = new PDFTextStripper();
			String text = textStripper.getText(parser.getPDDocument());
			return text;
		} catch (IOException e) {
			System.out.println("Greksa pri konvertovanju dokumenta u pdf");
		}
		return null;
	}
	
	public String getText(PDFParser parser) {
		try {
			PDFTextStripper textStripper = new PDFTextStripper();
			String text = textStripper.getText(parser.getPDDocument());
			return text;
		} catch (IOException e) {
			System.out.println("Greksa pri konvertovanju dokumenta u pdf");
		}
		return null;
	}

}
