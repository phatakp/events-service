package com.kpevents.events_service.services.impl;

import com.kpevents.events_service.config.exceptions.APIException;
import com.kpevents.events_service.dtos.temple.TempleItemRequestDTO;
import com.kpevents.events_service.dtos.temple.TempleItemRespDTO;
import com.kpevents.events_service.entities.temple.TempleItem;
import com.kpevents.events_service.mappers.TempleMapper;
import com.kpevents.events_service.repositories.AnnadaanItemRepository;
import com.kpevents.events_service.repositories.TempleBookingRepository;
import com.kpevents.events_service.repositories.TempleItemRepository;
import com.kpevents.events_service.repositories.TxnRepository;
import com.kpevents.events_service.services.TempleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class TempleServiceImpl implements TempleService {
    private final TempleItemRepository templeItemRepository;
    private final TempleMapper templeMapper;
    private final TempleBookingRepository templeBookingRepository;
    private final TxnRepository txnRepository;

    @Override
    public List<TempleItemRespDTO> getAvailableItems() {
        var items =  templeItemRepository.getAllItems();
        return items.stream()
                .map(templeMapper::mapToItemResponseDTO)
                .filter(i->i.getBookedAmount()<i.getAmount())
                .toList();
    }

    @Override
    public TempleItemRespDTO addNewTempleItem(TempleItemRequestDTO request) {
        var item = templeItemRepository.findTempleItemByItemName(request.getItemName()).orElse(null);
        if (item != null){
            throw APIException.alreadyPresent(String.format("Item with name %s already exists", request.getItemName()));
        }
        var newItem = templeMapper.mapToTempleItem(request);
        var templeItem = templeItemRepository.save(newItem);
        return templeMapper.mapToItemResponseDTO(templeItem);
    }

    @Override
    public TempleItemRespDTO updateTempleItem(TempleItemRequestDTO request, Long itemId) {
        var item = templeItemRepository.findById(itemId).orElse(null);
        if (item == null){
            throw APIException.notFound("Item not found for id: " + itemId);
        }
        var updatedItem = templeMapper.mapToTempleItem(request,item);
        var templeItem = templeItemRepository.save(updatedItem);
        return templeMapper.mapToItemResponseDTO(templeItem);
    }

    @Override
    public void deleteTempleItem(Long itemId) {
        var item = templeItemRepository.findById(itemId).orElse(null);
        if (item == null){
            throw APIException.notFound("Item not found for id: " + itemId);
        }
        templeItemRepository.delete(item);
    }

    @Override
    public TempleItem validateItem(Long id) {
        var item = templeItemRepository.findById(id).orElse(null);
        if (item == null){
            throw APIException.notFound("Item not found for id: " + id);
        }
        return item;
    }

    @Override
    @Transactional
    public void deleteTempleBooking(Long bookingId) {
        var booking = templeBookingRepository.findById(bookingId).orElse(null);
        if (booking == null){
            throw APIException.notFound("Booking not found for id: " + bookingId);
        }
        var txn = booking.getTxn();
        var item = booking.getTempleItem();

        txn.getTempleBookings().remove(booking);
        item.getTempleBookings().remove(booking);
        templeBookingRepository.delete(booking);
        if (txn.getTempleBookings().isEmpty()){
            txnRepository.delete(txn);
        }
    }


}
