package com.orphan.common.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

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

    @Column(columnDefinition = "text default null")
    private String image;

    @Temporal(TemporalType.DATE)
    private Date importDate;

    private Long unitPrice;

    @Column(columnDefinition = "text default null")
    private String note;

    @ManyToOne
    @JoinColumn(name = "categoryId")
    private FurnitureCategory furnitureCategory;
}
