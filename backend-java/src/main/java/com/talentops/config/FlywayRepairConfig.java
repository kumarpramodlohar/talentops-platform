package com.talentops.config;

import lombok.extern.slf4j.Slf4j;
import org.flywaydb.core.Flyway;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import javax.sql.DataSource;

//@Configuration
//@Profile("dev")
@Slf4j
public class FlywayRepairConfig {

    //@Bean
    CommandLineRunner flywayRepair(DataSource dataSource) {
        return args -> {
            Flyway flyway = Flyway.configure()
                    .dataSource(dataSource)
                    .load();
            flyway.repair();
            log.info("Flyway repair complete.");
        };
    }
}
