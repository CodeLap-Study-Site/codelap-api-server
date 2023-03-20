package com.codelap.common.support;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    ACTOR_VALIDATE("error.actor.validate"),
    INVALID_MEMBER_SIZE("error.member.size.validate");

    private final String message;
}