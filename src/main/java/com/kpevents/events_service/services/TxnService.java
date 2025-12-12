package com.kpevents.events_service.services;

import com.kpevents.events_service.dtos.annadaan.AnnadaanBookingRequestDTO;
import com.kpevents.events_service.dtos.annadaan.AnnadaanBookingRespDTO;
import com.kpevents.events_service.dtos.annadaan.AnnadaanItemRespDTO;
import com.kpevents.events_service.dtos.temple.TempleBookingRequestDTO;
import com.kpevents.events_service.dtos.temple.TempleBookingRespDTO;
import com.kpevents.events_service.dtos.temple.TempleItemRespDTO;
import com.kpevents.events_service.dtos.transactions.*;
import com.kpevents.events_service.entities.enums.Committee;
import com.kpevents.events_service.entities.transactions.Transaction;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TxnService {
    TxnResponseDTO addDonation(@Valid DonationRequestDTO request);
    TxnResponseDTO updateDonation(Long txnId,@Valid DonationRequestDTO request);
    void deleteTxn(Long txnId);
    List<TxnResponseDTO> getDonationsByCommitteeForYear(Committee committee, Short year,Pageable pageable);
    List<DonationStatsRespDTO> getDonationsStatsByCommitteeForYear(Committee committee,Short year);
    List<UserBalanceRespDTO> getCurrentBalanceByUserForCommittee(Committee committee);
    List<CommitteeBalanceRespDTO> getCurrentBalanceByCommittee(Committee committee);

    List<AnnadaanBookingRespDTO> getAnnadaanBookings(Short year);
    void cancelAnnadaanBooking(Long bookingId);
    void confirmAnnadaanBooking(String txnId);
    List<AnnadaanItemRespDTO> addNewAnnadaanBooking(AnnadaanBookingRequestDTO request);


    List<TxnResponseDTO> addExpenseOrTransfer(@Valid OtherTxnRequestDTO request);
    TxnResponseDTO updateExpense(Long txnId, @Valid OtherTxnRequestDTO request);
    List<TxnResponseDTO> getOtherTxnsByCommittee(Committee committee);
    List<TempleBookingRespDTO> getTempleBookings();
    List<TempleItemRespDTO> addNewTempleBooking(@Valid TempleBookingRequestDTO request);
}
