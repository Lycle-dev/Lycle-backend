package com.Lycle.Server.dto.Order;

public interface SearchOrderWrapper {
    Long getId();
    Integer getQuantity();
    Long getTotalPrice();
    String getStore();
    String getName();
    Long getPrice();
    String getCreatedDate();
}
