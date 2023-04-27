package com.codelap.api.service.study;

import com.codelap.api.service.study.dto.GetStudiesDto;
import com.codelap.common.bookmark.domain.QBookmark;
import com.codelap.common.study.domain.QStudy;
import com.codelap.common.study.domain.StudyRepository;
import com.codelap.common.study.dto.GetOpenedStudiesDto;
import com.codelap.common.studyComment.domain.QStudyComment;
import com.codelap.common.studyView.domain.QStudyView;
import com.codelap.common.user.domain.User;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.codelap.common.study.domain.StudyStatus.DELETED;
import static com.codelap.common.study.dto.GetStudiesCardDto.GetStudyInfo;
import static com.codelap.common.study.dto.GetStudiesCardDto.GetTechStackInfo;
import static com.querydsl.core.types.ExpressionUtils.count;
import static com.querydsl.core.types.Projections.constructor;

@Repository
@Primary
@RequiredArgsConstructor
public class DefaultStudyQueryDslAppService implements StudyQueryAppService {

    private final JPAQueryFactory queryFactory;

    private final StudyRepository studyRepository;
    @Override
    public List<GetStudiesDto.GetStudiesStudyDto> getStudies(User user) {
        return null;
    }
    @Override
    public List<GetStudyInfo> getAttendedStudiesByUser(User user) {
        return queryFactory
                .select(
                constructor(
                        GetStudyInfo.class,
                        QStudy.study.id,
                        QStudy.study.name,
                        QStudy.study.period,
                        QStudy.study.leader.name,
                        ExpressionUtils.as(JPAExpressions
                                        .select(count(QStudyComment.studyComment.id))
                                        .from(QStudyComment.studyComment)
                                        .where(QStudyComment.studyComment.study.id.eq(QStudy.study.id)),
                                "commentCount"
                        ),
                        ExpressionUtils.as(JPAExpressions
                                        .select(count(QStudyView.studyView.id))
                                        .from(QStudyView.studyView)
                                        .where(QStudyView.studyView.study.id.eq(QStudy.study.id)),
                                "viewCount"
                        ),
                        ExpressionUtils.as(JPAExpressions
                                        .select(count(QBookmark.bookmark.id))
                                        .from(QBookmark.bookmark)
                                        .where(QBookmark.bookmark.study.id.eq(QStudy.study.id)),
                                "bookmarkCount"
                        ),
                        QStudy.study.maxMembersSize))
                .from(QStudy.study)
                .where(QStudy.study.status.ne(DELETED))
                .where(QStudy.study.members.contains(user))
                .fetch();
    }

    @Override
    public List<GetTechStackInfo> getTechStacks(List<Long> studyIds) {
        return studyRepository.getTechStacks(studyIds);
    }

    @Override
    public List<GetOpenedStudiesDto> getOpenedStudies() {
        return null;
    }
}
