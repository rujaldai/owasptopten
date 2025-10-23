# Security Improvements Documentation

This document outlines the comprehensive security improvements implemented in the OWASP Top 10 secure application.

## Overview

The following security enhancements have been implemented to address common web application vulnerabilities:

1. **CSRF Protection**
2. **CORS Configuration**
3. **Rate Limiting**
4. **Security Headers**
5. **Enhanced Error Handling**
6. **Security Event Logging**

---

## 1. CSRF Protection

### Implementation
- **File**: `SecurityConfig.java`
- **Method**: Cookie-based CSRF token repository
- **Configuration**: 
  - CSRF tokens stored in cookies (not HttpOnly to allow JavaScript access)
  - Excluded endpoints: `/actuator/**`, `/api/login`

### How It Works
- Each request that modifies state requires a valid CSRF token
- Token is sent in the `X-CSRF-TOKEN` header
- Prevents Cross-Site Request Forgery attacks

### Usage
```java
// Frontend should include CSRF token in requests
fetch('/api/users', {
  method: 'POST',
  headers: {
    'X-CSRF-TOKEN': getCsrfToken(), // Get from cookie
    'Content-Type': 'application/json'
  },
  body: JSON.stringify(data)
});
```

---

## 2. CORS Configuration

### Implementation
- **File**: `CorsConfig.java`
- **Configurable via**: `application.yml`

### Configuration Options
```yaml
app:
  cors:
    allowed-origins: http://localhost:3000,http://localhost:8080,http://localhost:8081
    allowed-methods: GET,POST,PUT,DELETE,OPTIONS,PATCH
    allowed-headers: authorization,content-type,x-csrf-token,x-requested-with
    exposed-headers: x-csrf-token
    allow-credentials: true
    max-age: 3600
```

### Security Benefits
- Prevents unauthorized cross-origin requests
- Explicitly defines allowed origins (no wildcards in production)
- Supports credentials (cookies, authorization headers)
- Caches preflight requests for performance

---

## 3. Rate Limiting

### Implementation
- **Files**: 
  - `RateLimitService.java` - Core rate limiting logic
  - `RateLimitFilter.java` - Servlet filter
  - `SchedulingConfig.java` - Cleanup scheduler

### Configuration
```yaml
app:
  rate-limit:
    requests-per-minute: 100
    login-requests-per-minute: 5
```

### Features
- **IP-based rate limiting** - Tracks requests per client IP
- **Special limits for login endpoints** - Prevents brute force attacks
- **Automatic cleanup** - Removes old entries every 5 minutes
- **X-Forwarded-For support** - Works behind proxies/load balancers

### Response
When rate limit is exceeded:
```json
{
  "code": "RL429",
  "message": "Too many requests. Please try again later."
}
```
HTTP Status: `429 Too Many Requests`

---

## 4. Security Headers

### Implementation
- **File**: `SecurityConfig.java`

### Headers Configured

#### Content Security Policy (CSP)
```
default-src 'self';
script-src 'self' 'unsafe-inline' 'unsafe-eval';
style-src 'self' 'unsafe-inline';
img-src 'self' data: https:;
font-src 'self' data:;
connect-src 'self';
frame-ancestors 'self';
form-action 'self';
base-uri 'self';
object-src 'none';
```

#### X-Frame-Options
- Value: `SAMEORIGIN`
- Prevents clickjacking attacks

#### X-XSS-Protection
- Value: `1; mode=block`
- Enables browser XSS filtering

#### HTTP Strict Transport Security (HSTS)
- Max Age: 1 year (31536000 seconds)
- Include Subdomains: Yes
- Forces HTTPS connections

#### Referrer Policy
- Value: `strict-origin-when-cross-origin`
- Controls referrer information sent with requests

#### Permissions Policy
- Disabled: `geolocation`, `microphone`, `camera`
- Prevents unauthorized access to browser features

---

## 5. Enhanced Error Handling

### Implementation
- **File**: `ApplicationControllerAdvice.java`

### Features

#### Exception Handlers
1. **ApplicationException** - Application-specific errors
2. **AuthenticationException** - Authentication failures
3. **AccessDeniedException** - Authorization failures
4. **MethodArgumentNotValidException** - Validation errors
5. **Generic Exception** - Catch-all for unexpected errors

#### Error Message Sanitization
Prevents information leakage by:
- Masking sensitive data (passwords, tokens, secrets)
- Hiding SQL query details
- Providing generic error messages to clients

#### Validation Error Response
```json
{
  "code": "V400",
  "message": "Validation failed",
  "errors": {
    "username": "must not be blank",
    "email": "must be a valid email address"
  }
}
```

---

## 6. Security Event Logging

### Implementation
- **File**: `SecurityEventLogger.java`

### Logged Events
- Authentication success/failure
- Authorization failures
- Password changes
- Account lockouts
- Session creation/destruction
- CSRF token mismatches
- Rate limit violations
- Suspicious activities

