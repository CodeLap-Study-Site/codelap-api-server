package com.codelap.api.service.bookmark;

import com.codelap.common.bookmark.domain.Bookmark;
import com.codelap.common.bookmark.dto.GetBookmarkCardDto;
import com.codelap.common.study.dto.GetStudiesCardDto;
import com.codelap.common.support.TechStack;
import com.codelap.common.user.domain.User;

import java.util.List;
import java.util.Map;

import static com.codelap.common.bookmark.dto.GetBookmarkCardDto.*;

public interface BookmarkQueryAppService {

    List<GetBookmarkInfo> getBookmarkStudiesByUser(User usercond, String statusCond, List<Bookmark> bookmarks, List<TechStack> techStackList);

    List<GetBookmarkCardDto.GetTechStackInfo> getTechStacks(List<Long> studyIds);
}

