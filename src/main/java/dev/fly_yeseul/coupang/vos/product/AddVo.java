package dev.fly_yeseul.coupang.vos.product;

import dev.fly_yeseul.coupang.entities.product.ProductEntity;
import dev.fly_yeseul.coupang.enums.product.AddResult;

public class AddVo extends ProductEntity {
    private AddResult result;



    public AddResult getResult() {
        return result;
    }

    public void setResult(AddResult result) {
        this.result = result;
    }
}
