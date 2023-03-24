package com.codelap.common.user.domain;

import com.codelap.common.study.domain.Study;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.codelap.common.study.domain.QStudy.study;
import static com.codelap.common.user.domain.QUser.user;
import static com.codelap.common.user.domain.UserStatus.DELETED;

@Repository
public class UserJpaRepository {
    private final EntityManager em;
    private final JPAQueryFactory queryFactory;

    public UserJpaRepository(EntityManager em) {
        this.em = em;
        this.queryFactory = new JPAQueryFactory(em);
    }

    public List<Study> findStudyListByUserId(User attendUser) {
        return queryFactory
                .select(study)
                .from(study)
                .leftJoin(study.members, QUser.user)
                .where(user.eq(attendUser))
                .where(user.status.ne(DELETED))
                .fetch();
    }
}
