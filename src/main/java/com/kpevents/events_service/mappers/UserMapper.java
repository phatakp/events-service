package com.kpevents.events_service.mappers;

import com.kpevents.events_service.dtos.users.UserRequestDTO;
import com.kpevents.events_service.dtos.users.UserResponseDTO;
import com.kpevents.events_service.dtos.users.UserShortDTO;
import com.kpevents.events_service.entities.users.User;
import com.kpevents.events_service.entities.enums.UserRole;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public static User updateUser(User user,UserRequestDTO request) {
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setBuilding(request.getBuilding());
        user.setFlat(request.getFlat());
        return user;
    }

    public static User mapToEntity(UserRequestDTO user) {
        return User
                .builder()
                .clerkId(user.getClerkId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .role(UserRole.USER)
                .imageUrl(user.getImageUrl())
                .building(user.getBuilding())
                .flat(user.getFlat())
                .build();
    }

    public UserResponseDTO mapToResponseDTO(User user) {
        if (user==null) {
            return null;
        }

        return UserResponseDTO
                .builder()
                .clerkId(user.getClerkId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .role(user.getRole())
                .imageUrl(user.getImageUrl())
                .flatNumber(user.getBuilding()+"-"+user.getFlat())
                .build();
    }

    public UserShortDTO mapToUserShortDTO(User user) {
        return UserShortDTO.builder()
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .clerkId(user.getClerkId())
                .flatNumber(user.getBuilding()+"-"+user.getFlat())
                .imageUrl(user.getImageUrl())
                .build();
    }
}
