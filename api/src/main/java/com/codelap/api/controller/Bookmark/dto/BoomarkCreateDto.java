package com.codelap.api.controller.Bookmark.dto;

public class BoomarkCreateDto {
    public record BoomarkCreateRequest(
            Long studyId,
            Long userId
    ) {
    }
}
