package com.kpevents.events_service.dtos.transactions;

import com.kpevents.events_service.entities.enums.Committee;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OtherTxnRequestDTO {
    @NotNull(message = "Amount is required")
    private Float amount;

    private LocalDate date =  LocalDate.now();

    private Boolean isTransfer = Boolean.FALSE;


    @NotNull(message = "Committee is required")
    @Enumerated(EnumType.STRING)
    private Committee committee;

    @NotNull(message = "From User is required")
    private String fromUserId;

    private String toUserId;

    private String description;

}
