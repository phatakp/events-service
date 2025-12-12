package com.kpevents.events_service.repositories;

import com.kpevents.events_service.entities.transactions.Donation;
import org.springframework.data.jpa.repository.JpaRepository;


public interface DonationRepository extends JpaRepository<Donation, Long> {
}
