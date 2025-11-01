package com.csbets.vcsbets;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class VcsBetsApplication {

    public static void main(String[] args) {
        SpringApplication.run(VcsBetsApplication.class, args);
    }

}
