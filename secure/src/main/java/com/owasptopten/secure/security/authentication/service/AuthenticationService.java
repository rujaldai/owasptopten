package com.owasptopten.secure.security.authentication.service;

import com.owasptopten.secure.security.JwtService;
import com.owasptopten.secure.security.cookies.SecureCookies;
import com.owasptopten.secure.security.dto.LoginRequest;
import com.owasptopten.secure.userdetails.domain.User;
import com.owasptopten.secure.userdetails.dto.UserDetailDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final JwtService jwtService;

    // Rate limiting: Track failed login attempts
    private final ConcurrentHashMap<String, AtomicInteger> failedLoginAttempts = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, Long> lockoutTimestamps = new ConcurrentHashMap<>();
    private static final int MAX_FAILED_ATTEMPTS = 5;
    private static final long LOCKOUT_DURATION_MINUTES = 15;

    public UserDetailDto authenticate(HttpServletResponse response, LoginRequest loginRequest) {
        // Check for account lockout
        checkAccountLockout(loginRequest.username());

        try {
            UsernamePasswordAuthenticationToken authRequest = 
                UsernamePasswordAuthenticationToken.unauthenticated(loginRequest.username(), loginRequest.password());
            
            Authentication authentication = authenticationManager.authenticate(authRequest);
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // Reset failed attempts on successful login
            failedLoginAttempts.remove(loginRequest.username());
            lockoutTimestamps.remove(loginRequest.username());

            User user = (User) authentication.getPrincipal();
            String token = jwtService.generateToken(user);
            String refreshToken = jwtService.generateRefreshToken(user);

            // Set secure cookies
            SecureCookies.createSecureCookie("token", token, Duration.ofMinutes(15));
            SecureCookies.createSecureCookie("refreshToken", refreshToken, Duration.ofDays(7));
            SecureCookies.addSecurityHeaders(response);

            return UserDetailDto.of(user);
        } catch (BadCredentialsException e) {
            // Increment failed attempts
            AtomicInteger attempts = failedLoginAttempts.computeIfAbsent(loginRequest.username(), k -> new AtomicInteger(0));
            int currentAttempts = attempts.incrementAndGet();

            if (currentAttempts >= MAX_FAILED_ATTEMPTS) {
                lockoutTimestamps.put(loginRequest.username(), System.currentTimeMillis());
                throw new LockedException("Account locked due to too many failed attempts. Try again in " + LOCKOUT_DURATION_MINUTES + " minutes.");
            }

            throw new BadCredentialsException("Invalid username or password. " + (MAX_FAILED_ATTEMPTS - currentAttempts) + " attempts remaining.");
        }
    }

    public void authenticate(HttpServletRequest request, String token) {
        if (token == null || !jwtService.isTokenValid(token)) {
            return;
        }

        String username = jwtService.extractUsername(token);
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        if (jwtService.isTokenValid(token, userDetails)) {
            UsernamePasswordAuthenticationToken authentication = 
                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
    }

    public void logout(HttpServletRequest request, HttpServletResponse response) {
        // Clear security context
        SecurityContextHolder.clearContext();

        // Remove cookies securely
        SecureCookies.removeCookie("token", response);
        SecureCookies.removeCookie("refreshToken", response);
        SecureCookies.addSecurityHeaders(response);
    }

    private void checkAccountLockout(String username) {
        Long lockoutTimestamp = lockoutTimestamps.get(username);
        if (lockoutTimestamp != null) {
            long elapsedMinutes = (System.currentTimeMillis() - lockoutTimestamp) / (60 * 1000);
            if (elapsedMinutes < LOCKOUT_DURATION_MINUTES) {
                throw new LockedException("Account is locked. Try again in " + (LOCKOUT_DURATION_MINUTES - elapsedMinutes) + " minutes.");
            } else {
                // Lockout period expired, reset counters
                failedLoginAttempts.remove(username);
                lockoutTimestamps.remove(username);
            }
        }
    }
} 