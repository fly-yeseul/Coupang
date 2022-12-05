package dev.fly_yeseul.coupang.entities.shopping;

public class CartEntity {
    private long index;
    private String userEmail;
    private int productIndex;
    private int count;

    public CartEntity() {
    }

    public CartEntity(long index, String userEmail, int productIndex, int count) {
        this.index = index;
        this.userEmail = userEmail;
        this.productIndex = productIndex;
        this.count = count;
    }

    public long getIndex() {
        return index;
    }

    public void setIndex(long index) {
        this.index = index;
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
}
