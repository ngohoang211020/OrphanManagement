package com.orphan.common.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "charity_event")
public class CharityEvent extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer charityEventId;

    @Column(length = 256, nullable = false)
    private String charityName;

    @Column(length = 256, nullable = false)
    private String title;

    @Column(columnDefinition = "text")
    private String content;

    @Temporal(TemporalType.DATE)
    private Date dateOfEvent;

    @Column(length = 256)
    private String image;

    private Long money;

    private String status;

    @OneToMany(mappedBy = "charityEvent", cascade = CascadeType.ALL)
    private List<BenefactorCharity> benefactorCharities;
}
