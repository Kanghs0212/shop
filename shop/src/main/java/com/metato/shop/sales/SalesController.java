package com.metato.shop.sales;

import com.metato.shop.Member.CustomUser;
import com.metato.shop.Member.Member;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class SalesController {

    private final SalesService salesService;
    private final CartItemRepository cartItemRepository;

    @PostMapping("/order")
    String order(@RequestParam String title, @RequestParam Integer price, @RequestParam Long id, @RequestParam Integer orderCount, Authentication auth){
        if(auth == null) return "redirect:/login";
        CustomUser customUser = (CustomUser) auth.getPrincipal();
        salesService.saveSales(title, price, orderCount, customUser.id);
        return "redirect:/order/complete";
    }

    @GetMapping("/order/complete")
    String orderComplete(){
        return "orderComplete.html";
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
            salesDtoList.add(salesDto);
        }

        if(!result.isEmpty()){
            model.addAttribute("orders", salesDtoList);
        }

        return "sales.html";
    }

    @GetMapping("/order/my")
    String getMyOrders(Model model, Authentication auth){
        if(auth == null) return "redirect:/login";
        CustomUser customUser = (CustomUser) auth.getPrincipal();
        List<Sales> result = salesService.findByMemberId(customUser.id);

        List<SalesDto> salesDtoList = new ArrayList<>();
        for (Sales sale : result) {
            SalesDto salesDto = new SalesDto();
            salesDto.itemName = sale.getItemName();
            salesDto.price = sale.getPrice();
            salesDto.count = sale.getCount();
            salesDto.username = sale.getMember().getUsername();
            salesDto.created = sale.getCreated();
            salesDtoList.add(salesDto);
        }

        model.addAttribute("orders", salesDtoList);
        return "myOrders.html";
    }

    @PostMapping("/cart")
    String addToCart(@RequestParam String title, @RequestParam Integer price, @RequestParam Long id, @RequestParam Integer orderCount, Authentication auth){
        if(auth == null) return "redirect:/login";
        CustomUser customUser = (CustomUser) auth.getPrincipal();
        CartItem cartItem = new CartItem();
        cartItem.setItemId(id);
        cartItem.setTitle(title);
        cartItem.setPrice(price);
        cartItem.setCount(orderCount);
        Member member = new Member();
        member.setId(customUser.id);
        cartItem.setMember(member);
        cartItemRepository.save(cartItem);
        return "redirect:/cart";
    }

    @GetMapping("/cart")
    String viewCart(Model model, Authentication auth){
        if(auth == null) return "redirect:/login";
        CustomUser customUser = (CustomUser) auth.getPrincipal();
        List<CartItem> cartItems = cartItemRepository.findByMemberId(customUser.id);
        int totalPrice = 0;
        for(CartItem item : cartItems){
            totalPrice += item.getPrice() * item.getCount();
        }
        model.addAttribute("cartItems", cartItems);
        model.addAttribute("totalPrice", totalPrice);
        return "cart.html";
    }

    @PostMapping("/cart/remove")
    String removeFromCart(@RequestParam Long cartItemId, Authentication auth){
        if(auth == null) return "redirect:/login";
        CustomUser customUser = (CustomUser) auth.getPrincipal();
        cartItemRepository.deleteByIdAndMemberId(cartItemId, customUser.id);
        return "redirect:/cart";
    }

    @PostMapping("/cart/order")
    String orderFromCart(Authentication auth){
        if(auth == null) return "redirect:/login";
        CustomUser customUser = (CustomUser) auth.getPrincipal();
        List<CartItem> cartItems = cartItemRepository.findByMemberId(customUser.id);
        if(!cartItems.isEmpty()){
            for(CartItem item : cartItems){
                salesService.saveSales(item.getTitle(), item.getPrice(), item.getCount(), customUser.id);
            }
            cartItemRepository.deleteAll(cartItems);
        }
        return "redirect:/order/complete";
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
