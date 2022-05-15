package com.orphan.common.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "benefactors")
public class Benefactor extends BasePeopleEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer benefactorId;

    private Long totalDonation;

    @OneToMany(mappedBy = "benefactor", cascade = CascadeType.ALL)
    private List<BenefactorCharity> benefactorCharities;
}
