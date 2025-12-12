package com.kpevents.events_service.repositories;

import com.kpevents.events_service.entities.users.User;
import com.kpevents.events_service.entities.enums.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,String> {
    Boolean existsByClerkIdAndRole(String clerkId, UserRole role);
    Optional<User> findByClerkId(String userId);
}
