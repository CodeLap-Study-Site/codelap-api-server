package com.codelap.api.service.study;

import com.codelap.api.service.study.dto.GetStudiesDto;
import com.codelap.api.service.study.support.DynamicCond;
import com.codelap.common.bookmark.domain.QBookmark;
import com.codelap.common.study.domain.QStudy;
import com.codelap.common.study.domain.QStudyTechStack;
import com.codelap.common.study.domain.StudyRepository;
import com.codelap.common.study.domain.TechStack;
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

import static com.codelap.common.study.dto.GetStudiesCardDto.GetStudyInfo;
import static com.codelap.common.study.dto.GetStudiesCardDto.GetTechStackInfo;
import static com.querydsl.core.types.ExpressionUtils.count;
import static com.querydsl.core.types.Projections.constructor;

@Repository
@Primary
@RequiredArgsConstructor
public class DefaultStudyQueryDslAppService extends DynamicCond implements StudyQueryAppService {

    private final JPAQueryFactory queryFactory;

    private final StudyRepository studyRepository;
    @Override
    public List<GetStudiesDto.GetStudiesStudyDto> getStudies(User user) {
        return null;
    }
    @Override
    public List<GetStudyInfo> getAttendedStudiesByUser(User userCond, String statusCond, List<TechStack> techStackList) {
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
                .innerJoin(QStudy.study.techStackList, QStudyTechStack.studyTechStack)
                .where(checkStatus(statusCond))
                .where(userContains(userCond))
                .where(techStackFilter(techStackList))
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
