package com.orphan.common.entity;

import com.orphan.enums.TypeStaff;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class Staff extends BasePeopleEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer staff_id;

    @Enumerated(EnumType.STRING)
    private TypeStaff typeStaff;

}
