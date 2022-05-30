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
@Table(name = "feedbacks")
public class FeedbackEntity extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 128, nullable = false)
    private String fullName;

    private String email;

    private String title;

    private Boolean isReplied=false;

    private LocalDateTime dateReply=null;

    @Column(columnDefinition = "text")
    private String content;

    @OneToMany(mappedBy = "feedback", cascade = CascadeType.ALL)
    private List<MailTrackingEntity> mailTrackingEntities;
}
