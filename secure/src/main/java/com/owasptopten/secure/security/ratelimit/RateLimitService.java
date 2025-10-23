package com.owasptopten.secure.security.ratelimit;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Service for rate limiting requests
 */
@Slf4j
@Service
public class RateLimitService {

    @Value("${app.rate-limit.requests-per-minute:100}")
    private int requestsPerMinute;

    @Value("${app.rate-limit.login-requests-per-minute:5}")
    private int loginRequestsPerMinute;

    private final Map<String, RequestCounter> requestCounters = new ConcurrentHashMap<>();
    private final Map<String, RequestCounter> loginCounters = new ConcurrentHashMap<>();

    public boolean allowRequest(String clientIp, String requestUri) {
        // Special rate limit for login endpoints
        if (requestUri.contains("/login") || requestUri.contains("/api/login")) {
            return checkRateLimit(loginCounters, clientIp, loginRequestsPerMinute);
        }
        
        // General rate limit
        return checkRateLimit(requestCounters, clientIp, requestsPerMinute);
    }

    private boolean checkRateLimit(Map<String, RequestCounter> counters, String key, int maxRequests) {
        RequestCounter counter = counters.computeIfAbsent(key, k -> new RequestCounter());
        
        synchronized (counter) {
            Instant now = Instant.now();
            
            // Reset counter if window has passed
            if (Duration.between(counter.windowStart, now).toMinutes() >= 1) {
                counter.count = 0;
                counter.windowStart = now;
            }
            
            // Check if limit exceeded
            if (counter.count >= maxRequests) {
                return false;
            }
            
            counter.count++;
            return true;
        }
    }

    /**
     * Clean up old entries periodically
     */
    public void cleanup() {
        Instant cutoff = Instant.now().minus(Duration.ofMinutes(5));
        
        requestCounters.entrySet().removeIf(entry -> 
            entry.getValue().windowStart.isBefore(cutoff)
        );
        
        loginCounters.entrySet().removeIf(entry -> 
            entry.getValue().windowStart.isBefore(cutoff)
        );
    }

    private static class RequestCounter {
        int count = 0;
        Instant windowStart = Instant.now();
    }
}
