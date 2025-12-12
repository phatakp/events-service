package com.kpevents.events_service.dtos.annadaan;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AnnadaanBookingDTO {
    private Long id;
    private String bookingName;
    private String flatNumber;
    private Float quantity;
    private Float amount;
    private Short year;
}
