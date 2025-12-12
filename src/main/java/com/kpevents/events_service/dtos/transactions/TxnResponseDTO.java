package com.kpevents.events_service.dtos.transactions;

import com.kpevents.events_service.dtos.users.UserShortDTO;
import com.kpevents.events_service.entities.enums.Committee;
import com.kpevents.events_service.entities.enums.TxnSubType;
import com.kpevents.events_service.entities.enums.TxnMode;
import com.kpevents.events_service.entities.enums.TxnType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TxnResponseDTO {
    private Long id;
    private String description;
    private LocalDate date;
    private Float amount;

    @Enumerated(EnumType.STRING)
    private TxnMode txnMode;

    @Enumerated(EnumType.STRING)
    private TxnType txnType;

    @Enumerated(EnumType.STRING)
    private TxnSubType txnSubType;

    @Enumerated(EnumType.STRING)
    private Committee committee;

    private DonationRespDTO donation;
    private UserShortDTO user;
}
