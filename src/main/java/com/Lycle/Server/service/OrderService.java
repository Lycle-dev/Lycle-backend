package com.Lycle.Server.service;

import com.Lycle.Server.domain.Orders;
import com.Lycle.Server.domain.jpa.OrderRepository;
import com.Lycle.Server.dto.Order.RequestOrderDto;
import com.Lycle.Server.dto.Order.SearchOrderWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;

    @Transactional
    public Long makeOrder(RequestOrderDto requestOrderDto) {
        return orderRepository.save(requestOrderDto.toEntity()).getId();
    }

    @Transactional(readOnly = true)
    public List<SearchOrderWrapper> searchOrder(Long id) {
        return orderRepository.findAllByUserIdOrderByCreatedDateDesc(id);
    }

    @Transactional
    public boolean deleteOrder(Long id, Long userId) {
        Orders order = orderRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("존재하는 주문이 아닙니다."));
        if (order.getUserId().equals(userId)) {
            orderRepository.delete(order);
            return true;
        }
        return false;
    }

}
