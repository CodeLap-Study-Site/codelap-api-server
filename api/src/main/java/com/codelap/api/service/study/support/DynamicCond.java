package com.codelap.api.service.study.support;

import com.codelap.common.study.domain.QStudy;
import com.codelap.common.study.domain.QStudyTechStack;
import com.codelap.common.support.TechStack;
import com.querydsl.core.types.dsl.BooleanExpression;

import java.util.List;

import static com.codelap.common.study.domain.StudyStatus.CLOSED;
import static com.codelap.common.study.domain.StudyStatus.OPENED;

public class DynamicCond {
    protected static BooleanExpression checkStatus(String statusCond) {
        return (statusCond.equals("open"))
                ? QStudy.study.status.in(OPENED)
                : QStudy.study.status.in(CLOSED, OPENED);
    }

    protected static BooleanExpression techStackFilter(List<TechStack> techStackList) {
        return techStackList != null ? QStudyTechStack.studyTechStack.techStack.in(techStackList) : null;
    }
}
