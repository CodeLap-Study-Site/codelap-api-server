package com.codelap.common.study.dto;

import com.codelap.common.study.domain.StudyPeriod;
import com.codelap.common.study.domain.TechStack;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class GetMyStudiesDto {
    private String studyName;
    private StudyPeriod studyPeriod;
    private String leaderName;
    private Long commentCount;
    private Long viewCount;
    private Long bookmarkCount;
    private int maxMemberSize;
    private List<TechStack> techStackList;

    public GetMyStudiesDto(String studyName, StudyPeriod studyPeriod, String leaderName, Long commentCount, Long viewCount, Long bookmarkCount, int maxMemberSize) {
        this.studyName = studyName;
        this.studyPeriod = studyPeriod;
        this.leaderName = leaderName;
        this.commentCount = commentCount;
        this.viewCount = viewCount;
        this.bookmarkCount = bookmarkCount;
        this.maxMemberSize = maxMemberSize;
    }
}
