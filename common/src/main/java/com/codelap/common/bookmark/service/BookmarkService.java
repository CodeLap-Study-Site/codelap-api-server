package com.codelap.common.bookmark.service;

public interface BookmarkService {

    void create(Long studyId, Long userId);

    void delete(Long bookmarkId, Long userId);
}
