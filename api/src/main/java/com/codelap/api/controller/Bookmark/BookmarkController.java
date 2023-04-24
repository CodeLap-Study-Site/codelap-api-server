package com.codelap.api.controller.Bookmark;

import com.codelap.common.bookmark.service.BookmarkService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.codelap.api.controller.Bookmark.dto.BoomarkCreateDto.BoomarkCreateRequest;

@RestController
@RequestMapping("/bookmark")
@RequiredArgsConstructor
public class BookmarkController {

    private final BookmarkService bookmarkService;

    @PostMapping
    public void create(
            @RequestBody BoomarkCreateRequest req
    ){
        bookmarkService.create(req.studyId(), req.userId());
    }
}
