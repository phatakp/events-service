package com.kpevents.events_service.dtos.annadaan;

import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
public class AnnadaanItemRespDTO {
    private Long id;
    private String itemName;
    private Float price;
    private Float quantity;
    private Float amount;
    private Float bookedQuantity;
    private Float bookedAmount;
    private List<AnnadaanBookingDTO> bookings = new ArrayList<>();
}
