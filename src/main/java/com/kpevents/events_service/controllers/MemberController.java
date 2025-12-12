package com.kpevents.events_service.controllers;

import com.kpevents.events_service.dtos.members.MemberRequestDTO;
import com.kpevents.events_service.dtos.members.MemberResponseDTO;
import com.kpevents.events_service.entities.enums.Committee;
import com.kpevents.events_service.mappers.MemberMapper;
import com.kpevents.events_service.services.MemberService;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name="Member Module")
@RestController
@RequestMapping("/members")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;
    private final MemberMapper memberMapper;
    private final UserService userService;



    @Operation(summary = "Add new member to committee")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = MemberResponseDTO.class))}),
            @ApiResponse(responseCode = "401", description = "Not Authenticated", content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid data in input", content = @Content),
            @ApiResponse(responseCode = "403", description = "Not authorized for operation", content = @Content),
    })
    @PostMapping
    public ResponseEntity<MemberResponseDTO> createMember(
            @Valid @RequestBody MemberRequestDTO request
    ) {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        memberService.assertOwner(authentication.getName(), request.getUserId(),"add committee member");
        var member = memberService.addMember(request);
        return ResponseEntity.ok(memberMapper.mapToResponseDTO(member));
    }



    @Operation(summary = "Check if logged in user is active member of a committee")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = MemberResponseDTO.class))}),
            @ApiResponse(responseCode = "401", description = "Not Authenticated", content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid data in input", content = @Content),
    })
    @GetMapping("/check/{committee}")
    public ResponseEntity<MemberResponseDTO> isCommitteeMember(
            @Parameter(description = "Committee Name",required = true)
            @PathVariable Committee committee
    ) {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        var member = memberService.isCommitteeMember(authentication.getName(), committee);
        return ResponseEntity.ok(memberMapper.mapToResponseDTO(member));
    }



    @Operation(summary = "Get all members of a committee")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = MemberResponseDTO.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid data in input", content = @Content),
    })
    @GetMapping("/{committee}")
    public ResponseEntity<List<MemberResponseDTO>> getMembers(
            @Parameter(description = "Committee Name",required = true)
            @PathVariable Committee committee
    ) {
        return ResponseEntity.ok(memberService.getMembersByCommittee(committee));
    }


    @Operation(summary = "Get all inactive members of a committee")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = MemberResponseDTO.class))}),
            @ApiResponse(responseCode = "401", description = "Not Authenticated", content = @Content),
            @ApiResponse(responseCode = "403", description = "Not authorized for operation", content = @Content),
    })
    @GetMapping("/pending")
    public ResponseEntity<List<MemberResponseDTO>> getPendingMembers() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        userService.assertIsAdmin(authentication.getName(), "get pending members");
        return ResponseEntity.ok(memberService.getPendingMembers());
    }



    @Operation(summary = "Approve a request to become member of a committee")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = @Content),
            @ApiResponse(responseCode = "401", description = "Not Authenticated", content = @Content),
            @ApiResponse(responseCode = "404", description = "Member Not Found", content = @Content),
            @ApiResponse(responseCode = "403", description = "Not authorized for operation", content = @Content),
    })
    @PutMapping("/approve/{memberId}")
    public ResponseEntity<Void> approveMember(
            @PathVariable("memberId") Long memberId
    ) {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        userService.assertIsAdmin(authentication.getName(), "approve member");
        memberService.approveMember(memberId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }


    @Operation(summary = "Delete a pending request to become member of a committee")
    @ApiResponses({
            @ApiResponse(responseCode = "204", content = @Content),
            @ApiResponse(responseCode = "401", description = "Not Authenticated", content = @Content),
            @ApiResponse(responseCode = "404", description = "Member Not Found", content = @Content),
            @ApiResponse(responseCode = "403", description = "Not authorized for operation", content = @Content),
    })
    @DeleteMapping("/{memberId}")
    public ResponseEntity<Void> deleteMember(
            @PathVariable("memberId") Long memberId
    ) {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        userService.assertIsAdmin(authentication.getName(),"delete member");
        memberService.deleteMember(memberId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
