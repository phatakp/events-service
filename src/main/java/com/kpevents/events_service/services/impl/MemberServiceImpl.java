package com.kpevents.events_service.services.impl;

import com.kpevents.events_service.config.exceptions.APIException;
import com.kpevents.events_service.dtos.members.MemberRequestDTO;
import com.kpevents.events_service.dtos.members.MemberResponseDTO;
import com.kpevents.events_service.entities.enums.Committee;
import com.kpevents.events_service.entities.members.CommitteeMember;
import com.kpevents.events_service.entities.users.User;
import com.kpevents.events_service.mappers.MemberMapper;
import com.kpevents.events_service.repositories.MemberRepository;
import com.kpevents.events_service.repositories.UserRepository;
import com.kpevents.events_service.services.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {
    private final MemberRepository memberRepository;
    private final UserRepository userRepository;
    private final MemberMapper memberMapper;

    @Override
    public CommitteeMember addMember(MemberRequestDTO request) {
        var user = userRepository.findByClerkId(request.getUserId())
                .orElse(null);
        if (user == null) {
            throw APIException.notFound("User not found");
        }
        var member = CommitteeMember.builder()
                .committee(request.getCommittee())
                .user(user)
                .isActive(false)
                .build();

        return memberRepository.save(member);
    }

    @Override
    public List<MemberResponseDTO> getMembersByCommittee(Committee committee) {
        var members = memberRepository.getMembersByCommittee(committee);
        return members.stream().map(memberMapper::mapToResponseDTO).toList();
    }

    @Override
    public List<MemberResponseDTO> getPendingMembers() {
        var members = memberRepository.getPendingMembers();
        return members.stream().map(memberMapper::mapToResponseDTO).toList();
    }

    @Override
    public CommitteeMember isCommitteeMember(String clerkId, Committee committee) {
        return memberRepository.isCommitteeMember(clerkId, committee).orElse(null);
    }

    @Override
    public void approveMember(Long id) {
        var member = memberRepository.findById(id).orElse(null);
        if (member == null) {
            throw APIException.notFound("Member not found");
        }
        member.setIsActive(true);
        memberRepository.save(member);
    }

    @Override
    public void deleteMember(Long id) {
        var member = memberRepository.findById(id).orElse(null);
        if (member == null) {
            throw APIException.notFound("Member not found");
        }
        memberRepository.delete(member);
    }

    @Override
    public void assertOwner(String loggedInUserId, String clerkId, String action) {
        if (!loggedInUserId.equals(clerkId)) {
            throw APIException.unAuthorized(action);
        }
    }

    @Override
    public void assertCommitteeMember(Committee committee, String clerkId, String action) {
        var member = memberRepository.isCommitteeMember(clerkId, committee).orElse(null);
        if (member == null || member.getIsActive()==false) {
            throw APIException.unAuthorized(action);
        }
    }

    @Override
    public User validateCommitteeMember(String userId, Committee committee) {
        var member = memberRepository.validateCommitteeMember(userId,committee).orElse(null);

        if (member == null) {
            throw APIException.notFound("User not a member: " + userId + ' ' + committee.name());
        }
        return member.getUser();
    }
}
