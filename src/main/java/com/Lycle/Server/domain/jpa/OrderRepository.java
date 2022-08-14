package com.Lycle.Server.domain.jpa;


import com.Lycle.Server.domain.Orders;
import com.Lycle.Server.dto.Order.SearchOrderWrapper;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Orders, Long> {
    @Query(value = "select o.quantity, o.total_price totalPrice, i.name, i.price, i.store" +
            " from orders o left JOIN item i on o.item_id=i.id where o.user_id=:userId" +
            "order by o.created_date createdDate desc", nativeQuery = true)
    List<SearchOrderWrapper>findAllByUserIdOrderByCreatedDateDesc(Long userId);

    @Query(value = "select o.quantity, o.total_price totalPrice, i.name, i.price, i.store" +
            " from orders o left JOIN item i on o.item_id=i.id", nativeQuery = true)
    Optional<SearchOrderWrapper> findOrdersById(Long id);

    @Override
    Optional<Orders> findById(Long id);
}
