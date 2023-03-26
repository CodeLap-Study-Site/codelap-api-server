package com.codelap.common.study.domain;

import com.codelap.common.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StudyRepository extends JpaRepository<Study, Long> {
    List<Study> findByLeader(User leader);
}
