package com.kpevents.events_service.dtos.temple;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TempleItemRequestDTO {
    @NotNull(message = "Item Name is required")
    private String itemName;

    @NotNull(message = "Amount is required")
    private Float amount;
}
