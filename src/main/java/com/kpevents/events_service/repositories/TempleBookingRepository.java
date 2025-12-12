package com.kpevents.events_service.repositories;

import com.kpevents.events_service.entities.temple.TempleBooking;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TempleBookingRepository extends JpaRepository<TempleBooking,Long> {
}
