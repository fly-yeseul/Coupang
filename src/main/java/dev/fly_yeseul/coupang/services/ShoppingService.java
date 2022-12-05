package dev.fly_yeseul.coupang.services;

import dev.fly_yeseul.coupang.entities.member.user.UserEntity;
import dev.fly_yeseul.coupang.entities.product.ProductEntity;
import dev.fly_yeseul.coupang.entities.product.StockEntity;
import dev.fly_yeseul.coupang.entities.shopping.CartEntity;
import dev.fly_yeseul.coupang.entities.shopping.OrderEntity;
import dev.fly_yeseul.coupang.entities.shopping.OrderStatusEntity;
import dev.fly_yeseul.coupang.entities.shopping.ReviewEntity;
import dev.fly_yeseul.coupang.enums.shopping.cart.AddResult;
import dev.fly_yeseul.coupang.mappers.IProductMapper;
import dev.fly_yeseul.coupang.mappers.IShoppingMapper;
import dev.fly_yeseul.coupang.vos.shopping.cart.AddVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service(value = "dev.fly_yeseul.coupang.services.ShoppingServices")
public class ShoppingService {
    private final IProductMapper productMapper;
    private final IShoppingMapper shoppingMapper;

    @Autowired
    public ShoppingService(IProductMapper productMapper, IShoppingMapper shoppingMapper) {
        this.productMapper = productMapper;
        this.shoppingMapper = shoppingMapper;
    }


    public void addCart(AddVo addVo) {
        ProductEntity productEntity = this.productMapper.selectProductByIndex(addVo.getProductIndex());
        if (productEntity == null) {
            addVo.setResult(AddResult.NOT_FOUND);
            return;
        }
        if (this.shoppingMapper.insertCart(addVo) == 0) {
            addVo.setResult(AddResult.FAILURE);
            return;
        }
        addVo.setResult(AddResult.SUCCESS);
    }

    public void deleteCartsOf(UserEntity userEntity, int productIndex) {
        this.shoppingMapper.deleteCartByEmailAndProductIndex(userEntity.getEmail(), productIndex);
    }

    public CartEntity[] getCartsOf(UserEntity userEntity) {
        return this.shoppingMapper.selectCartsByEmail(userEntity.getEmail());
    }

    public OrderEntity getOrder(int orderIndex) {
        return this.shoppingMapper.selectOrder(orderIndex);
    }

    public OrderEntity[] getOrdersAll() {
        return this.shoppingMapper.selectOrders();
    }

    public OrderEntity[] getOrdersOf(UserEntity userEntity) {
        return this.shoppingMapper.selectOrdersByEmail(userEntity.getEmail());
    }

    public OrderStatusEntity[] getOrderStatuses() {
        return this.shoppingMapper.selectOrderStatuses();
    }

    public ReviewEntity[] getReviews(ProductEntity productEntity) {
        return this.shoppingMapper.selectReviewsByProductIndex(productEntity.getIndex());

    }

    public boolean modifyOrder(OrderEntity orderEntity) {
        return this.shoppingMapper.updateOrder(orderEntity) > 0;
    }

    @Transactional
    public void orderAll(UserEntity userEntity) {
        CartEntity[] cartEntities = this.shoppingMapper.selectCartsByEmail(userEntity.getEmail());
        Date currentDate = new Date();
        for (CartEntity cartEntity : cartEntities) {
            this.orderSpecific(userEntity, cartEntity.getProductIndex(), cartEntity.getCount(), currentDate);
            this.shoppingMapper.deleteCartByEmailAndProductIndex(userEntity.getEmail(), cartEntity.getProductIndex());


//         OrderEntity orderEntity = new OrderEntity();
//         orderEntity.setCreatedAt(currentDate);
//         orderEntity.setUserEmail(userEntity.getEmail());
//         orderEntity.setProductIndex(cartEntity.getProductIndex());
//         orderEntity.setCount(cartEntity.getCount());
//         orderEntity.setOrderStatusIndex(1);
//         this.shoppingMapper.insertOrder(orderEntity);
//         this.shoppingMapper.deleteCartByEmailAndProductIndex(userEntity.getEmail(), cartEntity.getProductIndex());

//         StockEntity stockEntity = new StockEntity();
//         stockEntity.setCreatedAt(currentDate);
//         stockEntity.setProductIndex(cartEntity.getProductIndex());
//         stockEntity.setCount(cartEntity.getCount() * -1);
//         this.productMapper.insertStock((stockEntity));
//
//         this.shoppingMapper.deleteCartByEmailAndProductIndex(userEntity.getEmail(), cartEntity.getProductIndex());
        }
    }

    public void orderSpecific(UserEntity userEntity, int productIndex, int count) {
        // overriding 이고, date는 현재시간 디폴트로 들어가서 매개변수 안받아도 된당.
        this.orderSpecific(userEntity, productIndex, count, new Date());
    }

    public void orderSpecific(UserEntity userEntity, int productIndex, int count, Date currentDate) {
//        Date currentDate = new Date();

        ProductEntity productEntity = this.productMapper.selectProductByIndex(productIndex);
        int priceProduct = productEntity.getPrice();
        int priceShipping = productEntity.getDeliveryValue().equals("normal") ? 3000 : 0;

        OrderEntity orderEntity = new OrderEntity();
        orderEntity.setCreatedAt(currentDate);
        orderEntity.setUserEmail(userEntity.getEmail());
        orderEntity.setProductIndex(productIndex);
        orderEntity.setCount(count);
        orderEntity.setOrderStatusIndex(1);
        orderEntity.setPriceProduct(priceProduct);
        orderEntity.setPriceShipping(priceShipping);
        this.shoppingMapper.insertOrder(orderEntity);

        StockEntity stockEntity = new StockEntity();
        stockEntity.setCreatedAt(currentDate);
        stockEntity.setProductIndex(productIndex);
        stockEntity.setCount(count * -1);
        this.productMapper.insertStock(stockEntity);
    }

    public void putReview(ReviewEntity reviewEntity){
        this.shoppingMapper.insertReview(reviewEntity);
    }

    public boolean reviewExists(OrderEntity orderEntity) {
        return this.shoppingMapper.selectReviewCountByOrderIndex(orderEntity.getIndex()) > 0;

    }
}
