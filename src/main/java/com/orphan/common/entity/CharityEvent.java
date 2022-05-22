package com.orphan.common.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
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
    private Integer id;

    @Column(length = 256, nullable = false)
    private String nameCharity;

    @Column(length = 256, nullable = false)
    private String title;

    @Column(columnDefinition = "text")
    private String content;

    private LocalDateTime dateStart;

    private LocalDateTime dateEnd;

    @Column(length = 256)
    private String image;

    private Long money;

    private Boolean isCompleted=false;

    @OneToMany(mappedBy = "charityEvent", cascade = CascadeType.ALL)
    private List<BenefactorCharity> benefactorCharities;
}
