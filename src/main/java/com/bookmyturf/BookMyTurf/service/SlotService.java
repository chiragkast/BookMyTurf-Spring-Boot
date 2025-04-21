package com.bookmyturf.BookMyTurf.service;

import com.bookmyturf.BookMyTurf.model.Slot;
import com.bookmyturf.BookMyTurf.model.Turf;
import com.bookmyturf.BookMyTurf.repo.SlotRepository;
import com.bookmyturf.BookMyTurf.repo.TurfRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SlotService {

    @Autowired
    private SlotRepository slotRepository;

    @Autowired
    private TurfRepository turfRepository;

    public List<Slot> getAllSlotsByTurf(Long turfId) {
        return slotRepository.findByTurfId(turfId);
    }

    public Slot  addSlot(Long turfId, Slot slot) {
        Turf turf = turfRepository.findById(turfId).orElseThrow(() -> new RuntimeException("Turf not found"));
        slot.setTurf(turf);
        slot.setBooked(true);
        return slotRepository.save(slot);
    }

    public Slot bookSlot(Long slotId) {
        Slot slot = slotRepository.findById(slotId)
                .orElseThrow(() -> new RuntimeException("Slot not found"));

        if (!slot.isBooked()) {
            throw new RuntimeException("Slot already booked");
        }

        slot.setBooked(false);
        return slotRepository.save(slot);
    }
}
