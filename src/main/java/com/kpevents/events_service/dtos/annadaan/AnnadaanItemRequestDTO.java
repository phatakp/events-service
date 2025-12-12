package com.kpevents.events_service.dtos.annadaan;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AnnadaanItemRequestDTO {
    @NotNull(message = "Item Name is required")
    private String itemName;

    @NotNull(message = "Price is required")
    private Float price;

    @NotNull(message = "Quantity is required")
    private Float quantity;

    @NotNull(message = "Amount is required")
    private Float amount;
}
