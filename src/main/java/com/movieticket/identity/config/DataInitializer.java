package com.movieticket.identity.config;

import com.movieticket.identity.domain.*;
import com.movieticket.identity.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private static final Logger log =
            LoggerFactory.getLogger(DataInitializer.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {

        log.info("DataInitializer started...");

        if (userRepository.findByUsername("admin").isEmpty()) {

            User admin = User.builder()
                    .username("admin")
                    .email("roopa.sri@gmail.com")
                    .password(passwordEncoder.encode("admin123"))
                    .role(Role.ADMIN)
                    .active(true)
                    .build();

            userRepository.save(admin);

            log.info("Default admin user created");
        } else {
            log.info("Admin already exists");
        }
    }
}