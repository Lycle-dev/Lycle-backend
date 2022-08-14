package com.Lycle.Server.dto.Order;

public interface SearchOrderWrapper {
    int getQuantity();
    Long getTotalPrice();
    String getStore();
    String getName();
    Long getPrice();
}
