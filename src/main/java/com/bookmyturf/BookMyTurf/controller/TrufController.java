package com.bookmyturf.BookMyTurf.controller;

import com.bookmyturf.BookMyTurf.model.Turf;
import com.bookmyturf.BookMyTurf.repo.TurfRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
@RequestMapping("/api/turfs")
public class TrufController {
    @Autowired
    private TurfRepository turfRepo;

    @GetMapping
    public List<Turf> getAllTurfs() {
        return turfRepo.findAll();
    }

    @PostMapping
    public Turf addTurf(@RequestBody Turf turf) {
        return turfRepo.save(turf);
    }
}
