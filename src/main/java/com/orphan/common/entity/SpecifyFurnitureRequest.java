package com.orphan.common.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name = "specify_furniture_requests")
public class SpecifyFurnitureRequest extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer specifyFurnitureRequestId;

    private Integer importQuantity;

    private Integer fixQuantity;

    private String note;

    @ManyToOne
    @JoinColumn(name = "furniture_id")
    private Furniture furniture;
    @ManyToOne
    @JoinColumn(name = "furniture_request_form_id")
    private FurnitureRequestForm furnitureRequestForm;
}
