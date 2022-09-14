package com.Lycle.Server.domain.jpa;

import com.Lycle.Server.domain.Orders;
import com.Lycle.Server.dto.Order.SearchOrderWrapper;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Orders, Long> {
    @Query(value = "select o.quantity, o.total_price totalPrice, o.created_date createdDate, o.id, i.store, i.name, i.price from orders o, item i " +
            "where o.item_id=i.id and o.user_id=:id and o.order_state=1 order by o.created_date desc", nativeQuery = true)
    List<SearchOrderWrapper>findAllByUserId(Long id);

    @Query(value = "select o.quantity, o.total_price totalPrice, i.store, i.name, i.price from orders o, item i"
            +" where o.item_id=i.id and o.id=:id", nativeQuery = true)
    SearchOrderWrapper findOrderById(Long id);

    @Override
    Optional<Orders> findById(Long id);

}
