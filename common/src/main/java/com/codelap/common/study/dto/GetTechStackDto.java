package com.codelap.common.study.dto;

import com.codelap.common.study.domain.TechStack;
import lombok.Data;

@Data
public class GetTechStackDto {
    private Long studyId;
    private TechStack techStackList;

    public GetTechStackDto(Long studyId, TechStack techStackList) {
        this.studyId = studyId;
        this.techStackList = techStackList;
    }
}
