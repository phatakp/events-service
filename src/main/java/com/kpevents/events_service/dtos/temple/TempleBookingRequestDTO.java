package com.kpevents.events_service.dtos.temple;

import com.kpevents.events_service.dtos.annadaan.AnnadaanItemDTO;
import com.kpevents.events_service.dtos.users.FlatNumberDTO;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class TempleBookingRequestDTO {
    @NotNull(message = "Booking Name is required")
    private String bookingName;

    @NotNull(message = "Receiver is required")
    private String userId;

    @NotNull(message = "Amount is required")
    private Float amount;

    @NotNull(message = "Amount is required")
    private FlatNumberDTO flatNumber;


    private List<TempleItemDTO> items;
}
