package dev.fly_yeseul.coupang.vos.member.user;

import dev.fly_yeseul.coupang.entities.member.SessionEntity;
import dev.fly_yeseul.coupang.entities.member.user.UserEntity;
import dev.fly_yeseul.coupang.enums.member.user.LoginResult;

public class LoginVo extends UserEntity {
    private LoginResult result;
    private boolean autosign;
    private SessionEntity sessionEntity;


    public SessionEntity getSessionEntity() {
        return sessionEntity;
    }

    public void setSessionEntity(SessionEntity sessionEntity) {
        this.sessionEntity = sessionEntity;
    }

    public LoginResult getResult() {
        return result;
    }

    public void setResult(LoginResult result) {
        this.result = result;
    }

    public boolean isAutosign() {
        return autosign;
    }

    public void setAutosign(boolean autosign) {
        this.autosign = autosign;
    }
}
