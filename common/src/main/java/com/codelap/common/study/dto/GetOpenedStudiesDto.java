package com.codelap.common.study.dto;

import com.codelap.common.study.domain.StudyPeriod;

import java.util.List;

public class GetOpenedStudiesDto {
    private Long studyId;
    private String studyName;
    private StudyPeriod studyPeriod;
    private String leaderName;
    private Long commentCount;
    private Long viewCount;
    private Long bookmarkCount;
    private int maxMemberSize;
    private List<GetStudiesCardDto.GetTechStackInfo> techStackList;

    public GetOpenedStudiesDto(Long studyId, String studyName, StudyPeriod studyPeriod, String leaderName, Long commentCount, Long viewCount, Long bookmarkCount, int maxMemberSize) {
        this.studyId = studyId;
        this.studyName = studyName;
        this.studyPeriod = studyPeriod;
        this.leaderName = leaderName;
        this.commentCount = commentCount;
        this.viewCount = viewCount;
        this.bookmarkCount = bookmarkCount;
        this.maxMemberSize = maxMemberSize;
    }
}
