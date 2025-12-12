package com.kpevents.events_service.dtos.transactions;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class DonationRespDTO {
    private Long txnId;
    private String donorName;
    private String flatNumber;
    private Integer quantity;
    private Short year;
}
