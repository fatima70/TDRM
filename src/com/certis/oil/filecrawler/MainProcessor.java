package com.certis.oil.filecrawler;

public class MainProcessor {

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
