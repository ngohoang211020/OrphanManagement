package com.orphan.database;

import com.orphan.common.entity.Role;
import com.orphan.common.repository.RoleRepository;
import com.orphan.enums.ERole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class RoleDatabase {
    @Autowired
    private RoleRepository roleRepository;

    @Bean
    CommandLineRunner initDatabase() {
       return new CommandLineRunner() {
            @Override
            public void run(String... args) throws Exception {
                List<Role> roleList = new ArrayList<>();
               roleList.add(new Role(1, ERole.ROLE_ADMIN));
                roleList.add(new Role(2, ERole.ROLE_MANAGER));
                roleRepository.saveAll(roleList);
            }
        };
    }
}
