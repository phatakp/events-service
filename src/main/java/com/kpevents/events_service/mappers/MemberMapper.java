package com.kpevents.events_service.mappers;

import com.kpevents.events_service.dtos.members.MemberResponseDTO;
import com.kpevents.events_service.entities.members.CommitteeMember;
import org.springframework.stereotype.Component;

@Component
public class MemberMapper {
    private final UserMapper userMapper;

    public MemberMapper(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    public MemberResponseDTO mapToResponseDTO(CommitteeMember member) {
        if (member == null) return null;
        return MemberResponseDTO
                .builder()
                .id(member.getId())
                .committee(member.getCommittee())
                .isActive(member.getIsActive())
                .user(userMapper.mapToResponseDTO(member.getUser()))
                .build();
    }
}
