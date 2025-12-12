package com.kpevents.events_service.services;

import com.kpevents.events_service.dtos.temple.TempleItemRequestDTO;
import com.kpevents.events_service.dtos.temple.TempleItemRespDTO;
import com.kpevents.events_service.entities.temple.TempleItem;
import jakarta.validation.Valid;

import java.util.List;

public interface TempleService {
    List<TempleItemRespDTO> getAvailableItems();
    TempleItemRespDTO addNewTempleItem(@Valid TempleItemRequestDTO request);
    TempleItemRespDTO updateTempleItem(@Valid TempleItemRequestDTO request, Long itemId);
    void deleteTempleItem(Long itemId);
    TempleItem validateItem(Long id);
    void deleteTempleBooking(Long bookingId);
}
