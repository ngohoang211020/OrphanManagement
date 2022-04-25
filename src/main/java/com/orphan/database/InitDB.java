package com.orphan.database;

import com.orphan.common.entity.Role;
import com.orphan.common.repository.RoleRepository;
import com.orphan.common.repository.UserRepository;
import com.orphan.enums.ERole;
import com.orphan.enums.UserStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Configuration
public class InitDB {
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private UserRepository userRepository;

    @Bean
    CommandLineRunner initDatabase() {
        return new CommandLineRunner() {
            @Override
            public void run(String... args) throws Exception {
//                List<Role> roleList = new ArrayList<>();
//                roleList.add(new Role(1, ERole.ROLE_ADMIN.getCode(),"Quản trị viên"));
//                roleList.add(new Role(2, ERole.ROLE_EMPLOYEE.getCode(),"Nhân viên"));
//                roleList.add(new Role(3, ERole.ROLE_MANAGER_LOGISTIC.getCode(),"Quản lý hoạt động trung tâm"));
//                roleList.add(new Role(4, ERole.ROLE_MANAGER_HR.getCode(),"Quản lý nhân sự"));
//                roleList.add(new Role(5, ERole.ROLE_MANAGER_CHILDREN.getCode(),"Quản lý trẻ em"));
//                roleRepository.saveAll(roleList);
//                userRepository.deleteByRecoveryExpirationDateAndUserStatus(new Date(), UserStatus.DELETED.getCode());

            }
        };
    }
}
