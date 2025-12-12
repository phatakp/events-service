package com.kpevents.events_service.mappers;

import com.kpevents.events_service.dtos.annadaan.AnnadaanBookingRequestDTO;
import com.kpevents.events_service.dtos.annadaan.AnnadaanBookingRespDTO;
import com.kpevents.events_service.dtos.annadaan.AnnadaanItemRespDTO;
import com.kpevents.events_service.dtos.temple.TempleBookingRequestDTO;
import com.kpevents.events_service.dtos.temple.TempleBookingRespDTO;
import com.kpevents.events_service.dtos.temple.TempleItemRespDTO;
import com.kpevents.events_service.dtos.transactions.DonationRequestDTO;
import com.kpevents.events_service.dtos.transactions.DonationRespDTO;
import com.kpevents.events_service.dtos.transactions.OtherTxnRequestDTO;
import com.kpevents.events_service.dtos.transactions.TxnResponseDTO;
import com.kpevents.events_service.entities.enums.Committee;
import com.kpevents.events_service.entities.enums.TxnMode;
import com.kpevents.events_service.entities.enums.TxnSubType;
import com.kpevents.events_service.entities.enums.TxnType;
import com.kpevents.events_service.entities.transactions.Donation;
import com.kpevents.events_service.entities.transactions.Transaction;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@AllArgsConstructor
public class TxnMapper {
    private final UserMapper userMapper;
    private final AnnadaanMapper annadaanMapper;
    private final TempleMapper templeMapper;

    public static Transaction toTransaction(DonationRequestDTO request) {
        var txn = new Transaction();
        txn.setAmount(request.getAmount());
        txn.setDate(request.getDate());
        txn.setTxnType(request.getTxnType());
        txn.setTxnMode(request.getTxnMode());
        txn.setCommittee(request.getCommittee());
        txn.setDescription(request.getDescription());
        txn.setTxnSubType(request.getTxnSubType());
        return txn;
    }

    public static Transaction toTransaction(OtherTxnRequestDTO request, Boolean fromUser) {
        return Transaction.builder()
                .amount(fromUser ? request.getAmount() * -1 : request.getAmount())
                .date(request.getDate())
                .txnType(!request.getIsTransfer()
                        ? TxnType.expense
                        : fromUser
                        ? TxnType.transfer_out
                        : TxnType.transfer_in)
                .txnMode(TxnMode.online)
                .committee(request.getCommittee())
                .description(!request.getIsTransfer()?request.getDescription():null)
                .build();
    }


    public static Transaction toTransaction(Transaction txn, DonationRequestDTO request) {
        txn.setAmount(request.getAmount());
        txn.setDate(request.getDate());
        txn.setTxnMode(request.getTxnMode());
        txn.setDescription(request.getDescription());
        return txn;
    }

    public static Transaction toTransaction(Transaction txn, OtherTxnRequestDTO request, Boolean fromUser) {
        txn.setAmount(fromUser ? request.getAmount() * -1 : request.getAmount());
        txn.setDate(request.getDate());
        if (!request.getIsTransfer()) {
            txn.setDescription(request.getDescription());
        }
        return txn;
    }

    public static Donation toDonation(DonationRequestDTO request, Transaction txn) {
        var donation = new Donation();
        donation.setBuilding(request.getFlatNumber().getBuilding());
        donation.setFlat(request.getFlatNumber().getFlat());
        donation.setDonorName(request.getDonorName());
        donation.setYear(request.getYear());
        donation.setQuantity(request.getQuantity());
        donation.setTxn(txn);
        return donation;
    }

    public DonationRespDTO toDonationResp(Donation donation) {
        if (donation == null) {
            return null;
        }
        return DonationRespDTO.builder()
                .txnId(donation.getTxn().getId())
                .donorName(donation.getDonorName())
                .flatNumber(donation.getBuilding() + "-" + donation.getFlat())
                .quantity(donation.getQuantity())
                .year(donation.getYear())
                .build();
    }

    public TxnResponseDTO toTxnResponseDTO(Transaction transaction) {
        return TxnResponseDTO.builder()
                .id(transaction.getId())
                .description(transaction.getDescription())
                .amount(transaction.getAmount())
                .date(transaction.getDate())
                .txnType(transaction.getTxnType())
                .txnMode(transaction.getTxnMode())
                .user(userMapper.mapToUserShortDTO(transaction.getUser()))
                .committee(transaction.getCommittee())
                .donation(toDonationResp(transaction.getDonation()))
                .txnSubType(transaction.getTxnSubType())
                .build();
    }

