package com.codelap.common.study.dto;

import com.codelap.common.study.domain.StudyPeriod;
import com.codelap.common.study.domain.TechStack;
import lombok.Data;

import java.util.List;

public class GetStudiesCardDto {
    @Data
    public static class GetStudyInfo {
        private Long studyId;
        private String studyName;
        private StudyPeriod studyPeriod;
        private String leaderName;
        private Long commentCount;
        private Long viewCount;
        private Long bookmarkCount;
        private int maxMemberSize;
        private List<GetTechStackInfo> techStackList;

        public GetStudyInfo(Long studyId, String studyName, StudyPeriod studyPeriod, String leaderName, Long commentCount, Long viewCount, Long bookmarkCount, int maxMemberSize) {
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

    @Data
    public static class GetTechStackInfo {
        private Long studyId;
        private TechStack techStack;

        public GetTechStackInfo(Long studyId, TechStack techStack) {
            this.studyId = studyId;
            this.techStack = techStack;
        }
    }
}