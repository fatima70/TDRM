package com.certis.oil.filecrawler;

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

	public Map<String, Boolean> readExtensionRules(String fileName) {
		return null;
	}

	public Map<String, String> readTags(String fileName) throws IOException {
		Map<String, String> tagToCategoryMap = new HashMap<>();
		List<List<String>> tagList = readXLSXFile(fileName);
		boolean first = true;
		for(List<String> cellList : tagList) {
			if(first) {//skip header row.
				first = false;
				continue;
			}
			String category = cellList.get(0).trim();
			String[] clues = cellList.get(1).split(",");			
			for(String clue : clues) {
				clue = clue.toUpperCase().trim();
				if("".equals(clue)) {
					continue;
				}
				log.info(category+":"+clue);
				tagToCategoryMap.put(category,clue);
			}
		}		
		return tagToCategoryMap;
	}

	public Map<String, Boolean> readTitles(String fileName) {
		return null;
	}

	
	public Map<String, String> readWellNames(String fileName) throws IOException {
		Map<String, String> wellMap = new HashMap<>();
		List<List<String>> wellList = readXLSXFile(fileName);
		int count = 1;
		for(List<String> cellList : wellList) {
			String wellName = cellList.get(0).trim();
			String wellNumber = cellList.get(1).trim();
			log.info(count+"#: "+wellName+"="+wellNumber);
			wellMap.put(wellName, wellNumber);
			count++;
		}
		return wellMap;
	}
	
	public List<List<String>> readXLSXFile(String fileName) throws IOException {
		File excelFile = new File(fileName);
		FileInputStream fis = new FileInputStream(excelFile);
		XSSFWorkbook myWorkBook = new XSSFWorkbook (fis);
		XSSFSheet mySheet = myWorkBook.getSheetAt(0);
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
