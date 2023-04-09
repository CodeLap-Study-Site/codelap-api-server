package com.codelap.api.service.study;

import com.codelap.common.study.domain.StudyRepository;
import com.codelap.common.study.domain.TechStack;
import com.codelap.common.user.domain.User;
import com.codelap.common.user.domain.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@SpringBootTest
class DefaultStudyAppServiceTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    StudyRepository studyRepository;

    @Autowired
    StudyAppService studyAppService;

    private User leader;
    private List<TechStack> techStackList;
}