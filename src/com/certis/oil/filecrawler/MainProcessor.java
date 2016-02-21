package com.certis.oil.filecrawler;

import java.io.IOException;
import java.util.Map;

import com.certis.oil.filecrawler.fileio.ConfigurationReader;
import com.certis.oil.filecrawler.windows.FileCSVScannerWindows;
import com.certis.oil.filecrawler.windows.FileCrawlerWindows;

public class MainProcessor {
	
	private static ConfigurationReader cr = new ConfigurationReader();
	private static Map<String, String> extMap = null;
	private static Map<String, String> tagMap = null;
	private static Map<String, String> titleMap = null;
	private static Map<String, String> wellNameMap = null;
	
	public static void loadExtensionRules(String fileName) throws IOException {
		extMap = cr.readExtensionRules(fileName);
	}
	
	public static void loadTagList(String fileName) throws IOException {
		tagMap = cr.readTags(fileName);
	}
	
	public static void loadTitleList(String fileName) throws IOException {
		titleMap = cr.readTitles(fileName);
	}
	
	public static void loadWellNames(String fileName) throws IOException {
		wellNameMap = cr.readWellNames(fileName);
	}
	
	public static Map<String, String> getExtMap() {
		return extMap;
	}

	public static Map<String, String> getTagMap() {
		return tagMap;
	}

	public static Map<String, String> getTitleMap() {
		return titleMap;
	}

	public static Map<String, String> getWellNameMap() {
		return wellNameMap;
	}
	
	public static void main(String[] args) {
		if(args.length != 1) {
			System.out.println("Give type: scanner or crawler");
			return;
		}
		if(args[0].equalsIgnoreCase("scanner")) {
			FileCSVScannerWindows.launchApp(args);
		} else if(args[0].equalsIgnoreCase("crawler")) {
			FileCrawlerWindows.launchApp(args);
		} else {
			System.err.println("Invalid parameter: "+args[0]);
		}
	}

}
