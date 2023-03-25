package com.codelap.api.service.study;

import com.codelap.api.service.study.dto.GetStudiesDto.GetStudiesStudyDto;
import com.codelap.common.study.domain.QStudy;
import com.codelap.common.user.domain.User;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.codelap.common.study.domain.StudyStatus.DELETED;
import static com.querydsl.core.types.Projections.constructor;

@Repository
@RequiredArgsConstructor
public class DefaultStudyQueryAppService implements StudyQueryAppService {
    private final JPAQueryFactory queryFactory;

    @Override
    public List<GetStudiesStudyDto> getStudies(User user) {
        return queryFactory
                .select(
                        constructor(
                                GetStudiesStudyDto.class,
                                QStudy.study.id,
                                QStudy.study.name,
                                QStudy.study.createdAt,
                                QStudy.study.status
                        )
                )
                .from(QStudy.study)
                .where(
                        QStudy.study.members.contains(user),
                        QStudy.study.status.ne(DELETED)
                )
                .fetch();
    }
}
