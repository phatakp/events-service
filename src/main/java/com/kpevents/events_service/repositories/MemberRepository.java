package com.kpevents.events_service.repositories;

import com.kpevents.events_service.entities.enums.Committee;
import com.kpevents.events_service.entities.members.CommitteeMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<CommitteeMember, Long> {

    @Query("select m from CommitteeMember m " +
            "left join fetch m.user " +
            "where m.committee=:committee " +
            "and m.user.clerkId=:clerkId")
    Optional<CommitteeMember> isCommitteeMember(@Param("clerkId") String clerkId,
                                               @Param("committee") Committee committee);

    @Query("select m from CommitteeMember m " +
            "left join fetch m.user " +
            "where m.committee=:committee")
    List<CommitteeMember> getMembersByCommittee(@Param("committee") Committee committee);

    @Query("select m from CommitteeMember m " +
            "left join fetch m.user " +
            "where m.isActive=FALSE ")
    List<CommitteeMember> getPendingMembers();

    @Query(value = "select m from CommitteeMember m " +
            "join fetch m.user " +
            "where m.committee=:committee " +
            "and m.user.clerkId=:userId " +
            "and m.isActive=TRUE")
    Optional<CommitteeMember> validateCommitteeMember(
            @Param("userId") String userId,
            @Param("committee") Committee committee);

}
