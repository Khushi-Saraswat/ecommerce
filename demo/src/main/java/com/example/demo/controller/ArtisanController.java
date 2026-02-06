package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.request.ArtisanRequestDTO;
import com.example.demo.service.methods.AartisanService;

@RestController
@RequestMapping("/api/artisans")
public class ArtisanController {

    @Autowired
    private AartisanService aartisanService;

    @PreAuthorize("hasRole('ARTISAN')")
    @PostMapping("/apply")
    public ResponseEntity<?> SaveArtisan(@RequestBody ArtisanRequestDTO artisanRequestDTO) {

        return ResponseEntity.ok(aartisanService.SaveArtisanDetails(artisanRequestDTO));

    }

    @GetMapping("/getArtisan")
    public ResponseEntity<?> getArtisanDetails(@RequestHeader("Authorization") String jwt) {

        return ResponseEntity.ok(aartisanService.getArtisanDetails(jwt));
    }

    @PostMapping("/updateArtisan")
    public ResponseEntity<?> UpdateArtisanDetails(@RequestBody ArtisanRequestDTO artisanRequestDTO,
            @RequestHeader("Authorization") String jwt) {

        return ResponseEntity.ok(aartisanService.UpdateArtisanDetails(artisanRequestDTO, jwt));

    }

}
