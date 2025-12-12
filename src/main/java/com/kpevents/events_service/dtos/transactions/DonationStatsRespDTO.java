package com.kpevents.events_service.dtos.transactions;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DonationStatsRespDTO {
    private Character building;
    private Float amount;
}
