package de.hsba.bi.projectwork;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ProjectWorkApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProjectWorkApplication.class, args);
    }

}
