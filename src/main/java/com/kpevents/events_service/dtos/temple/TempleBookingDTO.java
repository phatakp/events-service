package com.kpevents.events_service.dtos.temple;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TempleBookingDTO {
    private Long id;
    private String bookingName;
    private String flatNumber;
    private Float amount;
}
