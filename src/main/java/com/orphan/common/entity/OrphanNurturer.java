package com.orphan.common.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "orphan_nurturer")
public class OrphanNurturer extends BasePeopleEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer nurturerId;

    @OneToMany(mappedBy = "orphanNurturer", cascade = CascadeType.ALL)
    private List<Children> childrens;

}
