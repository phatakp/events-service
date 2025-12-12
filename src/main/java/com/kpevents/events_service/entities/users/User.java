package com.kpevents.events_service.entities.users;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kpevents.events_service.entities.enums.Building;
import com.kpevents.events_service.entities.enums.UserRole;
import com.kpevents.events_service.entities.members.CommitteeMember;
import com.kpevents.events_service.entities.transactions.Transaction;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "users")
public class User {
    @Id
    private String clerkId;

    @Column(unique = true, nullable = false)
    private String email;

    private String firstName;

    private String lastName;

    private String imageUrl;

    @Enumerated(EnumType.STRING)
    private UserRole role;

    @Enumerated(EnumType.STRING)
    private Building building;

    private Short flat;

    @CreationTimestamp
    @JsonIgnore
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @JsonIgnore
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    @ToString.Exclude
    @JsonIgnore
    private List<CommitteeMember> committeeMembers = new ArrayList<>();

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    @ToString.Exclude
    @JsonIgnore
    private List<Transaction> txns = new ArrayList<>();
}