    public AnnadaanBookingRespDTO toAnnadaanResponseDTO(Transaction txn) {
        var bookings = txn.getAnnadaanBookings();
        var items = bookings.stream().map(annadaanMapper::mapToBookingItemDTO).toList();

        return AnnadaanBookingRespDTO.builder()
                .txnId(txn.getId())
                .bookingName(bookings.getFirst().getBookingName())
                .flatNumber(bookings.getFirst().getBuilding() + "-" +
                        bookings.getFirst().getFlat())
                .totalAmount(txn.getAmount())
                .year(bookings.getFirst().getYear())
                .isConfirmed(bookings.getFirst().getIsConfirmed())
                .createdAt(txn.getCreatedAt())
                .user(userMapper.mapToUserShortDTO(txn.getUser()))
                .items(items)
                .build();
    }

    public List<AnnadaanItemRespDTO> toAnnadaanBookingResp(Transaction txn) {
        var annadaanBookings = txn.getAnnadaanBookings();
        var result = new ArrayList<AnnadaanItemRespDTO>();

        for  (var annadaanBooking : annadaanBookings) {
            var item = annadaanBooking.getAnnadaanItem();
            var bookings = item.getAnnadaanBookings()
                    .stream()
                    .map(annadaanMapper::mapToItemBookingDTO)
                    .toList();
            var bookedQuantity = bookings.stream()
                    .reduce(0.0f,
                            (partSum,i)->i.getQuantity()+partSum,Float::sum);
            var bookedAmount = bookings.stream()
                    .reduce(0.0f,
                            (partSum,i)->i.getAmount()+partSum,Float::sum);
            result.add(AnnadaanItemRespDTO.builder()
                    .id(item.getId())
                    .itemName(item.getItemName())
                    .amount(item.getAmount())
                    .quantity(item.getQuantity())
                    .price(item.getPrice())
                    .bookedQuantity(bookedQuantity)
                    .bookedAmount(bookedAmount)
                    .bookings(bookings)
                    .build());
        }
        return result;

    }

    public Transaction toTransaction(AnnadaanBookingRequestDTO request) {
        return Transaction.builder()
                .amount(request.getAmount())
                .date(LocalDate.now())
                .txnType(TxnType.donation)
                .txnSubType(TxnSubType.annadaan)
                .txnMode(TxnMode.online)
                .committee(Committee.cultural)
                .build();
    }

    public Transaction toTransaction(TempleBookingRequestDTO request) {
        return Transaction.builder()
                .amount(request.getAmount())
                .date(LocalDate.now())
                .txnType(TxnType.donation)
                .txnSubType(TxnSubType.itemized)
                .txnMode(TxnMode.online)
                .committee(Committee.temple)
                .build();
    }

    public TempleBookingRespDTO toTempleResponseDTO(Transaction txn) {
        var bookings = txn.getTempleBookings();
        var items = bookings.stream().map(templeMapper::mapToBookingItemDTO).toList();

        return TempleBookingRespDTO.builder()
                .txnId(txn.getId())
                .bookingName(bookings.getFirst().getBookingName())
                .flatNumber(bookings.getFirst().getBuilding() + "-" +
                        bookings.getFirst().getFlat())
                .totalAmount(txn.getAmount())
                .createdAt(txn.getCreatedAt())
                .items(items)
                .build();
    }

    public List<TempleItemRespDTO> toTempleBookingResp(Transaction txn) {
        var templeBookings = txn.getTempleBookings();
        var result = new ArrayList<TempleItemRespDTO>();

        for  (var templeBooking : templeBookings) {
            var item = templeBooking.getTempleItem();
            var bookings = item.getTempleBookings()
                    .stream()
                    .map(templeMapper::mapToItemBookingDTO)
                    .toList();
            var bookedAmount = bookings.stream()
                    .reduce(0.0f,
                            (partSum,i)->i.getAmount()+partSum,Float::sum);
            result.add(TempleItemRespDTO.builder()
                    .id(item.getId())
                    .itemName(item.getItemName())
                    .amount(item.getAmount())
                    .bookedAmount(bookedAmount)
                    .bookings(bookings)
                    .build());
        }
        return result;

    }
}
