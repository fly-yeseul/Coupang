package dev.fly_yeseul.coupang.entities.product;

import java.util.Date;

public class ImageEntity {
    private String id;
    private Date createdAt;
    private String fileName;
    private String fileType;
    private byte[] data;


    public ImageEntity(String id, Date createdAt, String fileName, String fileType, byte[] data) {
        this.id = id;
        this.createdAt = createdAt;
        this.fileName = fileName;
        this.fileType = fileType;
        this.data = data;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
