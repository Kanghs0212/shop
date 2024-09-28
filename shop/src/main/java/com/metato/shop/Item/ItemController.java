package com.metato.shop.Item;

import com.metato.shop.comment.Comment;
import com.metato.shop.comment.CommentRepository;
import com.metato.shop.comment.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
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

    /*
    또는 lombok을 사용하지 않을려면 아래 명령어를 사용해야한다.
    방법은 우클릭-생성자-방금 만든 리포시토리를 선택하면 생성된다
    이후 위에 @Autowired를 붙이면 끝
    뜻은 스프링한테 Itemrepository에서 오브젝트를 하나뽑아와
     this변수에 넣어달라는뜻이다.

    @Autowired
    public ItemController(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }
    * */
//    @GetMapping("/list")
//    String list(Model model){
//        List<Item> result = itemService.findAllItem();
//        model.addAttribute("items",result);
//
//        return "list.html";
//    }

    @GetMapping("/write")
    String write(){

        return "write.html";
    }

    @PostMapping("/add")
    String addPost(String title, Integer price,String text ,String imgURL, Authentication auth){
        itemService.saveItem(title, price, text, imgURL, auth);
        return "redirect:/list/page/1";
    }

    @GetMapping("/detail/{id}")
    String detail(Model model, @PathVariable Long id, @RequestParam Integer pageNum){

        Optional<Item> result = itemService.findById(id);
        // Optional은 비어있을수도있고, Item 오브젝트가 들어있을수도있다를 말함
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
        // Optional은 비어있을수도있고, Item 오브젝트가 들어있을수도있다를 말함

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

    @GetMapping("/presigned-url")
    @ResponseBody
    String getURL(@RequestParam String filename){
        var result = s3Service.createPresignedUrl("test/"+filename);

        return result;
    }

    @PostMapping("/search")
    String postSearch(Model model,@RequestParam String searchText){

        List<Item> result  = itemService.searchText(searchText);
        model.addAttribute("items", result);
        model.addAttribute("cnt", 0);
        return "list.html";
    }


}
