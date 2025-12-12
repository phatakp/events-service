package com.kpevents.events_service.dtos.transactions;

import com.kpevents.events_service.dtos.users.FlatNumberDTO;
import com.kpevents.events_service.entities.enums.Committee;
import com.kpevents.events_service.entities.enums.TxnSubType;
import com.kpevents.events_service.entities.enums.TxnMode;
import com.kpevents.events_service.entities.enums.TxnType;
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
public class DonationRequestDTO {
    @NotNull(message = "Donor Name is required")
    private String donorName;

    @NotNull(message = "Amount is required")
    private Float amount;

    @NotNull(message = "Year is required")
    private Short year;

    private LocalDate date =  LocalDate.now();

    @NotNull(message = "Txn Type is required")
    @Enumerated(EnumType.STRING)
    private TxnType txnType;

    @NotNull(message = "Txn Mode is required")
    @Enumerated(EnumType.STRING)
    private TxnMode txnMode;

    @NotNull(message = "Committee is required")
    @Enumerated(EnumType.STRING)
    private Committee committee;

    @Enumerated(EnumType.STRING)
    private TxnSubType txnSubType;

    private Integer quantity= 0;

    @NotNull(message = "User is required")
    private String userId;

    private FlatNumberDTO flatNumber;
    private String description;

}
