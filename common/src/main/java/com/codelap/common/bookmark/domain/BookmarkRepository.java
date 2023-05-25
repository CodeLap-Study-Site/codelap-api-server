package com.codelap.common.bookmark.domain;

import com.codelap.common.bookmark.dto.GetBookmarkCardDto;
import com.codelap.common.study.dto.GetStudiesCardDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {

    @Query("SELECT new com.codelap.common.bookmark.dto.GetBookmarkCardDto.GetTechStackInfo(s.id, t.techStack) FROM Study s JOIN s.techStackList t WHERE s in :studyIds")
    List<GetBookmarkCardDto.GetTechStackInfo> getTechStacks(@Param("studyIds") List<Long> studyIds);

}
