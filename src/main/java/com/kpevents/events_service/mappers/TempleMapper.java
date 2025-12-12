package com.kpevents.events_service.mappers;

import com.kpevents.events_service.dtos.annadaan.*;
import com.kpevents.events_service.dtos.temple.*;
import com.kpevents.events_service.entities.temple.TempleBooking;
import com.kpevents.events_service.entities.temple.TempleItem;
import org.springframework.stereotype.Component;


@Component
public class TempleMapper {

    public TempleBookingDTO mapToItemBookingDTO(TempleBooking booking) {

        return TempleBookingDTO.builder()
                .id(booking.getId())
                .bookingName(booking.getBookingName())
                .amount(booking.getAmount())
                .flatNumber(booking.getBuilding()+"-"+booking.getFlat())
                .build();
    }

    public TempleItemRespDTO mapToItemResponseDTO(TempleItem item) {
        var bookings = item.getTempleBookings().stream().map(this::mapToItemBookingDTO).toList();
        var bookedAmount = bookings.stream()
                .reduce(0.0f,
                        (partSum,i)->i.getAmount()+partSum,Float::sum);

        return TempleItemRespDTO.builder()
                .id(item.getId())
                .itemName(item.getItemName())
                .amount(item.getAmount())
                .bookedAmount(bookedAmount)
                .bookings(bookings)
                .build();
    }

    public TempleItemDTO mapToBookingItemDTO(TempleBooking booking) {

        return TempleItemDTO.builder()
                .id(booking.getId())
                .itemName(booking.getTempleItem().getItemName())
                .bookedAmount(booking.getAmount())
                .build();
    }

    public TempleItem mapToTempleItem(TempleItemRequestDTO request) {
        return TempleItem.builder()
                .itemName(request.getItemName())
                .amount(request.getAmount())
                .build();
    }

    public TempleItem mapToTempleItem(TempleItemRequestDTO request, TempleItem item) {
        item.setItemName(request.getItemName());
        item.setAmount(request.getAmount());
        return item;
    }

    public TempleBooking mapToTempleBooking(TempleItemDTO item, TempleBookingRequestDTO request) {
        return TempleBooking.builder()
                .building(request.getFlatNumber().getBuilding())
                .flat(request.getFlatNumber().getFlat())
                .bookingName(request.getBookingName())
                .amount(item.getBookedAmount())
                .build();
    }
}
