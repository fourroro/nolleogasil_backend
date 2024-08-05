/*
 * 이 클래스는 redis test를 위한 컨트롤러입니다.
 * @author 박초은
 * @since 2024-01-05
 */
package com.fourroro.nolleogasil_backend.controller.redis;

import com.fourroro.nolleogasil_backend.service.redis.RedisService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class RedisController {

    private final RedisService redisService;

    @GetMapping("/redis")
    public String redisHello() {
        redisService.redisString();
        return "redisHello";
    }
}
