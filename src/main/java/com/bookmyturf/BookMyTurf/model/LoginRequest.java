package com.bookmyturf.BookMyTurf.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
@Data
@NoArgsConstructor
public class LoginRequest {
    private String emailOrPhone;
    private String password;
}
