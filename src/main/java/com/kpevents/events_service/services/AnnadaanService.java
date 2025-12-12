package com.kpevents.events_service.services;

import com.kpevents.events_service.dtos.annadaan.AnnadaanItemRequestDTO;
import com.kpevents.events_service.dtos.annadaan.AnnadaanItemRespDTO;
import com.kpevents.events_service.entities.annadaan.AnnadaanBooking;
import com.kpevents.events_service.entities.annadaan.AnnadaanItem;

import java.util.List;

public interface AnnadaanService {
    List<AnnadaanItemRespDTO> getAvailableAnnadaanItems(Short year);
    void cancelAnnadaanBooking(Long bookingId);
    AnnadaanItemRespDTO addNewAnnadaanItem(AnnadaanItemRequestDTO request);
    AnnadaanItemRespDTO updateAnnadaanItem(AnnadaanItemRequestDTO request,Long itemId);
    void deleteAnnadaanItem(Long itemId);
    AnnadaanItem validateItem(Long itemId);
    void saveBooking(AnnadaanBooking annadaanBooking);

    void deleteAnnadaanBooking(Long bookingId);
}
