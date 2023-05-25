package com.codelap.api.service.bookmark;

import com.codelap.common.bookmark.domain.Bookmark;
import com.codelap.common.bookmark.dto.GetBookmarkCardDto;
import com.codelap.common.study.dto.GetStudiesCardDto;
import com.codelap.common.support.TechStack;
import com.codelap.common.user.domain.User;
import com.codelap.common.user.domain.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.codelap.common.bookmark.dto.GetBookmarkCardDto.*;

@Service
@Transactional
@RequiredArgsConstructor
public class DefaultBookmarkAppService implements BookmarkAppService {

    private final BookmarkQueryAppService bookmarkQueryDslAppService;

    private final UserRepository userRepository;


    @Override
    public List<GetBookmarkInfo> getBookmarkStudiesByUser(Long userId, String statusCond, List<Bookmark> bookmarks, List<TechStack> techStackList) {
        User user = userRepository.findById(userId).orElseThrow();

        List<GetBookmarkInfo> allStudies = bookmarkQueryDslAppService.getBookmarkStudiesByUser(user, statusCond, bookmarks, techStackList);

        Map<Long, List<GetTechStackInfo>> techStacksMap = bookmarkQueryDslAppService.getTechStacks(toStudyIds(allStudies))
                .stream()
                .collect(Collectors
                        .groupingBy(GetTechStackInfo::getStudyId));

        allStudies.forEach(study -> study.setTechStackList(techStacksMap.get(study.getStudyId())));

        return allStudies;
    }

    private static List<Long> toStudyIds(List<GetBookmarkInfo> allStudies) {
        return allStudies
                .stream()
                .map(GetBookmarkInfo::getStudyId)
                .collect(Collectors.toList());
    }

}
