package dev.fly_yeseul.coupang.vos.shopping.cart;

import dev.fly_yeseul.coupang.entities.shopping.CartEntity;
import dev.fly_yeseul.coupang.enums.shopping.cart.AddResult;

public class AddVo extends CartEntity {
    private AddResult result;


    public AddResult getResult() {
        return result;
    }

    public void setResult(AddResult result) {
        this.result = result;
    }
}
