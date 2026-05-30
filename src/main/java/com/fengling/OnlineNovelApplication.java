package com.fengling;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class OnlineNovelApplication {

    public static void main(String[] args) {
        SpringApplication.run(OnlineNovelApplication.class, args);
    }

}
