package com.angeya.pig.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @ Decs:
 * @ Author: angeya
 * @ Date: 2021/3/29
 */

@Configuration
public class AppConfig {

    @Bean
    ExecutorService executorService  () {
        return Executors.newFixedThreadPool(8);
    }
}
