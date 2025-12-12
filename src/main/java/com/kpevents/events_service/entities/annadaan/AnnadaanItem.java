package com.kpevents.events_service.entities.annadaan;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "annadaan_items")
public class AnnadaanItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String itemName;

    @NotNull
    private Float price;

    @NotNull
    private Float quantity;

    @NotNull
    private Float amount;

    @OneToMany(mappedBy = "annadaanItem", fetch = FetchType.LAZY,cascade = CascadeType.ALL,orphanRemoval = true)
    @JsonIgnore
    @ToString.Exclude
    private List<AnnadaanBooking> annadaanBookings = new ArrayList<>();

    @JsonIgnore
    public Float getBookedQty(){
        return this.getAnnadaanBookings().stream()
                .reduce(0.0f,
                        (partSum,i)->i.getQuantity()+partSum,Float::sum);
    }

}