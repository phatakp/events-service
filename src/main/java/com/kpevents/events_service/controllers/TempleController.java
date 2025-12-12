package com.kpevents.events_service.controllers;

import com.kpevents.events_service.dtos.temple.TempleBookingRequestDTO;
import com.kpevents.events_service.dtos.temple.TempleBookingRespDTO;
import com.kpevents.events_service.dtos.temple.TempleItemRequestDTO;
import com.kpevents.events_service.dtos.temple.TempleItemRespDTO;
import com.kpevents.events_service.services.TempleService;
import com.kpevents.events_service.services.TxnService;
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
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Temple Module")
@RestController
@RequestMapping("/transactions/temple")
@RequiredArgsConstructor
@Slf4j
public class TempleController {
    private final TxnService txnService;
    private final UserService userService;
    private final TempleService templeService;

    @Operation(summary = "Get list of available temple itemized requirements")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = TempleItemRespDTO.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid data in input", content = @Content),
    })
    @GetMapping("/items")
    public ResponseEntity<List<TempleItemRespDTO>> getTempleItems() {
        return ResponseEntity.ok(templeService.getAvailableItems());
    }


    @Operation(summary = "Get list of temple bookings")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = TempleBookingRespDTO.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid data in input", content = @Content),
    })
    @GetMapping("/bookings")
    public ResponseEntity<List<TempleBookingRespDTO>> getTempleBookings() {
        return ResponseEntity.ok(txnService.getTempleBookings());
    }


    @Operation(summary = "Add new temple item")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = TempleItemRespDTO.class))}),
            @ApiResponse(responseCode = "401", description = "Not Authenticated", content = @Content),
            @ApiResponse(responseCode = "403", description = "Not Authorized for operation", content = @Content),
    })
    @PostMapping
    public ResponseEntity<TempleItemRespDTO> addTempleItem(
            @Valid @RequestBody TempleItemRequestDTO request) {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        userService.assertIsAdmin(authentication.getName(),"add temple item");
        return ResponseEntity.ok(templeService.addNewTempleItem(request));
    }


    @Operation(summary = "Update temple item")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = TempleItemRespDTO.class))}),
            @ApiResponse(responseCode = "404", description = "Item not found", content = @Content),
            @ApiResponse(responseCode = "401", description = "Not Authenticated", content = @Content),
            @ApiResponse(responseCode = "403", description = "Not Authorized for operation", content = @Content),
    })
    @PutMapping("/{itemId}")
    public ResponseEntity<TempleItemRespDTO> updateTempleItem(
            @Parameter(description = "Id of Temple Item", required = true)
            @PathVariable Long itemId,
            @Valid @RequestBody TempleItemRequestDTO request) {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        userService.assertIsAdmin(authentication.getName(),"update temple item");
        return ResponseEntity.ok(templeService.updateTempleItem(request,itemId));
    }


    @Operation(summary = "Delete temple item")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = @Content),
            @ApiResponse(responseCode = "404", description = "Item not found", content = @Content),
            @ApiResponse(responseCode = "401", description = "Not Authenticated", content = @Content),
            @ApiResponse(responseCode = "403", description = "Not Authorized for operation", content = @Content),
    })
    @DeleteMapping("/{itemId}")
    public ResponseEntity<Void> deleteTempleItem(
            @Parameter(description = "Id of Temple Item", required = true)
            @PathVariable Long itemId) {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        userService.assertIsAdmin(authentication.getName(),"delete temple item");
        templeService.deleteTempleItem(itemId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }


    @Operation(summary = "Add new temple booking")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = TempleItemRespDTO.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid Input", content = @Content),
    })
    @PostMapping("/bookings")
    public ResponseEntity<List<TempleItemRespDTO>> addTempleBooking(
            @Valid @RequestBody TempleBookingRequestDTO request) {
        return ResponseEntity.ok(txnService.addNewTempleBooking(request));
    }

    @Operation(summary = "Delete temple booking")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = @Content),
            @ApiResponse(responseCode = "404", description = "Booking not found", content = @Content),
            @ApiResponse(responseCode = "401", description = "Not Authenticated", content = @Content),
            @ApiResponse(responseCode = "403", description = "Not Authorized for operation", content = @Content),
    })
    @DeleteMapping("/bookings/{bookingId}")
    public ResponseEntity<Void> deleteTempleBooking(
            @Parameter(description = "Id of Temple Booking", required = true)
            @PathVariable Long bookingId) {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        userService.assertIsAdmin(authentication.getName(),"delete temple booking");
        templeService.deleteTempleBooking(bookingId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
