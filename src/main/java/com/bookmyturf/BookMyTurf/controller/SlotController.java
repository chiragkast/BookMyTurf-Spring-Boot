package com.bookmyturf.BookMyTurf.controller;

import com.bookmyturf.BookMyTurf.model.Slot;
import com.bookmyturf.BookMyTurf.service.BookingService;
import com.bookmyturf.BookMyTurf.service.SlotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
@RequestMapping("/api/slots")
public class SlotController {

    @Autowired
    private SlotService slotService;

    @GetMapping("/turf/{turfId}")
    public List<Slot> getSlotsByTurf(@PathVariable Long turfId) {
        return slotService.getAllSlotsByTurf(turfId);
    }

    @PostMapping("/turf/{turfId}/add")
    public Slot addSlot(@PathVariable Long turfId, @RequestBody Slot slot) {
        return slotService.addSlot(turfId, slot);
    }

    @PostMapping("/{slotId}/book")
    public Slot bookSlot(@PathVariable Long slotId) {
        return slotService.bookSlot(slotId);
    }
}
