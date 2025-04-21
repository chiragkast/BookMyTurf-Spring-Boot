package com.bookmyturf.BookMyTurf.dto;

import com.bookmyturf.BookMyTurf.model.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponse {
    private int statusCode;
    private String message;
    private LoginResult result;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class LoginResult {
//        private String token;
//        private UserDTO user;

        private String accessToken;
        private String refreshToken;
        private String tokenType = "Bearer";
        private UserDTO user;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UserDTO {
        private Long id;
        private String email;
        private String phone;
        private Role role;
        private boolean isOtpVerified;
    }
}
