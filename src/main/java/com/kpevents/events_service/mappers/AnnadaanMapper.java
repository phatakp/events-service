package com.kpevents.events_service.mappers;

import com.kpevents.events_service.dtos.annadaan.*;
import com.kpevents.events_service.entities.annadaan.AnnadaanBooking;
import com.kpevents.events_service.entities.annadaan.AnnadaanItem;
import org.springframework.stereotype.Component;


@Component
public class AnnadaanMapper {


    public AnnadaanBookingDTO mapToItemBookingDTO(AnnadaanBooking booking) {

        return AnnadaanBookingDTO.builder()
                .id(booking.getId())
                .bookingName(booking.getBookingName())
                .amount(booking.getAmount())
                .quantity(booking.getQuantity())
                .flatNumber(booking.getBuilding()+"-"+booking.getFlat())
                .year(booking.getYear())
                .build();
    }

    public AnnadaanItemDTO mapToBookingItemDTO(AnnadaanBooking booking) {

        return AnnadaanItemDTO.builder()
                .id(booking.getId())
                .itemName(booking.getAnnadaanItem().getItemName())
                .bookedAmount(booking.getAmount())
                .bookedQuantity(booking.getQuantity())
                .price(booking.getAnnadaanItem().getPrice())
                .build();
    }

    public AnnadaanItemRespDTO mapToItemResponseDTO(AnnadaanItem item) {
        var bookings = item.getAnnadaanBookings().stream().map(this::mapToItemBookingDTO).toList();
        var bookedQuantity = bookings.stream()
                .reduce(0.0f,
                        (partSum,i)->i.getQuantity()+partSum,Float::sum);
        var bookedAmount = bookings.stream()
                .reduce(0.0f,
                        (partSum,i)->i.getAmount()+partSum,Float::sum);

        return AnnadaanItemRespDTO.builder()
                .id(item.getId())
                .itemName(item.getItemName())
                .amount(item.getAmount())
                .quantity(item.getQuantity())
                .price(item.getPrice())
                .bookedQuantity(bookedQuantity)
                .bookedAmount(bookedAmount)
                .bookings(bookings)
                .build();
    }

    public AnnadaanItem mapToAnnadaanItem(AnnadaanItemRequestDTO request) {
        return AnnadaanItem.builder()
                .itemName(request.getItemName())
                .price(request.getPrice())
                .quantity(request.getQuantity())
                .amount(request.getAmount())
                .build();
    }

    public AnnadaanItem mapToAnnadaanItem(AnnadaanItemRequestDTO request, AnnadaanItem item) {
        item.setItemName(request.getItemName());
        item.setPrice(request.getPrice());
        item.setQuantity(request.getQuantity());
        item.setAmount(request.getAmount());
        return item;
    }

    public AnnadaanBooking mapToAnnadaanBooking(AnnadaanItemDTO item, AnnadaanBookingRequestDTO request) {
        return AnnadaanBooking.builder()
                .building(request.getFlatNumber().getBuilding())
                .flat(request.getFlatNumber().getFlat())
                .bookingName(request.getBookingName())
                .amount(item.getBookedAmount())
                .quantity(item.getBookedQuantity())
                .isConfirmed(false)
                .year(request.getYear())
                .build();
    }
}
