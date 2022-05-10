package com.orphan.common.entity;

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

    @Column(nullable = false)
    private String status;

    @Temporal(TemporalType.DATE)
    private Date dateOfBirth;

    @Column(columnDefinition = "text default null")
    private String image;

    @Temporal(TemporalType.DATE)
    private Date introductoryDate;

    @Temporal(TemporalType.DATE)
    private Date adoptiveDate;

    @ManyToOne
    @JoinColumn(name = "introducer_id")
    private OrphanIntroducer orphanIntroducer;

    @ManyToOne
    @JoinColumn(name = "nurturer_id")
    private OrphanNurturer orphanNurturer;
}
