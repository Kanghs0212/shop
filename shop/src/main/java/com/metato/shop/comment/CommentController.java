package com.metato.shop.comment;

import com.metato.shop.Member.CustomUser;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    @PostMapping("/comment")
    public String comment(@RequestParam Long id,@RequestParam String commentInput, Authentication auth, Model model){
        CustomUser user = (CustomUser) auth.getPrincipal();

        commentService.saveComment(commentInput, user.getUsername(), id);
        
        return "redirect:/detail/"+id + "?pageNum=1";
    }
}
