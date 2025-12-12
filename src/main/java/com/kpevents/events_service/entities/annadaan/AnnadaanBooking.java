package com.kpevents.events_service.entities.annadaan;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.kpevents.events_service.entities.transactions.Transaction;
import com.kpevents.events_service.entities.enums.Building;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "annadaan_bookings")
public class AnnadaanBooking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(nullable = false)
    private String bookingName;

    @NotNull
    @Enumerated(EnumType.STRING)
    private Building building;

    @NotNull
    private Short flat;

    @NotNull
    private Float quantity;

    @NotNull
    private Float amount;

    @NotNull
    private Boolean isConfirmed = false;

    @NotNull
    private Short year;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id", nullable = false)
    @JsonIgnore
    @ToString.Exclude
    private AnnadaanItem annadaanItem;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "txn_id", nullable = false)
    @JsonIgnore
    @ToString.Exclude
    private Transaction txn;

}