package com.codelap.api.controller.study;

import com.codelap.common.study.service.StudyService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.codelap.api.controller.study.dto.StudyCreateDto.StudyCreateRequest;
import static com.codelap.api.controller.study.dto.StudyUpdateDto.StudyUpdateRequest;
import static com.codelap.common.study.domain.StudyDifficulty.HARD;

@RestController
@RequestMapping("/study")
@RequiredArgsConstructor
public class StudyController {

    private final StudyService studyService;

    @PostMapping
    public void create(
            @RequestBody StudyCreateRequest req
    ) {
        studyService.create(req.leaderId(), req.name(), req.info(), req.maxMembersSize(), HARD, req.period().toStudyPeriod(), req.career().toStudyNeedCareer());
    }

    @PostMapping("/update")
    public void update(
            @RequestBody StudyUpdateRequest req
    ) {
        studyService.update(req.studyId(), req.leaderId(), req.name(), req.info(), req.maxMembersSize(), req.difficulty(), req.period().toStudyPeriod(), req.career().toStudyNeedCareer());
    }
}
