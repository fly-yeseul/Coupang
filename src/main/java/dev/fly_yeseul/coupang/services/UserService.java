package dev.fly_yeseul.coupang.services;

import dev.fly_yeseul.coupang.entities.member.SessionEntity;
import dev.fly_yeseul.coupang.entities.member.user.UserEntity;
import dev.fly_yeseul.coupang.enums.member.user.LoginResult;
import dev.fly_yeseul.coupang.enums.member.user.RegisterResult;
import dev.fly_yeseul.coupang.mappers.IUserMapper;
import dev.fly_yeseul.coupang.utils.CryptoUtil;
import dev.fly_yeseul.coupang.vos.member.user.LoginVo;
import dev.fly_yeseul.coupang.vos.member.user.RegisterVo;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.SearchStrategy;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service(value = "dev.fly_yeseul.coupang.services.UserService")
public class UserService {
    private final IUserMapper userMapper;

    @Autowired
    public UserService(IUserMapper userMapper) {
        this.userMapper = userMapper;
    }


    // 만료
    public void expireSession(SessionEntity sessionEntity) {
        sessionEntity.setExpired(true);
//        System.out.println('1');
        this.userMapper.updateSession(sessionEntity);
//        System.out.println('2');
    }


    // 연장
    public void extendSession(SessionEntity sessionEntity) {
        sessionEntity.setUpdatedAt(new Date());
        sessionEntity.setExpiresAt(DateUtils.addMinutes(sessionEntity.getUpdatedAt(), 60));
        this.userMapper.updateSession(sessionEntity);
    }

    public static boolean checkEmail(String input) {
        return input != null && input.matches("^(?=.{10,50}$)([0-9a-z][0-9a-z_]*[0-9a-z])@([0-9a-z][0-9a-z\\-]*[0-9a-z]\\.)?([0-9a-z][0-9a-z\\-]*[0-9a-z])\\.([a-z]{2,15})(\\.[a-z]{2})?$");
    }

    public static boolean checkPassword(String input) {
        return input != null && input.matches("^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[a-zA-Z]).{8,100}$");
    }


    // 서비스는 오버로딩되기때문에 getUserByEmail 이런식으로 길게 안함
    public SessionEntity getSession(String key) {
        return this.userMapper.selectSessionByKey(key);
    }

    public UserEntity getUser(String email) {
        return this.userMapper.selectUserByEmail(email);
    }

    // 로그인 과정
    // 1. 누가 로그인 요청함
    // 2. 동일한 이메일을 가진 모든 세션 제거하기
    // 3. 세션 키 INSERT
    public void login(LoginVo loginVo, HttpServletRequest request) {
        loginVo.setPassword(CryptoUtil.hashSha512(loginVo.getPassword()));
        UserEntity userEntity = this.userMapper.selectUser(loginVo);
        if (userEntity == null) {
            loginVo.setResult(LoginResult.FAILURE);
            return;
        }
        loginVo.setEmail(userEntity.getEmail());
        loginVo.setPassword(userEntity.getPassword());
        loginVo.setName(userEntity.getName());
        loginVo.setContact(userEntity.getContact());
        loginVo.setAdmin(userEntity.isAdmin());


        // 세션 만료처리
        this.userMapper.updateSessionExpiredByEmail(loginVo.getEmail());

        // 세션처리 - Redis
        String sessionKey = String.format("%s%s%s%f%f",
                new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date()),
                loginVo.getEmail(),
                loginVo.getPassword(),
                Math.random(),
                Math.random());

        String userAgent = request.getHeader("User-Agent");
        sessionKey = CryptoUtil.hashSha512(sessionKey);
        userAgent = CryptoUtil.hashSha512(userAgent);
        SessionEntity sessionEntity = new SessionEntity();
        sessionEntity.setCreatedAt(new Date());
        sessionEntity.setUpdatedAt(sessionEntity.getCreatedAt());
        sessionEntity.setExpiresAt(DateUtils.addMinutes(sessionEntity.getCreatedAt(), 60));
        sessionEntity.setExpired(false);
        sessionEntity.setUserEmail(loginVo.getEmail());
        sessionEntity.setKey(sessionKey);
        sessionEntity.setUa(userAgent);
        this.userMapper.insertSession(sessionEntity);

        loginVo.setSessionEntity(sessionEntity);
        loginVo.setResult(LoginResult.SUCCESS);
    }

    public void register(RegisterVo registerVo) {
        // 비밀번호 hashing
        registerVo.setPassword(CryptoUtil.hashSha512(registerVo.getPassword()));

//        if(!UserService.checkEmail(registerVo.getEmail())) {
//            registerVo.setResult(RegisterResult.FAILURE);
//            return;
//        }

        // 주어진 이메일이 있으면 Duplicate
        // 1. 주어진 이메일이 뭘까?
        // 2. 주어진 이메일의 개수를 가져오려면 어느 객체의 어느 메서드를 호출헤야할까?
        // 3. <2>에서 가져온 값이 어떨 때 이메일이 이미 있는걸까?
        // 인서트 결과 영행을 받은(update, delete, insert) 레코드 수가 0이면 FAILURE, 아니면 SUCCESS

        if (this.userMapper.selectUserCountByEmail(registerVo.getEmail()) > 0) {
            registerVo.setResult(RegisterResult.DUPLICATE);
            return;
        }
        if (this.userMapper.insertUser(registerVo) == 0) {
            registerVo.setResult(RegisterResult.FAILURE);
        } else {
            registerVo.setResult(RegisterResult.SUCCESS);
        }
    }


}
