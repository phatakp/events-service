package com.kpevents.events_service.controllers;

import com.kpevents.events_service.dtos.transactions.OtherTxnRequestDTO;
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
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Expense Module")
@RestController
@RequestMapping("/transactions/expenses")
@RequiredArgsConstructor
@Slf4j
public class OtherTxnController {
    private final MemberService memberService;
    private final TxnService txnService;
    private final UserService userService;

    @Operation(summary = "Create new expense/transfer entry")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = TxnResponseDTO.class))}),
            @ApiResponse(responseCode = "401", description = "Not Authenticated", content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid data in input", content = @Content),
            @ApiResponse(responseCode = "403", description = "Not authorized for operation", content = @Content),
    })
    @PostMapping
    public ResponseEntity<List<TxnResponseDTO>> createExpenseOrTransfer(
            @Valid @RequestBody OtherTxnRequestDTO request) {
        // Check User is Authenticated and Member of Committee
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        memberService.assertCommitteeMember(request.getCommittee(), authentication.getName(), "create expense/transfer entry");
        return ResponseEntity.ok(txnService.addExpenseOrTransfer(request));
    }


    @Operation(summary = "Update an expense/transfer entry")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = TxnResponseDTO.class))}),
            @ApiResponse(responseCode = "404", description = "Txn not found", content = @Content),
            @ApiResponse(responseCode = "401", description = "Not Authenticated", content = @Content),
            @ApiResponse(responseCode = "403", description = "Not Authorized for operation", content = @Content),
    })
    @PutMapping("/{txnId}")
    public ResponseEntity<TxnResponseDTO> updateExpenseOrTransfer(
            @Parameter(description = "Id of Txn", required = true)
            @PathVariable Long txnId,
            @Valid @RequestBody OtherTxnRequestDTO request) {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        userService.assertIsAdmin(authentication.getName(),"update expense entry");

        return ResponseEntity.ok(txnService.updateExpense(txnId, request));
    }


    @Operation(summary = "Get list of expenses/transfers by committee")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = TxnResponseDTO.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid data in input", content = @Content),
    })
    @GetMapping("/{committee}")
    public ResponseEntity<List<TxnResponseDTO>> getOtherTxnByCommittee(
            @Parameter(description = "Committee Name", required = true)
            @PathVariable Committee committee) {
        return ResponseEntity.ok(txnService.getOtherTxnsByCommittee(committee));
    }
}
