package com.Lycle.Server.dto.Order;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class MakeOrderDto {
    private Long itemId;
    private Integer quantity;
    private Long totalPrice;

    @Builder
    public MakeOrderDto(Long itemId, Integer quantity, Long totalPrice){
        this.itemId = itemId;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
    }


}
