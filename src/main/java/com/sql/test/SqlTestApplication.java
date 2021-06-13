package com.sql.test;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.sql.test"})
public class SqlTestApplication {

    public static void main(String[] args) {
        SpringApplication.run(SqlTestApplication.class, args);
    }

}
