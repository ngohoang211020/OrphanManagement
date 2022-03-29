package com.orphan.common.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Getter
@Setter
@Table(name = "received_childrenInfo")
public class ReceivedChildrenInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer received_id;

    private Date dateReceived;

    @OneToOne
    @JoinColumn(name = "nuturer_id")
    private OrphanNuturer orphanNuturer;

    @OneToOne
    @JoinColumn(name = "children_id")
    private Children children;
}
