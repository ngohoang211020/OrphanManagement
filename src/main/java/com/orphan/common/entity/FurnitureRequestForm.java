package com.orphan.common.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "furniture_requests")
public class FurnitureRequestForm extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer furnitureRequestId;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "furnitureRequestForm", cascade = CascadeType.MERGE,orphanRemoval = true)
    private List<SpecifyFurnitureRequest> specifyFurnitureRequestList;

    @Column(columnDefinition = "text")
    private String note;

    private Long totalPrice;

    @Temporal(TemporalType.DATE)
    private Date finishDate;

    @Temporal(TemporalType.DATE)
    private Date deadlineDate;

    @Temporal(TemporalType.DATE)
    private Date startDate;

    private String status;
}
