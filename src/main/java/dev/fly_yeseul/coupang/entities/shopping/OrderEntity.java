package dev.fly_yeseul.coupang.entities.shopping;

import java.util.Date;

public class OrderEntity {
    private long index;
    private Date createdAt;
    private String userEmail;
    private int productIndex;
    private int count;
    private int orderStatusIndex;
    private int priceProduct;
    private int priceShipping;


    public OrderEntity() {
    }

    public OrderEntity(long index, Date createdAt, String userEmail, int productIndex, int count, int orderStatusIndex, int priceProduct, int priceShipping) {
        this.index = index;
        this.createdAt = createdAt;
        this.userEmail = userEmail;
        this.productIndex = productIndex;
        this.count = count;
        this.orderStatusIndex = orderStatusIndex;
        this.priceProduct = priceProduct;
        this.priceShipping = priceShipping;
    }

    public long getIndex() {
        return index;
    }

    public void setIndex(long index) {
        this.index = index;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public int getProductIndex() {
        return productIndex;
    }

    public void setProductIndex(int productIndex) {
        this.productIndex = productIndex;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getOrderStatusIndex() {
        return orderStatusIndex;
    }

    public void setOrderStatusIndex(int orderStatusIndex) {
        this.orderStatusIndex = orderStatusIndex;
    }

    public int getPriceProduct() {
        return priceProduct;
    }

    public void setPriceProduct(int priceProduct) {
        this.priceProduct = priceProduct;
    }

    public int getPriceShipping() {
        return priceShipping;
    }

    public void setPriceShipping(int priceShipping) {
        this.priceShipping = priceShipping;
    }
}
