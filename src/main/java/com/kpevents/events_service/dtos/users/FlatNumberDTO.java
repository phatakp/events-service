package com.kpevents.events_service.dtos.users;

import com.kpevents.events_service.entities.enums.Building;
import com.kpevents.events_service.validators.FlatNumber;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@FlatNumber
public class FlatNumberDTO {
    @NotNull(message = "Building is required")
    private Building building;

    @NotNull(message = "Flat is required")
    private Short flat;

}
