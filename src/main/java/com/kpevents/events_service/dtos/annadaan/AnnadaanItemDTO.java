package com.kpevents.events_service.dtos.annadaan;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AnnadaanItemDTO {
    private Long id;
    private String itemName;
    private Float price;
    private Float bookedQuantity;
    private Float bookedAmount;
}
