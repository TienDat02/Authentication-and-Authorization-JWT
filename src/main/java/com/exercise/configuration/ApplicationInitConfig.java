package com.exercise.configuration;

import com.exercise.entity.MyUser;
import com.exercise.repository.MyUserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;

@Configuration
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ApplicationInitConfig {
    PasswordEncoder passwordEncoder;

    @NonFinal
    static final String ADMIN_USER_NAME = "admin";

    @NonFinal
    static final String ADMIN_PASSWORD = "admin";

    @Bean
    @ConditionalOnProperty(
            prefix = "spring",
            value = "datasource.driverClassName",
            havingValue = "org.postgresql.Driver")
    ApplicationRunner applicationRunner(MyUserRepository userRepository) {

        log.info("Initializing application.....");
        return args -> {
            if (userRepository.findByUsername(ADMIN_USER_NAME).isEmpty()) {
                MyUser newUser = new MyUser(ADMIN_USER_NAME, "" ,passwordEncoder.encode(ADMIN_PASSWORD));
                newUser.addRole("ADMIN");
                newUser.setPermissions(Set.of("READ", "UPDATE","CREATE", "DELETE"));
                userRepository.save(newUser);
                log.warn("admin user has been created with default password: admin, please change it");
            }
            log.info("Application initialization completed .....");


        };
    }
}
