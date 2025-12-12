package com.kpevents.events_service.entities.transactions;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kpevents.events_service.entities.enums.Building;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.UUID;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@Table(name = "donations")
public class Donation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @OneToOne
    @JoinColumn(name = "txn_id") // Join column name matches the primary key name
    @JsonIgnore
    @ToString.Exclude
    private Transaction txn;

    @Column(nullable = false)
    private String donorName;

    @Enumerated(EnumType.STRING)
    private Building building;

    @Column(nullable = false)
    private Short flat;

    @Column(nullable = false)
    private Short year;

    private Integer quantity;
}
