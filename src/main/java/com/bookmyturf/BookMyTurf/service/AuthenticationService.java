package com.bookmyturf.BookMyTurf.service;

import com.bookmyturf.BookMyTurf.dto.LoginResponse;
import com.bookmyturf.BookMyTurf.model.JwtUtil;
import com.bookmyturf.BookMyTurf.model.Role;
import com.bookmyturf.BookMyTurf.model.User;
import com.bookmyturf.BookMyTurf.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

@Service
public class AuthenticationService {

    @Autowired
    private UserRepo userRepository;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private RefreshTokenService refreshTokenService;

//    @Autowired
//    private LoginResponse.LoginResult response;

    public ResponseEntity<Map<String, Object>> registerUser(User user) {
        Map<String, Object> response = new HashMap<>();
        if (userRepository.existsByEmail(user.getEmail()) || userRepository.existsByPhone(user.getPhone())) {
            response.put("statusCode", 409);
            response.put("message", "User with this email/phone already exists!");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        }

        user.setRole(Role.USER); // Default role USER
        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        user.setOtp(generateOtp());
        userRepository.save(user);

        sendOtpEmail(user.getEmail(), user.getOtp());

        response.put("statusCode", 200);
        response.put("message", "User registered successfully! And OTP sent to email!");

        return ResponseEntity.ok(response);
    }

//    public String loginUser(String emailOrPhone, String password) {
//        User user = userRepository.findByEmail(emailOrPhone).orElse(userRepository.findByPhone(emailOrPhone).orElse(null));
//        if (user == null || !user.getPassword().equals(password)) {
//            return "Invalid credentials!";
//        }
//
//        if (!user.isOtpVerified()) {
//            return "OTP not verified!";
//        }
//
//        return "Bearer " + jwtUtil.generateToken(user.getEmail());
//    }

    public LoginResponse loginUser(String emailOrPhone, String password) {
        // Try finding user by email or phone
        Optional<User> optionalUser = userRepository.findByEmail(emailOrPhone);
        if (optionalUser.isEmpty()) {
            optionalUser = userRepository.findByPhone(emailOrPhone);
            if (optionalUser.isEmpty()) {
                return new LoginResponse(401, "Invalid credentials", null); // user not found
            }
        }
        User user = optionalUser.get();

        // Check password
        if(!new BCryptPasswordEncoder().matches(password, user.getPassword())){
            return new LoginResponse(401, "Invalid credentials", null); // wrong password
        }

        // Check OTP verification
        if (!user.isOtpVerified()) {
            return new LoginResponse(403, "OTP not verified", null); // OTP not verified
        }

        // Generate JWT token
//        String token = jwtUtil.generateToken(user.getEmail());
//        String token = "Bearer " + jwtUtil.generateToken(user.getEmail());
        String accessToken = jwtUtil.generateToken(user.getEmail());

        // ✅ Generate Refresh Token
        String refreshToken = refreshTokenService.createRefreshToken(user.getId()).getToken();

        LoginResponse.UserDTO userDTO = new LoginResponse.UserDTO(
                user.getId(),
                user.getEmail(),
                user.getPhone(),
                user.getRole(),
                user.isOtpVerified()
        );

        // Prepare response
        LoginResponse.LoginResult result = new LoginResponse.LoginResult(accessToken, refreshToken, "Bearer", userDTO);

        return new LoginResponse(200, "Login successful", result);
    }


    public String verifyOtp(String email, String otp) {
        User user = userRepository.findByEmail(email).orElse(null);
        if (user == null || !user.getOtp().equals(otp)) {
            return "Invalid OTP!";
        }

        user.setOtpVerified(true);
        userRepository.save(user);
        return "OTP verified!";
    }

    private String generateOtp() {
        Random random = new Random();
        int otp = 100000 + random.nextInt(900000);
        return String.valueOf(otp);
    }

    private void sendOtpEmail(String to, String otp) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Your OTP Code");
        message.setText("Your OTP code is: " + otp);
        mailSender.send(message);
    }
}
