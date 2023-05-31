package com.codelap.api.service.study;

import com.codelap.api.service.study.dto.GetStudiesDto.GetStudiesStudyDto;
import com.codelap.common.study.domain.StudyFile;
import com.codelap.common.study.dto.GetOpenedStudiesDto;
import com.codelap.common.study.dto.GetStudiesCardDto.GetStudyInfo;
import com.codelap.common.support.TechStack;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface StudyAppService {

    List<GetStudiesStudyDto> getStudies(Long userId);

    List<GetStudyInfo> findStudyCardsByCond(Long userId, String statusCond, List<TechStack> techStackList);

    List<GetOpenedStudiesDto> getOpenedStudies();

    StudyFile imageUpload(MultipartFile multipartFile);
}
