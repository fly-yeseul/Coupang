package dev.fly_yeseul.coupang.services;

import dev.fly_yeseul.coupang.dtos.product.ProductDto;
import dev.fly_yeseul.coupang.entities.product.ImageEntity;
import dev.fly_yeseul.coupang.entities.product.ProductEntity;
import dev.fly_yeseul.coupang.entities.product.StockEntity;
import dev.fly_yeseul.coupang.entities.product.ThumbnailEntity;
import dev.fly_yeseul.coupang.enums.product.AddResult;
import dev.fly_yeseul.coupang.enums.product.DetailResult;
import dev.fly_yeseul.coupang.mappers.IProductMapper;
import dev.fly_yeseul.coupang.utils.CryptoUtil;
import dev.fly_yeseul.coupang.vos.product.AddVo;
import dev.fly_yeseul.coupang.vos.product.DetailVo;
import dev.fly_yeseul.coupang.vos.product.SearchVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service(value = "dev.fly_yeseul.coupang.services.ProductService")
public class ProductService {
    private final IProductMapper productMapper;

    @Autowired
    public ProductService(IProductMapper productMapper) {
        this.productMapper = productMapper;
    }

    public void addProduct(AddVo addVo) {
        if (this.productMapper.insertProduct(addVo) == 0) {
            addVo.setResult(AddResult.FAILURE);
        } else {
            addVo.setResult(AddResult.SUCCESS);
        }
    }

    public ImageEntity getImage(String id) {
        return this.productMapper.selectImage(id);
    }

    public ThumbnailEntity getThumbnail(String id) {
        return this.productMapper.selectThumbnail(id);
    }


    public void getProduct(DetailVo detailVo){
        ProductEntity productEntity = this.productMapper.selectProductByIndex(detailVo.getIndex());
        if(productEntity == null || productEntity.getTitle() == null) {
            detailVo.setResult(DetailResult.NOT_FOUND);
            return;
        }
        detailVo.setIndex(productEntity.getIndex());
        detailVo.setTitle(productEntity.getTitle());
        detailVo.setPrice(productEntity.getPrice());
        detailVo.setContent(productEntity.getContent());
        detailVo.setDeliveryValue(productEntity.getDeliveryValue());
        detailVo.setThumbnailId(productEntity.getThumbnailId());
        detailVo.setResult(DetailResult.SUCCESS);
    }
    public void getProduct(ProductDto productDto){
        ProductEntity productEntity = this.productMapper.selectProductByIndex(productDto.getIndex());
        if(productEntity == null || productEntity.getTitle() == null) {
            return;
        }
        productDto.setIndex(productEntity.getIndex());
        productDto.setTitle(productEntity.getTitle());
        productDto.setPrice(productEntity.getPrice());
        productDto.setContent(productEntity.getContent());
        productDto.setDeliveryValue(productEntity.getDeliveryValue());
        productDto.setThumbnailId(productEntity.getThumbnailId());
        productDto.setCount(this.productMapper.selectProductStock(productDto));
    }
    @Transactional
    public void deleteThumbnail(ProductEntity productEntity, ThumbnailEntity thumbnailEntity) {
        this.productMapper.deleteThumbnail(thumbnailEntity);
        productEntity.setThumbnailId(null);
        this.productMapper.updateProduct(productEntity);
    }

    public void deleteProduct(ProductEntity productEntity){
        this.productMapper.deleteProduct(productEntity);
    }


    public ProductEntity getProductByIndex(int index) {
        return this.productMapper.selectProductByIndex(index);
    }

    public ProductDto[] getProducts() {
        return this.productMapper.selectProducts();
    }

//    @Transactional
    public ProductDto[] searchProducts(String keyword) {
        return this.productMapper.selectProductsByTitle(keyword);
    }

    public void modifyProduct(ProductEntity productEntity){
    }

    public void putThumbnail(int productIndex, MultipartFile multipartFile) throws IOException {
        String id = String.format("%s%d%f%f",
                new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date()),
                productIndex,
                Math.random(),
                Math.random());
        id = CryptoUtil.hashSha512(id);
        ThumbnailEntity thumbnailEntity = new ThumbnailEntity(id, multipartFile.getBytes());
        this.productMapper.insertThumbnail(thumbnailEntity);

        ProductEntity productEntity = this.productMapper.selectProductByIndex(productIndex);
        productEntity.setThumbnailId(id);
        this.productMapper.updateProduct(productEntity);
    }

    public void uploadImages(ImageEntity... imageEntities) {
        for (ImageEntity imageEntity : imageEntities) {
            this.productMapper.insertImage(imageEntity);
        }
    }


    public StockEntity[] getStocks(ProductDto productDto, int limit) {
        return this.productMapper.selectStocks(productDto.getIndex(), limit);
    }

    public void putStock(StockEntity stockEntity) {
        this.productMapper.insertStock(stockEntity);
    }



}
