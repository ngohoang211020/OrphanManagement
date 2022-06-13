package com.orphan;

import com.orphan.common.repository.PicnicRepository;
import com.orphan.common.repository.UserRepository;
import com.orphan.common.service.CharityEventService;
import com.orphan.common.service.NotificationService;
import com.orphan.enums.UserStatus;
import com.orphan.exception.NotFoundException;
import java.util.Date;
import javax.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@SpringBootApplication
@EnableScheduling
@ConditionalOnProperty(name = "scheduling.enabled", matchIfMissing = true)
public class OrphanManagementApplication {

    public static void main(String[] args) {
        SpringApplication.run(OrphanManagementApplication.class, args);
    }

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private NotificationService notificationService;

    @Autowired
    private CharityEventService charityEventService;

    @Autowired
    private PicnicRepository picnicRepository;

    @Scheduled(cron = "*/30 * * * * *")
    public void myMethod() throws MessagingException, NotFoundException {
        notificationService.sendMailImmediately();
        notificationService.sendMailAtDateSend();
    }

    @Scheduled(cron = "* 30 * * * *")
    public void myMethod2() {
        userRepository.deleteByRecoveryExpirationDateAndUserStatus(new Date(),
                UserStatus.DELETED.getCode());
        picnicRepository.updateIsCompletedTrue();
        charityEventService.updateIsCompletedTrue();
    }

}
