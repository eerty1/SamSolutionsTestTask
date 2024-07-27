package com.samsolutions;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class SamSolutionsTestTaskApplication {

    public static void main(String[] args) {
        SpringApplication.run(SamSolutionsTestTaskApplication.class, args);
    }
}
