package dev.fly_yeseul.coupang.vos.product;

import dev.fly_yeseul.coupang.entities.product.ProductEntity;
import dev.fly_yeseul.coupang.enums.product.DetailResult;

public class DetailVo extends ProductEntity {
    private DetailResult result;

    public DetailResult getResult() {
        return result;
    }

    public void setResult(DetailResult result) {
        this.result = result;
    }
}
