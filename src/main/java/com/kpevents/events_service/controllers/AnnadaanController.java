package com.kpevents.events_service.controllers;

import com.kpevents.events_service.dtos.annadaan.AnnadaanBookingRequestDTO;
import com.kpevents.events_service.dtos.annadaan.AnnadaanBookingRespDTO;
import com.kpevents.events_service.dtos.annadaan.AnnadaanItemRequestDTO;
import com.kpevents.events_service.dtos.annadaan.AnnadaanItemRespDTO;
import com.kpevents.events_service.services.AnnadaanService;
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

@Tag(name = "Annadaan Module")
@RestController
@RequestMapping("/transactions/annadaan")
@RequiredArgsConstructor
@Slf4j
public class AnnadaanController {

    private final TxnService txnService;
    private final UserService userService;
    private final AnnadaanService annadaanService;

    @Operation(summary = "Get list of available annadaan items for a given year")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = AnnadaanItemRespDTO.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid data in input", content = @Content),
    })
    @GetMapping("/items/{year}")
    public ResponseEntity<List<AnnadaanItemRespDTO>> getAnnadaanItemsByYear(
            @Parameter(description = "Year of Annadaan", required = true)
            @PathVariable Short year) {
        return ResponseEntity.ok(annadaanService.getAvailableAnnadaanItems(year));
    }


    @Operation(summary = "Get list of annadaan bookings for a given year")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = AnnadaanBookingRespDTO.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid data in input", content = @Content),
    })
    @GetMapping("/bookings/{year}")
    public ResponseEntity<List<AnnadaanBookingRespDTO>> getAnnadaanBookingsByYear(
            @Parameter(description = "Year of Annadaan", required = true)
            @PathVariable Short year) {
        return ResponseEntity.ok(txnService.getAnnadaanBookings(year));
    }




    @Operation(summary = "Confirm a annadaan booking")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = @Content),
            @ApiResponse(responseCode = "404", description = "Booking not found", content = @Content),
            @ApiResponse(responseCode = "401", description = "Not Authenticated", content = @Content),
            @ApiResponse(responseCode = "403", description = "Not Authorized for operation", content = @Content),
    })
    @PutMapping("/bookings/{txnId}")
    public ResponseEntity<Void> confirmBooking(
            @Parameter(description = "Id of Transaction", required = true)
            @PathVariable String txnId) {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        userService.assertIsAdmin(authentication.getName(),"confirm annadaan booking");
        txnService.confirmAnnadaanBooking(txnId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }


    @Operation(summary = "Add new annadaan item")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = AnnadaanItemRespDTO.class))}),
            @ApiResponse(responseCode = "401", description = "Not Authenticated", content = @Content),
            @ApiResponse(responseCode = "403", description = "Not Authorized for operation", content = @Content),
    })
    @PostMapping
    public ResponseEntity<AnnadaanItemRespDTO> addAnnadaanItem(
            @Valid @RequestBody AnnadaanItemRequestDTO request) {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        userService.assertIsAdmin(authentication.getName(),"add annadaan item");
        return ResponseEntity.ok(annadaanService.addNewAnnadaanItem(request));
    }


    @Operation(summary = "Update annadaan item")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = AnnadaanItemRespDTO.class))}),
            @ApiResponse(responseCode = "404", description = "Item not found", content = @Content),
            @ApiResponse(responseCode = "401", description = "Not Authenticated", content = @Content),
            @ApiResponse(responseCode = "403", description = "Not Authorized for operation", content = @Content),
    })
    @PutMapping("/{itemId}")
    public ResponseEntity<AnnadaanItemRespDTO> updateAnnadaanItem(
            @Parameter(description = "Id of Annadaan Item", required = true)
            @PathVariable Long itemId,
            @Valid @RequestBody AnnadaanItemRequestDTO request) {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        userService.assertIsAdmin(authentication.getName(),"update annadaan item");
        return ResponseEntity.ok(annadaanService.updateAnnadaanItem(request,itemId));
    }


    @Operation(summary = "Delete annadaan item")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = @Content),
            @ApiResponse(responseCode = "404", description = "Item not found", content = @Content),
            @ApiResponse(responseCode = "401", description = "Not Authenticated", content = @Content),
            @ApiResponse(responseCode = "403", description = "Not Authorized for operation", content = @Content),
    })
    @DeleteMapping("/{itemId}")
    public ResponseEntity<Void> deleteAnnadaanItem(
            @Parameter(description = "Id of Annadaan Item", required = true)
            @PathVariable Long itemId) {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        userService.assertIsAdmin(authentication.getName(),"delete annadaan item");
        annadaanService.deleteAnnadaanItem(itemId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }


    @Operation(summary = "Add new annadaan booking")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = AnnadaanItemRespDTO.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid Input", content = @Content),
    })
    @PostMapping("/bookings")
    public ResponseEntity<List<AnnadaanItemRespDTO>> addAnnadaanBooking(
            @Valid @RequestBody AnnadaanBookingRequestDTO request) {
        return ResponseEntity.ok(txnService.addNewAnnadaanBooking(request));
    }


    @Operation(summary = "Delete annadaan booking")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = @Content),
            @ApiResponse(responseCode = "404", description = "Booking not found", content = @Content),
            @ApiResponse(responseCode = "401", description = "Not Authenticated", content = @Content),
            @ApiResponse(responseCode = "403", description = "Not Authorized for operation", content = @Content),
    })
    @DeleteMapping("/bookings/{txnId}")
    public ResponseEntity<Void> deleteAnnadaanBooking(
            @Parameter(description = "Id of Transaction for Annadaan Booking", required = true)
            @PathVariable Long txnId) {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        userService.assertIsAdmin(authentication.getName(),"delete annadaan booking");
        txnService.deleteTxn(txnId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
