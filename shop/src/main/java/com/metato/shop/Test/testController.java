package com.metato.shop.Test;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class testController {

    private final testRepository testrepository;

    @GetMapping("/test")
    String test(Model model){
        var a = testrepository.findAll();

        model.addAttribute("tests", a);
        return "test.html";
    }
}
