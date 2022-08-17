package com.Lycle.Server.controller;

import com.Lycle.Server.config.auth.UserPrincipal;
import com.Lycle.Server.dto.BasicResponse;
import com.Lycle.Server.dto.Order.MakeOrderDto;
import com.Lycle.Server.dto.Order.RequestOrderDto;
import com.Lycle.Server.service.OrderService;
import com.Lycle.Server.service.UserService;
import lombok.RequiredArgsConstructor;
import org.json.JSONException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;
    private final UserService userService;

    //주문 생성
    @PostMapping("/user/order")
    public ResponseEntity<BasicResponse> makeOrder(Authentication authentication, @RequestBody MakeOrderDto makeOrderDto){
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        BasicResponse orderResponse = BasicResponse.builder()
                .code(HttpStatus.CREATED.value())
                .httpStatus(HttpStatus.CREATED)
                .message("주문이 생성되었습니다.")
                .count(1)
                .result(Collections.singletonList(orderService.makeOrder(userPrincipal.getId(), makeOrderDto)))
                .build();

        return new ResponseEntity<>(orderResponse, orderResponse.getHttpStatus());
    }

    //생성중인 주문의 정보 조회
    @GetMapping("/user/order/{id}")
    public ResponseEntity<BasicResponse> getOrderInfo(Authentication authentication, @PathVariable Long id){
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        Long userId = userPrincipal.getId();

        //HaspMap 생성하여 사용자 정보와 주문 관련 정보 저장
        Map<String,Object> orderInfo = new HashMap<>();

        orderInfo.put("userProfile",userService.searchProfile(userId));
        orderInfo.put("itemInfo", orderService.loadOrderInfo(id));

        //응답 생성
        BasicResponse orderResponse = BasicResponse.builder()
                .code(HttpStatus.OK.value())
                .httpStatus(HttpStatus.OK)
                .message("생성 중인 주문 정보입니다.")
                .result(Collections.singletonList(orderInfo))
                .build();

        return new ResponseEntity<>(orderResponse, orderResponse.getHttpStatus());
    }

    //주문 완료
    @PutMapping("/user/order/{id}")
    public ResponseEntity<BasicResponse> saveOrder(Authentication authentication,@PathVariable Long id, @RequestBody RequestOrderDto requestOrderDto) throws JSONException, IOException {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        BasicResponse orderResponse = BasicResponse.builder()
                .code(HttpStatus.OK.value())
                .httpStatus(HttpStatus.OK)
                .message("주문이 완료되었습니다.")
                .result(Collections.singletonList(orderService.saveOrder(userPrincipal.getId(), id, requestOrderDto)))
                .build();

        return new ResponseEntity<>(orderResponse, orderResponse.getHttpStatus());
    }



    //완료 주문 조회
    @GetMapping("/user/order")
    public ResponseEntity<BasicResponse> searchOrder(Authentication authentication){
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        BasicResponse searchOrder = BasicResponse.builder()
                .code(HttpStatus.OK.value())
                .httpStatus(HttpStatus.OK)
                .message("주문 조회가 완료되었습니다.")
                .count(orderService.searchAllOrder(userPrincipal.getId()).size())
                .result(Collections.singletonList(orderService.searchAllOrder(userPrincipal.getId())))
                .build();

        return new ResponseEntity<>(searchOrder, searchOrder.getHttpStatus());

    }


    //주문 삭제
    @DeleteMapping("/user/order/{id}")
    public ResponseEntity<BasicResponse> deleteOrder(Authentication authentication, @PathVariable Long id) throws JSONException, IOException {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        orderService.deleteOrder(userPrincipal.getId(), id);
        BasicResponse orderResponse = BasicResponse.builder()
                .code(HttpStatus.OK.value())
                .httpStatus(HttpStatus.OK)
                .message("주문이 취소 되었습니다.")
                .build();

        return new ResponseEntity<>(orderResponse, orderResponse.getHttpStatus());
    }
}
