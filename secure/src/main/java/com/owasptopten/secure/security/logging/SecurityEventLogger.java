package com.owasptopten.secure.security.logging;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * Logger for security-related events
 */
@Slf4j
@Component
public class SecurityEventLogger {

    public void logAuthenticationSuccess(String username, String ip) {
        log.info("SECURITY_EVENT: Authentication successful - User: {}, IP: {}", username, ip);
    }

    public void logAuthenticationFailure(String username, String ip, String reason) {
        log.warn("SECURITY_EVENT: Authentication failed - User: {}, IP: {}, Reason: {}", username, ip, reason);
    }

    public void logAuthorizationFailure(String username, String resource, String action) {
        log.warn("SECURITY_EVENT: Authorization failed - User: {}, Resource: {}, Action: {}", username, resource, action);
    }

    public void logSuspiciousActivity(String description, String ip, String details) {
        log.error("SECURITY_EVENT: Suspicious activity detected - Description: {}, IP: {}, Details: {}", description, ip, details);
    }

    public void logPasswordChange(String username, String ip) {
        log.info("SECURITY_EVENT: Password changed - User: {}, IP: {}", username, ip);
    }

    public void logAccountLockout(String username, String ip) {
        log.warn("SECURITY_EVENT: Account locked - User: {}, IP: {}", username, ip);
    }

    public void logSessionCreated(String username, String sessionId, String ip) {
        log.info("SECURITY_EVENT: Session created - User: {}, SessionID: {}, IP: {}", username, sessionId, ip);
    }

    public void logSessionDestroyed(String username, String sessionId) {
        log.info("SECURITY_EVENT: Session destroyed - User: {}, SessionID: {}", username, sessionId);
    }

    public void logCsrfTokenMismatch(String ip, String endpoint) {
        log.warn("SECURITY_EVENT: CSRF token mismatch - IP: {}, Endpoint: {}", ip, endpoint);
    }

    public void logRateLimitExceeded(String ip, String endpoint) {
        log.warn("SECURITY_EVENT: Rate limit exceeded - IP: {}, Endpoint: {}", ip, endpoint);
    }
}
