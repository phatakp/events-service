package com.kpevents.events_service.repositories;

import com.kpevents.events_service.entities.temple.TempleItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TempleItemRepository extends JpaRepository<TempleItem,Long> {

    @Query("select i from TempleItem i " +
            "left join fetch i.templeBookings")
    List<TempleItem> getAllItems();

    Optional<TempleItem> findTempleItemByItemName(String itemName);
}
