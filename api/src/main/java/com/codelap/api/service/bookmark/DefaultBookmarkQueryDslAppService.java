package com.codelap.api.service.bookmark;

import com.codelap.api.service.bookmark.support.DynamicCond;
import com.codelap.common.bookmark.domain.Bookmark;
import com.codelap.common.bookmark.domain.BookmarkRepository;
import com.codelap.common.bookmark.domain.QBookmark;
import com.codelap.common.bookmark.dto.GetBookmarkCardDto;
import com.codelap.common.study.domain.QStudy;
import com.codelap.common.study.domain.QStudyTechStack;
import com.codelap.common.study.domain.Study;
import com.codelap.common.study.domain.StudyRepository;
import com.codelap.common.study.dto.GetStudiesCardDto;
import com.codelap.common.studyComment.domain.QStudyComment;
import com.codelap.common.studyView.domain.QStudyView;
import com.codelap.common.support.TechStack;
import com.codelap.common.user.domain.User;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.codelap.common.bookmark.dto.GetBookmarkCardDto.*;
import static com.querydsl.core.types.ExpressionUtils.count;
import static com.querydsl.core.types.Projections.constructor;

@Repository
@RequiredArgsConstructor
public class DefaultBookmarkQueryDslAppService extends DynamicCond implements  BookmarkQueryAppService {

    private final JPAQueryFactory queryFactory;

    private final BookmarkRepository bookmarkRepository;

    private final StudyRepository studyRepository;

    @Override
    public List<GetBookmarkInfo> getBookmarkStudiesByUser(User userCond, String statusCond, List<Bookmark> bookmarks, List<TechStack> techStackList) {
        return queryFactory
                .selectDistinct(
                        constructor(
                                GetBookmarkInfo.class,
                                QStudy.study.id,
                                QStudy.study.name,
                                QStudy.study.period,
                                QStudy.study.leader.name,
                                ExpressionUtils.as(JPAExpressions
                                                 .select(count(QStudyComment.studyComment.id))
                                                 .from(QStudyComment.studyComment)
                                                 .where(QStudyComment.studyComment.id.eq(QStudy.study.id)),
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
                .innerJoin(QStudy.study.bookmarks, QBookmark.bookmark)
                .where(QBookmark.bookmark.user.id.eq(userCond.getId()))
                .where(checkStatus(statusCond))
                .where(techStackFilter(techStackList))
                .fetch();
    }

    @Override
    public List<GetBookmarkCardDto.GetTechStackInfo> getTechStacks(List<Long> studyIds) {
        return bookmarkRepository.getTechStacks(studyIds);
    }

}
