package com.metato.shop.sales;

import com.metato.shop.Member.CustomUser;
import com.metato.shop.Member.Member;
import com.metato.shop.Member.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import software.amazon.awssdk.services.s3.endpoints.internal.Value;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class SalesController {

    private final SalesService salesService;

    @PostMapping("/order")
    String order(@RequestParam String title, @RequestParam Integer price ,@RequestParam Long id, @RequestParam Integer orderCount, Authentication auth){
        CustomUser customUser = (CustomUser) auth.getPrincipal();

        salesService.saveSales(title, price, orderCount, customUser.id);

        return "redirect:/order/all";
    }



    @GetMapping("/order/all")
    String getOrderAll(Model model){
        List<Sales> result = salesService.customFindAll();

        List<SalesDto> salesDtoList = new ArrayList<>();
        for (Sales sale : result) {
            SalesDto salesDto = new SalesDto();
            salesDto.itemName = sale.getItemName();
            salesDto.price = sale.getPrice();
            salesDto.count = sale.getCount();
            salesDto.username = sale.getMember().getUsername();
            salesDto.created = sale.getCreated();

            // 리스트에 추가
            salesDtoList.add(salesDto);
        }

        if(!result.isEmpty()){
            model.addAttribute("orders", salesDtoList);
        }

        return "sales.html";
    }
}

@ToString
class SalesDto{
    public String itemName;
    public Integer price;
    public Integer count;

    public String username;
    public LocalDateTime created;

}