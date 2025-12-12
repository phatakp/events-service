package com.kpevents.events_service.dtos.members;

import com.kpevents.events_service.entities.enums.Committee;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MemberRequestDTO {
    @NotNull
    @NotBlank(message = "User Id is required")
    private String userId;

    @NotNull(message = "Committee is required")
    private Committee committee;
}
