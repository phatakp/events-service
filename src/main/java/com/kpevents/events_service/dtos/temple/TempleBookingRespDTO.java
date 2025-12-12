package com.kpevents.events_service.dtos.temple;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
public class TempleBookingRespDTO {
    private Long txnId;
    private String bookingName;
    private String flatNumber;
    private Float totalAmount;
    private LocalDateTime createdAt;
    private List<TempleItemDTO> items = new ArrayList<>();
}
