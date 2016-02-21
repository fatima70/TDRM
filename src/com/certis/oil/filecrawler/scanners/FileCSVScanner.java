package com.certis.oil.filecrawler.scanners;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.log4j.Logger;

import com.certis.oil.filecrawler.fileio.FileCSVReader;
import com.certis.oil.filecrawler.fileio.FileCSVWriter;
import com.certis.oil.filecrawler.vo.FileInfo;
import com.certis.oil.filecrawler.windows.CallBack;

public class FileCSVScanner implements Runnable {

	private Logger log = Logger.getLogger(this.getClass());
	private boolean running = false;
	private Thread runner = null;
	private String fileName = null;
	private CallBack callback;
	
	public FileCSVScanner(String fileName, CallBack callback) {
		this.fileName = fileName;
		this.callback = callback;
	}
	
	public void start() {
		if(running) {
			log.error("Already running");
			return;
		}
		running = true;
		runner = new Thread(this);
		runner.start();			
	}
	
	@Override
	public void run() {
		FileCSVReader fcr = new FileCSVReader();
		FileCSVWriter fcw = new FileCSVWriter();
		try {
			fcr.openFile(fileName);
			fcw.openFile(fileName+"_output");
		} catch (IOException e) {
			running = false;
			e.printStackTrace();
			callback.errorMessage(e.getMessage());
			return;
		}
		try {
			FileInfo fi = null;
			fcr.skipRow();
			fcr.skipRow();
			//int rowCount = fcr.countRowsAndReset();
			//log.info("Rows in the CSV file: "+rowCount);
			//Skipping first two rows.
			while((fi=fcr.readNext())!= null) {
				scanForDocumentType(fi);
				log.info(fi.toString());
			}
			
		} catch (IOException e) {
			running = false;
			callback.errorMessage(e.getMessage());
			e.printStackTrace();
		}
		
		running = false;
	}

	private void scanForDocumentType(FileInfo fi) {
		String extension = fi.getFileExtension();		
		String fileName = fi.getFileName();
		String filePath = fi.getFilePath();
		
	}
}
