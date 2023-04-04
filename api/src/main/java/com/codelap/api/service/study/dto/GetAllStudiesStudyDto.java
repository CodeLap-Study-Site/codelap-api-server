package com.codelap.api.service.study.dto;

import com.codelap.common.study.domain.StudyPeriod;
import com.codelap.common.support.TechStack;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@ToString
public class GetAllStudiesStudyDto {
    private Long id;
    private String name;
    private StudyPeriod period;
    private String leader;
    private Long commentCount;
    private Long viewCount;
    private Long bookmarkCount;
    private int memberCount;
    private int maxMembersSize;
    private List<TechStack> techStackList;

    public GetAllStudiesStudyDto(Long id, String name, StudyPeriod period, String leader, Long commentCount, Long viewCount, Long bookmarkCount, int memberCount, int maxMembersSize, List<TechStack> techStackList) {
        this.id = id;
        this.name = name;
        this.period = period;
        this.leader = leader;
        this.commentCount = commentCount;
        this.viewCount = viewCount;
        this.bookmarkCount = bookmarkCount;
        this.memberCount = memberCount;
        this.maxMembersSize = maxMembersSize;
        this.techStackList = techStackList;
    }
}
