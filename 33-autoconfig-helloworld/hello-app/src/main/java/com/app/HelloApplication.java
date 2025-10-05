package com.app;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.lib.HelloService;

@SpringBootApplication
// @Import({ MyOwnConfig.class })
public class HelloApplication {

    public static void main(String[] args) {
        SpringApplication.run(HelloApplication.class, args);
    }

    // @Bean
    // public HelloService helloService() {
    // return new TypicalHelloService();
    // }

    @Bean
    public CommandLineRunner commandLineRunner(HelloService helloService) {
        return args -> helloService.greet();
    }

}
