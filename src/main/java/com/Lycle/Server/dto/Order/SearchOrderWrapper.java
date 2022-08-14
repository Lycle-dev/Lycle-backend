package com.Lycle.Server.dto.Order;

public interface SearchOrderWrapper {
    Integer getQuantity();
    Long getTotalPrice();
    String getStore();
    String getName();
    Long getPrice();
}
