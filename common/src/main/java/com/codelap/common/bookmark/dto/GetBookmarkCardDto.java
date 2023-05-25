package com.codelap.common.bookmark.dto;

import com.codelap.common.study.domain.StudyPeriod;
import com.codelap.common.study.dto.GetStudiesCardDto;
import com.codelap.common.support.TechStack;
import lombok.Data;

import java.util.List;

public class GetBookmarkCardDto {

    @Data
    public static class GetBookmarkInfo{
        private  Long studyId;
        private String  studyName;
        private StudyPeriod studyPeriod;
        private String leaderName;
        private Long commentCount;
        private Long viewCount;
        private Long bookmarkCount;
        private int maxMemberSize;
        private List<GetTechStackInfo> techStackList;

        public GetBookmarkInfo(Long studyId, String studyName, StudyPeriod studyPeriod, String leaderName, Long commentCount, Long viewCount, Long bookmarkCount, int maxMemberSize, List<GetTechStackInfo> techStackList) {
            this.studyId = studyId;
            this.studyName = studyName;
            this.studyPeriod = studyPeriod;
            this.leaderName = leaderName;
            this.commentCount = commentCount;
            this.viewCount = viewCount;
            this.bookmarkCount = bookmarkCount;
            this.maxMemberSize = maxMemberSize;
            this.techStackList = techStackList;
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
