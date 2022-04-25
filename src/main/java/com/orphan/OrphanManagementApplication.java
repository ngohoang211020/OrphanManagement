package com.orphan;

import com.orphan.common.repository.UserRepository;
import com.orphan.enums.UserStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

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

    @Scheduled(fixedRate = 10000L) // this method will be executed as 12:00:00 AM of every friday
    public void myMethod() {
        userRepository.deleteByRecoveryExpirationDateAndUserStatus(new Date(), UserStatus.DELETED.getCode());
        // do your logic
    }
}
