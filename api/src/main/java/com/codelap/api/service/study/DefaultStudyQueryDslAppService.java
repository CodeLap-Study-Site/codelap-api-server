package com.codelap.api.service.study;

import com.codelap.api.service.study.dto.GetStudiesDto.GetStudiesStudyDto;
import com.codelap.api.service.study.support.DynamicCond;
import com.codelap.common.bookmark.domain.QBookmark;
import com.codelap.common.study.domain.QStudy;
import com.codelap.common.study.domain.QStudyTechStack;
import com.codelap.common.study.dto.GetOpenedStudiesDto;
import com.codelap.common.studyComment.domain.QStudyComment;
import com.codelap.common.studyView.domain.QStudyView;
import com.codelap.common.support.TechStack;
import com.codelap.common.user.domain.User;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQuery;
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
public class DefaultStudyQueryDslAppService extends DynamicCond implements StudyQueryAppService {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<GetStudiesStudyDto> getStudies(User user) {
        return null;
    }

    @Override
    public List<GetStudyInfo> getAttendedStudiesByUser(User user, String statusCond, List<TechStack> techStackList) {
        return getGetStudyInfoJPAQuery()
                .where(QStudy.study.members.contains(user))
                .where(QStudy.study.status.ne(DELETED))
                .where(checkStatus(statusCond))
                .where(techStackFilter(techStackList))
                .fetch();
    }

    @Override
    public List<GetStudyInfo> getBookmarkedStudiesByUser(List<Long> studyIds) {
        return getGetStudyInfoJPAQuery()
                .where(QStudy.study.id.in(studyIds))
                .where(QStudy.study.status.ne(DELETED))
                .fetch();
    }

    @Override
    public List<GetTechStackInfo> getTechStacks(List<Long> studyIds) {
        return queryFactory
                .select(
                        constructor(
                                GetTechStackInfo.class,
                                QStudy.study.id,
                                QStudyTechStack.studyTechStack.techStack
                        )
                )
                .from(QStudy.study)
                .innerJoin(QStudy.study.techStackList, QStudyTechStack.studyTechStack)
                .fetch();
    }

    @Override
    public List<GetOpenedStudiesDto> getOpenedStudies() {
        return null;
    }

    private JPAQuery<GetStudyInfo> getGetStudyInfoJPAQuery() {
        return queryFactory
                .selectDistinct(
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
                .innerJoin(QStudy.study.techStackList, QStudyTechStack.studyTechStack);
    }
}
