package com.kpevents.events_service.dtos.users;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserShortDTO {
    private String clerkId;
    private String firstName;
    private String lastName;
    private String imageUrl;
    private String flatNumber;
}
