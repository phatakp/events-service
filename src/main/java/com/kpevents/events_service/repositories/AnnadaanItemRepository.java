package com.kpevents.events_service.repositories;

import com.kpevents.events_service.entities.annadaan.AnnadaanItem;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AnnadaanItemRepository extends JpaRepository<AnnadaanItem,Long> {

    @Query("select i from AnnadaanItem i " +
            "left join fetch i.annadaanBookings ")
    List<AnnadaanItem> getAllAnnadaanItems();


    Optional<AnnadaanItem> findAnnadaanItemByItemName(String itemName);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select i from AnnadaanItem i " +
            "left join fetch i.annadaanBookings " +
            "where i.id=:itemId")
    Optional<AnnadaanItem> findByItemId(@Param("itemId") Long itemId);
}
