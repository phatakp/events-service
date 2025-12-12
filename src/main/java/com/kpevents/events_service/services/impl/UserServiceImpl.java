package com.kpevents.events_service.services.impl;


import com.kpevents.events_service.config.exceptions.APIException;
import com.kpevents.events_service.dtos.users.UserRequestDTO;
import com.kpevents.events_service.entities.users.User;
import com.kpevents.events_service.entities.enums.UserRole;
import com.kpevents.events_service.mappers.UserMapper;
import com.kpevents.events_service.repositories.UserRepository;
import com.kpevents.events_service.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public User saveUserToDB(UserRequestDTO request) {

        var user = userRepository.findByClerkId(request.getClerkId());
        if (user.isPresent()) {
            User existingUser = user.get();
            var updatedUser = UserMapper.updateUser(existingUser, request);
            return userRepository.save(updatedUser);
        }

        User newUser = UserMapper.mapToEntity(request);
        return userRepository.save(newUser);
    }

    @Override
    public User getUserByClerkId(String userId) {
        return userRepository.findByClerkId(userId).orElse(null);
    }

    @Override
    public void assertIsAdmin(String clerkId, String action) {
        if (!userRepository.existsByClerkIdAndRole(clerkId, UserRole.ADMIN)){
            throw APIException.unAuthorized(action);
        }
    }

    @Override
    public void assertIsOwner(String loggedInUserId, String clerkId, String action) {
        if (!loggedInUserId.equals(clerkId)){
            throw APIException.unAuthorized(action);
        }
    }

    @Override
    public User validateUser(String clerkId) {
        var user = userRepository.findByClerkId(clerkId).orElse(null);
        if(user == null){
            throw APIException.notFound("User not found: " + clerkId);
        }
        return user;
    }
}
