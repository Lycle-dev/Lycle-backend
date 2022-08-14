package com.Lycle.Server.dto.Order;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class MakeOrderDto {
    private Long userId;
    private Long itemId;
    private Integer quantity;
    private Long totalPrice;

    @Builder
    public MakeOrderDto(Long userId, Long itemId, Integer quantity, Long totalPrice){
        this.userId =userId;
        this.itemId = itemId;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
    }


}
