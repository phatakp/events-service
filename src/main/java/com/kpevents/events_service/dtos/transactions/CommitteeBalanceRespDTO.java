package com.kpevents.events_service.dtos.transactions;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CommitteeBalanceRespDTO {
    private String txnType;
    private String txnSubType;
    private Float amount;
}
