package dev.fly_yeseul.coupang.interceptors;

import dev.fly_yeseul.coupang.entities.member.SessionEntity;
import dev.fly_yeseul.coupang.entities.member.user.UserEntity;
import dev.fly_yeseul.coupang.services.UserService;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.text.html.Option;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Date;
import java.util.Optional;

public class SessionInterceptor implements HandlerInterceptor {
    @Autowired
    private UserService userService;


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 1. 쿠키에서 sk 값 가져오기
        // 2. <1>의 sk 를 이용하여 DB 에서 SessionEntity 긁어오기. 단, 만료되지 않은 거만
        // 3. <2>에서 들어온 SessionEntity 가 null 이거나 userEmail 이 없거나 하면 로그인 안한거임.
        // 4. <2>에서 긁어온 SessionEntity 의 UserEmail 이 있고 이 email 로 UserEntity 를 긁어왔더니 정상이다 = 로그인 된 상태.
        // 5. <4>에서 긁어온 UserEntity 를 request 객체에 Attribute 로 추가하면 다 OK.

        Cookie sessionKeyCookie = null;
        // 쿠키값을 null 로 선언
        if (request.getCookies() != null) {              // 쿠키가 아무것도 없는 상태에서 getCookies() 하면 null값 나와서 nullpointException 나옴 그래서 null값은 걸러줄려고 if문 씀
            for (Cookie cookie : request.getCookies()) {
                if (cookie.getName().equals("sk")) {        // sk 라는 이름의 쿠키 값을 가져옴
                    sessionKeyCookie = cookie;              // 변수 선언
                    break;
                }
            }

            //위에 for 문이랑 같은거임.
//            sessionKeyCookie = Arrays.stream(request.getCookies())
//                    .filter(x -> x.getName().equals("sk"))
//                    .findFirst()
//                    .orElse(null);
//            // -.findFirst() 는 Optional<Cookie>를 돌려줌
//            Optional <T>;
        }
        if (sessionKeyCookie != null && sessionKeyCookie.getValue() != null) {                      // 원래 정규화도 해야함
            String sessionKey = sessionKeyCookie.getValue();                                        // (1번 끝) sk 쿠키의 value(값)을 가져옴
            SessionEntity sessionEntity = this.userService.getSession(sessionKey);                  // 가져온 쿠키의 세션값을 sessionEntity 에 대입함.
            if (sessionEntity != null && sessionEntity.getUserEmail() != null) {                    // sessionEntity 가 null 이 아니고 sessionEntity 의 userEmail 값이 null 이 아니면
                UserEntity userEntity = this.userService.getUser(sessionEntity.getUserEmail());     // sessionEntity 에서 userEmail 값을 가져와서 userService 를 통해 userEntity 에 넣는다.
//                System.out.println('1' + sessionEntity.getUserEmail());
                if (userEntity != null && userEntity.getEmail() != null) {                          // userEntity 가 null 이 아니고 userEntity 의 Email 값이 null 이 아니면
                    this.userService.extendSession(sessionEntity);                                  // sessionEntity 의 expiresAt 값을 현재 시간기준으로 연장한다.
//                    System.out.println('2' + sessionEntity.getUserEmail());
                    request.setAttribute("sessionEntity", sessionEntity);                        // @@ setAttribute(session 에 Data 저장)
//                    System.out.println('3' + sessionEntity.getUserEmail());                                                                           // session.setAttribute("변수명", 변수값)
                    request.setAttribute("userEntity", userEntity);
//                    System.out.println('4' + sessionEntity.getUserEmail());
                }
            }
        }
        if (request.getAttribute("userEntity") == null && sessionKeyCookie != null) {            // request
                                                                                                    // getAttribute : 세션에 저장된 변수값 불러온다.
            sessionKeyCookie.setMaxAge(0);      // 쿠키는 생명이다. life span
            // Cookie 는 remove 나 delete 같은게 없어서 setMaxAge 를 0으로 둠으로써 삭제할 수 있다.
//            response.addCookie(sessionKeyCookie);
//            System.out.println('2');
        }
        return true;
    }
}
