package com.certis.oil.filecrawler.fileio;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import com.certis.oil.filecrawler.vo.FileInfo;

/**
 * CSV file reader. Reads a row into FileInfo object.
 * 
 * @author timppa
 *
 */
public class FileCSVReader {
	
	private String fileName = null;
	private BufferedReader reader = null;
	
	public void openFile(String fileName) throws FileNotFoundException {
		this.fileName = fileName;
		reader = new BufferedReader(new FileReader(new File(fileName)));
	}
	
	public void skipRow() throws IOException {
		reader.readLine();
	}
	
	public FileInfo readNext() throws IOException {
		FileInfo fi = null;
		String row = reader.readLine();
		if(row == null){
			return fi;
		}
		fi = new FileInfo();
		fi.fromCSVRow(row);
		return fi;
	}

	/**
	 * Count the number of rows in the file and reopen the file from the start.
	 * 
	 * @return
	 * @throws IOException
	 */
	public int countRowsAndReset() throws IOException {
		int count = 0;
		while(reader.readLine() != null) {			
			count++;
		}
		reader.close();
		openFile(fileName);
		return count;
	}
	
	public void closeFile() throws IOException {
		if(reader != null) {
			reader.close();
			reader = null;
		}
	}
}
