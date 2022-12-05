package dev.fly_yeseul.coupang.vos.member.user;

import dev.fly_yeseul.coupang.entities.member.user.UserEntity;
import dev.fly_yeseul.coupang.enums.member.user.RegisterResult;

public class RegisterVo extends UserEntity {
    private RegisterResult result;

    public RegisterResult getResult() {
        return result;
    }

    public void setResult(RegisterResult result) {
        this.result = result;
    }
}
