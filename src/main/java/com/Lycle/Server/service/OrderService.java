package com.Lycle.Server.service;

import com.Lycle.Server.domain.Orders;
import com.Lycle.Server.domain.jpa.OrderRepository;
import com.Lycle.Server.dto.Order.MakeOrderDto;
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
    public Long makeOrder(MakeOrderDto makeOrderDto) {
        return orderRepository.save(Orders.builder()
                        .itemId(makeOrderDto.getItemId())
                        .userId(makeOrderDto.getUserId())
                        .quantity(makeOrderDto.getQuantity())
                        .totalPrice(makeOrderDto.getTotalPrice())
                .build()).getId();
    }

    @Transactional(readOnly = true)
    public SearchOrderWrapper loadOrderInfo(Long id){
        return orderRepository.findOrdersById(id).orElseThrow(()->
                new IllegalArgumentException("존재하지 않는 주문 입니다."));
    }

    @Transactional
    public Long saveOrder(RequestOrderDto requestOrderDto){
        Orders order = orderRepository.findById(requestOrderDto.getId())
                .orElseThrow(()-> new IllegalArgumentException("존재하지 않는 주문 입니다."));

        order.updateOrder(requestOrderDto.getAddress(), requestOrderDto.getTelephone());
        return order.getId();
    }


    @Transactional(readOnly = true)
    public List<SearchOrderWrapper> searchAllOrder(Long id) {
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
