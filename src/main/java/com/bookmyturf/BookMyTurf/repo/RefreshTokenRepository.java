package com.bookmyturf.BookMyTurf.repo;

import com.bookmyturf.BookMyTurf.model.RefreshToken;
import com.bookmyturf.BookMyTurf.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByToken(String token);
    int deleteByUser(User user);
}
