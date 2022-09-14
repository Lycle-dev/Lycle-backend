package com.Lycle.Server.service;

import com.Lycle.Server.domain.Orders;
import com.Lycle.Server.domain.User.User;
import com.Lycle.Server.domain.jpa.OrderRepository;
import com.Lycle.Server.domain.jpa.UserRepository;
import com.Lycle.Server.dto.Order.MakeOrderDto;
import com.Lycle.Server.dto.Order.RequestOrderDto;
import com.Lycle.Server.dto.Order.SearchOrderWrapper;
import lombok.RequiredArgsConstructor;
import org.json.JSONException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final RewardService rewardService;
    private final UserRepository userRepository;

    @Transactional
    public Long makeOrder(Long id,MakeOrderDto makeOrderDto) {
        return orderRepository.save(Orders.builder()
                        .itemId(makeOrderDto.getItemId())
                        .userId(id)
                        .quantity(makeOrderDto.getQuantity())
                        .totalPrice(makeOrderDto.getTotalPrice())
                .build()).getId();
    }

    @Transactional(readOnly = true)
    public SearchOrderWrapper loadOrderInfo(Long id){
        return orderRepository.findOrderById(id);
    }

    @Transactional
    public Map saveOrder(Long id, Long orderId ,RequestOrderDto requestOrderDto) throws JSONException, IOException {
        Map<String,Long> orderInfo = new HashMap<>();

        User me = userRepository.findById(id).orElseThrow(()-> new IllegalArgumentException("존재하지 않는 회원 입니다."));

        Orders order = orderRepository.findById(orderId)
                .orElseThrow(()-> new IllegalArgumentException("존재하지 않는 주문 입니다."));
        Long pointInfo = rewardService.transferReward(me.getEmail(),"admin@lycle.com", order.getTotalPrice().intValue());

        order.updateOrder(requestOrderDto.getReceiver(), requestOrderDto.getAddress(), requestOrderDto.getTelephone());
        order.updateState(true);

        orderInfo.put("orderId", order.getId());
        orderInfo.put("pointInfo", pointInfo);

        order.updateState(true);

        return orderInfo;
    }


    @Transactional(readOnly = true)
    public List<SearchOrderWrapper> searchAllOrder(Long id) {
        return orderRepository.findAllByUserId(id);
    }

    @Transactional
    public boolean deleteOrder(Long userId, Long orderId) throws JSONException, IOException {
        Orders order = orderRepository.findById(orderId).orElseThrow(() -> new IllegalArgumentException("존재하는 주문이 아닙니다."));
        User me = userRepository.findById(userId).orElseThrow(()-> new IllegalArgumentException("존재하지 않는 회원 입니다."));
        //주문을 삭제하고 다시 포인트 회수
        if (order.getUserId().equals(userId)) {
            orderRepository.delete(order);
            Long point = rewardService.transferReward("admin@lycle.com",me.getEmail(),order.getTotalPrice().intValue());
            return true;
        }
        return false;
    }

}
