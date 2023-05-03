package com.codelap.api.service.user;

import com.codelap.common.user.domain.QUser;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class DefaultUserQueryDslAppService implements UserQueryAppService{

    private final JPAQueryFactory queryFactory;

    @Override
    public boolean getDuplicateCheckByName(String name) {

        return Optional
                .ofNullable(
                        queryFactory
                            .selectFrom(QUser.user)
                            .where(QUser.user.name.eq(name))
                            .fetchOne()
                ).isPresent();
    }
}
