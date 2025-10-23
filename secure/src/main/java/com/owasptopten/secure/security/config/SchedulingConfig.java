package com.owasptopten.secure.security.config;

import com.owasptopten.secure.security.ratelimit.RateLimitService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Slf4j
@Configuration
@EnableScheduling
@RequiredArgsConstructor
public class SchedulingConfig {

    private final RateLimitService rateLimitService;

    /**
     * Clean up old rate limit entries every 5 minutes
     */
    @Scheduled(fixedRate = 300000) // 5 minutes
    public void cleanupRateLimitCounters() {
        log.debug("Cleaning up rate limit counters");
        rateLimitService.cleanup();
    }
}
