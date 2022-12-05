package dev.fly_yeseul.coupang.controllers;


import dev.fly_yeseul.coupang.entities.member.user.UserEntity;
import dev.fly_yeseul.coupang.entities.product.ProductEntity;
import dev.fly_yeseul.coupang.entities.shopping.CartEntity;
import dev.fly_yeseul.coupang.entities.shopping.OrderEntity;
import dev.fly_yeseul.coupang.entities.shopping.ReviewEntity;
import dev.fly_yeseul.coupang.enums.ExtendedResult;
import dev.fly_yeseul.coupang.enums.GeneralResult;
import dev.fly_yeseul.coupang.enums.shopping.cart.AddResult;
import dev.fly_yeseul.coupang.services.ProductService;
import dev.fly_yeseul.coupang.services.ShoppingService;
import dev.fly_yeseul.coupang.vos.shopping.cart.AddVo;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.ImmutableTriple;
import org.apache.commons.lang3.tuple.Pair;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@Controller(value = "dev.fly_yeseul.coupang.controllers.ShoppingController")
@RequestMapping(value = "/shopping")
public class ShoppingController {

    private final ShoppingService shoppingService;
    private final ProductService productService;


    @Autowired
    public ShoppingController(ShoppingService shoppingService, ProductService productService) {
        this.shoppingService = shoppingService;
        this.productService = productService;
    }


    @RequestMapping(value = "cart", method = RequestMethod.GET)
    public ModelAndView getCart(
            @RequestAttribute(value = "userEntity", required = false) UserEntity userEntity,
            ModelAndView modelAndView
    ) {
        if (userEntity == null) {
            modelAndView.setViewName("redirect:/user/login");
        } else {
            // Tuple 튜플
            int totalProduct = 0;
            int totalShipping = 0;
            List<Pair<CartEntity, ProductEntity>> pairList = new ArrayList<>();       //  key value
            CartEntity[] cartEntities = this.shoppingService.getCartsOf(userEntity);
            for (CartEntity cartEntity : cartEntities) {
                ProductEntity productEntity = this.productService.getProductByIndex(cartEntity.getProductIndex());

                // Immutable: 불편의
                // 보통 final
                ImmutablePair<CartEntity, ProductEntity> pair = new ImmutablePair<>(cartEntity, productEntity);
                pairList.add(pair);
                totalProduct += productEntity.getPrice() * cartEntity.getCount();
                if (productEntity.getDeliveryValue().equals("normal")) {
                    totalShipping += 3000;
                }
            }
            modelAndView.addObject("totalProduct", totalProduct);
            modelAndView.addObject("totalShipping", totalShipping);
            modelAndView.addObject("total", totalProduct + totalShipping);
            modelAndView.addObject("pairList", pairList);
            modelAndView.setViewName("shopping/cart");
        }
        return modelAndView;
    }


    @RequestMapping(value = "cart/add", method = RequestMethod.POST)
    @ResponseBody
    public String postCartAdd(
            @RequestAttribute(value = "userEntity", required = false) UserEntity userEntity,
            @RequestParam(value = "productIndex", required = true) int productIndex,
            @RequestParam(value = "count", required = true) int count
    ) {
        AddVo addVo = new AddVo();
        if (userEntity == null) {
            addVo.setResult(AddResult.NOT_SIGNED);
        } else {
            addVo.setUserEmail(userEntity.getEmail());
            addVo.setProductIndex(productIndex);
            addVo.setCount(count);
            this.shoppingService.addCart(addVo);
        }
        JSONObject jsonResponse = new JSONObject();
        jsonResponse.put("result", addVo.getResult().name());
        return jsonResponse.toString();
    }

    @RequestMapping(value = "cart/delete", method = RequestMethod.GET)
    public ModelAndView getCartDelete(
            @RequestAttribute(value = "userEntity", required = false) UserEntity userEntity,
            @RequestParam(value = "index", required = true) int productIndex,
            ModelAndView modelAndView
    ) {
        if (userEntity == null) {
            modelAndView.setViewName("redirect:/user/login");
        } else {
            this.shoppingService.deleteCartsOf(userEntity, productIndex);
            modelAndView.setViewName("redirect:/shopping/cart");
        }
        return modelAndView;
    }

    @RequestMapping(value = "order", method = RequestMethod.GET)
    public ModelAndView getOrder(
            @RequestAttribute(value = "userEntity", required = false) UserEntity userEntity,
            ModelAndView modelAndView
    ) {
        if (userEntity == null) {
            modelAndView.setViewName("redirect:/user/login");
        } else {
            modelAndView.setViewName("shopping/order");
        }
        return modelAndView;
    }

