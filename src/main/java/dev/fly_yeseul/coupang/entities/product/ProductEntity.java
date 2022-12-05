package dev.fly_yeseul.coupang.entities.product;

public class ProductEntity {
    private int index;
    private String title;
    private int price;
    private String content;
    private String deliveryValue;
    private String thumbnailId;

    public ProductEntity() {
    }

    public ProductEntity(int index, String title, int price, String content, String deliveryValue, String thumbnailId) {
        this.index = index;
        this.title = title;
        this.price = price;
        this.content = content;
        this.deliveryValue = deliveryValue;
        this.thumbnailId = thumbnailId;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDeliveryValue() {
        return deliveryValue;
    }

    public void setDeliveryValue(String deliveryValue) {
        this.deliveryValue = deliveryValue;
    }

    public String getThumbnailId() {
        return thumbnailId;
    }

    public void setThumbnailId(String thumbnailId) {
        this.thumbnailId = thumbnailId;
    }
}
