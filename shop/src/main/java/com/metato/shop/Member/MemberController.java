package com.metato.shop.Member;


import com.metato.shop.Item.ItemService;
import com.metato.shop.sales.Sales;
import com.metato.shop.sales.SalesRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    @GetMapping("/register")
    public String signUp(){
        return "register.html";
    }

    @PostMapping("/member")
    public String member(
            String username, String password, String passwordCheck,
            String displayName, String email, String postcode,
            String roadAddress, String jibun, String detailAddress,
            String extraAddress
            ){
        memberService.saveMember(username, password, displayName, email, postcode, roadAddress, jibun, detailAddress, extraAddress);
        return "redirect:/";
    }

    @GetMapping("/login")
    public String login(){
        return "login.html";
    }

    @GetMapping("/my-page")
    public String myPage(Authentication auth, Model model){
        CustomUser result = (CustomUser) auth.getPrincipal();
        Optional<Member> tmp = memberService.findByUsername(result.getUsername());


        if(tmp.isPresent()){
            Member member = tmp.get();
            model.addAttribute("member", member);
        }

        return "mypage.html";
    }


    @PostMapping("/login/jwt")
    public ResponseEntity<Void> loginJWT(Model model,@RequestBody Map<String, String> data, HttpServletResponse response){

        try{
            var authToken = new UsernamePasswordAuthenticationToken(
                    data.get("username"), data.get("password")
            );
            var auth = authenticationManagerBuilder.getObject().authenticate(authToken);
            SecurityContextHolder.getContext().setAuthentication(auth);

            var jwt = JwtUtil.createToken(SecurityContextHolder.getContext().getAuthentication());
            System.out.println(jwt);

            var cookie = new Cookie("jwt", jwt);
            cookie.setMaxAge(1000);
            cookie.setHttpOnly(true);
            cookie.setPath("/");
            response.addCookie(cookie);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch(Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/logout_jwt")
    public ResponseEntity<Void> logout(HttpServletResponse response) {
        // JWT 쿠키 삭제

        Cookie cookie = new Cookie("jwt", null);
        cookie.setPath("/"); // 쿠키의 경로 설정
        cookie.setHttpOnly(true); // 클라이언트 측에서 접근할 수 없도록 설정
        cookie.setMaxAge(0); // 쿠키 만료 시간 설정
        response.addCookie(cookie);

        return new ResponseEntity<>(HttpStatus.OK);
    }


}

class MemberDto{
    // DTO 장점 1. 보내는 데이터의 타입체트가 쉽다. EX) 어떤 함수가 사용가능한가
    // 2. 재사용이 쉬움, 만약 위에서 ID도 보내준다면, 새로운 API를 만들어서 추가하는경우가 많다(해당 API에 의존하는
    // 다른 API가 존재하게된다면 에레거 일어날수있음.
    public String username;
    public String displayName;

    public Long id;
    MemberDto(String a, String b){
        this.username = a;
        this.displayName = b;
    }

    MemberDto(String a, String b, Long id){
        this.username = a;
        this.displayName = b;
        this.id = id;
    }

}