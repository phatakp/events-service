package com.kpevents.events_service.utils;

import com.kpevents.events_service.config.exceptions.APIException;
import com.kpevents.events_service.entities.enums.TxnType;
import org.springframework.stereotype.Component;

@Component
public class TxnUtil {

    public static void validateAmount(Float amount){
        if(amount <0){
            throw APIException.invalidData("Invalid amount");
        }
    }

    public static void validateTxnTypeForDonation(TxnType txnType){
        if(txnType != TxnType.donation){
            throw APIException.invalidData("Invalid Txn Type");
        }
    }

}
