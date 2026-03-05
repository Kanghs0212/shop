package com.metato.shop;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import java.time.LocalDate;
import java.text.SimpleDateFormat;
import java.time.LocalTime;

@Controller
public class BasicController {

    @GetMapping("/date")
    @ResponseBody
    String date(){
        LocalDate now  = LocalDate.now();
        return now.toString();
    }

    @GetMapping("/about")
    @ResponseBody
    String about(){
        return "피싱사이트에요";
    }


}
