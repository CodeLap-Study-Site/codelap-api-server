package com.codelap.api.controller.Bookmark;

import com.codelap.api.security.user.DefaultCodeLapUser;
import com.codelap.common.bookmark.service.BookmarkService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import static com.codelap.api.controller.Bookmark.dto.BookmarkCreateDto.BookmarkCreateRequest;
import static com.codelap.api.controller.Bookmark.dto.BookmarkDeleteDto.BookmarkDeleteRequest;

@RestController
@RequestMapping("/bookmark")
@RequiredArgsConstructor
public class BookmarkController {

    private final BookmarkService bookmarkService;

    @PostMapping
    public void create(
            @AuthenticationPrincipal DefaultCodeLapUser user,
            @RequestBody BookmarkCreateRequest req
    ) {
        bookmarkService.create(req.studyId(), user.getId());
    }

    @DeleteMapping
    public void delete(
            @AuthenticationPrincipal DefaultCodeLapUser user,
            @RequestBody BookmarkDeleteRequest req
    ) {
        bookmarkService.delete(req.bookmarkId(), user.getId());
    }
}
