package com.kpevents.events_service.services;

import com.kpevents.events_service.dtos.users.UserRequestDTO;
import com.kpevents.events_service.entities.users.User;

public interface UserService {
    User saveUserToDB(UserRequestDTO request);

    User getUserByClerkId(String userId);

    void assertIsAdmin(String clerkId, String action);

    void assertIsOwner(String loggedInUserId, String clerkId, String action);

    User validateUser(String clerkId);

}
