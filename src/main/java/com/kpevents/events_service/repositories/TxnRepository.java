package com.kpevents.events_service.repositories;

import com.kpevents.events_service.dtos.transactions.CommitteeBalanceRespDTO;
import com.kpevents.events_service.dtos.transactions.DonationStatsRespDTO;
import com.kpevents.events_service.dtos.transactions.TxnResponseDTO;
import com.kpevents.events_service.dtos.transactions.UserBalanceRespDTO;
import com.kpevents.events_service.entities.annadaan.AnnadaanBooking;
import com.kpevents.events_service.entities.enums.Committee;
import com.kpevents.events_service.entities.transactions.Transaction;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TxnRepository extends JpaRepository<Transaction, Long> {
    @Query("select t from Transaction t " +
            "left join fetch t.user " +
            "left join fetch t.donation d " +
            "where t.committee=:committee " +
            "and t.txnType='donation' " +
            "and ((t.txnSubType is null and d.year=:year ) " +
            "or t.txnSubType = 'additional') ")
    List<Transaction> getDonationsByCommitteeForYear(
            @Param("committee") Committee committee,
            @Param("year") Short year);

    @Query(value = "select d.building as building, " +
            "coalesce(sum(t.amount)) as amount " +
            "from transactions t " +
            "join donations d on t.id=d.txn_id " +
            "where t.committee=:committee " +
            "and d.year=:year " +
            "and t.txn_type='donation' " +
            "and t.txn_sub_type is null  " +
            "group by d.building " +
            "order by 1",nativeQuery = true)
    List<DonationStatsRespDTO> getDonationsStatsByCommitteeForYear(
            @Param("committee") String committee,
            @Param("year") Short year);

    @Query(value = "select t.txn_type,t.txn_sub_type," +
            " coalesce(sum(t.amount)) as amount " +
            "from transactions t " +
            "where t.committee=:committee " +
            "group by t.txn_type, t.txn_sub_type ",nativeQuery = true)
    List<CommitteeBalanceRespDTO> getCurrentBalanceByCommittee(
            @Param("committee") String committee);

    @Query(value = "select u.first_name,u.last_name," +
            "u.building||'-'||u.flat flat_number,u.clerk_id,u.image_url, txn_type, " +
            "txn_sub_type,s.amount as balance from" +
            "(select user_id, txn_type, txn_sub_type, coalesce(sum(amount)) as amount " +
            "from transactions " +
            "where committee=:committee " +
            "group by user_id,txn_type,txn_sub_type) s," +
            "users u where u.clerk_id=s.user_id " +
            "order by balance desc",nativeQuery = true)
    List<UserBalanceRespDTO> getCurrentBalanceByUserForCommittee(
            @Param("committee") String committee);



    @Query("select t from Transaction t " +
            "left join fetch t.user  " +
            "left join fetch t.donation  " +
            "left join fetch t.annadaanBookings b " +
            "left join fetch b.annadaanItem " +
            "where t.txnSubType='annadaan' " +
            "and b.year=:year")
    List<Transaction> getAnnadaanBookings(
            @Param("year") Short year
    );


    @Query("select t from Transaction t " +
            "left join fetch t.user  " +
            "left join fetch t.donation  " +
            "left join fetch t.annadaanBookings b " +
            "left join fetch b.annadaanItem " +
            "where t.id=:txnId")
    Optional<Transaction> getAnnadaanBookingsForTxn(@Param("txnId") String txnId);


    @Query("select t from Transaction t " +
            "left join fetch t.user " +
            "left join fetch t.donation " +
            "where t.committee=:committee " +
            "and t.txnType <> 'donation' ")
    List<Transaction> getOtherTxnsByCommittee(
            @Param("committee") Committee committee);


    @Query("select t from Transaction t " +
            "left join fetch t.user  " +
            "left join fetch t.donation  " +
            "left join fetch t.templeBookings b " +
            "left join fetch b.templeItem " +
            "where t.txnSubType='itemized' ")
    List<Transaction> getTempleBookings();
}
