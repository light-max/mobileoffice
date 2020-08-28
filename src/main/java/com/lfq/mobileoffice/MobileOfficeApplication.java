package com.lfq.mobileoffice;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@MapperScan("com.lfq.mobileoffice.mapper")
@EnableScheduling
public class MobileOfficeApplication {

    public static void main(String[] args) {
        SpringApplication.run(MobileOfficeApplication.class, args);
    }

}
