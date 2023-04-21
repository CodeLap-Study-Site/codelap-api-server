package com.codelap.common.study.domain;


import com.codelap.common.study.dto.GetMyStudiesDto;
import com.codelap.common.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface StudyRepository extends JpaRepository<Study, Long> {
    List<Study> findByLeader(User leader);

    @Query("SELECT new com.codelap.common.study.dto.GetMyStudiesDto(s.id, s.name, s.period, s.leader.name, count(c), count(v), count(b), s.maxMembersSize) " +
            "FROM Study s " +
            "JOIN s.members m LEFT JOIN s.comments c LEFT JOIN s.views v LEFT JOIN s.bookmarks b " +
            "WHERE m = :user " +
            "AND s.status <> 'DELETED' group by s")
    List<GetMyStudiesDto> getAttendedStudiesByUser(@Param("user") User user);

    @Query("SELECT t FROM Study s JOIN s.techStackList t WHERE s = :study")
    List<TechStack> getTechStacks(@Param("study") Study study);
}
