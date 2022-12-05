package dev.fly_yeseul.coupang.dtos.product;

import dev.fly_yeseul.coupang.entities.product.ProductEntity;

public class ProductDto extends ProductEntity {
    private int count;
    private int reviewCount;
    private double reviewRate;



    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getReviewCount() {
        return reviewCount;
    }

    public void setReviewCount(int reviewCount) {
        this.reviewCount = reviewCount;
    }

    public double getReviewRate() {
        return reviewRate;
    }

    public void setReviewRate(double reviewRate) {
        this.reviewRate = reviewRate;
    }
}
