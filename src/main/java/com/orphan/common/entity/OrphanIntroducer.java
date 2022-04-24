package com.orphan.common.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "orphan_introducer")
public class OrphanIntroducer extends BasePeopleEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer introducerId;

    @OneToMany(mappedBy = "orphanIntroducer", cascade = CascadeType.ALL)
    private List<Children> childrens;
}