    @RequestMapping(value = "order", method = RequestMethod.POST)
    public ModelAndView postOrder(
            @RequestAttribute(value = "userEntity", required = false) UserEntity userEntity,
            @RequestParam(value = "pid", required = false) Optional<Integer> productIndex,
            @RequestParam(value = "count", required = false) Optional<Integer> count,
            ModelAndView modelAndView
    ) {
        if (userEntity == null) {
            modelAndView.setViewName("redirect:/user/login");
        } else if (productIndex.orElse(0) == 0) {
            this.shoppingService.orderAll(userEntity);
            modelAndView.setViewName("redirect:/shopping/order");
        } else {
            this.shoppingService.orderSpecific(userEntity, productIndex.get(), count.orElse(1));
            modelAndView.setViewName("redirect:/shopping/order");
        }
        return modelAndView;
    }

    @RequestMapping(value = "order/status/modify", method = RequestMethod.POST)
    @ResponseBody
    public String postOrderStatusModify(
            @RequestAttribute(value = "userEntity", required = true) UserEntity userEntity,
            @RequestParam(value = "orderIndex", required = true) int orderIndex,
            @RequestParam(value = "orderStatusIndex", required = true) int orderStatusIndex,
            HttpServletResponse response
    ) {
        if (userEntity == null || !userEntity.isAdmin()) {
            response.setStatus(404);
            return null;
        }
        GeneralResult result;
        OrderEntity orderEntity = this.shoppingService.getOrder(orderIndex);
        if (orderEntity == null) {
            result = GeneralResult.NOT_FOUND;
        } else {
            orderEntity.setOrderStatusIndex(orderStatusIndex);
            result = this.shoppingService.modifyOrder(orderEntity)
                    ? GeneralResult.SUCCESS
                    : GeneralResult.FAILURE;
        }
        JSONObject responseJSON = new JSONObject();
        responseJSON.put("result", result.name());
        return responseJSON.toString();
    }

    @RequestMapping(value = "review/write", method = RequestMethod.GET)
    public ModelAndView getReviewWrite(
            @RequestAttribute(value = "userEntity", required = false) UserEntity userEntity,
            @RequestParam(value = "oid", required = true) int orderIndex,
            ModelAndView modelAndView
    ) {
        if (userEntity == null) {
            modelAndView.setViewName("redirect:/user/login");
            return modelAndView;
        }
        Enum<?> result;         // generic
        OrderEntity orderEntity = this.shoppingService.getOrder(orderIndex);
        if (orderEntity == null) {
            result = GeneralResult.NOT_FOUND;
        } else if (!userEntity.getEmail().equals(orderEntity.getUserEmail())) {
            result = ExtendedResult.NOT_ALLOWED;
        } else {
            ProductEntity productEntity = this.productService.getProductByIndex(orderEntity.getProductIndex());
            result = GeneralResult.SUCCESS;
            modelAndView.addObject("productEntity", productEntity);
        }
        modelAndView.addObject("result", result);
        modelAndView.setViewName("shopping/review/write");
        return modelAndView;
    }

    @RequestMapping(value = "review/write", method = RequestMethod.POST)
    public ModelAndView postReviewWrite(
            @RequestAttribute(value = "userEntity", required = false) UserEntity userEntity,
            @RequestParam(value = "oid", required = true) int orderIndex,
            @RequestParam(value = "rate", required = true) int rate,
            @RequestParam(value = "comment", required = true, defaultValue = "") String comment,
            ModelAndView modelAndView
    ) {
        if (userEntity == null) {
            modelAndView.setViewName("redirect:/user/login");
            return modelAndView;
        }
        Enum<?> result;         // generic
        OrderEntity orderEntity = this.shoppingService.getOrder(orderIndex);
        if (orderEntity == null) {
            result = GeneralResult.NOT_FOUND;
        } else if (!userEntity.getEmail().equals(orderEntity.getUserEmail())) {
            result = ExtendedResult.NOT_ALLOWED;
        } else if (this.shoppingService.reviewExists(orderEntity)) {
            result = ExtendedResult.DUPLICATE;
        } else {
            ReviewEntity reviewEntity = new ReviewEntity();
            reviewEntity.setUserEmail(userEntity.getEmail());
            reviewEntity.setProductIndex(orderEntity.getProductIndex());
            reviewEntity.setOrderIndex(orderEntity.getIndex());
            reviewEntity.setRate(rate);
            reviewEntity.setComment(comment);
            this.shoppingService.putReview(reviewEntity);
            modelAndView.setViewName("redirect:/product/detail/" + orderEntity.getProductIndex());
            return modelAndView;
        }
        ProductEntity productEntity = this.productService.getProductByIndex(orderEntity.getProductIndex());
        modelAndView.addObject("result", result);
        modelAndView.setViewName("shopping/review/write");
        return modelAndView;
    }
}

