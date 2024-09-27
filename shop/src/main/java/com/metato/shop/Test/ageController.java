package com.metato.shop.Test;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class ageController {


    @GetMapping("/age")
    @ResponseBody
    String tests(){
        var a = new age();
        a.setAge(60);
        a.setName("홍길동");
        System.out.println(a.getAge());
        System.out.println(a.getName());
        a.나이설정(-10);
        System.out.println(a.getAge());
        a.한살더하기();
        System.out.println(a.getAge());
        return "hello";
    }
}
