package com.kpevents.events_service.services.impl;

import com.kpevents.events_service.config.exceptions.APIException;
import com.kpevents.events_service.dtos.annadaan.AnnadaanItemDTO;
import com.kpevents.events_service.dtos.annadaan.AnnadaanBookingRequestDTO;
import com.kpevents.events_service.dtos.annadaan.AnnadaanBookingRespDTO;
import com.kpevents.events_service.dtos.annadaan.AnnadaanItemRespDTO;
import com.kpevents.events_service.dtos.temple.TempleBookingRequestDTO;
import com.kpevents.events_service.dtos.temple.TempleBookingRespDTO;
import com.kpevents.events_service.dtos.temple.TempleItemDTO;
import com.kpevents.events_service.dtos.temple.TempleItemRespDTO;
import com.kpevents.events_service.dtos.transactions.*;
import com.kpevents.events_service.entities.annadaan.AnnadaanBooking;
import com.kpevents.events_service.entities.enums.Committee;
import com.kpevents.events_service.entities.enums.TxnType;
import com.kpevents.events_service.entities.temple.TempleBooking;
import com.kpevents.events_service.entities.transactions.Transaction;
import com.kpevents.events_service.mappers.AnnadaanMapper;
import com.kpevents.events_service.mappers.TempleMapper;
import com.kpevents.events_service.mappers.TxnMapper;
import com.kpevents.events_service.repositories.TxnRepository;
import com.kpevents.events_service.services.*;
import com.kpevents.events_service.utils.TxnUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class TxnServiceImpl implements TxnService {

    private final MemberService memberService;
    private final TxnMapper txnMapper;
    private final TxnRepository txnRepository;
    private final AnnadaanService annadaanService;
    private final UserService userService;
    private final AnnadaanMapper annadaanMapper;
    private final TempleService templeService;
    private final TempleMapper templeMapper;

    @Override
    public TxnResponseDTO addDonation(DonationRequestDTO request) {
        var user = memberService.validateCommitteeMember(request.getUserId(), request.getCommittee());
        TxnUtil.validateAmount(request.getAmount());
        TxnUtil.validateTxnTypeForDonation(request.getTxnType());

        var txn = TxnMapper.toTransaction(request);
        txn.setUser(user);
        if (request.getTxnSubType() == null) {
            var donation = TxnMapper.toDonation(request,txn);
            txn.setDonation(donation);
        }
        txnRepository.save(txn);
        return txnMapper.toTxnResponseDTO(txn);
    }

    @Override
    public TxnResponseDTO updateDonation(Long txnId, DonationRequestDTO request) {
        var txn = txnRepository.findById(txnId).orElse(null);
        if (txn == null) {
            throw  APIException.notFound("Txn not found");
        }
        TxnUtil.validateAmount(request.getAmount());
        TxnUtil.validateTxnTypeForDonation(request.getTxnType());
        var user = memberService.validateCommitteeMember(request.getUserId(), request.getCommittee());
        var updatedTxn = TxnMapper.toTransaction(txn,request);
        updatedTxn.setUser(user);
        txnRepository.save(updatedTxn);
        return txnMapper.toTxnResponseDTO(updatedTxn);
    }

    @Override
    public List<TxnResponseDTO> getDonationsByCommitteeForYear(Committee committee, Short year, Pageable pageable) {
        return txnRepository.getDonationsByCommitteeForYear(committee, year)
                .stream()
                .map(txnMapper::toTxnResponseDTO)
                .toList();
    }

    @Override
    public List<DonationStatsRespDTO> getDonationsStatsByCommitteeForYear(Committee committee, Short year) {
        return txnRepository.getDonationsStatsByCommitteeForYear(committee.name(), year);
    }

    @Override
    public List<UserBalanceRespDTO> getCurrentBalanceByUserForCommittee(Committee committee) {
        return txnRepository.getCurrentBalanceByUserForCommittee(committee.name());
    }


    @Override
    public List<CommitteeBalanceRespDTO> getCurrentBalanceByCommittee(Committee committee) {
        return txnRepository.getCurrentBalanceByCommittee(committee.name());
    }

    @Override
    public void deleteTxn(Long txnId) {
        var txn = txnRepository.findById(txnId).orElse(null);
        if (txn == null) {
            throw  APIException.notFound("Txn not found");
        }
        txnRepository.delete(txn);
    }


    @Override
    public List<AnnadaanBookingRespDTO> getAnnadaanBookings(Short year) {
        var txns = txnRepository.getAnnadaanBookings(year);
        return txns.stream().map(txnMapper::toAnnadaanResponseDTO).toList();
    }


    @Override
    public void cancelAnnadaanBooking(Long bookingId) {
        annadaanService.cancelAnnadaanBooking(bookingId);
    }


    @Override
    @Transactional
    public void confirmAnnadaanBooking(String txnId) {
        var txn= txnRepository.getAnnadaanBookingsForTxn(txnId).orElse(null);
        if  (txn==null) {
            throw APIException.notFound("No annadaan booking found for txnId: " + txnId);
        }

        for (AnnadaanBooking annadaanBooking : txn.getAnnadaanBookings()) {
            annadaanBooking.setIsConfirmed(true);
            annadaanService.saveBooking(annadaanBooking);
        }
        txnRepository.save(txn);
    }

    @Override
    @Transactional
    public List<AnnadaanItemRespDTO> addNewAnnadaanBooking(AnnadaanBookingRequestDTO request) {
        var items = request.getItems();
        var bookings = new ArrayList<AnnadaanBooking>();
        var user = userService.validateUser(request.getUserId());
        var txn = txnMapper.toTransaction(request);
        for (AnnadaanItemDTO item: items) {
            var annadaanItem = annadaanService.validateItem(item.getId());
            if (annadaanItem.getQuantity()-annadaanItem.getBookedQty()<item.getBookedQuantity()){
                throw APIException.invalidData("Item " + item.getItemName() + " already booked by someone");
            }
            var booking = annadaanMapper.mapToAnnadaanBooking(item,request);
            booking.setAnnadaanItem(annadaanItem);
            booking.setTxn(txn);
            bookings.add(booking);
        }

        txn.setAnnadaanBookings(bookings);
        txn.setUser(user);
        txnRepository.save(txn);
        return txnMapper.toAnnadaanBookingResp(txn);
    }

    @Override
    @Transactional
    public List<TxnResponseDTO> addExpenseOrTransfer(OtherTxnRequestDTO request) {
        var fromUser = memberService.validateCommitteeMember(request.getFromUserId(), request.getCommittee());
        TxnUtil.validateAmount(request.getAmount());

        var txn = TxnMapper.toTransaction(request,true);
        txn.setUser(fromUser);

        if (request.getIsTransfer()) {
           var toUser = memberService.validateCommitteeMember(request.getToUserId(), request.getCommittee());
            txn.setDescription("Transferred to "+
                    toUser.getFirstName() + " " + toUser.getLastName() +
                    " (" + toUser.getBuilding() + toUser.getFlat() + ")");
            var trf = TxnMapper.toTransaction(request,false);
            trf.setDescription("Received from "+
                    fromUser.getFirstName() + " " + fromUser.getLastName() +
                    " (" + fromUser.getBuilding() + fromUser.getFlat() + ")");
            trf.setUser(toUser);
            txnRepository.saveAll(List.of(txn,trf));
            return List.of(txnMapper.toTxnResponseDTO(txn),txnMapper.toTxnResponseDTO(trf));
        }
        txnRepository.save(txn);
        return List.of(txnMapper.toTxnResponseDTO(txn));
    }

    @Override
    @Transactional
    public TxnResponseDTO updateExpense(Long txnId, OtherTxnRequestDTO request) {
        var txn = txnRepository.findById(txnId).orElse(null);
        if (txn == null) {
            throw  APIException.notFound("Txn not found");
        }
        if (txn.getTxnType() == TxnType.transfer_in || txn.getTxnType()==TxnType.transfer_out) {
            throw APIException.invalidData("Transfer cannot be modified");
        }
        TxnUtil.validateAmount(request.getAmount());


        var user = memberService.validateCommitteeMember(request.getFromUserId(), request.getCommittee());
        var updatedTxn = TxnMapper.toTransaction(txn,request,true);
        updatedTxn.setUser(user);
        txnRepository.save(updatedTxn);

        return txnMapper.toTxnResponseDTO(updatedTxn);
    }

    @Override
    public List<TxnResponseDTO> getOtherTxnsByCommittee(Committee committee) {
        return txnRepository.getOtherTxnsByCommittee(committee)
                .stream()
                .map(txnMapper::toTxnResponseDTO)
                .toList();
    }

    @Override
    public List<TempleBookingRespDTO> getTempleBookings() {
        var txns = txnRepository.getTempleBookings();
        return txns.stream().map(txnMapper::toTempleResponseDTO).toList();
    }

    @Override
    public List<TempleItemRespDTO> addNewTempleBooking(TempleBookingRequestDTO request) {
        var items = request.getItems();
        var bookings = new ArrayList<TempleBooking>();
        var user = userService.validateUser(request.getUserId());
        var txn = txnMapper.toTransaction(request);
        for (TempleItemDTO item: items) {
            var templeItem = templeService.validateItem(item.getId());
            if (templeItem.getAmount()-templeItem.getBookedAmount()<item.getBookedAmount()){
                throw APIException.invalidData("Item " + item.getItemName() + " already booked by someone");
            }
            var booking = templeMapper.mapToTempleBooking(item,request);
            booking.setTempleItem(templeItem);
            booking.setTxn(txn);
            bookings.add(booking);
        }

        txn.setTempleBookings(bookings);
        txn.setUser(user);
        txnRepository.save(txn);
        return txnMapper.toTempleBookingResp(txn);
    }


}


