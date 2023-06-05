package com.codelap.api.controller.study;

import com.codelap.api.controller.study.dto.GetMyStudiesDto.GetMyStudiesResponse;
import com.codelap.api.controller.study.dto.StudyCloseDto.StudyCloseRequest;
import com.codelap.api.controller.study.dto.StudyLeaveDto.StudyLeaveRequest;
import com.codelap.api.security.user.DefaultCodeLapUser;
import com.codelap.api.service.study.StudyAppService;
import com.codelap.common.study.service.StudyService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import static com.codelap.api.controller.study.cond.GetStudyCardsCond.GetStudyCardsParam;
import static com.codelap.api.controller.study.dto.StudyCreateDto.StudyCreateRequest;
import static com.codelap.api.controller.study.dto.StudyDeleteDto.StudyDeleteRequest;
import static com.codelap.api.controller.study.dto.StudyOpenDto.StudyOpenRequest;
import static com.codelap.api.controller.study.dto.StudyProceedDto.StudyProceedRequest;
import static com.codelap.api.controller.study.dto.StudyRemoveMemberDto.StudyRemoveMemberRequest;
import static com.codelap.api.controller.study.dto.StudyUpdateDto.StudyUpdateRequest;

@RestController
@RequestMapping("/study")
@RequiredArgsConstructor
public class StudyController {

    private final StudyService studyService;
    private final StudyAppService studyAppService;

    @PostMapping
    public void create(
            @AuthenticationPrincipal DefaultCodeLapUser user,
            @RequestBody StudyCreateRequest req
    ) {
        studyService.create(user.getId(), req.name(), req.info(), req.maxMembersSize(), req.difficulty(), req.period().toStudyPeriod(), req.career().toStudyNeedCareer(), req.techStackList());
    }

    @PostMapping("/image-upload/{studyId}")
    public void imageUpload(
            @RequestPart(value = "multipartFile") MultipartFile multipartFile,
            @PathVariable Long studyId,
            @AuthenticationPrincipal DefaultCodeLapUser leader
    ) {
        studyAppService.imageUpload(leader.getId(), studyId, multipartFile);
    }

    @PostMapping("/update")
    public void update(
            @RequestBody StudyUpdateRequest req,
            @AuthenticationPrincipal DefaultCodeLapUser leader
    ) {
        studyService.update(req.studyId(), leader.getId(), req.name(), req.info(), req.maxMembersSize(), req.difficulty(), req.period().toStudyPeriod(), req.career().toStudyNeedCareer(), req.techStackList());
    }

    @PostMapping("/proceed")
    public void proceed(
            @AuthenticationPrincipal DefaultCodeLapUser leader,
            @RequestBody StudyProceedRequest req
    ) {
        studyService.proceed(req.studyId(), leader.getId());
    }

    @PostMapping("/remove-member")
    public void removeMember(
            @AuthenticationPrincipal DefaultCodeLapUser leader,
            @RequestBody StudyRemoveMemberRequest req
    ) {
        studyService.removeMember(req.studyId(), req.memberId(), leader.getId());
    }

    @PostMapping("/close")
    public void close(
            @AuthenticationPrincipal DefaultCodeLapUser leader,
            @RequestBody StudyCloseRequest req
    ) {
        studyService.close(req.studyId(), leader.getId());
    }

    @PostMapping("/leave")
    public void leave(
            @AuthenticationPrincipal DefaultCodeLapUser member,
            @RequestBody StudyLeaveRequest req
    ) {
        studyService.leave(req.studyId(), member.getId());
    }

    @DeleteMapping
    public void delete(
            @AuthenticationPrincipal DefaultCodeLapUser leader,
            @RequestBody StudyDeleteRequest req
    ) {
        studyService.delete(req.studyId(), leader.getId());
    }

    @PostMapping("/open")
    public void open(
            @AuthenticationPrincipal DefaultCodeLapUser leader,
            @RequestBody StudyOpenRequest req
    ) {
        studyService.open(req.studyId(), leader.getId(), req.period().toStudyPeriod());
    }

    @GetMapping("/my-study")
    public GetMyStudiesResponse findStudyCardsByCond(
            GetStudyCardsParam param
    ) {
        return GetMyStudiesResponse.create(studyAppService.findStudyCardsByCond(param.userId(), param.statusCond(), param.techStackList()));
    }
}
