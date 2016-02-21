package com.certis.oil.filecrawler.fileio;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;

import com.certis.oil.filecrawler.vo.FileInfo;

/**
 * File writer class for CSV format.
 * 
 * @author timppa
 *
 */
public class FileCSVWriter {

	private BufferedWriter writer = null;
	
	public void openFile(String fileName) throws IOException {
		writer = new BufferedWriter(new java.io.FileWriter(new File(fileName)));
	}
	
	public void writeRow(FileInfo fi) throws IOException {
		writer.write(fi.toCSVRow());
		writer.write("\r\n");
		writer.flush();
	}
	
	public void closeFile() throws IOException {
		if(writer != null) {
			writer.close();
			writer = null;
		}
	}
}