### Log Format
```
SECURITY_EVENT: [Event Type] - [Details]
```

### Configuration
```yaml
logging:
  level:
    com.owasptopten.secure.security: DEBUG
    org.springframework.security: INFO
  file:
    name: logs/security.log
    max-size: 10MB
    max-history: 30
```

### Example Logs
```
2025-10-23 16:30:00 - SECURITY_EVENT: Authentication successful - User: admin, IP: 192.168.1.100
2025-10-23 16:30:15 - SECURITY_EVENT: Authentication failed - User: unknown, IP: 192.168.1.200, Reason: Invalid credentials
2025-10-23 16:30:30 - SECURITY_EVENT: Rate limit exceeded - IP: 192.168.1.200, Endpoint: /api/login
```

---

## Additional Security Configurations

### Error Codes
New error codes added to `ErrorCode.java`:
- `RATE_LIMIT_EXCEEDED` (RL429)
- `CSRF_TOKEN_INVALID` (CSRF403)
- `VALIDATION_ERROR` (V400)
- `ACCESS_DENIED` (A403)

### Jackson Configuration
- **File**: `JacksonConfig.java`
- Configures JSON serialization/deserialization
- Supports Java 8 date/time types
- Prevents timestamp serialization issues

---

## Testing the Security Features

### 1. Test CSRF Protection
```bash
# Without CSRF token (should fail)
curl -X POST http://localhost:8081/api/users \
  -H "Content-Type: application/json" \
  -d '{"username":"test"}'

# With CSRF token (should succeed)
curl -X POST http://localhost:8081/api/users \
  -H "Content-Type: application/json" \
  -H "X-CSRF-TOKEN: your-csrf-token" \
  -d '{"username":"test"}'
```

### 2. Test Rate Limiting
```bash
# Send multiple requests quickly
for i in {1..10}; do
  curl http://localhost:8081/api/login \
    -H "Content-Type: application/json" \
    -d '{"username":"test","password":"test"}'
done
```

### 3. Test CORS
```javascript
// From a different origin (e.g., http://localhost:3000)
fetch('http://localhost:8081/api/users', {
  method: 'GET',
  credentials: 'include'
})
.then(response => response.json())
.then(data => console.log(data));
```

### 4. Test Security Headers
```bash
curl -I http://localhost:8081/api/users
# Check for headers:
# - Content-Security-Policy
# - X-Frame-Options
# - X-XSS-Protection
# - Strict-Transport-Security
```

---

## Production Recommendations

### 1. CORS Configuration
- Replace `localhost` origins with actual production domains
- Never use wildcard (`*`) for `allowed-origins` in production
- Minimize `allowed-methods` to only what's needed

### 2. Rate Limiting
- Adjust limits based on expected traffic patterns
- Consider using Redis for distributed rate limiting
- Implement different limits for different endpoints

### 3. CSRF Protection
- Ensure frontend properly handles CSRF tokens
- Consider using double-submit cookie pattern for SPAs

### 4. Security Headers
- Review and tighten CSP directives
- Remove `unsafe-inline` and `unsafe-eval` if possible
- Enable HSTS preloading

### 5. Logging
- Implement log aggregation (e.g., ELK stack)
- Set up alerts for security events
- Regularly review security logs
- Ensure logs don't contain sensitive data

### 6. Additional Considerations
- Enable HTTPS (TLS/SSL)
- Implement API versioning
- Add request/response encryption for sensitive data
- Regular security audits and penetration testing
- Keep dependencies up to date

---

## Files Created/Modified

### New Files
1. `/security/config/CorsConfig.java`
2. `/security/config/JacksonConfig.java`
3. `/security/config/SchedulingConfig.java`
4. `/security/ratelimit/RateLimitService.java`
5. `/security/ratelimit/RateLimitFilter.java`
6. `/security/logging/SecurityEventLogger.java`

### Modified Files
1. `/security/config/SecurityConfig.java`
2. `/errorhandling/advice/ApplicationControllerAdvice.java`
3. `/errorhandling/enums/ErrorCode.java`
4. `/resources/application.yml`
5. `/gradle/libs.versions.toml`

---

## Compliance

These security improvements help address the following OWASP Top 10 vulnerabilities:

- **A01:2021 - Broken Access Control** - Enhanced authorization and logging
- **A02:2021 - Cryptographic Failures** - Security headers, HSTS
- **A03:2021 - Injection** - Input validation, error sanitization
- **A04:2021 - Insecure Design** - Rate limiting, CSRF protection
- **A05:2021 - Security Misconfiguration** - Proper security headers, CORS
- **A07:2021 - Identification and Authentication Failures** - Rate limiting on login, security logging
- **A09:2021 - Security Logging and Monitoring Failures** - Comprehensive security event logging

---

## Support

For questions or issues related to these security implementations, please refer to:
- Spring Security Documentation: https://spring.io/projects/spring-security
- OWASP Cheat Sheet Series: https://cheatsheetseries.owasp.org/
