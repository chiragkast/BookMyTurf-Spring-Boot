package com.bookmyturf.BookMyTurf.controller;

import com.bookmyturf.BookMyTurf.model.Slot;
import com.bookmyturf.BookMyTurf.service.BookingService;
import com.bookmyturf.BookMyTurf.service.SlotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
@RequestMapping("/api/slots")
public class BookingController {

    @Autowired
    private BookingService bookService;

//    @PostMapping("/book/{slotId}")
//    public ResponseEntity<String> bookSlot(@PathVariable Long slotId) {
//        String response = bookService.bookSlot(slotId);
//        return ResponseEntity.ok(response);
//    }
}
