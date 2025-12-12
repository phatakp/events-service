package com.kpevents.events_service.entities.temple;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kpevents.events_service.entities.enums.Building;
import com.kpevents.events_service.entities.transactions.Transaction;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "temple_bookings")
public class TempleBooking {
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
    private Float amount;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id", nullable = false)
    @JsonIgnore
    @ToString.Exclude
    private TempleItem templeItem;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "txn_id", nullable = false)
    @JsonIgnore
    @ToString.Exclude
    private Transaction txn;
}
