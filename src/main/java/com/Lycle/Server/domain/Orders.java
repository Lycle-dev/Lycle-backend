package com.Lycle.Server.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Getter
public class Orders extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private Long itemId;

    @Column(length = 200, nullable = true)
    private String address;

    @Column(length = 100)
    private String receiver;

    @Column(length = 50)
    private String telephone;

    @Column(length = 10, nullable = false)
    private Integer quantity;

    @Column(nullable = false)
    private Long totalPrice;

    @Builder
    public Orders(Long id, Long userId, Long itemId, String address, String receiver, String telephone, Integer quantity, Long totalPrice) {
        this.id = id;
        this.userId = userId;
        this.itemId = itemId;
        this.address = address;
        this.receiver = receiver;
        this.telephone = telephone;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
    }


    public void updateOrder(String receiver, String address, String telephone){
        this.receiver = receiver;
        this.address = address;
        this.telephone = telephone;
    }
}
