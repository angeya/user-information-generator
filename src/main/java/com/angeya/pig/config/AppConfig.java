package com.angeya.pig.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.*;

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
