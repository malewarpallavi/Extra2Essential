package com.Extra2Essentials.Extra2Essentials.controller;

import com.Extra2Essentials.Extra2Essentials.model.*;
import com.Extra2Essentials.Extra2Essentials.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/donations")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class DonationController {

    private final DonationRepository donationRepository;
    private final UserRepository userRepository;

    // Donor creates a donation
    @PostMapping
        public ResponseEntity<?> createDonation(@RequestBody Map<String, String> body,
                                                Authentication auth) {
            User donor = userRepository.findByEmail(auth.getName()).orElseThrow();

            Donation donation = Donation.builder()
                    .donor(donor)
                    .title(body.get("title"))
                    .category(Category.valueOf(body.get("category").toUpperCase()))
                    .quantity(Integer.parseInt(body.get("quantity")))
                    .city(body.get("city"))
                    .pickupDate(LocalDate.parse(body.get("pickupDate")))
                    .status(DonationStatus.AVAILABLE)
                    .build();

            // Save address and coordinates if provided
            if (body.get("address") != null) 
                donation.setAddress(body.get("address"));
            if (body.get("latitude") != null && !body.get("latitude").isEmpty()) 
                donation.setLatitude(Double.parseDouble(body.get("latitude")));
            if (body.get("longitude") != null && !body.get("longitude").isEmpty()) 
                donation.setLongitude(Double.parseDouble(body.get("longitude")));

            donationRepository.save(donation);
            return ResponseEntity.ok("Donation created successfully");
        }

    // Get all available donations
    @GetMapping
    public ResponseEntity<List<Donation>> getAllDonations() {
        return ResponseEntity.ok(donationRepository.findByStatus(DonationStatus.AVAILABLE));
    }


    @GetMapping("/all")
    public ResponseEntity<List<Donation>> getAllDonationsForImpact() {
        return ResponseEntity.ok(donationRepository.findAll());
    }

    // Get donations by city
    @GetMapping("/city/{city}")
    public ResponseEntity<List<Donation>> getDonationsByCity(@PathVariable String city) {
        return ResponseEntity.ok(donationRepository.findByCityAndStatus(city, DonationStatus.AVAILABLE));
    }

    @GetMapping("/stats")
    @CrossOrigin(origins = "*")
    public ResponseEntity<?> getStats() {
        List<Donation> all = donationRepository.findAll();
        long total = all.size();
        long claimed = all.stream().filter(d -> 
            d.getStatus() == DonationStatus.CLAIMED || 
            d.getStatus() == DonationStatus.DELIVERED).count();
        long cities = all.stream().map(Donation::getCity)
            .filter(c -> c != null).distinct().count();
        long ngos = userRepository.countByRole(Role.NGO);

        return ResponseEntity.ok(Map.of(
            "total", total,
            "claimed", claimed,
            "cities", cities,
            "ngos", ngos,
            "impact", claimed * 5
        ));
    }

    // NGO claims a donation
    @PutMapping("/{id}/claim")
    public ResponseEntity<?> claimDonation(@PathVariable Long id, Authentication auth) {
        User ngo = userRepository.findByEmail(auth.getName()).orElseThrow();
        Donation donation = donationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Donation not found"));

        if (donation.getStatus() != DonationStatus.AVAILABLE) {
            return ResponseEntity.badRequest().body("Donation already claimed");
        }

        donation.setStatus(DonationStatus.CLAIMED);
        donation.setClaimedBy(ngo);
        donationRepository.save(donation);
        return ResponseEntity.ok("Donation claimed successfully");
    }

    // Get my donations (donor view)
    @GetMapping("/my")
    public ResponseEntity<List<Donation>> myDonations(Authentication auth) {
        User donor = userRepository.findByEmail(auth.getName()).orElseThrow();
        return ResponseEntity.ok(donationRepository.findByDonorId(donor.getId()));
    }
}