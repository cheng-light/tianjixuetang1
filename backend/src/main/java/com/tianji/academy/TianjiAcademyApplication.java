package com.tianji.academy;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.tianji.academy.mapper")
public class TianjiAcademyApplication {
    public static void main(String[] args) {
        SpringApplication.run(TianjiAcademyApplication.class, args);
    }
}
