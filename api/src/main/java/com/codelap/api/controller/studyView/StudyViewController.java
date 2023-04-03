package com.codelap.api.controller.studyView;

import com.codelap.common.studyView.service.StudyViewService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.codelap.api.controller.studyView.dto.StudyViewCreateDto.StudyViewCreateRequest;


@RestController
@RequestMapping("study-view")
@RequiredArgsConstructor
public class StudyViewController {
    private final StudyViewService studyViewService;

    @PostMapping
    public void create(
            HttpServletRequest request,
            @RequestBody StudyViewCreateRequest dto
    ) {
        studyViewService.create(dto.studyId(), request.getRemoteAddr());
    }
}
