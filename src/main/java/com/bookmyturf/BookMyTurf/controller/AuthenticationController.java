package com.bookmyturf.BookMyTurf.controller;

import com.bookmyturf.BookMyTurf.dto.LoginResponse;
import com.bookmyturf.BookMyTurf.dto.TokenRefreshRequest;
import com.bookmyturf.BookMyTurf.dto.TokenRefreshResponse;
import com.bookmyturf.BookMyTurf.exception.TokenRefreshException;
import com.bookmyturf.BookMyTurf.model.*;
import com.bookmyturf.BookMyTurf.repo.RefreshTokenRepository;
import com.bookmyturf.BookMyTurf.repo.UserRepo;
import com.bookmyturf.BookMyTurf.service.AuthenticationService;
import com.bookmyturf.BookMyTurf.service.RefreshTokenService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
public class AuthenticationController {

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private RefreshTokenService refreshTokenService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/register")
    public  ResponseEntity<Map<String, Object>> register(@RequestBody User user) {
        return authenticationService.registerUser(user);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok(authenticationService.loginUser(loginRequest.getEmailOrPhone(), loginRequest.getPassword()));
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<String> verifyOtp(@RequestBody OtpRequest otpRequest) {
        return ResponseEntity.ok(authenticationService.verifyOtp(otpRequest.getEmail(), otpRequest.getOtp()));
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<?> refreshToken(@RequestBody TokenRefreshRequest request) {
        String requestRefreshToken = request.getRefreshToken();

        return refreshTokenRepository.findByToken(requestRefreshToken)
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshToken::getUser)
                .map(user -> {
                    String newToken = jwtUtil.generateToken(user.getEmail());
                    return ResponseEntity.ok(new TokenRefreshResponse(newToken, requestRefreshToken, "Bearer"));
                })
                .orElseThrow(() -> new TokenRefreshException(requestRefreshToken, "Refresh token not found."));
    }


    @Transactional
    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletResponse response) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Map<String, Object> logoutResponse = new HashMap<>();
        if (authentication == null || !authentication.isAuthenticated()) {
            logoutResponse.put("statusCode", 401);
            logoutResponse.put("message", "User not authenticated");
            logoutResponse.put("data", null);
            return ResponseEntity.status(401).body(logoutResponse);
        }

        // Get the user's email or username from the token
        String email = authentication.getName(); // this works if JWT sets email/username as principal

        // Fetch user from DB
        User user = userRepo.findByEmail(email)
                .orElse(null);

        if (user == null) {
            logoutResponse.put("statusCode", 403);
            logoutResponse.put("message", "User not found");
            logoutResponse.put("data", null);
            return ResponseEntity.status(403).body(logoutResponse);
        }

        // Delete refresh token from DB
        refreshTokenRepository.deleteByUser(user);

        // Clear cookies
        ResponseCookie accessTokenCookie = ResponseCookie.from("accessToken", "")
                .path("/")
                .httpOnly(true)
                .secure(true)
                .maxAge(0)
                .build();

        ResponseCookie refreshTokenCookie = ResponseCookie.from("refreshToken", "")
                .path("/")
                .httpOnly(true)
                .secure(true)
                .maxAge(0)
                .build();

        response.addHeader("Set-Cookie", accessTokenCookie.toString());
        response.addHeader("Set-Cookie", refreshTokenCookie.toString());

        logoutResponse.put("statusCode", 200);
        logoutResponse.put("message", "Logout successful");
        logoutResponse.put("timestamp", Instant.now());

        return ResponseEntity.ok(logoutResponse);
    }
}
