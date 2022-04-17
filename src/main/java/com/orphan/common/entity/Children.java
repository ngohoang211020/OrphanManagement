package com.orphan.common.entity;

import com.orphan.enums.ChildrenStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "childrens")
public class Children extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 128, nullable = false)
    private String fullName;

    private Boolean gender;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ChildrenStatus status;

    @Temporal(TemporalType.DATE)
    private Date dateOfBirth;

    @Column(length = 256)
    private String image;

    @Temporal(TemporalType.DATE)
    private Date adoptiveDate;

    @ManyToOne
    @JoinColumn(name = "introducer_id")
    private OrphanIntroducer orphanIntroducer;

    @ManyToOne
    @JoinColumn(name = "nurturer_id")
    private OrphanNurturer orphanNurturer;
}
