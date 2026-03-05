package com.metato.shop.comment;


import com.metato.shop.Item.Item;
import com.metato.shop.Member.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    Page<Comment> findPageByParentId(Long parentId, Pageable page);
    Page<Comment> findPageBy(Pageable page);
}
