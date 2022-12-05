package dev.fly_yeseul.coupang.controllers;

import dev.fly_yeseul.coupang.dtos.product.ProductDto;
import dev.fly_yeseul.coupang.entities.member.user.UserEntity;
import dev.fly_yeseul.coupang.entities.product.ImageEntity;
import dev.fly_yeseul.coupang.entities.product.ProductEntity;
import dev.fly_yeseul.coupang.entities.product.StockEntity;
import dev.fly_yeseul.coupang.entities.product.ThumbnailEntity;
import dev.fly_yeseul.coupang.entities.shopping.ReviewEntity;
import dev.fly_yeseul.coupang.enums.product.AddResult;
import dev.fly_yeseul.coupang.services.ProductService;
import dev.fly_yeseul.coupang.services.ShoppingService;
import dev.fly_yeseul.coupang.utils.CryptoUtil;
import dev.fly_yeseul.coupang.vos.product.AddVo;
import dev.fly_yeseul.coupang.vos.product.DetailVo;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.thymeleaf.model.IModel;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.ws.Response;
import java.awt.*;
import java.io.IOException;
import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

@Controller(value = "dev.fly_yeseul.coupang.controllers.ProductController")
@RequestMapping(value = "/product")
public class ProductController {
    private final ProductService productService;
    private final ShoppingService shoppingService;


    @Autowired
    public ProductController(ProductService productService, ShoppingService shoppingService) {
        this.productService = productService;
        this.shoppingService = shoppingService;
    }





    // 업로드 하는거 보여지는 페이지 생성
    @RequestMapping(value = "add", method = RequestMethod.GET)
    public ModelAndView getAdd(
            @RequestAttribute(value = "userEntity", required = false) UserEntity userEntity,
            // UserEntity 속성 부여
            HttpServletResponse response,
            // httpservletrequest 써서 인스턴스어쩌구저쩌구해서 가져오는거 안해도되게 해줌.
            ModelAndView modelAndView
    ) {
        // 유저 정보가 없거나 유저가 관리자가 아니면 404 오류 낸다.
        if (userEntity == null || !userEntity.isAdmin()) {
            response.setStatus(404);
            return null;
        }
        modelAndView.setViewName("product/add");
        return modelAndView;
    }

    // 게시글 작성해서 DB 넘기는 그거
    @RequestMapping(value = "add", method = RequestMethod.POST)
    public ModelAndView postAdd(
            @RequestAttribute(value = "userEntity", required = false) UserEntity userEntity,
            HttpServletResponse response,
            AddVo addVo,
            // httpservletrequest 써서 인스턴스어쩌구저쩌구해서 가져오는거 안해도되게 해줌.
            ModelAndView modelAndView
    ) {
        // 회원이 아니거나 관리자가 아니면 오류발생시키기
        if (userEntity == null || !userEntity.isAdmin()) {
            response.setStatus(404);
            return null;
        }
        addVo.setResult(null);
        this.productService.addProduct(addVo);
        if (addVo.getResult() == AddResult.SUCCESS) {
            modelAndView.setViewName("redirect:/product/detail/" + addVo.getIndex());
        } else {
            modelAndView.addObject("addVo", addVo);
            modelAndView.setViewName("product/add");
        }
        return modelAndView;
    }

    @RequestMapping(value = "delete", method = RequestMethod.GET)
    public ModelAndView getDelete(
            HttpServletResponse response,
            ModelAndView modelAndView,
            @RequestAttribute(value = "userEntity", required = false) UserEntity userEntity,
            @RequestParam(value = "pid", required = true) int productIndex
    ) {
        ProductEntity productEntity = new ProductEntity();
        productEntity.setIndex(productIndex);
        this.productService.deleteProduct(productEntity);
        modelAndView.setViewName("redirect:/");
        return modelAndView;
    }


