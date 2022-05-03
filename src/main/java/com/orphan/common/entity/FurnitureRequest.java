package com.orphan.common.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name = "furniture_requests")
public class FurnitureRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer furnitureRequestId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "furniture_id")
    private Furniture furniture;

    @Column(columnDefinition = "text")
    private String note;

    private Long totalPrice;

}
