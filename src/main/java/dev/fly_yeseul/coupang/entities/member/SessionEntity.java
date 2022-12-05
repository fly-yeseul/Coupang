package dev.fly_yeseul.coupang.entities.member;

import java.util.Date;

public class SessionEntity {
    private Date createdAt;
    private Date updatedAt;
    private Date expiresAt;         // 공공기관&은행 등 페이지에서 어떤 링크로의 이동이 없으면 로그아웃시키는 기능이 여기서 나오는 것
    private boolean isExpired;
    private String userEmail;
    private String key;
    private String ua;

    public SessionEntity() {
    }

    public SessionEntity(Date createdAt, Date updatedAt, Date expiresAt, boolean isExpired, String userEmail, String key, String ua) {
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.expiresAt = expiresAt;
        this.isExpired = isExpired;
        this.userEmail = userEmail;
        this.key = key;
        this.ua = ua;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Date getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(Date expiresAt) {
        this.expiresAt = expiresAt;
    }

    public boolean isExpired() {
        return isExpired;
    }

    public void setExpired(boolean expired) {
        isExpired = expired;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getUa() {
        return ua;
    }

    public void setUa(String ua) {
        this.ua = ua;
    }
}
