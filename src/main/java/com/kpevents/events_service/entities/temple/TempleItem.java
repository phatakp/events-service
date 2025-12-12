package com.kpevents.events_service.entities.temple;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "temple_requirements")
public class TempleItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String itemName;

    @NotNull
    private Float amount;

    @OneToMany(mappedBy = "templeItem", fetch = FetchType.LAZY,cascade = CascadeType.ALL,orphanRemoval = true)
    @JsonIgnore
    @ToString.Exclude
    private List<TempleBooking> templeBookings = new ArrayList<>();

    @JsonIgnore
    public Float getBookedAmount(){
        return this.getTempleBookings().stream()
                .reduce(0.0f,
                        (partSum,i)->i.getAmount()+partSum,Float::sum);
    }
}
