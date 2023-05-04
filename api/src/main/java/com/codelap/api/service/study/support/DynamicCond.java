package com.codelap.api.service.study.support;

import com.codelap.common.study.domain.QStudy;
import com.codelap.common.study.domain.QStudyTechStack;
import com.codelap.common.study.domain.TechStack;
import com.codelap.common.user.domain.User;
import com.querydsl.core.types.dsl.BooleanExpression;

import java.util.List;
import java.util.Optional;

import static com.codelap.common.study.domain.StudyStatus.DELETED;
import static com.codelap.common.study.domain.StudyStatus.OPENED;

public class DynamicCond {
    protected static BooleanExpression userContains(User userCond) {
        return Optional.ofNullable(userCond)
                .map(QStudy.study.members::contains)
                .orElse(null);
    }

    protected static BooleanExpression checkStatus(String statusCond) {
        return (statusCond.equals("open"))
                ? QStudy.study.status.eq(OPENED)
                : QStudy.study.status.notIn(DELETED);
    }

    protected static BooleanExpression techStackFilter(List<TechStack> techStackList) {
        return techStackList != null ? QStudyTechStack.studyTechStack.techStack.in(techStackList) : null;
    }
}
