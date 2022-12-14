package dev.fly_yeseul.coupang.controllers;


import dev.fly_yeseul.coupang.entities.member.SessionEntity;
import dev.fly_yeseul.coupang.entities.member.user.UserEntity;
import dev.fly_yeseul.coupang.entities.product.ProductEntity;
import dev.fly_yeseul.coupang.entities.shopping.OrderEntity;
import dev.fly_yeseul.coupang.entities.shopping.OrderStatusEntity;
import dev.fly_yeseul.coupang.enums.member.user.LoginResult;
import dev.fly_yeseul.coupang.services.ProductService;
import dev.fly_yeseul.coupang.services.ShoppingService;
import dev.fly_yeseul.coupang.services.UserService;
import dev.fly_yeseul.coupang.vos.member.user.LoginVo;
import dev.fly_yeseul.coupang.vos.member.user.RegisterVo;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.ModelAndView;

import javax.jws.soap.SOAPBinding;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Controller(value = "dev.fly_yeseul.coupang.controllers.UserController")
@RequestMapping(value = "/user")
//@SessionAttributes(value = "userEntity")
public class UserController {

    private final UserService userService;
    private final ShoppingService shoppingService;
    private final ProductService productService;

    @Autowired
    public UserController(UserService userService, ShoppingService shoppingService, ProductService productService) {
        this.userService = userService;
        this.shoppingService = shoppingService;
        this.productService = productService;
    }


    // ????????? ?????? ???????????? ???
    @RequestMapping(value = "login", method = RequestMethod.GET)
    public ModelAndView getLogin(
            ModelAndView modelAndView
    ) {
        modelAndView.setViewName("user/login");
        return modelAndView;
    }


    // ????????? ?????? post ?????? ???
    @RequestMapping(value = "login", method = RequestMethod.POST)
    public ModelAndView postLogin(
            ModelAndView modelAndView,
            HttpServletResponse response,
            LoginVo loginVo,
            HttpServletRequest request
    ) {
        loginVo.setResult(null);
        this.userService.login(loginVo, request);
        if (loginVo.getResult() == LoginResult.SUCCESS) {
            Cookie sessionKeyCookie = new Cookie("sk", loginVo.getSessionEntity().getKey());
            // ?????? Path ??? ?????? ????????? ??????. (/user/**)
            sessionKeyCookie.setPath("/");
            response.addCookie(sessionKeyCookie);
//            modelAndView.addObject("userEntity", loginVo);
//            modelAndView.setViewName("redirect:/");
        }
        modelAndView.addObject("loginVo", loginVo);
        modelAndView.setViewName("user/login");
        return modelAndView;
    }


    @RequestMapping(value = "logout", method = RequestMethod.GET)
    public ModelAndView getLogout(
//            SessionStatus sessionStatus,      // SessionAttributes ??? ????????? ???????????? ????????????.
            ModelAndView modelAndView,
            HttpServletRequest request
//            SessionEntity sessionEntity

    ) {
        // 1. ???????????? 'UserEntity' ????????? ?????? ???????????? ?????? ?????? email ??? null ??? ???????????? 'session' ??????????????? ??? ???????????? ?????? ????????? ?????? ?????? ?????????.
        // 2. "sk"?????? ????????? ????????? ????????? ????????? ???.
        // 3. redirect

//        sessionStatus.setComplete();            // SessionAttributes ??? ????????? ???????????? ????????????.
        // ?????????????????? ??????????????? ??????????????? ????????? ????????????.
        if (request.getAttribute("sessionEntity") instanceof SessionEntity) {
//            System.out.println(sessionEntity.getUserEmail());
            SessionEntity sessionEntity = (SessionEntity) request.getAttribute("sessionEntity");
//            System.out.println(sessionEntity.getUserEmail());
            this.userService.expireSession(sessionEntity);
//            System.out.println(sessionEntity.getUserEmail());
        }

        modelAndView.setViewName("redirect:/");
        return modelAndView;
    }

    @RequestMapping(value = "register", method = RequestMethod.GET)
    public ModelAndView postRegister(
            ModelAndView modelAndView
    ) {
        modelAndView.setViewName("user/register");
        return modelAndView;
    }

    @RequestMapping(value = "register", method = RequestMethod.POST)
    public ModelAndView getRegister(
            ModelAndView modelAndView,
            RegisterVo registerVo
    ) {
        registerVo.setAdmin(false);
        registerVo.setResult(null);

        this.userService.register(registerVo);
        modelAndView.addObject("registerVo", registerVo);
        modelAndView.setViewName("user/register");
        return modelAndView;
    }

    @RequestMapping(value = "my", method = RequestMethod.GET)
    public ModelAndView getMy(
            @RequestAttribute(value = "userEntity", required = false) UserEntity userEntity,
            ModelAndView modelAndView
    ) {
        // ??????????????? ?????????????????? ?????????????????? ?????????
        if (userEntity == null) {
            modelAndView.setViewName("redirect:/user/login");
        } else {
            List<Pair<OrderEntity, ProductEntity>> pairList = new ArrayList<>();
            OrderEntity[] orderEntities = userEntity.isAdmin()
                    ? this.shoppingService.getOrdersAll()
                    : this.shoppingService.getOrdersOf(userEntity);
            for(OrderEntity orderEntity : orderEntities) {
                ProductEntity productEntity = this.productService.getProductByIndex(orderEntity.getProductIndex());
                ImmutablePair<OrderEntity, ProductEntity> pair = new ImmutablePair<>(orderEntity, productEntity);
                pairList.add(pair);
            }
            // HashMap ?????? ??? ????????????
            HashMap<Integer, String> orderStatuses = new HashMap<>();
            OrderStatusEntity[] orderStatusEntities = this.shoppingService.getOrderStatuses();
            for(OrderStatusEntity orderStatusEntity : orderStatusEntities) {
                orderStatuses.put(orderStatusEntity.getIndex(), orderStatusEntity.getText());
            }

            modelAndView.addObject("orderStatuses", orderStatuses);
            modelAndView.addObject("pairList", pairList);
            modelAndView.setViewName("user/my");
        }
        return modelAndView;
    }
}
