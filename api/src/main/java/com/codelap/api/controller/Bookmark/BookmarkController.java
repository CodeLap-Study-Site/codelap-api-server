package com.codelap.api.controller.Bookmark;

import com.codelap.common.bookmark.service.BookmarkService;
import lombok.RequiredArgsConstructor;
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
            @RequestBody BookmarkCreateRequest req
    ) {
        bookmarkService.create(req.studyId(), req.userId());
    }

    @DeleteMapping
    public void delete(
            @RequestBody BookmarkDeleteRequest req
    ) {
        bookmarkService.delete(req.bookmarkId(), req.userId());
    }
}
