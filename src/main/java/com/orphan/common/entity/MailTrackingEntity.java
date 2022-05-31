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

    private LocalDateTime dateSend;

    private Boolean isAllRole=false;

    @Column(columnDefinition = "text")
    private String roles;

    private String type;

    @ManyToOne
    @JoinColumn(name = "feedback_id")
    private FeedbackEntity feedback = null;

}
