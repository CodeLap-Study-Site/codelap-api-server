package com.codelap.api.controller.study.cond;

import com.codelap.common.support.TechStack;

import java.util.List;

public class GetStudyCardsCond {
    public record GetStudyCardsParam (
            Long userId,
            String statusCond,
            List<TechStack> techStackList
    ) {
    }
}
