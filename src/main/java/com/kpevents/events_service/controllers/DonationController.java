package com.kpevents.events_service.controllers;

import com.kpevents.events_service.dtos.transactions.DonationRequestDTO;
import com.kpevents.events_service.dtos.transactions.DonationStatsRespDTO;
import com.kpevents.events_service.dtos.transactions.TxnResponseDTO;
import com.kpevents.events_service.entities.enums.Committee;
import com.kpevents.events_service.services.MemberService;
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
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Donation Module")
@RestController
@RequestMapping("/transactions/donations")
@RequiredArgsConstructor
@Slf4j
public class DonationController {
    private final MemberService memberService;
    private final TxnService txnService;
    private final UserService userService;

    @Operation(summary = "Create new donation entry")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = TxnResponseDTO.class))}),
            @ApiResponse(responseCode = "401", description = "Not Authenticated", content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid data in input", content = @Content),
            @ApiResponse(responseCode = "403", description = "Not authorized for operation", content = @Content),
    })
    @PostMapping
    public ResponseEntity<TxnResponseDTO> createDonation(
            @Valid @RequestBody DonationRequestDTO request) {
        // Check User is Authenticated and Member of Committee
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        memberService.assertCommitteeMember(request.getCommittee(), authentication.getName(), "add donation entry");
        return ResponseEntity.ok(txnService.addDonation(request));
    }


    @Operation(summary = "Update a donation entry")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = TxnResponseDTO.class))}),
            @ApiResponse(responseCode = "404", description = "Donation not found", content = @Content),
            @ApiResponse(responseCode = "401", description = "Not Authenticated", content = @Content),
            @ApiResponse(responseCode = "403", description = "Not Authorized for operation", content = @Content),
    })
    @PutMapping("/{txnId}")
    public ResponseEntity<TxnResponseDTO> updateDonation(
            @Parameter(description = "Id of Donation Txn", required = true)
            @PathVariable Long txnId,
            @Valid @RequestBody DonationRequestDTO request) {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        userService.assertIsAdmin(authentication.getName(),"update donation entry");

        return ResponseEntity.ok(txnService.updateDonation(txnId, request));
    }


    @Operation(summary = "Get total donations by building for committee for particular year")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = DonationStatsRespDTO.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid data in input", content = @Content),
    })
    @GetMapping("/stats/{committee}/{year}")
    public ResponseEntity<List<DonationStatsRespDTO>> getDonationStatsByCommitteeForYear(
            @Parameter(description = "Committee Name", required = true)
            @PathVariable Committee committee,

            @Parameter(description = "Year of donation", required = true)
            @PathVariable Short year) {
        return ResponseEntity.ok(txnService.getDonationsStatsByCommitteeForYear(committee, year));
    }


    @Operation(summary = "Get list of donations by committee for particular year")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = TxnResponseDTO.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid data in input", content = @Content),
            @ApiResponse(responseCode = "401", description = "Not Authenticated", content = @Content),
            @ApiResponse(responseCode = "403", description = "Not Authorized for operation", content = @Content),
    })
    @GetMapping("/{committee}/{year}")
    public ResponseEntity<List<TxnResponseDTO>> getDonationsByCommitteeForYear(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,

            @Parameter(description = "Committee Name", required = true)
            @PathVariable Committee committee,

            @Parameter(description = "Year of donation", required = true)
            @PathVariable short year) {
        // Check User is Authenticated and Member of Committee
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        memberService.assertCommitteeMember(committee, authentication.getName(), "get donations");
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(txnService.getDonationsByCommitteeForYear(committee, year, pageable));
    }


}
