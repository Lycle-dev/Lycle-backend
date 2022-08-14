package com.Lycle.Server.dto.Order;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class RequestOrderDto {
    private Long id;
    private String address;
    private String telephone;

    @Builder
    public RequestOrderDto(Long id, String address, String telephone){
        this.id = id;
        this.address = address;
        this.telephone = telephone;
    }
}
