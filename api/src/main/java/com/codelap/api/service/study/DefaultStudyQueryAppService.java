package com.codelap.api.service.study;

import com.codelap.api.service.study.dto.GetAllStudiesStudyDto;
import com.codelap.api.service.study.dto.GetStudiesDto.GetStudiesStudyDto;
import com.codelap.common.bookmark.domain.QBookmark;
import com.codelap.common.study.domain.QStudy;
import com.codelap.common.study.domain.QStudyTechStack;
import com.codelap.common.studyComment.domain.QStudyComment;
import com.codelap.common.studyView.domain.QStudyView;
import com.codelap.common.user.domain.User;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.codelap.common.study.domain.StudyStatus.DELETED;
import static com.querydsl.core.types.ExpressionUtils.count;
import static com.querydsl.core.types.Projections.constructor;
import static com.querydsl.core.types.Projections.list;

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

    @Override
    public List<GetAllStudiesStudyDto> getAllStudies(){
        return queryFactory
                .select(
                        constructor(
                                GetAllStudiesStudyDto.class,
                                QStudy.study.id,
                                QStudy.study.name,
                                QStudy.study.period,
                                QStudy.study.leader.name,
                                ExpressionUtils.as(
                                        JPAExpressions.select(count(QStudyComment.studyComment.id))
                                                .from(QStudyComment.studyComment)
                                                .where(QStudyComment.studyComment.study.id.eq(QStudy.study.id)),
                                        "commentCount"
                                ),
                                ExpressionUtils.as(
                                        JPAExpressions.select(count(QStudyView.studyView.id))
                                                .from(QStudyView.studyView)
                                                .where(QStudyView.studyView.study.id.eq(QStudy.study.id)),
                                        "viewCount"
                                ),
                                ExpressionUtils.as(
                                        JPAExpressions.select(count(QBookmark.bookmark.id))
                                                .from(QBookmark.bookmark)
                                                .where(QBookmark.bookmark.study.id.eq(QStudy.study.id)),
                                        "bookmarkCount"
                                ),
                                QStudy.study.members.size(),
                                QStudy.study.maxMembersSize,
                                list(QStudyTechStack.studyTechStack.techStack)
                        )
                )
                .from(QStudy.study)
                .where(QStudy.study.status.ne(DELETED))
                .fetch();
    }
}
