package com.fourroro.nolleogasil_backend.config;

import feign.Client;
import org.springframework.cloud.openfeign.FeignClientProperties;
import org.springframework.cloud.openfeign.FeignContext;
import org.springframework.cloud.openfeign.Targeter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UsersConfig {
    @Bean
    public Client feignClient(){
        return new Client.Default(null, null);
    }
    @Bean
    public FeignContext feignContext() {
        return new FeignContext();
    }

    @Bean
    public FeignClientProperties feignClientProperties() {
        return new FeignClientProperties();
    }

    @Bean
    public Targeter feignTargeter() {
        return new KakaoClientTargeter();
    }
}