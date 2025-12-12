package com.kpevents.events_service.dtos.annadaan;

import com.kpevents.events_service.dtos.users.UserShortDTO;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
public class AnnadaanBookingRespDTO {
    private Long txnId;
    private String bookingName;
    private String flatNumber;
    private Float totalAmount;
    private Short year;
    private Boolean isConfirmed;
    private LocalDateTime createdAt;
    private UserShortDTO user;
    private List<AnnadaanItemDTO> items = new ArrayList<>();
}
