package com.orphan.common.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

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

    private Integer goodQuantity;

    private Integer brokenQuantity;

    @Column(columnDefinition = "text default null")
    private String image;

    private Long unitPrice;

    @OneToMany(mappedBy = "furniture", cascade = CascadeType.ALL)
    private List<SpecifyFurnitureRequest> specifyFurnitureRequestList;
}
