package com.kpevents.events_service.controllers;

import com.kpevents.events_service.dtos.transactions.CommitteeBalanceRespDTO;
import com.kpevents.events_service.dtos.transactions.UserBalanceRespDTO;
import com.kpevents.events_service.entities.enums.Committee;
import com.kpevents.events_service.services.TxnService;
import com.kpevents.events_service.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Txn Module")
@RestController
@RequestMapping("/transactions")
@RequiredArgsConstructor
@Slf4j
public class TxnController {

    private final TxnService txnService;
    private final UserService userService;

    @Operation(summary = "Delete a transaction entry")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = @Content),
            @ApiResponse(responseCode = "404", description = "Txn not found", content = @Content),
            @ApiResponse(responseCode = "401", description = "Not Authenticated", content = @Content),
            @ApiResponse(responseCode = "403", description = "Not Authorized for operation", content = @Content),
    })
    @DeleteMapping("/{txnId}")
    public ResponseEntity<Void> deleteTxn(
            @Parameter(description = "Id of Txn", required = true)
            @PathVariable Long txnId) {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        userService.assertIsAdmin(authentication.getName(),"delete transaction");
        txnService.deleteTxn(txnId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @Operation(summary = "Get current balance of a committee")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = CommitteeBalanceRespDTO.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid data in input", content = @Content),
    })
    @GetMapping("/balance/{committee}")
    public ResponseEntity<List<CommitteeBalanceRespDTO>> getCurrentBalanceByCommittee(
            @Parameter(description = "Committee Name", required = true)
            @PathVariable Committee committee) {
        return ResponseEntity.ok(txnService.getCurrentBalanceByCommittee(committee));
    }


    @Operation(summary = "Get list of current balance of users by committee")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = UserBalanceRespDTO.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid data in input", content = @Content),
    })
    @GetMapping("/member-balance/{committee}")
    public ResponseEntity<List<UserBalanceRespDTO>> getCurrentBalanceByUserForCommittee(
            @Parameter(description = "Committee Name", required = true)
            @PathVariable Committee committee) {
        return ResponseEntity.ok(txnService.getCurrentBalanceByUserForCommittee(committee));
    }




}
