package com.codelap.api.service.bookmark;

import com.codelap.common.bookmark.domain.Bookmark;
import com.codelap.common.bookmark.dto.GetBookmarkCardDto;
import com.codelap.common.support.TechStack;

import java.util.List;

public interface BookmarkAppService {

    List<GetBookmarkCardDto.GetBookmarkInfo> getBookmarkStudiesByUser(Long userId, String statusCond, List<Bookmark> bookmarks, List<TechStack> techStackList);
}
