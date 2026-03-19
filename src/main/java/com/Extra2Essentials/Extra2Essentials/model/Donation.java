package com.Extra2Essentials.Extra2Essentials.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "donations")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Donation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "donor_id")
    private User donor;

    private String title;

    @Enumerated(EnumType.STRING)
    private Category category;

    private int quantity;
    private String city;
    private String address;
    private Double latitude;
    private Double longitude;
    private LocalDate pickupDate;

    @Enumerated(EnumType.STRING)
    private DonationStatus status = DonationStatus.AVAILABLE;

    @ManyToOne
    @JoinColumn(name = "claimed_by")
    private User claimedBy;

    private LocalDateTime createdAt = LocalDateTime.now();
}