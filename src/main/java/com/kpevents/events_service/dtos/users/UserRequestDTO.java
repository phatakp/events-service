package com.kpevents.events_service.dtos.users;

import com.kpevents.events_service.utils.SlugUtil;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserRequestDTO extends FlatNumberDTO {
    @NotNull
    @NotBlank(message = "Clerk ID is required")
    private String clerkId;

    @NotNull
    @NotBlank(message = "Email is required")
    private String email;

    @NotNull
    @NotBlank(message = "First Name is required")
    private String firstName;

    @NotNull
    @NotBlank(message = "Last Name is required")
    private String lastName;

    private String imageUrl;



}
