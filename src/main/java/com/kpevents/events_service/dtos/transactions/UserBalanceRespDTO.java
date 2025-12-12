package com.kpevents.events_service.dtos.transactions;

import com.kpevents.events_service.entities.enums.Building;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserBalanceRespDTO {
    private String firstName;
    private String lastName;
    private String flatNumber;
    private String clerkId;
    private String imageUrl;
    private String txnType;
    private String txnSubType;
    private float balance;
}
