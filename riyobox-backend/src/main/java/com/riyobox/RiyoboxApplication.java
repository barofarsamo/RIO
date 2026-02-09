package com.riyobox;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

@SpringBootApplication
@EnableMongoAuditing
public class RiyoboxApplication {
    public static void main(String[] args) {
        SpringApplication.run(RiyoboxApplication.class, args);
    }
}
