package com.orphan.common.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "info_system")
public class InformationSystem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String nameOfSystem;

    private Integer amountOfUser;

    private Integer amountOfChildren;

    private Integer amountOfChildrenAdopted;

    private Long fund;

    private Integer amountOfIntroducer;

    private Integer amountOfNurturer;

}
