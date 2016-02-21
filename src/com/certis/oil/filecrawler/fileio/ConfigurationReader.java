package com.certis.oil.filecrawler.fileio;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


public class ConfigurationReader {

	private Logger log = Logger.getLogger(this.getClass());

	/**
	 * Read file extension to document type map from configuration Excel sheet.
	 * 
	 * @param fileName
	 * @return
	 * @throws IOException
	 */
	public Map<String, String> readExtensionRules(String fileName) throws IOException {
		Map<String, String> extMap = new HashMap<>();
		List<List<String>> tagList = readXLSXFile(fileName, "File extensions");
		boolean first = true;
		for(List<String> cellList : tagList) {
			if(first) {//skip header row.
				first = false;
				continue;
			}
			String ext = cellList.get(0).trim().toUpperCase();
			String documentType= cellList.get(1).trim();			
			extMap.put(ext, documentType);
			log.info(ext+": "+documentType);
		}		
		return extMap;
	}

	/**
	 * Read file name tags to document type map.
	 * 
	 * @param fileName
	 * @return
	 * @throws IOException
	 */
	public Map<String, String> readTags(String fileName) throws IOException {
		Map<String, String> tagMap = new HashMap<>();
		List<List<String>> tagList = readXLSXFile(fileName, "Tag list");
		boolean first = true;
		for(List<String> cellList : tagList) {
			if(first) {//skip header row.
				first = false;
				continue;
			}
			String tag = cellList.get(0).trim().toUpperCase();
			String documentType= cellList.get(1).trim();			
			tagMap.put(tag, documentType);
			log.info(tag+": "+documentType);
		}		
		return tagMap;
	}

	public  Map<String, String> readTitles(String fileName) {
		return null;
	}
	
	/**
	 * Read full well names from Excel sheet.
	 * 
	 * @param fileName
	 * @return
	 * @throws IOException
	 */
	public Map<String, String> readWellNames(String fileName) throws IOException {
		Map<String, String> wellMap = new HashMap<>();
		List<List<String>> wellList = readXLSXFile(fileName, "Well names");
		int count = 1;
		for(List<String> cellList : wellList) {
			if(count == 1) {
				count++;
				continue;
			}
			String wellName = cellList.get(0).trim().toUpperCase();
			log.info(count+"#: "+wellName);
			wellMap.put(wellName, "1");
			count++;
		}
		return wellMap;
	}
	
	/**
	 * Read fully given Excel file sheet from a XSLS file.
	 * @param fileName
	 * @param sheetName
	 * @return
	 * @throws IOException
	 */
	public List<List<String>> readXLSXFile(String fileName, String sheetName) throws IOException {
		File excelFile = new File(fileName);
		FileInputStream fis = new FileInputStream(excelFile);
		XSSFWorkbook myWorkBook = new XSSFWorkbook (fis);
		XSSFSheet mySheet = myWorkBook.getSheet(sheetName);
		Iterator<Row> rowIterator = mySheet.iterator();
        // Traversing over each row of XLSX file
		List<List<String>> rowList = new ArrayList<>();
        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();
    		List<String> cellList = new ArrayList<>();    		
            Iterator<Cell> cellIterator = row.cellIterator();
            while (cellIterator.hasNext()) {
                Cell cell = cellIterator.next();
                switch (cell.getCellType()) {
                case Cell.CELL_TYPE_STRING:
                    cellList.add(cell.getStringCellValue());
                    break;
                case Cell.CELL_TYPE_NUMERIC:
                    cellList.add(""+cell.getNumericCellValue());
                    break;
                case Cell.CELL_TYPE_BOOLEAN:
                    cellList.add(""+cell.getBooleanCellValue());
                    break;
                default :
                	log.info("Unknown cell format: "+cell.getCellType());
                    cellList.add(cell.getStringCellValue());
                	break;
                }
            }
            rowList.add(cellList);
        }
        myWorkBook.close();
        return rowList;
	}
}
