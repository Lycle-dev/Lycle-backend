package com.Lycle.Server.dto.Order;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class RequestOrderDto {
    private String receiver;
    private String address;
    private String telephone;

    @Builder
    public RequestOrderDto(String receiver, String address, String telephone){
        this.receiver = receiver;
        this.address = address;
        this.telephone = telephone;
    }
}
