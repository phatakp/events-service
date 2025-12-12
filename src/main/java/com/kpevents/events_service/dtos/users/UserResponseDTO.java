package com.kpevents.events_service.dtos.users;

import com.kpevents.events_service.entities.enums.UserRole;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserResponseDTO {
    private String clerkId;
    private String email;
    private String firstName;
    private String lastName;
    private String imageUrl;
    private String flatNumber;
    private UserRole role;
}
