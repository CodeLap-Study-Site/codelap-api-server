package com.codelap.api.controller.Bookmark.dto;

public class BookmarkDeleteDto {
    public record BookmarkDeleteRequest(
            Long bookmarkId,
            Long userId
    ) {
    }
}
