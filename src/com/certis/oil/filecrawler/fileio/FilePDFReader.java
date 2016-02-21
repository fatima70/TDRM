package com.certis.oil.filecrawler.fileio;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

/**
 * Simple PDF text reader class.
 * 
 * @author timppa
 *
 */
public class FilePDFReader {

	private PDDocument pdfDoc = null;
	
	public void openFile(String fileName) throws IOException {
		pdfDoc = PDDocument.load(new File(fileName));		
	}
	
	public int getNumberOfPages() throws IOException {
		if(pdfDoc == null) {
			throw new IOException("Open PDF file first.");
		}
		return pdfDoc.getNumberOfPages();
	}
	
	public void closeFile() throws IOException {
		if(pdfDoc != null) {
			pdfDoc.close();						
			pdfDoc = null;
		}
	}
	
	/**
	 * Extract specific page as text.
	 * 
	 * @param fileName
	 * @param page
	 * @return
	 * @throws IOException
	 */
	public String getContentAsText(String fileName, int page) throws IOException {
		if(pdfDoc == null) {
			throw new IOException("Open PDF file first.");
		}
		String text = null;
		PDFTextStripper stripper = new PDFTextStripper();
		stripper.setStartPage(page);
        stripper.setEndPage(page);        
        StringWriter writer = new StringWriter();
        stripper.writeText(pdfDoc, writer);
        text = writer.toString();
		return text;
	}
}
