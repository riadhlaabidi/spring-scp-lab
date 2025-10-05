package com.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.app.MyOwnHelloService;
import com.lib.HelloService;

@Configuration
public class MyOwnConfig {

    @Bean
    public HelloService helloService() {
        return new MyOwnHelloService();
    }
}
