package com.deiz0n.studfit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class StudfitApplication {

    public static void main(String[] args) {
        SpringApplication.run(StudfitApplication.class, args);
    }

}
