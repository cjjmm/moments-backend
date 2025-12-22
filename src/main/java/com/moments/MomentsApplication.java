package com.moments;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 多媒体展示软件主启动类
 * 
 * @author moments-team
 */
@SpringBootApplication
@MapperScan("com.moments.mapper")
public class MomentsApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(MomentsApplication.class, args);
        System.out.println("\n========================================");
        System.out.println("多媒体展示软件后端启动成功！");
        System.out.println("访问地址：http://localhost:8080");
        System.out.println("========================================\n");
    }
}
