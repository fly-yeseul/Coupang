package dev.fly_yeseul.coupang.vos.temp;

import dev.fly_yeseul.coupang.entities.temp.UploadEntity;
import dev.fly_yeseul.coupang.enums.temp.UploadResult;

public class UploadVo extends UploadEntity {

    private UploadResult result;

    public UploadResult getResult() {
        return result;
    }

    public void setResult(UploadResult result) {
        this.result = result;
    }
}
