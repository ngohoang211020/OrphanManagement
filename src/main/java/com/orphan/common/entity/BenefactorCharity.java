package com.orphan.common.entity;

import io.swagger.models.auth.In;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "benefactor_events")
public class BenefactorCharity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer benefactorEventId;

    @ManyToOne
    @JoinColumn(name = "benefactorId")
    private Benefactor benefactor;

    @ManyToOne
    @JoinColumn(name = "charityEventId")
    private CharityEvent charityEvent;

    private Long donation;

    private String status;

    @Column(columnDefinition = "text")
    private String content;
}
