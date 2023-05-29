package com.codelap.api.controller.study.cond;

import com.codelap.common.support.TechStack;
import jakarta.annotation.Nullable;

import java.util.List;

public class GetStudyCardsCond {
    public record GetStudyCardsRequest (
            Long userId,
            String statusCond,
            List<TechStack> techStackList
    ) {
    }
}
