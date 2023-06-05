package com.codelap.common.bookmark.domain;

import com.codelap.common.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {
    List<Bookmark> findByUser(User user);
}
