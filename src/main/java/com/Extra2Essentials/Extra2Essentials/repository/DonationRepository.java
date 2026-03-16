package com.Extra2Essentials.Extra2Essentials.repository;

import com.Extra2Essentials.Extra2Essentials.model.*;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface DonationRepository extends JpaRepository<Donation, Long> {
    List<Donation> findByStatus(DonationStatus status);
    List<Donation> findByCityAndStatus(String city, DonationStatus status);
    List<Donation> findByDonorId(Long donorId);
}