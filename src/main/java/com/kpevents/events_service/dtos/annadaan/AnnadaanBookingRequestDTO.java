package com.kpevents.events_service.dtos.annadaan;


import com.kpevents.events_service.dtos.users.FlatNumberDTO;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class AnnadaanBookingRequestDTO {
    @NotNull(message = "Booking Name is required")
    private String bookingName;

    @NotNull(message = "Year is required")
    private Short year;

    @NotNull(message = "Receiver is required")
    private String userId;

    @NotNull(message = "Amount is required")
    private Float amount;

    @NotNull(message = "Amount is required")
    private FlatNumberDTO flatNumber;

    private List<AnnadaanItemDTO> items;
}
