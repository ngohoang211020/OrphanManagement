package com.orphan.common.entity;

import com.orphan.enums.FurnitureStatus;
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

    @Enumerated(EnumType.STRING)
    private FurnitureStatus status;

    private Integer quantity;
}
