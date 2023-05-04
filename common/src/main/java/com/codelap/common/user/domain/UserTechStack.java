package com.codelap.common.user.domain;

import com.codelap.common.support.TechStack;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import static jakarta.persistence.EnumType.STRING;
import static lombok.AccessLevel.PROTECTED;

@Getter
@Embeddable
@AllArgsConstructor
@NoArgsConstructor(access = PROTECTED)
@ToString
public class UserTechStack {

    @Enumerated(STRING)
    private TechStack techStack;

}
