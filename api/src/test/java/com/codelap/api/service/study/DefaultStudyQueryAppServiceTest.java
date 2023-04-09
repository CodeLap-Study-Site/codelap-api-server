package com.codelap.api.service.study;

import com.codelap.common.study.domain.StudyRepository;
import com.codelap.common.study.domain.TechStack;
import com.codelap.common.user.domain.User;
import com.codelap.common.user.domain.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
@Transactional
class DefaultStudyQueryAppServiceTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    StudyRepository studyRepository;

    @Autowired
    StudyQueryAppService studyQueryAppService;

    private User leader;
    private List<TechStack> techStackList;

}