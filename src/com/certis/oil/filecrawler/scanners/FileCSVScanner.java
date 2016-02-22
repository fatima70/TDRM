package com.certis.oil.filecrawler.scanners;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import com.certis.oil.filecrawler.MainProcessor;
import com.certis.oil.filecrawler.fileio.FileCSVReader;
import com.certis.oil.filecrawler.fileio.FileCSVWriter;
import com.certis.oil.filecrawler.vo.FileInfo;
import com.certis.oil.filecrawler.windows.CallBack;

public class FileCSVScanner implements Runnable {

	private Logger log = Logger.getLogger(this.getClass());
	private boolean running = false;
	private Thread runner = null;
	private String csvFileName = null;
	private CallBack callback;
	private Set<String> wellNames = null;
	private String extRulesFileName;
	private String tagsFileName;
	private String wellsFileName;
	private String csvOutputFileName;

	public FileCSVScanner(String csvFileName, CallBack callback, String extRulesFileName,
			String tagsFileName, String wellsFileName, String csvOutputFileName) {
		this.csvFileName = csvFileName;
		this.extRulesFileName = extRulesFileName;
		this.tagsFileName = tagsFileName;
		this.wellsFileName = wellsFileName;
		this.callback = callback;
		this.csvOutputFileName = csvOutputFileName;
	}

	public void start() {
		if (running) {
			log.error("Already running");
			return;
		}
		running = true;
		runner = new Thread(this);
		runner.start();
	}

	public void stop() {
		running = false;
	}
	
	public boolean isRunning() {
		return running;
	}
	/**
	 * Main program logic for CSV file scanning.
	 * 
	 */
	@Override
	public void run() {
		FileCSVReader fcr = new FileCSVReader();
		FileCSVWriter fcw = new FileCSVWriter();
		try {
			callback.progress("Loading extension rules.", -1d);
			MainProcessor.loadExtensionRules(extRulesFileName);
			callback.progress("Loading tag list.", -1d);
			MainProcessor.loadTagList(tagsFileName);
			callback.progress("Loading well names.", -1d);
			MainProcessor.loadWellNames(wellsFileName);
			fcr.openFile(csvFileName);			
			fcw.openFile(csvOutputFileName);
		} catch (Exception e) {
			running = false;
			e.printStackTrace();
			callback.errorMessage(e.getMessage());
			return;
		}
		int id = 0;
		try {
			FileInfo fi = null;
			fcr.skipRow();
			fcr.skipRow();
			// int rowCount = fcr.countRowsAndReset();
			// log.info("Rows in the CSV file: "+rowCount);
			// Skipping first two rows.
			
			log.info("----------------------------------------------------------------");
			log.info("Input file name: " + csvFileName);
			log.info("Output file name: " + csvOutputFileName);
			String msg = null;
			callback.progress("Started processing CSV file.", -1d);
			while ((fi = fcr.readNext()) != null && running) {
				fi.setId("" + id);
				scanForDocumentType(id, fi);
				scanForAPINumber(fi);
				scanForDate(fi);
				scanForWellName(fi);
				//log.info(fi.toString());
				fcw.writeRow(fi);
				id++;
				if(id%250==0) {
					msg = "Processed "+id+" rows.";
					log.info(msg);
					callback.progress(msg, -1d);
				}
			}
			fcr.closeFile();
			fcw.closeFile();
		} catch (Exception e) {
			running = false;
			callback.errorMessage(e.getMessage());
			e.printStackTrace();
			return;
		}
		if(running) {
			callback.progress("Completed OK, processed: "+id+" rows.", 100);				
		} else {
			callback.progress("Stopped, processed: "+id+" rows.", 100);
		}
		running = false;
	}

