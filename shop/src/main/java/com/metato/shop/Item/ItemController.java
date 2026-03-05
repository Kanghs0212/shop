package com.metato.shop.Item;

import com.metato.shop.comment.Comment;
import com.metato.shop.comment.CommentRepository;
import com.metato.shop.comment.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;


@Controller
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;
    private final S3Service s3Service;
    private final CommentService commentService;


    @GetMapping("/")
    String hello(Model model){

        Page<Item> result = itemService.findPageThree();
        model.addAttribute("items", result);
        return "index.html";
    }

    @GetMapping("/write")
    String write(){

        return "write.html";
    }

    @PostMapping("/add")
    String addPost(String title, Integer price, String text, String category,
                   @RequestParam(value = "imgFile", required = false) MultipartFile imgFile,
                   Authentication auth){
        String imgURL = "";
        if (imgFile != null && !imgFile.isEmpty()) {
            try {
                imgURL = s3Service.saveFile(imgFile);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        itemService.saveItem(title, price, text, imgURL, category, auth);
        return "redirect:/list/page/1";
    }

    @GetMapping("/detail/{id}")
    String detail(Model model, @PathVariable Long id, @RequestParam(defaultValue = "1") Integer pageNum){

        Optional<Item> result = itemService.findById(id);
        Page<Comment> texts = commentService.findPageByParentId(id,pageNum);


        if(result.isPresent()){
            model.addAttribute("item", result.get());
            if(!texts.isEmpty()){
                model.addAttribute("texts", texts);
            }

            model.addAttribute("cnt", texts.getTotalPages());
            model.addAttribute("cnt_now", pageNum);

            return "detail.html";
        } else{
            return "redirect:/list/page/1";
        }
    }

    @GetMapping("/detail/{id}/update")
    String detailUpdate(Model model, @PathVariable Long id){

        Optional<Item> result = itemService.findById(id);

        if(result.isPresent()){
            model.addAttribute("item", result.get());
            return "detailUpdate.html";
        } else{
            return "redirect:/list/page/1";
        }
    }

    @PostMapping("/update")
    String updatePost(String title, Integer price, Long id, Authentication auth){
        itemService.updateItem(title, price, id, auth);
        return "redirect:/list/page/1";
    }

    @DeleteMapping("/delete")
    ResponseEntity<String> deletePost(@RequestParam Long id){
        itemService.deleteItem(id);
        return ResponseEntity.status(200).body("삭제 완료");
    }

    @GetMapping("/list/page/{id}")
    String getListPage(Model model, @PathVariable Integer id){
        Page<Item> result = itemService.findPageBy(id);
        model.addAttribute("items",result);
        model.addAttribute("cnt", result.getTotalPages());
        model.addAttribute("cnt_now", id);
        return "list.html";
    }

    @GetMapping("/list/category/{category}/page/{id}")
    String getListByCategory(Model model, @PathVariable String category, @PathVariable Integer id){
        Page<Item> result = itemService.findPageByCategory(category, id);
        model.addAttribute("items", result);
        model.addAttribute("cnt", result.getTotalPages());
        model.addAttribute("cnt_now", id);
        model.addAttribute("category", category);
        return "list.html";
    }

    @PostMapping("/search")
    String postSearch(Model model,@RequestParam String searchText){

        List<Item> result  = itemService.searchText(searchText);
        model.addAttribute("items", result);
        model.addAttribute("cnt", 0);
        return "list.html";
    }


}
