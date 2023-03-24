package com.codelap.api.service.user;

import com.codelap.common.study.domain.Study;

import java.util.List;

public interface UserAppService {
    List<Study> findStudyListByUserId(Long userId);
}
