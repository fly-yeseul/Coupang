package dev.fly_yeseul.coupang.mappers;

import dev.fly_yeseul.coupang.dtos.product.ProductDto;
import dev.fly_yeseul.coupang.entities.product.ImageEntity;
import dev.fly_yeseul.coupang.entities.product.ProductEntity;
import dev.fly_yeseul.coupang.entities.product.StockEntity;
import dev.fly_yeseul.coupang.entities.product.ThumbnailEntity;
import dev.fly_yeseul.coupang.services.ProductService;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface IProductMapper {

    int deleteThumbnail(ThumbnailEntity thumbnailEntity);
    int deleteProduct(ProductEntity productEntity);
    int insertImage(ImageEntity imageEntity);

    int insertProduct(ProductEntity productEntity);

    int insertStock(StockEntity stockEntity);
    int insertThumbnail(ThumbnailEntity thumbnailEntity);
    ImageEntity selectImage(
            @Param(value = "id") String id
    );

    ThumbnailEntity selectThumbnail (
            @Param(value = "id") String id
    );

    ProductEntity selectProductByIndex(
            @Param(value = "index") int index);

    int selectProductStock(ProductEntity productEntity);

    ProductDto[] selectProducts();

    ProductDto[] selectProductsByTitle(
            @Param(value = "title") String title
    );

    StockEntity[] selectStocks(
            @Param(value = "productIndex") int productIndex,
            @Param(value = "limit") int limit
    );


    int updateProduct(ProductEntity productEntity);
}
