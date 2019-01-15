package com.bianli24.wikison;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.sql.Connection;

@SpringBootApplication
public class WikisonApplication {

    public static void main(String[] args) {
        System.out.println("start wikison");
        SpringApplication.run(WikisonApplication.class, args);
        JsonUtil.writeVersion();
        DatabaseUtil.getTableInfo();
    }

}

