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

    @Column(nullable = false, unique = true)
    private String status;

    private Integer quantity;

    @Column(length = 128)
    private String image;

    @Lob
    @Column(name="prof_pic")
    private byte[] profPic;
}
