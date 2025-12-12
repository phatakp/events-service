package com.kpevents.events_service.entities.transactions;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.kpevents.events_service.entities.annadaan.AnnadaanBooking;

import com.kpevents.events_service.entities.enums.Committee;
import com.kpevents.events_service.entities.enums.TxnSubType;
import com.kpevents.events_service.entities.enums.TxnMode;
import com.kpevents.events_service.entities.enums.TxnType;
import com.kpevents.events_service.entities.temple.TempleBooking;
import com.kpevents.events_service.entities.users.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Table(name="transactions")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String description;

    @NotNull(message = "Amount is required")
    private Float amount;

    @NotNull(message = "Date is required")
    private LocalDate date;

    @NotNull(message = "Txn Type is required")
    @Enumerated(EnumType.STRING)
    private TxnType txnType;

    @NotNull(message = "Txn Mode is required")
    @Enumerated(EnumType.STRING)
    private TxnMode txnMode;

    @CreationTimestamp
    @JsonIgnore
    private LocalDateTime createdAt;

    @Enumerated(EnumType.STRING)
    private TxnSubType txnSubType;

    @NotNull
    @Enumerated(EnumType.STRING)
    private Committee committee;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private User user;

    @OneToOne(mappedBy = "txn", cascade = CascadeType.ALL)
    @JsonIgnore
    private Donation donation;

    @OneToMany(mappedBy = "txn", fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    @ToString.Exclude
    @JsonIgnore
    private List<AnnadaanBooking> annadaanBookings = new ArrayList<>();

    @OneToMany(mappedBy = "txn", fetch = FetchType.LAZY,cascade = CascadeType.ALL,orphanRemoval = true)
    @ToString.Exclude
    @JsonIgnore
    private List<TempleBooking> templeBookings = new ArrayList<>();



}
