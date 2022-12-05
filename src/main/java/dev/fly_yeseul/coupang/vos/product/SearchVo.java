package dev.fly_yeseul.coupang.vos.product;

import dev.fly_yeseul.coupang.entities.product.ProductEntity;
import dev.fly_yeseul.coupang.enums.product.SearchResult;

public class SearchVo extends ProductEntity {

    private SearchResult result;

    public SearchResult getResult() {
        return result;
    }

    public void setResult(SearchResult result) {
        this.result = result;
    }
}
