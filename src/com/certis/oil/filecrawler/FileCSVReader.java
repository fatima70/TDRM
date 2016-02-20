package com.certis.oil.filecrawler;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import com.certis.oil.filecrawler.vo.FileInfo;

public class FileCSVReader {

	private BufferedReader reader = null;
	
	public void openFile(String fileName) throws FileNotFoundException {
		reader = new BufferedReader(new FileReader(new File(fileName)));
	}
	
	public FileInfo readNext() {
		FileInfo fi = new FileInfo();
	
		return fi;
	}
	
	
	public void closeFile() throws IOException {
		if(reader != null) {
			reader.close();
			reader = null;
		}
	}
}
