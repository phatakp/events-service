package com.kpevents.events_service.controllers;

import com.kpevents.events_service.dtos.users.UserRequestDTO;
import com.kpevents.events_service.dtos.users.UserResponseDTO;
import com.kpevents.events_service.entities.users.User;
import com.kpevents.events_service.mappers.UserMapper;
import com.kpevents.events_service.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@Tag(name="User Module")
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;

    @Operation(summary = "Create new User Profile")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = UserResponseDTO.class))}),
            @ApiResponse(responseCode = "401", description = "Not Authenticated", content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid data in input", content = @Content),
    })
    @PostMapping
    public ResponseEntity<UserResponseDTO> createUser(
            @Valid @RequestBody UserRequestDTO request
    ) {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        userService.assertIsOwner(authentication.getName(), request.getClerkId(), "create user profile");
        User user = userService.saveUserToDB(request);
        return ResponseEntity.ok(userMapper.mapToResponseDTO(user));
    }



    @Operation(summary = "Update User Profile")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = UserResponseDTO.class))}),
            @ApiResponse(responseCode = "401", description = "Not Authenticated", content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid data in input", content = @Content),
            @ApiResponse(responseCode = "403", description = "Logged in User is not the owner of the record", content = @Content),
    })
    @PutMapping
    public ResponseEntity<UserResponseDTO> updateUser(
            @Valid @RequestBody UserRequestDTO request
    ) {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        userService.assertIsOwner(authentication.getName(), request.getClerkId(), "update user profile");
        User user = userService.saveUserToDB(request);
        return ResponseEntity.ok(userMapper.mapToResponseDTO(user));
    }



    @Operation(summary = "Get User Profile By User ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {@Content(mediaType = "application/json",
            schema = @Schema(implementation = UserResponseDTO.class))}),
            @ApiResponse(responseCode = "401", description = "Not Authenticated", content = @Content),
            @ApiResponse(responseCode = "403", description = "Logged in User is not the owner of the record", content = @Content),
    })
    @GetMapping("/{userId}")
    public ResponseEntity<UserResponseDTO> getUserById(
            @Parameter(description = "ID of the user to be retrieved",required = true)
            @PathVariable(name = "userId") String userId) {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        userService.assertIsOwner(authentication.getName(), userId, "get user profile");
        User user = userService.getUserByClerkId(userId);
        return ResponseEntity.ok(userMapper.mapToResponseDTO(user));
    }
}
