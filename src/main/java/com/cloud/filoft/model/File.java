package com.cloud.filoft.model;

import javax.persistence.Entity;

import java.sql.Timestamp;

@Entity
public class File {
	
	private String firstname;
	private String lastname;
	private String emailid;
	private String filename;
	private String description;
	private String fileSize;
	private Timestamp createdtime;
	private Timestamp updatedtime;
	private String fileurl;
	
	public String getEmailId() {
		return emailid;
	}
	public void setEmailId(String emailid) {
		this.emailid = emailid;
	}
	public String getFileName() {
        return filename;
    }
	public void setFileName(String filename) {
        this.filename = filename;
    }
	
	public String getDescription() {
        return description;
    }
	public void setDescription(String description) {
        this.description = description;
    }
	
	public String getFileSize() {
        return fileSize;
    }
	public void setFileSize(String fileSize) {
        this.fileSize = fileSize;
    }
	
	public Timestamp getCreatedTime() {
        return createdtime;
    }
	public void setCreatedTime(Timestamp createdtime) {
        this.createdtime = createdtime;
    }
	
	public Timestamp getUpdatedTime() {
        return updatedtime;
    }
	public void setUpdatedTime(Timestamp updatedtime) {
        this.updatedtime = updatedtime;
    }
	
	public String getFileUrl() {
		return fileurl;
	}
	
	public void setFileUrl(String fileurl) {
		this.fileurl = fileurl;
	}
	

}
