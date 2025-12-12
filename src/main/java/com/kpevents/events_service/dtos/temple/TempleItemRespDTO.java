package com.kpevents.events_service.dtos.temple;

import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
public class TempleItemRespDTO {
    private Long id;
    private String itemName;
    private Float amount;
    private Float bookedAmount;
    private List<TempleBookingDTO> bookings = new ArrayList<>();
}
