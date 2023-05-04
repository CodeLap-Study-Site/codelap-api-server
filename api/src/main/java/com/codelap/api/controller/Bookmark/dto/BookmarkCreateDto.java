package com.codelap.api.controller.Bookmark.dto;

public class BookmarkCreateDto {
    public record BookmarkCreateRequest(
            Long studyId
    ) {
    }
}
