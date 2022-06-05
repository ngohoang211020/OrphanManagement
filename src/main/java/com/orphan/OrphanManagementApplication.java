package com.orphan;

import com.orphan.common.repository.UserRepository;
import com.orphan.common.service.NotificationService;
import com.orphan.enums.UserStatus;
import com.orphan.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import javax.mail.MessagingException;
import java.util.Date;

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
//    @Autowired
//    private FurnitureRequestFormService furnitureRequestFormService;

    @Scheduled(fixedRate = 10000L) // this method will be executed as 12:00:00 AM of every friday
    public void myMethod() throws MessagingException, NotFoundException {
        userRepository.deleteByRecoveryExpirationDateAndUserStatus(new Date(), UserStatus.DELETED.getCode());
//        furnitureRequestFormService.updateAutoFurnitureForm();
        // do your logic
        notificationService.sendMailImmediately();
        notificationService.sendMailAtDateSend();
    }

//    @Scheduled(fixedDelay = 1000*60*60*24)
//    public void fundInfundOut(){
//        furnitureRequestFormService.updateFundInFundOut();
//    }
}