	/**
	 * Scan for document type in file extension, file name or file path.
	 * 
	 * @param id
	 * @param fi
	 */
	private void scanForDocumentType(int id, FileInfo fi) {
		String extension = fi.getFileExtension();
		Map<String, String> map = MainProcessor.getExtMap();
		String docType = map.get(extension);
		if (docType != null) {
			//log.info("Found document type from extension: " + extension + "=" + docType);
			fi.setDocumentType(docType);
			return;
		}

		String fileName = fi.getFileName().toUpperCase();
		map = MainProcessor.getTagMap();
		Set<String> keySet = map.keySet();
		for (String key : keySet) {
			String test = key.toUpperCase();
			if (fileName.contains(test)) {
				docType = key;
				fi.setDocumentType(docType);
				//log.info("Found document type from file name: " + fileName + "=" + docType);
				return;
			}
		}
		
		String filePath = fi.getFilePath().toUpperCase().replace("MICROSOFT.POWERSHELL.CORE", "");
		keySet = map.keySet();
		for (String key : keySet) {
			String test = key.toUpperCase();
			if (filePath.contains(test)) {
				docType = map.get(key);
				fi.setDocumentType(docType);
				//log.info("Found document type from file path: " + filePath + "=" + docType);
				return;
			}
		}
		//log.info("No document type found from file extension, name or path.");
		fi.setDocumentType("");
		return;
	}

	/**
	 * Scan API number either from file name or path.
	 * 
	 * @param fi
	 */
	private void scanForAPINumber(FileInfo fi) {
		String data = fi.getFileName();
		String apiNumber = getAPINumber(data);
		if (apiNumber != null) {
			//log.info("Found API number from file name:" + apiNumber);
			fi.setApiNumber(apiNumber);
		} else {
			data = fi.getFilePath();
			apiNumber = getAPINumber(data);
			if (apiNumber != null) {
				//log.info("Found API number from file path:" + apiNumber);
				fi.setApiNumber(apiNumber);
			} else {
				fi.setApiNumber("");
				//log.info("No api number from from file name or path.");
			}
		}
	}

	/**
	 * Scan for date from file name and path.
	 * 
	 * @param fi
	 */
	private void scanForDate(FileInfo fi) {
		String data = fi.getFileName();
		String date = getDate(data).trim();
		if (date != null && !"".equals(date)) {
			//log.info("Found date from file name:" + date);
			fi.setDate(date);
			return;
		} else {
			data = fi.getFilePath();
			int cut = data.lastIndexOf("\\");
			if (cut < 0) {
				cut = data.lastIndexOf("/");
			}
			if (cut > 0) {
				data = data.substring(cut);
				date = getDate(data).trim();
				if (date != null && !"".equals(date)) {
					//log.info("Found date from parent directory:" + date);
					fi.setDate(date);
					return;
				}
			}
		}
		fi.setDate("");
		//log.info("No date found from file name or path.");
	}

	/**
	 * Get api number which is in specific number format.
	 * 
	 * @param data
	 * @return
	 */
	public String getAPINumber(String data) {
		int count = 0;
		Matcher m = Pattern.compile("([0-9-]+){10,20}").matcher(data);
		String api = "";
		while (m.find()) {
			api = api + m.group();
		}
		Matcher m1 = Pattern.compile("\\d").matcher(api);
		while (m1.find()) {
			count++;
		}
		if (count >= 10) {
			return api;
		}
		return null;
	}

	private String getDate(String data) {

		Matcher m = Pattern.compile("(0[1-9]|[12][0-9]|3[01])[- /.](0[1-9]|1[012])[- /.](19|20)\\d\\d").matcher(data);
		String date = "";
		while (m.find()) {
			date = date + m.group();
		}
		return date;
	}

	private void scanForWellName(FileInfo fi) {
		if(wellNames == null) {
			Map<String, String> wellMap = MainProcessor.getWellNameMap();
			wellNames = wellMap.keySet();
		}
		String fileName = fi.getFileName().toUpperCase();
		String fileName2 = fileName.replace(" #", "#");
		String fileName3 = fileName2.replace("ST.","STATE");
		//log.debug(fileName+","+fileName2+", "+fileName3);
		List<String> wellList = new ArrayList<>();
		for(String well : wellNames) {
			well = well.toUpperCase();
			wellList.add(well);
			if(fileName.contains(well) || 
			   fileName2.contains(well) ||
			   fileName3.contains(well)){
				fi.setWellName(well);
				//log.info("Well name found from file name: "+well);
				return;
			}
		}
		String filePath = fi.getFilePath().toUpperCase();
		String filePath2 = filePath.replace(" #", "#");
		String filePath3 = filePath2.replace("ST.","STATE");
		for(String well : wellList) {
			if(filePath.contains(well) ||
			   filePath2.contains(well) || 
			   filePath3.contains(well) ) {
				fi.setWellName(well);
				//log.info("Well name found from file path: "+well);
				return;
			}			
		}
		//log.info("No well name found from file name or path.");
		fi.setWellName("");
	}
}
