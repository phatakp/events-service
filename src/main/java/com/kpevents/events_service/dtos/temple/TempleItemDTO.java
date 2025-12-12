package com.kpevents.events_service.dtos.temple;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TempleItemDTO {
    private Long id;
    private String itemName;
    private Float bookedAmount;
}
