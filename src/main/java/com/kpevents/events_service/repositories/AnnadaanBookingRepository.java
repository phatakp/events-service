package com.kpevents.events_service.repositories;

import com.kpevents.events_service.entities.annadaan.AnnadaanBooking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AnnadaanBookingRepository extends JpaRepository<AnnadaanBooking, Long> {
}
