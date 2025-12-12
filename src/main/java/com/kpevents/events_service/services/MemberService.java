package com.kpevents.events_service.services;

import com.kpevents.events_service.dtos.members.MemberRequestDTO;
import com.kpevents.events_service.dtos.members.MemberResponseDTO;
import com.kpevents.events_service.entities.enums.Committee;
import com.kpevents.events_service.entities.members.CommitteeMember;
import com.kpevents.events_service.entities.users.User;

import java.util.List;

public interface MemberService {
    CommitteeMember addMember(MemberRequestDTO request);
    List<MemberResponseDTO> getMembersByCommittee(Committee committee);
    List<MemberResponseDTO> getPendingMembers();
    CommitteeMember isCommitteeMember(String clerkId, Committee committee);
    void approveMember(Long id);
    void deleteMember(Long id);
    void assertOwner(String loggedInUserId, String clerkId, String action);
    void assertCommitteeMember(Committee committee, String clerkId, String action);
    User validateCommitteeMember(String userId, Committee committee);
}
