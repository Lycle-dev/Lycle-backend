package com.Lycle.Server.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Getter
@DynamicInsert
@DynamicUpdate
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
    @ColumnDefault("0")
    private boolean orderState;

    @Builder
    public Orders(Long id, Long userId, Long itemId, String address, String receiver, String telephone, Integer quantity, Long totalPrice, boolean orderState) {
        this.id = id;
        this.userId = userId;
        this.itemId = itemId;
        this.address = address;
        this.receiver = receiver;
        this.telephone = telephone;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
        this.orderState = orderState;
    }


    public void updateOrder(String receiver, String address, String telephone){
        this.receiver = receiver;
        this.address = address;
        this.telephone = telephone;
    }

    public void updateState(boolean orderState){
        this.orderState = orderState;
    }
}
