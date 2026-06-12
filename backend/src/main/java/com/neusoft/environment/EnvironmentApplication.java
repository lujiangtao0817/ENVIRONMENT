package com.neusoft.environment;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 东软环保公众监督系统 - 启动类
 *
 * @author NEUSOFT
 */
@SpringBootApplication
@MapperScan("com.neusoft.environment.mapper")
public class EnvironmentApplication {

    public static void main(String[] args) {
        SpringApplication.run(EnvironmentApplication.class, args);
        System.out.println("========================================");
        System.out.println("  东软环保公众监督系统启动成功！");
        System.out.println("  http://localhost:8080");
        System.out.println("========================================");
    }
}
