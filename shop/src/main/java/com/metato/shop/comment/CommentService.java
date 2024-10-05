package com.metato.shop.comment;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;

    public Page<Comment> findPageByParentId(Long id, Integer pageNum){
        Page<Comment> result = commentRepository.findPageByParentId(id, PageRequest.of(pageNum-1, 5));
        return result;
    }

    public void saveComment(String commentInput, String username, Long id, int rating){
        Comment comment = new Comment();
        comment.setContent(commentInput);
        comment.setUsername(username);
        comment.setParentId(id);
        comment.setRating(rating);
        commentRepository.save(comment);
    }
}
