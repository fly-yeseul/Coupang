package dev.fly_yeseul.coupang.entities.product;

import java.util.Date;

public class StockEntity {
    private long index;
    private Date createdAt;
    private int productIndex;
    private int count;

    public StockEntity() {
    }

    public StockEntity(long index, Date createdAt, int productIndex, int count) {
        this.index = index;
        this.createdAt = createdAt;
        this.productIndex = productIndex;
        this.count = count;
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
}
