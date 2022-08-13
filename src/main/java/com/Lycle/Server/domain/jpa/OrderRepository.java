package com.Lycle.Server.domain.jpa;


import com.Lycle.Server.domain.Orders;
import com.Lycle.Server.dto.Order.SearchOrderWrapper;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrderRepository extends JpaRepository<Orders, Long> {
    @Query(value = "select o.quantity, o.totalPrice, i.name, i.price, i.store" +
            " from orders o left JOIN Item i on o.itemId=i.id " +
            "order by o.createdDate desc", nativeQuery = true)
    List<SearchOrderWrapper>findOrdersByUserIdOrderByDateDesc(Long userId);
}