    // 이미지 다운받는거
    @RequestMapping(value = "download-image", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<byte[]> getDownloadImage(
            // 파라메터 아이디 받아온다
            @RequestParam(value = "id", required = false) String id
    ) {
        ImageEntity imageEntity = null;
        // id 가 null 이 아니면 id에 맞는 이미지를 imageEntity 에 대입한다.
        if (id != null) {
            imageEntity = this.productService.getImage(id);
        }
        byte[] data = null;
        HttpHeaders headers = new HttpHeaders();
        HttpStatus status = HttpStatus.NOT_FOUND;           // 404
        if (imageEntity != null && imageEntity.getData() != null) {
            data = imageEntity.getData();
            headers.add("Content-Type", imageEntity.getFileType());
            headers.add("Content-Disposition", String.format("attachment; filename=\"%s\"", imageEntity.getFileName()));
            headers.add("Content-Length", String.valueOf(data.length));
            // 다운로드 받을 때 11gb 중 10gb 다운로드 중 이거 뜰수 있게 하는 요소
            status = HttpStatus.OK;
        }
        return new ResponseEntity<>(data, headers, status);
    }


    @RequestMapping(value = "add/upload-image", method = RequestMethod.POST)
    @ResponseBody
    public String postAddUploadImage(
            HttpServletResponse response,
            @RequestAttribute(name = "userEntity", required = false) UserEntity userEntity,
            @RequestParam(name = "upload") MultipartFile[] images
            // 이거 for 문 때문에 여러 사진 한꺼번에 올리면 각각 요청이 들어가는데 한꺼번에 요청 들어가는걸로 바꿔보기
    ) throws IOException {
        if (userEntity == null || !userEntity.isAdmin()) {
            response.setStatus(404);
            return null;
        }
        ImageEntity[] imageEntities = new ImageEntity[images.length];
        for (int i = 0; i < images.length; i++) {
            MultipartFile image = images[i];
            Date createdAt = new Date();
            String id = String.format("%s%s%f",
                    new SimpleDateFormat("yyyyMMddHHmmssSSS").format(createdAt),
                    image.getOriginalFilename(),
                    Math.random()
            );

            id = CryptoUtil.hashSha512(id);
            imageEntities[i] = new ImageEntity(id, createdAt,
                    image.getOriginalFilename(),
                    image.getContentType(),
                    image.getBytes());
// entity랑 뭐그런거 다 받아서 서비스한테 줘야해서 이 로직이 컨트롤러에 있다.

        }
        this.productService.uploadImages(imageEntities);
        JSONObject responseJson = new JSONObject();
        JSONArray urlJson = new JSONArray();
        for (ImageEntity imageEntity : imageEntities) {
            urlJson.put(String.format("http://127.0.0.1:8080/product/download-image?id=%s", imageEntity.getId()));
        }
        responseJson.put("url", urlJson);
        return responseJson.toString();
    }


    @RequestMapping(value = "detail/{pid}", method = RequestMethod.GET)
    public ModelAndView getDetail(
            ProductDto productDto,
            ModelAndView modelAndView,
            @PathVariable(name = "pid", required = true) int productIndex
    ) {
        productDto.setIndex(productIndex);
        this.productService.getProduct(productDto);

        double reviewRate = 00;
        ReviewEntity[] reviewEntities = this.shoppingService.getReviews(productDto);
        for (ReviewEntity reviewEntity : reviewEntities) {
            reviewRate += reviewEntity.getRate();
        }
        reviewRate /= reviewEntities.length;
        productDto.setReviewCount(reviewEntities.length);
        productDto.setReviewRate(reviewRate);

        modelAndView.addObject("productDto", productDto);
        modelAndView.addObject("stockEntities", this.productService.getStocks(productDto, 5));
        modelAndView.addObject("reviewEntities", reviewEntities);
        modelAndView.setViewName("product/detail");
        return modelAndView;
    }

    @RequestMapping(value = "detail/thumbnail", method = RequestMethod.GET)
    public ResponseEntity<byte[]> getDetailThumbnail (
            // 파라메터 아이디 받아온다
            @RequestParam(value = "id", required = false) String id
    ) {
        ThumbnailEntity thumbnailEntity = null;
        // id 가 null 이 아니면 id에 맞는 이미지를 imageEntity 에 대입한다.
        if (id != null) {
            thumbnailEntity = this.productService.getThumbnail(id);
        }
        byte[] data = null;
        HttpHeaders headers = new HttpHeaders();
        HttpStatus status = HttpStatus.NOT_FOUND;           // 404
        if (thumbnailEntity != null && thumbnailEntity.getData() != null) {
            data = thumbnailEntity.getData();
            headers.add("Content-Type", "image/png");
            headers.add("Content-Disposition", String.format("attachment; filename=\"%s\"", id + ".png"));
            headers.add("Content-Length", String.valueOf(data.length));
            // 다운로드 받을 때 11gb 중 10gb 다운로드 중 이거 뜰수 있게 하는 요소
            status = HttpStatus.OK;
        }
        return new ResponseEntity<>(data, headers, status);
    }

    @RequestMapping(value = "detail/thumbnail/add", method = RequestMethod.POST)
    public ModelAndView postDetailThumbnailAdd(
            HttpServletResponse response,
            ModelAndView modelAndView,
            @RequestAttribute(name = "userEntity", required = false) UserEntity userEntity,
            @RequestParam(value = "productIndex", required = true) int productIndex,
            @RequestParam(value = "thumbnail", required = true) MultipartFile thumbnail
    ) throws IOException {
        if(userEntity == null || !userEntity.isAdmin()) {
            response.setStatus(404);
            return null;
        }
        this.productService.putThumbnail(productIndex, thumbnail);
        modelAndView.setViewName("redirect:/product/detail/" + productIndex);
        return null;
    }

    @RequestMapping(value = "detail/thumbnail/delete", method = RequestMethod.GET)
    public ModelAndView getDetailThumbnailDelete(
            HttpServletResponse response,
            ModelAndView modelAndView,
            @RequestAttribute(name = "userEntity", required = false) UserEntity userEntity,
            @RequestParam(value = "id", required = true) String thumbnailId,
            @RequestParam(value = "pid", required = true) int productIndex
    ) {
        if (userEntity == null || !userEntity.isAdmin()) {
            response.setStatus(404);
            return null;
        }
        ProductEntity productEntity = new ProductEntity();
        ThumbnailEntity thumbnailEntity = new ThumbnailEntity();
        productEntity.setIndex(productIndex);
        thumbnailEntity.setId(thumbnailId);
        this.productService.deleteThumbnail(productEntity, thumbnailEntity);
        modelAndView.setViewName("redirect:/product/detail/" + productIndex);
        return modelAndView;
    }


    @RequestMapping(value = "modify", method = RequestMethod.GET)
    public ModelAndView getModify(
            HttpServletResponse response,
            ModelAndView modelAndView,
            @RequestAttribute(name = "userEntity", required = false) UserEntity userEntity,
            @RequestParam(value = "pid", required = true) int productIndex
    ) {
        if(userEntity != null && userEntity.isAdmin()) {
            response.setStatus(404);
            return null;
        }
        DetailVo detailVo = new DetailVo();
        detailVo.setIndex(productIndex);
        this.productService.getProduct(detailVo);
        modelAndView.addObject("detailVo", detailVo);
        modelAndView.setViewName("product/modify");
        return modelAndView;
    }

    @RequestMapping(value = "modify", method = RequestMethod.POST)
    public ModelAndView postModify(
            HttpServletResponse response,
            ProductEntity productEntity,
            ModelAndView modelAndView,
            @RequestAttribute(name = "userEntity", required = false) UserEntity userEntity,
            @RequestParam(value = "pid", required = true) int productIndex
    ) {
        if(userEntity != null && userEntity.isAdmin()) {
            response.setStatus(404);
            return null;
        }
        productEntity.setIndex(productIndex);
        this.productService.modifyProduct(productEntity);
        modelAndView.setViewName("redirect:/product/detail/" + productIndex);
        return modelAndView;
    }

    @RequestMapping(value = "stock/add", method = RequestMethod.POST)
    public ModelAndView postStockAdd(
            ModelAndView modelAndView,
            HttpServletResponse response,
            @RequestAttribute(name = "userEntity", required = false) UserEntity userEntity,
            @RequestParam(value = "productIndex", required = true) int productIndex,
            @RequestParam(value = "type", required = true) String type,
            @RequestParam(value = "count", required = true) int count
    ) {
        if(userEntity == null || !userEntity.isAdmin()) {
            response.setStatus(404);
            return null;
        }
        StockEntity stockEntity = new StockEntity();
        stockEntity.setProductIndex(productIndex);
        stockEntity.setCreatedAt(new Date());
        stockEntity.setCount(type.equals("out") ? count * -1 : count);
        this.productService.putStock(stockEntity);
        modelAndView.setViewName("redirect:/product/detail/" + productIndex);
        return modelAndView;
    }
}
