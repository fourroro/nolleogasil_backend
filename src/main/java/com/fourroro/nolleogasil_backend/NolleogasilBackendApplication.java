package com.fourroro.nolleogasil_backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
@EnableFeignClients
@EnableRedisHttpSession
public class NolleogasilBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(NolleogasilBackendApplication.class, args);
    }

}
