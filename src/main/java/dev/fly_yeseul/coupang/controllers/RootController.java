package dev.fly_yeseul.coupang.controllers;


import dev.fly_yeseul.coupang.dtos.product.ProductDto;
import dev.fly_yeseul.coupang.entities.product.ProductEntity;
import dev.fly_yeseul.coupang.services.ProductService;
import jdk.jfr.internal.RequestEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

@Controller(value = "dev.fly_yeseul.coupang.controllers.RootController")
@RequestMapping(value = "/")
//@SessionAttributes(value = "userEntity")
public class RootController {
    private final ProductService productService;

    @Autowired
    public RootController(ProductService productService) {
        this.productService = productService;
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ModelAndView getIndex(
            ModelAndView modelAndView
    ) {
        ProductDto[] productDtos = this.productService.getProducts();
        modelAndView.addObject("productDtos", productDtos);
        modelAndView.setViewName("root/index");
        return modelAndView;
    };

//    @RequestMapping(value = "/", method = RequestMethod.GET)
//    public ModelAndView getSearch(
//            ProductDto productDto,
//            ModelAndView modelAndView,
//            @RequestParam(value = "keyword", required = true) String keyword
//    ) {
//        modelAndView.addObject("productDto", productDto);
//        modelAndView.setViewName("root/index");
//        return modelAndView;
//    }

}
