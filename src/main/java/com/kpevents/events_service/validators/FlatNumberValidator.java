package com.kpevents.events_service.validators;

import com.kpevents.events_service.dtos.users.FlatNumberDTO;
import com.kpevents.events_service.entities.enums.Building;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public class FlatNumberValidator implements ConstraintValidator<FlatNumber, FlatNumberDTO> {
    private final Map<Building,Short> FLOORS_PER_BUILDING = new HashMap<>();


    @Override
    public void initialize(FlatNumber constraintAnnotation) {
        // Optional: Initialization logic if needed
        FLOORS_PER_BUILDING.put(Building.A, (short) 12);
        FLOORS_PER_BUILDING.put(Building.B, (short) 12);
        FLOORS_PER_BUILDING.put(Building.C, (short) 11);
        FLOORS_PER_BUILDING.put(Building.D, (short) 11);
        FLOORS_PER_BUILDING.put(Building.E, (short) 12);
        FLOORS_PER_BUILDING.put(Building.F, (short) 12);
        FLOORS_PER_BUILDING.put(Building.G, (short) 12);
    }

    @Override
    public boolean isValid(FlatNumberDTO request, ConstraintValidatorContext context) {

        var flat = request.getFlat();
        var building = request.getBuilding();
        log.info("Building:{}", building.toString());
        var floors = FLOORS_PER_BUILDING.get(building);
        for (int i = 1; i <= floors ; i++) {
            for (int j = 0; j <= 4; j++) {
                short flatNum = (short) (i*100+j);
                if (flatNum == flat) {
                    return true;
                }
            }
        }
        return false;
    }

}
