package dev.fly_yeseul.coupang.mappers;

import dev.fly_yeseul.coupang.entities.shopping.CartEntity;
import dev.fly_yeseul.coupang.entities.shopping.OrderEntity;
import dev.fly_yeseul.coupang.entities.shopping.OrderStatusEntity;
import dev.fly_yeseul.coupang.entities.shopping.ReviewEntity;
import dev.fly_yeseul.coupang.vos.shopping.cart.AddVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.core.annotation.Order;

@Mapper
public interface IShoppingMapper {

    int deleteCartByEmailAndProductIndex(
            @Param(value = "email") String email,
            @Param(value = "productIndex") int productIndex
    );
    int insertCart(CartEntity cartEntity);

    int insertOrder(OrderEntity orderEntity);

    int insertReview(ReviewEntity reviewEntity);

    CartEntity[] selectCartsByEmail(@Param(value = "email") String email);

    OrderEntity selectOrder(
            @Param(value = "index") int index
    );
    OrderEntity[] selectOrders();

    OrderEntity[] selectOrdersByEmail(
            @Param(value = "email") String email
    );

    OrderStatusEntity[] selectOrderStatuses();
    ReviewEntity[] selectReviewsByProductIndex(
            @Param(value = "productIndex") int productIndex
    );

    int selectReviewCountByOrderIndex(
            @Param(value = "orderIndex") long orderIndex
    );
    int updateOrder(OrderEntity orderEntity);
}
