package com.houyaozu.knowledge.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * @ Author     ：侯耀祖
 * @ Description：应用程序启动类
 */
@SpringBootApplication
@EnableAsync
@ComponentScan({"com.houyaozu.knowledge"})
public class AppWebApplication {
    public static void main(String[] args) {
        SpringApplication.run(AppWebApplication.class);
    }
}
