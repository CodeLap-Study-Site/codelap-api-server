package com.codelap.common.support;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    ACTOR_VALIDATE("error.actor.validate"),
    INVALID_MEMBER_SIZE("error.member.size.validate"),
    NOT_EXISTING_MEMBER("error.member.not.existing"),
    EXISTING_MEMBER("error.member.existing"),
    IS_LEADER("error.member.is.leader");

    private final String message;
}
