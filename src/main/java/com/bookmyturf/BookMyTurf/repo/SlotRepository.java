package com.bookmyturf.BookMyTurf.repo;

import com.bookmyturf.BookMyTurf.model.Slot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SlotRepository extends JpaRepository<Slot, Long> {
    List<Slot> findByTurfId(Long turfId);
}
