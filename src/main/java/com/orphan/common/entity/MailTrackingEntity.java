package com.orphan.common.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "mail_tracking")
public class MailTrackingEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String recipients;

    private String title;

    @Column(columnDefinition = "text")
    private String content;

    private Boolean isSendImmediately=true;

    private Boolean isCompleted=false;

    private LocalDateTime dateSend=null;
    @Column(name = "opt_counter")
    private Long optCounter;

    private Boolean isAllRole=false;

    @Column(columnDefinition = "text")
    private String roles;


    @ManyToOne
    @JoinColumn(name = "feedback_id")
    private FeedbackEntity feedback;

    @PrePersist
    public void prePersist() {
        this.setOptCounter(0L);
    }

    @PreUpdate
    public void preUpdate() {
        Long counter = this.optCounter;
        counter = counter + 1L;
        this.setOptCounter(counter);
    }
}
