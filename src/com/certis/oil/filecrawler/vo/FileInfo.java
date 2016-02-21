package com.certis.oil.filecrawler.vo;

public class FileInfo {

	private String id;
	private String filePath;
	private String fileName;
	private String fileExtension;
	private long fileSize;
	private String documentType;
	private String apiNumber;
	private String wellName;
	private String date;
	private String comments;
	
	
	public String toCSVRow() {
		return id+";"+filePath+";"+fileName+";"+fileSize+";"+fileExtension+";"+
				documentType+";"+apiNumber+";"+wellName+";"+date+";"+comments;
	}
	
	@Override
	public String toString() {
		return "FileInfo [id=" + id + ", filePath=" + filePath + ", fileName=" + fileName + ", fileExtension="
				+ fileExtension + ", fileSize=" + fileSize + ", documentType=" + documentType + ", apiNumber="
				+ apiNumber + ", wellName=" + wellName + ", date=" + date + ", comments=" + comments + "]";
	}
	
}
