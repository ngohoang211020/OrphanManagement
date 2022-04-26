package com.orphan.common.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name = "furnitures")
public class Furniture extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer furnitureId;

    @Column(nullable = false)
    private String furnitureName;

    @Column(nullable = false)
    private String status;

    private Integer quantity;

    @Column(length = 256, columnDefinition = "default null")
    private String image;

}
