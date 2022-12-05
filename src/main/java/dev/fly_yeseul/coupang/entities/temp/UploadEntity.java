package dev.fly_yeseul.coupang.entities.temp;

import java.util.Date;

public class UploadEntity {
    private int index;
    private Date createdAt;
    private String fileName;
    private String fileType;
    private byte[] data;

    public UploadEntity() {
    }

    public UploadEntity(int index, Date createdAt, String fileName, String fileType, byte[] data) {
        this.index = index;
        this.createdAt = createdAt;
        this.fileName = fileName;
        this.fileType = fileType;
        this.data = data;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }
}
