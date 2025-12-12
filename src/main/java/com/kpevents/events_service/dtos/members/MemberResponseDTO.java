package com.kpevents.events_service.dtos.members;

import com.kpevents.events_service.dtos.users.UserResponseDTO;
import com.kpevents.events_service.entities.enums.Committee;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MemberResponseDTO {
    private Long id;
    private Committee committee;
    private Boolean isActive;
    private UserResponseDTO user;
}
