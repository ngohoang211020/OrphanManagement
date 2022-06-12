package com.orphan.common.entity;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

}
