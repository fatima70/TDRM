package com.certis.oil.filecrawler.windows;

public interface CallBack {

	public void errorMessage(String errorMsg);
	
	public void progress(String message, double percentage);
	
	public void summary(String summaryMsg[]);
}
