package com.kpevents.events_service.services.impl;

import com.kpevents.events_service.config.exceptions.APIException;
import com.kpevents.events_service.dtos.annadaan.AnnadaanItemRequestDTO;
import com.kpevents.events_service.dtos.annadaan.AnnadaanItemRespDTO;
import com.kpevents.events_service.entities.annadaan.AnnadaanBooking;
import com.kpevents.events_service.entities.annadaan.AnnadaanItem;
import com.kpevents.events_service.mappers.AnnadaanMapper;
import com.kpevents.events_service.repositories.AnnadaanBookingRepository;
import com.kpevents.events_service.repositories.AnnadaanItemRepository;
import com.kpevents.events_service.repositories.TxnRepository;
import com.kpevents.events_service.services.AnnadaanService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AnnadaanServiceImpl implements AnnadaanService {

    private final AnnadaanItemRepository annadaanItemRepository;
    private final AnnadaanMapper annadaanMapper;
    private final AnnadaanBookingRepository annadaanBookingRepository;
    private final TxnRepository txnRepository;


    @Override
    public List<AnnadaanItemRespDTO> getAvailableAnnadaanItems(Short year) {

        var items =  annadaanItemRepository.getAllAnnadaanItems();
        var result = new ArrayList<AnnadaanItem>();
        for (AnnadaanItem item: items){
            var bookings = item.getAnnadaanBookings();
            var filteredBookings = bookings
                    .stream()
                    .filter(booking -> year.equals(booking.getYear()))
                    .toList();
            item.setAnnadaanBookings(filteredBookings);
            result.add(item);
        }

        return result.stream()
                .map(annadaanMapper::mapToItemResponseDTO)
                .filter(i->i.getBookedQuantity()<i.getQuantity())
                .toList();
    }



    @Override
    public void cancelAnnadaanBooking(Long bookingId) {
        var booking = annadaanBookingRepository.findById(bookingId).orElse(null);
        if (booking == null){
            throw APIException.notFound("Booking not found for id: " + bookingId);
        }
        annadaanBookingRepository.delete(booking);
    }

    @Override
    public AnnadaanItemRespDTO addNewAnnadaanItem(AnnadaanItemRequestDTO request) {
        var item = annadaanItemRepository.findAnnadaanItemByItemName(request.getItemName()).orElse(null);
        if (item != null){
            throw APIException.alreadyPresent(String.format("Item with name %s already exists", request.getItemName()));
        }
        var newItem = annadaanMapper.mapToAnnadaanItem(request);
        var annadaanItem = annadaanItemRepository.save(newItem);
        return annadaanMapper.mapToItemResponseDTO(annadaanItem);
    }

    @Override
    public AnnadaanItemRespDTO updateAnnadaanItem(AnnadaanItemRequestDTO request,Long itemId) {
        var item = annadaanItemRepository.findById(itemId).orElse(null);
        if (item == null){
            throw APIException.notFound("Item not found for id: " + itemId);
        }

        var updatedItem = annadaanMapper.mapToAnnadaanItem(request,item);
        var annadaanItem = annadaanItemRepository.save(updatedItem);
        return annadaanMapper.mapToItemResponseDTO(annadaanItem);
    }

    @Override
    public void deleteAnnadaanItem(Long itemId) {
        var item = annadaanItemRepository.findById(itemId).orElse(null);
        if (item == null){
            throw APIException.notFound("Item not found for id: " + itemId);
        }
        annadaanItemRepository.delete(item);
    }

    @Override
    public AnnadaanItem validateItem(Long itemId) {
        var item = annadaanItemRepository.findByItemId(itemId).orElse(null);
        if (item == null){
            throw APIException.notFound("Item not found for id: " + itemId);
        }
        return item;
    }

    @Override
    public void saveBooking(AnnadaanBooking annadaanBooking) {
        annadaanBookingRepository.save(annadaanBooking);
    }

    @Override
    public void deleteAnnadaanBooking(Long bookingId) {
        var booking = annadaanBookingRepository.findById(bookingId).orElse(null);
        if (booking == null){
            throw APIException.notFound("Booking not found for id: " + bookingId);
        }
        var txn = booking.getTxn();
        var item = booking.getAnnadaanItem();

        txn.getAnnadaanBookings().remove(booking);
        item.getAnnadaanBookings().remove(booking);
        annadaanBookingRepository.delete(booking);
        if (txn.getAnnadaanBookings().isEmpty()){
            txnRepository.delete(txn);
        }
    }


}
