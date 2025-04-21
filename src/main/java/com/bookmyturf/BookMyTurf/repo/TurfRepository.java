package com.bookmyturf.BookMyTurf.repo;

import com.bookmyturf.BookMyTurf.model.Turf;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TurfRepository extends JpaRepository<Turf, Long> {
}
