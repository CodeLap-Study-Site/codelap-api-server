package com.codelap.api.controller.study.cond;

import com.codelap.common.support.TechStack;

import java.util.List;

public class GetBookmarkStudyCardsCond {
    public record GetBookmarkStudyCardsParam (
            Long userId
    ) {
    }
}
