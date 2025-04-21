package com.bookmyturf.BookMyTurf.service;

import com.bookmyturf.BookMyTurf.model.Slot;
import com.bookmyturf.BookMyTurf.repo.SlotRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class BookingService{
    @Autowired
    private SlotRepository slotRepository;

    public String bookSlot(Long slotId) {
        Optional<Slot> slot = slotRepository.findById(slotId);
        if (slot.isPresent() && !slot.get().isBooked()) {
            slot.get().setBooked(true);
            slotRepository.save(slot.get());
            return "Booking successful!";
        }
        return "Slot already booked!";
    }
}
