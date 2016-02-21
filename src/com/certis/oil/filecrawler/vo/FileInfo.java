package com.certis.oil.filecrawler.vo;

public class FileInfo {

	private String id = "";
	private String filePath = "";
	private String fileName = "";
	private String fileExtension = "";
	private long fileSize = -1;
	private String documentType = "";
	private String apiNumber = "";
	private String wellName = "";
	private String date = "";
	private String comments = "";
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFileExtension() {
		return fileExtension;
	}

	public void setFileExtension(String fileExtension) {
		this.fileExtension = fileExtension;
	}

	public long getFileSize() {
		return fileSize;
	}

	public void setFileSize(long fileSize) {
		this.fileSize = fileSize;
	}

	public String getDocumentType() {
		return documentType;
	}

	public void setDocumentType(String documentType) {
		this.documentType = documentType;
	}

	public String getApiNumber() {
		return apiNumber;
	}

	public void setApiNumber(String apiNumber) {
		this.apiNumber = apiNumber;
	}

	public String getWellName() {
		return wellName;
	}

	public void setWellName(String wellName) {
		this.wellName = wellName;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public void fromCSVRow(String row) {
		int cut = 0;
		int endCut = 0;
		int count = 0;
		String cell = "";
		while(cut>=0) {
			cut = row.indexOf("\"", cut);
			if(cut>=0) {
				endCut = row.indexOf("\"", cut+1);
				cell = row.substring(cut+1, endCut).trim();
				cut = endCut+1;
			} else {
				continue;
			}
			switch(count) {
			case 0:
				filePath = cell;
				break;
			case 1:
				fileName = cell;
				int extCut = fileName.lastIndexOf(".");
				if(extCut>0) {
					fileExtension = fileName.substring(extCut+1).toUpperCase();
				}
				break;
			case 2:
				if(!"".equals(cell)) {
					try {
						fileSize = Long.parseLong(cell);											
					} catch(NumberFormatException e)  {
						e.printStackTrace();
					}
				}
				break;
			}
			count++;
		}
	}
	
	public String toCSVRow() {
		return "\""+id+"\",\""+filePath+"\",\""+fileName+"\",\""+fileSize+"\",\""+fileExtension+"\",\""+
				documentType+"\",\""+apiNumber+"\",\""+wellName+"\",\""+date+"\",\""+comments+"\"";
	}
	
	@Override
	public String toString() {
		return "FileInfo [id=" + id + ", filePath=" + filePath + ", fileName=" + fileName + ", fileExtension="
				+ fileExtension + ", fileSize=" + fileSize + ", documentType=" + documentType + ", apiNumber="
				+ apiNumber + ", wellName=" + wellName + ", date=" + date + ", comments=" + comments + "]";
	}
	
}
