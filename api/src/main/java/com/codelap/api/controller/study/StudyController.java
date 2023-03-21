package com.codelap.api.controller.study;

import com.codelap.api.controller.study.dto.StudyCloseDto.StudyCloseRequest;
import com.codelap.api.controller.study.dto.StudyDeleteDto;
import com.codelap.api.controller.study.dto.StudyLeaveDto.StudyLeaveRequest;
import com.codelap.common.study.service.StudyService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import static com.codelap.api.controller.study.dto.StudyCreateDto.StudyCreateRequest;
import static com.codelap.api.controller.study.dto.StudyDeleteDto.*;
import static com.codelap.api.controller.study.dto.StudyProceedDto.StudyProceedRequest;
import static com.codelap.api.controller.study.dto.StudyRemoveMemberDto.StudyRemoveMemberRequest;
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

    @PostMapping("/proceed")
    public void proceed(
            @RequestBody StudyProceedRequest req
    ) {
        studyService.proceed(req.studyId(), req.leaderId());
    }

    @PostMapping("/remove-member")
    public void removeMember(
            @RequestBody StudyRemoveMemberRequest req
    ) {
        studyService.removeMember(req.studyId(), req.memberId(), req.leaderId());
    }

    @PostMapping("/close")
    public void close(
            @RequestBody StudyCloseRequest req
    ) {
        studyService.close(req.studyId(), req.leaderId());
    }

    @PostMapping("/leave")
    public void leave(
            @RequestBody StudyLeaveRequest req
    ) {
        studyService.leave(req.studyId(), req.memberId());
    }

    @DeleteMapping("/delete")
    public void delete(
            @RequestBody StudyDeleteRequest req
    ){
        studyService.delete(req.studyId(), req.leaderId());
    }
}
